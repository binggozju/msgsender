package org.binggo.msgsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import org.binggo.msgsender.domain.Sms;
import org.binggo.msgsender.domain.Weixin;
import org.binggo.msgsender.service.SmsSenderService;
import org.binggo.msgsender.tools.FeedBack;
import org.binggo.msgsender.tools.SendResult;

@RestController
@RequestMapping("/sms")
public class SmsController {
	private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
	
	private SmsSenderService smsSenderService;
	
	@Autowired
	public void setSmsSenderService(SmsSenderService smsSenderService) {
		this.smsSenderService = smsSenderService;
	}
	
	/**
	 * @param sms
	 * @throws Exception
	 */
	@RequestMapping(value="/sync", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody FeedBack handleSyncSms(@RequestBody Sms sms) throws Exception {
		logger.info("Receive a sync sms request: " + sms.toString());
		
		SendResult ret = smsSenderService.sendSyncMessage(sms);
		if (ret == SendResult.SUCCESS) {
			return new FeedBack(0, "ok");
		} else {
			return new FeedBack(1, "fail to send the sms");
		}
	}

	@RequestMapping(value="/async", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody FeedBack handleAsyncSms(@RequestBody Sms sms) throws Exception {
		logger.info("Receive a async sms request: " + sms.toString());

		smsSenderService.sendAsyncMessage(sms);
		return new FeedBack(0, "ok");
	}
	
}
