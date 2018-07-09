package com.entersekt.restlet.client;

import java.io.IOException;

import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entersekt.push.pojo.Attributes;
import com.entersekt.push.pojo.AuthRq;
import com.entersekt.push.pojo.AuthVerifyResp;
import com.entersekt.push.pojo.AuthVerifyRq;
import com.entersekt.push.pojo.Buttons;
import com.entersekt.push.pojo.Multifactor;
import com.entersekt.push.pojo.PollSignupRq;
import com.entersekt.push.pojo.SignupRq;
import com.entersekt.push.pojo.Text_boxes;
import com.entersekt.push.pojo.Value_pairs;
import com.entersekt.push.pojo.VerifyOTPRq;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EntersektRestletClient{
	
	 private final Logger logger = LoggerFactory.getLogger("entersektAuth");
	  ClientResource cr = null;
	  
	  public EntersektRestletClient(String url,String api_user,String api_password) {
		  cr = new ClientResource(url);
		  cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC,api_user, api_password);  
		  cr.setMethod(Method.POST);
	  }
	  
	  public static void main(String[] args) {
		  EntersektRestletClient rc = new EntersektRestletClient("https://sgpi.sg.entersekt.com/api/rest/sgpi/1.0/auth", "sacumen", "jqereknseCwkVgcnjy9ggpw2");
		  AuthRq auth = new AuthRq();
	    	Attributes atts = new Attributes();
	    	Buttons buts[] = new Buttons[1];
	    	Multifactor mfact = new Multifactor();
	    	Text_boxes tbs[] = new Text_boxes[1];
	    	Value_pairs vps []= new Value_pairs[1];
	    	
	    	
	    	
	    	auth.setService_id("02314844-8808-4122-a908-d25b0c39aa34");
	    	auth.setUsername("mkathi");
	    	auth.setSubject_id("145418044");
	    	auth.setPin_block_enabled("false");
	    	auth.setSimplify_response("true");
	    	
	    	atts.setPush_notify("true");
	    	atts.setText("Do you want to make a payment?");
	    	atts.setTitle("Confirm Payment");
	    	atts.setTtl_seconds("60");
	    	atts.setPositive_button("Yes");
	    	atts.setNegative_button("No");
	    	
	    	
	    	Value_pairs vp = new Value_pairs();
	    	vp.setName("Account");
	    	vp.setValue("20331738923");
	    	
	    	vps[0]=vp;
	    	
	    	
	    	Text_boxes tb = new Text_boxes();
	    	tb.setConstraints("5");
	    	tb.setLabel("Amount");
	    	tb.setMax_size("10");
	    	tb.setMin_size("1");
	    	tb.setText("0.00");
	    	tbs[0]=tb;
	    	
	    	Buttons but = new Buttons();
	    	but.setLabel("Cancel");
	    	but.setRole("NEUTRAL");
	    	String flag[] = {new String("pin")};
	    	but.setFlags(flag);
	    	buts[0]=but;
	    	atts.setButtons(buts);
	    	
	    	mfact.setDevice_type("iOS");
	    	mfact.setTry_suppress_transakt_popup("true");
	    	atts.setValue_pairs(vps);
	    	//atts.setMultifactor(mfact);
	    	atts.setText_boxes(tbs);
	    	auth.setAttributes(atts);
		  
	    String auth_id =	rc.pushNotify(auth);
	    AuthVerifyRq vrq = new AuthVerifyRq();
	    vrq.setAuth_id("bf341ea5-aca0-4716-9140-8cae7cf74e95");
	    vrq.setService_id("02314844-8808-4122-a908-d25b0c39aa34");
	    EntersektRestletClient vrc = new EntersektRestletClient("https://sgpi.sg.entersekt.com/api/rest/sgpi/1.0/pollAuthJson", "sacumen", "jqereknseCwkVgcnjy9ggpw2");
	    System.out.println("response"+vrc.validatePushNotify(vrq));
	}
	  
	
	public String pushNotify(AuthRq auth) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String req = mapper.writeValueAsString(auth);
			logger.debug("pushNotify Request:  "+ req);
			System.out.println("pushNotify Request:  "+ req);
			
			Representation rep = cr.post(new JsonRepresentation(req));
			Response resp = cr.getResponse();
			String jsonResponse = "";
			if (resp.getStatus().isSuccess()) {

				jsonResponse = rep.getText();
				logger.debug("pushNotify Response: "+ jsonResponse);
				System.out.println("pushNotify Response: "+ jsonResponse);
				return jsonResponse;

			} else {
				logger.debug(resp.getStatus() + "");
			}

		} catch (IOException e) {
			logger.error("IOException: {}", e.getMessage());
			System.out.println("IOException: {}"+ e.getMessage());
		}
		return "";
	}
	
	public AuthVerifyResp validatePushNotify(AuthVerifyRq request) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String req = mapper.writeValueAsString(request);
			logger.debug("validatePushNotify Request:"+ req);
			System.out.println("validatePushNotify Request:"+ req);
			
			Representation rep = cr.post(new JsonRepresentation(req));
			Response resp = cr.getResponse();
			String jsonResponse = "";
			if (resp.getStatus().isSuccess()) {

				jsonResponse = rep.getText();
				logger.debug(jsonResponse);
				
				if(!jsonResponse.isEmpty()) {
					
					AuthVerifyResp authresp = mapper.readValue(jsonResponse, AuthVerifyResp.class);
					logger.debug("validatePushNotify Resonse:"+ authresp);
				    System.out.println("validatePushNotify Resonse:"+ authresp);
					return authresp;
				}
				
				

			} else {
				logger.error(resp.getStatus() + "");
				System.out.println(resp.getStatus() + "");
			}

		} catch (IOException e) {
			logger.error("IOException: {}", e.getMessage());
			System.out.println("IOException: {}"+ e.getMessage());
		}
		return new AuthVerifyResp();
	}
	
	public String getSignUpCode(SignupRq request) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String req = mapper.writeValueAsString(request);
			logger.debug("getSignUpCode Request:"+ req);
			System.out.println("getSignUpCode Request:"+ req);

			Representation rep = cr.post(new JsonRepresentation(req));
			Response resp = cr.getResponse();
			String jsonResponse = "";
			if (resp.getStatus().isSuccess()) {

				jsonResponse = rep.getText();
				logger.debug("json response for getSignUpCode",jsonResponse);
				
				if(!jsonResponse.isEmpty()) {
					logger.debug("getSignUpCode Response:"+ jsonResponse);
					System.out.println("getSignUpCode Response:"+ jsonResponse);
					//AuthVerifyResp authresp = mapper.readValue(jsonResponse, AuthVerifyResp.class);
					return jsonResponse;
				}
				
				

			} else {
				logger.debug(resp.getStatus() + "");
				System.out.println(resp.getStatus() + "");
			}

		} catch (IOException e) {
			logger.error("IOException: {}", e.getMessage());
			System.out.println("IOException: {}"+ e.getMessage());
		}
		return "";
	}
	
	
	public String pollSignup(PollSignupRq request) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String req = mapper.writeValueAsString(request);
			logger.debug("pollSignup Request:"+ req);
			System.out.println("pollSignup Request:"+ req);
			
			Representation rep = cr.post(new JsonRepresentation(req));
			Response resp = cr.getResponse();
			String jsonResponse = "";
			if (resp.getStatus().isSuccess()) {

				jsonResponse = rep.getText();
				logger.debug(jsonResponse);
				
				if(!jsonResponse.isEmpty()) {
					logger.debug("pollSignup Response:"+ jsonResponse);
					System.out.println("pollSignup Response:"+ jsonResponse);
					//AuthVerifyResp authresp = mapper.readValue(jsonResponse, AuthVerifyResp.class);
					return jsonResponse;
				}
				
				

			} else {
				logger.debug(resp.getStatus() + "");
			}

		} catch (IOException e) {
			logger.error("IOException: {}", e.getMessage());
			System.out.println("IOException: {}"+ e.getMessage());
		}
		return "";
	}
	
	public String verifyOTP(VerifyOTPRq request) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String req = mapper.writeValueAsString(request);
			logger.debug("verifyOTP Request:"+ req);
			System.out.println("verifyOTP Request:"+ req);
			Representation rep = cr.post(new JsonRepresentation(req));
			Response resp = cr.getResponse();
			String jsonResponse = "";
			if (resp.getStatus().isSuccess()) {

				jsonResponse = rep.getText();
				logger.debug(jsonResponse);
				
				if(!jsonResponse.isEmpty()) {
					logger.debug("verifyOTP Response:"+ jsonResponse);
					System.out.println("verifyOTP Response:"+ jsonResponse);
					//AuthVerifyResp authresp = mapper.readValue(jsonResponse, AuthVerifyResp.class);
					return jsonResponse;
				}
				
				

			} else {
				logger.debug(resp.getStatus() + "");
			}

		} catch (IOException e) {
			logger.error("IOException: {}", e.getMessage());
			System.out.println("IOException: {}"+ e.getMessage());
		}
		return "";
	}
	
	
	
}