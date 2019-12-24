package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {
    /**
     * 读取custom开头的属性，然后创建BaseProperty，最后执行BaseProperty里面的set方法，
     * 前提是custom开头的子属性和BaseProperty属性名一致，比如BaseProperty里面的key属性，对应custom.key=value，
     * 而且该类被@ConfigurationProperties标注，所以会自动执行BaseProperty里面的set方法，并且如果cunsul配置属性做了变更，
     * 那么会刷新配置项，也就是会在原有BaseProperty bean继续执行set方法（不会创建新bean），set的参数就是更新后的参数，
     * 这就保证了consul更新之后能够将配置属性刷新到bean里面
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "custom")
    public BaseProperty property() {
        return new BaseProperty();
    }
}
