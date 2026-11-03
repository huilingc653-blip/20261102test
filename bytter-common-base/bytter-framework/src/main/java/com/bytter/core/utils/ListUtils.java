package com.bytter.core.utils;
import java.util.ArrayList;
import java.util.List;
/**
 * List集合帮助类
 *
 * @author zuoy
 */
public class ListUtils {
    /**
     * 按新的Class对象复制
     *
     * @param source
     *            list, example: list
     * @param destinationClass
     *            class, example: UserEntity.class;
     * @param <E>
     *            泛型对象
     * @return List集合
     */
    public static <E> List<E> copyTo(List<?> source, Class<E> destinationClass) throws Exception {
        if (source.size() == 0) {
            return new ArrayList();
        }
        List<E> res = new ArrayList<E>(source.size());
        for (Object o : source) {
            E e = destinationClass.newInstance();
            BeanUtils.copyProperties(e, o);
            res.add(e);
        }
        return res;
    }
    /**
     * 判断List集合是否为空
     *
     * @param list
     *            集合
     * @return 是否为空
     */
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }
    /**
     * 判断List集合是否为空
     *
     * @param list
     *            集合
     * @return 是否为空
     */
    public static boolean isNotNullOrEmpty(List list) {
        return !isNullOrEmpty(list);
    }
}
