package org.binggo.msgsender.service;

import java.io.IOException;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;

import org.binggo.msgsender.domain.Weixin;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

@Service
@Configurable
public class WeixinSenderService extends AbstractSender {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinSenderService.class);
	
	private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/"
			+ "gettoken";
	private static final String SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/"
			+ "message/send?access_token={0}";
	
	@Value("${weixin.corpid}")
	private String corpId;		// 企业id
	
	@Value("${weixin.corpsecret}")
	private String corpSecret;	// 管理组的凭证秘钥
	
	@Value("${weixin.agentid}")
	private String agentId;	// 企业应用的ID
	
	private HttpClient client;
	private JsonParser jsonParser;
	
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
			logger.error("fail to get access token: " + ex.getMessage());
			post.releaseConnection();
			return "";
		}
		
		try {
			JsonObject jsonObj = jsonParser.parse(response).getAsJsonObject();
			String accessToken = jsonObj.get("access_token").getAsString();
			post.releaseConnection();
			return accessToken;
		} catch (JsonSyntaxException ex) {
			logger.error("get an unvalid access token: " + ex.getMessage());
			post.releaseConnection();
			return "";
		}

	}
	
	private String sendMessage(String toUser, String toParty, String toTag, String content) {
		String accessToken = getAccessToken();
		if (accessToken.isEmpty()) {
			return "";
		}
		
		String url = String.format(SEND_MESSAGE_URL, accessToken);
		PostMethod post = new PostMethod(url);
		post.releaseConnection();
		post.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
			    
		NameValuePair[] param = {
				new NameValuePair("touser", toUser),
				new NameValuePair("toparty", toParty),
				new NameValuePair("totag", toTag),
				new NameValuePair("msgtype", "text"),
				new NameValuePair("agentid", agentId),
				new NameValuePair("text", String.format("{\"content\": %s}", content)),
				new NameValuePair("safe", "0")
		};
		post.setRequestBody(param);
	    
	    try {
	    	client.executeMethod(post);
	    	String response = new String(post.getResponseBodyAsString().getBytes("gbk"));
	    	post.releaseConnection();
	    	return response;
	    } catch (IOException ex) {
			logger.error("fail to post the message: " + ex.getMessage());
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
	
	
	private class SyncWeixinTask implements Callable<SendResult> {
		private Weixin weixin;
		
		public SyncWeixinTask(Weixin weixin) {
			this.weixin = weixin;
		}
			
		@Override
		public SendResult call() throws Exception {
			String toUser = weixin.getReceivers().replace(';', '|');
			String content = "";
			if (weixin.getSource() != null) {
				content = String.format("[%s] %s", weixin.getSource(), weixin.getContent());
			} else {
				content = weixin.getContent();
			}
			
			String response = sendMessage(toUser, "", "", content);
			if (response.isEmpty()) {
				logger.error("fail to send the sync weixin message");
				return SendResult.FAILURE;
			}
					
			try {
				JsonObject jsonObj = jsonParser.parse(response).getAsJsonObject();
				int errCode = jsonObj.get("errcode").getAsInt();
				if (errCode == 0) {
					return SendResult.OK;
				} else {
					return SendResult.FAILURE;
				}
			} catch (JsonSyntaxException ex) {
				logger.error("get an unvalid response of sending weixin message: " + ex.getMessage());
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
			String content = String.format("[%s] %s", weixin.getSource(), weixin.getContent());
			
			String response = sendMessage(toUser, "", "", content);
			if (response.isEmpty()) {
				logger.error("fail to send the async weixin message");
				return;
			}
			
			try {
				JsonObject jsonObj = jsonParser.parse(response).getAsJsonObject();
				int errCode = jsonObj.get("errcode").getAsInt();
				if (errCode != 0) {
					logger.error("fail to send the async weixin message");
				}
			} catch (JsonSyntaxException ex) {
				logger.error("get an unvalid response of sending weixin message: " + ex.getMessage());
			}
		}
	}

}
