package com.entersekt.api.utility;

import static com.entersekt.api.config.APIConfigConstants.APIPASSWORD;
import static com.entersekt.api.config.APIConfigConstants.APIUSER;
import static com.entersekt.api.config.APIConfigConstants.EXTERNALPASSKEY;
import static com.entersekt.api.config.APIConfigConstants.LENGTH;
import static com.entersekt.api.config.APIConfigConstants.SERVICEID;
import static com.entersekt.api.config.APIConfigConstants.SIGNUPEPURL;
import static com.entersekt.api.config.APIConfigConstants.SIGNUPPOLLURL;
import static com.entersekt.api.config.APIConfigConstants.SUBJECTID;
import static com.entersekt.api.config.APIConfigConstants.TTLSECONDS;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.entersekt.push.pojo.PollSignupRq;
import com.entersekt.push.pojo.SignupRq;
import com.entersekt.restlet.client.EntersektRestletClient;


public class EntersektSignUpClientUtility {
	Map<String, String>  config;
	
	public EntersektSignUpClientUtility(Map<String, String>  config) {
		this.config=config;
	}
	
	
	public List<EntersektRestletClient> getSignupClient() {
		List<EntersektRestletClient> rc = new ArrayList<EntersektRestletClient>();
		final String su_uri = config.get(SIGNUPEPURL);
		final String sup_uri = config.get(SIGNUPPOLLURL);
		final String api_user = config.get(APIUSER);
		final String api_password = config.get(APIPASSWORD);
		rc.add(new  EntersektRestletClient(su_uri,api_user,api_password));
		rc.add(new  EntersektRestletClient(sup_uri,api_user,api_password));
		return rc;
	}
	
	public SignupRq getSignupReuest(String username,String subjectid) {
		SignupRq request = new SignupRq();
		request.setUsername(username);
		request.setSubject_id(subjectid);
		request.setService_id(config.get(SERVICEID));
		request.setTtl_seconds(Integer.valueOf(config.get(TTLSECONDS)));
		request.setLength(Integer.valueOf(config.get(LENGTH)));
		request.setExternal_passkey(config.get(EXTERNALPASSKEY));
		return request;
		
	}
	public PollSignupRq getPollSignupReuest(String username,String subjectid) {
	PollSignupRq psq = new PollSignupRq();
	psq.setUsername(username);
	psq.setSubject_id(subjectid);
	psq.setService_id(config.get(SERVICEID));
	return psq;
	}

}
