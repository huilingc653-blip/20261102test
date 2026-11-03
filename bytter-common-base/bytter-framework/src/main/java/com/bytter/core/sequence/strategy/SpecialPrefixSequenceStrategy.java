package com.bytter.core.sequence.strategy;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
/**
 * 账务流水号策略
 *
 * @author liyh
 */
public class SpecialPrefixSequenceStrategy extends BaseSequenceStrategy {
    private String specialCode;
    public SpecialPrefixSequenceStrategy(ISequenceEnum sequenceEnum, String specialCode) {
        super(sequenceEnum);
        this.specialCode = specialCode;
    }
    @Override
    public String getSequencePlaceHolder() {
        StringBuilder sb = new StringBuilder(getPrefix());
        sb.append(specialCode).append(getDate()).append("%s");
        return sb.toString();
    }
}
