package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondInterest;
import com.bytter.finance.domain.bond.vo.CmsBondInterestVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Dao
 * @Description: 债券利息预计明细表
 * @date 2022-06-08 10:07:46
 */
@Repository
public class CmsBondInterestDao extends BaseDao<CmsBondInterest, String> {
    public List<CmsBondInterestVo> getByRegisterId(String registerId) {
        if (!StringUtils.isNotEmpty(registerId)) {
            return new ArrayList<>();
        }
        return new ArrayList<CmsBondInterestVo>();
    }
}
