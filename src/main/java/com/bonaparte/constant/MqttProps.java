package com.bonaparte.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yangmingquan on 2018/9/26.
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttProps {
    String host;
    String clientid;
    String topic;
    String username;
    String password;
    Integer timeout;
    Integer keepalive;
}
