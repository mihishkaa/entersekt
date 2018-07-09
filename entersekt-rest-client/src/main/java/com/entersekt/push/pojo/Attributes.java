package com.entersekt.push.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes
{
    private String positive_button;

    private String ttl_seconds;

    private String title;

    private String text;

    private String negative_button;

    private Value_pairs[] value_pairs;

    private String push_notify;

    private Multifactor multifactor;

    private Buttons[] buttons;

    private Text_boxes[] text_boxes;

    public String getPositive_button ()
    {
        return positive_button;
    }

    public void setPositive_button (String positive_button)
    {
        this.positive_button = positive_button;
    }

    public String getTtl_seconds ()
    {
        return ttl_seconds;
    }

    public void setTtl_seconds (String ttl_seconds)
    {
        this.ttl_seconds = ttl_seconds;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getNegative_button ()
    {
        return negative_button;
    }

    public void setNegative_button (String negative_button)
    {
        this.negative_button = negative_button;
    }

    public Value_pairs[] getValue_pairs ()
    {
        return value_pairs;
    }

    public void setValue_pairs (Value_pairs[] value_pairs)
    {
        this.value_pairs = value_pairs;
    }

    public String getPush_notify ()
    {
        return push_notify;
    }

    public void setPush_notify (String push_notify)
    {
        this.push_notify = push_notify;
    }

    public Multifactor getMultifactor ()
    {
        return multifactor;
    }

    public void setMultifactor (Multifactor multifactor)
    {
        this.multifactor = multifactor;
    }

    public Buttons[] getButtons ()
    {
        return buttons;
    }

    public void setButtons (Buttons[] buttons)
    {
        this.buttons = buttons;
    }

    public Text_boxes[] getText_boxes ()
    {
        return text_boxes;
    }

    public void setText_boxes (Text_boxes[] text_boxes)
    {
        this.text_boxes = text_boxes;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [positive_button = "+positive_button+", ttl_seconds = "+ttl_seconds+", title = "+title+", text = "+text+", negative_button = "+negative_button+", value_pairs = "+value_pairs+", push_notify = "+push_notify+", multifactor = "+multifactor+", buttons = "+buttons+", text_boxes = "+text_boxes+"]";
    }
}
