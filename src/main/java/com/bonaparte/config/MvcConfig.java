package com.bonaparte.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.bonaparte.constant.MqttProps;
import com.bonaparte.service.MqttPushCallback;
import com.bonaparte.service.MqttPushClient;
import com.karakal.commons.filter.ApiCorsFilter;
import com.karakal.commons.filter.StatisticsFilter;
import com.karakal.commons.uc.interceptor.AuthorizationInterceptor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private MqttProps mqttProps;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**/*.do").addPathPatterns("/**/*.json").addPathPatterns("/**/*.action");
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**").excludePathPatterns("/swagger*/**").excludePathPatterns("/v2/**").excludePathPatterns("/webjars/**");
        super.addInterceptors(registry);
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }


    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        //fastJsonHttpMessageConverter.getFastJsonConfig().setFeatures(Feature.DisableCircularReferenceDetect);//有了setSerializerFeatures，禁止循环应用失效
        fastJsonHttpMessageConverter.getFastJsonConfig().setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        fastJsonHttpMessageConverter.getFastJsonConfig().setDateFormat("yyyy-MM-dd HH:mm:ss");//时间转换
        converters.add(fastJsonHttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Bean
    public FilterRegistrationBean charFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter apiCorsFilter = new CharacterEncodingFilter();
        apiCorsFilter.setForceEncoding(true);
        apiCorsFilter.setEncoding("utf-8");
        registrationBean.setFilter(apiCorsFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean apiCorsFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        ApiCorsFilter apiCorsFilter = new ApiCorsFilter();
        registrationBean.setFilter(apiCorsFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean basicFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        StatisticsFilter statisticsFilter = new StatisticsFilter();
        registrationBean.setFilter(statisticsFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Bean
    public MqttClient MqttClient() {

        MqttClient mqttClient = null;
        try {
            mqttClient = new MqttClient(mqttProps.getHost(),
                    mqttProps.getClientid(),
                    new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(mqttProps.getUsername());
            options.setPassword(mqttProps.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProps.getTimeout());
            options.setKeepAliveInterval(mqttProps.getKeepalive());
            try {
                mqttClient.setCallback(new MqttPushCallback());
                mqttClient.connect(options);
                mqttClient.subscribe(mqttProps.getTopic());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return mqttClient;
    }
}
