package it.gov.pagopa.bpd.winning_transaction.connector.jpa.config;

import com.zaxxer.hikari.HikariDataSource;
import it.gov.pagopa.bpd.common.connector.jpa.CustomJpaRepository;
import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import it.gov.pagopa.bpd.common.connector.jpa.config.BaseJpaConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:config/jpaConnectionConfig.properties")
@EnableJpaRepositories(
        repositoryBaseClass = CustomJpaRepository.class,
        basePackages = {"it.gov.pagopa.bpd.winning_transaction.connector.jpa"},
        excludeFilters = @ComponentScan.Filter(ReadOnlyRepository.class),
        includeFilters = @ComponentScan.Filter(Repository.class),
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class WinningTransactionJpaConfig extends BaseJpaConfig {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private int connectionPoolSize;

    @Value("${spring.datasource.hikari.schema}")
    private String schema;

    @Value("${spring.datasource.hikari.connectionTimeout}")
    private long timeout;

    @Value("${spring.datasource.hikari.readOnly}")
    private boolean readOnly;

    @Value("${spring.jpa.database-platform}")
    private String hibernateDialect;

    @Value("${spring.jpa.show-sql}")
    private boolean showSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    @Bean(name = {"dataSource"})
    public DataSource dataSource() throws Exception{
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(this.connectionPoolSize);
        ds.setConnectionTimeout(this.timeout);
        ds.setDriverClassName(this.driverClassName);
        ds.setJdbcUrl(this.url);
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        if(StringUtils.isNotBlank(this.schema)) {
            ds.setSchema(this.schema);
        }
        ds.setReadOnly(readOnly);

        return ds;
    }

    @Bean(name = {"entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource datasource
    ){
        Properties jpaProperties = new Properties();


        jpaProperties.put("hibernate.dialect", this.hibernateDialect);
        jpaProperties.put("hibernate.show_sql", this.showSql);
        jpaProperties.put("hibernate.jdbc.batch_size", 5);
        jpaProperties.put("hibernate.order_inserts", Boolean.TRUE);
        jpaProperties.put("hibernate.order_updates", Boolean.TRUE);
        jpaProperties.put("hibernate.jdbc.batch_versioned_data", Boolean.FALSE);
        jpaProperties.put("hibernate.id.new_generator_mappings", Boolean.FALSE);
        jpaProperties.put("hibernate.jdbc.lob.non_contextual_creation",
                Objects.isNull(null) ? Boolean.TRUE
                        : null);
        if (Boolean.FALSE) {
            jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        } else {
            jpaProperties.put("hibernate.hbm2ddl.auto", this.hibernateDdlAuto);
        }

        List<String> propertyPackages = new ArrayList<String>();
        if (propertyPackages.isEmpty()) {
            propertyPackages.add("eu.sia.meda");
            propertyPackages.add("it.gov.pagopa.bpd.winning_transaction.connector.jpa.model");
        }
        String[] packagesToScan = (String[]) propertyPackages.toArray(new String[propertyPackages.size()]);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(datasource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(packagesToScan);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean(name = {"transactionManager"})
    public PlatformTransactionManager transactionManager() throws Exception {
        return new JpaTransactionManager(this.entityManagerFactory(this.dataSource()).getObject());
    }
}
