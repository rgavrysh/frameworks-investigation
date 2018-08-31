package org.spring;

import org.spring.annotations.PrintVIN;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Component
public class PrintVinBeanPostProcessor implements BeanPostProcessor {

    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PrintVIN.class)) {
                App.logger.info(PrintVinBeanPostProcessor.class.getCanonicalName() +
                        ": on method annotated @PrintVIN " + method.getName() +
                        " do nothing before initialization");
            }
        }
        return bean;
    }

    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PrintVIN.class)) {
                Object o = ReflectionUtils.invokeMethod(method, bean);
                App.logger.info(PrintVinBeanPostProcessor.class.getCanonicalName() + ": after initialization " +
                        o.toString());
            }
        }
        return bean;
    }
}
