package com.work.model.zhima;

import com.antgroup.zmxy.openplatform.api.DefaultZhimaClient;
import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditAntifraudIntegrationQueryRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditScoreBriefGetRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditWatchlistBriefGetRequest;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditAntifraudIntegrationQueryResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditScoreBriefGetResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditWatchlistBriefGetResponse;

import java.util.Map;

/**
 * @ClassName: ZhimaCreditServerImpl 
 * @Description: 芝麻信用业务类
 * @author:	ganduo
 * @date: 2018-1-31 下午04:12:02  
 */
public class ZhimaCreditServerImpl implements ZhimaCreditServer {
	
	/**
	 * @throws ZhimaApiException
	 * @return 
	* @Description: 欺诈评分综合版
	* @param     
	* @return void
	* @throws
	 */
	@Override
	public ZhimaCreditAntifraudIntegrationQueryResponse zhimaCreditAntifraudIntegrationQuery(Map<String, String> requestParam) throws ZhimaApiException {
		ZhimaCreditAntifraudIntegrationQueryRequest req = new ZhimaCreditAntifraudIntegrationQueryRequest();
		req.setTransactionId(requestParam.get("transaction_id"));
        req.setChannel(requestParam.get("channel"));
        req.setPlatform(requestParam.get("platform"));
        req.setProductCode( requestParam.get("product_code")); 
        //业务参数
        req.setCertType(requestParam.get("cert_type"));
        req.setCertNo(requestParam.get("cert_no"));
        req.setName(requestParam.get("name"));
        req.setMobile(requestParam.get("mobile"));
        req.setEmail(requestParam.get("email")); 
        req.setBankCard(requestParam.get("bank_card")); 
        req.setAddress(requestParam.get("address"));
        req.setIp(requestParam.get("ip"));
        req.setMac(requestParam.get("mac")); 
        req.setWifimac(requestParam.get("wifimac")); 
        req.setImei(requestParam.get("imei"));

        String gatewayUrl = requestParam.get("gatewayUrl");
        String app_id = requestParam.get("app_id");
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, app_id,
        		ZM_PRIVATE_KEY, ZM_PUBLIC_KEY);
        try {
            ZhimaCreditAntifraudIntegrationQueryResponse response = client.execute(req);
            //芝麻信用服务端出现内部错误 ,进行重试
			if (response != null && ZM_UNKNOW_ERROR.equals(response.getErrorCode())){
	            response = client.execute(req);
			}
			return response;
        } catch (ZhimaApiException e) {
            throw e;
        }	
	}

	/**
	 * @throws ZhimaApiException
	* @Description: 芝麻信用评分(普惠版)
	* @param     
	* @return void
	* @throws
	 */
	@Override
	public ZhimaCreditScoreBriefGetResponse zhimaCreditScoreBriefGet(Map<String, String> requestParam) throws ZhimaApiException {
    	ZhimaCreditScoreBriefGetRequest req = new ZhimaCreditScoreBriefGetRequest();
    	req.setTransactionId(requestParam.get("transaction_id"));
    	req.setChannel(requestParam.get("channel"));
        req.setPlatform(requestParam.get("platform"));
        req.setProductCode( requestParam.get("product_code"));
        //业务参数
        req.setCertType(requestParam.get("cert_type"));
        req.setCertNo(requestParam.get("cert_no"));
        req.setName(requestParam.get("name"));
        req.setAdmittanceScore(Long.parseLong(requestParam.get("admittance_score")));
        
        String gatewayUrl = requestParam.get("gatewayUrl");
        String app_id = requestParam.get("app_id");
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, app_id,
        		ZM_PRIVATE_KEY, ZM_PUBLIC_KEY);
        
        try {
        	ZhimaCreditScoreBriefGetResponse response  = client.execute(req);
            //芝麻信用服务端出现内部错误 ,进行重试
			if (response != null && ZM_UNKNOW_ERROR.equals(response.getErrorCode())){
	            response = client.execute(req);
			}
			return response;
        } catch (ZhimaApiException e) {
            throw e;
        }
	}

	/**
	 * @throws ZhimaApiException
	 * @return 
	* @Description: 行业关注名单(普惠版)
	* @param     
	* @return void
	* @throws
	 */
	@Override
	public ZhimaCreditWatchlistBriefGetResponse zhimaCreditWatchlistBriefGet(Map<String, String> requestParam) throws ZhimaApiException {
		ZhimaCreditWatchlistBriefGetRequest req = new ZhimaCreditWatchlistBriefGetRequest();
		req.setTransactionId(requestParam.get("transaction_id"));
		req.setChannel(requestParam.get("channel"));
        req.setPlatform(requestParam.get("platform"));
        req.setProductCode( requestParam.get("product_code"));
        
        //业务参数
        req.setCertType(requestParam.get("cert_type"));
        req.setCertNo(requestParam.get("cert_no"));
        req.setName(requestParam.get("name"));
        
        String gatewayUrl = requestParam.get("gatewayUrl");
        String app_id = requestParam.get("app_id");
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, app_id,
        		ZM_PRIVATE_KEY, ZM_PUBLIC_KEY);
        
        try {
        	ZhimaCreditWatchlistBriefGetResponse response = client.execute(req);
            //芝麻信用服务端出现内部错误 ,进行重试
			if (response != null && ZM_UNKNOW_ERROR.equals(response.getErrorCode())){
	            response = client.execute(req);
			}
			return response;
        } catch (ZhimaApiException e) {
            throw e;
        }
	}
}
