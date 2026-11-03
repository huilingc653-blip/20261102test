package com.bytter.core.utils;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * 排序list，适合两个字段 Created by zhangxb on 2017/7/11.
 */
public class SortList<E> implements Serializable {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void Sort(List<E> list, final String method1, final String method2, final String sort) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Method m1 = ((E) a).getClass().getMethod(method1, null);
                    Method m2 = ((E) b).getClass().getMethod(method1, null);
                    ret = getRe(m1, m2, a, b);
                    if (ret == 0) {
                        Method m21 = ((E) a).getClass().getMethod(method2, null);
                        Method m22 = ((E) b).getClass().getMethod(method2, null);
                        ret = getRe(m21, m22, a, b);
                    }
                } catch (NoSuchMethodException ne) {
                    ne.printStackTrace();
                }
                return ret;
            }
            public int getRe(Method m1, Method m2, Object a, Object b) {
                int ret = 0;
                try {
                    if (sort != null && "desc".equals(sort)) {
                        ret = m2.invoke(((E) b), null).toString().compareTo(m1.invoke(((E) a), null).toString());
                    } else {
                        ret = m1.invoke(((E) a), null).toString().compareTo(m2.invoke(((E) b), null).toString());
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }
}
