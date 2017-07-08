package com.github.sandokandias.payments;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.cassandra.config.CompressionType;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Profile("postgresql")
    @Configuration
    @EnableAutoConfiguration(exclude = {CassandraDataAutoConfiguration.class})
    static class Postgresql {
    }

    @Profile("cassandra")
    @Configuration
    @EnableConfigurationProperties(CassandraProperties.class)
    @EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
    static class Cassandra extends AbstractCassandraConfiguration {

        @Autowired
        CassandraProperties cassandraProperties;

        @Override
        protected String getContactPoints() {
            return cassandraProperties.getContactPoints();
        }

        @Override
        protected int getPort() {
            return cassandraProperties.getPort();
        }

        @Override
        protected String getKeyspaceName() {
            return cassandraProperties.getKeyspaceName();
        }

        @Override
        protected AuthProvider getAuthProvider() {
            if (StringUtils.hasText(this.cassandraProperties.getUsername())) {
                return new PlainTextAuthProvider(this.cassandraProperties.getUsername(),
                        this.cassandraProperties.getPassword());
            } else {
                return null;
            }
        }

        @Override
        public SchemaAction getSchemaAction() {
            return SchemaAction.valueOf(this.cassandraProperties.getSchemaAction());
        }


        @Override
        protected CompressionType getCompressionType() {
            return CompressionType.NONE;
        }

        @Override
        protected boolean getMetricsEnabled() {
            return CassandraCqlClusterFactoryBean.DEFAULT_METRICS_ENABLED;
        }

        @Override
        protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
            return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName())
                    .withSimpleReplication()
                    .ifNotExists());
        }
    }

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
        //TODO define an equation for pool size
        t.setCorePoolSize(100);
        t.setMaxPoolSize(100);
        t.setQueueCapacity(1000);
        t.setAllowCoreThreadTimeOut(true);
        t.setKeepAliveSeconds(120);
        t.initialize();
        return t;
    }
}
