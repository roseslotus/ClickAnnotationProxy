package com.lotus.clickannotationproxy.helper;

import android.app.Activity;
import android.view.View;

import com.lotus.clickannotationproxy.anno.EventType;
import com.lotus.clickannotationproxy.anno.OnClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectHelper {


    public static void inject(final Activity target) {
        if (target == null) {
            return;
        }
        Class<? extends Activity> clz = target.getClass();
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            Annotation[] annotations = declaredMethod.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(EventType.class)){
                    EventType eventType =  annotationType.getAnnotation(EventType.class);
                    final String listenerSetter = eventType.listenerSetter();
                    final Class listenerType = eventType.listenerType();
                    try {
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                       final int[] ids = (int[])valueMethod.invoke(annotation);

                       Object proxyInstance = Proxy.newProxyInstance(target.getClassLoader(), new Class[]{listenerType}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                return method.invoke(target,args);
                            }
                        });
                        for (int id : ids) {
                            View view = target.findViewById(id);
                            Method setter = view.getClass().getMethod(listenerSetter,listenerType);
                            setter.invoke(view,proxyInstance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static Object  listener=null;
    public static void injectEvent(final Activity target) {
        if (null == target){
            return;
        }
        Class<? extends Activity> activityClass = target.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(OnClick.class)){
                OnClick annotation = declaredMethod.getAnnotation(OnClick.class);
                int[] value = annotation.value();
                //拿注解上面的注解
                EventType eventType = annotation.annotationType().getAnnotation(EventType.class);
                Class listenerType = eventType.listenerType();
                String listenerSetter = eventType.listenerSetter();
//                String methoNadme = eventType.methodName();
                listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return declaredMethod.invoke(target,args);
                    }
                });

                for (int id : value) {
                    View view = target.findViewById(id);
                    try {
                        Method listenerSetMethod = view.getClass().getMethod(listenerSetter,listenerType);
                        listenerSetMethod.setAccessible(true);
                        listenerSetMethod.invoke(view,listener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
     }
}
