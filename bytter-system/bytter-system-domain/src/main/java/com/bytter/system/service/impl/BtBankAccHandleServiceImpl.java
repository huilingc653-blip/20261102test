package com.bytter.system.service.impl;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccHandleService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.BtBankAccHandleVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.dao.BtBankAccCommonDao;
import com.bytter.system.dao.BtBankAccHandleDao;
import com.bytter.system.entity.BtBankAcc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("btBankAccHandleServiceI")
@DubboService
@Transactional
public class BtBankAccHandleServiceImpl implements IBtBankAccHandleService {
    @Autowired
    private BtBankAccHandleDao btBankAccHandleDao;
    @Autowired
    private BtBankAccCommonDao btBankAccCommonDao;
    @Autowired
    private DictServiceImpl dictServiceImpl;
    @Autowired
    private IBankTypeService bankTypeService;
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String appSql, String accSql, String busType) {
        DataTablesOutput output = btBankAccHandleDao.getDataTablesUnion(dataTablesInput, appSql, accSql, busType);
        setCurName( output);
        injectBankTypeName(output);
        return output;
    }
    /**
     * 设置币别
     *
     * @param output
     */
    public void setCurName(DataTablesOutput output) {
        if (null != output.getData()) {
            for (int i = 0; i < output.getData().size(); i++) {
                BtBankAccHandleVO vo = (BtBankAccHandleVO) output.getData().get(i);
                List<BtCurrencyDTO> list = this.btBankAccCommonDao.listCurAccById(vo.getId());
                if (EmptyUtils.isEmptyList(list)) {
                    list = this.btBankAccCommonDao.listCurAccAppById(vo.getId());
                }
                StringBuilder curName = new StringBuilder();
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        curName.append(list.get(j).getCurName());
                    } else {
                        curName.append(CommonConstant.COMMA_SPLIT).append(list.get(j).getCurName());
                    }
                }
                vo.setCurName(curName.toString());
            }
        }
    }
    public void injectBankTypeName(DataTablesOutput output){
        if (null != output.getData()) {
            for (int i = 0; i < output.getData().size(); i++) {
                if (output.getData().get(i) instanceof BtBankAccHandleVO) {
                    BtBankAccHandleVO vo = (BtBankAccHandleVO) output.getData().get(i);
                    BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(vo.getBankTypeId());
                    if(!EmptyUtils.isEmpty(bankTypeDTO)){
                        vo.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }else if(output.getData().get(i) instanceof BtBankAccVO){
                    BtBankAccVO vo = (BtBankAccVO) output.getData().get(i);
                    BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(vo.getBankTypeId());
                    if(!EmptyUtils.isEmpty(bankTypeDTO)){
                        vo.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }
            }
        }
    }
    @Override
    public List<BtBankAccVO> getBankaccList() {
        List<BtBankAcc> listVO = btBankAccHandleDao.getBankaccList();
        if (EmptyUtils.isEmptyList(listVO)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listVO, BtBankAccVO.class);
    }
    @Override
    public DataTablesOutput getDataTableByBifCode(DataTablesInput dataTablesInput, String joinSql) {
        return btBankAccHandleDao.getDataTableByBifCode(dataTablesInput, joinSql);
    }
    @Override
    public boolean checkQuoteBusiness(String bankAcc) {
        return btBankAccHandleDao.checkQuoteBusiness(bankAcc);
    }
    @Override
    public boolean checkQuotePrentBankAcc(String id) {
        return btBankAccHandleDao.checkQuotePrentBankAcc(id);
    }
    @Override
    public boolean checkQuoteFcAcc(String id) {
        return btBankAccHandleDao.checkQuoteFcAcc(id);
    }
    @Override
    public boolean checkQuoteEntityBankAcc(String id) {
        return btBankAccHandleDao.checkQuoteEntityBankAcc(id);
    }
    @Override
    public boolean isHasParentAccById(String id) {
        return btBankAccHandleDao.isHasParentAccById(id);
    }
    @Override
    public boolean isHasChildAccById(String id) {
        return btBankAccHandleDao.isHasChildAccById(id);
    }
    @Override
    public List<BtBankAccDTO> listByExpireDate(String searchDate) {
        List<BtBankAccVO> listVO = btBankAccHandleDao.listByExpireDate(searchDate);
        if (EmptyUtils.isEmptyList(listVO)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listVO, BtBankAccDTO.class);
    }
    @Override
    public List<BtBankAccDTO> listByEndDate(String accType,String searchDate) {
        List<BtBankAcc> listVO = btBankAccHandleDao.listByEndDate(accType,searchDate);
        if (EmptyUtils.isEmptyList(listVO)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listVO, BtBankAccDTO.class);
    }
    @Override
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput output = btBankAccHandleDao.dataTableApprove(dataTablesInput, joinSql);
        injectBankTypeName(output);
        return output;
    }
}
