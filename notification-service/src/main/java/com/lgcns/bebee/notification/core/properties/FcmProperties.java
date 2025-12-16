package com.lgcns.bebee.notification.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "firebase")
public record FcmProperties(
        String serviceAccountKeyPath
){
}
