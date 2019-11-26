package com.ai.redis.service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author liuhb
 * @date 2019-11-21 16:31
 */
public class ReloadPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);
    }

    private void init(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Thread th = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(2L));
                    Properties mergedProps = mergeProperties();

                    // Convert the merged properties, if necessary.
                    convertProperties(mergedProps);
                    System.out.println(mergedProps.getProperty("name"));
                    processProperties(beanFactory, mergedProps);
                }
            } catch (IOException ex) {
                throw new BeanInitializationException("Could not load properties", ex);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        th.setDaemon(true);
        th.run();

    }
}
