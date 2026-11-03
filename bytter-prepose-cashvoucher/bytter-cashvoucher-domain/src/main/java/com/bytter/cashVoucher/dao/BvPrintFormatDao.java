package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvPrintFormat;
import com.bytter.cashVoucher.vo.BvPrintFormatVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证打印格式
* @author onlineGenerator
* @date 2017-05-31 15:25:10
* @version 1.0
*
*/
@Repository
public class BvPrintFormatDao extends BaseDao<BvPrintFormat,String>{
	
	
	/**
	 * 展示套打配置页面列表查询信息
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listBvPrintFormatInfo(DataTablesInput input) {
		StringBuilder sql=new StringBuilder("select VT.id as vouTid,");
		sql.append(" P.ID,VT.NAME as voucherType,P.FMT_NO as FMTNO,P.FMT_NAME as FMTNAME,P.PAPER_SIZE as PAPERSIZE,P.VALID_SIGN as VALIDSIGN,P.LEFT_BORDER as LEFTBORDER,P.UP_BORDER as UPBORDER,P.SCENE_ID as SCENEID,VS.NAME as SCENENAME ");
		sql.append(" from BV_PRINT_FORMAT p ");
		sql.append(" LEFT JOIN BV_VOUCHER_TYPE VT ON VT.ID=P.VOU_TID ");
		sql.append(" LEFT JOIN BV_VOUTYPE_SCENE VS ON P.SCENE_ID = VS.ID ");
		sql.append(" WHERE P.VALID_SIGN<>'2' ");

		DataTablesOutput dataOutInfo=this.getDynamicBySQL(input, sql.toString(), BvPrintFormatVo.class, "P", false);
		return dataOutInfo;
	}
	
	/**
	 * 根据ID查询查询凭证打印格式主信息
	 * @param id
	 * @return
	 */
	public List<BvPrintFormatVo> listBvPrintFormatVoById(String id) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" P.ID,VT.NAME as voucherType,VT.id as vouTid,P.FMT_NO as FMTNO,P.FMT_NAME as FMTNAME,P.PAPER_SIZE as PAPERSIZE,P.VALID_SIGN as VALIDSIGN,P.LEFT_BORDER as LEFTBORDER,P.UP_BORDER as UPBORDER,P.BACKGROUND_IMAGE as backgroundImage,P.SCENE_ID as SCENEID ");
		sql.append(" from BV_PRINT_FORMAT p ");
		sql.append(" LEFT JOIN BV_VOUCHER_TYPE VT ON VT.ID=P.VOU_TID ");
		sql.append(" WHERE P.ID=:id ");
		Map<String, Object> params=new HashMap<>();
		params.put("id", id);
		List<BvPrintFormatVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, BvPrintFormatVo.class);
		return listInfo;
	}
	
	/**
	 * 根据IDS批量删除数据 逻辑删除设置
	 * @param params
	 * @param user
	 * @return
	 */
	public int batchDeleteBvPrintFormat(Map<String,Object> params,ICurrentUser user){
		String hql="update BvPrintFormat set validSign=:validSign"
				+ ",updateBy=:updateBy "
				+ ",updateName=:updateName "
				+ ",updateDate=:updateDate "
				+ "where id in(:ids)";
		params.put("validSign", "2");
		params.put("updateName", user.getUsername());
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", new Date());
		int num=this.batchExecute(hql, params);
		return num;
	}
	
	/**
	 * 打印格式编号是否重复
	 * @param opType 操作类型
	 * @param fmtNo 套打编号
	 * @param id
	 * @return
	 */
	public int checkFmtNo(String opType,String fmtNo,String id){
		Map<String, Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select id From BV_PRINT_FORMAT where VALID_SIGN<>:validSign");
		params.put("validSign", "2");
		if("update".equals(opType)&& StringUtils.isNotBlank(id)){
			sql.append(" and id<>:id");
			params.put("id", id);
		}
		if(StringUtils.isNotBlank(fmtNo)){
			sql.append(" and fmt_no=:fmtNo");
			params.put("fmtNo", fmtNo);
		}
		@SuppressWarnings("rawtypes")
		List list=this.listBySQL(sql.toString(), params);
		if(list!=null){
			return list.size();
		}
		return 0;
	}
	
	/**
	 * 根据ID查询查询凭证打印格式信息
	 * @param vouTid
	 * @return
	 */
	public List<BvPrintFormatVo> getPrintFormatByVouTid(String vouTid, String sceneId) {
		Map<String, Object> params=new HashMap<>();
		params.put("vouTid", vouTid);
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" P.ID,P.FMT_NO as FMTNO,P.vou_tid as vouTid,P.FMT_NAME as FMTNAME,P.PAPER_SIZE as PAPERSIZE,P.LEFT_BORDER as LEFTBORDER,P.UP_BORDER as UPBORDER,P.BACKGROUND_IMAGE as backgroundImage  ");
		sql.append(" from BV_PRINT_FORMAT p ");
		sql.append(" WHERE P.vou_tid=:vouTid ");
		sql.append(" and P.valid_sign='1' ");
		if(StringUtils.isEmpty(sceneId)){
			sql.append(" and P.scene_Id is null ");
		}else{
			sql.append(" and (P.scene_Id is null or P.scene_Id=:sceneId) ");
			params.put("sceneId", sceneId);
		}
		sql.append(" order by p.fmt_no desc ");
		List<BvPrintFormatVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, BvPrintFormatVo.class);
		return listInfo;
	}
}
