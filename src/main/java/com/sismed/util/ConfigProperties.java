package com.sismed.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {
    private String subject;
    private String subjectLoaded;
    private String textLoading;
    private String textLoaded;
}


