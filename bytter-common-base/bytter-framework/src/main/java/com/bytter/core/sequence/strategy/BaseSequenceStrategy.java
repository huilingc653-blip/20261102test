package com.bytter.core.sequence.strategy;
import org.springframework.util.StringUtils;
import com.bytter.core.sequence.enums.SequenceDateEnum;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
import java.util.Date;
/**
 * @author zhoul
 * @date 2017/5/31
 */
public abstract class BaseSequenceStrategy {
    protected ISequenceEnum sequenceEnum;
    public BaseSequenceStrategy(ISequenceEnum sequenceEnum) {
        this.sequenceEnum = sequenceEnum;
    }
    protected String getPrefix() {
        String prefix = sequenceEnum.getPrefix();
        if (StringUtils.hasText(prefix)) {
            return prefix;
        }
        return "";
    }
    protected String getDate() {
        SequenceDateEnum dateEnum = sequenceEnum.getDateEnum();
        if (dateEnum == null) {
            return "";
        }
        return dateEnum.getSimpleDateFormat().format(new Date());
    }
    /**
     * 获取流水占位符，模版方法
     *
     * @return 获取流水占位符
     */
    public abstract String getSequencePlaceHolder();
}
