package com.bytter.core.sequence.strategy;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
/**
 * @author Covenanter
 * @date 17/5/28
 */
public class BankSequenceStrategy extends BaseSequenceStrategy {
    public BankSequenceStrategy(ISequenceEnum sequenceEnum) {
        super(sequenceEnum);
    }
    @Override
    public String getSequencePlaceHolder() {
        return "";
    }
}
