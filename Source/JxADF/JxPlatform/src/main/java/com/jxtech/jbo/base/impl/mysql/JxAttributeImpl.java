package com.jxtech.jbo.base.impl.mysql;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2016.05
 *
 */
public class JxAttributeImpl extends com.jxtech.jbo.base.impl.JxAttributeImpl {

    private static final long serialVersionUID = 6059716936692566573L;

    @Override
    public String getNumOrDateTimeCause() {
        String maxType = this.getMaxType();
        if ("DATE".equals(maxType)) {
            return "DATE_FORMAT(?,'%Y-%m-%d')";
        } else if ("DATETIME".equals(maxType)) {
            return "DATE_FORMAT(?,'%Y-%m-%d %T')";
        } else if ("TIME".equals(maxType)) {
            return "DATE_FORMAT(?,'%T')";
        } else if (isNumeric()) {
            return "?";
        }
        return "";
    }

}
