package com.bytter.credit.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatYearFinPlanRegisterDao;
import com.bytter.credit.domain.entity.RatYearFinPlanRegister;
import com.bytter.credit.domain.vo.RatYearFinPlanRegisterVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 授信合作银行维护
 *
 * @author fangcf
 * @date 2024-06-20
 */
@Service("ratYearFinPlanRegisterService")
@Transactional
public class RatYearFinPlanRegisterService extends BaseService<RatYearFinPlanRegister, String> implements IUserExcelVerifyHandler<RatYearFinPlanRegisterVO> {

    @Autowired
    private RatYearFinPlanRegisterDao ratYearFinPlanRegisterDao;
    @DubboReference
    private ICorpService corpService;

    @Override
    public BaseDao<RatYearFinPlanRegister, String> getEntityDao() {
        return ratYearFinPlanRegisterDao;
    }

    /**
     * 查询授信合作银行维护数据
     *
     * @param input 入参对象
     * @return 出参对象
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listOutputData(DataTablesInput input, String userId) {
        // 获取资源权限单位
        List<String> authCorpList = corpService.getUserCorpIds(RationConstant.CMS_RAT_YEARFINPLANREGISTER_QUERY, userId);
        return ratYearFinPlanRegisterDao.listOutputData(input, authCorpList);
    }

    /**
     * 新增年度融资规划登记
     *
     * @param entity 年度融资规划登记对象
     * @param user   用户对象
     */
    public void saveYearFinPlanRegister(RatYearFinPlanRegister entity, ICurrentUser user) {
        // 做了冗余字段，查询单位信息
        CorpDTO corpDTO = corpService.get(entity.getCorpId());
        if (!ObjectUtils.isEmpty(corpDTO)) {
            String corpName = corpDTO.getName();
            entity.setCorpName(corpName);
        }
        // todo 暂时无审批流，默认审批通过
        entity.setStatus(CommonConstant.STATUE_CHECK);
        entity.setCreateBy(user.getId());
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
    }

    /**
     * 删除年度融资规划登记 -- 逻辑删除
     *
     * @param id 年度融资规划登记表主键
     */
    public void deleteYearFinPlanRegister(String id) {
        RatYearFinPlanRegister entity = this.get(id, true);
        if (!ObjectUtils.isEmpty(entity)) {
            entity.setStatus(CommonConstant.STATUE_DELETE);
            this.saveOrUpdate(entity);
        }
    }

    /**
     * 修改年度融资规划登记
     *
     * @param entity 年度融资规划登记对象
     * @param user   用户对象
     */
    public void updateYearFinPlanRegister(RatYearFinPlanRegister entity, ICurrentUser user) {
        // 原数据库实体信息
        RatYearFinPlanRegister oldEntity = this.get(entity.getId(), true);
        if (!ObjectUtils.isEmpty(oldEntity)) {
            // 表单内容复制给原实体
            BeanUtils.copyNotNullProperties(entity, oldEntity);
            // 做了冗余字段，查询单位信息
            CorpDTO corpDTO = corpService.get(entity.getCorpId());
            if (!ObjectUtils.isEmpty(corpDTO)) {
                String corpName = corpDTO.getName();
                oldEntity.setCorpName(corpName);
            }
            oldEntity.setStatus(CommonConstant.STATUE_CHECK);
            oldEntity.setUpdateBy(user.getId());
            oldEntity.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(oldEntity);
        }
    }

    /**
     * 年度融资规划登记导入
     *
     * @param list 导入数据集合
     * @param user 用户对象
     */
    public void saveImport(List<RatYearFinPlanRegister> list, ICurrentUser user) {
        list.forEach(entity -> this.saveYearFinPlanRegister(entity, user));
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(RatYearFinPlanRegisterVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<RatYearFinPlanRegisterVO> list, Object... user) {
        if (!CollectionUtils.isEmpty(list)) {
            String userId = "";
            if (user[0] != null) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                userId = userInfo.getId();
            }
            for (RatYearFinPlanRegisterVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 单位名称
                if (StringUtils.isEmpty(vo.getCorpName())) {
                    errorMsgList.add(RationConstant.CMS_RAT_REGISTERCORPNAME_NULL);
                } else {
                    CorpDTO corp = corpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corp)) {
                        // 单位不存在
                        errorMsgList.add(RationConstant.CMS_RAT_REGISTERCORPNAME_NOTEXIST);
                    } else {
                        // 该用户是否存在单位权限
                        // 获取资源权限单位
                        List<String> authCorpList = corpService.getUserCorpIds(RationConstant.CMS_RAT_YEARFINPLANREGISTER_QUERY, userId);
                        // 判断该单位id是否有资源权限
                        if (CollectionUtils.isEmpty(authCorpList) || !authCorpList.contains(corp.getId())) {
                            // 用户不存在该单位权限
                            errorMsgList.add(RationConstant.CMS_RAT_REGISTERCORPNAME_NOTAUTH);
                        } else {
                            vo.setCorpId(corp.getId());
                        }
                    }
                }
                // 年度
                if (StringUtils.isEmpty(vo.getYears())) {
                    errorMsgList.add(RationConstant.CMS_RAT_REGISTERYEARS_NULL);
                } else {
                    // 1、年度格式校验，格式为yyyy。2、年度日期区间校验，年份应为1900-2099之间
                    String regex = "^(19|20)\\d{2}$";
                    if (!vo.getYears().matches(regex)) {
                        errorMsgList.add(RationConstant.CMS_RAT_REGISTERYEARS_FORMATERROR);
                    }
                }
                // 年采购量
                if (EmptyUtils.isEmpty(vo.getYearPurchaseAmount())) {
                    errorMsgList.add(RationConstant.CMS_RAT_REGISTERYEARPURCHASEAMOUNT_NULL);
                }
                // 并购投资
                if (EmptyUtils.isEmpty(vo.getMergeInvestmentAmount())) {
                    errorMsgList.add(RationConstant.CMS_RAT_REGISTERMERGEINVESTMENTAMOUNT_NULL);
                }
                // 解决措施- 解决措施不为空时，该字段长度不能超过500
                if (EmptyUtils.isNotEmpty(vo.getSolveMeasure())) {
                    if (vo.getSolveMeasure().length() > 500) {
                        errorMsgList.add(RationConstant.CMS_RAT_REGISTERSOLVEMEASURE_LENGTH);
                    }
                }
                // 存在的困难- 存在的困难不为空时，该字段长度不能超过500
                if (EmptyUtils.isNotEmpty(vo.getExistDifficulty())) {
                    if (vo.getExistDifficulty().length() > 500) {
                        errorMsgList.add(RationConstant.CMS_RAT_REGISTEREXISTDIFFICULTY_LENGTH);
                    }
                }
                vo.addErrorMsg(errorMsgList);
            }
        }
    }
}
