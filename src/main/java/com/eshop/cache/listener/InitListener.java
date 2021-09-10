package com.eshop.cache.listener;

import com.eshop.cache.kafka.KafkaConsumer;
import com.eshop.cache.spring.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * * 系统初始化的监听器
 *
 * @author: RenLiLi
 * @date: 2021/9/9 14:09
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);

        new Thread(new KafkaConsumer("cache-message")).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
