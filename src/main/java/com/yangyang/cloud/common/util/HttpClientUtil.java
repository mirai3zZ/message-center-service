package com.yangyang.cloud.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 处理 HTTP 请求
 * @author daiyan
 */
public class HttpClientUtil {
	public static final String RESPONSE_CODE = "responseCode";
	public static final String RESPONSE_MESSAGE = "responseMessage";
	
	public static Map<String, Object> httpPut(String urlPath, Map<String, Object> requestParam) throws IOException {
		URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter pw = new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
        pw.write(objectMapper.writeValueAsString(requestParam));
        pw.flush();
        pw.close();

        String line = null;
        StringBuffer result = new StringBuffer();
        if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
        	BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        	while ((line = br.readLine()) != null) {
        		result.append(line + "\n");
        	}
        }
        connection.disconnect();
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(RESPONSE_CODE, connection.getResponseCode());
        resultMap.put(RESPONSE_MESSAGE, result);
        return resultMap;
	}
	
	/**
	 * just for test
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Map<String, Object> requestParam = new HashMap<String, Object>();
		requestParam.put("userId", "shaoao-123-crm-test-8888");
		requestParam.put("region", "cn-north-3");
		requestParam.put("productLine", "VPC");
		requestParam.put("productType", "VPC");
		requestParam.put("quotaType", "amount");
		requestParam.put("quotaKey", "vpc-one-subnet-num");
		requestParam.put("quota", "15");
		requestParam.put("unit", "个");
		
		String urlPath = "http://117.73.2.105:8083/crm/quota";
		Map<String, Object> resultMap = httpPut(urlPath, requestParam);
		
		System.out.println(resultMap.get(HttpClientUtil.RESPONSE_CODE));
		System.out.println(resultMap.get(HttpClientUtil.RESPONSE_MESSAGE));
	}
}
