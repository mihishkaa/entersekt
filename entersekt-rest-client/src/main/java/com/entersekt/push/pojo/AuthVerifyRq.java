package com.entersekt.push.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthVerifyRq
{
    private String service_id;

    private String auth_id;

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}

	@Override
	public String toString() {
		return "AuthVerifyRq [service_id=" + service_id + ", auth_id=" + auth_id + "]";
	}

    
}