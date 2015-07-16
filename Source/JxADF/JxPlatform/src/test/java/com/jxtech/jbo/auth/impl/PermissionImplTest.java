package com.jxtech.jbo.auth.impl;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11.26
 * 
 */
public class PermissionImplTest {

    @Test
    public void testIsIgoreSecurity() {
        PermissionImpl perm = new PermissionImpl();
        perm.putIgnoreSecurity("/JxOsgiClound/(index_ons\\S*|index_list-search).action", "JxOsgiClound");
        assertTrue(perm.isIgoreSecurity("/JxOsgiClound/index_list-search.action"));
        assertFalse(perm.isIgoreSecurity("/JxOsgiClound/index_list.action"));
    }

}
