package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.billPool.dao.AbPoolDao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPoolVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: 票据池业务Service
 * @Description: 票据池业务处理类
 * @date 2020-1-3
 */
@Service("abPoolService")
@Transactional
public class AbPoolService extends BaseService<AbPool, String> {
    @Autowired
    private AbPoolDao abPoolDao;
    @Autowired
    private AbPoolCheckService abPoolCheckService;
    @DubboReference(check = false)
    private IDictService dictService;
    
    @Override
    public BaseDao<AbPool, String> getEntityDao() {
        return abPoolDao;
    }
    /**
     * 票据池接口列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<AbPoolVO> getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<AbPoolVO> dataTables = abPoolDao.getDataTablesUnion(input);
        Optional.ofNullable(dataTables.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_POOL_BP_TYPE","AB_BILL_POOL_BP_MODE","AB_POOL_VALID_SIGN");
            listData.forEach(item-> redundantValues(listDictMap, item));
        });
        return dataTables;
    }

    private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolVO item) {
        Optional.ofNullable(listDictMap).ifPresent(listMap -> {
            Optional.ofNullable(item.getBpType()).flatMap(bpType -> Optional.ofNullable(listDictMap.get("AB_BILL_POOL_BP_TYPE"))
                    .flatMap(dictMap -> Optional.ofNullable(dictMap.get(bpType)))).ifPresent(dict -> item.setBpTypeName(dict.getName()));
            Optional.ofNullable(item.getBpMode()).flatMap(bpMode -> Optional.ofNullable(listDictMap.get("AB_BILL_POOL_BP_MODE"))
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(bpMode))))
                    .ifPresent(dict -> item.setBpModeName(dict.getName()));
            Optional.ofNullable(item.getValidSign()).flatMap(validSign -> Optional.ofNullable(listDictMap.get("AB_POOL_VALID_SIGN"))
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(validSign))))
                    .ifPresent(dict -> item.setValidSignName(dict.getName()));
        });
    }

    /**
     * 保存票据
     *
     * @param abPoolVO 票据池VO
     */
    public void saveAbPool(AbPoolVO abPoolVO, ICurrentUser userVo) {
        if (abPoolVO == null) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPool abPool = new AbPool();
        BeanUtils.copyNotNullProperties(abPoolVO, abPool);
        abPool.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
        if (userVo != null) {
            abPool.setCreateId(userVo.getId());
            abPool.setCreateTime(new Date());
        }
        abPoolDao.save(abPool);
    }
    /**
     * 根据id批量删除票据池
     *
     * @param id     票据池ID
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
        int num = abPoolDao.batchUpdate(params);
        if (num > 0) {
            return true;
        }
        return false;
    }
    /**
     * 修改票据池接口
     *
     * @param abPoolVO 票据池VO
     * @return 修改成功的笔数
     */
    public void updateAbPoolByAbPoolVO(AbPoolVO abPoolVO, ICurrentUser userVo) {
        if (StringUtils.isEmpty(abPoolVO.getId())) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPool abPool = abPoolDao.get(abPoolVO.getId());
        BeanUtils.copyNotNullProperties(abPoolVO, abPool);
        if (userVo != null) {
            abPool.setUpdateId(userVo.getId());
            abPool.setUpdateTime(new Date());
        }
        abPoolDao.update(abPool);
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
            abPoolCheckService.poolCheckQuery(id);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", id);
        params.put("validSign", status);
        if (userVo != null) {
            params.put("updateId", userVo.getId());
            params.put("updateTime", new Date());
        }
        int num = abPoolDao.batchUpdate(params);
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
    public AbPoolVO getAbPoolVO(String id) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            params.put("id", id);
        }
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        List<AbPoolVO> list = this.getAbPoolVOByParams(params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 判断是否有相同的接口代码或者接口名称
     *
     * @param bpCode 票据池代码
     * @param bpName 票据池名称
     * @param id     票据池id
     * @param pifId  池接口id
     * @return
     */
    public boolean checkBpCodeOrBpNameOrPifId(String bpCode, String bpName, String id, String pifId) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(bpCode)) {
            params.put("bpCode", bpCode);
        } else if (StringUtils.isNotEmpty(bpName)) {
            params.put("bpName", bpName);
        } else if (StringUtils.isNotEmpty(pifId)) {
            params.put("pifId", pifId);
        } else {
            return false;
        }
        if (StringUtils.isNotEmpty(id)) {
            params.put("notId", id);
        }
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        List<AbPoolVO> list = this.getAbPoolVOByParams(params);
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
    public List<AbPoolVO> getAllPool(AbPoolVO abPoolVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_1});
        params.put("pledgeeValidSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return this.getAbPoolVOByParams(params);
    }
    /**
     * 通过id获取票据池接口信息及质权人信息、池接口信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolVO getAbPoolVOByPId(String id) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            params.put("id", id);
        }
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        params.put("pledgeeValidSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        List<AbPoolVO> list = this.getAbPoolVOByParams(params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据参数查询票据池接口信息
     *
     * @param params 查询参数
     * @return
     */
    public List<AbPoolVO> getAbPoolVOByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        List<AbPoolVO> listData = abPoolDao.getAbPoolVOByParams(params);
        Optional.ofNullable(listData).ifPresent(list -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_POOL_BP_TYPE", "AB_BILL_POOL_BP_MODE", "AB_POOL_VALID_SIGN");
            listData.forEach(item -> redundantValues(listDictMap, item));
        });
        return listData;
    }
    /**
     * 获取有效票据池（银行自动配置的银行有效且为票据池开票开启的银行）
     *
     * @return 票据池
     */
    public List<AbPoolVO> getAbPoolByBankAuto() {
        return abPoolDao.getAbPoolByBankAuto(null);
    }
}
