package com.ai.redis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ai.redis.model.MyTestBean;

/**
 * @author liuhb
 * @date 2019-11-14 16:46
 */
@SpringJUnitConfig
public class BeanFactoryTest {

    @Test
    public void testLoadBean() {
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("sampleBeans.xml"));
        MyTestBean testBean = (MyTestBean) bf.getBean("myTestBean");
        Assert.assertEquals(testBean.getStr().intValue(),100);
    }

}
