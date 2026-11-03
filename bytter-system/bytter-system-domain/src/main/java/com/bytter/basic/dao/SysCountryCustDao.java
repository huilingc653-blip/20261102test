package com.bytter.basic.dao;
import com.bytter.basic.entity.SysCountryCust;
import com.bytter.basic.vo.SysCountryCustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 国家地区表
 * @date 2020-1-19 11:14:22
 */
@Repository
public class SysCountryCustDao extends BaseDao<SysCountryCust, String> {
    /**
     * 查询国家地区表列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,c1.english_code as englishCode,c1.cur_name as curName,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,t.time_zone as timeZone,c.valid_sign as validSign,c.country_level as countryLevel,");
        sql.append(" c.create_by as createBy,c.create_date as createDate,c.update_by as updateBy,c.update_date as updateDate ");
        sql.append(" from sys_country_cust c left join sys_time_zone t on c.time_zone_id=t.id");
        sql.append(" left join bt_currency c1 on c.cur_id=c1.id where 1 = 1 and c.country_level='" + CommonConstant.NO + "'");
        return this.getDynamicBySQL(input, sql.toString(), SysCountryCustVO.class, "t", false);
    }
    /**
     * 根据ID查找国家地区表
     *
     * @param id
     * @return
     */
    public SysCountryCustVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,c1.english_code as englishCode,c1.cur_name as curName,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,t.time_zone as timeZone,c.valid_sign as validSign,c.country_level as countryLevel,");
        sql.append(" c.create_by as createBy,c.create_date as createDate,c.update_by as updateBy,c.update_date as updateDate ");
        sql.append(" from sys_country_cust c left join sys_time_zone t on c.time_zone_id=t.id");
        sql.append(" left join bt_currency c1 on c.cur_id=c1.id");
        sql.append(" where c.id = :id ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, SysCountryCustVO.class);
    }
    /**
     * 根据SYS_ID查找国家地区表
     *
     * @param sysId
     * @return
     */
    public List<SysCountryCustVO> listBySysId(String sysId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,c1.english_code as englishCode,c1.cur_name as curName,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,t.time_zone as timeZone,c.valid_sign as validSign,c.country_level as countryLevel,");
        sql.append(" c.create_by as createBy,c.create_date as createDate,c.update_by as updateBy,c.update_date as updateDate ");
        sql.append(" from sys_country_cust c left join sys_time_zone t on c.time_zone_id=t.id");
        sql.append(" left join bt_currency c1 on c.cur_id=c1.id");
        sql.append(" where c.sys_Id = :sysId ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("sysId", sysId);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCountryCustVO.class);
    }
    /**
     * 根据COUNTRY_LEVEL查找国家地区表
     *
     * @param countryLevel
     * @return
     */
    public List<SysCountryCustVO> listByCountryLevel(String countryLevel) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,c1.english_code as englishCode,c1.cur_name as curName,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,t.time_zone as timeZone,c.valid_sign as validSign,c.country_level as countryLevel,");
        sql.append(" c.create_by as createBy,c.create_date as createDate,c.update_by as updateBy,c.update_date as updateDate ");
        sql.append(" from sys_country_cust c left join sys_time_zone t on c.time_zone_id=t.id");
        sql.append(" left join bt_currency c1 on c.cur_id=c1.id");
        sql.append(" where c.valid_sign = :validSign");
        HashMap<String, String> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(countryLevel)) {
            sql.append(" and c.country_level = :countryLevel");
            params.put("countryLevel", countryLevel);
        }
        params.put("validSign", CommonConstant.YES);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCountryCustVO.class);
    }
    /**
     * 根据SYS_ID查找国家地区表
     *
     * @param sysId
     * @return
     */
    public List<SysCountryCustVO> listByNameZhAndSysId(String nameZh, String sysId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,c1.english_code as englishCode,c1.cur_name as curName,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,t.time_zone as timeZone,c.valid_sign as validSign,c.country_level as countryLevel,");
        sql.append(" c.create_by as createBy,c.create_date as createDate,c.update_by as updateBy,c.update_date as updateDate ");
        sql.append(" from sys_country_cust c left join sys_time_zone t on c.time_zone_id=t.id");
        sql.append(" left join bt_currency c1 on c.cur_id=c1.id");
        sql.append(" where c.sys_Id = :sysId and c.name_Zh=:nameZh");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("sysId", sysId);
        params.put("nameZh", nameZh);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCountryCustVO.class);
    }
    /**
     * 根据SYS_ID查找国家地区表
     *
     * @param nameZh
     * @return
     */
    public List<SysCountryCustVO> getByNameZh(String nameZh, String countryLevel) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,c.valid_sign as validSign,c.country_level as countryLevel");
        sql.append(" from sys_country_cust c");
        sql.append(" where c.country_Level = :countryLevel and c.name_Zh=:nameZh");
        sql.append(" and c.valid_sign = :validSign");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("countryLevel", countryLevel);
        params.put("nameZh", nameZh);
        params.put("validSign", CommonConstant.YES);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCountryCustVO.class);
    }

    /**
     * 根据SYS_ID查找国家地区表
     *
     * @param id
     * @return
     */
    public List<SysCountryCustVO> listById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id,c.sys_id as sysId,c.name_zh as nameZh,c.name_en as nameEn,c.cur_id as curId,c1.english_code as englishCode,c1.cur_name as curName,");
        sql.append(" c.en_code as enCode,c.code as code,c.time_zone_id as timeZoneId,t.time_zone as timeZone,c.valid_sign as validSign,c.country_level as countryLevel,");
        sql.append(" c.create_by as createBy,c.create_date as createDate,c.update_by as updateBy,c.update_date as updateDate ");
        sql.append(" from sys_country_cust c left join sys_time_zone t on c.time_zone_id=t.id");
        sql.append(" left join bt_currency c1 on c.cur_id=c1.id");
        sql.append(" where c.id = :id ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("id", id);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCountryCustVO.class);
    }
}
