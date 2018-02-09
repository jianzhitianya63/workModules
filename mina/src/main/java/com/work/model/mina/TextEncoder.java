package com.work.model.mina;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
/**
 * @description: 编码器,消息发出之前触发
 * @date: 10:04 2018/2/9
 * @author ganduo
 */
public class TextEncoder implements ProtocolEncoder {
	private String charset = "UTF-8";

	@Override
	public void encode(IoSession arg0, Object message, ProtocolEncoderOutput out)
			throws Exception {
		   /*out.write(message);
	        out.flush();*/
		Charset character = Charset.forName(charset); 

	    IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);  
        
        CharsetEncoder ce = character.newEncoder();  
          
        buf.putString((String)message, ce);  
          
        buf.flip();  
          
        out.write(buf);  
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public TextEncoder(String charset) {
		super();
		this.charset = charset;
	}
	
	public TextEncoder() {
		super();
	}

}
