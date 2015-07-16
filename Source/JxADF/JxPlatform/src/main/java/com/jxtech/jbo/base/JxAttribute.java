package com.jxtech.jbo.base;

import com.jxtech.jbo.JboValue;
import com.jxtech.jbo.util.JxFormat;
import com.jxtech.util.StrUtil;

import java.io.Serializable;
import java.sql.Types;

/**
 * 每个字段的基本信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxAttribute implements Serializable {

    private static final long serialVersionUID = -7951210857358076305L;
    public static final int WILDCARDSEARCH = 0;
    public static final int EXACTSEARCH = 1;
    public static final int TEXTSEARCH = 2;
    public static final int NONESEARCH = 3;

    private String attributeName;// 属性名
    private String objectName;// 对象名
    private String alias;// 别名
    private String autoKeyName;// 自动增长序列名
    private int attributeNumber;// 序号
    private String className;// 业务处理类名
    private String entityName;// 实体名
    private String entityColumnName;// 实体字段名
    private String defaultValue;// 默认值
    private boolean eAuditEnabled = false; // 已启用电子审计
    private boolean eSigEnabled = false;// 电子签名已启用
    private boolean isLDOwner; // 长描述
    private boolean isPositive; // 正向
    private int length = 50;// 长度
    private int scale = 0;// 精度
    private String maxType = "ALN";// 类型
    private boolean required = false;// 必填
    private boolean mustbe = false;// 如果选中此项，那么表明不能变更“最大类型”、“长度”和“小数位数”；如果不选此项，那么表明可以变更这些项。
    private boolean persistent;// 持久化
    private int primaryKeyColSequence = 0;// 主键列序列
    private String sameAsObject;// 等同表
    private String sameAsAttribute;// 等同列
    private String title;// 标题
    private boolean userdefined;// 用户定义
    private boolean restricted;// 受限
    private String domainId;// 域ID
    private String handleColumnName;// 操作列名称
    private String searchType;// 查询类型
    private boolean mlInUse = false;// 使用多语言
    private boolean mlSupported = false;// 支持多语言
    private boolean localizable = false;// 可本地化

    private String textdirection;// 文本方向
    private String complexexpression;// 复杂表达式类型

    private int sqlType = 0;
    private int searchTypeAsInt = 0;
    private String wherecause;// 查询条件1
    private String wherecause2;// 查询条件2
    private String wherecauseValue;// 查询条件1的值
    private String wherecauseValue2;// 查询条件2的值

    private int persistentAttributeNumber;
    private int fetchAttributeNumber;
    private boolean isContentAttribute = false;

    /**
     * 是否为数字、日期类型
     * 
     * @return
     */
    public boolean isNumOrDateTime() {
        return (isNumeric() || isDateType());
    }

    /**
     * 是否为日期类型
     * 
     * @return
     */
    public boolean isDateType() {
        if ("DATE".equalsIgnoreCase(maxType)) {
            return true;
        } else if ("DATETIME".equalsIgnoreCase(maxType)) {
            return true;
        } else if ("TIME".equalsIgnoreCase(maxType)) {
            return true;
        }
        return false;
    }

    public boolean isNumeric() {
        switch (getTypeAsInt()) {
            case JxFormat.INTEGER:
            case JxFormat.SMALLINT:
            case JxFormat.FLOAT:
            case JxFormat.DECIMAL:
            case JxFormat.DURATION:
            case JxFormat.AMOUNT:
            case JxFormat.BIGINT:
            case JxFormat.NUMBER:
                return true;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
        }
        return false;
    }

    /**
     * 是否要限制输入字符串的长度，一般为字符串类型的需要限制
     * 
     * @return
     */
    public boolean isRestrictLength() {
        if (",ALN,LOWER,UPPER,GL,".indexOf(maxType) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否要转换为大写字符
     * 
     * @return
     */
    public boolean isUpper() {
        return "UPPER".equalsIgnoreCase(maxType);
    }

    /**
     * 是否要转换为小写字母
     * 
     * @return
     */
    public boolean isLower() {
        return "LOWER".equalsIgnoreCase(maxType);
    }

    /**
     * 获得SQL的类型
     * 
     * @return
     */
    public int getSqlType() {
        if (sqlType != 0) {
            return sqlType;
        }
        if (",ALN,GL,LOWER,UPPER,CRYPTO,CRYPTOX,DURATION,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.VARCHAR;
        } else if (",LONGALN,LONG,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.LONGVARCHAR;
        } else if (",SMALLINT,YORN,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.SMALLINT;
        } else if (",INTEGER,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.INTEGER;
        } else if (",CLOB,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.CLOB;
        } else if (",BLOB,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.BLOB;
        } else if (",BIGINT,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.BIGINT;
        } else if (",AMOUNT,DECIMAL,NUMBER,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.DECIMAL;
        } else if (",DATE,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.DATE;
        } else if (",TIME,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.TIME;
        } else if (",DATETIME,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.TIMESTAMP;
        } else if ("FLOAT,".indexOf("," + maxType + ",") >= 0) {
            sqlType = Types.FLOAT;
        }
        return sqlType;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String aliasName) {
        this.alias = aliasName;
    }

    public void setHandleColumnName(String name) {
        this.handleColumnName = name;
    }

    public String getHandleColumnName() {
        return this.handleColumnName;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(String name) {
        this.attributeName = name;
    }

    public int getAttributeNumber() {
        return this.attributeNumber;
    }

    public void setAttributeNumber(int number) {
        this.attributeNumber = number;
    }

    public String getAutoKeyName() {
        return this.autoKeyName;
    }

    public void setAutoKeyName(String autoKeyName) {
        this.autoKeyName = autoKeyName;
    }

    public void setSearchType(String type) {
        this.searchType = type;

        if (type.equalsIgnoreCase("EXACT")) {
            this.searchTypeAsInt = EXACTSEARCH;
        } else if (type.equalsIgnoreCase("WILDCARD")) {
            this.searchTypeAsInt = WILDCARDSEARCH;
        } else if (type.equalsIgnoreCase("TEXT")) {
            this.searchTypeAsInt = TEXTSEARCH;
        } else if (type.equalsIgnoreCase("NONE")) {
            this.searchTypeAsInt = NONESEARCH;
        } else {
            this.searchTypeAsInt = WILDCARDSEARCH;
        }
    }

    public String getSearchType() {
        return this.searchType;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEntityColumnName() {
        return this.entityColumnName;
    }

    public void setEntityColumnName(String entityColumnName) {
        this.entityColumnName = entityColumnName;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String string) {
        this.defaultValue = string;
    }

    public String getDomainId() {
        return this.domainId;
    }

    public void setDomainId(String string) {
        this.domainId = string;
    }

    public boolean isEAuditEnabled() {
        return this.eAuditEnabled;
    }

    public void setEAuditEnabled(boolean enabled) {
        this.eAuditEnabled = enabled;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public boolean isESigEnabled() {
        return this.eSigEnabled;
    }

    public void setESigEnabled(boolean enabled) {
        this.eSigEnabled = enabled;
    }

    public boolean isLDOwner() {
        return this.isLDOwner;
    }

    public void setLDOwner(boolean isLDOwner) {
        this.isLDOwner = isLDOwner;
    }

    public boolean isPositive() {
        return this.isPositive;
    }

    public void setPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMaxType() {
        return this.maxType;
    }

    public void setMaxType(String string) {
        this.maxType = string;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(String string) {
        this.objectName = string;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public boolean isFetchAttribute() {
        return (this.persistent) || (this.entityColumnName != null);
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public int getPrimaryKeyColSequence() {
        return this.primaryKeyColSequence;
    }

    public void setPrimaryKeyColSequence(int sequenceNumber) {
        this.primaryKeyColSequence = sequenceNumber;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public boolean isUserdefined() {
        return this.userdefined;
    }

    public void setRestricted(boolean value) {
        this.restricted = value;
    }

    public boolean isRestricted() {
        return this.restricted;
    }

    public void setUserdefined(boolean userdefined) {
        this.userdefined = userdefined;
    }

    public String getSameAsAttribute() {
        return this.sameAsAttribute;
    }

    public void setSameAsAttribute(String sameAsAttribute) {
        this.sameAsAttribute = sameAsAttribute;
    }

    public String getSameAsObject() {
        return this.sameAsObject;
    }

    public void setSameAsObject(String sameAsObject) {
        this.sameAsObject = sameAsObject;
    }

    public int getPersistentAttributeNumber() {
        return this.persistentAttributeNumber;
    }

    public void setPersistentAttributeNumber(int persistentAttributeNumber) {
        this.persistentAttributeNumber = persistentAttributeNumber;
    }

    public int getFetchAttributeNumber() {
        return this.fetchAttributeNumber;
    }

    public void setFetchAttributeNumber(int fetchAttributeNumber) {
        this.fetchAttributeNumber = fetchAttributeNumber;
    }

    public String getName() {
        return this.attributeName;
    }

    public boolean isKey() {
        return this.primaryKeyColSequence > 0;
    }

    public boolean hasLongDescription() {
        return this.isLDOwner;
    }

    public String getType() {
        return this.maxType;
    }

    public int getTypeAsInt() {
        return JxFormat.getMaxTypeAsInt(getType());
    }

    public String getDomainName() {
        return this.domainId;
    }

    public int getPrimaryKeyColSeq() {
        return this.primaryKeyColSequence;
    }

    public void setMLInUse(boolean ml) {
        this.mlInUse = ml;
    }

    public boolean isMLInUse() {
        return this.mlInUse;
    }

    public void setMLSupported(boolean supported) {
        this.mlSupported = supported;
    }

    public boolean isMLSupported() {
        return this.mlSupported;
    }

    public int getSearchTypeAsInt() {
        return this.searchTypeAsInt;
    }

    public boolean isContentAttribute() {
        return this.isContentAttribute;
    }

    public void setIsContentAttribute(boolean is) {
        this.isContentAttribute = is;
    }

    public String getTextdirection() {
        return this.textdirection;
    }

    public void setTextdirection(String string) {
        this.textdirection = string;
    }

    public String getComplexexpression() {
        return this.complexexpression;
    }

    public void setComplexexpression(String string) {
        this.complexexpression = string;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getNumOrDateTimeCause() {
        if ("DATE".equals(maxType)) {
            return "to_date(?,'yyyy-mm-dd')";
        } else if ("DATETIME".equals(maxType)) {
            return "to_date(?,'yyyy-mm-dd HH24:MI:SS')";
        } else if ("TIME".equals(maxType)) {
            return "to_date(?,'HH24:MI:SS')";
        } else if (isNumeric()) {
            return "?";
        }
        return "";
    }

    public String getWherecause() {
        if (StrUtil.isNull(wherecause)) {
            if (isNumOrDateTime()) {
                wherecause = ">=" + getNumOrDateTimeCause();
            } else if (EXACTSEARCH == searchTypeAsInt) {
                wherecause = "=?";
            } else {
                wherecause = " like ?";
            }
        }
        return wherecause;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public boolean isLocalizable() {
        return localizable;
    }

    public void setLocalizable(boolean localizable) {
        this.localizable = localizable;
    }

    public String getWherecause2() {
        if (StrUtil.isNull(wherecause2)) {
            if (isNumOrDateTime()) {
                wherecause2 = "<=" + getNumOrDateTimeCause();
            } else if (EXACTSEARCH == searchTypeAsInt) {
                wherecause2 = "=?";
            } else {
                wherecause2 = " like ?";
            }
        }
        return wherecause2;
    }

    public long getFlag() {
        long flag = 0;
        if (isRequired()) {
            flag = flag | JboValue.REQUIRED;
        }
        return flag;
    }

    public void setWherecause2(String wherecause2) {
        this.wherecause2 = wherecause2;
    }

    public String getWherecauseValue() {
        return wherecauseValue;
    }

    public void setWherecauseValue(String wherecauseValue) {
        this.wherecauseValue = wherecauseValue;
    }

    public String getWherecauseValue2() {
        return wherecauseValue2;
    }

    public void setWherecauseValue2(String wherecauseValue2) {
        this.wherecauseValue2 = wherecauseValue2;
    }

    public boolean isMustbe() {
        return mustbe;
    }

    public void setMustbe(boolean mustbe) {
        this.mustbe = mustbe;
    }
}
