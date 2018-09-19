package com.efm;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@ComponentScan("com.efm")
@EnableJpaRepositories("com.efm.dao.iface")
@EnableSpringConfigured
//@PropertySource("classpath:/datasource.properties")
@Configuration
public class TestConfiguration {
    /*
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.efm.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }*/
    
	@Bean
	public DataSource dataSource() {

		// no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.HSQL) //.H2 or .DERBY
			//.addScript("db/sql/create-db.sql")
			//.addScript("db/sql/insert-data.sql")
			.build();
		return db;
	}
     
    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        
        properties.put("hibernate.archive.autodetection", "class");
        properties.put("hibernate.connection.driver_clas", "org.hsqldb.jdbcDriver");
        properties.put("hibernate.connection.url", "jdbc:hsqldb:file:target/testdb;shutdown=true");
        properties.put("hibernate.connection.user", "sa");
        properties.put("hibernate.flushMode", "FLUSH_AUTO");
                
        return properties;        
    }
    
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
	    LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
	    emfb.setDataSource(dataSource);
	    emfb.setPackagesToScan("com.efm.model"); 
	    emfb.setJpaVendorAdapter(jpaVendorAdapter());
	    emfb.setJpaPropertyMap(jpaPropertiesMap()); 
	    return emfb;
	}
	
	public Map<String, ?> jpaPropertiesMap() {
		Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect"); // assumption based on your pom-file
	    properties.put("hibernate.hbm2ddl.auto", "update"); // you need to google for appropriate option
	    return properties;
	}

	@Bean 
	public JpaVendorAdapter jpaVendorAdapter() {
	    return new HibernateJpaVendorAdapter();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	    return transactionManager;
	}
     
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
}
