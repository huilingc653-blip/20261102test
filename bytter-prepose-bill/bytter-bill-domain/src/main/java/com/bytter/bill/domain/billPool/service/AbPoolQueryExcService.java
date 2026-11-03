package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.dao.AbPoolQueryExcDao;
import com.bytter.bill.domain.billPool.entity.AbPoolQueryExc;
import com.bytter.bill.domain.billPool.vo.AbPoolExcVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 票据池指令查询业务Service
 * @Description: 票据池指令查询业务处理类
 * @date 2020-11-12
 */
@Service("abPoolQueryExcService")
@Transactional
public class AbPoolQueryExcService extends BaseService<AbPoolQueryExc, String> {
    @Autowired
    private AbPoolQueryExcDao abPoolQueryExcDao;
    @DubboReference(check = false)
	private ICorpService iCorpService;
    @Autowired
    private AbPoolCorpService abPoolCorpService;
    public BaseDao<AbPoolQueryExc, String> getEntityDao() {
        return abPoolQueryExcDao;
    }
    /**
     * 手工同步
     *
     * @param bean 票据池指令Vo
     */
    public void manualSynchronize(AbPoolExcVO bean, ICurrentUser user) {
        if (StringUtils.isEmpty(bean.getExcType())) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        //待发送状态
        bean.setStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
        bean.setCreateTime(new Date());
        List<String> corpIds = null;
        if (StringUtils.isNotBlank(bean.getCorpId())) {
            corpIds = Arrays.asList(bean.getCorpId().split(","));
        }
        //单位为空时为全选
        else {
            //判断指令查询类型指定操作权限
            //票据池额度
            if (bean.getExcType().equals(AbPoolConstant.QUERY_EXC_TYPE_01)) {
                //获取操作用户有操作权限的票据池单位
                corpIds = abPoolCorpService.getCorpListByPamas(AbPoolConstant.AB_POLL_QUERY_LIMIT_OP, user);
            }
            //出入池登记簿
            else if (bean.getExcType().equals(AbPoolConstant.QUERY_EXC_TYPE_02)) {
                //获取操作用户有操作权限的票据池单位
                corpIds = abPoolCorpService.getCorpListByPamas(AbPoolConstant.AB_POLL_QUERY_INOUT_OP, user);
            }
            //在池登记簿
            else if (bean.getExcType().equals(AbPoolConstant.QUERY_EXC_TYPE_03)) {
                //获取操作用户有操作权限的票据池单位
                corpIds = abPoolCorpService.getCorpListByPamas(AbPoolConstant.AB_POLL_QUERY_HOLD_OP, user);
            }
            //保证金交易登记薄
            else if (bean.getExcType().equals(AbPoolConstant.QUERY_EXC_TYPE_04)) {
                //获取操作用户有操作权限的票据池单位
                corpIds = abPoolCorpService.getCorpListByPamas(AbPoolConstant.AB_POLL_QUERY_BAIL_OP, user);
            }
            //票据池额度明细同步
            else if (bean.getExcType().equals(AbPoolConstant.QUERY_EXC_TYPE_05)) {
                //获取操作用户有操作权限的票据池单位
                corpIds = abPoolCorpService.getCorpListByPamas(AbPoolConstant.AB_POLL_LIMITALLOT_RESOURCE_QUERYOP, user);
            }
        }
        //根据所选择的单位来生成指令
        if (corpIds != null && !corpIds.isEmpty()) {
            AbPoolQueryExc queryExc;
            for (String corpId : corpIds) {
                queryExc = new AbPoolQueryExc();
                //复制
                BeanUtils.copyNotNullProperties(bean, queryExc);
                queryExc.setCorpId(corpId);
                //生成发送指令
                saveAbPoolQueryExc(queryExc);
            }
        }
    }
    /**
     * 提交票据池指令
     *
     * @param abPoolQueryExc 票据池指令对象
     * @return 修改成功的笔数
     */
    public void saveAbPoolQueryExc(AbPoolQueryExc abPoolQueryExc) {
        if (abPoolQueryExc == null) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        abPoolQueryExc.setCreateTime(new Date());
        abPoolQueryExcDao.save(abPoolQueryExc);
    }
    /**
     * 修改票据池指令状态
     *
     * @param id     票据池指令id
     * @param status 票据池指令状态
     * @return 修改成功的笔数
     */
    public int updateStatusById(String id, Integer status) {
        if (StringUtils.isEmpty(id)) {
            return 0;
        }
        String[] ids = {id};
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (status != null) {
            params.put("status", status);
        }
        params.put("updateTime", new Date());
        int num = abPoolQueryExcDao.batchUpdate(params);
        return num;
    }
}
