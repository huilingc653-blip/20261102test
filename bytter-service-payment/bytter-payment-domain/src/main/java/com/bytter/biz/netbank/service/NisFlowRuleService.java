package com.bytter.biz.netbank.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.NisFlowRulePurposeDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.netbank.dao.NisFlowRuleDao;
import com.bytter.biz.netbank.dto.NisFlowRuleDTO;
import com.bytter.biz.netbank.entity.NisFlowRule;
import com.bytter.biz.netbank.entity.NisFlowRulePurpose;
import com.bytter.biz.netbank.vo.NisFlowRulePurposeVO;
import com.bytter.biz.netbank.vo.NisFlowRuleVO;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 资金调拨流水规则
 * @date 2024-05-08
 */
@Service("nisFlowRuleService")
@Transactional
public class NisFlowRuleService extends BaseService<NisFlowRule, String> {

    @Autowired
    private NisFlowRuleDao nisFlowRuleDao;
    @Autowired
    private NisBusTypeService nisBusTypeService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @Autowired(required = false)
    private ThreadPoolTaskExecutor asyncThreadPoolExecutor;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @Override
    public BaseDao<NisFlowRule, String> getEntityDao() {
        return nisFlowRuleDao;
    }

    /**
     * 查询业务类型列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisFlowRuleVO> findListBysql(DataTablesInput input) {
        DataTablesOutput<NisFlowRuleVO> output= nisFlowRuleDao.findListBysql(input);
        List<NisFlowRuleVO> list = output.getData();
        for(NisFlowRuleVO vo:list) {
            vo.setTransferModeName(dictService.get("nisFlowRule.transferMode", vo.getTransferMode()));
            vo.setTransferTypeName(dictService.get("nisFlowRule.transferType", vo.getTransferType()));
            vo.setStatusName(dictService.get("nisFlowRule.status", vo.getStatus()));
            List<NisFlowRulePurposeVO> nisFlowRulePurposeList = nisFlowRuleDao.getNisFlowRulePurpose(vo.getId());
            String purposes = "";
            for(int i=0; i < nisFlowRulePurposeList.size(); i++){
                NisFlowRulePurposeVO nisFlowRulePurposeVO = nisFlowRulePurposeList.get(i);
                purposes = purposes + nisFlowRulePurposeVO.getPurpose();
                if(i < nisFlowRulePurposeList.size() - 1){
                    purposes = purposes + "、";
                }
            }
            vo.setPurpose(purposes);
        }
        return output;
    }

    /**
     * 保存
     *
     * @param nisFlowRuleVO
     */
    public void save(NisFlowRuleVO nisFlowRuleVO, HttpServletRequest request, ICurrentUser currentUser) {
        NisFlowRule entity = new NisFlowRule();
        BeanUtils.copyNotNullProperties(nisFlowRuleVO, entity);
        entity.setCreateBy(currentUser.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);

        //判断调拨方式 1-资金系统 3-人工
        if("2".equals(nisFlowRuleVO.getTransferMode())){
            String[] bankTypeIds = request.getParameterValues("bankTypeId");
            String[] purpose = request.getParameterValues("purpose1");
            List<NisFlowRulePurpose> list = new ArrayList<>();
            for(int i=0;i<bankTypeIds.length;i++){
                NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
                //规则id
                nisFlowRulePurpose.setRuleId(entity.getId());
                //用途
                nisFlowRulePurpose.setPurpose(purpose[i]);
                //银行类型id
                nisFlowRulePurpose.setBankTypeId(bankTypeIds[i]);
                list.add(nisFlowRulePurpose);
            }
            nisFlowRuleDao.batchSave(list);
        }else{
            NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
            //用途
            nisFlowRulePurpose.setPurpose(nisFlowRuleVO.getPurpose());
            //规则id
            nisFlowRulePurpose.setRuleId(entity.getId());
            nisFlowRuleDao.save(nisFlowRulePurpose);
        }
    }

    /**
     * 保存
     *
     * @param nisFlowRuleVO
     */
    public void saveV1(NisFlowRuleDTO nisFlowRuleVO, ICurrentUser currentUser) {
        NisFlowRule entity = new NisFlowRule();
        BeanUtils.copyNotNullProperties(nisFlowRuleVO, entity);
        entity.setCreateBy(currentUser.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);

        //判断调拨方式 1-资金系统 3-人工
        if("2".equals(nisFlowRuleVO.getTransferMode())){
            String[] bankTypeIds =nisFlowRuleVO.getBankTypeIds();
            String[] purposes = nisFlowRuleVO.getPurpose1();
            List<NisFlowRulePurpose> list = new ArrayList<>();
            for(int i=0;i<bankTypeIds.length;i++){
                NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
                //规则id
                nisFlowRulePurpose.setRuleId(entity.getId());
                //用途
                nisFlowRulePurpose.setPurpose(purposes[i]);
                //银行类型id
                nisFlowRulePurpose.setBankTypeId(bankTypeIds[i]);
                list.add(nisFlowRulePurpose);
            }
            nisFlowRuleDao.batchSave(list);
        }else{
            NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
            //用途
            nisFlowRulePurpose.setPurpose(nisFlowRuleVO.getPurpose());
            //规则id
            nisFlowRulePurpose.setRuleId(entity.getId());
            nisFlowRuleDao.save(nisFlowRulePurpose);
        }
    }



    /**
     * 根据规则id删除规则用途、规则
     * @param id
     */
    public void delete(String id){
        //删除规则用途
        nisFlowRuleDao.deleteNisFlowRulePurpose(id);
        //删除规则
        nisFlowRuleDao.delete(id);
    }

    /**
     * 根据规则id删除规则用途、规则
     * @param id
     */
    public NisFlowRuleVO getNisFlowRule(String id){
        NisFlowRuleVO nisFlowRuleVO = new NisFlowRuleVO();
        NisFlowRule nisFlowRule = nisFlowRuleDao.get(id);
        BeanUtils.copyNotNullProperties(nisFlowRule,nisFlowRuleVO);
        List<NisFlowRulePurposeVO> nisFlowRulePurpose = nisFlowRuleDao.getNisFlowRulePurpose(nisFlowRuleVO.getId());
        Map<String, BankTypeDTO> bankTypeMap = bankTypeService.getBankTypeMap();
        for(NisFlowRulePurposeVO nisFlowRulePurposeVO :nisFlowRulePurpose){
            if(bankTypeMap.containsKey(nisFlowRulePurposeVO.getBankTypeId())){
                nisFlowRulePurposeVO.setBankName(bankTypeMap.get(nisFlowRulePurposeVO.getBankTypeId()).getTypeName());
            }
        }
        nisFlowRuleVO.setNisFlowRulePurposes(nisFlowRulePurpose);
      //  nisFlowRuleVO.setTransferModeName(dictService.get("nisFlowRule.transferMode", nisFlowRuleVO.getTransferMode()));
      //  nisFlowRuleVO.setTransferTypeName(dictService.get("nisFlowRule.transferType", nisFlowRuleVO.getTransferType()));
      //  nisFlowRuleVO.setStatusName(dictService.get("nisFlowRule.status", nisFlowRuleVO.getStatus()));
        return nisFlowRuleVO;
    }

    /**
     * 修改
     *
     * @param nisFlowRuleVO
     */
    public void update(NisFlowRuleVO nisFlowRuleVO, HttpServletRequest request, ICurrentUser currentUser) {
        NisFlowRule entity = new NisFlowRule();
        BeanUtils.copyNotNullProperties(nisFlowRuleVO, entity);
        entity.setUpdateBy(currentUser.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        nisFlowRuleDao.update(entity);

        nisFlowRuleDao.deleteNisFlowRulePurpose(entity.getId());

        //判断调拨方式 1-资金系统 2-银行 3-人工
        if(nisFlowRuleVO.getTransferMode().equals("2")){
            String[] bankTypeIds = request.getParameterValues("bankTypeId");
            String[] purpose = request.getParameterValues("purpose1");
            List<NisFlowRulePurpose> list = new ArrayList<>();
            for(int i=0;i<bankTypeIds.length;i++){
                NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
                //规则id
                nisFlowRulePurpose.setRuleId(entity.getId());
                //用途
                nisFlowRulePurpose.setPurpose(purpose[i]);
                //银行类型id
                nisFlowRulePurpose.setBankTypeId(bankTypeIds[i]);
                list.add(nisFlowRulePurpose);
            }
            nisFlowRuleDao.batchSave(list);
        }else{
            NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
            //用途
            nisFlowRulePurpose.setPurpose(nisFlowRuleVO.getPurpose());
            //规则id
            nisFlowRulePurpose.setRuleId(entity.getId());
            nisFlowRuleDao.save(nisFlowRulePurpose);
        }
    }



    public void updateV1(NisFlowRuleDTO nisFlowRuleVO, ICurrentUser currentUser) {
        NisFlowRule entity = new NisFlowRule();
        BeanUtils.copyNotNullProperties(nisFlowRuleVO, entity);
        entity.setUpdateBy(currentUser.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        nisFlowRuleDao.update(entity);

        nisFlowRuleDao.deleteNisFlowRulePurpose(entity.getId());

        //判断调拨方式 1-资金系统 2-银行 3-人工
        if(nisFlowRuleVO.getTransferMode().equals("2")){
            String[] bankTypeIds =nisFlowRuleVO.getBankTypeIds();
            String[] purpose = nisFlowRuleVO.getPurpose1();
            List<NisFlowRulePurpose> list = new ArrayList<>();
            for(int i=0;i<bankTypeIds.length;i++){
                NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
                //规则id
                nisFlowRulePurpose.setRuleId(entity.getId());
                //用途
                nisFlowRulePurpose.setPurpose(purpose[i]);
                //银行类型id
                nisFlowRulePurpose.setBankTypeId(bankTypeIds[i]);
                list.add(nisFlowRulePurpose);
            }
            nisFlowRuleDao.batchSave(list);
        }else{
            NisFlowRulePurpose nisFlowRulePurpose = new NisFlowRulePurpose();
            //用途
            nisFlowRulePurpose.setPurpose(nisFlowRuleVO.getPurpose());
            //规则id
            nisFlowRulePurpose.setRuleId(entity.getId());
            nisFlowRuleDao.save(nisFlowRulePurpose);
        }
    }

    /**
     * 定时任务：资金调拨流水自动认领
     */
    @XxlJob("fundTansferFlowAutoClaimJob")
    public void fundTansferFlowAutoClaimJob() {
        List<NisFlowRuleVO> allNisFlowRule = this.getAllNisFlowRule();
        XxlJobHelper.log("当前获取【{}】条资金调拨流水自动认领规则记录。", allNisFlowRule.size());
        String busType = "";
        if(ObjectUtil.isNotEmpty(allNisFlowRule)){
            for(NisFlowRuleVO nisFlowRuleVO : allNisFlowRule){
                //调拨类型 10-上划 11-下拨
                if(nisFlowRuleVO.getTransferType().equals(AllocationConstant.TRANSFER_TYPE_10)){
                    //调拨方式 1-资金系统 2-银行自动 3-人工
                    if(NetbankConstant.TRANSFER_MODE_1.equals(nisFlowRuleVO.getTransferMode())){
                        //上划-资金系统自动
                        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS005;
                    }else if(NetbankConstant.TRANSFER_MODE_2.equals(nisFlowRuleVO.getTransferMode())){
                        //上划-银行系统自动
                        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS006;
                    }else{
                        //上划-人工
                        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS007;
                    }
                }else{
                    //调拨方式 1-资金系统 2-银行自动 3-人工
                    if(NetbankConstant.TRANSFER_MODE_1.equals(nisFlowRuleVO.getTransferMode())){
                        //下拨-资金系统自动
                        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS008;
                    }else if(NetbankConstant.TRANSFER_MODE_2.equals(nisFlowRuleVO.getTransferMode())){
                        //下拨-银行系统自动
                        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS009;
                    }else{
                        //下拨-人工
                        busType = NisBusTypeConstant.SYS_BUS_TYPE_BS010;
                    }
                }
                XxlJobHelper.log("获取业务类型【{}】！", busType);
                NisBusTypeDTO busTypeDTO = nisBusTypeService.getByBusType(busType);
                if (ObjectUtil.isEmpty(busTypeDTO)) {
                    XxlJobHelper.log("获取业务类型【{}】失败，请先添加相关业务类型！", busType);
                    return;
                }

                List<NisFlowRulePurposeDTO> list  = BeanUtils.copyListProperties(nisFlowRuleVO.getNisFlowRulePurposes(),NisFlowRulePurposeDTO.class);

                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() ->
                                bisAccDtlService.fundTansferFlowAutoClaim(busTypeDTO.getId(), list, nisFlowRuleVO.getTransferMode()),
                        asyncThreadPoolExecutor);
            }
        }
        XxlJobHelper.log("定时任务：资金调拨流水自动认领 结束》》》》》》》》》");
    }

    /**
     * 获取所有有效的业务规则
     */
    public List<NisFlowRuleVO> getAllNisFlowRule(){
        List<NisFlowRule> nisFlowRuleList = nisFlowRuleDao.listBy("status", CommonConstant.VALID_SIGN_YES);
        List<NisFlowRuleVO> nisFlowRuleVOList =BeanUtils.copyListProperties(nisFlowRuleList, NisFlowRuleVO.class);
        Map<String, BisBifInitDTO> bisBifInitBankTypeIdMap = bisBifInitService.getBisBifInitBankTypeIdMap();
        for(NisFlowRuleVO nisFlowRuleVO : nisFlowRuleVOList){
            List<NisFlowRulePurposeVO> nisFlowRulePurposeList = nisFlowRuleDao.getNisFlowRulePurpose(nisFlowRuleVO.getId());
            for(NisFlowRulePurposeVO nisFlowRulePurposeVO : nisFlowRulePurposeList){
                if(bisBifInitBankTypeIdMap.containsKey(nisFlowRulePurposeVO.getBankTypeId())){
                    nisFlowRulePurposeVO.setBifCode(bisBifInitBankTypeIdMap.get(nisFlowRulePurposeVO.getBankTypeId()).getId());
                }
            }
            nisFlowRuleVO.setNisFlowRulePurposes(nisFlowRulePurposeList);
        }

        return nisFlowRuleVOList;
    }
}
