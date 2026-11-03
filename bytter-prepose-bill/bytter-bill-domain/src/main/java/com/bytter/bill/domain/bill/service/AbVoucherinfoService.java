package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.BillCreditInfoDao;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.bill.vo.v1.AbVoucherinfoPageVO;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 票据主信息
 * @date 2017-06-21 15:50:47
 */
@Service("abVoucherinfoService")
@Transactional
public class AbVoucherinfoService extends BaseService<AbVoucherinfo, String> {
    @Autowired
    private AbVoucherinfoDao abVoucherinfoDao;
    @Autowired
    private AbBackbillinfoService abBackbillinfoService;
    @Autowired
    private AbBillReceivebizService abBillReceivebizService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbInputBankInfoService abInputBankInfoService;
    @Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillAccrecordsDao billAccrecordsDao;
	@Autowired
	private BillCreditInfoDao billCreditInfoDao;
	@Autowired
	private AbDiscountService abDiscountService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(AbVoucherinfo entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }

    public BaseDao<AbVoucherinfo, String> getEntityDao() {
        return abVoucherinfoDao;
    }


    /**
     * 票据主表统一新增方法
     *
     * @param info   票据主信息
     * @param opType 0应付票据新增 1应收票据新增
     */
    public void saveOrUpdateAbVoucherinfo(AbVoucherinfo info, String opType, ICurrentUser userVo) {
        saveOrUpdateAbVoucherinfo(info, opType, userVo, null);
    }

    /**
     * 票据主表统一新增方法
     *
     * @param info   票据主信息
     * @param opType 0应付票据新增 1应收票据新增
     */
    public void saveOrUpdateAbVoucherinfoAnt(AbVoucherinfo info, String opType, ICurrentUser userVo) {
        //-1标志为同意清偿签收业务
        saveOrUpdateAbVoucherinfo(info, opType, userVo, "-1");
    }

    /**
     * 票据主表统一新增方法
     *
     * @param info   票据主信息
     * @param opType 0应付票据新增 1应收票据新增
     */
    public void saveOrUpdateAbVoucherinfo(AbVoucherinfo info, String opType, ICurrentUser userVo, String billSource) {
        if (StringUtils.isEmpty(info.getOrigNo()) && AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(opType)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ADDBILL_ORIGNOISNULL);
        }
        // 冗余字段
        // 收票人银行类型
        Optional.ofNullable(info.getPayeeacc()).ifPresent(payeeAcc -> {
            BtBankAccDTO bankAccInfo = abAccrecordsService.getBtBankAccInfo(payeeAcc);
            Optional.ofNullable(bankAccInfo).ifPresent(bankAcc -> info.setPayeeBankTypeId(bankAccInfo.getBankTypeId()));
        });
        // 承兑人账号银行类型
        Optional.ofNullable(info.getAcceptoracc()).ifPresent(acceptoracc -> {
            BtBankAccDTO bankAccInfo = abAccrecordsService.getBtBankAccInfo(acceptoracc);
            Optional.ofNullable(bankAccInfo).ifPresent(bankAcc -> info.setAcceptorAccBankTypeId(bankAccInfo.getBankTypeId()));
        });
        if (StringUtils.isNotEmpty(info.getId())) {
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(opType)
                    || (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(opType)
                    && AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(info.getSourceType())
                    && checkIsUpdateBillInfo(info))) {
                if (userVo != null) {
                    info.setUpdateBy(userVo.getLoginName());
                    info.setUpdateName(userVo.getUsername());
                }
                info.setUpdateDate(new Date());
                info.setSourceType(opType);
                AbVoucherinfo vouInfoOld = abVoucherinfoDao.get(info.getId());
                BeanUtils.copyNotNullProperties(info, vouInfoOld,"id");
                abVoucherinfoDao.update(vouInfoOld);
            }
        } else {
			List<String> subNoList = NewDraftDealMethod.subTicketIntervalLenLack(info.getSbbllRngStrtSn(),info.getSbbllRngEndSn());
			info.setSbbllRngStrtSn(subNoList.get(0));
			info.setSbbllRngEndSn(subNoList.get(1));
			if(info.getSbbllRngStrtSn().length()>1){
				BigDecimal subMoney = (new BigDecimal(info.getSbbllRngEndSn()).subtract(new BigDecimal(info.getSbbllRngStrtSn())).add(BigDecimal.ONE)).divide(new BigDecimal(100));
				if(subMoney.compareTo(info.getBillMoney()) != 0){
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
				}
			}
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(opType)) {
                AbVoucherinfo oldVoucherInfo = null;
                if ("-1".equals(billSource)) {
                    AbVoucherinfo oldVoucherInfoOverRang = abVoucherinfoDao.getAbVoucherinfoByOrigNoAndRangSn(info.getOrigNo(), info.getSbbllRngStrtSn(), info.getSbbllRngEndSn());
                    if (oldVoucherInfoOverRang == null || (oldVoucherInfoOverRang.getSbbllRngStrtSn() + oldVoucherInfoOverRang.getSbbllRngEndSn()).equals(info.getSbbllRngStrtSn() + info.getSbbllRngEndSn())) {
                        oldVoucherInfo = oldVoucherInfoOverRang;
                    } else {
                        //List<BillAbAccrecords> ll = billBankDao.getAbAccrecordsListByVidVvid(oldVoucherInfoOverRang.getId(),null);
                        //BillAbAccrecords businessInfo=billBankDao.getAccrecordsByRecordId("0009ed6609a44ca2ac2b48920be99222");
                        BillAbVoucherinfo newInfo = com.bytter.bill.domain.billbank.tool.BillTicketSplittingUtils.ticketSplitting(oldVoucherInfoOverRang.getId(), info.getBillMoney().toString(), billBankDao, billAccrecordsDao);
                        oldVoucherInfo = new AbVoucherinfo();
                        //int i = 1/0;
                        BeanUtils.copyNotNullProperties(newInfo, oldVoucherInfo);
                    }
                } else {
                    oldVoucherInfo = abVoucherinfoDao.getAbVoucherinfoByOrigNo(info.getOrigNo(), info.getSbbllRngStrtSn(), info.getSbbllRngEndSn());
                }
                //oldVoucherInfo = abVoucherinfoDao.getAbVoucherinfoByOrigNo(info.getOrigNo(),info.getSbbllRngStrtSn(),info.getSbbllRngEndSn());
                if (oldVoucherInfo != null) {
                    if (oldVoucherInfo.getBillMoney() == null) {
                        oldVoucherInfo.setBillMoney(info.getBillMoney());
                    }
                    info.setId(oldVoucherInfo.getId());
                    if ((AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(oldVoucherInfo.getBillFlag()))
                            && AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(oldVoucherInfo.getSourceType())
                            && checkIsUpdateBillInfo(info)) || (info.getSaveImport() != null && info.getSaveImport() == true)) {
                        if (userVo != null) {
                            info.setUpdateBy(userVo.getLoginName());
                            info.setUpdateName(userVo.getUsername());
                        }
                        info.setUpdateDate(new Date());
                        info.setSourceType(opType);
                        BeanUtils.copyNotNullProperties(info, oldVoucherInfo);
                        abVoucherinfoDao.update(oldVoucherInfo);
                    }
                } else {
                    if (userVo != null) {
                        info.setCreateBy(userVo.getLoginName());
                        info.setCreateName(userVo.getUsername());
                    }
                    info.setCreateDate(new Date());
                    info.setSourceType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
                    abVoucherinfoDao.save(info);
                }
            } else {
                if (userVo != null) {
                    info.setCreateBy(userVo.getLoginName());
                    info.setCreateName(userVo.getUsername());
                }
                info.setCreateDate(new Date());
                info.setSourceType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
                abVoucherinfoDao.save(info);
            }
        }
        abVoucherinfoDao.flush();
    }

	/**
	 * 检查此票据新是否允许修改 如果已有单位收入库存且审批通过以后吃票据信息不允许修改
	 * @param info
	 * @return 不允许修改访问false 可以修改返回true
	 */
	@Transactional(readOnly = true)
	public boolean checkIsUpdateBillInfo(AbVoucherinfo info) {
		Map<String, Object> params = new HashMap<>();
		String[] briefIds={AccebillConstant.BRIEF_STATUS_001,
				AccebillConstant.BRIEF_STATUS_6001,
				AccebillConstant.BRIEF_STATUS_9003,
				AccebillConstant.BRIEF_STATUS_9010,
				AccebillConstant.BRIEF_STATUS_9018};
		params.put("briefIds", briefIds);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS11);
		params.put("vid", info.getId());
		return abVoucherinfoDao.checkIsUpdateBillInfo(params);
	}

    /**
     * 修改票据主表票据状态
     *
     * @param id         票据主表ID是
     * @param billStatus 票据状态
     * @return 修改成功的笔数
     */
    public int updateBillStatus(String id, String billStatus, ICurrentUser userVo, String ebillBlCrclIndc) {
        int num = updateBillStatusOrOrigNo(id, billStatus, null, userVo, null, null, ebillBlCrclIndc);
        return num;
    }

    /**
     * 修改票据主表票据状态
     *
     * @param id         票据主表ID是
     * @param billStatus 票据状态
     * @return 修改成功的笔数
     * <p>
     * sbBllRngStrtSN 子票区间开始序号
     * <p>
     * sbBllRngEndSN 子票区间结束序号
     * <p>
     * ebillBlCrclIndc 流通标志
     */
    public int updateBillStatusOrOrigNo(String id, String billStatus, String origNo, ICurrentUser userVo, String sbBllRngStrtSN, String sbBllRngEndSN, String ebillBlCrclIndc) {
        String[] ids = {id};
        if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(billStatus)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (StringUtils.isNotEmpty(origNo)) {
            params.put("origNo", origNo);
        }
        if (StringUtils.isNotEmpty(sbBllRngStrtSN)) {
            params.put("sbBllRngStrtSN", sbBllRngStrtSN);
        }
        if (StringUtils.isNotEmpty(sbBllRngEndSN)) {
            params.put("sbBllRngEndSN", sbBllRngEndSN);
        }
        if (StringUtils.isNotEmpty(ebillBlCrclIndc)) {
            params.put("ebillBlCrclIndc", ebillBlCrclIndc);
        }
        params.put("billStatus", billStatus);
        params.put("updateBy", userVo.getLoginName());
        params.put("updateName", userVo.getUsername());
        params.put("updateDate", new Date());
        int num = abVoucherinfoDao.batchUpdate(params);
        return num;
    }

    /**
     * 根据票据号获取票据信息
     *
     * @param origNo
     * @return
     */
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbVoucherinfoVOByOrigNo(String origNo, String sbbllRngStrtSn, String sbbllRngEndSn) {
        AbVoucherinfo info = abVoucherinfoDao.getAbVoucherinfoByOrigNo(origNo, sbbllRngStrtSn, sbbllRngEndSn);
        AbVoucherinfoVO reInfo = null;
        if (info != null) {
            reInfo = new AbVoucherinfoVO();
            BeanUtils.copyNotNullProperties(info, reInfo);
        }
        return reInfo;
    }

	/**
	 * 根据票据流水表ID获取票据信息
	 *
	 * @param accId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbVoucherinfoVOByAccId(String accId) {
		AbAccrecords info = abAccrecordsService.get(accId, true);
		if (info != null) {
			String billStatus=null;
			if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())
					&&AccebillConstant.AB_BILL_BUS_STATUS95.equals(info.getStatus())
					&&AccebillConstant.AB_BILL_SEND_SIGN_N.equals(info.getSendSign())){
				if(AccebillConstant.BRIEF_STATUS_001.equals(info.getBriefId())
						||AccebillConstant.BRIEF_STATUS_003.equals(info.getBriefId())){
					billStatus=AccebillConstant.AB_BILL_STATUS_030006;
				}else if(AccebillConstant.BRIEF_STATUS_6001.equals(info.getBriefId())){
					AbVoucherinfoVice vice=abVoucherinfoViceService.get(info.getVvid(),true);
					if(vice!=null){
						if(AccebillConstant.AB_BILL_SOURCE_ENDORSE.equals(vice.getSource())){
							billStatus=AccebillConstant.AB_BILL_STATUS_100006;
						}else if(AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(vice.getSource())){
							billStatus=AccebillConstant.AB_BILL_STATUS_180006;
						}else if(AccebillConstant.AB_BILL_SOURCE_REC.equals(vice.getSource())){
							billStatus=AccebillConstant.AB_BILL_STATUS_030006;
						}
					}
				}else if(AccebillConstant.BRIEF_STATUS_6019.equals(info.getBriefId())){
					billStatus=AccebillConstant.AB_BILL_STATUS_190006;
				}
			}
			AbVoucherinfoVO vouInfoVO=getAbVoucherinfoVOById(info.getVid(),billStatus);
			return vouInfoVO;
		}else{
			return null;
		}
	}

    /**
     * 根据票据ID获取票据信息
     *
     * @param vid
     * @return
     */
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbVoucherinfoVOById(String vid, String billStatus) {
        AbVoucherinfo info = abVoucherinfoDao.get(vid, true);
        AbVoucherinfoVO reInfo = null;
        if (info != null && AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(info.getBillFlag()))) {
            reInfo = new AbVoucherinfoVO();
            BeanUtils.copyNotNullProperties(info, reInfo);
            if (StringUtils.isEmpty(reInfo.getBillStatus()) && StringUtils.isNotEmpty(billStatus)) {
                reInfo.setBillStatus(billStatus);
                //新票的赋值
                if ("1".equals(info.getNewStmIssuofpprInd())) {
                    if (AccebillConstant.AB_BILL_STATUS_030006.equals(billStatus)) {
                        reInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                        reInfo.setEbillBlCrclIndc(AccebillConstant.AB_BANEM_N.equals(info.getBanendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0303 : AccebillConstant.CIRCULATE_TF0301);
                        reInfo.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC03);
                    } else if (AccebillConstant.AB_BILL_STATUS_100006.equals(billStatus)) {
                        reInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                        reInfo.setEbillBlCrclIndc(AccebillConstant.AB_BANEM_N.equals(info.getBanendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0303 : AccebillConstant.CIRCULATE_TF0301);
                        reInfo.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC07);
                    } else if (AccebillConstant.AB_BILL_STATUS_180006.equals(billStatus)) {
                        reInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                        reInfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0304);
                        reInfo.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC11);
                    } else if (AccebillConstant.AB_BILL_STATUS_190006.equals(billStatus)) {
                        boolean b = DateUtils.getDateYMD(new Date()).getTime() > DateUtils.getDateYMD(info.getEndDate()).getTime();
                        reInfo.setBillStatus(b ? AccebillConstant.NEWBILL_STATUS_CS04 : AccebillConstant.NEWBILL_STATUS_CS03);
                        reInfo.setEbillBlCrclIndc(b ? AccebillConstant.CIRCULATE_TF0401 : AccebillConstant.CIRCULATE_TF0301);
                        reInfo.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC12);
                    }
                }
            }
        }
        return reInfo;
    }

	/**
	 * 根据签收表ID获取票据信息
	 *
	 * @param recId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbVoucherinfoVoBySignId(String recId) {
		AbBillReceivebiz info = abBillReceivebizService.get(recId, true);
		abBillReceivebizService.setBillReceivebizBankName(info, null);
		AbVoucherinfo abVoucherinfo=abVoucherinfoDao.getAbVoucherinfoByOrigNoAndRangSn(info.getOrigNo(),info.getSbbllRngStrtSn(),info.getSbbllRngEndSn());
		AbVoucherinfoVO reInfo = null;
		if (info != null) {
			reInfo = new AbVoucherinfoVO();
			BeanUtils.copyNotNullProperties(info, reInfo);
			reInfo.setPayerCdtRatgs(info.getCdtratgs());
			reInfo.setPayerCdtRatgGgcy(info.getCdtratgagcy());
			reInfo.setPayerCdtRatgDuedt(info.getCdtratgduedt());
			reInfo.setAcontractNo(info.getTxlctrctnb());
			reInfo.setBanendrsmtmk(info.getBankendrsmtmk());
			reInfo.setPayerBankName(info.getPayerbankName());
			reInfo.setPayeeBankName(info.getPayeebankName());
			reInfo.setAcceptorBankName(info.getAcceptorbankName());
			reInfo.setEbillBlCrclIndc(abVoucherinfo == null ? null : abVoucherinfo.getEbillBlCrclIndc());
			//this.inject(reInfo);
		}
        return reInfo;
    }

	/**
	 * 初始化批量电票查看页面信息
	 * @param voucherinfoVO
	 * @param model
	 */
	@Transactional(readOnly = true)
	public void initBatchBillEcdsInfo(AbVoucherinfoVO voucherinfoVO, Model model){
		List<AbVoucherinfoVO> billList=new ArrayList<>();
		StringBuilder accIdSB=new StringBuilder();
		boolean isShowAdditional=false;
		List<BillAddCreditInfo> billAddCreditInfo=new ArrayList<>();
		if(StringUtils.isEmpty(voucherinfoVO.getAccIds())&&StringUtils.isNotEmpty(voucherinfoVO.getId())){
			voucherinfoVO.setAccIds(voucherinfoVO.getId());
			voucherinfoVO.setRecId(voucherinfoVO.getId());
			voucherinfoVO.setId(null);
		}
		if(StringUtils.isNotEmpty(voucherinfoVO.getAccIds())){
			String[] accIds=StringUtils.split(voucherinfoVO.getAccIds(), ",");
			Map<String,InputBankInfoDTO> bankMap = new HashMap<String, InputBankInfoDTO>();
			Map<String,String> origNoMap = new HashMap<String, String>();
			for(String accId:accIds){
				if(StringUtils.isNotEmpty(accId)){
					voucherinfoVO.setAccId(accId);
					voucherinfoVO.setVvid(voucherinfoVO.getAccId());
					AbVoucherinfoVO reInfo = getAbVoucherinfoVO(voucherinfoVO);
					if(reInfo!=null) {
						if("1".equals(reInfo.getNewStmIssuofpprInd())){
							isShowAdditional = true;
						}
						reInfo.setBillAddCreditInfoList(getBillAddCreditInfo(reInfo.getId()));
						AbAccrecords accrecords = abAccrecordsService.get(accId, true);
						if (accrecords != null) {
							AbVoucherinfoVice vice = abVoucherinfoViceService.get(accrecords.getVvid(), true);
							reInfo.setBillStatus(StringUtils.isEmpty(vice.getBillStatus()) ? reInfo.getBillStatus() : vice.getBillStatus());
							reInfo.setEbillBlCrclIndc(StringUtils.isEmpty(vice.getEbillBlCrclIndc()) ? reInfo.getEbillBlCrclIndc() : vice.getEbillBlCrclIndc());
						}
					}
					//是否为空且排除重复
					if(reInfo!=null && !origNoMap.containsKey(reInfo.getOrigNo()+reInfo.getSbbllRngStrtSn()+reInfo.getSbbllRngEndSn())){
						origNoMap.put(reInfo.getOrigNo()+reInfo.getSbbllRngStrtSn()+reInfo.getSbbllRngEndSn(),reInfo.getOrigNo()+reInfo.getSbbllRngStrtSn()+reInfo.getSbbllRngEndSn());
						setBillAccBankName(reInfo, bankMap);
						reInfo.setAccId(accId);
						billList.add(reInfo);
						if(accIdSB.length()>0){
							accIdSB.append(",");
						}
						accIdSB.append(accId);
					}
				}
			}
		}else if(StringUtils.isNotEmpty(voucherinfoVO.getVid())||StringUtils.isNotEmpty(voucherinfoVO.getVvid())){
			if(StringUtils.isEmpty(voucherinfoVO.getVid())
					&&StringUtils.isNotEmpty(voucherinfoVO.getVvid())){
				AbVoucherinfoVice vice = abVoucherinfoViceService.get(voucherinfoVO.getVvid(),true);
				if(vice!=null && StringUtils.isNotEmpty(vice.getId())){
					voucherinfoVO.setVid(vice.getVid());
				}else{
					voucherinfoVO.setVid(voucherinfoVO.getVvid());
				}
			}
			Map<String,InputBankInfoDTO> bankMap = new HashMap<String, InputBankInfoDTO>();
			Map<String,String> origNoMap = new HashMap<String, String>();
			voucherinfoVO.setId(voucherinfoVO.getVid());
			AbVoucherinfoVO reInfo = getAbVoucherinfoVO(voucherinfoVO);
			if(reInfo!=null) {
				if("1".equals(reInfo.getNewStmIssuofpprInd())){
					isShowAdditional = true;
				}
				reInfo.setBillAddCreditInfoList(getBillAddCreditInfo(reInfo.getId()));
				if("1".equals(reInfo.getNewStmIssuofpprInd())){
					if(StrUtil.isEmpty(reInfo.getBillStatus())){
						reInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
					}
					if(StrUtil.isEmpty(reInfo.getEbillBlCrclIndc())){
						reInfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0301);
					}
				}
				//是否为空且排除重复
				if (reInfo != null && !origNoMap.containsKey(reInfo.getOrigNo() + reInfo.getSbbllRngStrtSn() + reInfo.getSbbllRngEndSn())) {
					origNoMap.put(reInfo.getOrigNo() + reInfo.getSbbllRngStrtSn() + reInfo.getSbbllRngEndSn(), reInfo.getOrigNo() + reInfo.getSbbllRngStrtSn() + reInfo.getSbbllRngEndSn());
					setBillAccBankName(reInfo, bankMap);
					billList.add(reInfo);
				}
			}
			voucherinfoVO.setIsShowSyncro("0");
		}
		model.addAttribute("today", new Date());
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
				"AB_BANEM");
		for (AbVoucherinfoVO vo : billList) {
			injectValues(vo,listDictMap);
		}
		model.addAttribute("list", billList);
		model.addAttribute("accIds", accIdSB.toString());
		model.addAttribute("isShowSyncro", voucherinfoVO.getIsShowSyncro());
		model.addAttribute("isShowAdditional", isShowAdditional);
	}


	@Transactional(readOnly = true)
	public void initBatchBillEcdsInfo(AbVoucherinfoVO voucherinfoVO, AbVoucherinfoPageVO pageVO){
		List<AbVoucherinfoVO> billList=new ArrayList<>();
		StringBuilder accIdSB=new StringBuilder();
		boolean isShowAdditional=false;
		List<BillAddCreditInfo> billAddCreditInfo=new ArrayList<>();
		if(StringUtils.isEmpty(voucherinfoVO.getAccIds())&&StringUtils.isNotEmpty(voucherinfoVO.getId())){
			voucherinfoVO.setAccIds(voucherinfoVO.getId());
			voucherinfoVO.setRecId(voucherinfoVO.getId());
			voucherinfoVO.setId(null);
		}
		if(StringUtils.isNotEmpty(voucherinfoVO.getAccIds())){
			String[] accIds=StringUtils.split(voucherinfoVO.getAccIds(), ",");
			Map<String,InputBankInfoDTO> bankMap = new HashMap<String, InputBankInfoDTO>();
			Map<String,String> origNoMap = new HashMap<String, String>();
			for(String accId:accIds){
				if(StringUtils.isNotEmpty(accId)){
					voucherinfoVO.setAccId(accId);
					voucherinfoVO.setVvid(voucherinfoVO.getAccId());
					AbVoucherinfoVO reInfo = getAbVoucherinfoVO(voucherinfoVO);
					if(reInfo!=null) {
						if("1".equals(reInfo.getNewStmIssuofpprInd())){
							isShowAdditional = true;
						}
						reInfo.setBillAddCreditInfoList(getBillAddCreditInfo(reInfo.getId()));
						AbAccrecords accrecords = abAccrecordsService.get(accId, true);
						if (accrecords != null) {
							AbVoucherinfoVice vice = abVoucherinfoViceService.get(accrecords.getVvid(), true);
							reInfo.setBillStatus(StringUtils.isEmpty(vice.getBillStatus()) ? reInfo.getBillStatus() : vice.getBillStatus());
							reInfo.setEbillBlCrclIndc(StringUtils.isEmpty(vice.getEbillBlCrclIndc()) ? reInfo.getEbillBlCrclIndc() : vice.getEbillBlCrclIndc());
						}
					}
					//是否为空且排除重复
					if(reInfo!=null && !origNoMap.containsKey(reInfo.getOrigNo()+reInfo.getSbbllRngStrtSn()+reInfo.getSbbllRngEndSn())){
						origNoMap.put(reInfo.getOrigNo()+reInfo.getSbbllRngStrtSn()+reInfo.getSbbllRngEndSn(),reInfo.getOrigNo()+reInfo.getSbbllRngStrtSn()+reInfo.getSbbllRngEndSn());
						setBillAccBankName(reInfo, bankMap);
						reInfo.setAccId(accId);
						billList.add(reInfo);
						if(accIdSB.length()>0){
							accIdSB.append(",");
						}
						accIdSB.append(accId);
					}
				}
			}
		}else if(StringUtils.isNotEmpty(voucherinfoVO.getVid())||StringUtils.isNotEmpty(voucherinfoVO.getVvid())){
			if(StringUtils.isEmpty(voucherinfoVO.getVid())
					&&StringUtils.isNotEmpty(voucherinfoVO.getVvid())){
				AbVoucherinfoVice vice = abVoucherinfoViceService.get(voucherinfoVO.getVvid(),true);
				if(vice!=null && StringUtils.isNotEmpty(vice.getId())){
					voucherinfoVO.setVid(vice.getVid());
				}else{
					voucherinfoVO.setVid(voucherinfoVO.getVvid());
				}
			}
			Map<String,InputBankInfoDTO> bankMap = new HashMap<String, InputBankInfoDTO>();
			Map<String,String> origNoMap = new HashMap<String, String>();
			voucherinfoVO.setId(voucherinfoVO.getVid());
			AbVoucherinfoVO reInfo = getAbVoucherinfoVO(voucherinfoVO);
			if(reInfo!=null) {
				if("1".equals(reInfo.getNewStmIssuofpprInd())){
					isShowAdditional = true;
				}
				reInfo.setBillAddCreditInfoList(getBillAddCreditInfo(reInfo.getId()));
				if("1".equals(reInfo.getNewStmIssuofpprInd())){
					if(StrUtil.isEmpty(reInfo.getBillStatus())){
						reInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
					}
					if(StrUtil.isEmpty(reInfo.getEbillBlCrclIndc())){
						reInfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0301);
					}
				}
				//是否为空且排除重复
				if (reInfo != null && !origNoMap.containsKey(reInfo.getOrigNo() + reInfo.getSbbllRngStrtSn() + reInfo.getSbbllRngEndSn())) {
					origNoMap.put(reInfo.getOrigNo() + reInfo.getSbbllRngStrtSn() + reInfo.getSbbllRngEndSn(), reInfo.getOrigNo() + reInfo.getSbbllRngStrtSn() + reInfo.getSbbllRngEndSn());
					setBillAccBankName(reInfo, bankMap);
					billList.add(reInfo);
				}
			}
			voucherinfoVO.setIsShowSyncro("0");
		}
		pageVO.setToday(new Date());
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
				"AB_BANEM");
		for (AbVoucherinfoVO vo : billList) {
			injectValues(vo,listDictMap);
		}
		pageVO.setList(billList);
		pageVO.setAccIds(accIdSB.toString());
		pageVO.setIsShowSyncro(voucherinfoVO.getIsShowSyncro());
		pageVO.setIsShowAdditional(String.valueOf(isShowAdditional));
	}

	private void injectValues(AbVoucherinfoVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
		// 状态
		Optional.ofNullable(vo.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
					.ifPresent(dict -> vo.setBanendrsmtmkName(dict.getName()));
		});
	}

	/**
	 * 获取增信信息
	 * @param vid
	 * @return
	 */
	public  List<BillAddCreditInfo> getBillAddCreditInfo(String vid) {
		List<BillAddCreditInfo> billAddCreditInfo = new ArrayList<>();
		List<BillAddCreditInfo> billAddCreditInfoList = billCreditInfoDao.getBillAddCreditInfo(vid);
		BillAddCreditInfo addCreditInfo = null;
		if (CollectionUtil.isEmpty(billAddCreditInfoList)) {
			addCreditInfo = new BillAddCreditInfo();
			addCreditInfo.setTypeName("出票保证人");
			billAddCreditInfo.add(addCreditInfo);

			addCreditInfo = new BillAddCreditInfo();
			addCreditInfo.setTypeName("承兑保证人");
			billAddCreditInfo.add(addCreditInfo);

			addCreditInfo = new BillAddCreditInfo();
			addCreditInfo.setTypeName("背书保证人");
			billAddCreditInfo.add(addCreditInfo);
		} else {
			boolean billecredit = false;
			boolean acceptecredit = false;
			boolean endorsecredit = false;
			for (BillAddCreditInfo info : billAddCreditInfoList) {
				if ("出票保证人".equals(info.getTypeName())) {
					billecredit = true;
				} else if ("承兑保证人".equals(info.getTypeName())) {
					acceptecredit = true;
				} else if ("背书保证人".equals(info.getTypeName())) {
					endorsecredit = true;
				}
				billAddCreditInfo.add(info);
			}
			if (!billecredit) {
				addCreditInfo = new BillAddCreditInfo();
				addCreditInfo.setTypeName("出票保证人");
				billAddCreditInfo.add(0, addCreditInfo);
			}
			if (!acceptecredit) {
				addCreditInfo = new BillAddCreditInfo();
				addCreditInfo.setTypeName("承兑保证人");
				int index = 1;
				if (billecredit) {
					for (int i = 0; i < billAddCreditInfo.size(); i++) {
						BillAddCreditInfo info = billAddCreditInfo.get(i);
						if (!"出票保证人".equals(info.getTypeName())) {
							index = i;
						}
					}
				}
				billAddCreditInfo.add(index, addCreditInfo);
			}
			if (!endorsecredit) {
				addCreditInfo = new BillAddCreditInfo();
				addCreditInfo.setTypeName("背书保证人");
				billAddCreditInfo.add(addCreditInfo);
			}
		}
		return billAddCreditInfo;
	}

    /**
     * 初始化电票查看页面信息
     *
     * @param voucherinfoVO
     * @param model
     */
    @Transactional(readOnly = true)
    public void initBillEcdsInfo(AbVoucherinfoVO voucherinfoVO, Model model) {
        AbVoucherinfoVO reInfo = getAbVoucherinfoVO(voucherinfoVO);
        if (reInfo == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
        }
        setBillAccBankName(reInfo, null);
        model.addAttribute("today", new Date());
        model.addAttribute("info", reInfo);
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
				"AB_BANEM");
		injectValues(reInfo,listDictMap);
        String reqAccId = "";
        String isSendSign = AccebillConstant.AB_BILL_SEND_SIGN_N;
        if (StringUtils.isNotEmpty(voucherinfoVO.getAccId())) {
            AbAccrecords accInfo = abAccrecordsService.get(voucherinfoVO.getAccId(), true);
            if (accInfo != null) {

				AbVoucherinfoVice vice = abVoucherinfoViceService.get(accInfo.getVvid(),true);
				reInfo.setBillStatus(StringUtils.isEmpty(vice.getBillStatus()) ? reInfo.getBillStatus() : vice.getBillStatus());
				reInfo.setEbillBlCrclIndc(StringUtils.isEmpty(vice.getEbillBlCrclIndc()) ? reInfo.getEbillBlCrclIndc() : vice.getEbillBlCrclIndc());
                isSendSign = accInfo.getSendSign();
                //是否发送标记为线下还需判断对应账号是否线上账号，如果线上账号支撑同步正面和背面
                if (AccebillConstant.AB_BILL_SEND_SIGN_N.equals(isSendSign)
                        && StringUtils.isNotEmpty(accInfo.getReqAcctId())) {
                    BtBankAccDTO bankAccDTO = abAccrecordsService.getBtBankAccInfo(accInfo.getReqAcctId());
                    if (bankAccDTO != null && "1".equals(bankAccDTO.getCirculationMode())) {
                        isSendSign = AccebillConstant.AB_BILL_SEND_SIGN_Y;
                    }
                }
                model.addAttribute("acId", accInfo.getId());
                reqAccId = accInfo.getReqAcctId();
            }
        }
        //票据签收查看则为线上电票
        if (AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(voucherinfoVO.getIsSign())) {
            isSendSign = AccebillConstant.AB_BILL_SEND_SIGN_Y;
            if (StringUtils.isEmpty(reqAccId)) {
                AbBillReceivebiz info = abBillReceivebizService.get(voucherinfoVO.getRecId(), true);
                if (info != null) {
                    model.addAttribute("recId", voucherinfoVO.getRecId());
                    reqAccId = info.getRecAcctId();
                }
            }
        }
        model.addAttribute("reqAccId", reqAccId);
        model.addAttribute("isSendSign", isSendSign);//是否在线数据
//		model.addAttribute("isSendSign", abDraftExcService.getIsSendSign(voucherinfoVO.getId()));//是否在线数据
    }

	/**
	 * 获取票据主信息和背面信息
	 * @return
	 */
	public AbVoucherinfoVO getAbVoucherinfoVO(AbVoucherinfoVO voucherinfoVO){
		AbVoucherinfoVO reInfo = null;
        if (reInfo == null && StringUtils.isNotEmpty(voucherinfoVO.getId()) && !"undefined".equals(voucherinfoVO.getId())) {
			reInfo = getAbVoucherinfoVOById(voucherinfoVO.getId(),null);
		}
        if (reInfo == null && StringUtils.isNotEmpty(voucherinfoVO.getAccId()) && !"undefined".equals(voucherinfoVO.getAccId())) {
			reInfo = getAbVoucherinfoVOByAccId(voucherinfoVO.getAccId());
		}
		if (reInfo == null && StringUtils.isNotEmpty(voucherinfoVO.getRecId()) && !"undefined".equals(voucherinfoVO.getRecId())) {
            reInfo = getAbVoucherinfoVoBySignId(voucherinfoVO.getRecId());
		}
		if (reInfo == null && StringUtils.isNotEmpty(voucherinfoVO.getVvid()) && !"undefined".equals(voucherinfoVO.getVvid())) {
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(voucherinfoVO.getVvid(),true);
            if(abVoucherinfoVice!=null && StrUtil.isNotEmpty(abVoucherinfoVice.getId())) {
                AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abVoucherinfoVice.getVid());
                reInfo = getAbVoucherinfoVOById(abVoucherinfo.getId(), null);
            }
		}
		if (reInfo == null && StringUtils.isNotEmpty(voucherinfoVO.getAccId()) && !"undefined".equals(voucherinfoVO.getAccId())) {
            AbDiscount abDiscount = abDiscountService.get(voucherinfoVO.getAccId(),true);
            if(abDiscount!=null) {
                reInfo = getAbVoucherinfoVOByAccId(abDiscount.getRecordId());
            }
		}
        if (reInfo == null && StringUtils.isNotEmpty(voucherinfoVO.getOrigNo())) {
            reInfo = getAbVoucherinfoVOByOrigNo(voucherinfoVO.getOrigNo(),voucherinfoVO.getSbbllRngStrtSn(),voucherinfoVO.getSbbllRngEndSn());
        }
        if (reInfo != null) {
			reInfo.setBackbillinfoList(abBackbillinfoService.listAbBackbillinfoVOByOrigNo(reInfo, reInfo.getOrigNo(),reInfo.getSbbllRngStrtSn(),reInfo.getSbbllRngEndSn()));
			if(reInfo.getBillMoney()!=null){
				reInfo.setBillMoneyCNY(StringUtils.chineseMoney(reInfo.getBillMoney().doubleValue()));
			}
			if (StringUtils.isNotEmpty(reInfo.getOrigNo()) && reInfo.getOrigNo().length() == 30) {
				String origNo = reInfo.getOrigNo();
				origNo = origNo.charAt(0) + " " + origNo.substring(1, 13) + " " + origNo.substring(13, 21) + " "
						+ origNo.substring(21, 29) + " " + origNo.substring(29);
				reInfo.setOrigNo(origNo);
			}
		}
		return reInfo;
	}

    /**
     * 设置银行名称，银行行号设置行名信息
     *
     * @param info    票面主信息
     * @param bankMap 银行信息缓存map
     */
    @Transactional(readOnly = true)
    public void setBillAccBankName(AbVoucherinfoVO info, Map<String, InputBankInfoDTO> bankMap) {
        if (bankMap == null) {
            bankMap = new HashMap<String, InputBankInfoDTO>();
        }
        if (StringUtils.isNotEmpty(info.getPayerbankno())
                && StringUtils.isEmpty(info.getPayerBankName())) {
            if (bankMap.containsKey(info.getPayerbankno())) {
                info.setPayerBankName(bankMap.get(info.getPayerbankno()).getBankName());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(info.getPayerbankno());
                if (bankInfo != null) {
                    bankMap.put(info.getPayerbankno(), bankInfo);
                    info.setPayerBankName(bankMap.get(info.getPayerbankno()).getBankName());
                }
            }
        }
        if (StringUtils.isNotEmpty(info.getPayeebankno())
                && StringUtils.isEmpty(info.getPayeeBankName())) {
            if (bankMap.containsKey(info.getPayeebankno())) {
                info.setPayeeBankName(bankMap.get(info.getPayeebankno()).getBankName());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(info.getPayeebankno());
                if (bankInfo != null) {
                    bankMap.put(info.getPayeebankno(), bankInfo);
                    info.setPayeeBankName(bankMap.get(info.getPayeebankno()).getBankName());
                }
            }
        }
        if (StringUtils.isNotEmpty(info.getAcceptorbankno())
                && StringUtils.isEmpty(info.getAcceptorBankName())) {
            if (bankMap.containsKey(info.getAcceptorbankno())) {
                info.setAcceptorBankName(bankMap.get(info.getAcceptorbankno()).getBankName());
            } else {
                InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(info.getAcceptorbankno());
                if (bankInfo != null) {
                    bankMap.put(info.getAcceptorbankno(), bankInfo);
                    info.setAcceptorBankName(bankMap.get(info.getAcceptorbankno()).getBankName());
                }
            }
        }
    }

    /**
     * 新增票据时，票据号重复验证
     *
     * @param corpId 持票人单位
     * @param origNo 票据号码
     * @return 存在数据返回true否则false
     */
    @Transactional(readOnly = true)
    public boolean checkOrigNo(String corpId, String origNo, String viceType, String sbbllRngStrtSn, String sbbllRngEndSn) {
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("origNo", origNo);
        params.put("viceType", viceType);
        if (sbbllRngStrtSn != null && sbbllRngEndSn != null && (sbbllRngStrtSn + sbbllRngEndSn).length() == 24) {
            params.put("sbbllRngStrtSn", sbbllRngStrtSn);
            params.put("sbbllRngEndSn", sbbllRngEndSn);
        }
        return abVoucherinfoDao.checkOrigNo(params, null);
    }
//
//	/**
//	 * 修改票据时，票据号重复验证
//	 *
//	 * @param corpId
//	 *            持票人单位
//	 * @param origNo
//	 *            票据号码
//	 * @param vid
//	 *            票据主表id
//	 * @return 存在数据返回true否则false
//	 */
//	@Transactional(readOnly = true)
//	public boolean checkOrigNo(String corpId, String origNo, String vid) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("corpId", corpId);
//		params.put("origNo", origNo);
//		return abVoucherinfoDao.checkOrigNo(params, vid);
//	}

    @Transactional(readOnly = true)
    public boolean checkOrigNo(String corpId, String origNo, String vid, String viceType) {
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("origNo", origNo);
		if(viceType != null){
			params.put("viceType", viceType);
		}
		return abVoucherinfoDao.checkOrigNo(params, vid);
	}

	@Transactional(readOnly = true)
	public boolean checkOrigNo(String corpId, String origNo,String vid,String viceType,String sbbllRngStrtSn, String sbbllRngEndSn) {
		Map<String, Object> params = new HashMap<>();
		params.put("corpId", corpId);
		params.put("origNo", origNo);
		params.put("sbbllRngStrtSn",sbbllRngStrtSn);
		params.put("sbbllRngEndSn",sbbllRngEndSn);
        if (viceType != null) {
            params.put("viceType", viceType);
        }
        return abVoucherinfoDao.checkOrigNo(params, vid);
    }

    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbVoucherinfoIsPay(String origNo, String sbbllRngStrtSn, String sbbllRngEndSn) {
        AbVoucherinfo info = abVoucherinfoDao.getAbVoucherinfoByOrigNo(origNo, sbbllRngStrtSn, sbbllRngEndSn);
        AbVoucherinfoVO reInfo = null;
        if (info != null) {
            reInfo = new AbVoucherinfoVO();
            BeanUtils.copyProperties(info, reInfo);
        }
        return reInfo;
    }

    /**
     * 根据票据号修改信用等级信息
     *
     * @param origNo            票据号
     * @param payerCdtRatgDuedt 评级到期日
     * @param payerCdtRatgGgcy  评级机构
     * @param payerCdtRatgs     信用等级
     */
    public void updatePayerCdtByOrigNo(String origNo, Date payerCdtRatgDuedt, String payerCdtRatgGgcy,
                                       String payerCdtRatgs, String sbbllRngStrtSn, String sbbllRngEndSn) {
        abVoucherinfoDao.updatePayerCdtByOrigNo(origNo, payerCdtRatgDuedt, payerCdtRatgGgcy, payerCdtRatgs, sbbllRngStrtSn, sbbllRngEndSn);
    }

}
