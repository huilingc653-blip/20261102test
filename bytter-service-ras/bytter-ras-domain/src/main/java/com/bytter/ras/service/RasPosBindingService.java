package com.bytter.ras.service;

import com.bytter.ras.dao.RasPosBindingDao;
import com.bytter.ras.entity.RasPosBinding;
import com.bytter.ras.entity.RasPosBorrow;
import com.bytter.ras.vo.RasPosBindingVO;
import com.bytter.ras.vo.RasPosBorrowVO;
import com.bytter.ras.vo.RasShopVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 营收稽核POS机绑定业务处理类
 * @date 2019-05-05
 */
@Service("rasPosBindingService")
@Transactional
public class RasPosBindingService extends BaseService<RasPosBinding, String>
        implements IExcelVerifyHandler<RasPosBindingVO> {

    @Autowired
    private RasShopGrantService rasShopGrantService;

    @Autowired
    private RasPosBindingDao rasPosBindingDao;

    @Autowired
    private RasPosBorrowService rasPosBorrowService;

    @Autowired
    private RasLogService rasLogService;

    @Autowired(required=false)
    private ICorpService corpService;

    @Autowired
    private RasShopService rasShopService;

    @Override
    public BaseDao<RasPosBinding, String> getEntityDao() {
        return null;
    }


    /**
     * 加载营收稽核POS机绑定数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRasPosBinding(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_POSBINDING_OP, dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, dataTablesInput.getCurrentUser());
        return rasPosBindingDao.getDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }


    /**
     * 新增-营收稽核POS机绑定数据
     *
     * @param rasPosBindingVO
     * @param user
     * @param operationType   操作类型:新增,导入
     */
    public void saveRasPosBinding(RasPosBindingVO rasPosBindingVO, ICurrentUser user, String operationType) {
        //pos机绑定判断重复校验
        checkPosBinding(rasPosBindingVO.getPosNo(), null);
        //数据转换
        RasPosBinding rasPosBinding = getRasPosBinding(rasPosBindingVO, user);
        rasPosBindingDao.save(rasPosBinding);
        //添加日志
        String msg = "";
        if (RasConstant.RAS_BANK_OPERATE_STATUS_SAVE.equals(operationType)) {
            msg = "ras.pos.machine.add";
        } else if (RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT.equals(operationType)) {
            msg = "ras.pos.machine.import.add";
        }
        rasLogService.saveRasLog(rasPosBinding.getId(), RasConstant.RAS_LOGTYPE_ADD, msg, user);
    }

    /**
     * 修改-营收稽核POS机绑定数据
     *
     * @param rasPosBindingVO
     * @param user
     */
    public void updateRasPosBinding(RasPosBindingVO rasPosBindingVO, ICurrentUser user) {
        //pos机绑定判断重复校验
        checkPosBinding(rasPosBindingVO.getPosNo(), rasPosBindingVO.getId());
        //数据转换
        RasPosBinding rasPosBinding = getRasPosBinding(rasPosBindingVO, user);
        rasPosBindingDao.update(rasPosBinding);
        //添加日志
        rasLogService.saveRasLog(rasPosBinding.getId(), RasConstant.RAS_LOGTYPE_UPDATE, "ras.pos.machine.update", user);
    }

    /**
     * 添加营收稽核POS机借出信息
     *
     * @param rasPosBorrowVO
     * @param user
     */
    public void lendSave(RasPosBorrowVO rasPosBorrowVO, ICurrentUser user) {
        if (StringUtils.isNotEmpty(rasPosBorrowVO.getBdId())) {
            String[] ids = {rasPosBorrowVO.getBdId()};
            //判断借入人是否和借出人为同一门店
            RasPosBinding rasPosBinding = rasPosBindingDao.get(rasPosBorrowVO.getBdId());
            if (rasPosBinding != null) {
                if (rasPosBinding.getShopId().equals(rasPosBorrowVO.getShopId())) {
                    throw new BusinessException(RasMessageEnum.RAS_POSBINDING_MESSAGE_BORROWPOSNO_REPEAT);
                }
            }
            //更新pos绑定信息状态
            int size = this.batchUpdate(ids, RasConstant.RAS_POS_BD_STATUS_02, RasConstant.RAS_POS_BD_STATUS_01, user);
            if (size == 0) {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            //借入方为待确认状态
            rasPosBorrowVO.setStatus(RasConstant.RAS_POS_BR_STATUS_01);
            //POS机借出数据转换
            RasPosBorrow rasPosBorrow = rasPosBorrowService.getRasPosBorrow(rasPosBorrowVO, user);
            //保存借出信息
            rasPosBorrowService.save(rasPosBorrow);
            //添加日志
            rasLogService.saveRasLog(rasPosBorrowVO.getBdId(), RasConstant.RAS_LOGTYPE_LOAN, "ras.pos.machine.lender", user);
        } else {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
    }

    /**
     * 营收稽核POS机收回
     *
     * @param bdId pos机绑定id
     * @param user
     */
    public void revokeSave(String bdId, ICurrentUser user) {
        if (StringUtils.isNotEmpty(bdId)) {
            String[] ids = {bdId};
            int size = this.batchUpdate(ids, RasConstant.RAS_POS_BD_STATUS_01, RasConstant.RAS_POS_BD_STATUS_03, user);
            if (size == 0) {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            //根据pos机绑定id获取状态为已借出的pos借出数据
            RasPosBorrowVO rasPosBorrow = rasPosBorrowService.getRasPosBorrowByBdIdAndStatus(bdId, RasConstant.RAS_POS_BR_STATUS_02);
            String[] bIds = {rasPosBorrow.getId()};
            //保存借出信息 借出数据改为03已归还状态,更新解绑时间
            int sizePb = rasPosBorrowService.batchUpdate(bIds, RasConstant.RAS_POS_BR_STATUS_03, RasConstant.RAS_POS_BR_STATUS_02, null, new Date(), user);
            if (sizePb == 0) {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            //添加日志
            rasLogService.saveRasLog(bdId, RasConstant.RAS_LOGTYPE_BACK, "ras.pos.machine.take.back", user);
        } else {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
    }

    /**
     * 根据POS机绑定ID查询POS机绑定数据
     *
     * @param pid POS机绑定ID
     * @return
     */
    @Transactional(readOnly = true)
    public RasPosBindingVO getRasPosBindingVOByPid(String pid) {
        RasPosBindingVO vo = null;
        if (StringUtils.isNotEmpty(pid)) {
            List<RasPosBindingVO> rasPosBinding = rasPosBindingDao.getRasPosBindingVOByPid(pid);
            if (rasPosBinding != null && !rasPosBinding.isEmpty()) {
                vo = rasPosBinding.get(0);
            }
        }
        return vo;
    }


    /**
     * 根据参数获取POS机绑定数据
     *
     * @param posNo     终端号
     * @param validSign 有效标记 0禁用  1启用
     * @return
     */
    @Transactional(readOnly = true)
    public RasPosBinding getRasPosBindingByParams(String posNo, String validSign) {
        RasPosBinding bind = null;
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(posNo)) {
            params.put("posNo", posNo);
        }
        if (StringUtils.isNotEmpty(validSign)) {
            params.put("validSign", validSign);
        }
        List<RasPosBinding> rasPosBinding = rasPosBindingDao.getRasPosBindingByParams(params);
        if (rasPosBinding != null && !rasPosBinding.isEmpty()) {
            bind = rasPosBinding.get(0);
        }
        return bind;
    }

    /**
     * pos机绑定判断重复校验(规则:终端号做唯一校验来判断重复)
     *
     * @param posNo 终端号
     * @param pid   pos机绑定id
     */
    @Transactional(readOnly = true)
    public void checkPosBinding(String posNo, String pid) {
        Map<String, Object> params = new HashMap<>();
        params.put("posNo", posNo);
        if (StringUtils.isEmpty(pid)) {
            if (rasPosBindingDao.checkPosBinding(params, null)) {
                throw new BusinessException(RasMessageEnum.RAS_POSBINDING_MESSAGE_POSNO_REPEAT);
            }
        } else if (rasPosBindingDao.checkPosBinding(params, pid)) {
            throw new BusinessException(RasMessageEnum.RAS_POSBINDING_MESSAGE_POSNO_REPEAT);
        }

    }


    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<RasPosBindingVO> list, ICurrentUser userVo) {
        if (list != null && !list.isEmpty()) {
            for (RasPosBindingVO vo : list) {
                this.saveRasPosBinding(vo, userVo, RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT);
            }
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(RasPosBindingVO obj) {
        return null;
    }

    /**
     * 导入数据校验
     */
    @Override
    @Transactional(readOnly = true)
    public void verifyHandler(List<RasPosBindingVO> list) {
        if (list != null && !list.isEmpty()) {
            {
                List<String> errorMsgList = null;
                CorpDTO corpDTO = null;
                RasShopVO rasShopVO = null;
                Set<String> posNo = new HashSet<>();
                for (RasPosBindingVO rasPosBindingVO : list) {
                    errorMsgList = new ArrayList<>();
                    /** 单位代码和单位名称校验 */
                    if (StringUtils.isEmpty(rasPosBindingVO.getCorpCode())
                            && StringUtils.isEmpty(rasPosBindingVO.getCorpName())) {
                        errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
                    } else {
                        if (StringUtils.isNotEmpty(rasPosBindingVO.getCorpCode())) {
                            // 根据所属单位代码获取单位信息
                            corpDTO = corpService.getCorpByCode(rasPosBindingVO.getCorpCode());
                            if (corpDTO != null) {
                                if (StringUtils.isNotEmpty(rasPosBindingVO.getCorpName())
                                        && !rasPosBindingVO.getCorpName().equals(corpDTO.getName())) {
                                    errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORPNAME_ISNOT);
                                } else {
                                    rasPosBindingVO.setCorpName(corpDTO.getName());
                                }
                            } else {
                                errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE__CORPCODE_ERROR);
                            }
                        } else {
                            // 只填单位名称
                            corpDTO = corpService.getCorpIdByName(rasPosBindingVO.getCorpName());
                            if (corpDTO == null) {
                                errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORPID_ISNOTEXIST);
                            } else {
                                rasPosBindingVO.setCorpCode(corpDTO.getCode());
                            }
                        }
                    }
                    /** 获取corpId */
                    if (corpDTO != null) {
                        rasPosBindingVO.setCorpId(corpDTO.getId());
                    }
                    //单位不为空检测门店信息
                    if (StringUtils.isNotEmpty(rasPosBindingVO.getCorpId())) {
                        /** 门店编码和门店名称校验 */
                        if (StringUtils.isEmpty(rasPosBindingVO.getShopCode())
                                && StringUtils.isEmpty(rasPosBindingVO.getShopName())) {
                            errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_CODEANDNAME_NOTNULL);
                        } else {
                            if (StringUtils.isNotEmpty(rasPosBindingVO.getShopCode())) {
                                // 根据门店编码和单位id获取门店信息
                                rasShopVO = rasShopService.getRasShopByCode(rasPosBindingVO.getShopCode(), rasPosBindingVO.getCorpId());
                                if (rasShopVO != null) {
                                    //判断门店是否启用
                                    if (RasConstant.RAS_VALID_SIGN_1.equals(rasShopVO.getvSign())) {
                                        if (StringUtils.isNotEmpty(rasPosBindingVO.getShopName())
                                                && !rasPosBindingVO.getShopName().equals(rasShopVO.getName())) {
                                            errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_CODNAME_ISNOT);
                                        } else {
                                            rasPosBindingVO.setShopName(rasShopVO.getName());
                                        }
                                    } else {
                                        errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_VALIDSIGN_ISNOT);
                                    }
                                } else {
                                    errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPCODE_ERROR);
                                }
                            } else {
                                // 只填名门店名称
                                // 根据门店名称获取门店信息
                                rasShopVO = rasShopService.getRasShopByName(rasPosBindingVO.getShopName(), rasPosBindingVO.getCorpId());
                                if (rasShopVO == null) {
                                    errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPNAME_ISNOTEXIST);
                                } else {
                                    rasPosBindingVO.setShopCode(rasShopVO.getCode());
                                }
                            }
                        }
                        /** 获取门店Id */
                        if (rasShopVO != null) {
                            rasPosBindingVO.setShopId(rasShopVO.getId());
                        }
                    }


                    /** 导入数据终端号重复校验*/
                    if (StringUtils.isNotEmpty(rasPosBindingVO.getPosNo())) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("posNo", rasPosBindingVO.getPosNo());
                        if (posNo.contains(rasPosBindingVO.getPosNo())) {
                            errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_POSNO_NOTREPETITION);
                            //校验数据库中是否存在
                        } else if (rasPosBindingDao.checkPosBinding(params, null)) {
                            errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_POSNO_REPEAT);
                        } else {
                            posNo.add(rasPosBindingVO.getPosNo());
                        }
                    } else {
                        //终端号不能为空
                        errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_POSNO_NOTNULL);
                    }
                    rasPosBindingVO.addErrorMsg(errorMsgList);
                }
                //this.inject(list);
            }
        }
    }


    /**
     * 根据ids批量修改POS机绑定表数据状态
     *
     * @param ids       POS机绑定表ID
     * @param statusNew 修改为对的处理状态值（状态:01正常02待确认03已借出）
     * @param statusOld 根据对应旧状态处理状态值修改（状态:01正常02待确认03已借出）
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] ids, String statusNew, String statusOld, ICurrentUser userVo) {
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
        int num = rasPosBindingDao.batchUpdate(params);
        return num;
    }

    /**
     * 根据提交参数生成RasPosBinding pos机绑定对象
     *
     * @param rasPosBindingVO 页面传递信息
     * @param userVo          用户信息
     * @return RasPosBinding
     */
    protected RasPosBinding getRasPosBinding(RasPosBindingVO rasPosBindingVO, ICurrentUser userVo) {
        RasPosBinding entity = new RasPosBinding();
        if (StringUtils.isNotEmpty(rasPosBindingVO.getId())) {
            entity = rasPosBindingDao.get(rasPosBindingVO.getId());
            // 修改人名称
            entity.setUpdateName(userVo.getUsername());
            // 修改人登录名称
            entity.setUpdateBy(userVo.getLoginName());
            // 修改日期
            entity.setUpdateDate(new Date());
        } else {
            // 创建人名称
            entity.setCreateName(userVo.getUsername());
            // 创建人登录名称
            entity.setCreateBy(userVo.getLoginName());
            // 创建日期
            entity.setCreateDate(new Date());
        }
        entity.setCorpId(rasPosBindingVO.getCorpId());
        entity.setShopId(rasPosBindingVO.getShopId());
        entity.setPosNo(rasPosBindingVO.getPosNo());
        entity.setRmk(rasPosBindingVO.getRmk());
        //正常
        entity.setStatus(RasConstant.RAS_POS_BD_STATUS_01);
        //启用
        entity.setValidSign(RasConstant.RAS_VALID_SIGN_1);
        return entity;
    }


}