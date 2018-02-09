package com.work.model.zhima;

import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditAntifraudIntegrationQueryResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditScoreBriefGetResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditWatchlistBriefGetResponse;

import java.util.Map;

/**
* @ClassName: ZhimaCreditServer
* @Description: TODO
* @author:	ganduo
* @date: 2018-1-31 下午04:06:30
*/
public interface ZhimaCreditServer extends Constants{
   /**
    * @param requestParam
   * @Description: 芝麻信用评分(普惠版)
   * @param  requestParam 请求参数
   * @return void
   * @throws
    */
   public ZhimaCreditScoreBriefGetResponse zhimaCreditScoreBriefGet(Map<String, String> requestParam) throws ZhimaApiException;

   /**
   * @Description: 欺诈评分综合版
   * @param  requestParam 请求参数
   * @return void
   * @throws
    */
   public ZhimaCreditAntifraudIntegrationQueryResponse zhimaCreditAntifraudIntegrationQuery(Map<String, String> requestParam) throws ZhimaApiException;

   /**
   * @Description: 行业关注名单(普惠版)
   * @param  requestParam 请求参数
   * @return void
   * @throws
    */
   public ZhimaCreditWatchlistBriefGetResponse zhimaCreditWatchlistBriefGet(Map<String, String> requestParam) throws ZhimaApiException;
}
