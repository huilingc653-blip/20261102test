package com.bytter.biz.netbank.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.dao.BtBankAccUseDao;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.vo.BtBankAccUseVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description: 账号用途维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/23
 */
@Service("btBankAccUseService")
public class BtBankAccUseService extends BaseService<BtBankAccUse,String> implements IUserExcelVerifyHandler<BtBankAccUseVO> {
    @Autowired
    private BtBankAccUseDao dao;
    @Override
    public BaseDao<BtBankAccUse, String> getEntityDao() {
        return dao;
    }

    /***
     * 列表数据
     * @param dataTablesInput
     * @return TODO
     */
    public DataTablesOutput<BtBankAccUseVO> listData(DataTablesInput dataTablesInput) {
        return dao.listData(dataTablesInput);
    }

    /***
     * 保存数据
     * @param vo
     * @param currentUser
     * @return TODO
     */
    public void save(BtBankAccUseVO vo, ICurrentUser currentUser) {
        // 校验code是否重复
        if (dao.checkCode(vo.getUseCode(),vo.getId())){
            throw new BusinessException(NetbankMessageEnum.NETBANK_MESSAGE_BANKACCUSE_UNIQUE);
        }
        BtBankAccUse bankAccUse = new BtBankAccUse();
        if (ObjectUtil.isNotEmpty(vo.getId())){
            bankAccUse = this.get(vo.getId(),true);
            bankAccUse.setUpdateBy(currentUser.getLoginName());
            bankAccUse.setUpdateDate(new Date());
        }else{
            bankAccUse.setCreateBy(currentUser.getLoginName());
            bankAccUse.setCreateDate(new Date());
        }
        bankAccUse.setUseCode(vo.getUseCode());
        bankAccUse.setUseName(vo.getUseName());
        bankAccUse.setUseRmk(vo.getUseRmk());

        this.saveOrUpdate(bankAccUse);
    }

    /***
     * 删除数据
     * @param id
     * @return TODO
     */
    public void deleteById(String id) {
        // 校验用途是否使用
        BtBankAccUse bankAccUse = this.get(id, true);
        if (dao.checkUse(bankAccUse.getUseCode())){
            throw new BusinessException(NetbankMessageEnum.NETBANK_MESSAGE_BANKACCUSE_USE);
        }
        this.deleteEntity(bankAccUse);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtBankAccUseVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<BtBankAccUseVO> list, Object... user) {
        List<String> useCodes = new ArrayList<>();
        List<String> repeatCodes = new ArrayList<>();

        int i = 0;
        for (BtBankAccUseVO vo : list){
            List<String> errorList = new ArrayList<>();
            if (ObjectUtil.isEmpty(vo.getUseCode())){
                errorList.add("用途编码不能为空！");
            }else{
                if (dao.checkCode(vo.getUseCode(), "")){
                    errorList.add("该用途编码已存在，请修改！");
                }
                if (i > 0 && useCodes.contains(vo.getUseCode())){
                    repeatCodes.add(vo.getUseCode());
                }else{
                    useCodes.add(vo.getUseCode());
                }

            }
            if (ObjectUtil.isEmpty(vo.getUseName())){
                errorList.add("用途名称不能为空！");
            }
            vo.addErrorMsg(errorList);
            i++;
        }
        if (ObjectUtil.isNotEmpty(repeatCodes)){
            if (!useCodes.isEmpty()){
                for (String code : repeatCodes){
                    for (BtBankAccUseVO vo : list){
                        if (code.equals(vo.getUseCode())){
                            String str = "用途编码："+code+"；出现重复，请修改！";
                            vo.addErrorMsg(Arrays.asList(str));
                        }
                    }
                }
            }
        }
    }

    public void saveImport(List<BtBankAccUseVO> list, ICurrentUser currentUser) {
        List<BtBankAccUse> entityList = new ArrayList<>();
        for (BtBankAccUseVO vo : list){
            BtBankAccUse accUse = new BtBankAccUse();
            accUse.setUseCode(vo.getUseCode());
            accUse.setUseName(vo.getUseName());
            accUse.setUseRmk(vo.getUseRmk());
            accUse.setCreateDate(new Date());
            accUse.setCreateBy(currentUser.getLoginName());

            entityList.add(accUse);
        }
        if (ObjectUtil.isNotEmpty(entityList)){
            this.batchSave(entityList);
        }
    }

    public List<BtBankAccUseVO> getVOList(){
        List<BtBankAccUseVO> voList = new ArrayList<>();
        List<BtBankAccUse> btBankAccUses = dao.listAll();
        if (ObjectUtil.isNotEmpty(btBankAccUses)){
            for (BtBankAccUse btBankAccUse : btBankAccUses){
                BtBankAccUseVO vo = new BtBankAccUseVO();
                vo.setId(btBankAccUse.getId());
                vo.setUseCode(btBankAccUse.getUseCode());
                vo.setUseName(btBankAccUse.getUseName());
                vo.setUseRmk(btBankAccUse.getUseRmk());
                voList.add( vo);
            }
        }
        return voList;
    }
}
