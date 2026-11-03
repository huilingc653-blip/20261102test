package com.bytter.system.api.i18n;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class I18nConfig {
    @Bean
    public DatabaseMessageSource messageSource() {
        DatabaseMessageSource messageSource = new DatabaseMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
