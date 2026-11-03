package com.bytter.core.sequence.strategy;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
/**
 * @author Zhoul
 * @date 17/5/28
 */
public class NormalSequenceStrategy extends BaseSequenceStrategy {
    public NormalSequenceStrategy(ISequenceEnum sequenceEnum) {
        super(sequenceEnum);
    }
    @Override
    public String getSequencePlaceHolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPrefix()).append(getDate()).append("%s");
        return sb.toString();
    }
}
