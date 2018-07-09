package com.entersekt.push.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupRq {
	
	private String username;
	private String subject_id;
	private String service_id;
	private Integer ttl_seconds;
	private Integer length;
	private String external_passkey;
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
	public Integer getTtl_seconds() {
		return ttl_seconds;
	}
	public void setTtl_seconds(Integer ttl_seconds) {
		this.ttl_seconds = ttl_seconds;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public String getExternal_passkey() {
		return external_passkey;
	}
	public void setExternal_passkey(String external_passkey) {
		this.external_passkey = external_passkey;
	}
	@Override
	public String toString() {
		return "SignupRq [username=" + username + ", subject_id=" + subject_id + ", service_id=" + service_id
				+ ", ttl_seconds=" + ttl_seconds + ", length=" + length + ", external_passkey=" + external_passkey
				+ "]";
	}
	
	

}
