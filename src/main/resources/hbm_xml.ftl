<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<!--
    Mapping file autogenerated by hibernate-generate Tools
-->
<hibernate-mapping>
    <class name="${classFullName}" table="${tableName}" catalog="${catalog}" >
        <id name="id" type="integer">
            <column name="id" />
            <generator class="native" />
        </id>

    <#list columns as column>
        <property name="${column.fieldName}" type="${column.type}">
            <column name="${column.name}" <#if column.length!=0>length="${column.length}"</#if>/>
        </property>
    </#list>
    </class>
</hibernate-mapping>
