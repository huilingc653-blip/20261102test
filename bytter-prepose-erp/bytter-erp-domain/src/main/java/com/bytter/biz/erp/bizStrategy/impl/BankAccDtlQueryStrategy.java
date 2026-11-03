package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.component.CommonParam;
import com.bytter.biz.erp.entity.BisAccDtl;
import com.bytter.biz.erp.service.ErpBisAccDtlService;
import com.bytter.biz.erp.vo.ErpBisAccDtlVo;
import com.bytter.biz.erp.vo.request.BankAccDtlQueryReqVo;
import com.bytter.biz.erp.vo.response.ComPageResponse;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行明细查询接口
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Slf4j
@Component
public class BankAccDtlQueryStrategy implements BizStrategy {
    @Autowired
    private CommonParam commonParam;

    @Autowired
    private ErpBisAccDtlService bisAccDtlService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.BIS_ACC_DTL_QUERY.getServiceCode(); // 服务代码
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String requestNo = params.get(ErpConstant.DO_BIZ_SERIAL_NO);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 请求报文转为对象
        BankAccDtlQueryReqVo reqVo = safeParseJson(data, BankAccDtlQueryReqVo.class);
        String checkResult = checkData(reqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return map;
        }
        reqVo.setTenantId(tenantId);

        // 返回实体map
        ComPageResponse response = new ComPageResponse();
        // 未传入当前页码或当前页面小于等于0，默认为1
        long currentPage = 1;
        if (ObjectUtil.isNotEmpty(reqVo.getCurrentPage()) && reqVo.getCurrentPage() > 0) {
            currentPage = reqVo.getCurrentPage();
        }
        // 获取每页显示条数，从服务端参数配置中读取
        Long pageSize = commonParam.getLongVal(ErpConstant.PARAM_DATA_SIZE);
        if (pageSize == null || pageSize <= 0) {
            // 默认每页10条，防止配置缺失导致异常
            pageSize = 10L;
        }

        // 根据条件查询银行明细
        List<BisAccDtl> allList = bisAccDtlService.listBisAccDtl(reqVo);
        // 手动分页计算起始索引
        // 注意：skip的参数是跳过的元素个数，即 (currentPage - 1) * pageSize
        long skipCount = (currentPage - 1) * pageSize;
        // 执行手动分页
        List<BisAccDtl> showList = allList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
        // 计算出总页数
        response.setTotalPage(allList.size() == 0 ? 0 : (allList.size() + pageSize - 1) / pageSize);
        response.setCurrentPage(currentPage);

        if (!CollectionUtils.isEmpty(showList)) {
            List<ErpBisAccDtlVo> resultList = showList.stream().map(op -> {
                ErpBisAccDtlVo vo = new ErpBisAccDtlVo();
                BeanUtils.copyProperties(op, vo, new String[]{"id", "transTime"});
                vo.setSerialId(op.getId());
                vo.setTransTime(DateUtil.formatDateTime(op.getTransTime()));
                return vo;
            }).collect(Collectors.toList());
            // 当前返回条数
            response.setPageSize(Long.valueOf(resultList.size()));
            response.setList(resultList);
            map.put(ErpConstant.DO_BIZ_MSG, "查询成功");
        } else {
            response.setPageSize(0L);
            map.put(ErpConstant.DO_BIZ_MSG, "查无数据");
        }

        map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(response));
        return map;
    }

    // 校验数据
    private String checkData(BankAccDtlQueryReqVo reqVo) {
        StringBuilder result = new StringBuilder(128);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(reqVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }
        // 银行账号
        if (ObjectUtil.isEmpty(reqVo.getBankAcc())) {
            appendMsg(result, "银行账号不能为空");
        }
        // 收支
        String cdSign = reqVo.getCdSign();
        if (ObjectUtil.isNotEmpty(cdSign)) {
            if (!ErpConstant.YES.equals(cdSign) && !ErpConstant.NO.equals(cdSign)) {
                appendMsg(result, "收支标识不合法，请输入0或者1");
            }
        }
        // 开始日期
        String transTimeStart = reqVo.getBeginDate();
        if (ObjectUtil.isNotEmpty(transTimeStart)) {
            try {
                DateUtil.parseDate(transTimeStart);
            } catch (Exception e) {
                appendMsg(result, "请输入正确的开始日期");
            }
        } else {
            appendMsg(result, "请输入交易日期-区间开始");
        }
        // 结束日期
        String transTimeEnd = reqVo.getEndDate();
        if (ObjectUtil.isNotEmpty(transTimeEnd)) {
            try {
                DateUtil.parseDate(transTimeEnd);
            } catch (Exception e) {
                appendMsg(result, "请输入正确的结束日期");
            }
        } else {
            appendMsg(result, "请输入交易日期-区间结束");
        }
        //开始结束日期区间
        if (ObjectUtil.isEmpty(result)) {
            try {
                if (DateUtil.parseDate(transTimeStart).after(DateUtil.parseDate(transTimeEnd))) {
                    appendMsg(result, "开始日期不能大于结束日期");
                } else {
                    long daysBetween = DateUtil.betweenDay(DateUtil.parseDate(transTimeStart),
                            DateUtil.parseDate(transTimeEnd), true);
                    int dateRange = commonParam.getIntVal(ErpConstant.PARAM_DATE_RANGE);
                    if (daysBetween > dateRange) {
                        appendMsg(result, "日期范围超出限制，最大允许" + dateRange + "天");
                    }
                }
            } catch (Exception e) {
                appendMsg(result, "日期计算异常");
            }
        }

        return result.toString();
    }
}
