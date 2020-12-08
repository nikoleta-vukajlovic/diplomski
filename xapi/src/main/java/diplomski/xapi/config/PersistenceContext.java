/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.config;

import diplomski.xapi.exceptions.ExceptionTranslator;
import org.jooq.ExecuteListener;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Dusan
 */
@Configuration
@ComponentScan({"diplomski.xapi.jooq.model.tables"})
@EnableTransactionManagement
public class PersistenceContext {

    @Autowired
    private Environment environment;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public ExceptionTranslator exceptionTransformer() {
        return new ExceptionTranslator();
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        //jooqConfiguration.set(new DefaultExecuteListenerProvider(this.exceptionTransformer()));

        String sqlDialectName = environment.getRequiredProperty("spring.jooq.sql-dialect");
        SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
        jooqConfiguration.set(dialect);

        return jooqConfiguration;
    }

}
