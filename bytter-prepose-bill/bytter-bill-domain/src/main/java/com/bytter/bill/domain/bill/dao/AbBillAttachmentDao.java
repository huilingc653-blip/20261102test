package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillAttachment;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据附件操作
* @author onlineGenerator
* @date 2017-10-24 14:33:51
* @version 1.0
*
*/
@Repository
public class AbBillAttachmentDao extends BaseDao<AbBillAttachment,String>{
	
	/**
	 * 根据查询参数查询附件实体信息
	 * @param params 查询参数
	 * @return
	 */
	public AbBillAttachment getAbBillAttachmentByParams(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from AbBillAttachment where 1=1 ");
		if(params.containsKey("attachmentId")){
			hql.append(" and attachmentId=:attachmentId ");
		}
		if(params.containsKey("vvid")){
			hql.append(" and vvid=:vvid ");
		}
		if(params.containsKey("briefId")){
			hql.append(" and briefId =:briefId ");
		}
		if(params.containsKey("briefIds")){
			hql.append(" and briefId in(:briefIds) ");
		}
		if(params.containsKey("acids")){
			hql.append(" and acid in(:acids) ");
		}
		List<AbBillAttachment> listInfo = listByHQL(hql.toString(), params);
		if(listInfo!=null&&!listInfo.isEmpty()){
			return listInfo.get(0);
		}
		return null;
	}
}
