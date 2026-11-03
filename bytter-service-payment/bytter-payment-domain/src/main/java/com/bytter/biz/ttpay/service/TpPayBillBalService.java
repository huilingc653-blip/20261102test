package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpPayBillBalDao;
import com.bytter.biz.ttpay.vo.TpPayBillBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Service
 * @Description: 商户信息余额Service
 * @date 2022-02-15
 */
@Service("tpPayBillBalService")
@Transactional
public class TpPayBillBalService extends BaseService<T, String> {
    @Autowired
    private TpPayBillBalDao tpPayBillBalDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<T, String> getEntityDao() {
        return tpPayBillBalDao;
    }
    /**
     * @param input
     * @return 列表数据
     * @throws
     * @Title: 商户信息流水查询信息查询列表
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP, input.getCurrentUser().getId());
        DataTablesOutput data = tpPayBillBalDao.getDataTablesUnion(input, corpAuthList);
        return this.setJoinField(data);
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<TpPayBillBalVO> list = output.getData();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT);
            for (TpPayBillBalVO vo:list) {
                if (corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpCode(corpMap.get(vo.getCorpId()).getCode());
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                }
                //字典注入
                this.injectValues(vo, listDictMap);
            }
        }
        return output;
    }
    /**
     * 字典注入
     *
     * @param vo
     * @param listDictMap
     */
    private void injectValues(TpPayBillBalVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 支付方式
        Optional.ofNullable(vo.getPayType()).ifPresent(payType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_PAYTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payType)))
                    .ifPresent(dict -> vo.setPayTypeName(dict.getName()));
        });
    }
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public TpPayBillBalVO getTpPayBillBal(String corpId, String merchantNo, String payType) {
        return tpPayBillBalDao.getTpPayBillBal(corpId, merchantNo, payType);
    }
}
