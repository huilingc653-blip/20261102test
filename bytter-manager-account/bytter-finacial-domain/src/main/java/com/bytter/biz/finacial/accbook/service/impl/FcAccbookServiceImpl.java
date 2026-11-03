package com.bytter.biz.finacial.accbook.service.impl;

import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlnedDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.service.*;
import com.bytter.biz.finacial.accbook.vo.FcAccbookExtVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 柜台账务对外接口实现类
 *
 * @author liyh
 */
@Service("fcAccbookServiceImpl")
@Transactional
@Slf4j
@DubboService
public class FcAccbookServiceImpl implements IFcAccbookService {
    @Autowired
    private FcAccbookInvalidService invalidService;
    @Autowired
    private FcAccbookService fcAccbookService;
    @Autowired
    private FcAccbookRushService rushService;
    @Autowired
    private FcChargeAccrueService chargeAccrueService;
    @Autowired
    private FcAccbookDtlService fcAccbookDtlService;
    @Autowired
    private FcAccbookBlnedService bookBlnedService;
    @Autowired
    private FcAccbookBlnedDtlService bookBlnedDtlService;
    @Autowired
    private FcAccbookExtService fcAccbookExtService;
    @Autowired
    private FcAccbookDao fcAccbookDao;
    @DubboReference
    private IFcVoucherTypeService voucherTypeService;
    /**
     * 功能介绍 : 转换凭证对象。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    private VoucherWrapper transVoucher(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);//复制属性
        //凭证对象
        List<FcAccbookVoucherDTO> target = voucher.getTarget();
        if (null != target && !target.isEmpty()) {
            List<FcAccbookVoucherVO> ntarget = new ArrayList<FcAccbookVoucherVO>();
            for (FcAccbookVoucherDTO voucherDTO : target) {
                FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
                BeanUtils.copyProperties(voucherDTO, voucherVO);
                FcAccbookDTO bookDTO = voucherDTO.getAccbook();
                if (null == bookDTO.getInterestStartDate()) {
                    bookDTO.setInterestStartDate(bookDTO.getTallyDate());//起息日期默认记账日期
                }
                if (null != bookDTO) {
                    bookDTO.setInnerSign(voucher.getInnerSign());//内转资金计划内转标识
                    FcAccbook book = new FcAccbook();
                    BeanUtils.copyProperties(bookDTO, book);
                    voucherVO.setAccbook(book);
                }
                FcAccbookExtDTO bookExtDTO = voucherDTO.getBookExt();
                if (null != bookExtDTO) {
                    FcAccbookExt bookExt = new FcAccbookExt();
                    BeanUtils.copyProperties(bookExtDTO, bookExt);
                    bookExt.setExternalProv(bookExtDTO.getProv());//省市
                    bookExt.setExternalCity(bookExtDTO.getCity());
                    voucherVO.setBookExt(bookExt);
                }
                List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtl> accbookDtls = new ArrayList<FcAccbookDtl>();
                    List<FcAccbookDtl> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtl.class);
                    for (FcAccbookDtl accbookDtl : nbookDtls) {
                        if (null == accbookDtl.getDebitMoney()) {
                            accbookDtl.setDebitMoney(BigDecimal.ZERO);
                        }
                        if (null == accbookDtl.getCreditMoney()) {
                            accbookDtl.setCreditMoney(BigDecimal.ZERO);
                        }
                        if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0
                                || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                            accbookDtls.add(accbookDtl);
                        }
                    }
                    voucherVO.setBookDtls(accbookDtls);
                }
                ntarget.add(voucherVO);
            }
            wrapper.setTarget(ntarget);
        }
        //原凭证对象
        List<FcAccbookVoucherDTO> oriTarget = voucher.getOriTarget();
        if (null != oriTarget && !oriTarget.isEmpty()) {
            List<FcAccbookVoucherVO> ntarget = new ArrayList<FcAccbookVoucherVO>();
            for (FcAccbookVoucherDTO voucherDTO : oriTarget) {
                FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
                BeanUtils.copyProperties(voucherDTO, voucherVO);
                List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtl> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtl.class);
                    voucherVO.setBookDtls(nbookDtls);
                }
                ntarget.add(voucherVO);
            }
            wrapper.setOriTarget(ntarget);
        }
        return wrapper;
    }
    /**
     * 功能介绍 : 转换凭证对象。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    private VoucherWrapperDTO transWrapper(VoucherWrapperDTO wrapper, VoucherWrapper voucher) {
        BeanUtils.copyProperties(voucher, wrapper);//复制属性
        //凭证对象
        List<FcAccbookVoucherVO> target = voucher.getTarget();
        if (null != target && !target.isEmpty()) {
            List<FcAccbookVoucherDTO> ntarget = new ArrayList<FcAccbookVoucherDTO>();
            for (FcAccbookVoucherVO voucherDTO : target) {
                FcAccbookVoucherDTO voucherVO = new FcAccbookVoucherDTO();
                BeanUtils.copyProperties(voucherDTO, voucherVO);
                FcAccbook bookDTO = voucherDTO.getAccbook();
                if (null != bookDTO) {
                    FcAccbookDTO book = new FcAccbookDTO();
                    BeanUtils.copyProperties(bookDTO, book);
                    voucherVO.setAccbook(book);
                }
                List<FcAccbookDtl> bookDtls = voucherDTO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtlDTO> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlDTO.class);
                    voucherVO.setBookDtls(nbookDtls);
                }
                ntarget.add(voucherVO);
            }
            wrapper.setTarget(ntarget);
        }
        //原凭证对象
        List<FcAccbookVoucherVO> oriTarget = voucher.getOriTarget();
        if (null != oriTarget && !oriTarget.isEmpty()) {
            List<FcAccbookVoucherDTO> ntarget = new ArrayList<FcAccbookVoucherDTO>();
            for (FcAccbookVoucherVO voucherDTO : oriTarget) {
                FcAccbookVoucherDTO voucherVO = new FcAccbookVoucherDTO();
                BeanUtils.copyProperties(voucherDTO, voucherVO);
                List<FcAccbookDtl> bookDtls = voucherDTO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtlDTO> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlDTO.class);
                    voucherVO.setBookDtls(nbookDtls);
                }
                ntarget.add(voucherVO);
            }
            wrapper.setOriTarget(ntarget);
        }
        return wrapper;
    }
    @Override
    public VoucherWrapperDTO saveVoucher(VoucherWrapperDTO voucher) {
        // 提供给外部调用接口
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setTallerUser(wrapper);
//		设置现金流项目
        fcAccbookService.setCashFlow(wrapper);
        fcAccbookService.saveFcAccbook(wrapper);
        return transWrapper(voucher, wrapper);
    }
    @Override
    public VoucherWrapperDTO submitVoucher(VoucherWrapperDTO voucher) {
        log.error("进入this.fcAccbookService.submitVoucher方法");
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setTallerUser(wrapper);
        //		设置现金流项目
        fcAccbookService.setCashFlow(wrapper);
        fcAccbookService.submitFcAccbook(wrapper);
        voucher.setVoucherNo(wrapper.getBookVoucher().getAccbook().getVoucherNo());
        voucher = transWrapper(voucher, wrapper);
        return voucher;
    }
    @Override
    public VoucherWrapperDTO checkVoucher(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setCheckUser(wrapper);
        //凭证记账复核 里面有数据保存 也有指令操作
        fcAccbookService.checkFcAccbook(wrapper);
        voucher.setVoucherNo(wrapper.getBookVoucher().getAccbook().getVoucherNo());
        return  transWrapper(voucher, wrapper);
    }
    @Override
    public VoucherWrapperDTO backVoucher(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setCheckUser(wrapper);
        fcAccbookService.backFcAccbook(wrapper);
        return transWrapper(voucher, wrapper);
    }
    @Override
    public VoucherWrapperDTO deleteVoucher(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setTallerUser(wrapper);
        fcAccbookService.deleteFcAccbook(wrapper);
        return transWrapper(voucher, wrapper);
    }
    @Override
    public void invalidVoucher(VoucherWrapperDTO voucher) {
        voucher.setProcessType(ProcessType.PROCESS_INVALID);//作废凭证
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setTallerUser(wrapper);
        invalidService.processInvalidVoucherSubmit(wrapper);
        voucher = transWrapper(voucher, wrapper);
    }
    @Override
    public void rushVoucher(VoucherWrapperDTO voucher) {
        voucher.setProcessType(ProcessType.PROCESS_RUSH);//冲销凭证
        VoucherWrapper wrapper = transVoucher(voucher);
        fcAccbookService.setTallerUser(wrapper);
        rushService.submitRushVoucher(wrapper);
        voucher = transWrapper(voucher, wrapper);
    }
    @Override
    public VoucherWrapperDTO getVoucher(Integer oriType, String busGlobalId) {
        return fcAccbookService.getVoucherByBusGlobalId(busGlobalId);
    }
    @Override
    public VoucherWrapperDTO getVoucherByVoucherNo(String voucherNo) {
        return bookBlnedService.getVoucherByVoucherNo(voucherNo);
    }
    @Override
    public Map<String, BigDecimal> getNotOverChargeByAccount(String account) {
        return chargeAccrueService.getNotOverChargeByAccount(account);
    }
    @Override
    public boolean existsUnCheckAccbook(String blnctrAcc, String busGlobalId) {
        return fcAccbookDtlService.existUnCheckAccbook(blnctrAcc, busGlobalId);
    }
    @Override
    public FcAccbookCloseDTO getAccbookCloseDTO(String billId, Integer busOriType) {
        return bookBlnedService.geAccbookCloseDTOByBillId(billId, busOriType);
    }
    @Override
    public void updateFcAccFbsStatus(String id, String status, boolean dayCloseSign) {
        if (dayCloseSign) {
            FcAccbookBlnedDtl dtl = bookBlnedDtlService.get(id);
            dtl.setFbsStatus(status);
            bookBlnedDtlService.updateEntity(dtl);
        } else {
            FcAccbookDtl dtl = fcAccbookDtlService.get(id);
            dtl.setFbsStatus(status);
            fcAccbookDtlService.updateEntity(dtl);
        }
    }
    /**
     * 根据凭证号获取凭证信息
     *
     * @param voucherNo
     * @return
     */
    @Override
    public FcAccbookDTO getBookVOByVoucherNo(String voucherNo) {
        FcAccbookVO fcAccbook = fcAccbookService.getAccbookByVoucherNo(voucherNo);
        FcAccbookDTO dto = new FcAccbookDTO();
        if(null != fcAccbook) {
            BeanUtils.copyProperties(fcAccbook, dto);
        }
        return dto;
    }
    @Override
    public FcAccbookDTO getBookVOByOrderNo(String orderNo, String isPool) {
        FcAccbookVO fcAccbook = fcAccbookService.getBookVOByOrderNo(orderNo, isPool);
        FcAccbookDTO dto = new FcAccbookDTO();
        if (Objects.nonNull(fcAccbook)){
            BeanUtils.copyProperties(fcAccbook, dto);
        }else{
            return null;
        }
        return dto;
    }
    @Override
    public FcAccbookDTO saveFcAccbook(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);
        List<FcAccbookVoucherVO> fcVoList = new ArrayList<FcAccbookVoucherVO>();
        FcAccbookVoucherVO vo = null;
        for (int i = 0; i < wrapper.getTarget().size(); i++) {
            vo = new FcAccbookVoucherVO();
            FcAccbook book = new FcAccbook();
            FcAccbookExt ext = new FcAccbookExt();
            List<FcAccbookDtl> dtlList = new ArrayList<>();
            BeanUtils.copyProperties(voucher.getTarget().get(i).getAccbook(), book);
            BeanUtils.copyProperties(voucher.getTarget().get(i).getBookExt(), ext);
            BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class);
            vo.setAccbook(book);
            vo.setBookExt(ext);
            dtlList.addAll(BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class));
            vo.setBookDtls(dtlList);
            fcVoList.add(vo);
            wrapper.setTarget(fcVoList);
        }
        //		设置现金流项目
        fcAccbookService.setCashFlow(wrapper);
        fcAccbookService.saveFcAccbook(wrapper);
        BeanUtils.copyProperties(wrapper.getTarget().get(0).getAccbook(), voucher.getTarget().get(0).getAccbook());
        return voucher.getTarget().get(0).getAccbook();
    }
    @Override
    public FcAccbookDTO submitFcAccbook(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);
        List<FcAccbookVoucherVO> fcVoList = new ArrayList<FcAccbookVoucherVO>();
        FcAccbookVoucherVO vo = null;
        for (int i = 0; i < wrapper.getTarget().size(); i++) {
            vo = new FcAccbookVoucherVO();
            FcAccbook book = new FcAccbook();
            FcAccbookExt ext = new FcAccbookExt();
            List<FcAccbookDtl> dtlList = new ArrayList<>();
            BeanUtils.copyProperties(voucher.getTarget().get(i).getAccbook(), book);
            if (!EmptyUtils.isEmpty(voucher.getTarget().get(i).getBookExt())) {
                BeanUtils.copyProperties(voucher.getTarget().get(i).getBookExt(), ext);
            }
            //BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class);
            vo.setAccbook(book);
            vo.setBookExt(ext);
            dtlList.addAll(BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class));
            vo.setBookDtls(dtlList);
            fcVoList.add(vo);
            wrapper.setTarget(fcVoList);
        }
        //		设置现金流项目
        fcAccbookService.setCashFlow(wrapper);
        //记账提交凭证对象,里面有消息处理
        fcAccbookService.submitFcAccbook(wrapper); //里面有 指令相关操作
        BeanUtils.copyNotNullProperties(wrapper.getTarget().get(0).getAccbook(), voucher.getTarget().get(0).getAccbook());
        return voucher.getTarget().get(0).getAccbook();
    }
    @Override
    public VoucherWrapperDTO checkFcAccbook(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);
        List<FcAccbookVoucherVO> fcVoList = new ArrayList<FcAccbookVoucherVO>();
        FcAccbookVoucherVO vo = null;
        for (int i = 0; i < wrapper.getTarget().size(); i++) {
            vo = new FcAccbookVoucherVO();
            FcAccbook book = new FcAccbook();
            FcAccbookExt ext = new FcAccbookExt();
            List<FcAccbookDtl> dtlList = new ArrayList<>();
            BeanUtils.copyProperties(voucher.getTarget().get(i).getAccbook(), book);
            BeanUtils.copyProperties(voucher.getTarget().get(i).getBookExt(), ext);
            BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class);
            vo.setAccbook(book);
            vo.setBookExt(ext);
            dtlList.addAll(BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class));
            vo.setBookDtls(dtlList);
            fcVoList.add(vo);
            wrapper.setTarget(fcVoList);
        }
        wrapper.setBusGlobalId(voucher.getTarget().get(0).getAccbook().getBusGlobalId());
        fcAccbookService.checkFcAccbook(wrapper);
        BeanUtils.copyProperties(wrapper.getTarget().get(0).getAccbook(), voucher.getTarget().get(0).getAccbook());
        return voucher;
    }
    @Override
    public void deleteFcAccbook(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);
        List<FcAccbookVoucherVO> fcVoList = new ArrayList<FcAccbookVoucherVO>();
        FcAccbookVoucherVO vo = null;
        for (int i = 0; i < wrapper.getTarget().size(); i++) {
            vo = new FcAccbookVoucherVO();
            FcAccbook book = new FcAccbook();
            FcAccbookExt ext = new FcAccbookExt();
            List<FcAccbookDtl> dtlList = new ArrayList<>();
            BeanUtils.copyProperties(voucher.getTarget().get(i).getAccbook(), book);
            if (null != voucher.getTarget().get(i).getBookExt()) {
                BeanUtils.copyProperties(voucher.getTarget().get(i).getBookExt(), ext);
            }
            BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class);
            vo.setAccbook(book);
            vo.setBookExt(ext);
            dtlList.addAll(BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class));
            vo.setBookDtls(dtlList);
            fcVoList.add(vo);
            wrapper.setTarget(fcVoList);
        }
        fcAccbookService.deleteFcAccbook(wrapper);
        BeanUtils.copyProperties(wrapper.getTarget().get(0).getAccbook(), voucher.getTarget().get(0).getAccbook());
    }
    @Override
    public void backFcAccbook(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);
        List<FcAccbookVoucherVO> fcVoList = new ArrayList<FcAccbookVoucherVO>();
        FcAccbookVoucherVO vo = null;
        for (int i = 0; i < wrapper.getTarget().size(); i++) {
            vo = new FcAccbookVoucherVO();
            FcAccbook book = new FcAccbook();
            FcAccbookExt ext = new FcAccbookExt();
            List<FcAccbookDtl> dtlList = new ArrayList<>();
            BeanUtils.copyProperties(voucher.getTarget().get(i).getAccbook(), book);
            BeanUtils.copyProperties(voucher.getTarget().get(i).getBookExt(), ext);
            BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class);
            vo.setAccbook(book);
            vo.setBookExt(ext);
            dtlList.addAll(BeanUtils.copyListProperties(voucher.getTarget().get(i).getBookDtls(), FcAccbookDtl.class));
            vo.setBookDtls(dtlList);
            fcVoList.add(vo);
            wrapper.setTarget(fcVoList);
        }
        fcAccbookService.backFcAccbook(wrapper);
        BeanUtils.copyProperties(wrapper.getTarget().get(0).getAccbook(), voucher.getTarget().get(0).getAccbook());
    }
    @Override
    public FcAccbookExtDTO getBookExtVOByBookId(String bookId) {
        FcAccbookExtVO vo = fcAccbookExtService.getBookExtVOByBookId(bookId);
        FcAccbookExtDTO dto = new FcAccbookExtDTO();
        BeanUtils.copyProperties(vo, dto);
        return dto;
    }
    @Override
    public List<FcAccbookDtlDTO> getPoolPageDtlsByBookId(String bookId) {
        return fcAccbookDtlService.getPoolPageDtlsByBookId(bookId);
    }
    /**
     * 按照计息参数获取相关利息账号
     */
    @Override
    public List<FcAccDTO> getInterestAcc(String curCode, String netId, String corpId, String subjectCode, String busType, String bankCode, String user, String msg) {
        return fcAccbookService.getInterestAcc(curCode, netId, corpId, subjectCode, busType, bankCode, user, msg);
    }
    @Override
    public FcAccbookDTO getAccbookByBusGlobalId(String busGlobalId) {
        FcAccbook fcAccbook = fcAccbookService.getAccbookByBusGlobalId(busGlobalId);
        FcAccbookDTO fcAccbookDTO = new FcAccbookDTO();
        BeanUtils.copyProperties(fcAccbook, fcAccbookDTO);
        return fcAccbookDTO;
    }
    /*@Override
    public CorpDTO getUserNet(ICurrentUser user) {
        return fcAccbookService.getUserNet(user);
    }*/
    @Override
    public List<String> getTallyRecordListId(List<String> billId) {
        return fcAccbookService.getTallyRecordListId(billId);
    }
    @Override
    public List<Map<String, Object>> getOrderVoucherByGlobalId(List<String> busGlobalId, String voucherState, String orderNo, String voucherNo) {
        return fcAccbookDao.getOrderVoucherByGlobalId(busGlobalId, voucherState, orderNo, voucherNo);
    }
    @Override
    public FcAccbookDTO getAccBookVoById(String cbsBillId) {
        FcAccbookVO accBook = fcAccbookDao.getAccbookVoById(cbsBillId, false);
        FcAccbookDTO dto = new FcAccbookDTO();
        FcVoucherTypeDTO voucherType = voucherTypeService.getFcVoucherTypeById(accBook.getVoucherType());
        accBook.setVoucherName(voucherType.getVoucherName());
        BeanUtils.copyNotNullProperties(accBook, dto);
        return dto;
    }
    @Override
    public FcAccbookDTO getAccBookVOByOrigVoucherNo(String billCode) {
        FcAccbookVO bookVo = fcAccbookDao.getAccbookVOByOrigVoucherNo(billCode);
        FcAccbookDTO dto = new FcAccbookDTO();
        BeanUtils.copyNotNullProperties(bookVo, dto);
        return dto;
    }

    @Override
    public List<FcAccbookDTO> listByInCorpIdAndBusOriTypeAndInState(List<String> corpIds, String busOriType, List<String> voucherStates) {
        return  fcAccbookDao.listByInCorpIdAndBusOriTypeAndInState(corpIds,busOriType,voucherStates);
    }
	@Override
	public VoucherWrapperDTO checkSubmitVoucher(VoucherWrapperDTO voucher) {
		 	log.error("进入this.fcAccbookService.submitVoucher方法");
	        VoucherWrapper wrapper = transVoucher(voucher);
	        wrapper.setAutoAccounter(true);
	        fcAccbookService.setTallerUser(wrapper);
	        //		设置现金流项目
	        fcAccbookService.setCashFlow(wrapper);
	        fcAccbookService.checkSubmitFcAccbook(wrapper);
	        voucher.setVoucherNo(wrapper.getBookVoucher().getAccbook().getVoucherNo());
	        voucher = transWrapper(voucher, wrapper);
	        return voucher;
	}
}
