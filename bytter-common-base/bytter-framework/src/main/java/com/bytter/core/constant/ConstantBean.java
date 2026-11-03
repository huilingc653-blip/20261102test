package com.bytter.core.constant;
/**
 * 常量bean
 * 
 * @author liyh
 * @param <T>
 *            泛型
 * @since 2011-8-21
 */
public class ConstantBean<T> implements java.io.Serializable {
    /** 名称 **/
    private String name;
    /** 值 **/
    private T value;

    public ConstantBean() {
    }

    public ConstantBean(String name, T value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public T getValue() {
        return value;
    }
    public T get() {
        return getValue();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    /**
     * 判断是否相等
     * 
     * @param obj
     *            对象
     * @return true/false
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstantBean<T> other = (ConstantBean<T>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
