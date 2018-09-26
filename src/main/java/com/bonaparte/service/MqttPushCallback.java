package com.bonaparte.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by yangmingquan on 2018/9/26.
 */
public class MqttPushCallback implements MqttCallback{

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("连接断开");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("接收的消息主题为" + s);
        System.out.println("接收消息qos" + mqttMessage.getQos());
        System.out.println("接收消息内容为" + mqttMessage.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("消息推送完成");
    }
}
