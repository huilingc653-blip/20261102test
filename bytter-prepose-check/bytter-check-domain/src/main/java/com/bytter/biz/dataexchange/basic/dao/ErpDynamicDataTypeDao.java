package com.bytter.biz.dataexchange.basic.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpDynamicDataType;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
* @Title: Dao
* @Description: 自定义数据类型
 * @author zhengg
 * @date 2018-04-19 11:24:27
* @version 1.0
*
*/
@Repository
public class ErpDynamicDataTypeDao extends BaseDao<ErpDynamicDataType,String> {
	
	public List<ErpDynamicDataType> listByCode(String code) {
		List<ErpDynamicDataType> list = listBy("code",code);
		return list;
	}
	/**
	 * 获取所有自定义数据类型
	 * @return 返回数据集合
	 */
    public List<ErpDynamicDataType> getAllDynamicDataType() {
    	String hql="from ErpDynamicDataType order by sort asc ";
        List<ErpDynamicDataType> list = listByHQL(hql);
        return list;
    }
    
}
