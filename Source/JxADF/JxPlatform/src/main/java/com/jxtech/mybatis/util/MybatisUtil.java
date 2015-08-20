package com.jxtech.mybatis.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.08
 * 
 */
public class MybatisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MybatisUtil.class);

    public static SqlSessionFactory getSqlSessionFactory() {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            if (reader != null) {
                return new SqlSessionFactoryBuilder().build(reader);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }
        return null;
    }
}
