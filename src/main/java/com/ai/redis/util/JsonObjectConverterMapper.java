package com.ai.redis.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author liuhb
 * @date 2019-11-26 15:51
 */
public class JsonObjectConverterMapper extends ObjectMapper {

    public JsonObjectConverterMapper() {

        // 设置null值不参与序列化(字段不被显示)
        // this.setSerializationInclusion(Include.NON_NULL);
        // 禁用空对象转换json校验
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 驼峰命名法转换为小写加下划线
        // this.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.registerModule(new JavaTimeModule());
    }
}
