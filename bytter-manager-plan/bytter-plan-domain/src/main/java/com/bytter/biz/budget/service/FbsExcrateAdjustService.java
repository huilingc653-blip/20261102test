package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsExcrateAdjustDao;
import com.bytter.biz.budget.entity.FbsExcrateAdjust;
import com.bytter.biz.budget.vo.FbsExcrateAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: FBS_EXCRATE_ADJUST
 * @date 2019-08-15 10:09:08
 */
@Service("fbsExcrateAdjustService")
@Transactional
public class FbsExcrateAdjustService extends BaseService<FbsExcrateAdjust, String> {
    @Autowired
    private FbsExcrateAdjustDao fbsExcrateAdjustDao;
    @DubboReference
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference
    private ISystemForeignService iSystemForeignService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FbsExcrateAdjust entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FbsExcrateAdjust entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FbsExcrateAdjust entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FbsExcrateAdjust entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FbsExcrateAdjust, String> getEntityDao() {
        return fbsExcrateAdjustDao;
    }
    /**
     * 根据原始货币id和目标货币id和日期查找最新一笔计划汇率调整表信息
     * @param curId 原始货币id
     * @param referCurId 目标货币id
     * @param endDate 结束日期
     * @return
     */
    public FbsExcrateAdjustVO getFbsExcrateAdjustByCurReferDate(String curId, String referCurId, Date endDate){
        List<FbsExcrateAdjustVO> listVO = fbsExcrateAdjustDao.getFbsExcrateAdjustByCurReferDate(curId, referCurId, endDate);
        if(EmptyUtils.isEmptyList(listVO)){
            return null;
        }
        //填充币种信息
        FbsExcrateAdjustVO fbsExcrateAdjustVO = listVO.get(0);
        this.fillData(fbsExcrateAdjustVO);
        return fbsExcrateAdjustVO;
    }
    /**
     * 根据id查找计划汇率调整表信息
     * @param id
     * @return
     */
    public FbsExcrateAdjustVO getByIdUnion(String id) {
        FbsExcrateAdjustVO fbsExcrateAdjustVO = fbsExcrateAdjustDao.getByIdUnion(id);
        this.fillData(fbsExcrateAdjustVO);
        return fbsExcrateAdjustVO;
    }
    /**
     * 查询计划汇率调整表信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        //TODO getCommonSql 修改了
        /*DataTablesOutput dataTablesUnion = fbsExcrateAdjustDao.getDataTablesUnion(dataTablesInput);
        List data = dataTablesUnion.getData();
        for (Object datum : data) {
        }*/
        return fbsExcrateAdjustDao.getDataTablesUnion(dataTablesInput);
    }
    private void fillData(FbsExcrateAdjustVO fbsExcrateAdjustVO){
//        BtCurrencyDTO btCurrencyDTO = iSystemForeignService.getBtCurrencyByCurId(fbsExcrateAdjustVO.getCurId());
        BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(fbsExcrateAdjustVO.getCurId());
        fbsExcrateAdjustVO.setCurCode(btCurrencyDTO.getCurCode());
        fbsExcrateAdjustVO.setCurName(btCurrencyDTO.getCurName());
        fbsExcrateAdjustVO.setEnglishCode(btCurrencyDTO.getEnglishCode());
        fbsExcrateAdjustVO.setReferCurId(fbsExcrateAdjustVO.getCurId());
        fbsExcrateAdjustVO.setReferCurCode(btCurrencyDTO.getCurCode());
        fbsExcrateAdjustVO.setReferCurName(btCurrencyDTO.getCurName());
        fbsExcrateAdjustVO.setReferEnglishCode(btCurrencyDTO.getEnglishCode());
    }
}
