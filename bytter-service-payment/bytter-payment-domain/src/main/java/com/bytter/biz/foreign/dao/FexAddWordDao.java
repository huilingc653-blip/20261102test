package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexAddWord;
import com.bytter.biz.foreign.vo.FexAddWordVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @Title: Dao
 * @Description: 交易附言表
 * @author huangx
 * @date 2022-10-31 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexAddWordDao extends BaseDao<FexAddWord,String> {

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.tran_Code as tranCode,t.tran_Name as tranName,f.add_Word as addWord,f.cd_Sign as cdSign,f.valid_Sign as validSign");
        sql.append(" from Fex_Add_Word f left join Fex_Tran_Info t on f.tran_Code=t.tran_Code where 1=1");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexAddWordVO.class, "f", false);
    }

    /**
     * 根据ID获取交易附言
     *
     * @param id
     * @return
     */
    public FexAddWordVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.tran_Code as tranCode,t.tran_Name as tranName,f.add_Word as addWord,f.cd_Sign as cdSign,f.valid_Sign as validSign");
        sql.append(" from Fex_Add_Word f left join Fex_Tran_Info t on f.tran_Code=t.tran_Code");
        sql.append(" where f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexAddWordVO.class);
    }

    /**
     * 根据交易编码和收支方向获取交易附言
     *
     * @param tranCode
     * @param cdSign
     * @return
     */
    public FexAddWordVO getByTranCodeAndCdSign(String tranCode, String cdSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.tran_Code as tranCode,f.add_Word as addWord,f.cd_Sign as cdSign,f.valid_Sign as validSign");
        sql.append(" from Fex_Add_Word f where f.tran_Code=:tranCode and f.cd_Sign = :cdSign");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tranCode", tranCode);
        params.put("cdSign", cdSign);
        return getBySQLAliasToBean(sql.toString(), params, FexAddWordVO.class);
    }
}
