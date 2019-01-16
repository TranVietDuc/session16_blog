package com.codegym.blog;

import com.codegym.blog.repository.BlogRepository;
import com.codegym.blog.repository.impl.BlogRepositoryImpl;
import com.codegym.blog.service.BlogService;
import com.codegym.blog.service.impl.BlogServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Locale;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.codegym.blog")

public class ApplicationConfig  extends WebMvcConfigurerAdapter implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

         @Bean
         public BlogRepository blogRepository(){
            return new BlogRepositoryImpl();
         }

         @Bean
         public  BlogService blogService(){
            return new BlogServiceImpl();
         }


        //Thymeleaf Configuration
        @Bean
        public SpringResourceTemplateResolver templateResolver(){
            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
            templateResolver.setApplicationContext(applicationContext);
            templateResolver.setPrefix("/WEB-INF/views");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            return templateResolver;
        }

        @Bean
        public TemplateEngine templateEngine(){
            TemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.setTemplateResolver(templateResolver());
            return templateEngine;
        }

        @Bean
        public ThymeleafViewResolver viewResolver(){
            ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
            viewResolver.setTemplateEngine(templateEngine());
            return viewResolver;
        }

        //JPA configuration
        @Bean
        @Qualifier(value = "entityManager")
        public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
            return entityManagerFactory.createEntityManager();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource());
            em.setPackagesToScan(new String[]{"com.codegym.blog.model"});

            JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);
            em.setJpaProperties(additionalProperties());
            return em;
        }

        @Bean
        public DataSource dataSource(){
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/blog");
            dataSource.setUsername( "root" );
            dataSource.setPassword( "123456" );
            return dataSource;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(emf);
            return transactionManager;
        }

        Properties additionalProperties() {
            Properties properties = new Properties();
            properties.setProperty("hibernate.hbm2ddl.auto", "update");
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            return properties;
        }
        // Config Internationalize and localize
        @Bean
        public MessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("message");
            return messageSource;
        }

        //Cau hinh interceptor tu dong phan tich tham o lang di kem cac request de xac dinh ban dia hoa
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
            interceptor.setParamName("lang");
            registry.addInterceptor(interceptor);
        }

        //BO sung localResolve de lay thong tin ban dia su dung message tuong ung
         // O day chugn ta dung SessionLocalResolve de lay thong tin local tu session
          // Ngoai ra tao ra 1 mac dinh
     @Bean
    public LocaleResolver localeResolver(){
         SessionLocaleResolver localeResolver = new SessionLocaleResolver();
         localeResolver.setDefaultLocale(new Locale("vie"));
         return localeResolver;
     }

    }

