package com.softwarelma.epe.p3.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public class EpeDbEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // TODO
    private final EpeDbMetaDataEntity metaData;
    private final Map<String, Object> mapAttAndValue;

    public EpeDbEntity(EpeDbMetaDataEntity metaData, Map<String, Object> mapAttAndValue) throws EpeAppException {
        EpeAppUtils.checkNull("metaData", metaData);
        EpeAppUtils.checkEmptyMap("mapAttAndValue", mapAttAndValue);
        metaData.validateSetAttribute(mapAttAndValue.keySet());
        this.metaData = metaData;
        // TODO validate types
        this.mapAttAndValue = new HashMap<>(mapAttAndValue);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " " + this.mapAttAndValue.toString();
    }

    public EpeDbMetaDataEntity getMetaData() {
        return metaData;
    }

    @SuppressWarnings("unchecked")
    public <T> T getWithType(String attribute, Class<T> clazz) throws EpeAppException {
        Object value = this.get(attribute);
        EpeAppUtils.checkInstanceOf(attribute, value, clazz);
        return (T) value;
    }

    public Long getLong(String attribute) throws EpeAppException {
        return this.getWithType(attribute, Long.class);
    }

    /**
     * the type of the value must be String
     */
    public String getString(String attribute) throws EpeAppException {
        return this.getWithType(attribute, String.class);
    }

    /**
     * The type of the value gets converted to String. Null becomes "".
     */
    public String getToString(String attribute) throws EpeAppException {
        // TODO
        String className = this.metaData.getClassName(attribute);
        Object value = this.get(attribute);

        if (value == null) {
            return "";
        } else if (className.equals(String.class.getName())) {
            return value.toString();
        } else {
            throw new EpeAppException("Unknown class " + className);
        }
    }

    public Object get(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        attribute = attribute.toUpperCase();
        EpeAppUtils.checkContains(this.mapAttAndValue.keySet(), "attribute", attribute);
        Object value = this.mapAttAndValue.get(attribute);
        this.validateTypeAndNullable(attribute, value);
        return value;
    }

    private void validateTypeAndNullable(String attribute, Object value) throws EpeAppException {
        if (value == null) {
            if (!this.metaData.isNullable(attribute)) {
                throw new EpeAppException("The attribute " + attribute + " can't be null");
            }
        } else {
            String className = this.metaData.getClassName(attribute);
            EpeAppUtils.checkEquals("meataDataClassName", "valueClassName", className, value.getClass().getName());
        }
    }

    public void setFromString(String attribute, String valueStr) throws EpeAppException {
        // TODO
        String className = this.metaData.getClassName(attribute);
        Object value;

        if (valueStr == null) {
            value = null;
        } else if (className.equals(String.class.getName())) {
            value = valueStr;
        } else {
            throw new EpeAppException("Unknown class " + className);
        }

        this.set(attribute, value);
    }

    public void set(String attribute, Object value) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        attribute = attribute.toUpperCase();
        EpeAppUtils.checkContains(this.mapAttAndValue.keySet(), "attribute", attribute);
        this.validateTypeAndNullable(attribute, value);
        this.mapAttAndValue.put(attribute, value);
    }

    private String retrieveDescriptionShortOrLong(boolean shortDescr, String descriptionTemplate,
            String descriptionColumns) throws EpeAppException {
        if (EpeAppUtils.isEmpty(descriptionTemplate) || EpeAppUtils.isEmpty(descriptionColumns)) {
            return shortDescr ? this.getString(EpeDbEntityColumns.NAME) : "(" + this.get(EpeDbEntityColumns.ID) + ") "
                    + this.getString(EpeDbEntityColumns.NAME);
        }

        String[] arrayAttribute = descriptionColumns.split(Pattern.quote(","));
        String valueStr;

        for (String attribute : arrayAttribute) {
            valueStr = this.getToString(attribute);
            valueStr = valueStr == null ? "" : valueStr;
            descriptionTemplate = descriptionTemplate.replace("{" + attribute + "}", valueStr);
        }

        return descriptionTemplate;
    }

    public String retrieveDescriptionShort() throws EpeAppException {
        return this.retrieveDescriptionShortOrLong(true, EpeDbEntityColumns.DESCR_SHORT_TEMPL,
                EpeDbEntityColumns.DESCR_SHORT_COLS);
    }

    public String retrieveDescriptionLong() throws EpeAppException {
        return this.retrieveDescriptionShortOrLong(false, EpeDbEntityColumns.DESCR_LONG_TEMPL,
                EpeDbEntityColumns.DESCR_LONG_COLS);
    }

    public String retrieveDescription(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        Object value = this.mapAttAndValue.get(attribute);

        if (this.mapAttAndValue.containsKey(attribute.toUpperCase())) {
            EpeAppUtils.checkNull("value", value);
            return value + "";
        } else if (attribute.equals("retrieveDescriptionShort()")) {
            return this.retrieveDescriptionShort();
        } else if (attribute.equals("retrieveDescriptionLong()")) {
            return this.retrieveDescriptionLong();
        } else {
            throw new EpeAppException("Unknown attribute: " + attribute);
        }
    }

    public EpeDbEntity retrieveClone() throws EpeAppException {
        // TODO
        throw new EpeAppException("Invalid invocation");
    }

}
