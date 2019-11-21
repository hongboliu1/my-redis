package com.ai.redis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.redis.model.MyTestBean;

/**
 * @author liuhb
 * @date 2019-11-21 10:06
 */
public class AopTest {

    @Test
    public void testClassPathXml() {
        String[] configLocations = {"AopBeans.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(configLocations);
        MyTestBean testBean = (MyTestBean) ctx.getBean("myTestBean");
        Assert.assertEquals(testBean.getName(), "aop");
    }
}
