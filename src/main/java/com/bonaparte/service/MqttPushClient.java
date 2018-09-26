package com.bonaparte.service;

import com.bonaparte.bean.PushPayload;
import com.bonaparte.constant.MqttProps;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yangmingquan on 2018/9/26.
 */
@Component
public class MqttPushClient {
    @Autowired
    MqttProps mqttProps;

    private MqttClient mqttClient;
    public static volatile MqttPushClient mqttPushClient = null;
    public  static MqttPushClient getInstance(){
        if (mqttPushClient == null){
            synchronized (MqttPushClient.class){
                mqttPushClient = new MqttPushClient();
            }
        }
        return mqttPushClient;
    }

    public MqttPushClient(){
        try{
            mqttClient = new MqttClient(mqttProps.getHost(),
                    mqttProps.getClientid(),
                    new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(mqttProps.getUsername());
            options.setPassword(mqttProps.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProps.getTimeout());
            options.setKeepAliveInterval(mqttProps.getKeepalive());
            try{
                mqttClient.setCallback(new MqttPushCallback());
                mqttClient.connect();
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

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
