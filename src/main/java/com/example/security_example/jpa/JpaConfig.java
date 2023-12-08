package com.example.security_example.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "**.repository.**",
        transactionManagerRef = JpaConfig.TransactionManager,
        entityManagerFactoryRef = JpaConfig.EntityManager
)
public class JpaConfig {
    private static final String dbname = "local";

//    private String active="prod";

    private String url = "jdbc:mysql://127.0.0.1:3306/local?serverTimezone=Asia/Seoul";

    private String user="root";

    private String password="1715";

    private String driverName = "com.mysql.cj.jdbc.Driver";

    private String poolname="local-jpa";

    private Integer poolsize=5;

    final static String EntityManager = dbname + "_entityManagerFactory";
    final static String TransactionManager = dbname + "_jpa_transactionManager";



    @Primary
    @Bean(name = EntityManager)
    public LocalContainerEntityManagerFactoryBean local_entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(local_jpa_DataSource());
        em.setPackagesToScan("com.example.security_example");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(mysqldb_additionalProperties(null));
        em.setPersistenceUnitName("localEntityManager");

        return em;
    }

    @Primary
    @Bean
    public DataSource local_jpa_DataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(null);
        hikariConfig.setDriverClassName(driverName);

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);

//        hikariConfig.setJdbcUrl("jdbc:mariadb://localhost:3306/fourfree_sales?allowMultiQueries=true");
//        hikariConfig.setUsername("root");
//        hikariConfig.setPassword("");
        hikariConfig.setPoolName(poolname);
        hikariConfig.setMaximumPoolSize(poolsize);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Primary
    @Bean(name = TransactionManager)
    public PlatformTransactionManager local_jpa_transactionManager(@Qualifier(EntityManager) EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }


    public static Properties mysqldb_additionalProperties(String active) {
        Properties properties = new Properties();
        // 기존 속성 설정
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.use_sql_comments", "true");
        // SQL 쿼리 포맷팅 활성화
        properties.setProperty("hibernate.format_sql", "true");

        return properties;
    }
}
