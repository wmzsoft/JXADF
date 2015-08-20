package com.jxtech.jbo.virtual;

import com.jxtech.jbo.BaseJboSet;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 虚拟JboSet，不会进行持久化
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class VirtualJboSet extends BaseJboSet {

    private static final long serialVersionUID = -8890814440371572093L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new VirtualJbo(this);
        return currentJbo; // 创建自己的JBO
    }

    @Override
    public List<JboIFace> query(String shipname) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Connection conn) throws JxException {
        return true;
    }

    @Override
    public void setFlag() {
    }

    @Override
    public boolean rollback() throws JxException {
        return true;
    }

    @Override
    public boolean commit() throws JxException {
        return true;
    }

    @Override
    public boolean delete(String[] ids) throws JxException {
        return false;
    }

    @Override
    public boolean delete(Connection conn, JboIFace jbi, boolean isSave) throws JxException {
        return true;
    }

    @Override
    public JboIFace queryJbo(String uid) throws JxException {
        return null;
    }

    @Override
    public JboIFace getJboOfIndex(int index, boolean reload) throws JxException {
        return null;
    }

    @Override
    public int count() throws JxException {
        return 0;
    }

    @Override
    public void getBlob(String blobColumnName, String uid, OutputStream os) throws JxException {
    }

    @Override
    public String getWorkflowId() {
        return null;
    }

    @Override
    public void setWorkflowId(String wfId) {

    }

    @Override
    public boolean route() throws JxException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void afterLoad() throws JxException {
        // TODO Auto-generated method stub

    }

    @Override
    public void addMpp(List<Task> tasks, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {
        // TODO Auto-generated method stub

    }

    @Override
    public void expMpp(ProjectFile project, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {
        // TODO Auto-generated method stub

    }

    @Override
    public String loadImportFile(List<Map<Object, String>> importFileResult, JxUserInfo userInfo) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JboIFace queryJbo(String jboKey, String uid) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JboIFace queryJbo(String where, String jboKey, String uid) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void lookup(List<JboIFace> lookupList) throws JxException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<JboIFace> queryJbo(String[] ids) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSecurityrestrict(boolean elValue) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean commit(long flag) throws JxException {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean delete(Connection conn, String whereCause, Object[] params) throws JxException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(String whereCause, Object[] params) throws JxException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<JboIFace> getTree(String parentName, String parentValue, String idName, boolean includeSelf) throws JxException {
        // TODO Auto-generated method stub
        return null;
    }

}
