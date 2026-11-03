package com.bytter.bill.domain.bill.service;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.domain.bill.dao.AbBillSignStrategyDataDao;
import com.bytter.bill.domain.bill.entity.AbBillSignStrategyData;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @Title: AbBillSignStrategyDataService
 * @Description: 电票自动签收策略选择数据Service
 * @author wangzy
 * @date 2020/7/14 11:46
 *
 */
@Service("abBillSignStrategyDataService")
@Transactional
public class AbBillSignStrategyDataService extends BaseService<AbBillSignStrategyData, String> {
    @Autowired
    private AbBillSignStrategyDataDao abBillSignStrategyDataDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Override
    public BaseDao<AbBillSignStrategyData, String> getEntityDao() {
        return abBillSignStrategyDataDao;
    }
    /**
     * 获取全部有效的策略明细数据
     * zhengg 20200721
     * @return 策略明细数据信息
     */
    public List<AbBillSignStrategyDataVO> getAllValidBillSignStrategyData(){
        List<AbBillSignStrategyDataVO> strategyData = abBillSignStrategyDataDao.getAllValidBillSignStrategyData();
        if (ObjectUtil.isNotEmpty(strategyData)) {
            List<String> dataValList = strategyData.stream().map(AbBillSignStrategyDataVO::getDataVal).collect(Collectors.toList());
            List<Map<String, Object>> mapList = corpService.listStrategyDataByDataVal(dataValList);
            strategyData = strategyData.stream().map(source -> mapList.stream()
                            .filter(target -> ObjectUtil.equal(Convert.toStr(target.get("dataVal")), source.getDataVal()))
                            .findFirst()
                            .map(target -> {
                                source.setName(Convert.toStr(target.get("name")));
                                source.setCode(Convert.toStr(target.get("code")));
                                return source;
                            }).orElse(source))
                    .collect(Collectors.toList());
        }
        return strategyData;
    }
}
