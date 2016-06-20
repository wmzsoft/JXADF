package com.jxtech.app.usermetadata;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public interface UserMetadataSetIFace extends JboSetIFace {
    /**
     * 保存用户个性化信息
     * 
     * @param key
     * @param value
     *            当为空时，表示删除。
     * @return
     * @throws JxException
     */
    public boolean saveUserMetadata(String key, String value) throws JxException;
}
