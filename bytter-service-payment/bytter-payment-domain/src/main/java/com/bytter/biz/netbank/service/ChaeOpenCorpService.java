package com.bytter.biz.netbank.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.netbank.dao.ChaeOpenCorpDao;
import com.bytter.biz.netbank.vo.ChaeOpenCorpVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 差额下拨启用单位
 * @date 2017-05-17 19:23:52
 */
@Service("chaeOpenCorpService")
public class ChaeOpenCorpService implements IExcelVerifyHandler<ChaeOpenCorpVO>  {
    @Autowired
    private ICorpService iCorpService;
    @Autowired
    private ChaeOpenCorpDao chaeOpenCorpDao;
    @Autowired
    private IConfigService iConfigService;

    public DataTablesOutput<ChaeOpenCorpVO> dataTable(DataTablesInput dataTablesInput) {
        return chaeOpenCorpDao.dataTable(dataTablesInput);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(ChaeOpenCorpVO obj) {
        return null;
    }

    @Transactional
    public void save(String corpId) {
        String[] array = corpId.split(",");
        // 数组转为 list;corpIdList必须是可变的，下面使用了 corpIdList.remove();
        List<String> corpIdList =  new ArrayList<>();
        for(String id : array){
            corpIdList.add(id);
        }
        // 先查询已设置的，执行更新操作
        DataTablesInput input = new DataTablesInput();
        input.setLength(2000000);// 不分页
        input.getFormdata().put("corpId",corpId);
        DataTablesOutput output = chaeOpenCorpDao.dataTable(input);
        List<ChaeOpenCorpVO> data = output.getData();
        if (CollectionUtil.isNotEmpty(data)){
            for(ChaeOpenCorpVO vo : data){
                if (CommonConstant.NO.equals(vo.getIsChaeOpenCorp())){
                    Map<String,String> updateMap = new HashMap<>();
                    updateMap.put("id",vo.getId());
                    updateMap.put("value", CommonConstant.YES);
                    iConfigService.saveOrUpdate2(updateMap);
                }
                corpIdList.remove(vo.getCorpId());
            }
        }

        // corpIdList 剩下的则为新增
        for(String id : corpIdList){
            Map<String,String> saveMap = new HashMap<>();
            saveMap.put("type","nis-isChaeOpenCorp");
            saveMap.put("name",id);
            saveMap.put("value", CommonConstant.YES);
            iConfigService.saveOrUpdate2(saveMap);
        }
    }

    @Override
    public void verifyHandler(List<ChaeOpenCorpVO> list) {
        if(CollectionUtil.isEmpty(list)){
            return;
        }
        for(ChaeOpenCorpVO vo : list){
            List<String> errMessage = new ArrayList<String>();
            if(StringUtils.isBlank(vo.getCorpCode())){
                errMessage.add("单位编码不能为空");
            }
            if(StringUtils.isBlank(vo.getCorpName())){
                errMessage.add("单位名称不能为空");
            }
            if(StringUtils.isBlank(vo.getIsEnableCorpChaeXB())){
                errMessage.add("是否开启差额下拨不能为空");
            }
            if(!("是".equals(vo.getIsEnableCorpChaeXB()) || "否".equals(vo.getIsEnableCorpChaeXB()))){
                errMessage.add("是否开启差额下拨数据错误");
            }
            CorpDTO corpByCode = iCorpService.getCorpByCode(vo.getCorpCode());
            if(corpByCode == null || !corpByCode.getName().equals(vo.getCorpName())){
                errMessage.add("单位数据错误");
            }
            vo.addErrorMsg(errMessage);
        }
    }
}
