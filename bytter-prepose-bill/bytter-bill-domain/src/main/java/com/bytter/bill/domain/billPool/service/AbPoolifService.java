package com.bytter.bill.domain.billPool.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.domain.billPool.dao.AbPoolifDao;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPoolifVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: 票据池接口业务Service
 * @Description: 票据池接口业务处理类
 * @date 2019-12-30
 */
@Service("abPoolifService")
@Transactional
public class AbPoolifService extends BaseService<AbPoolif, String> {
    @Autowired
    private AbPoolifDao abPoolifDao;
    @Autowired
    private AbPoolCheckService abPoolCheckService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IDictService dictService;

    public BaseDao<AbPoolif, String> getEntityDao() {
        return abPoolifDao;
    }
    /**
     * 票据池接口列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<AbPoolifVO> dataTables = abPoolifDao.getDataTablesUnion(input);
        Optional.ofNullable(dataTables.getData()).ifPresent(this::redoundValues);
        return dataTables;
    }

    private void redoundValues(List<AbPoolifVO> dataList) {
        if (ObjectUtil.isNotEmpty(dataList)){
            List<String> codeList = dataList.stream().map(AbPoolifVO::getBifCode).collect(Collectors.toList());
            Map<String, BisBifInitDTO> bifInitMap = bisBifInitService.listBifInitByCode(codeList);
            Map<String, DictDTO> dictMap = dictService.listMap("AB_POOL_VALID_SIGN");
            dataList.forEach(item -> {
                Optional.ofNullable(bifInitMap).flatMap(bifMap -> Optional.ofNullable(bifInitMap.get(item.getBifCode())))
                        .ifPresent(bifInit -> item.setBifName(bifInit.getName()));
                Optional.ofNullable(dictMap).flatMap(dtoMap -> Optional.ofNullable(dictMap.get(item.getValidSign())))
                        .ifPresent(dict -> item.setValidSignName(dict.getName()));
            });
        }
    }

    /**
     * 提交票据池接口
     *
     * @param abPoolifVO 票据池接口VO
     * @return 修改成功的笔数
     */
    public void saveAbPoolif(AbPoolifVO abPoolifVO, ICurrentUser userVo) {
        if (abPoolifVO == null) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPoolif abPoolif = new AbPoolif();
        BeanUtils.copyNotNullProperties(abPoolifVO, abPoolif);
        abPoolif.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
        if (userVo != null) {
            abPoolif.setCreateId(userVo.getId());
            abPoolif.setCreateTime(new Date());
        }
        // 冗余银行接口ID
        Optional.ofNullable(abPoolif.getBifCode()).ifPresent(bifCode -> {
            BisBifInitDTO bifInit = bisBifInitService.getByBifCode(bifCode);
            abPoolif.setBifId(bifInit.getId());
        });
        abPoolifDao.save(abPoolif);
    }
    /**
     * 根据id批量删除票据池接口
     *
     * @param id     票据池接口ID
     * @param userVo 操作用户
     * @return 修改成功的笔数
     */
    public boolean batchDelete(String id, ICurrentUser userVo) {
        String[] ids = id.split(",");
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_2);
        if (userVo != null) {
            params.put("updateId", userVo.getId());
            params.put("updateTime", new Date());
        }
        int num = abPoolifDao.batchUpdate(params);
        if (num > 0) {
            return true;
        }
        return false;
    }
    /**
     * 修改票据池接口
     *
     * @param abPoolifVO 票据池接口VO
     * @return 修改成功的笔数
     */
    public int updateAbPoolifByAbPoolifVO(AbPoolifVO abPoolifVO, ICurrentUser userVo) {
        if (StringUtils.isEmpty(abPoolifVO.getId())) {
            return 0;
        }
        String[] ids = {abPoolifVO.getId()};
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (StringUtils.isNotEmpty(abPoolifVO.getPifCode())) {
            params.put("pifCode", abPoolifVO.getPifCode());
        }
        if (StringUtils.isNotEmpty(abPoolifVO.getPifName())) {
            params.put("pifName", abPoolifVO.getPifName());
        }
        if (StringUtils.isNotEmpty(abPoolifVO.getBifCode())) {
            BisBifInitDTO bifInit = bisBifInitService.getByBifCode(abPoolifVO.getBifCode());
            params.put("bifCode", abPoolifVO.getBifCode());
            params.put("bifId", bifInit.getId());
        }
        if (StringUtils.isNotEmpty(abPoolifVO.getRmk())) {
            params.put("rmk", abPoolifVO.getRmk());
        }
        if (userVo != null) {
            params.put("updateId", userVo.getId());
            params.put("updateTime", new Date());
        }
        return abPoolifDao.batchUpdate(params);
    }
    /**
     * 修改票据池接口状态
     *
     * @param id     池接口id
     * @param status 池接口状态
     * @param userVo 操作用户
     * @return 修改成功的笔数
     */
    public boolean updateStatus(String id, String status, ICurrentUser userVo) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(status)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        //禁用时检查是否有在途业务数据
        if(AbPoolConstant.AB_POLL_VALID_SIGN_0.equals(status)){
            abPoolCheckService.poolIfCheckQuery(id);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", id);
        params.put("validSign", status);
        if (userVo != null) {
            params.put("updateId", userVo.getId());
            params.put("updateTime", new Date());
        }
        int num = abPoolifDao.batchUpdate(params);
        if (num > 0) {
            return true;
        }
        return false;
    }
    /**
     * 通过id获取票据池接口信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolifVO getAbPoolifVO(String id) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            params.put("id", id);
        }
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        List<AbPoolifVO> list = this.getAbPoolifVOByParams(params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 判断是否有相同的接口代码、接口名称、银行接口代码
     *
     * @param pifCode 接口代码
     * @param pifName 接口名称
     * @param id      接口id
     * @param bifCode 银行接口代码
     * @return
     */
    public boolean checkPifCodeOrPifNameOrBifcode(String pifCode, String pifName, String id, String bifCode) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(pifCode)) {
            params.put("pifCode", pifCode);
        } else if (StringUtils.isNotEmpty(pifName)) {
            params.put("pifName", pifName);
        } else if (StringUtils.isNotEmpty(bifCode)) {
            params.put("bifCode", bifCode);
        } else {
            return false;
        }
        if (StringUtils.isNotEmpty(id)) {
            params.put("notId", id);
        }
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        List<AbPoolifVO> list = this.getAbPoolifVOByParams(params);
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }
    /**
     * 获取维护的启用状态的票据池接口
     *
     * @return
     */
    public List<AbPoolifVO> getAllPoolif(AbPoolifVO abPoolifVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_1});
        return this.getAbPoolifVOByParams(params);
    }
    /**
     * 根据参数查询票据池接口信息
     *
     * @param params 查询参数
     * @return
     */
    public List<AbPoolifVO> getAbPoolifVOByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        List<AbPoolifVO> dataList = abPoolifDao.getAbPoolifVOByParams(params);
        redoundValues(dataList);
        return dataList;
    }
}
