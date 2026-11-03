package com.bytter.bill.domain.bill.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillSignStrategyDao;
import com.bytter.bill.domain.bill.dao.AbBillSignStrategyDataDao;
import com.bytter.bill.domain.bill.entity.AbBillSignStrategy;
import com.bytter.bill.domain.bill.entity.AbBillSignStrategyData;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyDataVO;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: AbBillSignStrategyService
 * @Description: 电票自动签收配置Service
 * @author wangzy
 * @date 2020/7/13 16:21
 *
 */
@Service("abBillSignStrategyService")
@Transactional
public class AbBillSignStrategyService extends BaseService<AbBillSignStrategy, String> {
    private static final String CACHE_NAME ="BILL_SIGN_STRATEGY";
    @Autowired
    public AbBillSignStrategyDao abBillSignStrategyDao;
    @Autowired
    public AbCorpService abCorpService;
    @Autowired
    public AbBillSignStrategyDataService abBillSignStrategyDataService;
	@Autowired
	public AbBillSignStrategyDataDao abBillSignStrategyDataDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Override
    public BaseDao<AbBillSignStrategy, String> getEntityDao() {
        return abBillSignStrategyDao;
    }
    /**
     * 电票自动签收配置列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds("bill:accebill:sign:query",input.getCurrentUserId());
        DataTablesOutput<AbBillSignStrategyVO> data = abBillSignStrategyDao.getDataTablesUnion(input, corpAuthList);
        List<AbBillSignStrategyVO> infoList = data.getData();
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("AB_BILL_TYPE");
        Map<String, DictDTO> listSignMap = dictService.listMap("AB_BILL_SIGNERTYPE");
        Map<String, DictDTO> listBankMap = dictService.listMap("AB_BANEM");
        for (AbBillSignStrategyVO info : infoList) {
            injectValues(info, listTypeMap,listSignMap,listBankMap);
        }
        // 处理拼接签收类型
//        for(AbBillSignStrategyVO info:infoList){
//            if(StringUtils.isNotEmpty(info.getSignType())){
//                String[] signTypes = info.getSignType().split(",");
//                List<String> signTypeList = new ArrayList<>();
//                AbBillSignStrategyVO abBillSignStrategyVO = new AbBillSignStrategyVO();
//                for(String signType : signTypes){
//                    abBillSignStrategyVO.setLocale(input.getLocale());
//                    abBillSignStrategyVO.setSignType(signType);
//                    this.inject(abBillSignStrategyVO);
//                    signTypeList.add(abBillSignStrategyVO.getSignTypeName());
//                }
//                info.setSignTypeName(StringUtils.join(signTypeList.toArray(), ","));
//            }
//        }
        return data;
    }
    /**
     * 冗余票据签收数据。
     *
     * @param strategyVO  电票自动签收策略。
     * @param listTypeMap 票据类型数据。
     * @param listSignMap 签收类型数据。
     * @param listBankMap 不得转让标记数据。
     */
    private void injectValues(AbBillSignStrategyVO strategyVO, Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listSignMap,Map<String, DictDTO> listBankMap) {
        // 票据类型
        if (ObjectUtil.isAllNotEmpty(strategyVO.getBillType(), listTypeMap)) {
            Optional.ofNullable(listTypeMap.get(strategyVO.getBillType())).ifPresent(dict -> strategyVO.setBillTypeName(dict.getName()));
        }
        // 签收类型
        if (ObjectUtil.isAllNotEmpty(strategyVO.getSignType(), listSignMap)) {
            String[] signTypes = strategyVO.getSignType().split(",");
            List<String> signTypeList = new ArrayList<>();
            AbBillSignStrategyVO abBillSignStrategyVO = new AbBillSignStrategyVO();
            for(String signType : signTypes){
                abBillSignStrategyVO.setSignType(signType);
                Optional.ofNullable(listSignMap.get(abBillSignStrategyVO.getSignType())).ifPresent(dict -> abBillSignStrategyVO.setSignTypeName(dict.getName()));
                signTypeList.add(abBillSignStrategyVO.getSignTypeName());
            }
            strategyVO.setSignTypeName(StringUtils.join(signTypeList.toArray(), ","));
        }
        // 不得转让标记
        if (ObjectUtil.isAllNotEmpty(strategyVO.getBankendrsmtmk(), listBankMap)) {
            Optional.ofNullable(listBankMap.get(strategyVO.getBankendrsmtmk())).ifPresent(dict -> strategyVO.setBanendrsmtmkName(dict.getName()));
        }
    }
    private void injectValues(AbBillSignStrategyVO strategyVO,Map<String, DictDTO> listSignMap) {

        // 签收类型
        if (ObjectUtil.isAllNotEmpty(strategyVO.getSignType(), listSignMap)) {
            Optional.ofNullable(listSignMap.get(strategyVO.getSignType())).ifPresent(dict -> strategyVO.setSignTypeName(dict.getName()));
        }

    }
    /**
     * 电票自动签收策略新增或修改
     * @param operator 新增/修改
     * @param abBillSignStrategyVO 电票自动签收策略VO
     * @return 处理结果
     * @throws
     */
    //@CacheEvict(value = CACHE_NAME)
    public AjaxJson saveOrUpdate(String operator, AbBillSignStrategyVO abBillSignStrategyVO, ICurrentUser user){
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        AbBillSignStrategy abBillSignStrategy = new AbBillSignStrategy();
        BeanUtils.copyNotNullProperties(abBillSignStrategyVO, abBillSignStrategy);
        // 添加冗余数据
        Optional.ofNullable(abBillSignStrategy.getCorpId()).ifPresent(corpId -> {
            CorpDTO corpDTO = corpService.get(corpId);
            Optional.ofNullable(corpDTO).ifPresent(corp -> {
                abBillSignStrategy.setCorpCode(corp.getCode());
                abBillSignStrategy.setCorpName(corp.getName());
            });
        });
        // 新增或修改
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator) && StringUtils.isBlank(abBillSignStrategyVO.getId())){
            abBillSignStrategy.setCreateDate(new Date());
            abBillSignStrategy.setCreateId(user.getId());
            this.saveEntity(abBillSignStrategy);
        }else {
            // 修改数据
            abBillSignStrategy.setUpdateDate(new Date());
            abBillSignStrategy.setUpdateId(user.getId());
            this.updateEntity(abBillSignStrategy);
            // 对电票签收策略数据先删除再新增
            abBillSignStrategyDataDao.delAbBillSignStrategyData(abBillSignStrategyVO.getId());
        }
        //主表保存，处理从表
        List<AbBillSignStrategyData> strategyDataList = new ArrayList<AbBillSignStrategyData>();
        //出票人单位
        if(!StringUtils.isBlank(abBillSignStrategyVO.getPayerIds())){
            this.entranceList(strategyDataList, abBillSignStrategyVO.getPayerIds(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01, 
            		abBillSignStrategy.getId(),AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_01);
        }
        //出票人客商
        if(!StringUtils.isBlank(abBillSignStrategyVO.getPayerExternalIds())){
            this.entranceList(strategyDataList, abBillSignStrategyVO.getPayerExternalIds(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01, 
            		abBillSignStrategy.getId(),AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_02);
        }
        
        //承兑人
        //银票
        if(abBillSignStrategy.getBillType().equals(AccebillConstant.AB_BILL_TYPE_BANK)){
            if(!StringUtils.isBlank(abBillSignStrategyVO.getAcceptoracc())){
            	this.entranceList(strategyDataList, abBillSignStrategyVO.getAcceptoracc(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02, 
            			abBillSignStrategy.getId(),null);
            }
        //商票
        }else if(abBillSignStrategy.getBillType().equals(AccebillConstant.AB_BILL_TYPE_MERCH)){
            if(!StringUtils.isBlank(abBillSignStrategyVO.getAcceptorIds())){
            	this.entranceList(strategyDataList, abBillSignStrategyVO.getAcceptorIds(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02, 
            			abBillSignStrategy.getId(),AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_01);
            }
            if(!StringUtils.isBlank(abBillSignStrategyVO.getAcceptorExternalIds())){
            	this.entranceList(strategyDataList, abBillSignStrategyVO.getAcceptorExternalIds(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02, 
            			abBillSignStrategy.getId(),AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_02);
            }
        }
        
        //业务发起人
        //客商
        if(!StringUtils.isBlank(abBillSignStrategyVO.getReqIds())){
        	this.entranceList(strategyDataList, abBillSignStrategyVO.getReqIds(), AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_03, 
        			abBillSignStrategy.getId(),AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_01);
        }
        if(!StringUtils.isBlank(abBillSignStrategyVO.getReqExternalIds())){
        	this.entranceList(strategyDataList, abBillSignStrategyVO.getReqExternalIds(), 
        			AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_03, abBillSignStrategy.getId(),AccebillConstant.BILL_SIGN_STRATEGY_ORG_TAG_02);
        }
        
        
        if(ListUtils.isNotNullOrEmpty(strategyDataList)){
        	abBillSignStrategyDataDao.batchJdbcSave(strategyDataList);
        }
        reJson=  AjaxJson.success();
        return reJson;
    }
    
    /**
     * 删除操作
     * @param ids
     * @param user
     * @return
     */
    @CacheEvict(value = CACHE_NAME,key = "#ids")
	public AjaxJson saveOrUpdate(String ids, ICurrentUser user) {
		AjaxJson reJson = new AjaxJson();
		reJson.setSuccess(false);
		if (StringUtils.isNotEmpty(ids)) {
			String[] idArray = ids.split(",");
			if (abBillSignStrategyDao.updateValidSign(Arrays.asList(idArray)) > 0) {
				reJson.setSuccess(true);
			}
		}
		return reJson;
	}
    
    /**
     * 编辑查看详情
     * @param abBillSignStrategyVO
     * @param id
     */
    public AbBillSignStrategyVO combinationBillVo(AbBillSignStrategyVO abBillSignStrategyVO,String id){
    	abBillSignStrategyVO = abBillSignStrategyDao.getSignStrategyVO(id);
    	List<AbBillSignStrategyData> signStrategyDataList = abBillSignStrategyDataDao.getAbBillSignStrategyData(id);
    	if(ListUtils.isNotNullOrEmpty(signStrategyDataList)){
    		return this.entranceSignStrategyData(abBillSignStrategyVO, signStrategyDataList);
    	}
		return abBillSignStrategyVO;
    }
    
    //组合不同数据
    protected void entranceList(List<AbBillSignStrategyData> strategyDataList,
    		String entranceKey,String businesTypes,String strategyFkId,String opType){
        if(!StringUtils.isBlank(entranceKey)){
        	String[] payerArray = entranceKey.split(",");
        	for(String payerId:payerArray){
        		AbBillSignStrategyData strategyData = new AbBillSignStrategyData();
        		strategyData.setSsId(strategyFkId);
        		strategyData.setDataType(businesTypes);
        		strategyData.setDataVal(payerId);
        		strategyData.setOrgTag(opType);
        		strategyDataList.add(strategyData);
        	}
        }
    }
    
    //组合各种不同数据
    protected AbBillSignStrategyVO entranceSignStrategyData(AbBillSignStrategyVO abBillSignStrategyVO,List<AbBillSignStrategyData> signStrategyDataList){
    	List<String> payerIds = new ArrayList<String>()
    			,payerExternalIds = new ArrayList<String>()
    			,acceptoracc = new ArrayList<String>()
    			,acceptorIds = new ArrayList<String>()
    			,acceptorExternalIds = new ArrayList<String>()
    			,reqIds = new ArrayList<String>()
    			,reqExternalIds = new ArrayList<String>();
    	for (AbBillSignStrategyData abBillSignStrategyData : signStrategyDataList) {
			if(abBillSignStrategyData.getDataType().equals(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01)){
				if(abBillSignStrategyData.getOrgTag().equals(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01)){
					payerIds.add(abBillSignStrategyData.getDataVal());
				}else{
					payerExternalIds.add(abBillSignStrategyData.getDataVal());
				}
    		}else if(abBillSignStrategyData.getDataType().equals(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02)){
    			 if(abBillSignStrategyVO.getBillType().equals(AccebillConstant.AB_BILL_TYPE_BANK)){
 					  acceptoracc.add(abBillSignStrategyData.getDataVal());
    			 }else if(abBillSignStrategyVO.getBillType().equals(AccebillConstant.AB_BILL_TYPE_MERCH)){
    				if(abBillSignStrategyData.getOrgTag().equals(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01)){
    	    			acceptorIds.add(abBillSignStrategyData.getDataVal());
    				}else{
    					acceptorExternalIds.add(abBillSignStrategyData.getDataVal());
    				}
    			}
			}else if(abBillSignStrategyData.getDataType().equals(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_03)){
				if(abBillSignStrategyData.getOrgTag().equals(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01)){
					reqIds.add(abBillSignStrategyData.getDataVal());
				}else{
					reqExternalIds.add(abBillSignStrategyData.getDataVal());
				}
			}
		}
    	abBillSignStrategyVO.setPayerIds(StringUtils.join(payerIds.toArray(), ","));
    	abBillSignStrategyVO.setPayerExternalIds(StringUtils.join(payerExternalIds.toArray(), ","));
    	abBillSignStrategyVO.setAcceptorIds(StringUtils.join(acceptorIds.toArray(), ","));
    	abBillSignStrategyVO.setAcceptorExternalIds(StringUtils.join(acceptorExternalIds.toArray(), ","));
    	abBillSignStrategyVO.setAcceptoracc(StringUtils.join(acceptoracc.toArray(), ","));
    	abBillSignStrategyVO.setReqIds(StringUtils.join(reqIds.toArray(), ","));
    	abBillSignStrategyVO.setReqExternalIds(StringUtils.join(reqExternalIds.toArray(), ","));
    	return abBillSignStrategyVO;
    }
    /**
     * 获取自动签收策略数据
     * @return
     */
    //@Cacheable(value=CACHE_NAME)
    public Map<String,Object> getAllAbBillSignStrategy(){
        //查询签收数据参数条件
		Map<String,Object> params=new HashMap<>();
		//所有签收策略信息缓存
		Map<String,Object> signStrategyInfo=new HashMap<>();
		//进行策略数据初始化
		initAbBillSignStrategy(params,signStrategyInfo);
        initAbBillSignStrategyData(signStrategyInfo);
        signStrategyInfo.put("params",params);
        return signStrategyInfo;
    }
    /**
     * 进行策略数据初始化
     * @param params 查询签收数据参数条件 zhengg 20200721
     * @param signStrategyInfo 所有签收策略信息缓存
     */
    public void initAbBillSignStrategy(Map<String,Object> params,Map<String,Object> signStrategyInfo){
        List<AbBillSignStrategyVO> strategyVOList=abBillSignStrategyDao.getAllValidBillSignStrategy();
        if(ListUtils.isNotNullOrEmpty(strategyVOList)) {
            StringBuilder corpIdParams = new StringBuilder();//单位查询条件
            StringBuilder billTypeParams = new StringBuilder();//票据类型查询条件
            StringBuilder signTypeParams = new StringBuilder();//签收类型查询条件
            Map<String, AbBillSignStrategyVO> idToStrategyVOMap = new HashMap<>();
            for (AbBillSignStrategyVO strategyVO : strategyVOList) {
                idToStrategyVOMap.put(strategyVO.getId(), strategyVO);
                String[] signTypes = strategyVO.getSignType().split(",");
                for (String signType : signTypes) {
                    String key = strategyVO.getCorpId() + "_" + strategyVO.getBillType() + "_" + signType;
                    if (signStrategyInfo.containsKey(key)) {
                        ((List<AbBillSignStrategyVO>) signStrategyInfo.get(key)).add(strategyVO);
                    } else {
                        List<AbBillSignStrategyVO> keyValues = new ArrayList<>();
                        keyValues.add(strategyVO);
                        signStrategyInfo.put(key, keyValues);
                    }
                    if (signTypeParams.indexOf(signType) < 0) {
                        if (signTypeParams.length() > 0) {
                            signTypeParams.append(",");
                        }
                        signTypeParams.append(signType);
                    }
                }
                if (corpIdParams.indexOf(strategyVO.getCorpId()) < 0) {
                    if (corpIdParams.length() > 0) {
                        corpIdParams.append(",");
                    }
                    corpIdParams.append(strategyVO.getCorpId());
                }
                if (billTypeParams.indexOf(strategyVO.getBillType()) < 0) {
                    if (billTypeParams.length() > 0) {
                        billTypeParams.append(",");
                    }
                    billTypeParams.append(strategyVO.getBillType());
                }
            }
//          corpId billType signType
            params.put("corpId",corpIdParams.toString().split(","));
            params.put("signType",signTypeParams.toString().split(","));
            params.put("billType",billTypeParams.toString().split(","));
            signStrategyInfo.put("idToStrategyVO", idToStrategyVOMap);
        }
    }
    /**
     * 进行策略出票人、承兑人、收票人数据初始化 zhengg 20200721
     * @param signStrategyInfo 所有签收策略信息缓存
     */
    public void initAbBillSignStrategyData(Map<String,Object> signStrategyInfo){
        Map<String, AbBillSignStrategyVO> idToStrategyVOMap = (Map<String, AbBillSignStrategyVO>)signStrategyInfo.get("idToStrategyVO");
        List<AbBillSignStrategyDataVO> strategyDataVOList=abBillSignStrategyDataService.getAllValidBillSignStrategyData();
        if(ListUtils.isNotNullOrEmpty(strategyDataVOList)) {
            for(AbBillSignStrategyDataVO strategyDataVO:strategyDataVOList) {
                if(idToStrategyVOMap.containsKey(strategyDataVO.getSsId())){
                    if(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_01.equals(strategyDataVO.getDataType())){
                        idToStrategyVOMap.get(strategyDataVO.getSsId()).getPayerMap().put(strategyDataVO.getName(),strategyDataVO);
                    }else if(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_02.equals(strategyDataVO.getDataType())) {
                        if(StringUtils.isEmpty(strategyDataVO.getOrgTag())){
                            idToStrategyVOMap.get(strategyDataVO.getSsId()).getAcceptorMap().put(strategyDataVO.getCode(),strategyDataVO);
                        }else{
                            idToStrategyVOMap.get(strategyDataVO.getSsId()).getAcceptorMap().put(strategyDataVO.getName(),strategyDataVO);
                        }
                    }else if(AccebillConstant.BILL_SIGN_STRATEGY_DATA_TYPE_03.equals(strategyDataVO.getDataType())) {
                        idToStrategyVOMap.get(strategyDataVO.getSsId()).getApplyMap().put(strategyDataVO.getName(),strategyDataVO);
                    }
                }
            }
        }
    }
}
