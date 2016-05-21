package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * 每个字段的基本信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public interface JxAttribute extends Serializable {
    /**
     * 是否为数字、日期类型
     * 
     * @return
     */
    public boolean isNumOrDateTime();

    /**
     * 是否为日期类型
     * 
     * @return
     */
    public boolean isDateType();

    public boolean isNumeric();

    /**
     * 是否要限制输入字符串的长度，一般为字符串类型的需要限制
     * 
     * @return
     */
    public boolean isRestrictLength();

    /**
     * 是否要转换为大写字符
     * 
     * @return
     */
    public boolean isUpper();

    /**
     * 是否要转换为小写字母
     * 
     * @return
     */
    public boolean isLower();

    public boolean isBoolean();

    /**
     * 获得SQL的类型
     * 
     * @return
     */
    public int getSqlType();

    public String getAlias();

    public void setAlias(String aliasName) ;

    public void setHandleColumnName(String name) ;

    public String getHandleColumnName() ;

    public String getAttributeName();

    public void setAttributeName(String name);

    public int getAttributeNumber() ;

    public void setAttributeNumber(int number) ;

    public String getAutoKeyName();

    public void setAutoKeyName(String autoKeyName) ;

    public void setSearchType(String type);

    public String getSearchType() ;

    public String getClassName() ;

    public void setClassName(String className);

    public String getEntityColumnName() ;

    public void setEntityColumnName(String entityColumnName);

    public String getDefaultValue();

    public void setDefaultValue(String string);

    public String getDomainId() ;

    public void setDomainId(String string) ;

    public boolean isEAuditEnabled() ;

    public void setEAuditEnabled(boolean enabled);

    public String getEntityName();

    public void setEntityName(String entityName) ;

    public boolean isESigEnabled() ;

    public void setESigEnabled(boolean enabled) ;

    public boolean isLDOwner() ;

    public void setLDOwner(boolean isLDOwner) ;

    public boolean isPositive();

    public void setPositive(boolean isPositive) ;

    public int getLength() ;

    public void setLength(int length) ;

    public String getMaxType() ;

    public void setMaxType(String string) ;

    public String getObjectName() ;

    public void setObjectName(String string) ;

    public boolean isPersistent();

    public boolean isFetchAttribute() ;

    public void setPersistent(boolean persistent);

    public int getPrimaryKeyColSequence() ;

    public void setPrimaryKeyColSequence(int sequenceNumber);

    public boolean isRequired() ;

    public void setRequired(boolean required) ;

    public int getScale() ;

    public void setScale(int scale) ;

    public String getTitle() ;

    public void setTitle(String string);

    public boolean isUserdefined() ;

    public void setRestricted(boolean value) ;

    public boolean isRestricted() ;

    public void setUserdefined(boolean userdefined) ;

    public String getSameAsAttribute();

    public void setSameAsAttribute(String sameAsAttribute);

    public String getSameAsObject();

    public void setSameAsObject(String sameAsObject);

    public int getPersistentAttributeNumber() ;

    public void setPersistentAttributeNumber(int persistentAttributeNumber) ;

    public int getFetchAttributeNumber() ;

    public void setFetchAttributeNumber(int fetchAttributeNumber);

    public String getName() ;

    public boolean isKey() ;

    public boolean hasLongDescription();

    public String getType();

    public int getTypeAsInt();

    public String getDomainName() ;

    public int getPrimaryKeyColSeq();

    public void setMLInUse(boolean ml) ;

    public boolean isMLInUse();

    public void setMLSupported(boolean supported) ;

    public boolean isMLSupported();

    public int getSearchTypeAsInt();

    public boolean isContentAttribute() ;

    public void setIsContentAttribute(boolean is);

    public String getTextdirection() ;

    public void setTextdirection(String string) ;

    public String getComplexexpression() ;

    public void setComplexexpression(String string);

    public void setSqlType(int sqlType) ;

    public String getNumOrDateTimeCause();

    public String getWherecause() ;

    public void setWherecause(String wherecause);

    public boolean isLocalizable();

    public void setLocalizable(boolean localizable);
    public String getWherecause2() ;

    public long getFlag() ;

    public void setWherecause2(String wherecause2);

    public String getWherecauseValue() ;

    public void setWherecauseValue(String wherecauseValue);

    public String getWherecauseValue2();

    public void setWherecauseValue2(String wherecauseValue2) ;

    public boolean isMustbe() ;

    public void setMustbe(boolean mustbe) ;

}
