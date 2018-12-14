package com.bonaparte.controller;

import com.bonaparte.bean.PushPayload;
import com.bonaparte.constant.MqttProps;
import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangmingquan on 2018/9/26.
 */
@RestController
@RequestMapping("/mqtt")
public class MqttController {
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private MqttProps mqttProps;

    @RequestMapping("/push")
    public Object pushMessage(){
        Map<String, Object> map = new HashMap<>();
        try {
            PushPayload pushPayload = new PushPayload();
            pushPayload.setAddress("成都");
            pushPayload.setContent("四个现代化");
            pushPayload.setPhone("1111111111");
            pushPayload.setTitle("新时代");
            mqttClient.publish(mqttProps.getTopic(),  pushPayload);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

}
