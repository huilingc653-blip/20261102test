package com.bytter.biz.finacial.interest.service;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.interest.dao.FcEntrustValidationDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
 * <br>
 * <b>类功能：</b>委托贷款利息核销service类<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年8月26日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
@Transactional
@Service("ist_FcEntrustValidationService")
public class FcEntrustValidationService extends BaseService<FcInterestDtl, String> implements Serializable {
	
	@Autowired
	private FcEntrustValidationDao entrustValidDao;
	
	public BaseDao<FcInterestDtl, String> getEntityDao() {
		return entrustValidDao;
	}
	
	/**
	 * <b>功能介绍：</b>批量删除<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param list
	 */
	public void deleteListBatch(List list) {
        this.entrustValidDao.deleteListBatch(list);
    }
	
	/**
	 * <b>功能介绍：</b>批量更新数据<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param list
	 */
	public void updateListBatch(List list) {
        this.entrustValidDao.updateListBatch(list);
    }
}
