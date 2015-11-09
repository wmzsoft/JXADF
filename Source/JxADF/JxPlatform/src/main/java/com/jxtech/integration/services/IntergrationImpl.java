package com.jxtech.integration.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.integration.jsonvo.IntergrationVo;
import com.jxtech.integration.jsonvo.JboVo;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public class IntergrationImpl implements Intergration {
    private static Logger LOG = LoggerFactory.getLogger(IntergrationImpl.class);
    @SuppressWarnings("rawtypes")
    private static Map<String, Class> CLASS_MAP = new HashMap<String, Class>();

    static {
        CLASS_MAP.put("_jbos", JboVo.class);
        CLASS_MAP.put("_datas", HashMap.class);
        CLASS_MAP.put("_childrens", JboVo.class);
    }

    /**
     * 将传入的JSON数据持久化
     * 
     * @param jsonData
     * @return
     * @throws JxException
     */
    public boolean execute(String jsonData) throws JxException {
        if (StrUtil.isNull(jsonData)) {
            return false;
        }
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonData);
            LOG.debug("json:\r\n" + jsonData);
            IntergrationVo ivo = (IntergrationVo) JSONObject.toBean(jsonObject, IntergrationVo.class, CLASS_MAP);
            if (null != ivo) {
                // 首先获取数据中的jboname，如果jboname为空则根据appnametype获取对应的app，然后获取app的jboname
                String jboName = ivo.get_jboname();
                if (StrUtil.isNull(jboName)) {
                    String appNameType = ivo.get_appNameType();
                    if (!StrUtil.isNull(appNameType)) {
                        App app = JxSession.getApp(appNameType);
                        if (null != app) {
                            jboName = app.getJboset().getAppname();
                        }
                    }
                }

                if (!StrUtil.isNull(jboName)) {
                    JboSetIFace jboSet = JboUtil.getJboSet(jboName);// 主表的JboSet
                    List<JboVo> vos = ivo.get_jbos();// 得到主对象的Jbo列表
                    int size = vos.size();
                    for (int i = 0; i < size; i++) {
                        JboVo jboVo = vos.get(i);// 主对象JboVo
                        JboIFace jbo = convertJboFromVo(jboVo, jboSet);// 得到主对象的Jbo
                        if (null != jbo) {
                            List<JboVo> children = jboVo.get_childrens();// 得到子Jbo
                            if (null != children && !children.isEmpty()) {
                                int csize = children.size();
                                for (int j = 0; j < csize; j++) {
                                    handleChildJboVo(jbo, children.get(j));// 处理子Jbo
                                }
                            }
                        }
                    }
                    jboSet.commit();
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return false;
        }

        return true;
    }

    /**
     * 处理主对象的Jbo，没有联系名的
     * 
     * @param jboVo
     * @param jboSet
     * @return
     * @throws JxException
     */
    private JboIFace convertJboFromVo(JboVo jboVo, JboSetIFace jboSet) throws JxException {
        if (jboVo == null || jboSet == null) {
            return null;
        }
        JboIFace jbo = null;
        String action = jboVo.get_action();
        if ("C".equalsIgnoreCase(action)) {
            jbo = jboSet.add();
            jbo.getData().putAll(jboVo.get_datas());
            // jboVo.set_uid(jbo.getUidValue());
        } else if (!StrUtil.isNull(jboVo.get_uid())) {
            jbo = jboSet.queryJbo(jboVo.get_uid());
            if (jbo != null) {
                if ("U".equalsIgnoreCase(action) && !jboVo.get_datas().isEmpty()) {
                    modifyJboValue(jbo, jboVo);
                    jbo.setModify(true);
                } else if ("D".equalsIgnoreCase(action)) {
                    jbo.delete();
                }
            } else {
                LOG.debug("没有找到对应的记录：" + jboSet.getJboname() + "=" + jboVo.get_uid());
            }
        } else {
            LOG.debug("未知操作或uid为空,只允许C、U、D操作。action=" + action + ",uid=" + jboVo.get_uid());
        }
        return jbo;
    }

    /**
     * 处理子Jbo
     * 
     * @param pJbo
     * @param childJboVo
     * @return
     * @throws JxException
     */
    private JboIFace convertChildJboFromVo(JboIFace pJbo, JboVo childJboVo) throws JxException {
        if (null == pJbo && null == childJboVo) {
            LOG.debug("jbo or childJboVo is null.");
            return null;
        }
        String relationshipname = childJboVo.get_relationshipname();
        if (StrUtil.isNull(relationshipname)) {
            LOG.debug("Relationship is null.");
            return null;
        }
        String childJboAction = childJboVo.get_action();
        if (StrUtil.isNull(childJboAction)) {
            LOG.debug("子对象没有配置Action");
            return null;
        }
        JboSetIFace jboSet = pJbo.getChildrenJboSet(relationshipname);
        if (null == jboSet) {
            LOG.debug("没有找到相应的联系:jboname=" + pJbo.getJboName() + ",relationshipname=" + relationshipname);
            return null;
        }
        JboIFace childJbo = null;
        // 当父JBO为新增的时候，子jbo只能是新增的数据啊。没有其他操作
        if (pJbo.isToBeAdd()) {
            if ("C".equalsIgnoreCase(childJboAction)) {
                childJbo = jboSet.add();
                childJbo.getData().putAll(childJboVo.get_datas());
                // childJboVo.set_uid(childJbo.getUidValue());//回填
            } else {
                LOG.debug("Action is wrong." + childJboAction);
            }
        } else if (pJbo.isToBeDel()) {
            childJbo = jboSet.queryJbo(childJboVo.get_uid());
            if (null != childJbo) {
                childJbo.delete();
            }
        } else {
            if ("C".equalsIgnoreCase(childJboAction)) {
                // 创建记录Create
                childJbo = jboSet.add();
                childJbo.getData().putAll(childJboVo.get_datas());
                // childJboVo.set_uid(childJbo.getUidValue());
            } else if ("U".equalsIgnoreCase(childJboAction)) {
                // 更新记录Update
                childJbo = jboSet.queryJbo(childJboVo.get_uid());
                if (null != childJbo) {
                    modifyJboValue(childJbo, childJboVo);
                    childJbo.setModify(true);
                }
            } else if ("D".equalsIgnoreCase(childJboAction)) {
                // 删除记录 Delete
                childJbo = jboSet.queryJbo(childJboVo.get_uid());
                if (null != childJbo) {
                    childJbo.delete();
                }
            } else {
                LOG.debug("未知操作：Action=" + childJboAction);
            }
        }
        if (childJbo != null) {
            pJbo.addNeedSaveList(childJbo);
            jboSet.setJbolist(null);
            pJbo.setChangedChildren(null);
            pJbo.setChildren(null);
        }
        return childJbo;
    }

    /**
     * 处理单个jbovo对象，执行crud操作
     * 
     * @param pJbo 主对象Jbo
     * @param childJboVo 子对象jbovo
     * @throws JxException
     */
    private void handleChildJboVo(JboIFace pJbo, JboVo childJboVo) throws JxException {
        JboIFace childJbo = convertChildJboFromVo(pJbo, childJboVo);// 将ChildJboVo转换为ChildJbo
        // 递归处理
        List<JboVo> childrens = childJboVo.get_childrens();
        if (null != childJbo && null != childrens) {
            for (JboVo ccJbovo : childrens) {
                handleChildJboVo(childJbo, ccJbovo);
            }
        }
    }

    /**
     * 把 jboVo 中的值,赋值给Jbo
     * 
     * @param jbo
     * @param jboVo
     * @throws JxException
     */
    private void modifyJboValue(JboIFace jbo, JboVo jboVo) throws JxException {
        if (jbo == null || jboVo == null) {
            return;
        }
        Map<String, Object> voDatas = jboVo.get_datas();
        for (Map.Entry<String, Object> entry : voDatas.entrySet()) {
            String key = entry.getKey();
            jbo.setObject(key, entry.getValue(), JxConstant.SET_VALUE_NONE);
        }
    }

}
