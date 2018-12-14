package com.bonaparte.bean;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by yangmingquan on 2018/9/26.
 */
@Getter
@Setter
public class PushPayload extends MqttMessage {
    String title;
    String content;
    String phone;
    String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
