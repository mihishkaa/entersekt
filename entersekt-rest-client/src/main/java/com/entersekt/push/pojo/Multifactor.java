package com.entersekt.push.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Multifactor
{
    private String device_type;

    private String try_suppress_transakt_popup;

    public String getDevice_type ()
    {
        return device_type;
    }

    public void setDevice_type (String device_type)
    {
        this.device_type = device_type;
    }

    public String getTry_suppress_transakt_popup ()
    {
        return try_suppress_transakt_popup;
    }

    public void setTry_suppress_transakt_popup (String try_suppress_transakt_popup)
    {
        this.try_suppress_transakt_popup = try_suppress_transakt_popup;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [device_type = "+device_type+", try_suppress_transakt_popup = "+try_suppress_transakt_popup+"]";
    }
}