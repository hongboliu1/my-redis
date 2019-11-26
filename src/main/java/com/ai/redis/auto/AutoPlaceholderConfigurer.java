package com.ai.redis.auto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;

/**
 * @author liuhb
 * @date 2019-11-21 10:51
 */
public class AutoPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements BeanPostProcessor {

    private BeanFactory beanFactory;
    private ConfigurableListableBeanFactory listableBeanFactory;
    private StringValueResolver valueResolver;
    private String beanName;
    private Properties dbProperties = null;

    /**
     * 自动刷新开关,默认开启
     */
    private boolean autoReload = false;
    /**
     * 自动刷新间隔,单位毫秒，默认值60000 值不应该小于60000
     */
    private int reloadInterval = 1000 * 60;

    /**
     * debug打印 ，默认开启
     */
    private boolean debug = true;
    /**
     * 只刷新指定包路径下的属性，多个路径逗号分隔 默认刷新spring上下文中的所有类
     */
    private String packages;

    /**
     * 全局appname，可指定多个 用逗号分离，重复属性后者覆盖前者。如：public,front-all 重复属性取front-all中的定义
     */
    private String globalAppName = "PUBLIC";

    /**
     * 是否开启MQ推送模型，开启推送模型可降低定时拉取对数据库的压力。建议开启此配置后关闭autoReload或提高reloadInterval值
     */
    private boolean push;

    /**
     * 是否支持集合类型,默认不开启，此功能未全面测试，混合子类型的集合可能出现问题
     */
    private boolean supportCollection = false;

    /**
     * MQ推送模型需要的key
     */
    private String pushKey;

    private List<String> appNames = new ArrayList<String>();

    private Map<String, String> muiltIp;
    private static ReloadConfigThread dt;

    {
        dt = new ReloadConfigThread(this);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (autoReload) {
            dt.start0();
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        super.setBeanFactory(beanFactory);
    }

    private class PlaceholderResolvingStringValueResolver implements
            StringValueResolver {

        private final PropertyPlaceholderHelper helper;

        private final PlaceholderResolver resolver;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper(placeholderPrefix,
                    placeholderSuffix, valueSeparator,
                    ignoreUnresolvablePlaceholders);
            this.resolver = new PropertyPlaceholderConfigurerResolver(props);
        }

        @Override
        public String resolveStringValue(String strVal) throws BeansException {
            String value = this.helper.replacePlaceholders(strVal,
                    this.resolver);
            return (value.equals(nullValue) ? null : value);
        }
    }

    public PlaceholderResolvingStringValueResolver newResolver(Properties props) {
        return new PlaceholderResolvingStringValueResolver(props);
    }

    private class PropertyPlaceholderConfigurerResolver implements
            PlaceholderResolver {

        private final Properties props;

        private PropertyPlaceholderConfigurerResolver(Properties props) {
            this.props = props;
        }

        @Override
        public String resolvePlaceholder(String placeholderName) {
            return AutoPlaceholderConfigurer.this.resolvePlaceholder(
                    placeholderName, props, 1);
        }
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        super.setBeanName(beanName);
    }

    /**
     * 刷新后置事件
     */
    private void refreshAfter(Object bean) {
        if (bean instanceof ConfigChangeInterface) {
            ((ConfigChangeInterface) bean).refreshAfter();
        }
    }

    /**
     * 刷新后置事件
     */
    private void changed(Object bean) {
        if (bean instanceof ConfigChangeInterface) {
            ((ConfigChangeInterface) bean).changed();
        }
    }

    /**
     * 刷新前置事件
     */
    private void refreshBefore(Object bean) {
        if (bean instanceof ConfigChangeInterface) {
            ((ConfigChangeInterface) bean).refreshBefore();
        }
    }

    public Map refreshProperties(StringValueResolver valueResolver) {
        Map map = new HashMap();
        ReBeanDefinitionVisitor visitor = new ReBeanDefinitionVisitor(valueResolver);

        String[] beanNames = listableBeanFactory.getBeanDefinitionNames();
        for (String curName : beanNames) {
            if (!(curName.equals(beanName) && listableBeanFactory.equals(this.beanFactory))) {
                BeanDefinition bd = listableBeanFactory.getBeanDefinition(curName);
                if (bd.isAbstract()) {
                    continue;
                }
                Object bean = listableBeanFactory.getBean(curName);
                if (!ConfigUtils.checkPackage(packages, bean)) {
                    continue;
                }
                if (bean instanceof ConfigChangeInterface) {
                    if (!((ConfigChangeInterface) bean).autoReload) {
                        continue;
                    }
                }
                refreshBefore(bean);
                try {
                    visitor.visitPropertyValues2(listableBeanFactory, bd, curName);
                } catch (Exception ex) {
                    throw new BeanDefinitionStoreException(
                            bd.getResourceDescription(), curName,
                            ex.getMessage(), ex);
                }
                Field[] fields = bean.getClass().getDeclaredFields();
                boolean changed = false;
                for (Field field : fields) {
                    Value annotation = field.getAnnotation(Value.class);
                    if (annotation == null) {
                        continue;
                    }
                    ReflectionUtils.makeAccessible(field);
                    String _value = annotation.value();
                    String value = visitor.convert(curName, _value);
                    try {
                        Object object = field.get(bean);
                        if (ObjectUtils.nullSafeEquals(object,
                                ConfigUtils.getValue(field.getType(), value))) {
                            continue;
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    map.put(_value, _value);
                    changed = true;

                    ReflectionUtils.setField(field, bean,
                            ConfigUtils.getValue(field.getType(), value));
                }
                Method[] declaredMethods = bean.getClass().getDeclaredMethods();
                for (Method method : declaredMethods) {
                    boolean valueHit = false;
                    Value annotation = method.getAnnotation(Value.class);
                    Object[] params = new Object[method.getParameterTypes().length];
                    if (annotation != null) {
                        valueHit = true;
                        String _value = annotation.value();
                        String value = visitor.convert(curName, _value);
                        map.put(_value, _value);
                        int i = 0;
                        for (Class<?> clazz : method.getParameterTypes()) {
                            params[i++] = ConfigUtils.getValue(clazz, value);
                        }
                    }
                    Annotation[][] parameterAnnotations = method
                            .getParameterAnnotations();
                    int index = 0;
                    for (Annotation[] annotations : parameterAnnotations) {
                        for (Annotation annotation2 : annotations) {
                            if (annotation2 instanceof Value) {
                                valueHit = true;
                                String _value = ((Value) annotation2).value();
                                String value = visitor.convert(curName, _value);
                                params[index] = ConfigUtils.getValue(
                                        method.getParameterTypes()[index],
                                        value);
                                map.put(_value, _value);
                            }
                        }
                        index++;
                    }
                    if (!valueHit) {
                        continue;
                    }
                    try {
                        method.invoke(bean, params);
                        changed = true;
                    } catch (Exception e) {
                        StringBuilder builder = new StringBuilder();
                        for (Object p : params) {
                            builder.append(p + ",");
                        }
                        System.out.println("\n" + bean.getClass() + ":"
                                + method.getName() + "    params:" + builder);
                        e.printStackTrace();
                    }
                    if (changed) {
                        changed(bean);
                    }
                    refreshAfter(bean);
                }
            }
        }
        return map;
    }

    @Override
    protected void doProcessProperties(
            ConfigurableListableBeanFactory beanFactoryToProcess,
            StringValueResolver valueResolver) {
        this.listableBeanFactory = beanFactoryToProcess;
        this.valueResolver = valueResolver;
        ReBeanDefinitionVisitor visitor = new ReBeanDefinitionVisitor(valueResolver);

        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (String curName : beanNames) {
            if (!(curName.equals(beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
                BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(curName);
                try {
                    visitor.visitBeanDefinition(curName, bd);
                } catch (Exception ex) {
                    throw new BeanDefinitionStoreException(
                            bd.getResourceDescription(), curName,
                            ex.getMessage(), ex);
                }
            }
        }
        beanFactoryToProcess.resolveAliases(valueResolver);
        beanFactoryToProcess.addEmbeddedValueResolver(valueResolver);
    }

    // =============================================================


    private String profileName;

    private String appName;

    private boolean createPropertyFile;

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * {@link #autoReload}
     *
     * @param autoReload
     */
    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    /**
     * {@link #reloadInterval}
     *
     * @param reloadInterval
     */
    public void setReloadInterval(int reloadInterval) {
        if (reloadInterval > this.reloadInterval) {
            this.reloadInterval = reloadInterval;
        }
    }

    /**
     * {@link #debug}
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * {@link #packages}
     *
     * @param packages
     */
    public void setPackages(String packages) {
        this.packages = packages;
    }

    public void setGlobalAppName(String globalAppName) {
        this.globalAppName = globalAppName;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public String isPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public int getReloadInterval() {
        return reloadInterval;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setSupportCollection(boolean supportCollection) {
        this.supportCollection = supportCollection;
    }

    public void setCreatePropertyFile(boolean createPropertyFile) {
        this.createPropertyFile = createPropertyFile;
    }

    public Properties getProperties() {
        return dbProperties;
    }


}
