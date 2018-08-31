package org.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BmwBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
        App.logger.info(BmwBeanFactoryPostProcessor.class.getCanonicalName() + ": found bean definition names : \n"
                + Arrays.asList(beanDefinitionNames).toString());
        for (String beanDefinitionName : beanDefinitionNames) {
            if (beanDefinitionName.equalsIgnoreCase("e46BMW")) {
                App.logger.info("Bean Factory Post Processor works on e46BMW object");
                BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);
//                beanDefinition.setLazyInit(true);
//                beanDefinition.setScope("prototype");
//                beanDefinition.setBeanClassName("E46BMW");
//                beanDefinition.setFactoryBeanName("bmwBeanFactoryPostProcessor");
            }
        }
    }

}
