package com.generate.pojo;

/**
 * User: dorid
 * Date: 13-7-16
 * Time: 14:41
 */
public class Column {
    private String name;
    private String fieldName;
    private String fieldType;
    private String type;
    private Long length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Long getLength() {
        return length;

    }

    public void setLength(Long length) {
        this.length = length;
    }
}
