package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexProfitLossManageDtlDao;
import com.bytter.biz.foreign.entity.FexProfitLossManageDtl;
import com.bytter.biz.foreign.vo.FexProfitLossManageDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2025/9/10<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("fexProfitLossManageDtlService")
@Transactional
public class FexProfitLossManageDtlService extends BaseService<FexProfitLossManageDtl, String> {
    @Autowired
    private FexProfitLossManageDtlDao fexProfitLossManageDtlDao;

    @Override
    public BaseDao<FexProfitLossManageDtl, String> getEntityDao() {
        return fexProfitLossManageDtlDao;
    }

    /**
     * 根据交易id获取损益信息明细集合
     * @param reqId
     * @return
     */
    public List<FexProfitLossManageDtlVO> listFexProfitLossManageDtl(String reqId){
        List<FexProfitLossManageDtl> fexProfitLossManageDtls = fexProfitLossManageDtlDao.listFexProfitLossManageDtl(reqId);
        List<FexProfitLossManageDtlVO> fexProfitLossManageDtlList = new ArrayList<>();
        if(!EmptyUtils.isEmpty(fexProfitLossManageDtls)){
            fexProfitLossManageDtlList = BeanUtils.copyListProperties(fexProfitLossManageDtls, FexProfitLossManageDtlVO.class);
        }

        return fexProfitLossManageDtlList;
    }

}
