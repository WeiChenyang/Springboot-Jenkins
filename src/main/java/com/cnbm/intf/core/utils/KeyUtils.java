package com.cnbm.intf.core.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 *
 * @create 201903120:44
 */
@Component
public class KeyUtils {
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public ResourceLoader createResourceLoader() {
        return new DefaultResourceLoader();
    }

    public static String privateKey;
    public static String publicKey;

    @PostConstruct
    public void setKey() throws IOException {
        publicKey = IOUtils.toString(resourceLoader.getResource("classpath:key/publicKey.key").getInputStream(), "UTF-8");
        privateKey = IOUtils.toString(resourceLoader.getResource("classpath:key/privateKey.key").getInputStream(), "UTF-8");
    }
}
