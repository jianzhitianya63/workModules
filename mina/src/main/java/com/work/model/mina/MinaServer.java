package com.work.model.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
/**
 * @description: mina server
 * @date: 10:12 2018/2/9
 * @author : ganduo
 */
public class MinaServer extends IoHandlerAdapter implements IoHandler{	
		private static final Logger logger = Logger.getLogger(MinaServer.class);
		private final int PORT; 
		private final String charset;
		private final ServerHandler handler;
		private final String RECIVE_MSG_END;
		
		/*
		 * 参数: 端口, 字符集, 当服务端接收到消息的业务处理类
		 */
		public MinaServer(int port,String charset,ServerHandler handler,String RECIVE_MSG_END ){
			this.PORT = port;
			this.charset = charset;
			this.handler = handler;
			this.RECIVE_MSG_END = RECIVE_MSG_END;
		}
		
		public void start(){
			IoAcceptor acceptor = null;
			try {
				// 创建IoService
				acceptor = new NioSocketAcceptor();
				// 增加编码过滤器，统一编码UTF-8
				acceptor.getFilterChain().addLast("logger", new LoggingFilter());
				// 增加编码过滤器，统一编码UTF-8
				acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new SDInterfaceEncode(charset)));
				//创建缓存线程池,当超过线程池数量将自动扩展
				acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors
						.newCachedThreadPool()));
				acceptor.setHandler(this);
				// 设置报文读取缓存大小
				acceptor.getSessionConfig().setReadBufferSize(2048);
				// 设置指定类型的空闲时间，空闲时间超过这个值将触发sessionIdle方法
				acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
				// 启动服务
				acceptor.bind(new InetSocketAddress(PORT));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("服务器启动失败!");
				if(acceptor != null && !acceptor.isDisposed()){
					acceptor.dispose();
					acceptor = null;
				}
			}
		}
		
		//当接收到客户端的请求信息后触发此方法。
	    @Override
	    public void messageReceived(IoSession session, Object message) throws Exception
	    {
	    	try {
				String msgStr = null;
				byte[] reciveMag = null;
				// 读取消息放入缓存
				if(null != session.getAttribute("reciveMag")){
					reciveMag = this.byteMerger((byte[])session.getAttribute("reciveMag"),(byte[])message);
					session.setAttribute("reciveMag",reciveMag);
				}else{
					session.setAttribute("reciveMag", (byte[]) message);
				}
				msgStr = new String((byte[]) session.getAttribute("reciveMag"),charset);
				
				if(msgStr.length() > 0 && msgStr.contains(RECIVE_MSG_END)){
					logger.info("服务端接收到消息: " + msgStr);
					String responseStr = handler.handler(msgStr);
					session.setAttribute("responseMag", responseStr);
					byte[] bytes = responseStr.getBytes(charset);
					IoBuffer buf = IoBuffer.allocate(bytes.length);
					buf.put(bytes);
					buf.flip();
					org.apache.mina.core.future.WriteFuture write = session.write(buf);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("服务端接收报文失败");
			}
	    }
	 
	    //当信息已经发送至客户端后触发此方法。
	    @Override
	    public void messageSent(IoSession session, Object message) throws Exception
	    {	
	    	System.out.println("服务端发送信息:" + session.getAttribute("responseMag"));
	    }
	 
	    //当连接被关闭时触发，例如客户端程序意外退出等。
	    @Override
	    public void sessionClosed(IoSession session) throws Exception
	    {
	       // System.out.println("服务端关闭连接");
	    }
	    
	    //当一个新客户端连接后触发此方法。
	    @Override
	    public void sessionCreated(IoSession session) throws Exception
	    {
	        System.out.println("服务端与客户端IP：" + session.getRemoteAddress().toString() + " 创建连接");
	    }
	    
	    //当连接空闲时触发此方法。
	    @Override
	    public void sessionIdle(IoSession session, IdleStatus status) throws Exception
	    {
	        //System.out.println("server端闲置连接：会话 " + session.getId() + " 被触发 " + session.getIdleCount(status) + " 次");
	    }
	    
	    //当连接后打开时触发此方法，一般此方法与 sessionCreated 会被同时触发。 
	    @Override
	    public void sessionOpened(IoSession session) throws Exception
	    {
	        //System.out.println("server端打开连接");
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
}
