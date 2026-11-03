package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.guarantee.domain.dao.GuaExguaranteeAmountDao;
import com.bytter.guarantee.domain.dao.GuaExguaranteeCorpDao;
import com.bytter.guarantee.domain.entity.GuaExguaranteeAmount;
import com.bytter.guarantee.domain.entity.GuaExguaranteeCorp;
import com.bytter.guarantee.domain.vo.GuaExguaranteeAmountVO;
import com.bytter.guarantee.domain.vo.GuaExguaranteeCorpVO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("GuaExguaranteeAmountService")
@Transactional
public class GuaExguaranteeAmountService extends BaseService<GuaExguaranteeAmount, String> {
    @Autowired
    private GuaExguaranteeAmountDao guaExguaranteeAmountDao;
    @Autowired
    private GuaExguaranteeCorpDao guaExguaranteeCorpDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    private static ConcurrentHashMap<String,CorpDTO> corpCache = new ConcurrentHashMap<>();

    @Override
    public BaseDao<GuaExguaranteeAmount, String> getEntityDao() {
        return guaExguaranteeAmountDao;
    }
    /**
     * 查询信息列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<GuaExguaranteeAmountVO> output = guaExguaranteeAmountDao.getDataTablesUnion(input);
        initCorp(output.getData());
        output.getData().stream().forEach(vo -> {
            vo.setAssetsProportion(vo.getAssets().multiply(vo.getProportion()));
        });
        return output;
    }
    /**
     * 加载单位信息
     *
     * @param list
     * @return
     */
    public List<GuaExguaranteeAmountVO> initCorp(List<GuaExguaranteeAmountVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
        	 List<CorpDTO> corpDTOS = corpService.listEffectiveCorp();
             if (EmptyUtils.isEmpty(corpDTOS)) {
                 return list;
             }
             Map<String, CorpDTO> collect = corpDTOS.stream().collect(Collectors.toMap(corpDTO -> corpDTO.getId(), Function.identity()));
            for (GuaExguaranteeAmountVO vo : list) {
                List<GuaExguaranteeCorpVO> corpList = guaExguaranteeCorpDao.getGuaranteeCorpByid(vo.getId());
                if (!EmptyUtils.isEmptyList(corpList)) {
                    StringBuilder corpIds = new StringBuilder();
                    StringBuilder corpNames = new StringBuilder();
                    for (GuaExguaranteeCorpVO corp : corpList) {
                        if (EmptyUtils.isBlank(corpIds.toString())) {
                            corpIds.append(corp.getCorpId());
                            corpNames.append(((CorpDTO)collect.get(corp.getCorpId())).getName());
                        } else {
                            corpIds.append(CommonConstant.COMMA_SPLIT).append(corp.getCorpId());
                            corpNames.append(CommonConstant.COMMA_SPLIT).append(collect.get(corp.getCorpId()).getName());
                        }
                    }
                    vo.setCorpId(corpIds.toString());
                    vo.setCorpName(corpNames.toString());
                }
            }
        }
        return list;
    }
    /**
     * 加载单位信息
     *
     * @param vo
     * @return
     */
    public GuaExguaranteeAmountVO initCorp(GuaExguaranteeAmountVO vo) {
        if (!EmptyUtils.isEmpty(vo)) {
            List<GuaExguaranteeCorpVO> corpList = guaExguaranteeCorpDao.getGuaranteeCorpByid(vo.getId());
            if (!EmptyUtils.isEmptyList(corpList)) {
                StringBuilder corpIds = new StringBuilder();
                for (GuaExguaranteeCorpVO corp : corpList) {
                    if (EmptyUtils.isBlank(corpIds.toString())) {
                        corpIds.append(corp.getCorpId());
                    } else {
                        corpIds.append(CommonConstant.COMMA_SPLIT).append(corp.getCorpId());
                    }
                }
                vo.setCorpId(corpIds.toString());
            }
        }
        return vo;
    }
    /**
     * 根据id查找用途信息
     *
     * @param id
     * @return
     */
    public GuaExguaranteeAmountVO getByUnion(String id) {
        GuaExguaranteeAmountVO vo = new GuaExguaranteeAmountVO();
        GuaExguaranteeAmount exguaranteeAmount = this.get(id);
        if (EmptyUtils.isEmpty(exguaranteeAmount)) {
            return vo;
        }
        BeanUtils.copyNotNullProperties(exguaranteeAmount, vo);
        return initCorp(vo);
    }
    /**
     * 新增保存
     *
     * @param vo
     */
    public void saveBtPurpose(GuaExguaranteeAmountVO vo, String userLoginName) {
        GuaExguaranteeAmount guaExguaranteeAmount = new GuaExguaranteeAmount();
        BeanUtils.copyNotNullProperties(vo, guaExguaranteeAmount);
        guaExguaranteeAmount.setCreateBy(userLoginName);
        guaExguaranteeAmount.setCreateDate(DateUtils.getDate());
        initCorpDTO();
        //TODO 补充币种名称
        if(StringUtils.isNotEmpty(guaExguaranteeAmount.getCurId())){
            BtCurrencyDTO currencyDTO = btCurrencyServiceImpl.getObjectById(guaExguaranteeAmount.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currency -> guaExguaranteeAmount.setCurName(currency.getCurName()));
        }
        // 对维护的单位进行检测
        checkGuaRanteeCorp(vo.getCorpId(), vo.getCurId(),null);
        this.saveEntity(guaExguaranteeAmount);
        this.saveExguaranteeCorp(guaExguaranteeAmount.getId(), vo.getCorpId(), vo.getCurId());
        destoryCorpDTO();
    }
    /**
     * 修改保存
     *
     * @param vo
     */
    public void modifyBtPurpose(GuaExguaranteeAmountVO vo, String userLoginName) {
        GuaExguaranteeAmount guaExguaranteeAmount = this.get(vo.getId());
        BeanUtils.copyNotNullProperties(vo, guaExguaranteeAmount);
        guaExguaranteeAmount.setUpdateBy(userLoginName);
        guaExguaranteeAmount.setUpdateDate(DateUtils.getDate());
        initCorpDTO();
        //TODO 补充币种名称
        if(StringUtils.isNotEmpty(guaExguaranteeAmount.getCurId())){
            BtCurrencyDTO currencyDTO = btCurrencyServiceImpl.getObjectById(guaExguaranteeAmount.getCurId());
            Optional.ofNullable(currencyDTO).ifPresent(currency -> guaExguaranteeAmount.setCurName(currency.getCurName()));
        }

        // 对维护的单位进行检测
        checkGuaRanteeCorp(vo.getCorpId(), vo.getCurId(), vo.getId());
        this.updateEntity(guaExguaranteeAmount);
        this.saveExguaranteeCorp(guaExguaranteeAmount.getId(),vo.getCorpId(), vo.getCurId());
        destoryCorpDTO();
    }
    public void saveExguaranteeCorp(String amountId, String corpIds, String curId) {
        List<GuaExguaranteeCorpVO> oldCorpList = guaExguaranteeCorpDao.getGuaranteeCorpByid(amountId);
        // 删除之前的关联单位
        if (!EmptyUtils.isEmptyList(oldCorpList)) {
            guaExguaranteeCorpDao.deleteExguaranteeCorp(amountId);
        }
        BtCurrencyDTO cur = btCurrencyServiceImpl.getObjectById(curId);

        for (String corpId : corpIds.split(CommonConstant.COMMA_SPLIT)) {
            GuaExguaranteeCorp exguaranteeCorp = new GuaExguaranteeCorp();
            exguaranteeCorp.setExAmountId(amountId);
            exguaranteeCorp.setCorpId(corpId);
            //TODO 补充单位信息
            String corpName = !EmptyUtils.isEmpty(corpCache) ? corpCache.get(corpId).getName() : null;
            exguaranteeCorp.setCorpName(corpName);
            Integer sort = !EmptyUtils.isEmpty(corpCache) ? corpCache.get(corpId).getSort() : null;
            exguaranteeCorp.setSort(sort);

            exguaranteeCorp.setCurId(curId);
            exguaranteeCorp.setCurName(cur.getCurName());
            guaExguaranteeCorpDao.save(exguaranteeCorp);
        }
    }
    public void checkGuaRanteeCorp(String corpIds, String curId, String amountId) {
        String checkCorps = null;
        Boolean checkflag = false;
        for (String corpId : corpIds.split(CommonConstant.COMMA_SPLIT)) {
            List<GuaExguaranteeCorpVO> list = guaExguaranteeCorpDao.checkGuaranteeCorp(corpId, curId, amountId);
            //TODO 代码简化
            if (EmptyUtils.isEmpty(list)){
                continue;
            }
            if (EmptyUtils.isEmpty(corpCache)){
                checkflag = true;
                //没有就返回ID
                checkCorps = (checkCorps == null ? corpId : (checkCorps + "," + corpId));
                continue;
            }
            checkflag = true;
            String corpName = corpCache.get(corpId).getName();
            checkCorps = (checkCorps == null ? corpName : (checkCorps + "," + corpName));

        }
        if (checkflag) {
            String curName = btCurrencyServiceImpl.getObjectById(curId).getCurName();
            throw new ParameterException(GuaranteeMessageEnum.CMS_EXGUARANTEE_AMOUNT_UNIQUE, checkCorps, curName);
        }
    }
    /**
     * 根据用途ID加载所属单位的信息,并进行排序
     *
     * @param amountId
     * @return
     */
    public List<GuaExguaranteeCorpVO> getCorpIdByAmountId(String amountId) {
        List<GuaExguaranteeCorpVO> exguaranteeCorpVOS = guaExguaranteeCorpDao.getGuaranteeCorptreeByid(amountId);
        //TODO 补充单位相关信息,冗余了排序字段
        fillCorp(exguaranteeCorpVOS);
        // 当所选节点没有父节点时,而从sys_corp表中取出的parentId不为空,进行排序时会过滤掉,这里将子节点的parentID重置,以第一级父节点显示
        Map<String, GuaExguaranteeCorpVO> exguaranteeMap = new HashMap<>();
        for (GuaExguaranteeCorpVO vo : exguaranteeCorpVOS) {
            exguaranteeMap.put(vo.getCorpId(), vo);
        }
        for (GuaExguaranteeCorpVO vo : exguaranteeCorpVOS) {
            if (null != vo.getParentCorpId() && null == exguaranteeMap.get(vo.getParentCorpId())) {
                vo.setParentCorpId("");
            }
        }
        List<GuaExguaranteeCorpVO> guaExguaranteeCorpVOS = TreeUtil.coverTreeTable(exguaranteeCorpVOS);
        return guaExguaranteeCorpVOS;
    }
    private void fillCorp(List<GuaExguaranteeCorpVO> exguaranteeCorpVOS) {
        if (EmptyUtils.isEmpty(exguaranteeCorpVOS)){
            return;
        }
        List<String> corpIds = exguaranteeCorpVOS.stream().map(exguaranteeCorp -> exguaranteeCorp.getCorpId()).collect(Collectors.toList());
        Map<String, CorpDTO> sortCorpMap = corpService.listCorpDtoByCorpIds(corpIds);
        if (EmptyUtils.isEmpty(sortCorpMap)){
            return;
        }
        for(GuaExguaranteeCorpVO exguaranteeCorp : exguaranteeCorpVOS){
            exguaranteeCorp.setCorpCode(sortCorpMap.get(exguaranteeCorp.getCorpId()).getCode());
            exguaranteeCorp.setParentCorpId(sortCorpMap.get(exguaranteeCorp.getCorpId()).getParentId());
        }
    }
    public void delete(String amountId) {
        GuaExguaranteeAmount amount = this.get(amountId);
        guaExguaranteeCorpDao.deleteExguaranteeCorp(amountId);
        this.deleteEntity(amount);
    }
    //初始化单位信息，不存在并发线程
    private void initCorpDTO() {
        if (!EmptyUtils.isEmpty(corpCache)) {
            corpCache.clear();
        }
        List<CorpDTO> corpDTOS = corpService.listEffectiveCorp();
        if (EmptyUtils.isEmpty(corpDTOS)) {
            return;
        }
        Map<String, CorpDTO> collect = corpDTOS.stream().collect(Collectors.toMap(corpDTO -> corpDTO.getId(), Function.identity()));
        corpCache.putAll(collect);
    }
    //销毁
    private void destoryCorpDTO() {
        if (!EmptyUtils.isEmpty(corpCache)) {
            corpCache.clear();
        }
    }
}
