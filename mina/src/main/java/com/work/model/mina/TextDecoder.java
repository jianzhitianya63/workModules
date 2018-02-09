package com.work.model.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
/**
 * @description: 解码器,接收到服务端消息后触发
 * @date: 10:04 2018/2/9
 * @author ganduo
 */
public class TextDecoder implements ProtocolDecoder{
	private String charser = "UTF-8";
	
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput output)
			throws Exception {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);          
        int n = in.limit();   
        StringBuffer sb = new StringBuffer(512);   
        byte [] sizeBytes = null;   
        if(in.hasRemaining()){     
           sizeBytes = new byte[n];   
            in.get(sizeBytes);   
        }     
        output.write(sizeBytes);  
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {
		
	}
	
	public TextDecoder() {
		super();
	}
	public TextDecoder(String charser) {
		super();
		this.charser = charser;
	}
}
