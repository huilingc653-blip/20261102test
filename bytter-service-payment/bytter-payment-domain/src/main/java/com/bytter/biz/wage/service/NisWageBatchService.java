package com.bytter.biz.wage.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.service.BisBifRuleService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.biz.wage.dao.NisWageDao;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.biz.wage.vo.NisWageBatchVO;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BisBifRuleDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: NIS_WAGE
 * @date 2018-01-08 17:19:13
 */
@Service("nisWageBatchService")
@Transactional
public class NisWageBatchService extends BaseService<NisWage, String> implements IExcelVerifyHandler<NisWageBatchVO> {
    @Autowired
    private NisWageDao nisWageDao;
    @Autowired
    private NisWageService nisWageService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired(required = false)
    private NisBillheadService nisBillheadService;
    @DubboReference(check = false)
	private IInputBankInfoService btBankService;
    @Autowired
    private NisBusTypeService nisBusTypeService;
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Autowired
    private BisBifRuleService bisBifRuleService;
    @Override
    public BaseDao<NisWage, String> getEntityDao() {
        return nisWageDao;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisWageBatchVO nisWageBatchVO) {
        return null;
    }
    @Override
    public void verifyHandler(List<NisWageBatchVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //单位集合
            Map<String, CorpDTO> corpMap = new HashMap<>();
            //币种集合
            Map<String, String> curMap = new HashMap<>();
            //银行账号集合
            Map<String, BtBankAccDTO> bankAccMap = new HashMap<>();
            //明细账号集合
            Map<String, String> dtlBankAccMap = new HashMap<>();
            //接口代码规则集合
            Map<String, BisBifRuleDTO> bisBifRuleMap = new HashMap<>();
            //证件类型
            Map<String, DictDTO> dictMap = dictService.listMap("idType");
            
            Map<String, CorpDTO> corpAllMap = corpService.getCorpListByNamesAll();
            List<BtCurrencyDTO> currencyDTOList=  btCurrencyService.getBtCurrency();
            Map<String, String> curAllMap = currencyDTOList.stream().collect(Collectors.toMap(BtCurrencyDTO::getCurName, BtCurrencyDTO::getId));
            Set<String> setCurIds = new HashSet<String>();
            Set<String> setCorps = new HashSet<String>();
            List<String> bankCodes = list.stream().map(NisWageBatchVO::getBankCode).filter(bankCode -> StringUtils.isNotEmpty(bankCode)).distinct().collect(Collectors.toList());
            List<String> bankNames = list.stream().map(NisWageBatchVO::getBankName).filter(bankName -> StringUtils.isNotEmpty(bankName)).distinct().collect(Collectors.toList());
            for (NisWageBatchVO vo : list) {
            	if(!curMap.containsKey(vo.getCurName())) {
            		 curMap.put(vo.getCurName(), curAllMap.get(vo.getCurName()));
            		 setCurIds.add(curAllMap.get(vo.getCurName()));
            	}
            	if(!corpMap.containsKey(vo.getCorpName())) {
            		corpMap.put(vo.getCorpName(), corpAllMap.get(vo.getCorpName()));
            		CorpDTO corp = (CorpDTO)corpAllMap.get(vo.getCorpName());
            		if(corp!=null&&StringUtils.isNotEmpty(corp.getUserAccountCode())) {
	            		String[] userAccountCodes = corp.getUserAccountCode().split(CommonConstant.COMMA_SPLIT);
	            		if(userAccountCodes.length>0) {
                            setCorps.addAll(Arrays.asList(userAccountCodes));
	            		}else {
	            			setCorps.add(corp.getUserAccountCode());
	            		}
            		}
            	}
            	
            }
            List<String> corpIds = new ArrayList<>(setCorps);
            List<String> curIds = new ArrayList<>(setCurIds);
            List<String> accs = list.stream().map(NisWageBatchVO::getPayerAcc).distinct().collect(Collectors.toList());
            
            List<NisWage> listNisWage = nisWageService.getWageTileList();//数据多则需要优化
            Map<String, NisWage> wageMap = new HashMap<String, NisWage>();
            for(NisWage wage:listNisWage) {
            	if(!wageMap.containsKey(wage.getCorpId()+wage.getWageTitle())) {
            		wageMap.put(wage.getCorpId()+wage.getWageTitle(), wage);
            	}
            }
            List<BtBankAccDTO> listBankAccs = new ArrayList<BtBankAccDTO>(); 
            for(String curId:curIds) {
            	List<BtBankAccDTO> bankAccs = nisBillheadService.getBankAccDTOList(corpIds, accs, curId);
                if(EmptyUtils.isNotEmpty(bankAccs)){
                    listBankAccs.addAll(bankAccs);
                }
            }
            for(BtBankAccDTO dto:listBankAccs) {
            	if(!bankAccMap.containsKey(dto.getBankAcc())) {
            		bankAccMap.put(dto.getBankAcc(), dto);
            	}
            }
            //
            List<BisBifRuleDTO> listBisBifRuleDTO = bisBifRuleService.listAllUnion();
            bisBifRuleMap = listBisBifRuleDTO.stream().collect(Collectors.toMap(BisBifRuleDTO::getBifCode, Function.identity(),(key1,key2) -> key2));
            //
            List<NisBusType> listNisBusType =  nisBusTypeService.getValidBusType();
            Map<String,NisBusType> mapNisBusType = listNisBusType.stream().distinct().collect(Collectors.toMap(NisBusType::getBusType, Function.identity(),(key1,key2) -> key2));
            //
            List<NisCorpBusVO> listCorpBusVO = nisCorpBusService.getByBusTypeForAll();
            List<NisCorpBus> listCorpBus = BeanUtils.copyListProperties(listCorpBusVO, NisCorpBus.class);
            Map<String,List<NisCorpBus>> mapNisCorpBus = new HashMap<String,List<NisCorpBus>>();
            for(NisCorpBus nisCorpBus:listCorpBus){
            	if(!mapNisCorpBus.containsKey(nisCorpBus.getBusId())) {
            		List<NisCorpBus> listBus = new ArrayList<NisCorpBus>();
            		listBus.add(nisCorpBus);
            		mapNisCorpBus.put(nisCorpBus.getBusId(), listBus);
            	}else {
            		List<NisCorpBus> listBus = mapNisCorpBus.get(nisCorpBus.getBusId());
            		listBus.add(nisCorpBus);
            	}
            }
            		//listCorpBus.stream().distinct().collect(Collectors.toMap(NisCorpBus::getBusId, Function.identity(),(key1,key2) -> key2));
            List<InputBankInfoDTO> bankCodeList = null;
            Map<String,InputBankInfoDTO> mapInputBankInfoDTO = null;
            Map<String,InputBankInfoDTO> mapInputBankInfoDTOByBankName = null;
            if(CollectionUtils.isNotEmpty(bankCodes)) {//根据bankCodes查询
            	bankCodeList = btBankService.getByBankCodesList(bankCodes);
            }
            if(CollectionUtils.isNotEmpty(bankNames)){
            	//根据bankNames查询
                List<InputBankInfoDTO> tempList = btBankService.getByBankNamesList(bankNames);
                if(EmptyUtils.isNotEmpty(tempList)){
                    if(EmptyUtils.isEmpty(bankCodeList)){
                        bankCodeList = tempList;
                    }else{
                        bankCodeList.addAll(tempList);
                    }
                }
            }
            mapInputBankInfoDTO = bankCodeList.stream().distinct().collect(Collectors.toMap(InputBankInfoDTO::getBankCode, Function.identity(),(key1,key2) -> key2));
            mapInputBankInfoDTOByBankName = bankCodeList.stream().distinct().collect(Collectors.toMap(InputBankInfoDTO::getBankName, Function.identity(),(key1,key2) -> key2));
            
            for (NisWageBatchVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp=null;
                if(StringUtils.isEmpty(vo.getPayerAcc())){
                    msgList.add("nis.pay.message.payeracc.null");
                }
                if(StringUtils.isEmpty(vo.getCorpName())){
                    msgList.add("cms.rat.registercorpname.null");
                } else {
                    corp = corpMap.get(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                        corp = new CorpDTO();
                        msgList.add("nis.netbank.message.corp.null");
                    } else {
                        vo.setCorpId(corp.getId());
                        //判断是否是重复的工资册标题
                        NisWage nisgege = wageMap.get(vo.getCorpId()+vo.getWageTitle());
                        if (ObjectUtils.isNotEmpty(nisgege)) {//TODO1.5
                            msgList.add("nis.wage.message.wagetitle.unique");
                        }
                    }
                }

                BtBankAccDTO bankAcc = null;
                String bankCode = "";
                String city = "";
                //校验币别
                if(StringUtils.isEmpty(vo.getCurName())){
                    msgList.add("cms.precative.curid.not.null");
                }else {
                    String curId = curMap.get(vo.getCurName());
                    if (EmptyUtils.isBlank(curId)) {
                        msgList.add("nis.netbank.message.cur.null");
                    } else {
                        if (StringUtils.isNotEmpty(vo.getPayerAcc())) {
                            //获取付款账号
                            bankAcc = bankAccMap.get(vo.getPayerAcc());
                        }
                        if (EmptyUtils.isEmpty(bankAcc)) {
                            msgList.add("nis.netbank.message.btbankacc.null");
                        } else {
                            vo.setCurId(curId);
                            vo.setPayerAccName(bankAcc.getAccName());
                            vo.setBifCode(bankAcc.getBifCode());
                            bankCode = bankAcc.getBankCode();
                            city = bankAcc.getCity();
                        }
                    }
                }

                //获取接口业务规则
                BisBifRuleDTO bisBifRule = null;
                if (StringUtils.isNotBlank(vo.getBifCode())) {
                    bisBifRule = bisBifRuleMap.get(vo.getBifCode());
                }
                //工资发放日期
                if (vo.getWageDay() == null) {
                    msgList.add("nis.netbank.message.wageday.null");
                } else {
                    if (vo.getWageDay().compareTo(DateUtils.getDateYMD()) < 0) {
                        msgList.add("nis.netbank.message.wageday.mismatch");
                    }
                }
                //期望支付日期
                if (vo.getWishPayday() == null) {
                    vo.setWishPayday(DateUtils.getDateYMD());
                } else {
                    if (vo.getWageDay()!=null&&(vo.getWishPayday().compareTo(DateUtils.getDateYMD()) < 0 || vo.getWishPayday().compareTo(vo.getWageDay()) < 0)) {
                        msgList.add("nis.wage.message.wishpayday.mismatch");
                    }
                }
                
                //业务种类检验
                boolean flag = false;
                if (corp!=null&&EmptyUtils.isNotEmpty(vo.getBusCode()) && EmptyUtils.isNotEmpty(corp.getId())) {
                    NisBusType nisBusType = mapNisBusType.get(vo.getBusCode());//TODO5
                    if (nisBusType != null) {
                        //校验业务单位是否分配该业务种类
                        List<NisCorpBus> listBusType = mapNisCorpBus.get(nisBusType.getId());//TODO6
                        if (ObjectUtils.isEmpty(listBusType)) {
                            msgList.add("nis.netbank.message.bustype.mismatch");
                        } else {
                            for (NisCorpBus cb : listBusType) {
                                if (corp.getId().equals(cb.getCorpId())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (flag) {
                        //单位存在该业务类别
                        vo.setBusType(nisBusType.getId());
                    } else {
                        msgList.add("nis.netbank.message.bustype.mismatch");
                    }
                }
                //状态默认0-暂存
                vo.setStatus(NetbankConstant.STATUE_TEMP);
                //校验证件类型
                if(StringUtils.isNotBlank(vo.getIdType())){
                    if(!dictMap.containsKey(vo.getIdType())){
                        msgList.add("nis.netbank.message.idType.format");
                    }
                }
                //明细校验
                //金额不能为空且必须大于0
                if (vo.getDtlWages() == null) {
                    msgList.add("nis.wage.message.wages.null");
                } else {
                    if (vo.getDtlWages().compareTo(BigDecimal.ZERO) <= 0) {
                        msgList.add("nis.wage.message.wages.mismatch");
                    } else {
                        vo.setWages(new BigDecimal(NumberUtil.formatNumeric(vo.getDtlWages(), "#0.00")));
                        try {
                            vo.setWagesVerify(Base64Util.encodeToString((vo.getDtlWages().toString()).getBytes("UTF-8")));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //校验跨行标志
                if (StringUtils.isNotBlank(vo.getDifBank())) {
                    if (!"同行".equals(vo.getDifBank())
                            && !"他行".equals(vo.getDifBank())) {
                        msgList.add("nis.netbank.message.difbank.format");
                    }
                }
                if (StringUtils.isNotBlank(vo.getAreaSign())) {
                    //校验地域标志
                    if (!"同城".equals(vo.getAreaSign())
                            && !"异地".equals(vo.getAreaSign())) {
                        msgList.add("nis.netbank.message.areasign.format");
                    }
                }
                //判断开户行在系统中是否存在
                if (!EmptyUtils.isBlank(vo.getBankCode())) {
                    InputBankInfoDTO btBankDTO = mapInputBankInfoDTO.get(vo.getBankCode());//TODO7
                    if (EmptyUtils.isEmpty(btBankDTO)) {
                        msgList.add("nis.netbank.message.bank.null");
                    } else {
                        //开户行名称不为空且与开户行代码对应的名称不一致
                        if (!EmptyUtils.isBlank(StringUtils.trim(vo.getBankName())) && !StringUtils.trim(vo.getBankName()).equals(btBankDTO.getBankName())) {
                            msgList.add("nis.netbank.message.bank.mismatch");
                        } else {
                            vo.setBankCode(btBankDTO.getBankCode());
                            vo.setBankName(btBankDTO.getBankName());
                            vo.setProv(btBankDTO.getProv());
                            vo.setCity(btBankDTO.getCity());
                        }
                    }
                } else {
                    if (EmptyUtils.isBlank(vo.getBankName())) {
                        //跨行 or 同行且同行代发必填开户行行号
                        if (!"同行".equals(vo.getDifBank())
                                || ("同行".equals(vo.getDifBank())
                                && bisBifRule != null && CommonConstant.YES.equals(bisBifRule.getIsMustBankCode()))) {
                            msgList.add("nis.netbank.message.bankcode.null");
                        }
                    } else {
                        InputBankInfoDTO btBankDTO = mapInputBankInfoDTOByBankName.get(vo.getBankName());//btBankService.getByBankName(vo.getBankName());
                        if (EmptyUtils.isEmpty(btBankDTO)) {
                            msgList.add("开户行名称不存在！");
                        } else {
                            vo.setBankCode(btBankDTO.getBankCode());
                            vo.setBankName(btBankDTO.getBankName());
                            vo.setProv(btBankDTO.getProv());
                            vo.setCity(btBankDTO.getCity());
                        }
                    }
                }

                //校验“跨行标志”是否如实
                String difBank = vo.getDifBank();
                String dtlBankCode = vo.getBankCode();
                if (StringUtils.isNotBlank(difBank)) {
                    if("同行".equals(difBank)){
                        if(EmptyUtils.isNotEmpty(bankCode) && EmptyUtils.isNotEmpty(dtlBankCode)) {
                            if (!bankCode.substring(0, 3).equals(dtlBankCode.substring(0, 3))) {
                                msgList.add("nis.netbank.message.difBank.error");
                            }
                        }
                    }else if("他行".equals(difBank)){
                        if(EmptyUtils.isNotEmpty(bankCode) && EmptyUtils.isNotEmpty(dtlBankCode)) {
                            if (bankCode.substring(0, 3).equals(dtlBankCode.substring(0, 3))) {
                                msgList.add("nis.netbank.message.difBank.error");
                            }
                        }
                    }
                } else {
                    // 跨行标识为空时，系统自动判断
                    if(EmptyUtils.isNotEmpty(bankCode) && EmptyUtils.isNotEmpty(dtlBankCode)) {
                        if (bankCode.substring(0, 3).equals(dtlBankCode.substring(0, 3))) {
                            // 同行
                            vo.setDifBank("同行");
                        } else {
                            // 他行
                            vo.setDifBank("他行");
                        }
                    }
                }

                //校验“地域标志”是否如实
                String areaSign = vo.getAreaSign();
                if (StringUtils.isNotBlank(areaSign)) {
                    if("同城".equals(areaSign)){
                        if(EmptyUtils.isNotEmpty(vo.getCity()) && EmptyUtils.isNotEmpty(city)) {
                            if (!vo.getCity().equals(city)) {
                                msgList.add("地域标志有误");
                            }
                        }
                    }else if("异地".equals(areaSign)){
                        if(EmptyUtils.isNotEmpty(vo.getCity()) && EmptyUtils.isNotEmpty(city)) {
                            if (vo.getCity().equals(city)) {
                                msgList.add("地域标志有误");
                            }
                        }
                    }
                } else {
                    // 地域标志为空时，系统自动判断
                    if (EmptyUtils.isNotEmpty(vo.getCity()) && EmptyUtils.isNotEmpty(city)) {
                        if (vo.getCity().equals(city)) {
                            vo.setAreaSign("同城");
                        } else {
                            vo.setAreaSign("异地");
                        }
                    }
                }

                //工资标题+收款账号+收款金额+用途 相同则当成重复收款
                String dtlBankAccInfo = vo.getWageTitle()+vo.getBankAcc() + vo.getWages() + vo.getPurpose();
                if (dtlBankAccMap.containsKey(dtlBankAccInfo)) {
                    msgList.add("nis.wage.message.bankacc.repeat");
                } else {
                    dtlBankAccMap.put(dtlBankAccInfo, dtlBankAccInfo);
                }
                if (!EmptyUtils.isEmptyList(msgList)) {
                    vo.addErrorMsg(msgList);
                }
            }
        }
    }
    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<NisWageBatchVO> list, ICurrentUser user) {
        HashMap<String, NisWageVO> wageMap = new HashMap<>();
        for (NisWageBatchVO vo : list) {
            //工资标题+单位+付款账号+币种+工资发放日期
            String key = vo.getWageTitle()+vo.getCorpId()+vo.getPayerAcc()+vo.getCurId()+vo.getWageDay();
            if (wageMap.containsKey(key)) {
                NisWageDetailVO detailVO = new NisWageDetailVO();
                BeanUtils.copyNotNullProperties(vo, detailVO);
                detailVO.setWages(vo.getDtlWages());
                //工资总额=明细金额之和
                wageMap.get(key).setWages(wageMap.get(key).getWages().add(vo.getDtlWages()));
                wageMap.get(key).getNisWageDetailList().add(detailVO);
            } else {
                NisWageVO wageVO = new NisWageVO();
                BeanUtils.copyNotNullProperties(vo, wageVO);
                wageVO.setWages(BigDecimal.ZERO);
                List<NisWageDetailVO> dtlList =  new ArrayList<>();
                NisWageDetailVO detailVO = new NisWageDetailVO();
                BeanUtils.copyNotNullProperties(vo, detailVO);
                detailVO.setWages(vo.getDtlWages());
                dtlList.add(detailVO);
                //工资总额=明细金额之和
                wageVO.setWages(wageVO.getWages().add(vo.getDtlWages()));
                wageVO.setNisWageDetailList(dtlList);
                wageMap.put(key, wageVO);
            }
            NisWageVO nisWage = new NisWageVO();
            BeanUtils.copyNotNullProperties(vo, nisWage);
        }
        List<NisWageVO> nisWageList = new ArrayList<>();
        for(String key:wageMap.keySet()) {
            nisWageList.add(wageMap.get(key));
        }
        nisWageService.saveImport(nisWageList, user);
    }
}
