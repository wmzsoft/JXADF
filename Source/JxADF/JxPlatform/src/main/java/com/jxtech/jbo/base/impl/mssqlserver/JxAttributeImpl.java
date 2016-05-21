package com.jxtech.jbo.base.impl.mssqlserver;

public class JxAttributeImpl extends com.jxtech.jbo.base.impl.JxAttributeImpl {

    /**
     * 
     */
    private static final long serialVersionUID = 288850765918416393L;

    @Override
    public String getNumOrDateTimeCause() {
        String maxType = this.getMaxType();
        if ("DATE".equals(maxType)) {
            return "CAST(? as date)";
        } else if ("DATETIME".equals(maxType)) {
            return "CAST(? as datetime)";
        } else if ("TIME".equals(maxType)) {
            return "CAST(? as time)";
        } else if (isNumeric()) {
            return "?";
        }
        return "";
    }
}
