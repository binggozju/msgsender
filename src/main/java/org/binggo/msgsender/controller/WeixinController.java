package org.binggo.msgsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import org.binggo.msgsender.domain.Weixin;
import org.binggo.msgsender.service.WeixinSenderService;
import org.binggo.msgsender.utils.FeedBack;
import org.binggo.msgsender.utils.SendResult;

@RestController
@RequestMapping("/weixin")
public class WeixinController {
	private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);
	
	private WeixinSenderService weixinSenderService;
	
	@Autowired
	public void setWeixinSenderService(WeixinSenderService weixinSenderService) {
		this.weixinSenderService = weixinSenderService;
	}
	
	/**
	 * @param weixin
	 * @throws Exception
	 */
	@RequestMapping(value="/sync", method=RequestMethod.POST, consumes={"application/json"})
	public String handleSyncWeixin(@RequestBody Weixin weixin) throws Exception {
		logger.info("Receive a sync weixin request: " + weixin.toString());
		
		if (weixin.getReceivers() == null || weixin.getContent() == null) {
			logger.error("missing some fields in sync weixin request");
			return FeedBack.MISSING_FIELDS.toString();
		}
		
		SendResult ret = weixinSenderService.sendSyncMessage(weixin);
		return ret.convertToFeedBack().toString();
	}

	@RequestMapping(value="/async", method=RequestMethod.POST, consumes={"application/json"})
	public String handleAsyncWeixin(@RequestBody Weixin weixin) throws Exception {
		logger.info("Receive a async weixin request: " + weixin.toString());
		
		if (weixin.getReceivers() == null || weixin.getContent() == null) {
			logger.error("missing some fields in async weixin request");
			return FeedBack.MISSING_FIELDS.toString();
		}

		weixinSenderService.sendAsyncMessage(weixin);
		return FeedBack.OK.toString();
	}
	
}