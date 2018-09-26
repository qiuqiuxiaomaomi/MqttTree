package com.bonaparte.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yangmingquan on 2018/9/26.
 */
@Getter
@Setter
public class PushPayload {
    String title;
    String content;
    String phone;
    String address;
}
