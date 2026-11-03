package com.bytter.dual.interfaces;
/**
 * Created by zhoul on 2017/8/31.
 */
public interface DualManagerCommand<T>  {
    String serializable(T t);
    T serializableReversal(String str);
    void execute(T variables);
    String getApprovePageUrl();
}
