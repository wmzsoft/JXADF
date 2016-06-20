package com.jxtech.common;

import java.security.Principal;

/**
 * 最简单的一个权限类
 * @author wmzsoft@gmail.com
 *
 */
public class SimplePrincipal implements Principal {
    private String name;

    public SimplePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
