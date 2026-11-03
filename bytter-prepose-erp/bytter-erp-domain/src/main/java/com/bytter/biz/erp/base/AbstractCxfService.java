package com.bytter.biz.erp.base;

import com.bytter.core.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;

/**
 * @author zhoul
 */
public class AbstractCxfService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    protected String success() {
        return JsonUtils.toJSONString(Result.success("操作成功!"));
    }

    protected String success(String message) {
        return  JsonUtils.toJSONString((Result.success(message)));
    }

    protected String success(Object data) {
        return  JsonUtils.toJSONString((Result.success("操作成功", data)));
    }

    protected String failure() {
        return JsonUtils.toJSONString(Result.failure("操作失败!"));
    }

    protected String failure(String message) {
        return  JsonUtils.toJSONString(Result.failure(message));
    }

    protected <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> validateSet = Validation.buildDefaultValidatorFactory().getValidator().validate(object, groups);
        if (validateSet.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder("数据存在问题：");
        for (ConstraintViolation<T> constraintViolation : validateSet) {
            sb.append("[").append(constraintViolation.getPropertyPath()).append("]");
            sb.append(constraintViolation.getMessage()).append(",");
        }
        throw new RuntimeException(sb.toString());
    }

    protected <T> void validate(List<T> object, Class<?>... groups) {
        for (T t : object) {
            validate(t, groups);
        }
    }


}
