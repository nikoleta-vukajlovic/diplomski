/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dusan
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext appContext)
            throws BeansException {
        ctx = appContext;

    }

    public static void registerObject(Object object) {
        AutowireCapableBeanFactory autowireBeanFactory = (AutowireCapableBeanFactory) ((GenericApplicationContext) ctx).getBeanFactory();
        autowireBeanFactory.autowireBean(object);
        autowireBeanFactory.initializeBean(object, object.getClass().getName());
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

}
