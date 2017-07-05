package com.github.sandokandias.payments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        Locale ptBr = new Locale("pt", "BR");
        slr.setDefaultLocale(ptBr);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(executorContext());
    }

    @Bean
    public ThreadPoolTaskExecutor executorContext() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        //TODO put configs on the properties
        //TODO to define a equation for pool size
        t.setCorePoolSize(1000);
        t.setMaxPoolSize(1000);
        t.setQueueCapacity(10000);
        t.setAllowCoreThreadTimeOut(true);
        t.setKeepAliveSeconds(120);
        t.initialize();
        return t;
    }
}
