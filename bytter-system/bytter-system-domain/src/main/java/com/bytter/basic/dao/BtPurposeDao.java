package com.bytter.basic.dao;
import com.bytter.basic.entity.BtPurpose;
import com.bytter.basic.vo.BtPurposeVO;
import com.bytter.system.api.constant.BtPurposeConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 用途维护表
 * @date 2017-05-24 09:57:15
 */
@Repository
public class BtPurposeDao extends BaseDao<BtPurpose, String> {
    /**
     * 根据条件查询
     *
     * @param corpId     单位ID
     * @param purpose    用途
     * @param sysBusType 用途类型
     * @return
     */
    public List<BtPurposeVO> listBy(String corpId, String purpose, String sysBusType) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id,p.purpose,p.sys_bus_type,p.create_by,p.create_date,p.update_by,p.update_date ");
        sql.append(" from bt_purpose p ");
        if (!EmptyUtils.isBlank(corpId)) {
            sql.append(" left join bt_purpose_corp pc on p.id = pc.purpose_id ");
            sql.append(" where ");
            String[] corpIdArray = corpId.split(CommonConstant.COMMA_SPLIT);
            List<String> corpIdList = Arrays.asList(corpIdArray);
            sql.append(getSQLIn(corpIdList, 900, "pc.corp_Id"));
        } else {
            sql.append(" where 1 = 1 ");
        }
        if (!EmptyUtils.isBlank(purpose)) {
            sql.append(" and p.purpose = :purpose ");
            params.put("purpose", purpose);
        }
        if (!EmptyUtils.isBlank(sysBusType)) {
            sql.append(" and p.sys_bus_type like :sysBusType ");
            params.put("sysBusType", "%" + sysBusType + "%");
        }
        sql.append("order by p.sys_bus_type");
        return listBySQLAliasToBean(sql.toString(), params, BtPurposeVO.class);
    }
    /**
     * 查询用途信息列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BtPurposeVO> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id,p.purpose,p.sys_bus_type,p.create_by,p.create_date,p.update_by,p.update_date ");
        sql.append(" from bt_purpose p  ");
        if (!EmptyUtils.isEmpty(input.getFormdata().get("likes_corpId"))) {
            String corpId = input.getFormdata().get("likes_corpId").toString();
            if (!EmptyUtils.isBlank(corpId)) {
                sql.append(" left join bt_purpose_corp pc on p.id = pc.purpose_id ");
            }
        }
        return getDynamicBySQL(input, sql.toString(), BtPurposeVO.class, "", true);
    }
    /**
     * 根据id查找用途信息
     *
     * @param id
     * @return
     */
    public BtPurposeVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id,p.purpose,p.sys_bus_type,p.create_by,p.create_date,p.update_by,p.update_date ");
        sql.append(" from bt_purpose p where p.id = :id ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BtPurposeVO.class);
    }
    /**
     * 根据业务详情获取对应业务表
     *
     * @return
     */
    public String getTableNameByBusDtl(String busDtl) {
        if (BtPurposeConstant.BUS_DTL_PAY.equals(busDtl)) {
            return "nis_billhead";
        } else if (BtPurposeConstant.BUS_DTL_ALLOT.equals(busDtl)) {
            return "nis_allocation_req";
        }
        return "";
    }
    /**
     * 根据业务详情获取对应业务表
     *
     * @return
     */
    public String getFieldNameByBusDtl(String busDtl) {
        if (BtPurposeConstant.BUS_DTL_PAY.equals(busDtl)) {
            return "purpose";
        } else if (BtPurposeConstant.BUS_DTL_ALLOT.equals(busDtl)) {
            return "purpose";
        }
        return "purpose";
    }
    /**
     * 根据条件查询
     *
     * @param corpId     单位ID
     * @param sysBusType 用途
     * @param busDtl     业务详情
     * @return
     */
    public List<BtPurposeVO> listBySort(String corpId, String sysBusType, String busDtl) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id,p.purpose,p.sys_bus_type ");
        if (StringUtils.isNotBlank(busDtl)) {
            String filedName = this.getFieldNameByBusDtl(busDtl);
            sql.append(",(select count(" + filedName + ") from " + this.getTableNameByBusDtl(busDtl) + " t where t." + filedName + "=p.purpose) as useCount");
        }
        sql.append(" from bt_purpose p ");
        if (!EmptyUtils.isBlank(corpId)) {
            sql.append(" left join bt_purpose_corp pc on p.id = pc.purpose_id ");
            sql.append(" where ");
            String[] corpIdArray = corpId.split(CommonConstant.COMMA_SPLIT);
            List<String> corpIdList = Arrays.asList(corpIdArray);
            sql.append(getSQLIn(corpIdList, 900, "pc.corp_Id"));
        } else {
            sql.append(" where 1 = 1 ");
        }
        if (!EmptyUtils.isBlank(sysBusType)) {
            sql.append(" and p.sys_bus_type like :sysBusType ");
            params.put("sysBusType", "%" + sysBusType + "%");
        }
        if (StringUtils.isNotBlank(busDtl)) {
            sql.append("order by useCount desc");
        } else {
            sql.append("order by p.purpose desc");
        }
        return listBySQLAliasToBean(sql.toString(), params, BtPurposeVO.class);
    }
    /**
     * 根据条件查询
     *
     * @param corpId     单位ID
     * @param sysBusType 用途类别
     * @param curId      币别
     * @return
     */
    public List<BtPurposeVO> listBy(String corpId, String sysBusType, String curId, String countryId, String bankTypeId) {
        List<BtPurposeVO> list = new ArrayList<>();
        if (StringUtils.isBlank(corpId) || StringUtils.isBlank(curId) || StringUtils.isBlank(bankTypeId)) {
            return list;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct p.id,p.purpose,p.sys_bus_type ");
        sql.append(" from bt_purpose p ");
        if (!EmptyUtils.isBlank(corpId)) {
            sql.append(" left join bt_purpose_corp pc on p.id = pc.purpose_id ");
        }
        if (!EmptyUtils.isBlank(curId)) {
            sql.append(" left join bt_purpose_cur cur on p.id = cur.purpose_id ");
        }
        if (!EmptyUtils.isBlank(countryId)) {
            sql.append(" left join bt_purpose_country country on p.id = country.purpose_id ");
        }
        if (!EmptyUtils.isBlank(bankTypeId)) {
            sql.append(" left join bt_purpose_bank_type type on p.id = type.purpose_id ");
        }
        sql.append(" where 1 = 1 ");
        if (!EmptyUtils.isBlank(corpId)) {
            String[] corpIdArray = corpId.split(CommonConstant.COMMA_SPLIT);
            List<String> corpIdList = Arrays.asList(corpIdArray);
            sql.append(" and ").append(getSQLIn(corpIdList, 900, "pc.corp_Id"));
        }
        if (!EmptyUtils.isBlank(curId)) {
            String[] curIdArray = curId.split(CommonConstant.COMMA_SPLIT);
            List<String> curIdList = Arrays.asList(curIdArray);
            sql.append(" and (").append(getSQLIn(curIdList, 900, "cur.cur_Id")).append(" or cur.cur_Id is null)");
        }
        if (!EmptyUtils.isBlank(countryId)) {
            String[] countryIdArray = countryId.split(CommonConstant.COMMA_SPLIT);
            List<String> countryIdList = Arrays.asList(countryIdArray);
            sql.append(" and (").append(getSQLIn(countryIdList, 900, "country.country_Id")).append(" or country.country_Id is null)");
        }
        if (!EmptyUtils.isBlank(bankTypeId)) {
            String[] bankTypeIdArray = bankTypeId.split(CommonConstant.COMMA_SPLIT);
            List<String> bankTypeIdList = Arrays.asList(bankTypeIdArray);
            sql.append(" and (").append(getSQLIn(bankTypeIdList, 900, "type.bank_Type_Id")).append(" or type.bank_Type_Id is null)");
        }
        if (!EmptyUtils.isBlank(sysBusType)) {
            sql.append(" and p.sys_bus_type like :sysBusType ");
            params.put("sysBusType", "%" + sysBusType + "%");
        }
        sql.append("order by p.sys_bus_type");
        return listBySQLAliasToBean(sql.toString(), params, BtPurposeVO.class);
    }
}
