package com.jxtech.jbo.virtual;

import com.jxtech.jbo.BaseJbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;
import com.jxtech.workflow.option.WftParam;
import net.sf.mpxj.Task;

import java.util.List;
import java.util.Map;

/**
 * 虚拟Jbo，不会进行持久化
 *
 * @author wmzsoft@gmail.com
 * @date 2014.11
 */
public class VirtualJbo extends BaseJbo {

    private static final long serialVersionUID = -7118400319445392043L;

    public VirtualJbo(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void addMpp(Task tasks, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {
        throw new JxException("此方法未实现,请实现.addMpp");
    }

    @Override
    public String getWorkflowId() {
        return null;
    }

    @Override
    public boolean canRoute(Map<String, Object> params) throws JxException {
        throw new JxException("此方法未实现,请实现.canRoute");
    }

    @Override
    public boolean beforeRoute(Map<String, Object> params) throws JxException {
        throw new JxException("此方法未实现,请实现.beforeRoute");
    }

    @Override
    public boolean afterRoute(Map<String, Object> params) throws JxException {
        throw new JxException("此方法未实现,请实现.afterRoute");
    }

    @Override
    public boolean route(Map<String, Object> params) throws JxException {
        throw new JxException("此方法未实现,请实现.route");
    }

    @Override
    public boolean routeWorkflow(Map<String, Object> params) throws JxException {
        throw new JxException("此方法未实现,请实现.routeWorkflow");
    }

    @Override
    public Map<String, String> routeReassign(JboIFace curAct, JboIFace nextAct, Map<String, String> assign, int agree, String note, String tousers, String options) throws JxException {
        throw new JxException("此方法未实现,请实现.routeReassign");
    }

    @Override
    public int getAttachmentCount(String vFolder) throws JxException {
        throw new JxException("此方法未实现,请实现.getAttachmentCount");
    }

    @Override
    public List<WftParam> getWorkflowParam(String status, String nextStatus) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void routeHoldon(JboIFace jbo) throws JxException {
        throw new JxException("此方法未实现,请实现.routeHoldon");
    }

    @Override
    public void reloadData() throws JxException {
        throw new JxException("此方法未实现,请实现.reloadData");
    }

    @Override
    public void expMpp(Task task, JboIFace jbo) throws JxException {
        throw new JxException("此方法未实现,请实现.expMpp");
    }

    @Override
    public void prepareMaxmenu(List<JboIFace> menusToolbar, List<JboIFace> menulist) throws JxException {
        throw new JxException("此方法未实现,请实现.prepareMaxmenu");
    }
    @Override
    public void removeSomeMaxMenu(List<JboIFace> menusToolbar, List<String> options) throws JxException {
        throw new JxException("此方法未实现,请实现.removeSomeMaxMenu");
    }
    @Override
    public void afterLoad() throws JxException {

    }

    @Override
    public boolean setDefaultValue() throws JxException {
        return true;
    }

}
