package com.bytter.biz.corpCashier.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.corpCashier.dao.NisCorpCashierDao;
import com.bytter.biz.corpCashier.dao.NisCorpCashierUserDao;
import com.bytter.biz.corpCashier.entity.NisCorpCashier;
import com.bytter.biz.corpCashier.entity.NisCorpCashierUser;
import com.bytter.biz.corpCashier.vo.NisCorpCashierVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@Service("nisCorpCashierService")
public class NisCorpCashierService extends BaseService<NisCorpCashier, String> implements IExcelVerifyHandler<NisCorpCashierVO> {

    @Autowired
    private NisCorpCashierDao nisCorpCashierDao;
    @Autowired
    private NisCorpCashierUserDao nisCorpCashierUserDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IUserService userService;
    @Override
    public BaseDao<NisCorpCashier, String> getEntityDao() {
        return nisCorpCashierDao;
    }

    public DataTablesOutput<NisCorpCashierVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput<NisCorpCashierVO> output = nisCorpCashierDao.getDataTablesUnion(dataTablesInput);
        if (ObjectUtil.isNotEmpty(output.getData())){
            List<NisCorpCashierVO> data = output.getData();
            for (NisCorpCashierVO vo : data){
                vo.setCorpName("("+vo.getCorpCode()+")"+vo.getCorpName());
                // 查询用户信息
                List<NisCorpCashierUser> userList = nisCorpCashierUserDao.listBy("corpCashierId", vo.getId());
                // 把userList的userName 拼接成字符串 赋值给vo
                String userName = userList.stream().map(NisCorpCashierUser::getUserName).reduce((a,b)->a+","+b).get();
                vo.setUserName(userName);
            }
        }
        return output;
    }

    @Transactional
    public void save(NisCorpCashierVO vo,ICurrentUser currentUser) {
        if (ObjectUtil.isNotEmpty(vo.getCorpId()) && ObjectUtil.isNotEmpty(vo.getUserIds())){
            CorpDTO corpDTO = corpService.get(vo.getCorpId());
            // 检查单位是否存在
            Boolean checkCorpCashier = nisCorpCashierDao.checkCorpCashier(vo.getCorpId(),vo.getId());
            if (checkCorpCashier){
                throw new BusinessException("已存在该单位的出纳出纳，请直接编辑");
            }
            // 保存主表数据
            NisCorpCashier nisCorpCashier;
            if (ObjectUtil.isEmpty(vo.getId())){
                nisCorpCashier = new NisCorpCashier();
                BeanUtils.copyNotNullProperties(vo, nisCorpCashier);
                nisCorpCashier.setCreateBy(currentUser.getLoginName());
                nisCorpCashier.setCreateDate(new Date());
                nisCorpCashier.setCorpId(corpDTO.getId());
                nisCorpCashier.setCorpCode(corpDTO.getCode());
                nisCorpCashier.setCorpName(corpDTO.getName());
                nisCorpCashier.setStatus(CommonConstant.YES);
                nisCorpCashierDao.save(nisCorpCashier);
            }else{
                nisCorpCashier = nisCorpCashierDao.get(vo.getId());
                nisCorpCashier.setUpdateBy(currentUser.getLoginName());
                nisCorpCashier.setUpdateDate(new Date());
                nisCorpCashier.setCorpId(corpDTO.getId());
                nisCorpCashier.setCorpCode(corpDTO.getCode());
                nisCorpCashier.setCorpName(corpDTO.getName());
                nisCorpCashier.setStatus(CommonConstant.YES);
                nisCorpCashierDao.update(nisCorpCashier);

                // 用户数据先删除。再新增
                nisCorpCashierUserDao.deleteCashier(nisCorpCashier.getId());
            }

            // 保存用户数据
            for (String userId : vo.getUserIds().split(",")){
                UserDTO userDTO = userService.getByUserId(userId);
                NisCorpCashierUser nisCorpCashierUser = new NisCorpCashierUser();
                nisCorpCashierUser.setCorpCashierId(nisCorpCashier.getId());
                nisCorpCashierUser.setCorpId(corpDTO.getId());
                nisCorpCashierUser.setUserId(userId);
                nisCorpCashierUser.setUserCode(userDTO.getLoginName());
                nisCorpCashierUser.setUserName(userDTO.getUsername());
                nisCorpCashierUserDao.save(nisCorpCashierUser);
            }
        }
    }

    @Transactional
    public void deleteCorpCashier(String id) {
        if (ObjectUtil.isNotEmpty(id)){
            nisCorpCashierDao.delete(id);
            nisCorpCashierUserDao.deleteCashier(id);
        }
    }

    public NisCorpCashierVO getCorpCashier(String id) {
        NisCorpCashier nisCorpCashier = nisCorpCashierDao.get(id, true);
        NisCorpCashierVO vo = new NisCorpCashierVO();
        BeanUtils.copyNotNullProperties(nisCorpCashier, vo);
        List<NisCorpCashierUser> corpCashierUsers = nisCorpCashierUserDao.listBy("corpCashierId", id);
        if (ObjectUtil.isNotEmpty(corpCashierUsers)){
            String userIds = corpCashierUsers.stream().map(NisCorpCashierUser::getUserId).reduce((a,b)->a+","+b).get();
            vo.setUserIds(userIds);
            String userNames = corpCashierUsers.stream().map(NisCorpCashierUser::getUserName).reduce((a,b)->a+","+b).get();
            vo.setUserName(userNames);
        }
        return vo;
    }



    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisCorpCashierVO obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<NisCorpCashierVO> list) {
        for (NisCorpCashierVO vo : list){
            List<String> errMessage = new ArrayList<String>();
            if (ObjectUtil.isEmpty(vo.getCorpName())){
                errMessage.add("单位名称不能为空");
            }else{
                CorpDTO corpDTO = corpService.getCorpIdByName(vo.getCorpName());
                if (ObjectUtil.isEmpty(corpDTO)){
                    errMessage.add("单位名称无效");
                }else{
                    // 校验该单位是否维护数据
                    Boolean checkCorpCashier = nisCorpCashierDao.checkCorpCashier(corpDTO.getId(),"");
                    if (checkCorpCashier){
                        errMessage.add("已存在该单位的出纳，请直接编辑");
                    }else{
                        vo.setCorpId(corpDTO.getId());
                        vo.setCorpCode(corpDTO.getCode());
                    }
                }
            }

            if (ObjectUtil.isEmpty(vo.getUserCodes())){
                errMessage.add("出纳员工编码不能为空");
            }else{
                boolean flag = true;
                String userIds = "";
                for (String userCode : vo.getUserCodes().split(",")){
                    UserDTO userDTO = userService.getByLoginName(userCode);
                    if (ObjectUtil.isEmpty(userDTO)){
                        errMessage.add("出纳员工编码无效");
                        flag = false;
                        break;
                    }else{
                       if (ObjectUtil.isEmpty(userIds)){
                           userIds = userDTO.getId();
                       }else{
                           userIds = userIds + "," + userDTO.getId();
                       }
                    }
                }
                if ( flag){
                    vo.setUserIds(userIds);
                }
            }
            if (ObjectUtil.isNotEmpty(errMessage)){
                vo.addErrorMsg(errMessage);
            }
        }
    }

    public void saveImport(List<NisCorpCashierVO> list, ICurrentUser currentUser) {
        for (NisCorpCashierVO vo : list){
            this.save(vo,currentUser);
        }
    }

    public List<NisCorpCashierUser> getCashierUser(List<String> corpIds) {
        return nisCorpCashierUserDao.getCashierUser(corpIds);
    }
}
