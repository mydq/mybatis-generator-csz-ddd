package org.beans.util;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:25
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    public SpringUtil() {
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBeansByType(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBeansByName(String name) {
        return context.getBean(name);
    }
}
