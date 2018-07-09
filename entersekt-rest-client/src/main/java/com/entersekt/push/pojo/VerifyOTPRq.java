package com.entersekt.push.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyOTPRq {
	
	private String username;
	private String subject_id;
	private String service_id;
	private Integer ttl_seconds;
	private String otp;
	private String pin;
	
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
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	@Override
	public String toString() {
		return "SignupRq [username=" + username + ", subject_id=" + subject_id + ", service_id=" + service_id
				+ ", ttl_seconds=" + ttl_seconds + ", otp=" + otp + ", pin=" + pin + "]";
	}
	
	
	

}
