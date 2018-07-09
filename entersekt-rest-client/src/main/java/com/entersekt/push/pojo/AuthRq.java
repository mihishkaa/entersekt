package com.entersekt.push.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRq
{
    private String service_id;

    private String username;

    private String simplify_response;

    private String subject_id;

    private Attributes attributes;

    private String pin_block_enabled;

    public String getService_id ()
    {
        return service_id;
    }

    public void setService_id (String service_id)
    {
        this.service_id = service_id;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getSimplify_response ()
    {
        return simplify_response;
    }

    public void setSimplify_response (String simplify_response)
    {
        this.simplify_response = simplify_response;
    }

    public String getSubject_id ()
    {
        return subject_id;
    }

    public void setSubject_id (String subject_id)
    {
        this.subject_id = subject_id;
    }

    public Attributes getAttributes ()
    {
        return attributes;
    }

    public void setAttributes (Attributes attributes)
    {
        this.attributes = attributes;
    }

    public String getPin_block_enabled ()
    {
        return pin_block_enabled;
    }

    public void setPin_block_enabled (String pin_block_enabled)
    {
        this.pin_block_enabled = pin_block_enabled;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [service_id = "+service_id+", username = "+username+", simplify_response = "+simplify_response+", subject_id = "+subject_id+", attributes = "+attributes+", pin_block_enabled = "+pin_block_enabled+"]";
    }
}