package com.bytter.biz.finacial.inneraccount.service.impl;
import com.bytter.biz.finacial.api.service.IFcAccDetailQueryService;
import com.bytter.biz.finacial.inneraccount.service.FcAccDetailQueryService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
@Service("fcAccDetailQueryServiceImpl")
@DubboService
@Transactional
public class FcAccDetailQueryServiceImpl implements IFcAccDetailQueryService {
    @Autowired
    private FcAccDetailQueryService fcAccDetailQueryService;
    /**
     * 查询内部账号流水列表
     *
     * @param
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, Map<String,Object> params) {
        return fcAccDetailQueryService.getDataTablesUnion(dataTablesInput, joinSql, params);
    }
}
