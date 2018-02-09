package com.work.model.mina;

public class ClientTest {
	public static void main(String[] args) {
		try {
			String message = "<end>Hello World</end>";
			MinaClient client = new MinaClient("127.0.0.1", 8087, 3000,
					message, "GBK",Constants.RECIVE_MSG_END);
			String msgStr = client.getResultMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
