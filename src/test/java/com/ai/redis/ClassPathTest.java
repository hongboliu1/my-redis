package com.ai.redis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.redis.model.MyTestBean;

/**
 * @author liuhb
 * @date 2019-11-19 11:30
 */

public class ClassPathTest {

    @Test
    public void testClassPathXml() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        MyTestBean testBean = (MyTestBean) ctx.getBean("myTestBean");
        Assert.assertEquals(testBean.getName(), "chinese");
    }

}
