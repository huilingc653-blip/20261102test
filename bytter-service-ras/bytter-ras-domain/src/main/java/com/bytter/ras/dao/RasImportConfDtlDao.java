package com.bytter.ras.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasImportConfDtl;
import com.bytter.core.base.BaseDao;

/**
 * 
* @Title:   营收稽核 导入配置详细DAO  
* @Package com.bytter.ras.dao   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午3:40:48   
* @version V1.0
 */
@Repository
public class RasImportConfDtlDao extends BaseDao<RasImportConfDtl,String> {

	/**
     * 根据ids批量修改营收数据明细表数据状态
     * @param params
     * @return
     */
    public int batchUpdate(Map<String,Object> params){
        StringBuilder hql=new StringBuilder("");
        hql.append(" delete RasImportConfDtl where icId =:ircId ");
		return this.batchExecute(hql.toString(), params);
    }
}
