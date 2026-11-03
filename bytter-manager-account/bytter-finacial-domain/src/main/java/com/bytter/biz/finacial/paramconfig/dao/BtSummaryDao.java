package com.bytter.biz.finacial.paramconfig.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.paramconfig.entity.BtSummary;
import com.bytter.biz.finacial.paramconfig.vo.BtSummaryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
/**   
 * @Title: DAO  
 * @Description: BT_SUMMARY摘要词组维护
 * @author Lixi
 * @date 2017-05-16 15:36:55
 * @version V1.0   
 *
 */
@Repository
public class BtSummaryDao extends BaseDao<BtSummary,String>{
	
	/**
	 * 加载数据
	 * @return
	 */
	public DataTablesOutput dataTable(DataTablesInput input){
		StringBuilder sql = new StringBuilder();
		sql.append("select id as id,summary_content as summaryContent,rmk as rmk,valid_sign as validSign,usingtype  from Bt_Summary where 1 = 1 ");
		return getDynamicBySQL(input, sql.toString(), BtSummaryVo.class,"",false);
	}
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public List<BtSummary> listById(String id){
		Map<String, Object> params = new HashMap<String, Object>(); 	
		String hql = "from BtSummary where id=:id order by createTime desc ";
		params.put("id",id);
		return createQuery(hql,params).list();
	}
	/**
	 * <b>功能介绍：</b>根据内容查询<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param content
	 * @return
	 */
	public List<BtSummary> listByContent(String content) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from BtSummary where summaryContent = :summaryContent and validSign = :validSign order by createTime desc ";
		params.put("summaryContent", content);
		params.put("validSign", FinacialConstant.VALID_TRUE);
		return createQuery(hql, params).list();
	}
	/**
     * <b>功能：</b>获取摘要代码的最大值<br>
     * @return
     */
	public String getMaxSummaryCode(){
        String hql ="select max(summaryCode) from BtSummary";
        return (String)getSession().createQuery(hql).uniqueResult();
    }
	 /**
     * 获取有效的摘要词组
     * @param
     */
    public List<BtSummary> getListByValidSign(){
        return listBy("validSign","1");
    }
	
}
