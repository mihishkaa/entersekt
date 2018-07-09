package com.entersekt.push.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Text_boxes
{
    private String text;

    private String constraints;

    private String label;

    private String max_size;

    private String min_size;

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getConstraints ()
    {
        return constraints;
    }

    public void setConstraints (String constraints)
    {
        this.constraints = constraints;
    }

    public String getLabel ()
    {
        return label;
    }

    public void setLabel (String label)
    {
        this.label = label;
    }

    public String getMax_size ()
    {
        return max_size;
    }

    public void setMax_size (String max_size)
    {
        this.max_size = max_size;
    }

    public String getMin_size ()
    {
        return min_size;
    }

    public void setMin_size (String min_size)
    {
        this.min_size = min_size;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", constraints = "+constraints+", label = "+label+", max_size = "+max_size+", min_size = "+min_size+"]";
    }
}