package com.bytter.ras.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasImportConf;
import com.bytter.ras.vo.RasImportConfVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
/**
 * 
* @Title: 营收稽核 导入配置DAO
* @Package com.bytter.ras.dao   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午3:25:35   
* @version V1.0
 */
@Repository
public class RasImportConfDao  extends BaseDao<RasImportConf,String>{
	
	public List<RasImportConfVO> getRasImportConfVOByFileType(RasImportConfVO rasImportConfVO,String operate){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(" select ");
		sql.append(" ric.id as id,ric.type as type,ric.pay_type as payType,");
		sql.append(" ric.file_Type as fileType,ric.start_line as startLine,ric.is_format as isFormat,ric.date_source as dateSource,");
		sql.append(" ric.txt_format as txtFormat,ric.create_name as createName,ric.create_by as createBy,");
		sql.append(" ric.create_date as createDate,ric.update_Name as updateName,ric.update_By as updateBy,ric.update_Date as updateDate,");
		sql.append(" ricd.FIELD_KEY as fieldKey,ricd.id as ricdId,ricd.start_Val as startVal,ricd.end_Val as endVal");
		sql.append(" from RAS_IMPORT_CONF ric left join RAS_IMPORT_CONF_DTL ricd");
		sql.append("  on ricd.IC_ID = ric.id");
		sql.append(" where 1=1");
		if(!StringUtils.isBlank(rasImportConfVO.getFileType())){
			sql.append(" and ric.file_type=:fileType");
			params.put("fileType",rasImportConfVO.getFileType());
		}
		if(!StringUtils.isBlank(rasImportConfVO.getId())){
			sql.append(" and ric.id=:id");
			params.put("id",rasImportConfVO.getId());
		}
		//营收
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE)){
			sql.append(" and ric.TYPE=0");
		}
		//实收
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED)){
			sql.append(" and ric.TYPE=1");
			if(!StringUtils.isBlank(rasImportConfVO.getDateSource())){
				sql.append(" and ric.date_source=:dateSource");
				params.put("dateSource",rasImportConfVO.getDateSource());
			}
		}
		List<RasImportConfVO> list = this.listBySQLAliasToBean(sql.toString(), params, RasImportConfVO.class);
		return list;
		
	}
	
	
	public List<RasImportConfVO> getRasImportConfVOByoperate(final String fileType,String operate){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(" select ");
		sql.append(" ric.id as id,ric.type as type,ric.pay_type as payType,ric.date_Source as dateSource,");
		sql.append(" ric.file_Type as fileType,ric.start_line as startLine,ric.is_format as isFormat,");
		sql.append(" ric.txt_format as txtFormat,ric.create_name as createName,ric.create_by as createBy,");
		sql.append(" ric.create_date as createDate,ric.update_Name as updateName,ric.update_By as updateBy,ric.update_Date as updateDate");
		sql.append(" from RAS_IMPORT_CONF ric ");
		sql.append(" where 1=1");
		if(!StringUtils.isBlank(fileType)){
			sql.append(" and ric.file_type=:fileType");
			params.put("fileType",fileType);
		}
		//营收
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE)){
			sql.append(" and ric.TYPE=0");
		}
		//实收
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED)){
			sql.append(" and ric.TYPE=1");
		}
		List<RasImportConfVO> list = this.listBySQLAliasToBean(sql.toString(), params, RasImportConfVO.class);
		return list;
	}
	
	/**
     * 根据ids批量修改营收数据明细表数据状态
     * @param params
     * @return
     */
    public int batchUpdate(Map<String,Object> params){
        StringBuilder hql=new StringBuilder("");
        hql.append(" delete RasImportConf where id =:ircId ");
		return this.batchExecute(hql.toString(), params);
    }
}
