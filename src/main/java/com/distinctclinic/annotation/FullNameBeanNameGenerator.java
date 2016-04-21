package com.distinctclinic.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.beans.Introspector;

/**
 * Description:自定义bean名称生成器，使用全限定名，避免不同模块间bean名称冲突
 * <p>
 * Created by js.lee on 8/28/15.
 */
public class FullNameBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        return Introspector.decapitalize(definition.getBeanClassName());
    }
}
