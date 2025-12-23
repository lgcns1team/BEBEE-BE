package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.common.config.BaseWebConfig;
import com.lgcns.bebee.common.properties.CorsProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig extends BaseWebConfig {

    public WebConfig(CorsProperties corsProperties) {
        super(corsProperties);
    }
}