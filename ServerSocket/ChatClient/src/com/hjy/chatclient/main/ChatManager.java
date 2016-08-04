package com.hjy.chatclient.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.hjy.chatclient.view.MainWIndow;


public class ChatManager {

	private ChatManager(){}
	private static final ChatManager instance = new ChatManager();
	public static ChatManager getCM() {
		return instance;
	}
	
	MainWIndow window;
	String IP;
	Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	
	public void setWindow(MainWIndow window) {
		this.window = window;
		window.appendText("文本框已经和ChatManager绑定了。");
	}
	//连接按钮
	public void connect(String ip) {
		this.IP = ip;
		//循环的从服务器读取数据
		new Thread(){

			@Override
			public void run() {
				try {
					socket = new Socket(IP, 12345);
					writer = new PrintWriter(
							new OutputStreamWriter(
									socket.getOutputStream()));
					
					reader = new BufferedReader(
							new InputStreamReader(
									socket.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						window.appendText("收到："+line);
					}
					//关闭流 
					writer.close();
					reader.close();
					writer = null;
					reader = null;
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	//发送按钮
	public void send(String out) {
		if (writer != null) {
			writer.write(out+"\n");
			writer.flush();
		}else {
			window.appendText("当前的链接已经中断");
		}
	}
}
