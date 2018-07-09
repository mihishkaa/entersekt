package com.entersekt.push.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PollSignupRq {
	
	private String username;
	private String subject_id;
	private String service_id;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	@Override
	public String toString() {
		return "PollSignupRq [username=" + username + ", subject_id=" + subject_id + ", service_id=" + service_id + "]";
	}
	
	
	

}
