package com.example.mybatis.datasource.master;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Import(MasterDBConfig.MyBatisConfig.class)
public class MasterDBConfig {
    static final String NAME = "master";

    static final String PREFIX_DATASOURCE_CONFIG = "datasource." + NAME;
    static final String PREFIX_MYBATIS_CONFIG = "mybatis." + NAME;

    // Spring-JDBC
    static final String HIKARI_CONFIG = "hikariConfig-" + NAME;
    static final String DATASOURCE = "datasource-" + NAME;
    static final String TX_MANAGER = "transactionManager-" + NAME;
    static final String TX_TEMPLATE = "transactionTemplate-" + NAME;

    // MyBatis
    static final String MYBATIS_CONFIG = "mybatisProperties-" + NAME;
    static final String SQL_SESSION_FACTORY = "sqlSessionFactory-" + NAME;
    static final String SQL_SESSION_TEMPLATE = "sqlSessionTemplate-" + NAME;

    @Bean(HIKARI_CONFIG)
    @ConfigurationProperties(PREFIX_DATASOURCE_CONFIG)
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean(DATASOURCE)
    public DataSource datasource(@Qualifier(HIKARI_CONFIG) HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean(TX_MANAGER)
    public DataSourceTransactionManager txManager(
            @Qualifier(DATASOURCE) DataSource dataSource,
            ObjectProvider<TransactionManagerCustomizers> txManagerCustomizers
    ) {
        DataSourceTransactionManager txMgr = new DataSourceTransactionManager(dataSource);
        TransactionManagerCustomizers customizers = txManagerCustomizers.getIfAvailable();
        if (customizers != null) {
            customizers.customize(txMgr);
        }
        return txMgr;
    }

    @Bean(TX_TEMPLATE)
    public TransactionTemplate txTemplate(@Qualifier(TX_MANAGER) PlatformTransactionManager txManager) {
        return new TransactionTemplate(txManager);
    }

    @Configuration
    @MapperScan(basePackageClasses = MasterDBConfig.class, sqlSessionFactoryRef = SQL_SESSION_FACTORY, sqlSessionTemplateRef = SQL_SESSION_TEMPLATE)
    public static class MyBatisConfig {

        @Bean(MYBATIS_CONFIG)
        @ConfigurationProperties(PREFIX_MYBATIS_CONFIG)
        public MybatisProperties mybatisProperties() {
            return new MybatisProperties();
        }

        @Bean(SQL_SESSION_FACTORY)
        public SqlSessionFactory sessionFactory(
                @Qualifier(DATASOURCE) DataSource dataSource,
                @Qualifier(MYBATIS_CONFIG) MybatisProperties mybatisProperties,
                ObjectProvider<Interceptor[]> interceptorsProvider,
                ObjectProvider<TypeHandler[]> typeHandlersProvider,
                ObjectProvider<LanguageDriver[]> languageDriversProvider,
                ResourceLoader resourceLoader,
                ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider
        ) throws Exception {
            MybatisAutoConfiguration autoConfiguration = new MybatisAutoConfiguration(mybatisProperties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
            return autoConfiguration.sqlSessionFactory(dataSource);
        }

        @Bean(SQL_SESSION_TEMPLATE)
        public SqlSessionTemplate sessionTemplate(@Qualifier(SQL_SESSION_FACTORY) SqlSessionFactory sessionFactory) {
            return new SqlSessionTemplate(sessionFactory);
        }
    }

}
