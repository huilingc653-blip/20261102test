package com.bytter.core.common.context;

import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * 提示语返回本地化
 */
public class LocalMessageService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String getLangByAjaxJson(AjaxJson ajaxJson){
        if (StringUtils.isBlank(ajaxJson.getMsg())) {
            return ajaxJson.getMessageEnum() != null ? getLangByEnum(ajaxJson.getMessageEnum(), ajaxJson.getParams()) :
                    getLangByEnum(ajaxJson.isSuccess() ? CommonMessageEnum.COMMON_MESSAGE_SUCCESS : CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        }
        return ajaxJson.getMsg();
    }

    public String getLangByEnum(IMessageEnum iMessageEnum, String... params) {
        return formatMsg(iMessageEnum, params);
    }

    private String formatMsg(IMessageEnum messageEnum, String...params) {
        String enumName = Optional.ofNullable(messageEnum.getEnum()).map(item -> item.name()
                        .replaceAll(CommonConstant.UNDERLINE, CommonConstant.DOT).toLowerCase())
                .orElse(CommonConstant.EMPTY_STR);
        String localeMsg = getLocaleMsg(enumName);
        if (StringUtils.isBlank(localeMsg)) {
            return localeMsg;
        }
        if (null != params && params.length > 0) {
            return MessageFormat.format(localeMsg, params);
        }
        return localeMsg;
    }

    private String getLocaleMsg(String code) {
        //需区分中英文
        String hashKey = RedisCacheKey.makeRedisKey(CommonConstant.EMPTY_STR, RedisCacheKey.MUL_LANG_PRE);
        String language = LocaleContextHolder.getLocale().toString();
        return (String) redisTemplate.opsForHash().get(hashKey, language + CommonConstant.STRIKETHROUGH + code);
    }
}
