package com.generate.app;

import com.generate.pojo.Column;
import com.generate.util.Config;
import com.generate.util.FileUtil;
import com.generate.util.JdbcUtils;
import com.generate.util.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dorid
 * Date: 13-7-16
 * Time: 14:17
 */
public class Generate {


    public static void main(String[] args) throws Exception {
        Generate generate = new Generate();
        generate.checkConfig();
        List<String> tableList = generate.getTableList(Config.getInstance().get("table_name"));
        for (int i = 0; i < tableList.size(); i++) {
            String tableName = tableList.get(i);
            generate.generateXml(tableName);
            generate.generateJava(tableName);
            generate.generateDao(tableName);
            generate.generateDaoImpl(tableName);
            generate.generateService(tableName);
        }
    }

    private void generateService(String tableName) throws Exception {
        String java = runFreemarker(tableName, "daoImpl.ftl");
        String classPackage = Config.getInstance().get("class_package").replace(".", "/") + "/" + getClassName(tableName) + "Service.java";
        FileUtil.writeToFile(classPackage, java);
    }
    private void generateDaoImpl(String tableName) throws Exception {
        String java = runFreemarker(tableName, "daoImpl.ftl");
        String classPackage = Config.getInstance().get("class_package").replace(".", "/") + "/" + getClassName(tableName) + "DAOBean.java";
        FileUtil.writeToFile(classPackage, java);
    }
    private void generateDao(String tableName) throws Exception {
        String java = runFreemarker(tableName, "dao.ftl");
        String classPackage = Config.getInstance().get("class_package").replace(".", "/") + "/" + getClassName(tableName) + "DAO.java";
        FileUtil.writeToFile(classPackage, java);
    }

    private List<String> getTableList(String tableExpress) {
        Connection connection = null;
        List<String> tableNameList = new ArrayList<String>();
        try {
            String sql = "SELECT DISTINCT table_name FROM COLUMNS WHERE table_name like '" + tableExpress + "' " + "and table_schema='" + Config.getInstance().get("catalog") + "'";
            connection = JdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                tableNameList.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            JdbcUtils.free(null, null, connection);
        }
        return tableNameList;
    }

    private void generateJava(String tableName) throws Exception {
        String java = runFreemarker(tableName, "java.ftl");
        String classPackage = Config.getInstance().get("class_package").replace(".", "/") + "/" + getClassName(tableName) + ".java";
        FileUtil.writeToFile(classPackage, java);
    }

    public void checkConfig() throws Exception {
        String tableName = Config.getInstance().get("table_name").trim();
        String catalog = Config.getInstance().get("catalog").trim();

        if (tableName == null || "".equals(tableName)) {
            throw new Exception("table_name can't be empty");
        }

        if (catalog == null || "".equals(catalog)) {
            throw new Exception("catalog can't be empty");
        }
    }

    public Map<String, Object> getMetaData(String tableName) {

        Map<String, Object> metaMap = new HashMap<String, Object>();
        Connection connection = null;
        try {
            connection = JdbcUtils.getConnection();
//            String tableName = Config.getInstance().get("table_name");
            metaMap.put("className", getClassName(tableName));
            metaMap.put("tableName", tableName);
            metaMap.put("catalog", Config.getInstance().get("catalog"));

            String classPackage = Config.getInstance().get("class_package");
            String classFullName = getClassName(tableName);
            if (!"".equals(classPackage)) {
                classFullName = classPackage + "." + getClassName(tableName);
            }
            metaMap.put("classFullName", classFullName);

            String sql = "SELECT * FROM COLUMNS WHERE table_name like '" + tableName + "' " +
                    "and table_schema='" + Config.getInstance().get("catalog") + "'";


            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Column> columns = new ArrayList<Column>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                String columnKey = resultSet.getString("column_key");
                String dataType = resultSet.getString("data_type");
                String javaDataType = getJavaDataType(dataType);
                Long length = resultSet.getLong("character_maximum_length");
                if (dataType.equals("longtext")) {
                    length = 0L;
                }
                dataType = getSQLDataType(dataType);



                if ("PRI".equals(columnKey)) {
                    continue;
                }

                String[] split = columnName.split("_");
                String fieldName = split[0];
                for (int i = 1; i < split.length; i++) {
                    fieldName += split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
                }

                Column column = new Column();
                column.setName(columnName);
                column.setFieldType(javaDataType);
                column.setType(dataType);
                column.setLength(length);
                column.setFieldName(fieldName);

                columns.add(column);
            }

            metaMap.put("columns", columns);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.free(null, null, connection);
        }

        return metaMap;
    }

    private String getJavaDataType(String dataType) {
        if ("int".equals(dataType)) {
            return "Integer";
        }
        if ("varchar".equals(dataType)
                || "longtext".equals(dataType)
                || "char".equals(dataType)
                || "text".equals(dataType)
                ) {
            return "String";
        }
        if ("datetime".equals(dataType)) {
            return "Date";
        }
        if ("decimal".equals(dataType)
                || "double".equals(dataType)
                ) {
            return "Double";
        }
        return dataType;
    }

    private String getSQLDataType(String dataType) {
        if (dataType.equals("varchar") || dataType.equals("longtext") || dataType.equals("char")) {
            return "string";
        }

        if ("datetime".equals(dataType)) {
            return "timestamp";
        }

        return dataType;
    }

    public void generateXml(String tableName) throws Exception {

        String html = runFreemarker(tableName, "hbm_xml.ftl");

        String classPackage = Config.getInstance().get("class_package").replace(".", "/") + "/" + getClassName(tableName) + ".hbm.xml";
        FileUtil.writeToFile(classPackage, html);
    }

    private String runFreemarker(String tableName, String ftl) throws IOException, TemplateException {
        InputStream in  = this.getClass().getClassLoader().getResourceAsStream(ftl);
        ByteArrayOutputStream out =  new ByteArrayOutputStream();

        int i=0;
        byte[] buff = new byte[1024];
        while((i = in.read(buff)) != -1){
            out.write(buff,0,i);
        }
        in.close();
        out.close();
        String tempBody = new String(out.toByteArray());

        Configuration config = new Configuration();

        config.setTemplateLoader(new StringTemplateLoader(tempBody));
//        config.setDefaultEncoding(DEFAULT_ENCODING);

        Template template = config.getTemplate("");
        template.setClassicCompatible(true);
        StringWriter w = new StringWriter();
        template.process(getMetaData(tableName), w);
        w.flush();

        return w.getBuffer().toString();
    }


    public String getClassName(String tableName) {
        String[] tableSplit = tableName.split("_");
        String className = "";
        for (int i = 0; i < tableSplit.length; i++) {
            className += tableSplit[i].substring(0, 1).toUpperCase() + tableSplit[i].substring(1);
        }

        return className;
    }
}
