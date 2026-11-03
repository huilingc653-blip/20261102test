package com.bytter.basic.service;
import com.bytter.basic.dao.BtCurrencyPairDao;
import com.bytter.basic.entity.BtCurrencyPair;
import com.bytter.basic.vo.BtCurrencyPairVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 货币对管理
 * @date 2020-1-19 11:14:22
 */
@Service("btCurrencyPairServiceBase")
@Transactional
public class BtCurrencyPairService extends BaseService<BtCurrencyPair, String> {
    @Autowired
    private BtCurrencyPairDao btCurrencyPairDao;
    @Override
    public BaseDao<BtCurrencyPair, String> getEntityDao() {
        return btCurrencyPairDao;
    }
    /**
     * 查询货币对列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        return this.btCurrencyPairDao.getDataTablesUnion(input);
    }
    /**
     * 根据ID查找货币对
     *
     * @param id
     * @return
     */
    public BtCurrencyPairVO getByUnion(String id) {
        return this.btCurrencyPairDao.getByUnion(id);
    }
    /**
     * 保存货币对
     *
     * @param entity
     */
    public void saveBtCurrencyPair(BtCurrencyPair entity, String userLoginName) {
        entity.setCreateBy(userLoginName);
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
    }
    /**
     * 修改货币对
     *
     * @param entity
     */
    public void updateBtCurrencyPair(BtCurrencyPair entity, String userLoginName) {
        entity.setUpdateBy(userLoginName);
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }
    /**
     * 货币对配对
     *
     * @param params
     * @return
     */
    public BtCurrencyPairVO getByCurId(Map<String, Object> params) {
        return btCurrencyPairDao.getByCurId(params);
    }
    /**
     * 数据重复校验
     *
     * @param btCurrencyPair
     */
    public void checkData(BtCurrencyPair btCurrencyPair) {
        //校验货币对是否存在
        Map<String, Object> map = new HashMap<>();
        map.put("curId1", btCurrencyPair.getCurId1());
        map.put("curId2", btCurrencyPair.getCurId2());
        BtCurrencyPairVO checkEntity = this.getByCurId(map);
        if (checkEntity != null) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_PAIRS_ALREADY_EXIST);
        }
    }
    /**
     * 货币对编辑重复校验
     */
    public void checkEqualData(BtCurrencyPair source, BtCurrencyPair target) {
        boolean check = false;
        List<String> list = new ArrayList<>();
        list.add(target.getCurId1());
        list.add(target.getCurId2());
        if (list.contains(source.getCurId1())) {
            if (list.contains(source.getCurId2())) {
                check = true;
            }
        }
        if (!check) {
            this.checkData(target);
        }
    }


    public BtCurrencyPairVO getBtCurrencyPairByCurId(String curId1, String curId2) {
        Map<String, Object> params = new HashMap<>();
        params.put("curId1",curId1);
        params.put("curId2",curId2);
        BtCurrencyPairVO btCurrencyPairVO = btCurrencyPairDao.getByCurId(params);
        return btCurrencyPairVO;
    }

    /**
     *
     * 查询所有有效货币对
     *
     * @return
     */
    public List<BtCurrencyPairVO> getCurrencyPairjson() {
        List<BtCurrencyPairVO> currencyPairVOS = btCurrencyPairDao.listCurrencyPair();
        if(!EmptyUtils.isEmpty(currencyPairVOS)){
            for (BtCurrencyPairVO vo : currencyPairVOS) {
                if(vo.getCurId1().equals(vo.getBaseCurId())){
                    vo.setPairId(vo.getCurId1()+"_"+vo.getCurId2());
                    vo.setPairCode(vo.getEnglishCode1()+"/"+vo.getEnglishCode2());
                }else{
                    vo.setPairId(vo.getCurId2()+"_"+vo.getCurId1());
                    vo.setPairCode(vo.getEnglishCode2()+"/"+vo.getEnglishCode1());
                }
            }
        }
        return currencyPairVOS;
    }

    public BtCurrencyPairVO getById(String id) {
        BtCurrencyPair btCurrencyPair = this.get(id);
        if(null != btCurrencyPair){
            BtCurrencyPairVO btCurrencyPairVO = new BtCurrencyPairVO();
            BeanUtils.copyNotNullProperties(btCurrencyPair, btCurrencyPairVO);
            return btCurrencyPairVO;
        }else{
            return null;
        }
    }



    /**
     * 获取有效的货币对
     * @return
     */
    public List<BtCurrencyPairVO> getValidCurrencyPair(){
        List<BtCurrencyPairVO> currencyPairList = btCurrencyPairDao.getValidCurrencyPair();
        List<BtCurrencyPairVO> result = new ArrayList<>();
        for (BtCurrencyPairVO btCurrencyPairVO : currencyPairList) {
            BtCurrencyPairVO vo = new BtCurrencyPairVO();
            BeanUtils.copyNotNullProperties(btCurrencyPairVO,vo);
            if(btCurrencyPairVO.getCurId1().equals(btCurrencyPairVO.getBaseCurId())){
                vo.setCurrencyPair(btCurrencyPairVO.getEnglishCode1() + "/" + btCurrencyPairVO.getEnglishCode2());
                vo.setCurId1(btCurrencyPairVO.getCurId1());
                vo.setCurId2(btCurrencyPairVO.getCurId2());
                vo.setEnglishCode1(btCurrencyPairVO.getEnglishCode1());
                vo.setEnglishCode2(btCurrencyPairVO.getEnglishCode2());
            }else{
                vo.setCurrencyPair(btCurrencyPairVO.getEnglishCode2() + "/" + btCurrencyPairVO.getEnglishCode1());
                vo.setCurId1(btCurrencyPairVO.getCurId2());
                vo.setCurId2(btCurrencyPairVO.getCurId1());
                vo.setEnglishCode1(btCurrencyPairVO.getEnglishCode2());
                vo.setEnglishCode2(btCurrencyPairVO.getEnglishCode1());
            }
            result.add(vo);
        }
        return result;
    }
}
