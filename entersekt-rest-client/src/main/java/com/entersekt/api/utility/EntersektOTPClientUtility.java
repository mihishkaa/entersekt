package com.entersekt.api.utility;

import static com.entersekt.api.config.APIConfigConstants.APIPASSWORD;
import static com.entersekt.api.config.APIConfigConstants.APIUSER;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTOTPURL;
import static com.entersekt.api.config.APIConfigConstants.SERVICEID;

import java.util.Map;

import com.entersekt.push.pojo.VerifyOTPRq;
import com.entersekt.restlet.client.EntersektRestletClient;


public class EntersektOTPClientUtility {
	Map<String, String>  config;
	
	public EntersektOTPClientUtility(Map<String, String>  config) {
		this.config=config;
	}
	
	
	public EntersektRestletClient getOtpClient() {
		
		final String otpurl = config.get(ENDPOINTOTPURL);
		final String api_user = config.get(APIUSER);
		final String api_password = config.get(APIPASSWORD);
		return new  EntersektRestletClient(otpurl,api_user,api_password);
		
	}
	
	public VerifyOTPRq getOtpReuest(String username,String otp,String emcertID,String pin) {
		VerifyOTPRq otprq = new VerifyOTPRq();
		otprq.setOtp(otp);
		otprq.setUsername(username);
		otprq.setSubject_id(emcertID);
    	otprq.setPin(pin);
    	otprq.setService_id(config.get(SERVICEID));
    	return otprq;
		
	}
	

}
