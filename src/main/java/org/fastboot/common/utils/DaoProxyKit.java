package org.fastboot.common.utils;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class DaoProxyKit implements MethodInterceptor {

    private DaoProxyKit() {

    }

    public static Object newProxyInstance(Class<?> targetClass) {

        return Enhancer.create(targetClass, new net.sf.cglib.proxy.MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams,
                                    net.sf.cglib.proxy.MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(targetObject, methodParams);
            }
        });

    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(o, objects);
    }

}
