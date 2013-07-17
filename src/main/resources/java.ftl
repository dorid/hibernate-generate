package ${classFullName};

public class ${className} implements java.io.Serializable {


    private static final long serialVersionUID = 1L;

    private Integer id;
<#list columns as column>
    private ${column.fieldType} ${column.fieldName};
</#list>

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

<#list columns as column>
    public void set${column.fieldName?cap_first}(${column.fieldType} ${column.fieldName}){
        this.${column.fieldName} = ${column.fieldName}
    }

    public ${column.fieldType} get${column.fieldName?cap_first}(){
        return this.${column.fieldName}
    }
</#list>
}