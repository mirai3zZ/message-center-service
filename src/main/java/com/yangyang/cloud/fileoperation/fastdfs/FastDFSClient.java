package com.yangyang.cloud.fileoperation.fastdfs;

import com.yangyang.cloud.OperationServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class FastDFSClient {

	private static String domain;

	@Value("${tracker.server.domain}")
	public void setDomainName(String domain) {
		FastDFSClient.domain = domain;
	}

	static {
		try {
			String filePath = "";
			if(System.getProperty("os.name").toLowerCase().startsWith("win")){
				 filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
			}else{
				filePath= new ApplicationHome(OperationServiceApplication.class).getSource().getParentFile().getPath()+"/fdfs_client.conf";
			}
			log.info("fdfs_client filePath {}", filePath);

			ClientGlobal.init( filePath);
		} catch (Exception e) {
			log.error("FastDFS Client Init Fail!",e);
		}
	}

	public static String[] upload(FastDFSFile file) {
		log.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);

		NameValuePair[] meta_list = new NameValuePair[1];
		meta_list[0] = new NameValuePair("author", file.getAuthor());

		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		StorageClient storageClient=null;
		try {
			storageClient = getTrackerClient();
			//content 字节流 后缀 meta_lsit 作者信息
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
		} catch (IOException e) {
			log.error("IO Exception when uploadind the file:" + file.getName(), e);
		} catch (Exception e) {
			log.error("Non IO Exception when uploadind the file:" + file.getName(), e);
		}
		log.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

		if (uploadResults == null && storageClient!=null) {
			log.error("upload file fail, error code:" + storageClient.getErrorCode());
		}else{
			String groupName = uploadResults[0];
			String remoteFileName = uploadResults[1];
			log.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
		}
		return uploadResults;
	}

	public static FileInfo getFile(String groupName, String remoteFileName) {
		try {
			StorageClient storageClient = getTrackerClient();
			return storageClient.get_file_info(groupName, remoteFileName);
		} catch (IOException e) {
			log.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			log.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public static InputStream downFile(String groupName, String remoteFileName) {
		try {
			StorageClient storageClient = getTrackerClient();
			byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
			InputStream ins = new ByteArrayInputStream(fileByte);
			return ins;
		} catch (IOException e) {
			log.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			log.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public static int deleteFile(String groupName, String remoteFileName)
			throws Exception {
		StorageClient storageClient = getTrackerClient();
		int i = storageClient.delete_file(groupName, remoteFileName);
		log.info("delete file successfully!!!" + i);
        return i;
	}

	public static StorageServer[] getStoreStorages(String groupName)
			throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerClient.getStoreStorages(trackerServer, groupName);
	}

	public static ServerInfo[] getFetchStorages(String groupName,
												String remoteFileName) throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
	}

	public static String getTrackerUrl() throws IOException {
		//return "http://"+getTrackerServer().getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port()+"/";
		return domain+"/";
	}


	private static StorageClient getTrackerClient() throws IOException {
		TrackerServer trackerServer = getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		return  storageClient;
	}

	private static TrackerServer getTrackerServer() throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return  trackerServer;
	}
}