package com.bonaparte.service;

import com.bonaparte.bean.PushPayload;
import com.bonaparte.constant.MqttProps;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangmingquan on 2018/9/26.
 */
@Service
public class MqttPushClient {
    @Autowired
    MqttProps mqttProps;

    @Autowired
    private MqttClient mqttClient;

    public void publish(Integer qos, boolean persistence, String topic, PushPayload messageT){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(persistence);
        message.setPayload(messageT.toString().getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        if (null == mqttTopic){

        }
        MqttDeliveryToken mqttDeliveryToken;
        try{
            mqttDeliveryToken = mqttTopic.publish(message);
            mqttDeliveryToken.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(Integer qos, String topic){
        try{
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
