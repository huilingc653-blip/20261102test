package com.bytter.bill.domain.billbank.pool.dao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolQueryExc;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.core.utils.BeanUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>池查询指令DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-11-18 <br>
 */
@Repository("billPoolQueryExcImpDao")
public class BillPoolQueryExcImpDao extends BaseDao<AbPoolQueryExc,String> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 获取默认第一个的顶级单位
	 * @return
	 */
	public PoolBaseVO getTopLevelCorp(){
		StringBuilder sql=new StringBuilder();
		sql.append("select corp_id as corpId From ab_pool_corp ");
		sql.append("where parent_id is null and valid_sign ='1' and bustype_level=1 ");
		sql.append("order by create_time ");
		List<Map<String, Object>> list=this.listBySQL(sql.toString(), null);
		if(list!=null&&list.size()>0){
			return BeanUtils.copyMapProperties(list.get(0), PoolBaseVO.class);
		}
		return null;
	}
	
	
	
}
