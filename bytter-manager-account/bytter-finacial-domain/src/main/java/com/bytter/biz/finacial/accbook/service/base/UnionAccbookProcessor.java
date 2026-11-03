package com.bytter.biz.finacial.accbook.service.base;
import com.bytter.biz.finacial.api.dto.FcUnionAccbookDTO;
import com.bytter.biz.finacial.tally.service.impl.TallyServiceImpl;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.system.api.IFcAccService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
//import com.bytter.tally.api.dto.FcUnionAccbookDTO;
//import com.bytter.tally.api.service.IFcUnionAccbookService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service("unionAccbookProcessor")
@Transactional
public class UnionAccbookProcessor extends BaseService implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnionAccbookProcessor.class);
    @DubboReference(check = false)
    private IFcAccService iFcAccService;

    /*@DubboReference(check = false)
    private IFcUnionAccbookService iFcUnionAccbookService;*/

    @Autowired
    private TallyServiceImpl tallyServiceImpl;

    public void processUnionAccbook(VoucherWrapper voucher) {
        if (isCreditInterest(voucher)) {
            return;
        }
        if (voucher.getProcessType() == ProcessType.PROCESS_COMMON) {//记账经办
            if (voucher.getOpType() == OperateType.DELETE && isReceiveUnionAccbook(voucher.getBookVoucher().getAccbook())) {//来账抹账
                //delRecevieUnionAccbook(voucher);
            } else if (voucher.getOpType() == OperateType.CHECK && !isReceiveUnionAccbook(voucher.getBookVoucher().getAccbook())) {//往账记账复核
                saveSendUnionAccbook(voucher); //检测账务是否能生成并保存往来信息
            }
        } else if (voucher.getProcessType() == ProcessType.PROCESS_RUSH) {//冲账
            rushUnionAccbook(voucher);
        } else {//作废
            invalidUnionAccbook(voucher);
        }
    }
    /**
     * 检测账务是否能生成并保存往来信息
     * @param voucher
     */
    private void saveSendUnionAccbook(VoucherWrapper voucher) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始处理往账信息。");
        }
        List<FcUnionAccbookDTO> unionAccbooks = new ArrayList<>();
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            /**
             * 联行来账的数据不继续联行
             */
            if (FinaBookConstant.BusOriType.NB_COLLECTION_NET.get().equals(accbook.getBusOriType())) {
                continue;
            }
            for (FcAccbookDtl dtl : voucherVO.getBookDtls()) {
                FcAccDTO fcAcc = dtl.getAccDTO();
                if(null == fcAcc){
                    fcAcc = iFcAccService.getAccDTOByAccount(dtl.getAccount());//获取记账账户信息
                }
                if (isProccessSendUnionAccbook(fcAcc)) {//账户包含联行账号和联行网点
                    unionAccbooks.add(createUnionAccbook(voucherVO, dtl, fcAcc)); // TODO create里面逻辑感觉有点问题 unionAccbook.setBillState(FinaBookConstant.UnionAccbookBillState.W.get());
                }
            }
        }
        for (FcUnionAccbookDTO unionAccbook : unionAccbooks) {
//            iFcUnionAccbookService.saveFcUnionAccbook(unionAccbook);
            tallyServiceImpl.saveUnionAccBook(unionAccbook); //保存联行来账信息
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("结束处理往账信息。");
        }
    }
    private FcUnionAccbookDTO createUnionAccbook(FcAccbookVoucherVO voucherVO, FcAccbookDtl dtl, FcAccDTO fcAcc) {
        FcAccbook accbook = voucherVO.getAccbook();
        FcAccbookExt accbookExt = voucherVO.getBookExt();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始构建往账信息。");
        }
        FcUnionAccbookDTO unionAccbook = new FcUnionAccbookDTO();
        unionAccbook.setSendVoucher(accbook.getVoucherNo());
        unionAccbook.setSendBusGlobalId(accbook.getBusGlobalId());
        unionAccbook.setSendAcc(fcAcc.getBlnctrAcc());//触发联行业务的账号
        unionAccbook.setSendNet(fcAcc.getNetId());
        unionAccbook.setSendVoucherDate(accbook.getTallyDate());
        unionAccbook.setSummaryContent(dtl.getSummaryContent());
        unionAccbook.setVoucherType(accbook.getVoucherType());
        unionAccbook.setReceiveNet(fcAcc.getUnionNet());//联行网点
        unionAccbook.setReceiveAcc(fcAcc.getUnionAcc());//联行账号
        unionAccbook.setBillState(FinaBookConstant.UnionAccbookBillState.W.get());
        unionAccbook.setBusOriType(FinaBookConstant.UnionAccbookBusOriType.N2N_COLLECT.get());
        if(!FinaBookConstant.BusOriType.TAILLY.getValue().equals(accbook.getBusOriType())) {
            unionAccbook.setExternalProv(accbookExt.getProv());
            unionAccbook.setExternalCity(accbookExt.getCity());
            String startCorp = accbookExt.getStartCorp();//联行相关的账户信息
            if (EmptyUtils.isBlank(startCorp)) {
                unionAccbook.setBank(accbookExt.getExternalBank());
                unionAccbook.setExternalCorp(accbookExt.getExternalCorp());
                unionAccbook.setExternalAcc(accbookExt.getExternalAcc());
            } else {
                unionAccbook.setBank(accbookExt.getStartBank());
                unionAccbook.setExternalCorp(accbookExt.getStartCorp());
                unionAccbook.setExternalAcc(accbookExt.getStartCorpAcc());//联行网点需要记账的账号
            }
        }
        BigDecimal money = dtl.getDebitMoney();
        if (money == null || BigDecimal.ZERO.compareTo(money) == 0) {
            unionAccbook.setSendMoney(dtl.getCreditMoney());
            unionAccbook.setWay(Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT));//贷方
        } else {
            unionAccbook.setSendMoney(money);
            unionAccbook.setWay(Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT));//借方
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("构建往账信息完成,信息为：" + unionAccbook);
        }
        return unionAccbook;
    }
    /**
     * 判断往来信息是否已记账，如果已记账，则不处理
     * @param accbook
     */
    public boolean updateUnionAccbook(FcAccbook accbook) {
//        List<FcUnionAccbookDTO> accbookDTOS = iFcUnionAccbookService.getUnionAccbookBySendNo(accbook.getVoucherNo());
        List<FcUnionAccbookDTO> accbookDTOS = tallyServiceImpl.getUnionAccBookBySendNo(accbook.getVoucherNo());
        if(EmptyUtils.isEmptyList(accbookDTOS)) {
            return false;
        }
        boolean flag = false;
        for (FcUnionAccbookDTO accbookDto : accbookDTOS) {
            String billState = accbookDto.getBillState();//往来信息状态
            if(!FinaBookConstant.UnionAccbookBillState.W.get().equals(billState)) {
                continue;
            }
            //如果往来信息未记账，则往来信息标识为已作废处理
            /*iFcUnionAccbookService.updateUnionAccbookBillState(accbookDto.getId(),
                    FinaBookConstant.UnionAccbookBillState.B.get(), accbookDto.getBillState());*/
            tallyServiceImpl.updateUnionAccBookBillState(accbookDto.getId(),
                    FinaBookConstant.UnionAccbookBillState.B.get(), accbookDto.getBillState());
        }
        return false;
    }
    /**
     * 作废往来信息
     * @param voucher
     */
    private void invalidUnionAccbook(VoucherWrapper voucher) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始作废处理往账信息。");
        }
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("往账作废,处理联行往来凭证，往账凭证号为" + accbook.getVoucherNo());
            }
            updateUnionAccbook(accbook);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("往账作废,更新往账" + accbook.getVoucherNo() + "的联行往来信息完毕。");
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("结束作废处理往账信息。");
        }
    }
    /**
     * 冲销往来信息
     * @param voucher
     */
    private void rushUnionAccbook(VoucherWrapper voucher) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始冲销处理往账信息。");
        }
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("往账冲销,处理联行往来凭证，往账凭证号为" + accbook.getVoucherNo());
            }
            updateUnionAccbook(accbook);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("往账冲销,更新往账" + accbook.getVoucherNo() + "的联行往来信息完毕。");
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("结束冲销处理往账信息。");
        }
    }
    /**
     * 是否利息账务或者信贷账务
     * @param voucher
     * @return
     */
    private boolean isCreditInterest(VoucherWrapper voucher) {
        String generateMode = voucher.getBookVoucher().getAccbook().getGenerateMode();
        return FinaBookConstant.GenerateMode
                .CREDIT_BOOK.get().equals(generateMode)
                || FinaBookConstant.GenerateMode.INTEREST.get().equals(generateMode);
    }
    /**
     * 是否是联行来账产生的账务
     * @param accbook
     * @return
     */
    private boolean isReceiveUnionAccbook(FcAccbook accbook) {
        return accbook.getGenerateMode().equals(FinaBookConstant.GenerateMode.UNION_ACCBOOK.get());
    }
    /**
     * 账户是否有联行网点和对应联行账户
     * @param fcAcc
     * @return
     */
    private boolean isProccessSendUnionAccbook(FcAccDTO fcAcc) {
        return EmptyUtils.isNotEmpty(fcAcc.getUnionNet()) && EmptyUtils.isNotEmpty(fcAcc.getUnionAcc());
    }

    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
