package com.lotus.clickannotationproxy.anno;

import android.view.View;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerSetter = "setOnClickListener", listenerType = View.OnClickListener.class,methodName = "onClick")
public @interface OnClick {
    @IdRes int[] value();
}
