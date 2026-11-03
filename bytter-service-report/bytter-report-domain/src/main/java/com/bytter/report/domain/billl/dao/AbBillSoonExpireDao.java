package com.bytter.report.domain.billl.dao;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.report.domain.billl.vo.AbBillSoonExpireDetailedVO;
import com.bytter.report.domain.billl.vo.AbBillSoonExpireVO;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: dao
 * @Description: 即将到期票据分析
 * @author zhenghj
 * @date 2020.2.10
 * @version 1.0
 *
 */
@Repository
public class AbBillSoonExpireDao extends BaseDao<AbBillSoonExpireVO,String> {
    /**
     * 查询即将到期票据信息
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillSoonExpireVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, List<BiRemainTermDTO> biRemainTermDTOs) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.corpId,s.name as corpName,f.bankMoneyTotal,f.businessMoneyTotal,f.total,f.termId  from (");
        for(int i = 0; i < biRemainTermDTOs.size(); i++){
            BiRemainTermDTO biRemainTermDTO = biRemainTermDTOs.get(i);
            if(i != 0){
                sql.append( " union all ");
            }
            sql.append("select b.corp_id as corpId,");
            sql.append("sum(case when  a.Bill_Type='10' then a.bill_money else 0 end) as bankMoneyTotal,");
            sql.append("sum(case when  a.Bill_Type='11' then a.bill_money else 0 end) as businessMoneyTotal,");
            sql.append("bt_nvl_num(sum(a.bill_money),0) as total,");
            sql.append(" '" + biRemainTermDTO.getId() + "' as termId");
            sql.append(" from ab_voucherinfo_vice b ");
            sql.append(" inner join ab_voucherinfo a on a.id = b.vid ");
            sql.append(" inner join ab_accrecords l on l.vvid=b.id  ");
            sql.append(" left join sys_corp s on b.corp_id = s.id  ");
            sql.append(" where b.valid_sign=1  ");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())){
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
            //判断应付应收
            String type = String.valueOf(dataTablesInput.getFormdata().get("type"));
            if(StringUtils.isEmpty(type) || "null".equals(type)){
                type = AccebillConstant.AB_VOUCHERINFO_OP_TYPE0;
            }
            //应付
            if(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(type)){
                sql.append(" and b.type="+AccebillConstant.AB_VOUCHERINFO_OP_TYPE0+" and  ((((a.bill_flag= 11 and l.send_sign=0) or a.bill_flag= 10) ")
                   .append(" and l.valid_sign=1 and l.status=95  and l.opt_status=19 and l.brief_id in (001,6101)) ")
                   .append(" or (a.bill_flag= 11 and l.send_sign=1 and l.valid_sign=1 and l.status=95  and l.opt_status=19 and l.brief_id in (9003)) )");
            }
            //应收
            else if(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(type)){
                sql.append(" and b.type="+AccebillConstant.AB_VOUCHERINFO_OP_TYPE1+" and ((l.valid_sign=1 and l.status=95  and l.opt_status=19  and l.brief_id in (9003,9010,9012,9018,9019,6019,6001,6212) ) ")
                    .append(" OR (a.bill_flag= 11 and l.send_sign=1 and l.brief_id in (010,011,018,020) and l.status=95 and l.opt_status in (16,17,18,-1)) )");
            }
            //判断流通介质电票纸票
            if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("billFlagName"))){
                sql.append(" and  a.bill_flag="+dataTablesInput.getFormdata().get("billFlagName") +" ");
            }
            //判断票据类型
            if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("billTypeName"))){
            	sql.append(" and  a.bill_Type="+dataTablesInput.getFormdata().get("billTypeName") +" ");
            }
            sql.append(joinSql);
            sql.append(" group by b.corp_id");
        }
        sql.append(") f left join sys_corp s on f.corpId=s.id where 1=1 ");
        //left join sys_corp s on f.corpId=s.id
        return getDynamicBySQL(dataTablesInput,sql.toString(), AbBillSoonExpireVO.class,"",false);
    }
    /**
     * 即将到期票据分析明细数据
     *
     * @param dataTablesInput 查询条件
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillSoonExpireDetailedVO> listSoonExpireDetailed(DataTablesInput dataTablesInput,String searchSql) {
        Map<String,Object> params=new HashMap<>();
        StringBuilder sql = new StringBuilder("select a.orig_no as origNo,b.bank_acc as bankAcc,banktype.type_name as bankType,")
                .append("a.bill_type as billType,a.bill_flag as billFlag,a.start_date as startDate, a.end_date as endDate," )
                .append("a.bill_money as billMoney,a.payer,a.payee,a.acceptor ");
        sql.append(" from ab_voucherinfo_vice b ")
                .append(" left join ab_voucherinfo a on a.id = b.vid " )
                .append(" left join ab_accrecords l on l.vvid=b.id ");
                sql.append(" left join bt_bank_acc acc on b.bank_acc=acc.bank_acc ")
                .append(" left join  bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" where  b.valid_sign=1 ");
        //判断应付应收
        if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqi_type"))){
            String type = String.valueOf(dataTablesInput.getFormdata().get("eqi_type"));
            //应付
            if(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(type)){
                sql.append(" and b.type="+AccebillConstant.AB_VOUCHERINFO_OP_TYPE0+" and  ((((a.bill_flag= 11 and l.send_sign=0) or a.bill_flag= 10) ")
                        .append(" and l.valid_sign=1 and l.status=95  and l.opt_status=19 and l.brief_id in (001,6101)) ")
                        .append(" or (a.bill_flag= 11 and l.send_sign=1 and l.valid_sign=1 and l.status=95  and l.opt_status=19 and l.brief_id in (9003)) )");
            }
            //应收
            else if(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(type)){
                sql.append(" and b.type="+AccebillConstant.AB_VOUCHERINFO_OP_TYPE1+" and  ((l.valid_sign=1 and l.status=95  and l.opt_status=19  and l.brief_id in (9003,9010,9012,9018,9019,6019,6001,6212) ) ")
                        .append(" OR (a.bill_flag= 11 and l.send_sign=1 and l.brief_id in (010,011,018,020) and l.status=95 and l.opt_status in (16,17,18,-1)) )");
            }
        }
        if (ObjectUtil.isNotEmpty(searchSql)){
            sql.append(" and "+searchSql);
        }
        //判断流通介质电票纸票
        /*
        if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("billFlagName"))){
            sql.append(" and  a.bill_flag="+dataTablesInput.getFormdata().get("billFlagName") +" ");
        }*/
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillSoonExpireDetailedVO.class,"",false,params);
    }
}
