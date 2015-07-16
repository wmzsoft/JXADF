package com.jxtech.app.maxapps;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

import java.util.Set;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public interface MaxappsSetIFace extends JboSetIFace {

    /**
     * 通过应用程序路径获得应用程序信息。
     * 
     * @param appurl
     * @return
     * @throws JxException
     */
    public JboIFace getJboByUrl(String appurl) throws JxException;

/**
 * 获取用户授权的APP集合。
 *
 */
 public Set<String> getAuthApps(String userid) throws JxException;
}
