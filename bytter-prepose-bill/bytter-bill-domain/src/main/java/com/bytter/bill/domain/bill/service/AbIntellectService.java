package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbIntellectDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbIntellectElement;
import com.bytter.bill.domain.bill.vo.AbIntellectElementVO;
import com.bytter.bill.domain.bill.vo.AbIntellectGroupVO;
import com.bytter.bill.domain.bill.vo.AbIntellectVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 智能选票
 * @date 2022-10-12
 */
@Service("abIntellectService")
@Transactional
public class AbIntellectService extends BaseService<AbIntellectVO, String> {
    @Autowired
    private AbVoucherinfoViceDao abVoucherinfoViceDao;
    @Autowired
    private AbIntellectDao abIntellectDao;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Autowired
    private AbIntellectElementService abIntellectElementService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 列表显示票据信息
     *
     * @param input 查询条件入参
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<AbVoucherinfoViceVO> listAccebillInfoDataTable(DataTablesInput input) {
        //查询条件Map
        Map<String, Object> whereVOMap= new HashMap<>();
        //筛选数量
        int screenNum = 0;
        //智能选票规则组装
        screenNum = setIntellect(input, whereVOMap, screenNum);



        DataTablesOutput<AbVoucherinfoViceVO> output;
        List<String> corpAuthList=null;
        if ("1".equals(input.getFormdata().get("isSelect"))){
            String ids=input.getFormdata().get("ids")==null?null:String.valueOf(input.getFormdata().get("ids"));
            input.getFormdata().put("selectIds", StringUtils.isEmpty(ids)?"空":ids);
        }
        if(screenNum>0){
            input.setLength(screenNum);
            input.setPageSize(screenNum);
        }
        //应付票据：开票补录审批通过的票据
        if ("1".equals(input.getFormdata().get("isEmitBill"))){
            input.getFormdata().remove("isEmitBill");
            input.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, isEmitMake());
            output = abIntellectDao.listAccebillEmitInfoDataTable(input,corpAuthList,whereVOMap);
        }else{
            //应收
            output = abIntellectDao.listAccebillInfoDataTable(input,corpAuthList,whereVOMap);
        }
        Optional.ofNullable(output.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
                    ,"AB_BILL_SEARCHFOR_STATUS","AB_BRIEF_STATUS","AB_BILL_INSOPTSTATUS","AB_BAIL_TYPE","AB_BILL_SOURCE_QUERY","AB_BILL_SEND_SIGN"
                    ,"AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS","AB_BRIEF_STATUS","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS","AB_BRIEF_STATUS"
                    ,"AB_BILL_POOL_STATUS","AB_POOL_INOUT_TYPE","AB_BANEM","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbVoucherinfoViceVO voucher: listData){
                voucher.injectValues(listDictMap);
            }
        });
        if (screenNum > 0 && output != null && output.getRecordsTotal() > screenNum) {
            output.setRecordsTotal(screenNum);
            output.setRecordsFiltered(screenNum);
        }
        return output;
    }
    /**
     * 智能选票规则组装
     * @param input
     * @param whereVOMap
     * @param screenNum
     * @return
     */
    private int setIntellect(DataTablesInput input, Map<String, Object> whereVOMap, int screenNum) {
        //获取规则组id
        String ruleGroupId=input.getFormdata().get("ruleGroupId")==null?null:String.valueOf(input.getFormdata().get("ruleGroupId"));
        //获取规则id
        String ruleId=input.getFormdata().get("ruleId")==null?null:String.valueOf(input.getFormdata().get("ruleId"));
        input.getFormdata().remove("ruleId");
        String whereStr="";
        //排序方式sql
        StringBuilder sortSql = new StringBuilder();
        //排序方式value
        StringBuilder sortValue = new StringBuilder();
        //范围方式sql
        StringBuilder scopeSql = new StringBuilder();
        //范围方式value
        StringBuilder scopeValue = new StringBuilder();
        //值方式sql
        StringBuilder valSql = new StringBuilder();
        //值方式value
        StringBuilder valValue = new StringBuilder();
        //查询关联表语句sql
        StringBuilder tableSql = new StringBuilder();
        //规则组处理
        if(StringUtils.isNotEmpty(ruleGroupId)){
            //根据规则组id获取规则组明细
            List<AbIntellectGroupVO> groupVOList=abIntellectDao.listRuleGroupDtlByRuleGroupId(ruleGroupId);

            StringBuilder groupSql = new StringBuilder();
            for (AbIntellectGroupVO groupVO : groupVOList) {
                StringBuilder whereGroupSql = new StringBuilder();
                //组范围方式sql
                StringBuilder scopeGroupSql = new StringBuilder();
                //组范围方式value
                StringBuilder scopeGroupValue = new StringBuilder();
                //组值方式sql
                StringBuilder valGroupSql = new StringBuilder();
                //组值方式value
                StringBuilder valGroupValue = new StringBuilder();
                getIntellectRule(input, groupVO.getRuleId(), sortValue, scopeGroupSql, scopeGroupValue, valGroupSql, valGroupValue, tableSql, screenNum);
                if((scopeGroupSql.toString()+valGroupSql.toString()).length()>0){
                    whereGroupSql.append(" 1=1 " + scopeGroupSql).append(valGroupSql);
                }
                if(whereGroupSql.length()>0){
                    if(groupSql.length()>0){
                        groupSql.append(" "+groupVO.getRelation()+" ");
                    }
                    groupSql.append("(").append(whereGroupSql).append(") ");
                }
            }
            if(!StringUtils.isEmpty(groupSql.toString())) {
            	scopeSql.append(" and ").append(groupSql);
            }
            //设置筛选数量
            if(!CollectionUtils.isEmpty(groupVOList) && StringUtils.isNotEmpty(groupVOList.get(0).getScreenNum())){
                screenNum=Integer.valueOf(groupVOList.get(0).getScreenNum());
            }
        }
        //规则处理
        getIntellectRule(input, ruleId, sortValue, scopeSql, scopeValue, valSql, valValue, tableSql, screenNum);
        //查询表条件
        if(tableSql.length()>0){
            whereVOMap.put("table",tableSql.toString());
        }
        //查询条件
        whereStr=scopeSql.toString()+valSql.toString();
        if(whereStr.length()>0){
            whereVOMap.put("where",whereStr);
        }
        //排序条件
        if(sortValue.length()>0){
            sortSql.append(" order by " + sortValue);
            whereVOMap.put("order",sortSql.toString());
        }
        return screenNum;
    }

    /**
     * 根据 上级parentId 补全sql字段
     * @param intellectVO
     * @return
     */
    private void handleParentElement(AbIntellectVO intellectVO){
        if (StringUtils.isNotBlank(intellectVO.getParentId())
                && intellectVO.getSourceType().equals(AccebillConstant.SOURCE_TYPE3)
                && StringUtils.isBlank(intellectVO.getTableName())
                && StringUtils.isBlank(intellectVO.getFieldName())
        ){
            AbIntellectElement element = abIntellectElementService.get(intellectVO.getParentId());
            if (element != null){
                intellectVO.setSourceName(element.getSourceName());
                intellectVO.setDictGroup(element.getDictGroup());
                intellectVO.setDataType(element.getDataType());
                intellectVO.setTableName(element.getTableName());
                intellectVO.setFieldName(element.getFieldName());
                intellectVO.setComputeMode(element.getComputeMode());
                intellectVO.setSourceElementName1(element.getSourceElementName1());
                intellectVO.setSourceElementName2(element.getSourceElementName2());
            }
        }
    }
    /**
     * 根据规则id组装sql
     * @param input
     * @param ruleId
     * @param sortValue
     * @param scopeSql
     * @param scopeValue
     * @param valSql
     * @param valValue
     * @param tableSql
     * @param screenNum
     * @return
     */
    private int getIntellectRule(DataTablesInput input, String ruleId, StringBuilder sortValue, StringBuilder scopeSql, StringBuilder scopeValue, StringBuilder valSql, StringBuilder valValue, StringBuilder tableSql, int screenNum) {
        if(StringUtils.isNotEmpty(ruleId)){
            List<AbIntellectVO> intellectVOList = abIntellectDao.listRuleDtlByRuleId(ruleId);
            for(AbIntellectVO intellectVO : intellectVOList){
                handleParentElement(intellectVO);
                if(StringUtils.isNotEmpty(intellectVO.getScreenNum())){
                    screenNum=Integer.valueOf(intellectVO.getScreenNum());
                }
                switch (intellectVO.getScreenType()) {
                    //排序
                    case AccebillConstant.SCREEN_TYPE_SORT:
                        //排序方式：asc升序 desc降序
                        //要素来源类型为2数据字段
                        getSourceType2(sortValue,tableSql, intellectVO,null);
                        //要素来源类型为3计算公式
                        getSourceType3(input, sortValue, scopeSql, intellectVO);

                        //添加排序类型
                        if(sortValue.length()>0){
                            sortValue.append(" ").append(intellectVO.getSortOrder());
                        }

                        break;
                    //范围
                    case AccebillConstant.SCREEN_TYPE_SCOPE:
                        // 范围方式：series系列 valSco介于值之间
                        if(AccebillConstant.SCOPE_MODE_VALSCO.equals(intellectVO.getScopeMode())){
                            // ✅ 新增：先用临时变量获取字段名
                            StringBuilder fieldSql = new StringBuilder();

                            // 要素来源类型为表字段
                            if(AccebillConstant.SOURCE_TYPE2.equals(intellectVO.getSourceType())){
                                getSourceType2(fieldSql, tableSql, intellectVO, null);
                            } else if(AccebillConstant.SOURCE_TYPE3.equals(intellectVO.getSourceType())){
                                // ✅ 要素来源类型为计算公式：从上级要素获取字段名
                                if (StringUtils.isNotBlank(intellectVO.getParentId())) {
                                    String tableAlias = getTableAlias(intellectVO.getTableName());
                                    fieldSql.append(tableAlias).append(".").append(intellectVO.getFieldName());
                                } else {
                                    // 没有上级要素，通过 getSourceType3 获取
                                    getSourceType3(input, sortValue, fieldSql, intellectVO);
                                }
                            }

                            // ✅ 只有 fieldSql 不为空时才拼接范围条件
                            if (fieldSql.length() > 0) {
                                scopeSql.append(" and (");
                                // 开始
                                scopeSql.append(fieldSql).append(getRelationSymbol(intellectVO.getStartRelation()));
                                setValueByDataType(scopeSql, intellectVO.getStartValue(), intellectVO.getDataType(), true);
                                scopeSql.append(" and ");
                                // 结束
                                scopeSql.append(fieldSql).append(getRelationSymbol(intellectVO.getEndRelation()));
                                setValueByDataType(scopeSql, intellectVO.getEndValue(), intellectVO.getDataType(), true);
                                scopeSql.append(" ) ");
                            }
                        } else if(AccebillConstant.SCOPE_MODE_SERIES.equals(intellectVO.getScopeMode()) && StringUtils.isNotEmpty(intellectVO.getValue())){
                            // 要素来源类型为字典
                            getSourceTyp1(scopeSql, tableSql, intellectVO);
                            // 要素来源类型为表字段
                            if(AccebillConstant.SOURCE_TYPE2.equals(intellectVO.getSourceType())){
                                scopeSql.append(" and ");
                                // 获取查询字段
                                getSourceType2(scopeSql,tableSql, intellectVO,null);
                                if(scopeSql.toString().contains("in") || org.apache.commons.lang.StringUtils.isBlank(intellectVO.getValue())){
                                    continue;
                                }
                                scopeSql.append(" in (").append(intellectVO.getValue()).append(")");
                            }
                        }
                        break;
                    //值
                    case AccebillConstant.SCREEN_TYPE_VAL:
                        if(StringUtils.isNotEmpty(intellectVO.getValue())){
                            if("0".equals(intellectVO.getValueIsElement())){
                                //要素来源类型为表字段
                                getSourceType2(valValue,tableSql, intellectVO,null);
                                valSql.append(" and (");
                                //组装
                                valSql.append(valValue).append(getRelationSymbol(intellectVO.getRelation()));
                                //根据数据类型设计查询方式sql
                                setValueByDataType(valSql,intellectVO.getValue(), intellectVO.getDataType(),true);
                                valSql.append(" ) ");
                            }else if("1".equals(intellectVO.getValueIsElement())){
                                valSql.append(" and (");
                                //获取两要素
                                //选票要素
                                AbIntellectElementVO elementVO1 = abIntellectElementService.getAbIntellectElementVOById(intellectVO.getElementId());
                                //值
                                AbIntellectElementVO elementVO2 = abIntellectElementService.getAbIntellectElementVOById(intellectVO.getValue());
                                //来源类型
                                String sourceType1=elementVO1.getSourceType();
                                String sourceType2=elementVO2.getSourceType();
                                //数据类型
                                String dataType1=elementVO1.getDataType();
                                String dataType2=elementVO2.getDataType();

                                //类型都为数据字段且为money类型或者num类型
                                if (AccebillConstant.SOURCE_TYPE2.equals(sourceType1) && AccebillConstant.SOURCE_TYPE2.equals(sourceType2)
                                        && ((AccebillConstant.DATA_TYPE_MONEY.equals(dataType1)
                                        && AccebillConstant.DATA_TYPE_MONEY.equals(dataType2))
                                        || (AccebillConstant.DATA_TYPE_NUM.equals(dataType1)
                                        && (AccebillConstant.DATA_TYPE_NUM.equals(dataType2))))){
                                    String billMoney=input.getFormdata().get("billMoney")==null?null:String.valueOf(input.getFormdata().get("billMoney"));
                                    //当被凑数为付款金额时，取前台传过来的金额
                                    if("PAY_MONEY".equals(elementVO2.getFieldName()) && StringUtils.isNotEmpty(billMoney)){
                                        //组装
                                        valSql.append("(").append(getSourceType2(null,tableSql,null,elementVO1)).append(getRelationSymbol(intellectVO.getRelation())).append(billMoney).append(")");
                                    }else if("PAY_MONEY".equals(elementVO1.getFieldName()) && StringUtils.isNotEmpty(billMoney)){
                                        //组装
                                        valSql.append("(").append(billMoney).append(getRelationSymbol(intellectVO.getRelation())).append(getSourceType2(null,tableSql,null,elementVO1)).append(")");
                                    }else{
                                        valSql.append("(").append(getSourceType2(null,tableSql,null,elementVO1)).append(getRelationSymbol(intellectVO.getRelation())).append(getSourceType2(null,tableSql,null,elementVO2)).append(")");
                                    }

                                }
                                //
                                if (AccebillConstant.DATA_TYPE_DATE.equals(dataType1) && AccebillConstant.DATA_TYPE_DATE.equals(dataType2)){
                                    if("sysDate".equals(elementVO2.getSysConstant())){
                                        valSql.append("(").append(getSourceType2(null,tableSql,null,elementVO1)).append(getRelationSymbol(intellectVO.getRelation())).append("bt_to_date('").append(DateUtils.formatDate(DateUtils.getDate())).append("','yyyy-MM-dd'))");
                                    }else if("sysDate".equals(elementVO1.getSysConstant())){
                                        valSql.append("(").append("bt_to_date('").append(DateUtils.formatDate(DateUtils.getDate())).append("','yyyy-MM-dd')").append(getRelationSymbol(intellectVO.getRelation())).append(getSourceType2(null,tableSql,null,elementVO1)).append(")");
                                    }else{
                                        valSql.append("(").append(getSourceType2(null,tableSql,null,elementVO1)).append(getRelationSymbol(intellectVO.getRelation())).append(getSourceType2(null,tableSql,null,elementVO2)).append(")");
                                    }

                                }
                                valSql.append(" ) ");
                            }

                        }
                        break;
                    default:
                }
            }
        }
        return screenNum;
    }

    /**
     * 根据表名获取表别名
     * @param tableName 表名
     * @return 表别名
     */
    private String getTableAlias(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return "";
        }
        switch (tableName) {
            case "AB_VOUCHERINFO":
                return "z";
            case "AB_VOUCHERINFO_VICE":
                return "c";
            case "AB_ACCRECORDS":
                return "l";
            case "SYS_EXTERNAL":
                return "e";  // 如果有的话
            default:
                // 如果表名已经包含别名，直接返回
                if (tableName.contains(" ")) {
                    return tableName.substring(tableName.lastIndexOf(" ") + 1);
                }
                return tableName;
        }
    }

    /**
     * 根据数据类型设计查询方式sql
     * @param valSql 查询语句
     * @param value  值
     * @param dataType 数据类型
     */
    private void setValueByDataType(StringBuilder valSql,String value, String dataType,boolean isFormat) {
        //判断数据类型:money金额 date日期 text文本 num数值
        if(isFormat){
            if("date".equals(dataType)){
                valSql.append("BT_TO_DATE( '"+value+"','YYYY-MM-DD')");
            }else if("text".equals(dataType)){
                valSql.append("'"+value+"'");
            }else{
                valSql.append(" "+value+" ");
            }
        }else{
            valSql.append(value);
        }
    }
    /**
     * 要素来源类型为1数据字典
     * @param whereSql
     * @param tableSql
     * @param intellectVO
     */
    private void getSourceTyp1(StringBuilder whereSql, StringBuilder tableSql, AbIntellectVO intellectVO) {
        StringBuilder valueField=new StringBuilder();
        //要素来源类型为1数据字典、字典组代码不能为空、字典值不能为空
        if(AccebillConstant.SOURCE_TYPE1.equals(intellectVO.getSourceType()) && StringUtils.isNotEmpty(intellectVO.getDictGroup()) && StringUtils.isNotEmpty(intellectVO.getValue())) {
            //根据数据字典判断查询哪个表数据
            switch (intellectVO.getDictGroup()) {
                //银行种类
                case "sys.bankType":
                    if (!StringUtils.containsIgnoreCase(tableSql, "bt_bank_acc")) {
                        tableSql.append(" left join bt_bank_acc b on c.bank_acc=b.bank_acc left join bt_bank_type t on b.Bank_Type_Id=t.id ");
                    }
                    valueField.append(" t.bank_kind ");
                    break;
                //票据类型
                case "AB_BILL_TYPE":
                    valueField.append(" z.bill_type ");
                    break;
                //票据信用等级
                case "AB_BILL_CDT_RATGS":
                    valueField.append(" z.PAYER_CDT_RATGS ");
                    break;
                //流通介质
                case "AB_BILL_FLAG":
                    valueField.append(" z.bill_flag ");
                    break;
                default:
            }
            //获取值集合
            List<String> valueList = Arrays.asList(intellectVO.getValue().split(","));
            if(!CollectionUtils.isEmpty(valueList) && valueField.length()>0){
                whereSql.append(" and ").append(SqlUtil.getInSql(valueList, valueField.toString(), true));
            }
        }
    }
    /**
     * 要素来源类型为表字段
     * @param value
     * @param intellectVO
     * @return 字段
     */
    private String getSourceType2(StringBuilder value, StringBuilder tableSql, AbIntellectVO intellectVO,AbIntellectElementVO elementVO) {
        String sourceType="";
        String tableName="";
        String fieldName="";
        String screenType="";
        if(intellectVO!=null){
            sourceType=intellectVO.getSourceType();
            tableName=intellectVO.getTableName();
            fieldName=intellectVO.getFieldName();
            screenType=intellectVO.getScreenType();
        }else if(elementVO!=null){
            sourceType=elementVO.getSourceType();
            tableName=elementVO.getTableName();
            fieldName=elementVO.getFieldName();
            screenType="";
        }

        if(AccebillConstant.SOURCE_TYPE2.equals(sourceType)){
            if(value!=null && value.length()>0 && AccebillConstant.SCREEN_TYPE_SORT.equals(screenType)){
                value.append(",");
            }
            StringBuilder valueField=new StringBuilder();
            //主表
            String alias = getTableAlias(tableName);
            if (StringUtils.isNotBlank(alias)) {
                valueField.append(alias).append(".");
            }
            //当字段为信用等级时，且为排序时
            if (StringUtils.containsIgnoreCase(fieldName, "PAYER_CDT_RATGS")  && AccebillConstant.SCREEN_TYPE_SORT.equals(screenType)) {
                valueField = new StringBuilder();
                //查询内容语句设置对应值，这样查询列语句才可排序
                value.append("(CASE when z.payer_cdt_ratgs='AAA' then '9'")
                        .append(" when z.payer_cdt_ratgs='AA' then '8'")
                        .append(" when z.payer_cdt_ratgs='A' then '7'")
                        .append(" when z.payer_cdt_ratgs='BBB' then '6'")
                        .append(" when z.payer_cdt_ratgs='BB' then '5'")
                        .append(" when z.payer_cdt_ratgs='B' then '4'")
                        .append(" when z.payer_cdt_ratgs='CCC' then '3'")
                        .append(" when z.payer_cdt_ratgs='CC' then '2'")
                        .append(" when z.payer_cdt_ratgs='C' then '1'")
                        .append("ELSE '0' end )");
            } else
                //当为供应商时，则取供应商账号的银行性质，过滤票据持有账号的银行性质
                if(StringUtils.containsIgnoreCase(tableName, "SYS_EXTERNAL")
                        && StringUtils.containsIgnoreCase(fieldName, "BANKACC")
                        && StringUtils.isNotEmpty(intellectVO.getValue())){
                    valueField = new StringBuilder();
                    //根据客商账号获取银行性质
                    List<String> bankClassList = abIntellectDao.getBankClassByExtacc(Arrays.asList(intellectVO.getValue().split(",")));
                    //根据单位账号获取账号银行性质类型
                    List<String> bankClassList2 = abIntellectDao.getBankClassByBankAcc(Arrays.asList(intellectVO.getValue().split(",")));
                    bankClassList.addAll(bankClassList2);
                    if(!CollectionUtils.isEmpty(bankClassList)){
                        if (!StringUtils.containsIgnoreCase(tableSql, "SYS_EXTERNAL")) {
                            tableSql.append(" left join bt_bank_acc b on c.bank_acc=b.bank_acc left join bt_bank_type t on b.Bank_Type_Id=t.id ");
                        }
                        fieldName="";
                        valueField.append(SqlUtil.getInSql(bankClassList, "t.bank_class", true));

                    }
                }
            if(valueField.length()>0){
                valueField.append(fieldName);
                //添加字段
                if(value!=null){
                    value.append(valueField);
                }
                return valueField.toString();
            }
        }
        return null;
    }

    /**
     * 要素来源类型为3计算公式
     * @param input
     * @param value
     * @param intellectVO
     * @return 字段
     */
    private void getSourceType3(DataTablesInput input, StringBuilder value, StringBuilder whereSql, AbIntellectVO intellectVO) {
        //要素来源类型为3计算公式
        if(AccebillConstant.SOURCE_TYPE3.equals(intellectVO.getSourceType())){
            if(value!=null && value.length()>0 && AccebillConstant.SCREEN_TYPE_SORT.equals(intellectVO.getScreenType())){
                value.append(",");
            }
            //计算方式为sub相减时 计算方式：sum求和 count笔数 add相加 sub相减
            if(AccebillConstant.COMPUTE_MODE_SUB.equals(intellectVO.getComputeMode())){
                //获取两要素
                //减数
                AbIntellectElementVO elementVO1 = abIntellectElementService.getAbIntellectElementVOById(intellectVO.getSourceElementName1());
                //被减数
                AbIntellectElementVO elementVO2 = abIntellectElementService.getAbIntellectElementVOById(intellectVO.getSourceElementName2());
                //来源类型
                String sourceType1 = elementVO1.getSourceType();
                String sourceType2 = elementVO2.getSourceType();
                //数据类型
                String dataType1=elementVO1.getDataType();
                String dataType2=elementVO2.getDataType();

                //类型都为数据字段且为money类型或者num类型
                if ( ((AccebillConstant.DATA_TYPE_MONEY.equals(dataType1)
                        && AccebillConstant.DATA_TYPE_MONEY.equals(dataType2))
                        || (AccebillConstant.DATA_TYPE_NUM.equals(dataType1)
                        && (AccebillConstant.DATA_TYPE_NUM.equals(dataType2))))){
                    String billMoney=input.getFormdata().get("billMoney")==null?null:String.valueOf(input.getFormdata().get("billMoney"));
                    //当被凑数为付款金额时，取前台传过来的金额
                    if("PAY_MONEY".equals(elementVO2.getFieldName()) && StringUtils.isNotEmpty(billMoney)){
                        StringBuilder paySql = new StringBuilder();
                        paySql.append("(").append(billMoney).append("-").append(getSourceType2(null,null,null,elementVO1)).append(")");
                        if(value!=null){
                            value.append(paySql);
                        }
                        whereSql.append(" and ").append(paySql).append(">=0");
                    }else{
                        if(value!=null){
                            value.append("(").append(getSourceType2(null,null,null,elementVO2)).append("-").append(getSourceType2(null,null,null,elementVO1)).append(")");
                        }
                    }

                }
                //日期类型
                if (AccebillConstant.DATA_TYPE_DATE.equals(dataType1) && AccebillConstant.DATA_TYPE_DATE.equals(dataType2)){
                    String dialect = ((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();
                    //mysql用DATEDIFF
                    if (!dialect.toLowerCase().contains("oracle")) {
                        if("sysDate".equals(elementVO1.getSysConstant())){
                            whereSql.append("(").append(getSourceType2(null,null,null,elementVO2)).append("-").append(getSourceType2(null,null,null,elementVO1)).append(")");
                        }else{
                            whereSql.append("(").append(getSourceType2(null,null,null,elementVO2)).append("-").append(getSourceType2(null,null,null,elementVO1)).append(")");
                        }
                    }else {
                        if("sysDate".equals(elementVO1.getSysConstant())){
                            whereSql.append("(DATEDIFF(").append(getSourceType2(null,null,null,elementVO2)).append(",").append("bt_to_date('").append(DateUtils.formatDate(DateUtils.getDate())).append("','yyyy-MM-dd')))");
                        }else {
                            whereSql.append("(DATEDIFF(").append(getSourceType2(null,null,null,elementVO2)).append(",").append("bt_to_date('").append(DateUtils.formatDate(DateUtils.getDate())).append("','yyyy-MM-dd')))");
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据关系值获取对应的关系符号
     * @param relation
     * @return
     */
    private String getRelationSymbol(String relation){
        if(StringUtils.isNotEmpty(relation)){
            switch (relation) {
                //银行种类
                case "1":
                    return "=";
                case "2":
                    return "<>";
                case "3":
                    return ">";
                case "4":
                    return ">=";
                case "5":
                    return "<";
                case "6":
                    return "<=";
                default:
                    return "";
            }
        }
        return "";
    }
    /**
     * 开票是否需要补录
     *
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isEmitMake() {
        return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEMITMAKE).toBoolean(true);
    }

    @Override
    public BaseDao<AbIntellectVO, String> getEntityDao() {
        return abIntellectDao;
    }
}
