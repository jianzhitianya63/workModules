package com.work.model.mina;



/**
 * @description: Mina 自定义编码过滤器
 * @date: 10:15 2018/2/9
 * @author: ganduo
 */
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SDInterfaceEncode implements  ProtocolCodecFactory{
	private String charset = "UTF-8";
	/**
	 * @description: 解码
	 * @date: 10:15 2018/2/9
	 * @param session
	 * @return:
	 */
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
			return new TextDecoder(charset);
	}

	/**
	 * @description: 编码
	 * @date: 10:15 2018/2/9
	 * @param session
	 * @return:
	 */
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return new TextEncoder(charset);
	}

	public SDInterfaceEncode() {
		super();
	}
	public SDInterfaceEncode(String charset) {
		super();
		this.charset = charset;
	}
}
