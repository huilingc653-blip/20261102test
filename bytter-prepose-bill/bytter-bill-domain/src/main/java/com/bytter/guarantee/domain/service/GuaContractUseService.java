package com.bytter.guarantee.domain.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.credential.api.service.ILcEnrolService;
import com.bytter.credit.api.IRatBkrationService;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.api.ICmsProvideLoanService;
import com.bytter.guarantee.domain.dao.GuaContractUseDao;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.vo.GuaContractUseDtlVO;
import com.bytter.guarantee.domain.vo.GuaContractUseVO;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.bytter.core.utils.BeanUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: GuaContractUseService
 * @ProjectName T3(3.2)
 * @Description: 被担保合同使用登记服务层
 * @date 2020/4/29 10:01
 */
@Service("guaContractUseService")
public class GuaContractUseService extends BaseService<GuaContractOccup, String> {
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractUseDao guaContractUseDao;
    @DubboReference(check = false)
    private IDictService iDictService;

    @Autowired(required = false)
    private IRatBkrationService bkrationService;
    @Autowired(required = false)
    private ICmsLoanService cmsLoanService;
    @Autowired(required = false)
    private ICmsProvideLoanService cmsProvideLoanService;
    @Autowired(required = false)
    @Qualifier("accebillServiceImpl")
    private IAccebillService accebillService;
    @DubboReference(check = false)
    private ILcEnrolService lcEnrolService;

    /**
     * 保存担保合同使用记录
     */
    public void save(GuaContractUseVO guaContractUseVO, ICurrentUser user) {
        Integer status = guaContractUseVO.getStatus();
        Date useDate = DateUtils.getDate();
        List<GuaContractUseDtlVO> useDtls = guaContractUseVO.getUseDtls();
        List<GuaContractOccup> occups = BeanUtils.copyListProperties(useDtls, GuaContractOccup.class);
        if (status.equals(GuaranteeConstant.GUA_CONTRACT_USED)) {
            if (EmptyUtils.isNotEmpty(occups)) {
                for (GuaContractOccup guaContractOccup : occups) {
                    if (!EmptyUtils.isNotEmpty(guaContractOccup.getBusinessId())) {
                        continue;
                    }
                    guaContractOccup.setStatus(status);
                    guaContractOccup.setDataType(GuaranteeConstant.DATA_TYPE_1);// 数据类型为使用登记
                    guaContractOccup.setUseDate(useDate);
                    occupyBusGuaConfig(guaContractOccup.getSysCode(), guaContractOccup.getBusinessId(), user);
                    this.saveOrUpdate(guaContractOccup);
                }
            }
        } else {// 暂存状态不需要更改对应业务参数
            if (EmptyUtils.isNotEmpty(occups)) {
                for (GuaContractOccup guaContractOccup : occups) {
                    if (!EmptyUtils.isNotEmpty(guaContractOccup.getBusinessId())) {
                        continue;
                    }
                    guaContractOccup.setStatus(status);
                    guaContractOccup.setDataType(GuaranteeConstant.DATA_TYPE_1);// 数据类型为使用登记
                    guaContractOccup.setUseDate(useDate);
                    this.saveOrUpdate(guaContractOccup);
                }
            }
        }
    }
    @Override
    public BaseDao<GuaContractOccup, String> getEntityDao() {
        return guaContractUseDao;
    }
    /**
     * 删除暂存的使用登记数据
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 提交暂存数据
     */
    public void submit(String ids, ICurrentUser user) {
        String[] idArr = ids.split(",");
        GuaContractOccup contractOccup;
        if (idArr.length > 0) {
            for (String id : idArr) {
                contractOccup = this.get(id);
                contractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_USED);// 修改状态为使用中
                super.saveOrUpdate(contractOccup);
                occupyBusGuaConfig(contractOccup.getSysCode(), contractOccup.getBusinessId(), user);
                this.getEntityDao().flush();
            }
        }
    }
    /**
     * 查询审批通过的担保合同
     */
    public DataTablesOutput dataTables(DataTablesInput input, ICurrentUser user) {
        return guaContractInfoService.getApprovedDataTables(input, GuaranteeConstant.GUA_CONTRACT_USE_QUERY, user);
    }
    /**
     * 根据担保合同id查询担保合同使用列表数据
     */
    public List<GuaContractUseDtlVO> getUseList(String contractId) {
        List<GuaContractUseDtlVO> useList = guaContractUseDao.getUseList(contractId);
        // 字典组
        Map<String, DictDTO> dictDTOMap = iDictService.listMap("gua.busType");
        for (GuaContractUseDtlVO vo : useList){
            if (ObjectUtil.isNotEmpty(vo.getSysCode()) && dictDTOMap.containsKey(vo.getSysCode())){
                vo.setBusinessName(dictDTOMap.get(vo.getSysCode()).getName());
            }
        }

        return useList;
    }
    /**
     * todo 更改业务挂接合同参数修改的是业务模块自身的数据，和授信没任何交互需要迁移到自身模块内 更改业务挂接担保合同参数为是
     *
     * @param sysCode
     * @param busId
     */
    public void occupyBusGuaConfig(String sysCode, String busId, ICurrentUser user) {
        switch (sysCode) {
            case "rat":// 银行授信
                bkrationService.occupyGuaContract(busId);
                break;
            case "provide":// 银行借款
                cmsProvideLoanService.occupyGuaContract(busId);
                break;
            case "loan":// 内部贷款
                cmsLoanService.occupyGuaContract(busId);
                break;
            case "accebill":// 承兑汇票
                accebillService.updateGuarantee(busId,GuaranteeConstant.GUA_COMMON_Y,user);
                break;
            case "lcs":// 信用证
                lcEnrolService.updateLcGuaById(busId);
                break;
        }
    }
    /**
     * 根据id查询占用详情
     */
    public GuaContractUseDtlVO queryUseDtlById(String id) {
        GuaContractUseDtlVO guaContractUseDtlVO = guaContractUseDao.queryUseDtlById(id);
        if (ObjectUtil.isNotEmpty(guaContractUseDtlVO.getSysCode())){
            guaContractUseDtlVO.setBusinessName(iDictService.get("gua.busType",guaContractUseDtlVO.getSysCode()));
        }
        return guaContractUseDtlVO;
    }
    /**
     * 根据合同id及状态查询占用详情
     */
    public List<GuaContractUseDtlVO> queryUseDtlByConId(String ContractId, String sysCode) {
        return guaContractUseDao.queryUseDtlByConId(ContractId, sysCode);
    }
}
