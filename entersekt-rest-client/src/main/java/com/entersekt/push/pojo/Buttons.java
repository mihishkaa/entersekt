package com.entersekt.push.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Buttons
{
    private String[] flags;

    private String role;

    private String label;

    public String[] getFlags ()
    {
        return flags;
    }

    public void setFlags (String[] flags)
    {
        this.flags = flags;
    }

    public String getRole ()
    {
        return role;
    }

    public void setRole (String role)
    {
        this.role = role;
    }

    public String getLabel ()
    {
        return label;
    }

    public void setLabel (String label)
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [flags = "+flags+", role = "+role+", label = "+label+"]";
    }
}