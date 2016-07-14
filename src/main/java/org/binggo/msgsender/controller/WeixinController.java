package org.binggo.msgsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import org.binggo.msgsender.domain.Weixin;
import org.binggo.msgsender.service.WeixinSenderService;
import org.binggo.msgsender.tools.FeedBack;
import org.binggo.msgsender.tools.SendResult;

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
	public @ResponseBody FeedBack handleSyncWeixin(@RequestBody Weixin weixin) throws Exception {
		logger.info("Receive a sync weixin request: " + weixin.toString());
		
		SendResult ret = weixinSenderService.sendSyncMessage(weixin);
		if (ret == SendResult.SUCCESS) {
			return new FeedBack(0, "ok");
		} else {
			return new FeedBack(1, "fail to send the weixin");
		}
		
	}

	@RequestMapping(value="/async", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody FeedBack handleAsyncWeixin(@RequestBody Weixin weixin) throws Exception {
		logger.info("Receive a async weixin request: " + weixin.toString());

		weixinSenderService.sendAsyncMessage(weixin);
		return new FeedBack(0, "ok");
	}
	
}