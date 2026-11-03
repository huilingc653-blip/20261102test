package com.bytter.supply.domain.common.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.domain.common.dao.ScfInstructionDao;
import com.bytter.supply.domain.common.entity.ScfInstruction;
import com.bytter.supply.domain.common.vo.ScfInstructionVO;
import com.bytter.supply.domain.payable.dto.ScfPayableInstructionDTO;
import com.bytter.supply.domain.payable.entity.ScfPayableVoucher;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0
 * @Title: ScfInstructionService
 * @Description: 指令管理业务层
 */
@Service("scfInstructionService")
@Transactional
public class ScfInstructionService extends BaseService<ScfInstruction, String> {

    @Autowired
    private ScfInstructionDao scfInstructionDao;

    @DubboReference(check = false)
    private IConfigService configService;

    @Override
    public BaseDao<ScfInstruction, String> getEntityDao() {
        return scfInstructionDao;
    }

    /**
     * 列表查询
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<ScfInstructionVO> listScfInstruction(DataTablesInput dataTablesInput) {
        return scfInstructionDao.dataTable(dataTablesInput);
    }

    /**
     * 应付指令列表（sign = 0）
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<ScfInstructionVO> listPayableInstruction(DataTablesInput dataTablesInput) {
        return scfInstructionDao.dataTableBySign(dataTablesInput, "0");
    }

    /**
     * 应收指令列表（sign = 1）
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<ScfInstructionVO> listReceivableInstruction(DataTablesInput dataTablesInput) {
        return scfInstructionDao.dataTableBySign(dataTablesInput, "1");
    }

    /**
     * 应付指令新增（sign = 0）
     */
    public void savePayableInstruction(ScfPayableInstructionDTO dto, String instructionSign) {
        ScfInstruction entity = new ScfInstruction();
        BeanUtils.copyNotNullProperties(dto, entity);
        entity.setId(null);
        entity.setInstrType(instructionSign);
        entity.setInstrTypeName(ScfEntBankOpenConstant.getInterfaceName(instructionSign));
        entity.setInstrCode(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_INSTRUCTION_NO));
        entity.setInstrAmt(dto.getVoucherMoney());
        entity.setInstrDate(DateUtils.getDateYMD());
        //关联编号：应付侧存债权凭证凭证号，供撤销/重复指令校验精确关联
        entity.setRelateNo(dto.getRelateNo());
        entity.setSign(ScfEntBankOpenConstant.SCF_PAYABLE_INSTRUMENT_SIGN_0);
        //指令状态 这里需要判断参数配置 中的指令发送模式
        setInstrTransStatus(entity);
        entity.setCreateBy(dto.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
    }

    private void setInstrTransStatus(ScfInstruction entity) {
        ConfigDTO config = configService.getConfig("scf", "instructionMode");
        if(ScfEntBankOpenConstant.SCF_INSTRUMENT_MODE_1.equals(config.getValue())){
            entity.setTransStatus(ScfEntBankOpenConstant.INSTRUCTION_SEND_SUCCESS);
        }else{
            entity.setTransStatus(ScfEntBankOpenConstant.INSTRUCTION_PENDING_SEND);
        }
    }

    /**
     * 应收指令新增（sign = 1）
     */
    public void saveReceivableInstruction(ScfInstruction entity) {
        entity.setSign("1");
        entity.setCreateDate(DateUtils.getDate());
        //指令状态 这里需要判断参数配置 中的指令发送模式
        setInstrTransStatus(entity);
        super.saveEntity(entity);
    }



}
