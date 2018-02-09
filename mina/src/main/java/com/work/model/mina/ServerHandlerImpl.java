package com.work.model.mina;

public class ServerHandlerImpl implements ServerHandler{
	
	@Override
	public String handler(String message) {
		System.out.println("服务端接收到的消息: " + message);
		String responseStr = "<end>Server response!!!</end>";
		return responseStr;
	}

}
