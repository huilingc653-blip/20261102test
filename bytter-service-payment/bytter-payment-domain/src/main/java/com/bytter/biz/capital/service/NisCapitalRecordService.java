package com.bytter.biz.capital.service;
import com.bytter.biz.capital.dao.NisCapitalRecordDao;
import com.bytter.biz.capital.entity.NisCapitalRecord;
import com.bytter.biz.capital.vo.NisCapitalRecordVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * @author shenc
 * @date 2019.3.28
 * 资金备案台账
 */
@Service
@Transactional
public class NisCapitalRecordService extends BaseService<NisCapitalRecord,String> implements IExcelVerifyHandler<NisCapitalRecordVo> {
    @Autowired
    private NisCapitalRecordDao nisCapitalRecordDao;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @DubboReference(check = false)
    private IExternalCorpService iExternalCorpService;
    @Override
    public BaseDao getEntityDao() {
        return nisCapitalRecordDao;
    }
    @Override
    public void deleteEntity(NisCapitalRecord entity){
        nisCapitalRecordDao.delete(entity);
    }
    public void saveImport(List<NisCapitalRecordVo> list, ICurrentUser user) {
        for (NisCapitalRecordVo recordVo : list) {
            NisCapitalRecord record = new NisCapitalRecord();
            BeanUtils.copyProperties(recordVo,record);
            record.setStatus(0);
            record.setCreateBy(user.getLoginName());
            record.setCreateDate(DateUtils.getDate());
            this.saveNisCapitalRecord(record);
        }
    }
    public void saveNisCapitalRecord(NisCapitalRecord nisCapitalRecord) {
        nisCapitalRecord.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_CAPITAL_RECORD));
        nisCapitalRecordDao.save(nisCapitalRecord);
    }
    public DataTablesOutput<NisCapitalRecordVo> getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser user) {
        List<CorpDTO> corpDTOS = iCorpService.listUserCorp(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY, user.getId());
        List<String> corpIds = new ArrayList<>();
        for (CorpDTO dto:corpDTOS){
            corpIds.add(dto.getId());
        }
        DataTablesOutput<NisCapitalRecordVo> dataTablesOutput = nisCapitalRecordDao.getDataTableUnion(dataTablesInput,corpIds);
        List<NisCapitalRecordVo> nisCapitalRecordVoList = dataTablesOutput.getData();
        for(NisCapitalRecordVo nisCapitalRecordVo:nisCapitalRecordVoList){
            injectValue(nisCapitalRecordVo);
        }
        return dataTablesOutput;
    }
    public NisCapitalRecordVo getNisCapitalRecordVoById(String id) {
        NisCapitalRecordVo nisCapitalRecordVo = nisCapitalRecordDao.getNisCapitalRecordVoById(id);
        injectValue(nisCapitalRecordVo);
        return nisCapitalRecordVo;
    }
    public void toModify(NisCapitalRecord entity) {
        nisCapitalRecordDao.update(entity);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisCapitalRecordVo recordVo) {
        List<String> msgList = new ArrayList<>();
        //校验成员单位
        if (StringUtils.isNotEmpty(recordVo.getCorpName())){
            CorpDTO corpDto = iCorpService.getCorpIdByName(recordVo.getCorpName());
            if (EmptyUtils.isEmpty(corpDto)||EmptyUtils.isEmpty(corpDto.getId())){
                msgList.add("nis.capitalRecord.message.resetAcceptor.iserror");
            }else{
                recordVo.setCorpId(corpDto.getId());
            }
        }
        //校验结算中心
        if (StringUtils.isNotEmpty(recordVo.getNetName())){
            CorpDTO corpDto = iCorpService.getCorpIdByName(recordVo.getNetName());
            if (EmptyUtils.isEmpty(corpDto)||EmptyUtils.isEmpty(corpDto.getId())){
                msgList.add("nis.capitalRecord.message.netName.iserror");
            }else{
                recordVo.setNetId(corpDto.getId());
            }
        }
        //校验收款单位
        if (StringUtils.isNotEmpty(recordVo.getPayeeAccName())){
            CorpDTO corpDTO = iCorpService.getCorpIdByName(recordVo.getPayeeAccName());
            if (EmptyUtils.isEmpty(corpDTO)){
                ExternalCorpDTO externalCorpDTO = iExternalCorpService.findByCorpIdAndName(null,recordVo.getPayeeAccName());
                if (EmptyUtils.isEmpty(externalCorpDTO)){
                    msgList.add("nis.capitalRecord.message.payeeAcc.iserror");
                }
            }
        }
        //校验资金备案类别
        if (StringUtils.isNotEmpty(recordVo.getBusTypeStr())){
            String dictCode = iDictService.getCodeByName("capitalRecord.busType", recordVo.getBusTypeStr());
            if (StringUtils.isNotEmpty(dictCode)){
                recordVo.setBusType(dictCode);
            }else{
                msgList.add("nis.capitalRecord.message.busType.iserror");
            }
        }
        //校验金额
        if (!EmptyUtils.isEmpty(recordVo.getPayMoney())){
            if (recordVo.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
                msgList.add("nis.capitalRecord.message.payMoney.iserror");
            }
        }
        if (StringUtils.isNotEmpty(recordVo.getVoucherTypeStr())){
            String dictCode = iDictService.getCodeByName("capitalRecord.voucerType", recordVo.getVoucherTypeStr());
            if (StringUtils.isNotEmpty(dictCode)){
                recordVo.setVoucherType(dictCode);
            }
        }
        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }
    @Override
    public void verifyHandler(List<NisCapitalRecordVo> list) {
    }
    public void injectValue(NisCapitalRecordVo nisCapitalRecordVo){
        CorpDTO corpDTO = iCorpService.get(nisCapitalRecordVo.getCorpId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            nisCapitalRecordVo.setCorpName(corpDTO.getName());
        }
        corpDTO = iCorpService.get(nisCapitalRecordVo.getNetId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            nisCapitalRecordVo.setNetName(corpDTO.getName());
        }
    }
}
