package com.bytter.ras.service;

import com.bytter.ras.dao.RasPosBorrowDao;
import com.bytter.ras.entity.RasPosBorrow;
import com.bytter.ras.vo.RasPosBorrowVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 营收稽核POS机借入业务处理类
 * @date 2019-05-07
 */
@Service("rasPosBorrowService")
@Transactional
public class RasPosBorrowService extends BaseService<RasPosBorrow, String> {

    @Autowired
    private RasShopGrantService rasShopGrantService;

    @Autowired
    private RasPosBorrowDao rasPosBorrowDao;

    @Autowired
    private RasLogService rasLogService;

    @Autowired(required=false)
    private ICorpService corpService;

    @Autowired
    private RasPosBindingService rasPosBindingService;

    @Override
    public BaseDao<RasPosBorrow, String> getEntityDao() {
        return null;
    }

    /**
     * //保存pos借入信息
     *
     * @param rasPosBorrow
     */
    public void save(RasPosBorrow rasPosBorrow) {
        rasPosBorrowDao.save(rasPosBorrow);
    }

    /**
     * 加载营收稽核POS机绑定数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRasPosBorrow(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_POSBORROW_OP, dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, dataTablesInput.getCurrentUser());
        return rasPosBorrowDao.getDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }

    /**
     * POS机借入打回
     *
     * @param pid    POS机借入id
     * @param msg    打回原因
     * @param userVo 操作用户
     */
    public void back(String pid, String msg, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(pid)) {
            batchBack(pid, msg, userVo);
        }
    }

    /**
     * POS机借入确认
     *
     * @param pid    POS机借入id
     * @param userVo 操作用户
     */
    public void sure(String pid, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(pid)) {
            batchSure(pid, userVo);
        }
    }

    /**
     * 批量打回POS机借入
     *
     * @param pids   POS机借入id
     * @param msg    打回原因
     * @param userVo 操作用户
     */
    public boolean batchBack(String pids, String msg, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(pids)) {
            String[] pidArray = pids.split(",");
            RasPosBorrow rasPosBorrowras = null;
            for (int i = 0; i < pidArray.length; i++) {
                rasPosBorrowras = rasPosBorrowDao.get(pidArray[i]);
                if (rasPosBorrowras != null) {
                    String[] pid = {rasPosBorrowras.getId()};
                    String[] bdId = {rasPosBorrowras.getBdId()};
                    //更新pos机借入状态为打回
                    int borrowNum = batchUpdate(pid, RasConstant.RAS_POS_BR_STATUS_RETURN, RasConstant.RAS_POS_BR_STATUS_01, null, null, msg, userVo);
                    if (borrowNum == 0) {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    } else {
                        //更新pos机绑定状态为正常
                        int bindingNum = rasPosBindingService.batchUpdate(bdId, RasConstant.RAS_POS_BD_STATUS_01, RasConstant.RAS_POS_BD_STATUS_02, userVo);
                        if (bindingNum == 0) {
                            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //添加日志
                        rasLogService.saveRasLog(rasPosBorrowras.getBdId(), RasConstant.RAS_LOGTYPE_RETURN, "ras.pos.machine.lend.repulse", userVo);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 批量确认POS机借入
     *
     * @param pids   POS机借入id
     * @param userVo 操作用户
     */
    public boolean batchSure(String pids, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(pids)) {
            String[] pidArray = pids.split(",");
            RasPosBorrow rasPosBorrowras = null;
            for (int i = 0; i < pidArray.length; i++) {
                rasPosBorrowras = rasPosBorrowDao.get(pidArray[i]);
                if (rasPosBorrowras != null) {
                    String[] pid = {rasPosBorrowras.getId()};
                    String[] bdId = {rasPosBorrowras.getBdId()};
                    //更新pos机借入状态为借用
                    int borrowNum = batchUpdate(pid, RasConstant.RAS_POS_BR_STATUS_02, RasConstant.RAS_POS_BR_STATUS_01, new Date(), null, userVo);
                    if (borrowNum == 0) {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    } else {
                        //更新pos机绑定状态为已借出
                        int bindingNum = rasPosBindingService.batchUpdate(bdId, RasConstant.RAS_POS_BD_STATUS_03, RasConstant.RAS_POS_BD_STATUS_02, userVo);
                        if (bindingNum == 0) {
                            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //添加日志
                        rasLogService.saveRasLog(rasPosBorrowras.getBdId(), RasConstant.RAS_LOGTYPE_OK, "ras.pos.machine.lend.confirm", userVo);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 根据ids批量修改POS机借入表数据状态
     *
     * @param ids       借入表ID
     * @param statusNew 修改为对的处理状态值（借入状态-1打回01待确认02借用03已归还）
     * @param statusOld 根据对应旧状态处理状态值修改（借入状态-1打回01待确认02借用03已归还）
     * @param bdDate    绑定时间
     * @param ubdDate   解绑时间
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] ids, String statusNew, String statusOld, Date bdDate, Date ubdDate, ICurrentUser userVo) {
        return this.batchUpdate(ids, statusNew, statusOld, bdDate, ubdDate, null, userVo);
    }

    /**
     * 根据ids批量修改POS机借入表数据状态
     *
     * @param ids       借入表ID
     * @param statusNew 修改为对的处理状态值（借入状态-1打回01待确认02借用03已归还）
     * @param statusOld 根据对应旧状态处理状态值修改（借入状态-1打回01待确认02借用03已归还）
     * @param msg       打回原因
     * @param bdDate    绑定时间
     * @param ubdDate   解绑时间
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] ids, String statusNew, String statusOld, Date bdDate, Date ubdDate, String msg, ICurrentUser userVo) {
        if (ids == null || ids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        if (statusOld != null) {
            params.put("statusOld", statusOld);
        }
        if (msg != null) {
            params.put("msg", msg);
        }
        if (bdDate != null) {
            params.put("bdDate", bdDate);
        }
        if (ubdDate != null) {
            params.put("ubdDate", ubdDate);
        }
        int num = rasPosBorrowDao.batchUpdate(params);
        return num;
    }


    /**
     * 通过pos机绑定id查询pos机借出数据
     *
     * @param bdId   pos机绑定id
     * @param status 状态
     * @return
     */
    public RasPosBorrowVO getRasPosBorrowByBdIdAndStatus(String bdId, String status) {
        List<RasPosBorrowVO> list = rasPosBorrowDao.getRasPosBorrowByBdIdAndStatus(bdId, new String[]{status});
        RasPosBorrowVO rasPosBorrow = null;
        if (list != null && !list.isEmpty()) {
            rasPosBorrow = list.get(0);
        }
        return rasPosBorrow;
    }

    /**
     * 通过pos机绑定id查询pos机借出数据
     *
     * @param bdId   pos机绑定id
     * @param status 状态
     * @return
     */
    public List<RasPosBorrowVO> getRasPosBorrowByBdId(String bdId, String[] status) {
        return rasPosBorrowDao.getRasPosBorrowByBdIdAndStatus(bdId, status);
    }

    /**
     * 通过pos机借入id查询pos机借出数据
     *
     * @param id pos机借入id
     * @return
     */
    public RasPosBorrowVO getRasPosBorrowById(String id) {
        List<RasPosBorrowVO> list = rasPosBorrowDao.getRasPosBorrowById(id);
        RasPosBorrowVO rasPosBorrow = null;
        if (list != null && !list.isEmpty()) {
            rasPosBorrow = list.get(0);
        }
        return rasPosBorrow;
    }

    /**
     * 根据提交参数生成RasPosBorrow POS机借入对象
     *
     * @param rasPosBorrowVO 页面传递信息
     * @param userVo         用户信息
     * @return RasPosBorrow
     */
    protected RasPosBorrow getRasPosBorrow(RasPosBorrowVO rasPosBorrowVO, ICurrentUser userVo) {
        RasPosBorrow entity = new RasPosBorrow();
        if (StringUtils.isNotEmpty(rasPosBorrowVO.getId())) {
            entity = rasPosBorrowDao.get(rasPosBorrowVO.getId());
            entity.setUbdDate(new Date());
            entity.setUpdateName(userVo.getUsername());// 修改人名称
            entity.setUpdateBy(userVo.getLoginName());// 修改人登录名称
            entity.setUpdateDate(new Date());// 修改日期
        } else {
            // entity.setBdDate(new Date());
            entity.setCreateName(userVo.getUsername());// 创建人名称
            entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
            entity.setCreateDate(new Date());// 创建日期
        }
        entity.setBdId(rasPosBorrowVO.getBdId());
        entity.setCorpId(rasPosBorrowVO.getCorpId());
        entity.setShopId(rasPosBorrowVO.getShopId());
        entity.setRmk(rasPosBorrowVO.getRmk());
        entity.setStatus(rasPosBorrowVO.getStatus());
        entity.setMsg(rasPosBorrowVO.getMsg());
        return entity;
    }


}