/*
 * 版    权:  Timesoft Copyright 2014-2016,All rights reserved
 * 文 件 名:  CreateBeana.java
 * 描       述:  <描述>
 * 修改人:  LUOXWPC
 * 修改时间:  2014-8-21
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.yangyang.cloud.common.createmapper;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


/**
 * <功能详细描述>
 *
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CreateBean {
    @SuppressWarnings("unused")
    private Connection connection = null;
    private String url;
    private String username;
    private String password;
    private String rt = "\r\t";
    private String dataSchema = "icp_bss";
    private String sqlTables = String.format("SELECT t.TABLE_NAME, t.TABLE_COMMENT FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = '%s'", dataSchema);

    public String getDataSchema() {
        return dataSchema;
    }

    public void setDataSchema(String dataSchema) {
        this.dataSchema = dataSchema;
        this.sqlTables = String.format("SELECT t.TABLE_NAME, t.TABLE_COMMENT FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = '%s'", dataSchema);
    }

    //String sqlTables = "select table_name from user_tables t";
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    public void setOracleInfo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    public void setOracleInfo(String url, String username, String password,String dataSchema) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.dataSchema = dataSchema;
        this.sqlTables = String.format("SELECT t.TABLE_NAME, t.TABLE_COMMENT FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = '%s'", dataSchema);

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public List<String> getTables() throws SQLException {
        Connection con = this.getConnection();
        PreparedStatement ps = con.prepareStatement(sqlTables);
        ResultSet rs = ps.executeQuery();
        List<String> list = new ArrayList<String>();
        while (rs.next()) {
            String tableName = rs.getString(1);
            list.add(tableName);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public Map<String, String> getTableNames() throws SQLException {
        Map<String, String> results = new HashMap<String, String>();
        Connection con = this.getConnection();
        PreparedStatement ps = con.prepareStatement(sqlTables);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String tableName = rs.getString(1);
            String tableDes = rs.getString(2);
            results.put(tableName, tableDes);
        }
        rs.close();
        ps.close();
        con.close();
        return results;
    }

    /**
     * 查询表的字段，封装成List
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<ColumnData> getColumnDatas(String tableName)
            throws SQLException {
        String sqlcolumns = "select t1.COLUMN_NAME,t1.DATA_TYPE,t1.NUMERIC_PRECISION,t1.COLUMN_COMMENT,t1.COLUMN_KEY,t1.ORDINAL_POSITION,t1.column_key from information_schema.`TABLES` t,information_schema.`COLUMNS` t1 WHERE t.TABLE_NAME = t1.TABLE_NAME AND t.TABLE_SCHEMA = '" + dataSchema + "' and t.TABLE_SCHEMA = t1.TABLE_SCHEMA and t1.TABLE_NAME='"
                + tableName
                + "' ORDER BY T1.ORDINAL_POSITION";
        Connection con = this.getConnection();
        PreparedStatement ps = con.prepareStatement(sqlcolumns);
        List<ColumnData> columnList = new ArrayList<ColumnData>();
        ResultSet rs = ps.executeQuery();
        StringBuffer str = new StringBuffer();
        StringBuffer getset = new StringBuffer();
        while (rs.next()) {
            String name = rs.getString(1).toLowerCase();
            String columnNameStr = name;
            String[] splitName = name.split("_");
            name = "";
            for (String s : splitName) {
                if ("".equals(name)) {
                    name = s;
                } else {
                    String temp = s.substring(0, 1).toUpperCase();
                    temp += s.substring(1, s.length());
                    name = name + temp;
                }
            }
            String type = rs.getString(2);
            int length = rs.getInt(3);
            String comment = rs.getString(4);
            String isprimarykey = rs.getString(5);
            int scale = rs.getInt(6);
            type = this.getType(type, length, scale);
            ColumnData cd = new ColumnData();
            cd.setColumnName(columnNameStr);
            cd.setColumnNameStr(name);
            cd.setDataType(type);
            cd.setColumnComment(comment);
            cd.setDataDefault("");
            cd.setLength(length);
            cd.setIsPrimarykey("PRI".equals(isprimarykey) ? "true" : "false");
            cd.setScale(scale);
            columnList.add(cd);
        }
        argv = str.toString();
        method = getset.toString();
        rs.close();
        ps.close();
        con.close();
        return columnList;
    }

    private String method;
    private String argv;

    /**
     * 生成实体Bean 的属性和set,get方法
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public String getBeanFeilds(String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(tableName);
        StringBuffer str = new StringBuffer();
        StringBuffer getset = new StringBuffer();
        for (ColumnData d : dataList) {
            String name = d.getColumnNameStr();
            String type = d.getDataType();
            String comment = d.getColumnComment();
            // type=this.getType(type);
            String maxChar = name.substring(0, 1).toUpperCase();
            str.append(String.format("/**\r\t* %s\r\t*/\r\t", comment)).append("private ").append(type + " ").append(name).append(";\r\t");
            String method = maxChar + name.substring(1, name.length());

            getset.append(String.format("/**\r\t* @return 返回 %s\r\t*/", comment)).append("\r\tpublic ").append(type + " ").append("get" + method + "() {\r\t");
            getset.append("    return this.").append(name).append(";\r\t}\r\t");
            getset.append(String.format("/**\r\t* @param 对 %s进行赋值\r\t*/", comment)).append("\r\tpublic void ").append("set" + method + "(" + type + " " + name + ") {\r\t");
            getset.append("    this." + name + "=").append(name).append(";\r\t}\r\t");
        }
        argv = str.toString();
        return argv + method;
    }

    public String getToDto(String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(tableName);
        StringBuffer str = new StringBuffer();
        //StringBuffer getset = new StringBuffer();
        for (ColumnData d : dataList) {
            //String name = d.getColumnName();
            String name = d.getColumnNameStr();
            if (!"true".equals(d.getIsPrimarykey())) {
                //String type = d.getDataType();
                //String comment = d.getColumnComment();
                // type=this.getType(type);
                String maxChar = name.substring(0, 1).toUpperCase();
                str.append("\r\t").append("dto.set");
                String method = maxChar + name.substring(1, name.length());
                str.append(method).append("(this.get").append(method).append("());");
            }
        }
        argv = str.toString();

        return argv;
    }


    public String getToModel(String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(tableName);
        StringBuffer str = new StringBuffer();
        for (ColumnData d : dataList) {
            String name = d.getColumnNameStr();
            if (!"true".equals(d.getIsPrimarykey())) {
                String maxChar = name.substring(0, 1).toUpperCase();
                str.append("\r\t").append("model.set");
                String method = maxChar + name.substring(1, name.length());
                str.append(method).append("(this.get").append(method).append("());");
            }
        }
        argv = str.toString();

        return argv;
    }

    public String getType(String type, int length, int scale) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        type = type.toLowerCase();
        if ("char".equals(type) || "varchar".equals(type)
                || "varchar2".equals(type)) {
            return "String";
        } else if ("int".equals(type) || "tinyint".equals(type) || "smallint".equals(type) || "mediumint".equals(type)) {
            return "Integer";
        } else if ("bigint".equals(type)) {
            return "BigInteger";
        } else if ("bit".equals(type)) {
            return "boolean";
        } else if ("number".equals(type) || "float".equals(type) || "double".equals(type)) {
            if (scale > 0) {
                return "Double";
            }
            if (scale == 0) {
                if (length > 10) {
                    return "Long";
                } else {
                    return "Integer";
                }
            }
            return "Double";
        } else if ("date".equals(type)) {
            return "Date";
        } else if ("timestamp".equals(type) || "date".equals(type)
                || "datetime".equals(type) || "timestamp(6)".equals(type)) {
            return "java.util.Date";
        }
        return null;
    }

    public void getPackage(int type, String createPath, String content,
                           String packageName, String className, String extendsClassName,
                           String... importName) throws Exception {
        if (null == packageName) {
            packageName = "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("package ").append(packageName).append(";\r");
        sb.append("\r");
        for (int i = 0; i < importName.length; i++) {
            sb.append("import ").append(importName[i]).append(";\r");
        }
        sb.append("\r");
        sb.append("/**\r *  entity. @author wolf Date:"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()) + "\r */");
        sb.append("\r");
        sb.append("\rpublic class ").append(className);
        if (null != extendsClassName) {
            sb.append(" extends ").append(extendsClassName);
        }
        if (type == 1) { // bean
            sb.append(" ").append("implements java.io.Serializable {\r");
        } else {
            sb.append(" {\r");
        }
        sb.append("\r\t");
        sb.append("private static final long serialVersionUID = 1L;\r\t");
        String temp = className.substring(0, 1).toLowerCase();
        temp += className.substring(1, className.length());
        if (type == 1) {
            sb.append("private " + className + " " + temp + "; // entity ");
        }
        sb.append(content);
        sb.append("\r}");
        System.out.println(sb.toString());
        this.createFile(createPath, "", sb.toString());
    }

    /**
     * 处理规则：去除第一个，从第二个开始首字母大写
     *
     * @param tableName
     * @return
     */
    public String getTablesNameToClassName(String tableName) {
        String[] split = tableName.toLowerCase().split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                if (!"info".equals(split[i])) {
                    if (!"info".equals(split[i])) {
                        String tempTableName = split[i].substring(0, 1)
                                .toUpperCase()
                                + split[i].substring(1, split[i].length());
                        sb.append(tempTableName);
                    }
                }
            }
            return sb.toString();
        } else {
            String tempTables = split[0].substring(0, 1).toUpperCase()
                    + split[0].substring(1, split[0].length());
            return tempTables;
        }
    }

    /**
     * <br>
     * <b>功能：</b>创建文件<br>
     * <b>作者：</b><br>
     * <b>日期：</b> 2011-12-21 <br>
     *
     * @param path
     * @param fileName
     * @param str
     * @throws IOException
     */
    public void createFile(String path, String fileName, String str)
            throws IOException {
        FileWriter writer = new FileWriter(new File(path + fileName));
        writer.write(new String(str.getBytes("utf-8")));
        writer.flush();
        writer.close();
    }

    /**
     * <br>
     * <b>功能：</b>生成sql语句<br>
     * <b>作者：</b><br>
     * <b>日期：</b> 2011-12-21 <br>
     *
     * @param tableName
     * @throws Exception
     */
    static String selectStr = "select ";
    static String from = " from ";

    public Map<String, Object> getAutoCreateSql(String tableName)
            throws Exception {
        Map<String, Object> sqlMap = new HashMap<String, Object>();
        List<ColumnData> columnDatas = getColumnDatas(tableName);
        String columnstr = this.getColumnStrSplit(columnDatas);
        String columns = this.getColumnSplit(columnDatas);//columnsMap.get("fields");
        String[] columnList = getColumnList(columns); // 表所有字段
        String columnFields = getColumnFields(columns); // 表所有字段 按","隔开
        String insert = "insert into " + tableName + "("
                + columns.replaceAll("\\|", ",") + ")\n values(#{"
                + columnstr.replaceAll("\\|", "},#{") + "})";
        String update = getUpdateSql(tableName, columnDatas);
        String updateSelective = getUpdateSelectiveSql(tableName, columnDatas);
        String selectById = getSelectByIdSql(tableName, columnList);
        String delete = getDeleteSql(tableName, columnList);
        String batchInsert = getBatchInsert(tableName, columnDatas);
        String batchBatch = getBatchDelete(tableName, columnList);
        sqlMap.put("columnList", columnList);
        sqlMap.put("columnFields", columnFields);
        sqlMap.put(
                "insert",
                insert.replace("#{createTime}", "now()").replace(
                        "#{updateTime}", "now()"));
        sqlMap.put(
                "update",
                update.replace("#{createTime}", "now()").replace(
                        "#{updateTime}", "now()"));
        sqlMap.put("delete", delete);
        sqlMap.put("updateSelective", updateSelective);
        sqlMap.put("selectById", selectById);
        sqlMap.put("batchInsert", batchInsert);
        sqlMap.put("batchBatch", batchBatch);
        return sqlMap;
    }

    /**
     * @param @param  tableName
     * @param @param  columnsList
     * @param @return
     * @return String 返回类型
     * @throws
     * @Title: getBatchInsert
     * @Description:
     */
    public String getBatchInsert(String tableName, List<ColumnData> columnList) {
        StringBuffer sb = new StringBuffer();
        //sb.append("insert into " + tableName + "(");
        //for (int i = 0; i < columnsList.length; i++) {
        //	sb.append(columnsList[i]);
        //	sb.append(",");
        //}
        //sb.deleteCharAt(sb.length()-1);
        //sb.append(")\nvalues\n");
        //sb.append("		<foreach collection=\"list\" item=\"obj\" index=\"index\" separator=\",\" > \n (#{obj.id}");
        sb.append("#{obj.id}");
        for (int i = 1; i < columnList.size(); i++) {
            sb.append(",#{obj.");
            sb.append(columnList.get(i).getColumnNameStr());
            sb.append("}");
        }
        //sb.append(")</foreach>");
        return sb.toString();

    }

    public String getBatchDelete(String tableName, String[] columnsList) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from  " + tableName + " where  " + columnsList[0] + " in ");

        sb.append("<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">\n");
        sb.append(" #{item}");
        sb.append("\n</foreach>");
        return sb.toString();

    }

    /**
     * delete
     *
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public String getDeleteSql(String tableName, String[] columnsList)
            throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("delete ");
        sb.append("\t from ").append(tableName).append(" where ");
        sb.append(columnsList[0]).append(" = #{").append("id")
                .append("}");
        return sb.toString();
    }

    /**
     * 根据id查询
     *
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public String getSelectByIdSql(String tableName, String[] columnsList)
            throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("select <include refid=\"Base_Column_List\" /> \n");
        sb.append("\t from ").append(tableName).append(" where ");
        sb.append(columnsList[0]).append(" = #{").append("id").append("}");
        return sb.toString();
    }

    /**
     * 获取所有的字段，并按","分割
     *
     * @param columns
     * @return
     * @throws SQLException
     */
    public String getColumnFields(String columns) throws SQLException {
        String fields = columns;
        if (fields != null && !"".equals(fields)) {
            fields = fields.replaceAll("[|]", ",");
        }
        return fields;
    }

    /**
     * @param columns
     * @return
     * @throws SQLException
     */
    public String[] getColumnList(String columns) throws SQLException {
        String[] columnList = columns.split("[|]");
        return columnList;
    }

    /**
     * 修改记录
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public String getUpdateSql(String tableName, List<ColumnData> columnList)
            throws SQLException {
        StringBuffer sb = new StringBuffer();

        for (int i = 1; i < columnList.size(); i++) {
            ColumnData data = columnList.get(i);
            if ("CREATETIME".equals(data.getColumnName().toUpperCase())) {
                continue;
            }
            if ("UPDATETIME".equals(data.getColumnName().toUpperCase())) {
                sb.append(data.getColumnName() + "=now()");
            } else {
                sb.append(data.getColumnName() + "=#{" + data.getColumnNameStr() + "}");
            }
            // 最后一个字段不需要","
            if ((i + 1) < columnList.size()) {
                sb.append(",");
            }
        }
        String update = "update " + tableName + " set " + sb.toString()
                + " where " + columnList.get(0).getColumnName() + "=#{id}";
        return update;
    }

    public String getUpdateSelectiveSql(String tableName,
                                        List<ColumnData> columnList) throws SQLException {
        StringBuffer sb = new StringBuffer();
        ColumnData cd = columnList.get(0); // 获取第一条记录，主键
        sb.append("\t<trim  suffixOverrides=\",\" >\n");
        for (int i = 1; i < columnList.size(); i++) {
            ColumnData data = columnList.get(i);
            String columnName = data.getColumnName();
            sb.append("\t<if test=\"").append(data.getColumnNameStr()).append(" != null ");
            // String 还要判断是否为空
            if ("String" == data.getDataType()) {
                sb.append(" and ").append(data.getColumnNameStr()).append(" != ''");
            }
            sb.append(" \">\n\t\t");
            sb.append(columnName + "=#{" + data.getColumnNameStr() + "}");
            // 最后一个字段不需要","
            if ((i + 1) < columnList.size()) {
                sb.append(",");
            }
            sb.append("\n\t</if>\n");
        }
        sb.append("\t</trim>");
        String update = "update " + tableName + " set \n" + sb.toString()
                + " where " + cd.getColumnName() + "=#{" + cd.getColumnNameStr() + "}";
        ;
        return update;
    }

    /**
     * <br>
     * <b>功能：</b>获取所有列名字<br>
     * <b>作者：</b><br>
     * <b>日期：</b> 2011-12-21 <br>
     *
     * @return
     * @throws SQLException
     */
    public String getColumnSplit(List<ColumnData> columnList)
            throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        for (ColumnData data : columnList) {
            commonColumns.append(data.getColumnName() + "|");
        }
        return commonColumns.delete(commonColumns.length() - 1,
                commonColumns.length()).toString();
    }

    /**
     * <br>
     * <b>功能：</b>获取所有列名字<br>
     * <b>作者：</b><br>
     * <b>日期：</b> 2011-12-21 <br>
     *
     * @return
     * @throws SQLException
     */
    public Map<String, String> getColumnSplits(List<ColumnData> columnList)
            throws SQLException {
        StringBuffer fields = new StringBuffer();
        StringBuffer fieldsPk = new StringBuffer();
        Map<String, String> strings = new HashMap<String, String>();
        for (ColumnData data : columnList) {
            fields.append(data.getColumnName() + "|");
            if ("true".equals(data.getIsPrimarykey())) {
                fieldsPk.append("id|");
            } else {
                fieldsPk.append(data.getColumnName() + "|");
            }
        }
        strings.put("fields", fields.delete(fields.length() - 1, fields.length()).toString());
        strings.put("fieldsPk", fieldsPk.delete(fieldsPk.length() - 1, fieldsPk.length()).toString());
        return strings;
    }

    public String getColumnStrSplit(List<ColumnData> columnList)
            throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        for (ColumnData data : columnList) {
            commonColumns.append(data.getColumnNameStr() + "|");
        }
        return commonColumns.delete(commonColumns.length() - 1,
                commonColumns.length()).toString();
    }

    public void createFiles(String codeName, String username, String tableName, String srcPath, String pckPath, String module
            , String projectName, String currentDate
    ) throws UnsupportedEncodingException, SQLException {
        codeName = new String(codeName.getBytes(), "utf-8");
        username = new String(username.getBytes(), "utf-8");
        //user_base_info
        String className = getTablesNameToClassName(tableName);
        //user
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
        //获得所有的表名 key 英文名 value 注释
        Map<String, String> tableMap = getTableNames();
        if (tableMap.containsKey(tableName.toLowerCase())) {
            codeName = tableMap.get(tableName.toLowerCase());
        }
        String[] modules = module.split("\\.");
        String modulePath = "";
        String modelPath_ = "";
        int i = 1;
        for (String v : modules) {
            if (i == 1) {
                modelPath_ = modelPath_ + v;
                modulePath = modulePath + v;
            } else {
                modelPath_ = modelPath_ + "\\" + v;
                modulePath = modulePath + "/" + v;
            }
            i++;
        }
        pckPath = pckPath + modelPath_ + "\\";

        // 页面路径，放到WEB-INF下面是为了不让手动输入路径访问jsp页面，起到安全作用
        String webPath = srcPath + "WebRoot\\WEB-INF\\jsp\\" + module + "\\";

        // java,xml文件名称
        String beanPath = "bean\\" + className + "Entity.java";
        String mapperPath = "mapper\\" + className + "Mapper.java";
        String sqlMapperPath = "mapper\\" + className + "Mapper.xml";

        VelocityContext context = new VelocityContext();
        context.put("projectName", projectName); //
        context.put("className", className); //
        context.put("lowerName", lowerName);
        context.put("codeName", codeName);
        context.put("tableName", tableName);
        context.put("module", module);
        context.put("username", username);
        context.put("currentDate", currentDate);
        context.put("modulePath", modulePath);

        /****************************** 生成bean字段 *********************************/
        try {
            context.put("feilds", getBeanFeilds(tableName)); // 生成bean
        } catch (Exception e) {
            e.printStackTrace();
        }

        /******************************* 生成sql语句 **********************************/
        try {
            Map<String, Object> sqlMap = getAutoCreateSql(tableName);
            context.put("columnDatas", getColumnDatas(tableName)); // 生成bean
            context.put("SQL", sqlMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //生成文件代码
        CommonPageParser.WriterPage(context, "TempBean.java", pckPath, beanPath);
        // 生成MybatisMapper接口
        CommonPageParser.WriterPage(context, "TempMapper.java", pckPath, mapperPath);
        // 生成Mybatis
        CommonPageParser.WriterPage(context, "TempMapper.xml", pckPath, sqlMapperPath);
    }
}
