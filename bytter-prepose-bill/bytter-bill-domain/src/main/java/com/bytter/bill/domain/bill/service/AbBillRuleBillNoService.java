package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillRuleBillNoDao;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillNo;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillNoVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @author wanggl
 * @Title: Service
 * @Description: 票号规则列表查询
 * @date 2022-03-03
 */
@Service("AbBillRuleBillNoService")
@Transactional
public class AbBillRuleBillNoService extends BaseService<AbBillRuleBillNo, String> implements IAccebillBaseService {
    @Autowired
    private AbBillRuleBillNoDao abBillRuleBillNoDao;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<AbBillRuleBillNo, String> getEntityDao() {
        return abBillRuleBillNoDao;
    }
    /**
     * 票号规则列表查询
     *
     * @param input
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getBillNOList(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(
                AccebillConstant.AB_BILL_RESOURCE_RULEBILLNO_QUERY, input.getCurrentUserId());
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_RESOURCE_RULEBILLNO_QUERY, corpIds);
        DataTablesOutput<AbBillRuleBillNoVO> out = abBillRuleBillNoDao.getDataTablesUnion(input, corpAuthList);
        Optional.ofNullable(out.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("accebill_billNo_insbriefid","accebill_billNo_billNumField"
                    ,"accebill_billNo_matchRelation","valid_sign");
            for (AbBillRuleBillNoVO bill : listData){
                injectValues(bill, listDictMap);
            }
        });
        return out;
    }
    private void injectValues(AbBillRuleBillNoVO bill, Map<String, Map<String, DictDTO>> listDictMap){
        // 业务类型
        Optional.ofNullable(bill.getBusType()).ifPresent(busType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("accebill_billNo_insbriefid");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busType)))
                    .ifPresent(dict -> bill.setBusTypeName(dict.getName()));
        });
        // 票号字段
        Optional.ofNullable(bill.getBillNumField()).ifPresent(billNumField -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("accebill_billNo_billNumField");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billNumField)))
                    .ifPresent(dict -> bill.setBillNumFieldName(dict.getName()));
        });
        // 匹配关系
        Optional.ofNullable(bill.getRelation()).ifPresent(relation -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("accebill_billNo_matchRelation");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(relation)))
                    .ifPresent(dict -> bill.setRelationName(dict.getName()));
        });
        // 有效标志
        Optional.ofNullable(bill.getMark()).ifPresent(mark -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("valid_sign");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(mark)))
                    .ifPresent(dict -> bill.setMarkName(dict.getName()));
        });
    }
    /**
     * 获取有效的票号规则
     *
     * @return 票号规则列表
     */
    public List<AbBillRuleBillNo> listValidInfo() {
        return abBillRuleBillNoDao.listValidInfo();
    }
    public AbBillRuleBillNoVO getVO(String id) {
        AbBillRuleBillNo abBillRuleBillNo = super.get(id);
        AbBillRuleBillNoVO abBillRuleBillNoVO = BeanUtils.copyNewInstanceProperties(abBillRuleBillNo, AbBillRuleBillNoVO.class);
        return abBillRuleBillNoVO;
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(AbBillRuleBillNo entity) {
        super.saveEntity(entity);
    }
    /**
     * 删除
     */
    public void delete(String id) {
        abBillRuleBillNoDao.delete(id);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(AbBillRuleBillNo entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        // todo:未实现
        return null;
    }
    /**
     * 检验银行类型+业务类型不允许重复，当重复时系统弹出提示对话框，提示文字为“银行类型+业务类型不允许重复”
     *
     * @param abBillRuleBillNoVO
     */
    public boolean checkAddBillNO(AbBillRuleBillNoVO abBillRuleBillNoVO) {
        List<AbBillRuleBillNoVO> list = abBillRuleBillNoDao.getByRepeat(abBillRuleBillNoVO);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkupdateBillNO(AbBillRuleBillNoVO abBillRuleBillNoVO) {
        List<AbBillRuleBillNoVO> list = abBillRuleBillNoDao.getByRepeat(abBillRuleBillNoVO);
        if (list.size() > 1) {
            return true;
        } else {
            return false;
        }
    }
}
