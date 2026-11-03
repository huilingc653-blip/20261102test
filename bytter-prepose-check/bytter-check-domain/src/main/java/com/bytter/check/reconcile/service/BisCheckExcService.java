package com.bytter.check.reconcile.service;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.check.reconcile.dao.BisCheckExcDao;
import com.bytter.check.reconcile.entity.BisCheckExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Service
 * @Description: 银企对账指令表Service
 * @author zhenghj
 * @date 2020-10-16
 * @version 1.0
 *
 */
@Service("bisCheckExcService")
@Transactional
public class BisCheckExcService extends BaseService<BisCheckExc, String> {
    @Autowired
    private BisCheckExcDao bisCheckExcDao;
	@Override
	public BaseDao<BisCheckExc, String> getEntityDao() {
		return bisCheckExcDao;
	}
	/**
	 * 批量修改为待发送状态
	 * @param ids 指令表
	 * @return
	 */
	public int batchUpdateByIds(String [] ids, ICurrentUser userVo){
		if (StringUtils.isEmpty(ids)) {
			return 0;
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids);
		params.put("voucherStat", BdlinkConstant.PRE_SEND);
		if(userVo!=null){
			params.put("updateBy", userVo.getLoginName());
		}
		params.put("updateDate", new Date());
		return bisCheckExcDao.batchUpdate(params);
	}
}
