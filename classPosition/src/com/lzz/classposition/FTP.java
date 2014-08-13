package com.lzz.classposition;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTP {
	public FTPClient ftp;

	public FTP(boolean isPrintCommmand){
	ftp = new FTPClient();
		if(isPrintCommmand){
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		} 
	}

	//登录
	public boolean login(String host,int port,String username,String password) throws IOException{
		this.ftp.connect(host,port);
		if(FTPReply.isPositiveCompletion(this.ftp.getReplyCode())) {
			if(this.ftp.login(username,password)) { 
				this.ftp.setControlEncoding("GBK");
				return true; 
			} 
		} 
		if(this.ftp.isConnected()) {
			this.ftp.disconnect();
		} 
		return false;
	}

	//关闭连接
	public void disConnection() throws IOException
	{
		if(this.ftp.isConnected()) {
			this.ftp.disconnect(); 
		} 
	}

	//通过路径获得路径下所有文件 输出文件名 
	public List<String[]> ListFile(String pathName) throws IOException{
		int first,last;
		List<String[]> params = new ArrayList<String[]>();
		if(pathName.startsWith("/")&&pathName.endsWith("/")){
			String directory = pathName;
			this.ftp.changeWorkingDirectory(directory);
			FTPFile[] files = this.ftp.listFiles();
			for(int i=0;i<files.length;i++){
				if(!files[i].isDirectory()) {
					String[] aFile = new String[3];
					String name = files[i].getName();
					first = name.indexOf(".");
					last = name.lastIndexOf(".");
					aFile[0] = name.substring(0, first);
					aFile[1] = name.substring(first+1, last);
					aFile[2] = String.valueOf(files[i].getTimestamp().getTimeInMillis()/1000);
					System.out.println(first);
					params.add(aFile);
				}
			} 
		}
		return params; 
	}

	public static void main(String[] args) throws IOException {
		FTP f = new FTP(false);
		if(f.login("bngsn.bob.buttobi.net",21, "bngsn","911002")){
			f.ListFile("/pos/"); //地址，端口号，用户名，密码
		}
		f.disConnection(); 
	} 
}
