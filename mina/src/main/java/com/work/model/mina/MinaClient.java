package com.work.model.mina;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;



/**
 * @作者: ganduo
 * @日期: 2018/1/09
 * @描述: [MinaCreditClient]Socket客户端，使用MINA技术实现
 */
public class MinaClient extends IoHandlerAdapter implements IoHandler {
		private static final Logger logger = Logger.getLogger(MinaClient.class);
		private String ip; 
		private int port;
		private long timeout;
		private String charset;
		private final String message;
		private final String RECIVE_MSG_END;
		//创建客户端时在必须在构造函数中指定服务端ip,端口,超时时间,请求消息,字符集,报文返回结束标识
		public MinaClient(String ip, int port, long timeout,String message,String charset,String RECIVE_MSG_END){
			this.ip = ip;
			this.port = port;
			this.timeout = timeout;
			this.message = message;
			this.charset = charset;
			this.RECIVE_MSG_END = RECIVE_MSG_END;
			if(this.charset == null) this.charset = "UTF-8";
		}
		
		/***************************************************************************
		 *  <p>方法名称: request|描述: 向服务器发送请求并获取响应,超时将返回null</p>
		 * @param request 请求对象
		 * @return 
		 * @throws Exception 
		 * @throws Exception 
		 */
		public String getResultMessage() throws Exception{
			IoConnector connector = null;
			IoSession session = null;
				try {
					connector = new NioSocketConnector();
					//添加编码解码过滤器
					connector.getFilterChain().addLast("logger", new LoggingFilter());
					connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new SDInterfaceEncode(charset)));
					connector.setConnectTimeoutMillis(getTimeout());
					connector.setHandler(this);
					ConnectFuture connectFuture = connector.connect(
							new InetSocketAddress(getIp(),getPort()));
					connectFuture.awaitUninterruptibly(); 
					session = connectFuture.getSession();
					//超时时间等待: 等待其他线程处理消息, 如超时未处理完成,将返回null
					long future = System.currentTimeMillis() + timeout;
					long remaining = timeout;
					synchronized(MinaClient.class){
						while(session.getAttribute("resultMessage") == null && remaining > 0){
							MinaClient.class.wait(remaining);
							remaining = future - System.currentTimeMillis();
						}
						String resultMessage = (String) session.getAttribute("resultMessage");
						session.close(true);
						connector.dispose();
						return resultMessage;
					}
				} catch (Exception e) {
					throw e;
				} finally{//释放资源
					if(session != null && !session.isClosing()){
						session.close(true);
						session = null;
					}
					if(connector != null && !connector.isDisposed()){
						connector.dispose();
						connector = null;
					}
				}
		}
		
		/**
		 * session 本次连接会话
		 * message 客户端接受消息
		 * desc: 当服务端返回消息时触发
		 */
		@Override
	    public void messageReceived(IoSession session, Object message) throws Exception
	    {
				String msgStr = null;
				byte[] reciveMag = null;
				// 读取消息放入缓存
				if(null != session.getAttribute("reciveMag")){
					reciveMag = this.byteMerger((byte[])session.getAttribute("reciveMag"),(byte[])message);
					session.setAttribute("reciveMag",reciveMag);
				}else{
					session.setAttribute("reciveMag", (byte[]) message);
				}
				//获取返回消息
				msgStr = new String((byte[]) session.getAttribute("reciveMag"),charset);
				
				if(msgStr.length() > 0 && msgStr.contains(RECIVE_MSG_END)){
					//将消息织入到返回对象中
					synchronized(MinaClient.class){
						session.setAttribute("resultMessage", msgStr);
						MinaClient.class.notifyAll();
					}
					//logger.info("客户端接收到消息:" + msgStr);
					System.out.println("客户端接收到消息:" + msgStr);
				}
	    }
		
		/**
		 * session 本次连接会话
		 * Desc: 每次打开连接触发, 执行一些认证操作、发送数据等
		 */
	    @Override
	    public void sessionOpened(IoSession session) throws Exception
	    {
			String messageStr = message;
			byte [] message = messageStr.getBytes(charset);
			IoBuffer buffer = IoBuffer.allocate(message.length);
			buffer.put(message);
			buffer.flip();
			WriteFuture write = session.write(buffer);
	    }
	    
	    
	    /**
		 * session 本次连接会话
		 * message 发送消息
		 */
		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			//logger.info("客户端发送请求报文: " +  this.message);
			System.out.println("客户端发送请求报文: " +  this.message);
		}
		
		

		//当信息接收被关闭输入时触发次方法。
		@Override
	    public void inputClosed(IoSession session) throws Exception
	    {
	       // System.out.println("客户端关闭连接");
	    }

		@Override
		public void sessionCreated(IoSession session) throws Exception {
	        System.out.println("客户端与服务端IP：" + session.getRemoteAddress().toString() + " 创建连接");
		}

		/**
		 * byteMerger 将sission中的报文与新报文拼接成byte[]
		 * messageSission sission中的历史报文
		 * messageRecive  新接收的报文
		 */
		private static byte[] byteMerger(byte[] messageSission,byte[] messageRecive){
			byte[] message = new byte[messageSission.length + messageRecive.length];
			System.arraycopy(messageSission, 0, message, 0, messageSission.length);
			System.arraycopy(messageRecive, 0, message, messageSission.length, messageRecive.length);
			return message;
		}
		
		public String getCharset() {
			return charset;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}
		public String getMessage() {
			return message;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public long getTimeout() {
			return timeout;
		}
		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}
}
