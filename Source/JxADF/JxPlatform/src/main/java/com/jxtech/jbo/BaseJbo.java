package com.jxtech.jbo;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.Connection;
import java.util.*;
import java.util.Map.Entry;

import com.jxtech.util.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.autokey.AutoKeySetIFace;
import com.jxtech.app.max.MaxFactory;
import com.jxtech.app.max.MaxSequenceSetIFace;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataEdit;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.DataMap;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxObject;
import com.jxtech.jbo.base.JxRelationship;
import com.jxtech.jbo.base.JxRelationshipDao;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;

/**
 * 基类Jbo,此类不操作数据库
 *
 * @author wmzsoft@gmail.com
 * @date 2014.11
 */
public abstract class BaseJbo implements JboIFace {

    private static final Logger LOG = LoggerFactory.getLogger(BaseJbo.class);
    private static final long serialVersionUID = -4973080003987153751L;

    // 当前记录的所有字段信息:DataMap<String,Object>
    private Map<String, Object> data;
    private Map<String, JboValue> values;
    // 当前记录对应的记录集
    private JboSetIFace jboSet;
    // 当前记录的子对象HashMap<父表名.联系名，结果集>
    private Map<String, JboSetIFace> children;
    // 在某种情况下，需要保存一些与此Jbo无联系的独立的Jbo信息，一般情况下为空。
    private List<JboIFace> needSaveList;

    // 当前记录是否被修改，默认为false
    private boolean modify = false;
    // 当前记录是否为新增
    private boolean toBeAdd = false;
    // 当前记录是否准备删除
    private boolean toBeDel = false;
    // 当前记录是否为只读
    private boolean readonly = false;
    // 当前记录的保存标识
    private long saveFlag = 0xFFFFFF;

    // 发生改变的子表
    private Set<String> changedChildren = new HashSet<String>();

    // 附件
    private Map<String, JboSetIFace> attachments = new HashMap<String, JboSetIFace>();

    public BaseJbo(JboSetIFace jboset) throws JxException {
        this.jboSet = jboset;
        init();
    }

    protected boolean canAdd() throws JxException {
        boolean b = getJboSet().canAdd();
        if (b) {
            if (isReadonly()) {
                throw new JxException(JxLangResourcesUtil.getString("jbo.cannot.add.readonly"));
            }
        }
        return true;
    }

    protected boolean canAddFormFile(String fileType) throws JxException {
        return true;
    }

    /**
     * 数据加载成功之后执行的操作
     *
     * @throws JxException
     */
    @Override
    public void init() throws JxException {

    }

    /**
     * 创建Jbo之后就执行，如果要在加载数据之后执行请重载afterLoad方法。
     *
     * @throws JxException
     */
    @Override
    public void afterLoad() throws JxException {
        if (getJboSet().isReadonly() || isReadonly()) {// 集合只读或者自身在子类只读，均为只读
            readonly = true;
        } else if (!isToBeAdd()) {
            String status = getString("wft_status");
            readonly = ("CLOSE".equals(status) || "CANCEL".equalsIgnoreCase(status));
        }
        if (!readonly && !isToBeAdd()) {
            // 没有保存按钮的权限，则只读
            if (!getJboSet().canSave()) {
                readonly = true;
                return;
            }
            // 不是只读，不是新增，则设置自动编号字段只读
            Map<String, JxAttribute> autokeys = getJboSet().getAutokeysAttributes();
            if (autokeys != null) {
                for (Map.Entry<String, JxAttribute> entry : autokeys.entrySet()) {
                    setReadonly(entry.getKey(), true);
                }
            }
        }
    }

    @Override
    public void add() throws JxException {
        if (!canAdd()) {
            return;
        }
        setModify(true);
        setToBeAdd(true);
        data = new DataMap<String, Object>();
        setReadonly(false);
        setDefaultValue();
    }

    public boolean setDefaultValue() throws JxException {
        if (data == null) {
            LOG.info("不能设置默认值。");
            return false;
        }
        JxUserInfo jui = JxSession.getJxUserInfo();
        if (jui != null) {
            data.put("WFT_STATUS", "WAPPR");
            data.put("CHANGEBY", jui.getUserid());
            data.put("SITEID", jui.getSiteid());
            data.put("ORGID", jui.getOrgid());
            data.put("LANGCODE", jui.getLangcode());
            // 以下为康托普系统的东东
            data.put("CREATOR_ID", jui.getUserid());
            data.put("MODIFIER_ID", jui.getUserid());
            // 以下为工作流字段
            data.put("WFT_TRANSACTOR_ID", jui.getUserid());
            data.put("WFT_TRANSACTOR", jui.getDisplayname());
            // data.put("AUDIT_STATUS", "1");
        }
        java.sql.Timestamp ts = DateUtil.sqlDateTime();
        data.put("CHANGEDATE", ts);
        data.put("STATUSDATE", ts);

        data.put("CREATE_TIME", ts);
        data.put("MODIFY_TIME", ts);
        data.put("CREATE_DATE", ts);
        data.put("MODIFY_DATE", ts);
        String uidName = getUidName();
        if (isNumeric(uidName)) {
            data.put(uidName, getNewSequence());
        } else {
            data.put(uidName, String.valueOf(getNewSequence()));
        }
        return setSystemDefaultValue();
    }

    /**
     * 设定系统默认值，即Maxattribute表中配置的默认值
     *
     * @return
     * @throws JxException
     */
    private boolean setSystemDefaultValue() throws JxException {
        Map<String, JxAttribute> attrs = getJxAttributes();
        for (Map.Entry<String, JxAttribute> entity : attrs.entrySet()) {
            JxAttribute attr = entity.getValue();
            if (attr != null) {
                String dv = attr.getDefaultValue();
                if (!StrUtil.isNull(dv)) {
                    // 需要处理这个默认值
                    if ("&AUTOKEY&".equalsIgnoreCase(dv)) {
                        AutoKeySetIFace aks = (AutoKeySetIFace) JboUtil.getJboSet("AUTOKEY");
                        data.put(attr.getAttributeName(), aks.generateKeyValue(attr.getAutoKeyName(), null, null));
                    } else if ("&SYSDATE&".equals(dv)) {
                        java.sql.Timestamp ts = DateUtil.sqlDateTime();
                        data.put(attr.getAttributeName(), ts);
                    } else if (!StrUtil.isNull(dv)) {
                        data.put(attr.getAttributeName(), ELUtil.getJboElValue(this, dv));
                    }
                }
            }
        }
        return true;
    }

    /**
     * 能否保存
     *
     * @return true 可以保存,false不能保存
     * @throws JxException
     */
    public boolean canSave() throws JxException {
        // 如果做只读校验,并且只读,则返回false，保存失败
        if (readonly && (saveFlag & JboIFace.SAVE_NO_CHECK_READONLY) == JboIFace.SAVE_NO_CHECK_READONLY) {
            return false;
        }
        return getJboSet().canSave();
    }

    /**
     * 保存事件，子类可以继承，做自己的事。
     *
     * @return
     * @throws JxException
     */
    public boolean save() throws JxException {
        if (!canSave()) {
            return false;
        }
        if (!getChangedChildren().isEmpty()) {
            getChangedChildren().clear();
        }
        return beforeSave();
    }

    /**
     * 保存之前需要处理的工作
     *
     * @return
     * @throws JxException
     */
    public boolean beforeSave() throws JxException {
        return true;
    }

    @Override
    public boolean save(Connection conn) throws JxException {
        if (!save()) {
            return false;
        }
        DataEdit de = DBFactory.getDataEdit(this.getJboSet().getDbtype(), getJboSet().getDataSourceName());
        boolean flag = de.save(conn, this);
        if (flag) {
            /*
             * setModify(false); setToBeAdd(false); setToBeDel(false); setJboValueModifyAll(false);
             */
            if (children != null) {
                for (Map.Entry<String, JboSetIFace> entry : children.entrySet()) {
                    flag = flag & entry.getValue().save(conn);
                    if (flag == false) {
                        return flag;
                    }
                }
            }
        }
        if (needSaveList != null) {
            int size = needSaveList.size();
            for (int i = 0; i < size; i++) {
                flag = flag & needSaveList.get(i).save(conn);
            }
        }
        if (!flag) {
            return false;
        }
        return afterSave();
    }

    public boolean afterSave() throws JxException {
        return true;
    }

    public boolean setJboValueModifyAll(boolean modify) throws JxException {
        getValues();
        for (Map.Entry<String, JboValue> v : values.entrySet()) {
            v.getValue().setModify(modify);
        }
        return true;
    }

    /**
     * 回滚数据
     *
     * @return 返回需要移除新增jbo
     */
    @Override
    public JboIFace rollback() throws JxException {
        // 先让自己往回滚
        JboIFace needRemoveJbo = null;
        if (toBeAdd) {
            needRemoveJbo = this;
        } else if (modify) {
            reloadData();
        } else if (toBeDel) {
            setToBeDel(false);
        }
        // 让儿子们也要往回滚
        Map<String, JboSetIFace> children = getChildren();
        if (null != children) {
            Iterator<String> childrenKey = children.keySet().iterator();
            while (childrenKey.hasNext()) {
                children.get(childrenKey.next()).rollback();
            }
        }
        if (!getChangedChildren().isEmpty()) {
            getChangedChildren().clear();
        }
        return needRemoveJbo;
    }

    /**
     * 删除前要做的事情
     *
     * @return 可以继续删除，返回true，否则返回false
     * @throws JxException
     */
    public boolean beforeDelete() throws JxException {
        if (!canDelete()) {
            return false;
        }
        return true;
    }

    /**
     * 从只是标记删除，并没有执行删除操作。
     */
    @Override
    public boolean delete() throws JxException {
        if (!beforeDelete()) {
            return false;
        }
        // 保存特殊的内容
        if (!isToBeAdd()) {
            setToBeDel(true);
        } else {
            // setToBeAdd(false);
            getJboSet().getJbolist().remove(this);
        }
        return true;
    }

    @Override
    public boolean beforeUnDelete() throws JxException {
        return true;
    }

    /**
     * 将标记置为未删除。
     *
     * @return
     * @throws JxException
     */
    @Override
    public boolean unDelete() throws JxException {
        if (!beforeUnDelete()) {
            return false;
        }
        String[] delChildren = getDeleteChildren();
        // 1.删除子记录
        if (delChildren != null) {
            for (int i = 0; i < delChildren.length; i++) {
                if (StrUtil.isNullOfIgnoreCaseBlank(delChildren[i])) {
                    continue;
                }
                JboSetIFace js = getRelationJboSet(delChildren[i], JxConstant.READ_RELOAD);
                if (js != null) {
                    List<JboIFace> list = js.getJbolist();
                    int count = list.size();
                    for (int j = 0; j < count; j++) {
                        JboIFace ji = list.get(j);
                        boolean b = ji.unDelete();
                        if (!b) {
                            return false;
                        }

                    }
                }
            }
        }
        setToBeDel(false);
        return true;
    }

    /**
     * 如果需要删除子对象，需要覆盖此方法 。
     *
     * @return 返回需要删除的联系名
     * @throws JxException
     */
    @Override
    public String[] getDeleteChildren() throws JxException {
        return null;
    }

    /**
     * 判断能否删除
     *
     * @return true能，false不能
     */
    public boolean canDelete() throws JxException {
        boolean ret = true;
        if (readonly) {
            throw new JxException(JxLangResourcesUtil.getString("jbo.cannot.delete.readonly"));
        }
        // 判断流程退回状态
        String wfback = getString("WFT_BACKFLAG");
        if (StrUtil.isNull(wfback)) {
            ret = true;
        } else {
            if ("T".equals(wfback)) {
                throw new JxException(JxLangResourcesUtil.getString("jbo.cannot.delete.wfback"));
            }
        }
        // 判断流程是否可编辑状态
        String status = getString("wft_status");
        if (StrUtil.isNull(status)) {
            ret = true;
        } else {
            if (!"WAPPR".equals(status)) {
                throw new JxException(JxLangResourcesUtil.getString("jbo.cannot.delete.hadsendprocess"));
            }
        }
        return ret;
    }

    /**
     * 复制记录
     *
     * @return 复制之后的对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public JboIFace duplicate() throws JxException {
        JboIFace jbi = getJboSet().add();
        // 将对象的值全部复制过来

        Map<String, Object> nData = (DataMap<String, Object>) ((DataMap<String, Object>) data).clone();
        jbi.setData(nData);
        String[] ignores = getIgnoreAttributesOfDuplicate();
        if (ignores != null) {
            for (int i = 0; i < ignores.length; i++) {
                nData.remove(ignores[i].toUpperCase());
            }
        }
        nData.remove(getUidName());// 自动忽略主键
        // 复制主对象
        String[] needChildren = getNeedChildrenOfDuplicate();
        if (needChildren != null && children != null) {
            for (int j = 0; j < needChildren.length; j++) {
                String key = getJboName() + "." + needChildren[j];
                key = key.toUpperCase();
                if (children.containsKey(key)) {
                    JboSetIFace js = children.get(key);
                    if (js != null) {
                        List<JboIFace> list = js.getJbolist();
                        if (list != null) {
                            int size = list.size();
                            for (int k = 0; k < size; k++) {
                                list.get(k).duplicate();
                            }
                        }
                        Map<String, JboSetIFace> cs = jbi.getChildren();
                        if (cs == null) {
                            cs = new DataMap<String, JboSetIFace>();
                            jbi.setChildren(cs);
                        }
                        cs.put(key, js);
                    }
                }
            }
        }
        return jbi;
    }

    /**
     * 需要略过的复制字段
     *
     * @return
     */
    protected String[] getIgnoreAttributesOfDuplicate() throws JxException {
        return null;
    }

    /**
     * 请覆盖
     *
     * @return 返回需要复制的子对象的联系名。
     * @throws JxException
     */
    protected String[] getNeedChildrenOfDuplicate() throws JxException {
        return null;
    }

    @Override
    public Object getObject(String attributeName) throws JxException {
        return getObject(attributeName, JxConstant.READ_CACHE);
    }

    public String getElValue(String expression) {
        return ELUtil.parseJboElValue(this, expression);
    }

    private Object getObjectValue(String attributeName, long flag) throws JxException {
        if (StrUtil.isNull(attributeName) || data == null) {
            return null;
        }
        attributeName = attributeName.toUpperCase();
        if (data.containsKey(attributeName)) {
            return data.get(attributeName);
        } else if (attributeName.indexOf(".") > 0) {
            return getObjectOfReleationship(attributeName, flag);
        } else if (StrUtil.isNumber(attributeName)) {
            // 如果传入的数字，则直接返回此数字。
            return attributeName;
        } else {
            int len = attributeName.length();
            if (len > 1) {
                if (attributeName.charAt(0) == 39) {
                    // 第一个字母为单引号，则直接返回引号中的内容。
                    return attributeName.substring(1, len - 1);
                } else {
                    String method = StrUtil.contact("get", StrUtil.convertFirstUpperLower(attributeName));
                    Object val = invokeGetMethod(method);
                    // 判断是否需要缓存,默认是需要的
                    String cacheMethod = StrUtil.contact(method, "Cache");// Cache方法
                    Object cacheStatus = invokeGetMethod(cacheMethod);// Cache状态
                    if (cacheStatus instanceof Boolean) {
                        if (!((Boolean) cacheStatus)) {
                            // 不用缓存了
                            return val;
                        }
                    }
                    // 把这个值加入到对象中，缓存起来
                    data.put(attributeName.toUpperCase(), val);
                    return val;
                }
            }
        }
        return null;
    }

    protected Object invokeGetMethod(String name) {
        try {
            Method m = getClass().getMethod(name);
            return m.invoke(this);
        } catch (SecurityException e) {
            LOG.debug(e.getMessage());
        } catch (NoSuchMethodException e) {
            // 不需处理
        } catch (Exception e) {
            LOG.debug(e.getMessage());
        }
        return null;
    }

    protected Object invokeMethod(String name, Class<?> parameterTypes, Object[] params) {
        try {
            Method m;
            if (parameterTypes != null) {
                m = getClass().getMethod(name, parameterTypes);
                return m.invoke(this, params);
            } else {
                m = getClass().getMethod(name);
                return m.invoke(this);
            }
        } catch (SecurityException e) {
            LOG.info(e.getMessage());
        } catch (NoSuchMethodException e) {
            // 不需处理
            LOG.info(e.getMessage());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return null;
    }

    /**
     * 只能处理简单的 || 、 +、-、*、/ ，不能按运算符的优先级计算，更复杂的内容，请使用自定义列的方式处理。 http://svn.jxtech.net:8081/pages/viewpage.action?pageId=15728819
     *
     * @param attributeName
     * @param flag
     */
    @Override
    public Object getObject(String attributeName, long flag) throws JxException {
        if (StrUtil.isNull(attributeName) || data == null) {
            return null;
        }
        attributeName = attributeName.toUpperCase();
        String[] columns = StrUtil.splitWord(attributeName);
        if (columns.length > 1) {
            StringBuilder sb = new StringBuilder();// 最终计算出来的值
            int pos = 0;// 当前的运算符的位置pos
            for (int i = 0; i < columns.length; i++) {
                Object value = getObjectValue(columns[i], flag);// 得到当前值
                if (i == 0) {
                    if (value != null) {
                        sb.append(value);// 第一个值，直接加入
                    }
                    pos = columns[i].length();
                } else {
                    char c = attributeName.charAt(pos);
                    pos += columns[i].length() + 1;
                    if ("".equals(sb.toString()) || (StrUtil.isNumber(sb.toString())) && StrUtil.isNumber(String.valueOf(value))) {
                        // 如果前后都是数字，则进行四则运算
                        double v1 = StrUtil.parseDouble(sb.toString(), 0);
                        double v2 = StrUtil.parseDouble(String.valueOf(value), 0);
                        if (c == '+') {
                            sb.setLength(0);
                            sb.append(v1 + v2);
                        } else if (c == '-') {
                            sb.setLength(0);
                            sb.append(v1 - v2);
                        } else if (c == '*') {
                            sb.setLength(0);
                            sb.append(v1 * v2);
                        } else if (c == '/') {
                            sb.setLength(0);
                            if (v2 == 0) {
                                sb.append(v1).append("/0");
                            } else {
                                sb.append(v1 / v2);
                            }
                        } else if (c == '\\') {
                            sb.setLength(0);
                            if (v1 == 0) {
                                sb.append(v2).append("/0");
                            } else {
                                sb.append(v2 / v1);
                            }
                            pos++;
                        }
                    } else if (value != null) {
                        sb.append(value);
                    }
                }
            }
            return sb.toString();
        } else {
            return getObjectValue(attributeName, flag);
        }
    }

    /**
     * 获得 联系名.字段名 的值。
     *
     * @param attributeName
     * @param flag
     * @return
     * @throws JxException
     */
    public Object getObjectOfReleationship(String attributeName, long flag) throws JxException {
        String[] ans = attributeName.split("\\.");
        if (ans.length < 2) {
            return null;
        }
        int idx = 1;// 最后一个可用的字段名，默认为1
        JboSetIFace jset = getRelationJboSet(ans[0], flag);
        for (int i = 1; i <= ans.length - 2; i++) {
            if (jset == null) {
                break;
            }
            JboIFace jif = jset.getJbo();
            if (jif == null) {
                break;
            }
            jset = jif.getRelationJboSet(ans[i], flag);
            idx++;
        }
        if (jset != null && jset.getCount() > 0) {
            Object value = jset.getJbo().getObject(ans[idx]);
            data.put(attributeName, value);
            // 将属性信息放入进去。
            Map<String, JxAttribute> attrs = getJxAttributes();
            if (attrs != null) {
                attrs.put(attributeName, jset.getJxAttribute(ans[idx]));
            }
            return value;
        } else {
            LOG.debug("没有得到正确的值，属性：" + attributeName);
        }
        return null;
    }

    @Override
    public JxAttribute getJxAttribute(String attributeName) throws JxException {
        Map<String, JxAttribute> attrs = getJxAttributes();
        if (attrs != null) {
            Object obj = attrs.get(attributeName);
            if (obj instanceof JxAttribute) {
                return (JxAttribute) obj;
            } else {
                // LOG.warn("attributeName[" + attributeName + "] is not JxAttribute");
            }
        }
        return null;
    }

    @Override
    public boolean isNumeric(String attributeName) throws JxException {
        JxAttribute attr = getJxAttribute(attributeName);
        if (attr != null) {
            return attr.isNumeric();
        }
        return false;
    }

    @Override
    public long getNewSequence() throws JxException {
        MaxSequenceSetIFace seqset = MaxFactory.getMaxSequenceSetIFace();
        return seqset.generateNewSequence(getJboName(), getUidName());
    }

    @Override
    public String getString(String attributeName) throws JxException {
        return getString(attributeName, JxConstant.READ_CACHE);
    }

    @Override
    public boolean getBoolean(String attributeName) throws JxException {
        return getBoolean(attributeName, JxConstant.READ_CACHE);
    }

    @Override
    public boolean getBoolean(String attributeName, long flag) throws JxException {
        Object value = getObject(attributeName, flag);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof String) {
            String s = ((String) value).trim().toLowerCase();
            if ("Y".equals(s) || "T".equals(s) || "1".equals(s)) {
                return true;
            } else {
                return false;
            }
        } else if (value instanceof BigDecimal) {
            int i = ((java.math.BigDecimal) value).intValue();
            return i > 0;
        }
        try {
            long v = Long.parseLong((String) value);
            return (v > 0);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public long getLong(String attributeName) throws JxException {
        return getLong(attributeName, JxConstant.READ_CACHE);
    }

    @Override
    public double getDouble(String attributeName) throws JxException {
        return getDouble(attributeName, JxConstant.READ_CACHE);
    }

    @Override
    public double getDouble(String attributeName, long flag) throws JxException {
        Object value = getObject(attributeName, flag);
        if (value == null) {
            return 0;
        } else if (value instanceof java.lang.Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof java.lang.Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof java.lang.Boolean) {
            if (((Boolean) value).booleanValue()) {
                return 1D;
            } else {
                return 0D;
            }
        }
        try {
            JxAttribute attr = jboSet.getJxAttribute(attributeName);
            if (attr != null) {
                String maxtype = attr.getMaxType();
                if ("DATE".equalsIgnoreCase(maxtype)) {
                    return DateUtil.dateToLong(value);
                } else if ("TIME".equalsIgnoreCase(maxtype)) {
                    return DateUtil.timeToLong(value);
                } else if ("DATETIME".equals(maxtype)) {
                    return DateUtil.datetimeToLong(value);
                }
            }
            double d = Double.parseDouble((String) value);
            return d;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return 0.0D;
    }

    /**
     * 直接返回java.util.date
     *
     * @param attributeName
     * @return
     * @throws JxException
     */
    public java.util.Date getDate(String attributeName) throws JxException {
        return getDate(attributeName, JxConstant.READ_CACHE);
    }

    /**
     * 直接返回java.util.date
     *
     * @param attributeName
     * @param flag
     * @return
     * @throws JxException
     */
    public java.util.Date getDate(String attributeName, long flag) throws JxException {
        Object value = getObject(attributeName, flag);
        if (value == null) {
            return null;
        } else if (value instanceof java.sql.Date) {
            java.sql.Date d = (java.sql.Date) value;
            return new Date(d.getTime());
        } else if (value instanceof java.util.Date) {
            return (Date) value;
        }
        JxAttribute attr = jboSet.getJxAttribute(attributeName);
        if (attr != null) {
            String maxtype = attr.getMaxType();
            if ("DATE".equalsIgnoreCase(maxtype) || "TIME".equalsIgnoreCase(maxtype) || "DATETIME".equals(maxtype)) {
                return DateUtil.stringToDate((String) value);
            } else if (attr.isNumeric()) {
                long l = getLong(attributeName, flag);
                if (l > 0.0D) {
                    return new Date(l);
                }
            }
        }
        return null;
    }

    @Override
    public long getLong(String attributeName, long flag) throws JxException {
        double value = getDouble(attributeName, flag);
        return (long) value;
    }

    @Override
    public String getString(String attributeName, long flag) throws JxException {
        return getString(attributeName, flag, null);
    }

    public String getString(String attributeName, long flag, String formatter) throws JxException {
        Object value = getObject(attributeName, flag);
        if (value == null) {
            return "";
        }
        JxAttribute attr = jboSet.getJxAttribute(attributeName);
        if (attr != null) {
            String maxtype = attr.getMaxType();
            if ("DATE".equalsIgnoreCase(maxtype)) {
                return DateUtil.dateToString(value);
            } else if ("TIME".equalsIgnoreCase(maxtype)) {
                return DateUtil.timeToString(value);
            } else if ("DATETIME".equals(maxtype)) {
                return DateUtil.dateTimeToString(value);
            } else if (attr.isNumeric()) {
                double d = getDouble(attributeName, flag);
                return NumUtil.format(d, formatter);
            } else if ("CLOB".equals(maxtype)) {
                if (value instanceof java.lang.String) {
                    return (String) value;
                } else {
                    Clob clob = (Clob) value;
                    char[] tempDoc = null;
                    Reader inStreamDoc = null;
                    try {
                        tempDoc = new char[(int) clob.length()];
                        inStreamDoc = clob.getCharacterStream();
                        inStreamDoc.read(tempDoc);
                        return new String(tempDoc);
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    } finally {
                        IOUtils.closeQuietly(inStreamDoc);
                    }

                    return "";
                }

                /*
                 * if (value instanceof CLOB) { CLOB cv = (CLOB) value; try { int len = (int) cv.getLength(); return cv.getSubString(1, len); } catch (Exception e) { LOG.error(e.getMessage()); LOG.error(e.getMessage()); } }
                 */
            }
        }
        if (value instanceof String) {
            return (String) value;

        } else {
            return value.toString();
        }
    }

    /**
     * 将值转换为URL的格式
     *
     * @param attributeName
     * @return
     * @throws JxException
     */
    public String getURLString(String attributeName) throws JxException {
        String urlValue = getString(attributeName);
        try {
            if (!StrUtil.isNull(urlValue)) {
                urlValue = URLEncoder.encode(urlValue, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            throw new JxException(e.getMessage());
        }

        return urlValue;
    }

    /**
     * 将值转换为适合JSON格式的字符串
     *
     * @param attributeName
     * @return
     * @throws JxException
     */
    public String getJsonString(String attributeName) throws JxException {
        String val = this.getString(attributeName);
        return StrUtil.toJson(val);
    }

    @Override
    public boolean setObject(String attributeName, Object value) throws JxException {
        return setObject(attributeName, value, JxConstant.SET_VALUE_NONE);
    }

    @Override
    public boolean setObject(String attributeName, Object value, long flag) throws JxException {
        if (StrUtil.isNull(attributeName)) {
            throw new JxException(JxLangResourcesUtil.getString("jbo.not.attributename"));
        }
        if (data == null) {
            throw new JxException(JxLangResourcesUtil.getString("jbo.not.data"));
        }
        if (StrUtil.isObjectNull(value) && attributeName.indexOf(".") > 0) {
            data.remove(attributeName);
            return true;
        }
        if (data.containsKey(attributeName)) {
            if (StrUtil.compare(value, data.get(attributeName), "=")) {
                // 没有变化,也是设置成功
                return true;
            }
        }
        data.put(attributeName, value);
        // 判断是否存在这个属性，如果不存在则是虚拟字段，不予处理。
        JxAttribute attribute = getJxAttribute(attributeName);
        if (null != attribute) {
            setModify(true);
            getValue(attributeName).setModify(true);
            if (value != null && value instanceof String) {
                if (attribute.isUpper()) {
                    data.put(attributeName, ((String) value).toUpperCase());
                } else if (attribute.isLower()) {
                    data.put(attributeName, ((String) value).toLowerCase());
                }
            }
        }
        return true;
    }

    @Override
    public boolean setString(String attributeName, String value) throws JxException {
        return setString(attributeName, value, JxConstant.SET_VALUE_NONE);
    }

    @Override
    public boolean setString(String attributeName, String value, long flag) throws JxException {
        return setObject(attributeName, value, flag);
    }

    protected JboSetIFace findRelationship(JboIFace jf, String relationship) throws JxException {
        JboSetIFace jsf = null;
        if (null != jf) {
            jsf = jf.getRelationJboSet(relationship, JxConstant.READ_CACHE);
            if (null == jsf) {
                Map<String, JboSetIFace> children = jf.getChildren();
                Iterator<Entry<String, JboSetIFace>> it = children.entrySet().iterator();
                while (it.hasNext()) {
                    @SuppressWarnings("rawtypes")
                    Map.Entry child = (Map.Entry) it.next();
                    jsf = findRelationship(((JboSetIFace) child.getValue()).getJbo(), relationship);
                    if (null != jsf) {
                        break;
                    }
                }
            }
        }
        return jsf;
    }

    /**
     * 读取联系JboSet
     *
     * @param name 联系名
     */
    @Override
    public JboSetIFace getRelationJboSet(String name) throws JxException {
        // return getRelationJboSet(name, JxConstant.READ_CACHE);
        JboSetIFace jsf = findRelationship(this, name);
        return jsf;
    }

    /**
     * 通过联系名获得JboSet
     *
     * @param name 联系名
     * @param flag 参数 JxConstant.READ_CACHE 直接读取Cache，
     */
    @Override
    public JboSetIFace getRelationJboSet(String name, long flag) throws JxException {
        return getRelationJboSet(name, flag, false);
    }

    /**
     * 通过联系名获得JboSet
     *
     * @param name     联系名
     * @param flag     参数 JxConstant.READ_CACHE 直接读取Cache，
     * @param queryAll 是否查全部
     */
    @Override
    public JboSetIFace getRelationJboSet(String name, long flag, boolean queryAll) throws JxException {
        if (StrUtil.isNull(name)) {
            return null;
        }
        if (0 == flag) {
            flag = JxConstant.READ_CACHE;
        }
        String jboName = getJboName();
        String key = (jboName + "." + name).toUpperCase();
        if (children == null) {
            children = new DataMap<String, JboSetIFace>();
        } else if (children.containsKey(key) && ((flag & JxConstant.READ_CACHE) == JxConstant.READ_CACHE)) {
            return children.get(key);
        }
        JxRelationship ship = JxRelationshipDao.getJxRelationship(jboName, name);
        if (ship != null) {
            JboSetIFace jbos;

            String cacheKey = StrUtil.contact(JxRelationshipDao.CACHE_PREX, name);
            Object cacheJbos = CacheUtil.getBase(cacheKey);
            if (JxConstant.READ_RELOAD != flag && !isToBeAdd() && null != cacheJbos && cacheJbos instanceof JboSetIFace) {
                jbos = (JboSetIFace) cacheJbos;
            } else {
                jbos = JboUtil.getJboSet(ship.getChild());
                CacheUtil.putBaseCache(cacheKey, jbos);
            }

            String where = ship.getWhereclause();
            String clause = where;
            String pname;
            List<Object> params = new ArrayList<Object>();
            boolean needQuery = true;
            if (!StrUtil.isNull(where)) {
                int pos = where.indexOf(":");
                int end = -1;

                while (pos >= 0) {
                    end = -1;
                    int len = clause.length();
                    for (int i = pos; i < len; i++) {
                        char blank = ' ';
                        char rParenth = ')';
                        char x = clause.charAt(i);
                        if (x == blank || x == rParenth) {
                            end = i;
                            break;
                        }
                    }
                    if (end < pos) {
                        end = len;
                    }
                    if (pos > 0 && end > pos) {
                        pname = clause.substring(pos + 1, end);
                        clause = clause.replaceFirst(":" + pname, "?");
                        params.add(data.get(pname));
                    }
                    pos = clause.indexOf(":");
                }
                DataQueryInfo qi = jbos.getQueryInfo();

                String oldClause = qi.getRelationshipCause();
                Object[] oldParams = qi.getRelationshipParams();


                if (null != oldClause && oldClause.equalsIgnoreCase(clause) && Arrays.equals(params.toArray(), oldParams)) {
                    needQuery = false;
                } else {
                    qi.setRelationshipCause(clause);
                    qi.setRelationshipParams(params.toArray());
                }

            }

            if (needQuery) {
                if (queryAll) {
                    jbos.queryAll();
                } else {
                    jbos.query(name);
                }
            }

            jbos.setParent(this);
            jbos.setAppname(getJboSet().getAppname());
            children.put(key, jbos);

            // 既然获取了relationship 的jboset，顺便赋值给当前记录(1对1的情况下,非主从)
            if (jbos.getJbolist().size() == 1) {
                JboIFace jbo = jbos.getJbolist().get(0);
                Map<String, Object> dataMap = getData();
                Iterator<?> keyIterator = dataMap.keySet().iterator();
                while (keyIterator.hasNext()) {
                    String dataKey = keyIterator.next().toString();
                    if (dataKey.indexOf(".") > 0) {
                        String[] dataKeys = dataKey.split("\\.");
                        if (dataKeys[0].equalsIgnoreCase(name)) {
                            dataMap.put(dataKey, jbo.getObject(dataKeys[1]));
                        }
                    }
                }
            }
            return jbos;
        }
        return null;
    }

    @Override
    public JboIFace getParent() throws JxException {
        return getJboSet().getParent();
    }

    /**
     * 获得唯一关键字名
     *
     * @throws JxException
     */
    @Override
    public String getUidName() throws JxException {
        return jboSet.getUidName();
    }

    /**
     * 获得唯一关键字的值
     */
    @Override
    public String getUidValue() throws JxException {
        return getString(getUidName());
    }

    /**
     * 返回字段的动态信息
     *
     * @param attributeName 字段名
     * @param isCreate      不存在是否创建
     * @return
     * @throws JxException
     */
    public JboValue getValue(String attributeName, boolean isCreate) throws JxException {
        if (StrUtil.isNull(attributeName)) {
            return null;
        }
        if (values == null) {
            values = getValues();
        }
        attributeName = attributeName.toUpperCase();
        if (values.containsKey(attributeName)) {
            return values.get(attributeName);
        } else if (isCreate) {
            JxAttribute jxAttribute = getJboSet().getJxAttribute(attributeName);
            long flag = -1;
            if (null != jxAttribute) {
                flag = jxAttribute.getFlag();
            }
            JboValue value = new JboValue(attributeName, getObject(attributeName), flag);
            values.put(attributeName, value);
            return value;
        }
        return null;
    }

    public JboValue getValue(String attributeName) throws JxException {
        return getValue(attributeName, true);
    }

    /**
     * 返回某个字段是否只读 默认为False
     *
     * @param attributeName
     * @return
     * @throws JxException
     */
    @Override
    public boolean isReadonly(String attributeName) throws JxException {
        if (isReadonly()) {
            return true;// 如果整条记录只读，则所有字段只读
        } else if (StrUtil.isNull(attributeName)) {
            return true;// 属性为空，则只读
        }
        JboValue value = getValue(attributeName, false);
        if (value == null) {
            // return (attributeName.indexOf(".") > 0);
            return false;
        }
        return value.isReadonly();
    }

    /**
     * 设定某个字段的只读属性
     *
     * @param attributeName 字段名
     * @param flag          是否只读
     * @throws JxException
     */
    @Override
    public void setReadonly(String attributeName, boolean flag) throws JxException {
        JboValue value = getValue(attributeName, true);
        value.setReadonly(flag);
    }

    /**
     * 设定某个数组字段的只读属性
     *
     * @param attributeNames 字段名的数组
     * @param flag           是否只读
     * @throws JxException
     */
    @Override
    public void setReadonly(String[] attributeNames, boolean flag) throws JxException {
        if (attributeNames != null) {
            for (int i = 0; i < attributeNames.length; i++) {
                setReadonly(attributeNames[i], flag);
            }
        }
    }

    /**
     * 获得某个字段的必填属性
     *
     * @param attributeName
     * @return
     * @throws JxException
     */
    @Override
    public boolean isRequired(String attributeName) throws JxException {
        JboValue value = getValue(attributeName, false);
        if (value == null) {
            JxAttribute attribute = getJxAttribute(attributeName);
            if (attribute != null) {
                return attribute.isRequired();
            } else {
                return false;
            }
        }
        return value.isRequired();
    }

    /**
     * 设定某个字段的必填属性
     *
     * @param attributeName
     * @param flag
     * @throws JxException
     */
    @Override
    public void setRequired(String attributeName, boolean flag) throws JxException {
        JboValue value = getValue(attributeName, true);
        // if(value!=null)
        value.setRequired(flag);
    }

    @Override
    public Map<String, JboSetIFace> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Map<String, JboSetIFace> children) throws JxException {
        this.children = children;
    }

    @Override
    public JboSetIFace getJboSet() {
        return jboSet;
    }

    public void setJboSet(JboSetIFace jboSet) {
        this.jboSet = jboSet;
    }

    @Override
    public String getJboName() {
        return jboSet.getJboname();
    }

    @Override
    public Map<String, JxAttribute> getJxAttributes() throws JxException {
        return jboSet.getJxAttributes();
    }

    @Override
    public boolean isModify() {
        return modify;
    }

    @Override
    public void setModify(boolean modify) {
        this.modify = modify;
        operateChangedChildren(modify);
    }

    @Override
    public boolean isToBeAdd() {
        return toBeAdd;
    }

    @Override
    public void setToBeAdd(boolean toBeAdd) {
        this.toBeAdd = toBeAdd;
        operateChangedChildren(toBeAdd);
    }

    @Override
    public boolean isToBeDel() {
        return toBeDel;
    }

    @Override
    public void setToBeDel(boolean toBeDel) {
        this.toBeDel = toBeDel;
        operateChangedChildren(toBeDel);
    }

    @Override
    public JxObject getJxobject() throws JxException {
        return jboSet.getJxobject();
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public void setData(Map<String, Object> data) {
        this.data = data;
        if (data instanceof DataMap) {
            ((DataMap<String, Object>) data).setJbo(this);
        }
    }

    /**
     * 返回Data数据类型中的简单对象，专门用于JavaScript处理
     *
     * @return
     */
    @Override
    public Map<String, Object> getDatas() {
        if (data == null) {
            return null;
        }
        Map<String, Object> datas = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object v = entry.getValue();
            if (v != null) {
                if (v instanceof Clob) {
                    continue;
                }
                datas.put(entry.getKey(), v);
            }
        }
        return datas;
    }

    /**
     * 将Data数据作为字符串返回。
     *
     * @return
     * @throws JxException
     */
    public String toJson() throws JxException {
        if (data == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        int i = 0;
        int size = data.size();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            buf.append("\"");
            buf.append(entry.getKey());
            buf.append("\":\"");
            buf.append(StrUtil.toJson(getString(entry.getKey())));
            buf.append("\"");
            i++;
            if (i < size - 1) {
                buf.append(",");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    @Override
    public JxTable getJxTable() throws JxException {
        return jboSet.getJxTable();
    }

    @Override
    public boolean isReadonly() throws JxException {
        return readonly;
    }

    @Override
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public List<JboIFace> getNeedSaveList() {
        return needSaveList;
    }

    public void setNeedSaveList(List<JboIFace> needSaveList) {
        this.needSaveList = needSaveList;
    }

    /**
     * 添加需要保存
     *
     * @param jbo
     * @throws JxException
     */
    @Override
    public void addNeedSaveList(JboIFace jbo) throws JxException {
        if (jbo == null) {
            return;
        }
        if (needSaveList == null) {
            needSaveList = new ArrayList<JboIFace>();
        }
        needSaveList.add(jbo);
    }

    public Map<String, JboValue> getValues() {
        if (values == null) {
            values = new HashMap<String, JboValue>();
        }
        return values;
    }

    public void setValues(Map<String, JboValue> values) {
        this.values = values;
    }

    /**
     * 添加一个附件
     *
     * @param name
     * @param attachementSet
     */
    @Override
    public void addAttachment(String name, JboSetIFace attachementSet) {
        attachments.put(name, attachementSet);
        afterAttachmentLoad(name, attachementSet);
    }

    @Override
    public void afterAttachmentLoad(String name, JboSetIFace attachmentSet) {
        // 加载完附件后需要处理的业务逻辑
        attachmentSet.setReadonly(readonly);
    }

    /**
     * 移除一个附件
     *
     * @param name
     * @return Attachment
     */
    @Override
    public JboSetIFace removeAttachment(String name) {
        return attachments.remove(name);
    }

    /**
     * 获取附件
     *
     * @return Map<String, Attachment>
     */
    @Override
    public Map<String, JboSetIFace> getAttachments() {
        return this.attachments;
    }

    /**
     * 设置附件列表
     *
     * @param attachments
     */
    @Override
    public void setAttachments(Map<String, JboSetIFace> attachments) {
        this.attachments = attachments;
    }

    @Override
    public Set<String> getChangedChildren() {
        return changedChildren;
    }

    @Override
    public void setChangedChildren(Set<String> changedChildren) {
        this.changedChildren = changedChildren;
    }

    @Override
    public JboSetIFace getChildrenJboSet(String relationship) throws JxException {
        JboSetIFace jboset = null;

        if (getChildren() != null && !getChildren().isEmpty()) {
            Iterator<String> ite = getChildren().keySet().iterator();
            while (ite.hasNext()) {
                String key = ite.next();
                if (key.equalsIgnoreCase(getJboName() + "." + relationship)) {
                    jboset = getChildren().get(key);
                    break;
                } else {
                    JboIFace tempJbo = getChildren().get(key).getJbo();
                    if (null != tempJbo) {
                        jboset = tempJbo.getChildrenJboSet(relationship);
                    } else {
                        jboset = null;
                    }
                }
            }
        }

        return jboset;
    }

    private void operateChangedChildren(boolean flag) {
        try {
            if (null != getParent()) {
                if (flag && null != getUidValue()) {
                    getParent().getChangedChildren().add(getJboName() + String.valueOf(getUidValue()));
                } else {
                    getParent().getChangedChildren().remove(getJboName() + String.valueOf(getUidValue()));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public long getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(long saveFlag) {
        this.saveFlag = saveFlag;
    }
}
