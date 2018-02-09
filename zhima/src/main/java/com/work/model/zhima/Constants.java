/**
 * 
 */
package com.work.model.zhima;

/**
 * 
 * <p>常量</p>
 * @ClassName: Constants
 * @Description: 常量
 * @author LiuCheng
 * @date 2017-2-22 下午5:46:35
 *
 */
public interface Constants {
	/****************************BEGIN:芝麻信用**************************************/
	//芝麻开放平台地址
	public static final String ZM_GATEWAY_URL = "https://zmopenapi.zmxy.com.cn/sandbox.do"; //测试地址
	//商户应用 Id
	public static final String ZM_APP_ID = "***";
	//商户 RSA 私钥
	public static final String ZM_PRIVATE_KEY = "***";
	//芝麻 RSA 公钥
	public static final String ZM_PUBLIC_KEY = "***";
	//芝麻信用评分(普惠版)产品码
	public static final String ZM_SCORE_PRODUCT_CODE = "w1010100000000002733";
	//行业关注名单(普惠版)产品码
	public static final String ZM_WATCH_LIST_CODE = "w1010100000000002977";
	//欺诈评分综合版 产品码
	public static final String ZM_ANTIFRAUD_INTEGRATION = "w1010100003000001533";
	//来源渠道
	public static final String ZM_CHANNEL = "apppc";
	//来源平台，默认为zmop
	public static final String ZM_PLATFORM = "zmop";
	//芝麻信用服务器异常错误码
	public static final String ZM_UNKNOW_ERROR = "ZMOP.unknow_error";
	/****************************END:芝麻信用**************************************/
}
