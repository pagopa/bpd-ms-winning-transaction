package it.gov.pagopa.bpd.winning_transaction.connector.jpa.config;

import it.gov.pagopa.bpd.common.connector.jpa.CustomJpaRepository;
import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:config/replicaJpaConnectionConfig.properties")
@EnableJpaRepositories(
        repositoryBaseClass = CustomJpaRepository.class,
        basePackages = {"it.gov.pagopa.bpd.winning_transaction.connector.jpa"},
        includeFilters = @ComponentScan.Filter(ReadOnlyRepository.class),
        excludeFilters = @ComponentScan.Filter(Repository.class),
        entityManagerFactoryRef = "entityManagerFactoryReplica",
        transactionManagerRef = "transactionManagerReplica"
)
@EntityScan(
        basePackages = {"it.gov.pagopa.bpd.winning_transaction.connector.jpa"}
)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class WinningTransactionReplicaJpaConfig {

    @Bean("dataSourceReplicaProperties")
    @ConfigurationProperties("spring.datasource.replica")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("dataSourceReplica")
    @ConfigurationProperties(prefix = "spring.datasource.replica.hikari")
    public DataSource dataSource(@Qualifier("dataSourceReplicaProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }


    @Bean("entityManagerFactoryReplica")
    @ConfigurationProperties("spring.jpa")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            JpaProperties properties,
            @Qualifier("dataSourceReplica") DataSource dataSource,
            ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
            ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(properties.isShowSql());
        adapter.setDatabase(properties.determineDatabase(dataSource));
        adapter.setDatabasePlatform(properties.getDatabasePlatform());
        adapter.setGenerateDdl(properties.isGenerateDdl());

        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
                adapter,
                properties.getProperties(),
                persistenceUnitManager.getIfAvailable());
        customizers.orderedStream()
                .forEach((customizer) -> customizer.customize(builder));
        return builder
                .dataSource(dataSource)
                .packages("it.gov.pagopa.bpd.winning_transaction.connector.jpa.model")
                .build();
    }

    @Bean("transactionManagerReplica")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactoryReplica")
                                                                 EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}