/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neolore.card.config;

import java.util.Properties;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Config {

  private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

  static {

    // Uncomment the following code if you need a custom configuration for HikariCP.
    // Use "wrapperProfileName=datasource-with-internal-connection-pool" in connection strings (application.yml).
    // This configuration profile suits well both writer datasource and reader datasource.

    /*
    ConfigurationProfileBuilder.get().from(ConfigurationProfilePresetCodes.SF_F0)
        .withName("datasource-with-internal-connection-pool")
        .withConnectionProvider(new HikariPooledConnectionProvider(
            (HostSpec hostSpec, Properties originalProps) -> {
              LOGGER.debug("Start a new HikariCP pool for " + hostSpec.getHost());

              final HikariConfig config = new HikariConfig();
              config.setMaximumPoolSize(30);
              // holds few extra connections in case of sudden traffic peak
              config.setMinimumIdle(2);
              // close idle connection in 15min; helps to get back to normal pool size after load peak
              config.setIdleTimeout(TimeUnit.MINUTES.toMillis(15));
              // verify pool configuration and creates no connections during initialization phase
              config.setInitializationFailTimeout(-1);
              config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(10));
              // validate idle connections at least every 3 min
              config.setKeepaliveTime(TimeUnit.MINUTES.toMillis(3));
              // allows to quickly validate connection in the pool and move on to another connection if needed
              config.setValidationTimeout(TimeUnit.SECONDS.toMillis(1));
              config.setMaxLifetime(TimeUnit.DAYS.toMillis(1));
              return config;
            },
            null
        ))
        .buildAndSet();
    */
  }

  @Bean
  public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(false);

    LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    managerFactoryBean.setJpaVendorAdapter(vendorAdapter);
//    managerFactoryBean.setPackagesToScan("example.data");
    managerFactoryBean.setPackagesToScan("com.neolore.card.data");
    managerFactoryBean.setDataSource(dataSource);

    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

    managerFactoryBean.setJpaProperties(properties);
    managerFactoryBean.afterPropertiesSet();

    return managerFactoryBean.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
//
//  @Bean
//  public HibernateExceptionTranslator hibernateExceptionTranslator(){
//    return new HibernateExceptionTranslator() {
//      @Override
//      public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
//        // We'd like to retry all transactions that have failed due to OBJC connection. This includes
//        // failover exceptions (FailoverSQLException).
//        // Feel free to adjust the following condition according to your cases.
//        if (ex.getCause() != null
//            && ex.getCause() instanceof JDBCConnectionException) {
//          return new ShouldRetryTransactionException(ex);
//        }
//        return super.translateExceptionIfPossible(ex);
//      }
//    };
//  }

  @Bean
  public DataSource dataSource(
      @Qualifier("writerDataSource") DataSource writerDataSource,
      @Qualifier("loadBalancedReaderDataSource") DataSource loadBalancedReaderDataSource) {
    return new RoutingDataSource(writerDataSource, loadBalancedReaderDataSource);
  }

  @Bean(name  = "writerDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.writer-datasource")
  public DataSourceProperties writerDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name  = "loadBalancedReaderDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.load-balanced-reader-datasource")
  public DataSourceProperties loadBalancedReaderDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name  = "writerDataSource")
  public DataSource writerDataSource() {
    return writerDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean(name  = "loadBalancedReaderDataSource")
  public DataSource loadBalancedReaderDataSource() {
    return loadBalancedReaderDataSourceProperties().initializeDataSourceBuilder().build();
  }
}
