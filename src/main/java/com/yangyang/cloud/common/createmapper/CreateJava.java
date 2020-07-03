package com.yangyang.cloud.common.createmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CreateJava {
	private static ResourceBundle res = ResourceBundle.getBundle("DataSourceConfig");
	private static String url = res.getString("gpt.url");
	private static String username = res.getString("gpt.username");
	private static String passWord = res.getString("gpt.password");

	public static void main(String[] args) throws IOException, SQLException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		CreateBean createBean = new CreateBean();
		createBean.setOracleInfo(url, username, passWord,"icp_flow");

		/* 此处修改成你的 表名 和 中文注释 */
        //工程名：opreation-service
		String projectName = "process-bss-service";
		// 模块包名：workorder
		String module = "activiti";
		// 开发人员名字
		String username = "zhanghuazong";
		String currentDate = df.format(new Date());
		//表名 user_base_info
		String tableName = "workflow_extend_act_model";
		// 表名中文注释
		String codeName = "备案用户表";
		// 根路径
		String srcPath ="E:\\src";
		// 包路径
		String pckPath = srcPath + "\\main\\java\\com\\inspur\\cloud\\bss\\";
		createBean.createFiles(codeName,username,tableName,srcPath,pckPath,module,projectName,currentDate);
	}

	/**
	 * 获取项目的路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		String rootPath = "";
		try {
			File file = new File(CommonPageParser.class.getResource("/").getFile());
			rootPath = file.getParentFile().getParentFile().getParent() + "\\";
			rootPath = java.net.URLDecoder.decode(rootPath, "utf-8");
			System.out.println(rootPath);
			return rootPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootPath;
	}
	
	private static String getInputPara() throws IOException {
		String module;
		BufferedReader strin2=new BufferedReader(new InputStreamReader(System.in));  
		module=strin2.readLine();
		return module;
	}
	
}
