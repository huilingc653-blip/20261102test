package com.bytter.system.inneraccount.service;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.vo.FcAccVO;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * system内部帐号服务
 */
@Service
public class InnerAccountService {
    @Autowired
    private IFcAccService fcAccServiceI;
    /**
     * 根据账号和调整类型 改变利率
     * @param account
     * @param instrate
     * @param instrtType
     */
    public void updateRate(String account, BigDecimal instrate, String instrtType) {
        FcAccVO fcAcc = fcAccServiceI.findFcAccByBlnctrAcc(account);
        String accType = fcAcc.getAccType();
        InstrtType instrtTypeEnum = InstrtType.of(Integer.parseInt(instrtType));
        String colName = instrtTypeEnum.getDesc();
        if (InstrtType._06 == instrtTypeEnum) {
            if(AccTyp._05.code.equals(accType) || AccTyp._08.code.equals(accType)){
                colName="delayRate";
            }
        }
        fcAccServiceI.updateRate(account, colName,instrate);
    }
    /**
     * 内部帐号
     * @param corpIdList
     * @param joinSql
     * @return
     */
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, String joinSql){
        return fcAccServiceI.getFcAccByCorpIdList(corpIdList, AccStatus.getNP(), joinSql);
    }
    public List<FcAccDTO> listFcAccInfo(List<String> ids){
        return fcAccServiceI.listFcAccInfo(ids,AccStatus.getNP());
    }
    public List<FcAccDTO> getFcAccByCorpIdListContainDel(List<String> corpIdList, String joinSql) {
        return fcAccServiceI.getFcAccByCorpIdList(corpIdList, AccStatus.getNPD(), joinSql);
    }
    public List<FcAccDTO> listFcAccForHavDel(List<String> ids){
        return this.listFcAccByIdsAndStatus(ids, AccStatus.getNPD());
    }
    public List<FcAccDTO> listFcAccByIdsAndStatus(List<String> ids,List<String> status){
        return fcAccServiceI.listFcAccInfo(ids, status);
    }
    public List<String> getFcAccIdsByCorpIdList(List<String> corpIdList, String joinSql){
        return this.getFcAccByCorpIdList(corpIdList, joinSql).stream()
                .map(FcAccDTO::getId).collect(Collectors.toList());
    }
    public List<String> getFcAccsByCorpIdList(List<String> corpIdList, String joinSql){
        return fcAccServiceI.getFcAccsByCorpIdList(corpIdList, AccStatus.getNP(), joinSql);
    }
    public List<FcAccDTO> listFcAccInfo(List<String> ids, boolean containDelBankAcc){
        List<String> accStates = containDelBankAcc ? AccStatus.getNPD() : AccStatus.getNP();
        return fcAccServiceI.listFcAccInfo(ids,accStates);
    }
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, String joinSql, boolean containDelBankAcc){
        List<String> accStates = containDelBankAcc ? AccStatus.getNPD() : AccStatus.getNP();
        return fcAccServiceI.getFcAccByCorpIdList(corpIdList, accStates, joinSql);
    }
    @AllArgsConstructor
    @Getter
    enum AccStatus {
        N("正常"),
        P("部分冻结"),
        D("注销"),;
        private final String desc;
        public static List<String> getNP() {
            return Lists.newArrayList(N.name(), P.name());
        }
        public static List<String> getNPD() {
            return Lists.newArrayList(N.name(), P.name(), D.name());
        }
    }
    @AllArgsConstructor
    @Getter
    enum InstrtType{
        _01(1,"interestRate"),
        _02(2,"overdueRate"),
        _03(3,"overdueRate"),
        _04(4,"overdraftRate"),
        _05(5,"overdueRate"),
        _06(6,"overdraftRate"),
        _07(7,"delayRate"),
        UNK(-1, ""),;
        private final Integer code;
        private final String desc;
        /**
         * 获取InstraType
         * @param value code
         * @return enum
         */
        public static InstrtType of(Integer value) {
            return Arrays.stream(InstrtType.values())
                    .filter(item -> item.getCode().equals(value))
                    .findFirst().orElse(UNK);
        }
        public static final String FCACC_BANK_DEPOSIT = "08";
    }
    @AllArgsConstructor
    @Getter
    enum AccTyp{
        _05("05", "企业定期"),
        _08("08","通知存款"),;
        private final String code;
        private final String desc;
    }
}
