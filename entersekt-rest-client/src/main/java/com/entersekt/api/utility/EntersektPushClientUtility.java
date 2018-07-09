package com.entersekt.api.utility;

import static com.entersekt.api.config.APIConfigConstants.SERVICEID;
import static com.entersekt.api.config.APIConfigConstants.TTLSECONDS;
import static com.entersekt.api.config.APIConfigConstants.TEXT;
import static com.entersekt.api.config.APIConfigConstants.TITLE;
import static com.entersekt.api.config.APIConfigConstants.NEGATIVEBUTTON;
import static com.entersekt.api.config.APIConfigConstants.POSITIVEBUTTON;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTAUTHURL;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTAUTHPOLLURL;
import static com.entersekt.api.config.APIConfigConstants.APIUSER;
import static com.entersekt.api.config.APIConfigConstants.APIPASSWORD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.entersekt.push.pojo.Attributes;
import com.entersekt.push.pojo.AuthRq;
import com.entersekt.push.pojo.AuthVerifyRq;
import com.entersekt.push.pojo.Buttons;
import com.entersekt.push.pojo.Multifactor;
import com.entersekt.push.pojo.Text_boxes;
import com.entersekt.push.pojo.Value_pairs;
import com.entersekt.restlet.client.EntersektRestletClient;


public class EntersektPushClientUtility {
	Map<String, String>  config;
	
	public EntersektPushClientUtility(Map<String, String>  config) {
		this.config=config;
	}
	
	
	public List<EntersektRestletClient> getPushClient() {
		List<EntersektRestletClient> rc = new ArrayList<EntersektRestletClient>();
		    final String uri = config.get(ENDPOINTAUTHURL);
	        final String vuri = config.get(ENDPOINTAUTHPOLLURL);
			final String api_user = config.get(APIUSER);
			final String api_password = config.get(APIPASSWORD);
			rc.add(new EntersektRestletClient(uri,api_user,api_password));
			rc.add(new EntersektRestletClient(vuri,api_user,api_password));
		return rc;
	}
	
	public EntersektRestletClient getPushClient(String url) {

		final String api_user = config.get(APIUSER);
		final String api_password = config.get(APIPASSWORD);

		return new EntersektRestletClient(url, api_user, api_password);
	}
	
	public AuthRq getSignupReuest(String username,String emcertID) {
		AuthRq auth = new AuthRq();
    	Attributes atts = new Attributes();
    	Buttons buts[] = new Buttons[1];
    	Multifactor mfact = new Multifactor();
    	Text_boxes tbs[] = new Text_boxes[1];
    	Value_pairs vps []= new Value_pairs[1];
    	
    	
    	
    	auth.setService_id(config.get(SERVICEID));
    	auth.setUsername(username);
    	auth.setSubject_id(emcertID);
    	auth.setPin_block_enabled("false");
    	auth.setSimplify_response("true");
    	
    	atts.setPush_notify("true");
    	atts.setText(config.get(TEXT));
    	atts.setTitle(config.get(TITLE));
    	atts.setTtl_seconds(config.get(TTLSECONDS));
    	atts.setPositive_button(config.get(POSITIVEBUTTON));
    	atts.setNegative_button(config.get(NEGATIVEBUTTON));
    	
    	
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
		return auth;
		
	}
	public AuthVerifyRq getPollAuthReuest(String authid) {
		AuthVerifyRq avrq = new AuthVerifyRq();
		avrq.setAuth_id(authid);
		avrq.setService_id(config.get(SERVICEID));
	return avrq;
	}

}
