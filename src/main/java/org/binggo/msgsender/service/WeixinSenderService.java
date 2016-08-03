package org.binggo.msgsender.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;

import org.binggo.msgsender.domain.Weixin;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.generate.mapper.WeixinRecordMapper;
import org.binggo.msgsender.generate.model.WeixinRecord;
import org.binggo.msgsender.utils.SendResult;

@Service
@Configurable
public class WeixinSenderService extends AbstractSender {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinSenderService.class);
	
	private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/"
			+ "gettoken";
	private static final String SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/"
			+ "message/send?access_token=%s";
	
	@Value("${weixin.corpid}")
	private String corpId;		// 企业id
	
	@Value("${weixin.corpsecret}")
	private String corpSecret;	// 管理组的凭证秘钥
	
	@Value("${weixin.agentid}")
	private String agentId;	// 企业应用的ID，一般使用“企业小助手”，对应的id为0
	
	private HttpClient client;
	private JsonParser jsonParser;
	
	@Autowired
	private WeixinRecordMapper weixinRecordMapper;
	
	public WeixinSenderService() {
		super("weixin-sender");
		client = new HttpClient();
		jsonParser = new JsonParser();
	}
	
	private String getAccessToken() {
		PostMethod post = new PostMethod(ACCESS_TOKEN_URL);
		post.releaseConnection();
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		
		logger.debug(String.format("corpid=%s, corpsecret=%s", corpId, corpSecret));
		NameValuePair[] param = {
				new NameValuePair("corpid", corpId),
				new NameValuePair("corpsecret", corpSecret)
				};
		post.setRequestBody(param);
		
		String response = "";
		try {
			client.executeMethod(post);
			response = new String(post.getResponseBodyAsString().getBytes("gbk"));
		} catch(IOException ex) {
			logger.error("execute post method to get access token failed: " + ex.getMessage());
			post.releaseConnection();
			return "";
		}
		
		try {
			JsonObject jsonObj = jsonParser.parse(response).getAsJsonObject();
			String accessToken = jsonObj.get("access_token").getAsString();
			post.releaseConnection();
			return accessToken;
		} catch (JsonSyntaxException ex) {
			logger.error("fail to parse http response of getting access token: " + ex.getMessage());
			post.releaseConnection();
			return "";
		}

	}
	
	private String sendMessage(String toUser, String toParty, String toTag, String content) {
		String accessToken = getAccessToken();
		if (accessToken.isEmpty()) {
			logger.error("fail to get the access token");
			return "";
		}
		logger.debug(String.format("Access Token: %s", accessToken));
		
		String url = String.format(SEND_MESSAGE_URL, accessToken);
		PostMethod post = new PostMethod(url);
		post.releaseConnection();
		post.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
        sb.append("\"touser\":" + "\"" + toUser + "\",");
        sb.append("\"toparty\":" + "\"" + toParty + "\",");
        sb.append("\"totag\":" + "\"" + toTag + "\",");
        sb.append("\"msgtype\":" + "\"text\",");
        sb.append("\"agentid\":" + agentId + ",");
        sb.append("\"text\": {\"content\":\"" + content + "\"},");
        sb.append("\"safe\":\"0\"");
        sb.append("}");
        logger.debug("the request body to be posted: " + sb.toString());
		
        InputStream stream;
		try {
			stream = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			post.releaseConnection();
			return "";
		}
		RequestEntity entity = new InputStreamRequestEntity(stream);
		post.setRequestEntity(entity);
		
	    try {
	    	client.executeMethod(post);
	    	
	    	String response = new String(post.getResponseBodyAsString().getBytes("gbk"));
	    	post.releaseConnection();
	    	logger.info("the response from weixin server: " + response);
	    	
	    	return response;
	    } catch (IOException ex) {
			logger.error("fail to send the weixin message: " + ex.getMessage());
			post.releaseConnection();
			return "";
	    }
	}

	@Override
	protected Callable<SendResult> getSyncTask(Message message) {
		if (message != null && message instanceof Weixin) {
			return new SyncWeixinTask((Weixin) message);
		} else {
			logger.error("unvalid weixin object");
			return null;
		}
	}

	@Override
	protected Runnable getAsyncTask(Message message) {
		if (message != null && message instanceof Weixin) {
			return new AsyncWeixinTask((Weixin) message);
		} else {
			logger.error("unvalid weixin object");
			return null;
		}
	}
	
	private static WeixinRecord generateWeixinRecord(Weixin weixin, int sendResult) {
		WeixinRecord record = new WeixinRecord();
		
		record.setReceivers(weixin.getReceivers());
		record.setContent(weixin.getContent());
		
		if (weixin.getSource() != null) {
			record.setSource(weixin.getSource());
		} else {
			record.setSource(SenderConstants.DEFAULT_SOURCE);
		}
		
		if (weixin.getSendTime() != 0) {
			record.setSendTime(weixin.getSendTime());
		} else {
			long nowTime = System.currentTimeMillis() / 1000;
			record.setSendTime((int) nowTime);
		}
		record.setSendResult((byte) sendResult);
		
		return record;
	}
	
	
	private class SyncWeixinTask implements Callable<SendResult> {
		private Weixin weixin;
		
		public SyncWeixinTask(Weixin weixin) {
			this.weixin = weixin;
		}
			
		@Override
		public SendResult call() throws Exception {
			// send the weixin message
			String toUser = weixin.getReceivers().replace(';', '|');
			String content = "";
			if (weixin.getSource() != null) {
				content = String.format("[%s] %s", weixin.getSource(), weixin.getContent());
			} else {
				content = String.format("[%s] %s", SenderConstants.DEFAULT_SOURCE, weixin.getContent());
			}
			
			String response = sendMessage(toUser, "", "", content);
			if (response.isEmpty()) {
				logger.error("fail to send the sync weixin message");
				//WeixinRecord record = generateWeixinRecord(weixin, SendResult.FAILURE.getCode());
				//weixinRecordMapper.insert(record);
				//logger.info("save the sync weixin successfully");
				
				return SendResult.FAILURE;
			}
					
			try {
				JsonObject jsonObj = jsonParser.parse(response).getAsJsonObject();
				int errCode = jsonObj.get("errcode").getAsInt();
				if (errCode == 0) {
					logger.info("send the sync weixin successfully");
					//WeixinRecord record = generateWeixinRecord(weixin, SendResult.OK.getCode());
					//weixinRecordMapper.insert(record);
					//logger.info("save the sync weixin successfully");
					
					return SendResult.OK;
				} else {
					logger.error("get an error response from weixin server");
					//WeixinRecord record = generateWeixinRecord(weixin, SendResult.FAILURE.getCode());
					//weixinRecordMapper.insert(record);
					//logger.info("save the sync weixin successfully");
					
					return SendResult.FAILURE;
				}
			} catch (JsonSyntaxException ex) {
				logger.error("get an unvalid response of sending weixin message: " + ex.getMessage());
				//WeixinRecord record = generateWeixinRecord(weixin, SendResult.FAILURE.getCode());
				//weixinRecordMapper.insert(record);
				//logger.info("save the sync weixin successfully");
				
				return SendResult.FAILURE;
			}
		}
	} 
	
	private class AsyncWeixinTask implements Runnable {
		private Weixin weixin;
		
		public AsyncWeixinTask(Weixin weixin) {
			this.weixin = weixin;
		}
		
		@Override
		public void run() {
			String toUser = weixin.getReceivers().replace(';', '|');
			String content = "";
			if (weixin.getSource() != null) {
				content = String.format("[%s] %s", weixin.getSource(), weixin.getContent());
			} else {
				content = String.format("[%s] %s", SenderConstants.DEFAULT_SOURCE, weixin.getContent());
			}
			
			String response = sendMessage(toUser, "", "", content);
			if (response.isEmpty()) {
				logger.error("fail to send the async weixin message");
				//WeixinRecord record = generateWeixinRecord(weixin, SendResult.FAILURE.getCode());
				//weixinRecordMapper.insert(record);
				//logger.info("save the async weixin message successfully");
				
				
				
				return;
			}
			
			try {
				JsonObject jsonObj = jsonParser.parse(response).getAsJsonObject();
				int errCode = jsonObj.get("errcode").getAsInt();
				if (errCode != 0) {
					logger.error("fail to send the async weixin message");
					
					//WeixinRecord record = generateWeixinRecord(weixin, SendResult.FAILURE.getCode());
					//weixinRecordMapper.insert(record);
					//logger.info("save the async weixin message successfully");
				} else {
					//WeixinRecord record = generateWeixinRecord(weixin, SendResult.OK.getCode());
					//weixinRecordMapper.insert(record);
					//logger.info("save the async weixin message successfully");
				}
				
			} catch (JsonSyntaxException ex) {
				logger.error("get an unvalid response of sending weixin message: " + ex.getMessage());
				
				//WeixinRecord record = generateWeixinRecord(weixin, SendResult.FAILURE.getCode());
				//weixinRecordMapper.insert(record);
				//logger.info("save the async weixin message successfully");
			}
		}
	}

}
