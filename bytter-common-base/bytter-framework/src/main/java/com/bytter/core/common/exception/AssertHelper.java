package com.bytter.core.common.exception;

import com.bytter.core.enums.IMessageEnum;

import java.util.function.Supplier;

public final class AssertHelper {

    public static void isTrue(boolean cond, IMessageEnum iMessageEnum){
        if(!cond){
            throw new ParameterException(iMessageEnum);
        }
    }

    public static void isTrue(boolean cond, IMessageEnum iMessageEnum,String ...params){
        if(!cond){
            throw new ParameterException(iMessageEnum, params);
        }
    }

    public static void isTrue(boolean cond, Supplier<IMessageEnum> supplier){
        if(!cond){
            throw new ParameterException(supplier.get());
        }
    }

    public static void isTrue(boolean cond, String msg){
        if(!cond){
            throw new ParameterException(msg);
        }
    }

    public static void isTrueLoginEx(boolean cond,String msg) {
        if(!cond){
            throw new BytterLoginException(msg);
        }
    }

    public static void isTrueBusEx(boolean cond,IMessageEnum iMessageEnum) {
        if(!cond){
            throw new BusinessException(iMessageEnum);
        }
    }

    public static void isTrueBusEx(boolean cond, IMessageEnum iMessageEnum,String ...params) {
        if(!cond){
            throw new BusinessException(iMessageEnum, params);
        }
    }

    public static void isTrueBusEx(boolean cond,String msg) {
        if(!cond){
            throw new BusinessException(msg);
        }
    }

}
