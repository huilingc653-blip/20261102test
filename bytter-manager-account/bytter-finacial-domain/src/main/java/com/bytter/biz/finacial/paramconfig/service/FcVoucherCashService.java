package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.paramconfig.dao.FcVoucherCashDao;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherCash;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author
 * @version V1.0
 * @Title: dao
 * @Description: 现金流量项目科目关系服务
 * @date 2020-09-10 09:30:39
 */
@Service("fcVoucherCashService")
@Transactional
public class FcVoucherCashService extends BaseService<FcVoucherCash, String> {
    @Autowired
    private FcVoucherCashDao voucherCashDao;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @Override
    public BaseDao<FcVoucherCash, String> getEntityDao() {
        return voucherCashDao;
    }
    public DataTablesOutput listVoucherCash(DataTablesInput input) {
        DataTablesOutput dataTablesOutput = voucherCashDao.listVoucherCash(input);
        List<FcVoucherCashVo> lists = dataTablesOutput.getData();
        List<String> subjectIds01 = lists.stream().map(FcVoucherCashVo::getDebitSubCode).collect(Collectors.toList());
        List<String> subjectIds02 = lists.stream().map(FcVoucherCashVo::getCreditSubCode).collect(Collectors.toList());
        subjectIds02.addAll(subjectIds01);
        List<FcSubjectDTO> subjectDtoList = iFcSubjectService.getSubjectByIds(subjectIds02);
        Map<String, FcSubjectDTO> subjectDtoMap = subjectDtoList.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
        lists.forEach(a -> {
            Optional.ofNullable(subjectDtoMap.get(a.getDebitSubCode())).ifPresent(subject -> {
                a.setDebitSubCode(subject.getSubjectCode());
                a.setDebitSubName(subject.getSubjectName());
            });
            Optional.ofNullable(subjectDtoMap.get(a.getCreditSubCode())).ifPresent(subject -> {
                a.setCreditSubCode(subject.getSubjectCode());
                a.setCreditSubName(subject.getSubjectName());
            });
        });
        dataTablesOutput.setData(lists);
        return dataTablesOutput;
    }
    /**
    * <br>
    * <b>功能：</b> 现金流量科目关系保存更新 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/10 0010 <br>
    */
    public void submitVoucherCash(FcVoucherCash voucherCash, ICurrentUser user) {
        //校验是否重复
        List<FcVoucherCashVo> itemList = voucherCashDao.queryVoucherCash(voucherCash);
        if (!EmptyUtils.isEmptyList(itemList)) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_ITEM_EXISTS);
        }
        if (EmptyUtils.isEmpty(voucherCash.getId())) {
            voucherCash.setCreateBy(user.getId());
            voucherCash.setCreateTime(DateUtils.getDate());
        } else {
            voucherCash.setUpdateBy(user.getId());
            voucherCash.setUpdateTime(DateUtils.getDate());
        }
        voucherCashDao.saveOrUpdate(voucherCash);
    }
    /**
    * <br>
    * <b>功能：</b> 现金流量科目数据删除 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/14 0010 <br>
    */
    public void deleteVoucherCash(String voucherCashId, ICurrentUser user) {
        voucherCashDao.delete(voucherCashId);
    }
    /**
     * <br>
     * <b>功能：</b> 查询现金流量项目科目关系维护中已使用的现金流量科目 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/9/14 0011 <br>
     */
    public List<FcVoucherCashVo> queryUsedItem(String itemId) {
        return voucherCashDao.queryUsedItem(itemId);
    }
    /**
     * 根据id获取
     * @param id
     * @return
     */
    public FcVoucherCashVo getById(String id){
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        FcVoucherCashVo vo = voucherCashDao.getById(param);
        String debitSubCode = vo.getDebitSubCode();
        String creditSubCode = vo.getCreditSubCode();
        List<FcSubjectDTO> subjectDTOS = iFcSubjectService.getSubjectByIds(Lists.newArrayList(debitSubCode, creditSubCode));
        Map<String, FcSubjectDTO> subjectDTOMap = subjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
        FcSubjectDTO fcSubjectDTO01 = subjectDTOMap.get(debitSubCode);
        FcSubjectDTO fcSubjectDTO02 = subjectDTOMap.get(creditSubCode);
        vo.setDebitSubCode(fcSubjectDTO01.getSubjectCode());
        vo.setDebitSubName(fcSubjectDTO01.getSubjectName());
        vo.setCreditSubCode(fcSubjectDTO02.getSubjectCode());
        vo.setCreditSubName(fcSubjectDTO02.getSubjectName());
        return vo;
    }
    /**
     * 根据科目选择现金流项目
     * @param debitItem
     * @param creditItem
     * @return
     */
    public FcVoucherCashVo getItemBySubject(String debitItem,String creditItem) {
        List<FcVoucherCashVo> fcVoucherCashVos = voucherCashDao.getItemBySubject(debitItem, creditItem);
        FcVoucherCashVo fcVoucherCashVo = null;
        if (ListUtils.isNotNullOrEmpty(fcVoucherCashVos)) {
            for (FcVoucherCashVo voucherCashVo : fcVoucherCashVos) {
                if (StringUtils.isNotEmpty(debitItem) && StringUtils.isNotEmpty(voucherCashVo.getDebitItemName())) {
                    fcVoucherCashVo = voucherCashVo;
                    break;
                } else if (StringUtils.isNotEmpty(creditItem) && StringUtils.isNotEmpty(voucherCashVo.getCreditItemName())){
                    fcVoucherCashVo = voucherCashVo;
                }
            }
        }
        return fcVoucherCashVo;
    }
}
