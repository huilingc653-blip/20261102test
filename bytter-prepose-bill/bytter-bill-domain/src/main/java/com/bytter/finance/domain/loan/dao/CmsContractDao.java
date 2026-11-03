package com.bytter.finance.domain.loan.dao;
import com.bytter.finance.domain.loan.entity.CmsContract;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * @Title: Dao
 * @Description: CMS_CONTRACT
 * @author onlineGenerator
 * @date 2017-08-02 10:07:46
 * @version 1.0
 *
 */
@Repository
public class CmsContractDao extends BaseDao<CmsContract, String> {
    /**
     * <b>功能介绍：</b>根据licode查询<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public CmsContract findCmsContractByliCode(String liCode) {
        return getUniqueBy("liCode", liCode);
    }
}
