package com.entersekt.push.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthVerifyResp
{
    private String service_id;

    private String auth_id;
    
    private String status;
    
    private String subject_reply;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubject_reply() {
		return subject_reply;
	}

	public void setSubject_reply(String subject_reply) {
		this.subject_reply = subject_reply;
	}

	@Override
	public String toString() {
		return "AuthVerifyResp [service_id=" + service_id + ", auth_id=" + auth_id + ", status=" + status
				+ ", subject_reply=" + subject_reply + "]";
	}

	

	

    
}