package com.ai.redis.auto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.*;
import org.springframework.util.StringValueResolver;

public class ReBeanDefinitionVisitor extends BeanDefinitionVisitor {
    public ReBeanDefinitionVisitor(StringValueResolver valueResolver) {
        super(valueResolver);
    }

    protected ReBeanDefinitionVisitor() {
        super();
    }

    private static final Map<String, Map<String, PropertyValue>> pvMap = new HashMap<>();

    public void visitBeanDefinition(String beanName,
                                    BeanDefinition beanDefinition) {
        visitParentName(beanDefinition);
        visitBeanClassName(beanDefinition);
        visitFactoryBeanName(beanDefinition);
        visitFactoryMethodName(beanDefinition);
        visitScope(beanDefinition);
        record(beanName, beanDefinition);
        visitPropertyValues(beanDefinition.getPropertyValues());
        ConstructorArgumentValues cas = beanDefinition
                .getConstructorArgumentValues();
        visitIndexedArgumentValues(cas.getIndexedArgumentValues());
        visitGenericArgumentValues(cas.getGenericArgumentValues());
    }

    protected Map<String, Object> visitPropertyValues2(ConfigurableListableBeanFactory beanFactoryToProcess,
                                                       BeanDefinition nbd, String beanName) {
        Map<String, Object> map = new HashMap<>();
        MutablePropertyValues pvs = nbd.getPropertyValues();
        PropertyValue[] pvArray = pvs.getPropertyValues();
        BeanWrapper bw = null;
        for (PropertyValue pv : pvArray) {
            PropertyValue bakPv = getPv(beanName, pv.getName());
            if (bakPv == null) {
                continue;
            }
            if (!ConfigUtils.testType(bakPv.getValue())) {
                continue;
            }
            if (ConfigUtils.countOperator(bakPv.getValue()) <= 0) {
                continue;
            }
            try {
                Object copyObject = ConfigUtils.copyObject(bakPv.getValue());
                Object newVal = resolveValue(copyObject);
                Object bean = beanFactoryToProcess.getBean(beanName);
                if (bw == null) {
                    bw = new BeanWrapperImpl(bean);
                }
                bw.setPropertyValue(pv.getName(), ConfigUtils.convertValue(newVal));
                map.put(pv.getName(), pv.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    protected String convert(String name, String value) {
        TypedStringValue typedStringValue = new TypedStringValue(value);
        Object newVal = resolveValue(typedStringValue);
        return typedStringValue.getValue();
    }

    void record(String beanName, BeanDefinition beanDefinition) {
        if (pvMap.containsKey(beanName)) {
            return;
        }
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        PropertyValue[] propertyValues = mpv.getPropertyValues();
        Map<String, PropertyValue> pop = new HashMap<>();
        for (PropertyValue pv : propertyValues) {
            if (ConfigUtils.countOperator(pv.getValue()) <= 0) {
                continue;
            }
            PropertyValue opv = null;
            // if (pv.getValue() instanceof TypedStringValue) {
            // TypedStringValue tv = (TypedStringValue) pv.getValue();
            // opv = new PropertyValue(pv.getName(),
            // ConfigUtils.copyObject(tv));
            // } else {
            Object copyObject = ConfigUtils.copyObject(pv.getValue());

            opv = new PropertyValue(pv.getName(), copyObject);
            // }
            pop.put(opv.getName(), opv);
        }
        pvMap.put(beanName, pop);
    }

    PropertyValue getPv(String beanName, String key) {
        return pvMap.get(beanName).get(key);
    }
}
