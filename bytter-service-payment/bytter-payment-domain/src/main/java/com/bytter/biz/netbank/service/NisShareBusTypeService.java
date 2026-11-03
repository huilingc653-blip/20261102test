package com.bytter.biz.netbank.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.dao.BisPayPolicyBusTypeDao;
import com.bytter.biz.netbank.dao.NisBusTypeDao;
import com.bytter.biz.netbank.dao.NisShareBusTypeDao;
import com.bytter.biz.netbank.entity.BisPayPolicyBusType;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.entity.NisShareBusType;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.netbank.vo.NisShareBusTypeVO;
import com.bytter.biz.payment.api.dto.NisShareBusTypeDTO;
import com.bytter.biz.payment.api.service.INisShareBusTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 共享业务类型维护业务层
 *
 * @author fangcf
 * @date 2025-05-15
 */
@Service("nisShareBusTypeService")
@Transactional
@DubboService
public class NisShareBusTypeService extends BaseService<NisShareBusType, String> implements INisShareBusTypeService {

    @Autowired
    private NisShareBusTypeDao nisShareBusTypeDao;

    @Autowired
    private NisBusTypeDao nisBusTypeDao;

    @Autowired
    private BisPayPolicyBusTypeDao bisPayPolicyBusTypeDao;

    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<NisShareBusType, String> getEntityDao() {
        return nisShareBusTypeDao;
    }

    /**
     * 共享业务类型列表
     *
     * @param input 入参
     * @return 共享业务类型数据
     */
    public DataTablesOutput<NisShareBusTypeVO> getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<NisShareBusTypeVO> output = nisShareBusTypeDao.getDataTablesUnion(input);
        List<NisShareBusTypeVO> resultData = output.getData();
        resultData.forEach(item -> {
            // 关联业务类别列表显示
            item.setBusTypeNameShow("(" + item.getBusType() + ")" + item.getBusTypeName());
            // 状态字典
            item.setStatusName(dictService.get("status_yn", item.getStatus()));
        });
        return output;
    }

    /**
     * 新增共享业务类型
     *
     * @param vo   form表单
     * @param user 用户对象
     */
    public void saveShareBusType(NisShareBusTypeVO vo, ICurrentUser user) {
        // 新增前判断共享业务类型是否已存在
        List<NisShareBusType> list = nisShareBusTypeDao.listByShareBusType(vo.getShareBusType(), null);
        if (!CollectionUtils.isEmpty(list)) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_SHAREBUSTYPE_EXIST, vo.getShareBusType());
        }
        NisShareBusType entity = new NisShareBusType();
        BeanUtils.copyNotNullProperties(vo, entity);
        entity.setCreateBy(user.getId());
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
    }

    /**
     * 修改共享业务类型
     *
     * @param vo   form表单
     * @param user 用户对象
     */
    public void updateShareBusType(NisShareBusTypeVO vo, ICurrentUser user) {
        // 获取原数据
        NisShareBusType entity = this.get(vo.getId(), true);
        if (CommonConstant.VALID_SIGN_NO.equals(vo.getStatus())) {
            // 1、付款通知表是否已使用
            List<NisBillhead> nisBillHeadList = nisShareBusTypeDao.listByShareBusTypeId(vo.getId());
            if (!CollectionUtils.isEmpty(nisBillHeadList)) {
                // 不为空，则已使用
                throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_SHAREBUSTYPENAME_USE, entity.getShareBusTypeName());
            }
            // 2、自动支付策略是否已使用：跟据策略ID获取业务类别信息集合
            List<BisPayPolicyBusType> shareBusTypeList = bisPayPolicyBusTypeDao.listByShareBusTypeId(vo.getId());
            if (!CollectionUtils.isEmpty(shareBusTypeList)) {
                // 不为空，则已使用
                throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_SHAREBUSTYPENAME_USE, entity.getShareBusTypeName());
            }
        }
        // 更新新数据
        BeanUtils.copyNotNullProperties(vo, entity);
        entity.setUpdateBy(user.getId());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }

    /**
     * 删除共享业务类型
     *
     * @param id 主键id
     */
    public void deleteShareBusType(String id) {
        NisShareBusType entity = this.get(id, true);
        if (!ObjectUtils.isEmpty(entity)) {
            // 判断该共享类型是否已使用
            // 1、付款通知表是否已使用
            List<NisBillhead> nisBillHeadList = nisShareBusTypeDao.listByShareBusTypeId(id);
            if (!CollectionUtils.isEmpty(nisBillHeadList)) {
                // 不为空，则已使用
                throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_SHAREBUSTYPENAME_USE, entity.getShareBusTypeName());
            }
            // 2、自动支付策略是否已使用：跟据策略ID获取业务类别信息集合
            List<BisPayPolicyBusType> shareBusTypeList = bisPayPolicyBusTypeDao.listByShareBusTypeId(id);
            if (!CollectionUtils.isEmpty(shareBusTypeList)) {
                // 不为空，则已使用
                throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_SHAREBUSTYPENAME_USE, entity.getShareBusTypeName());
            }
            this.deleteEntity(entity);
        }
    }

    /**
     * 获取“收付款标志=付款，有效标志=有效”的业务种类
     *
     * @param cdSign 收付款标志
     * @return 业务类型数据集合
     */
    public List<NisBusTypeVO> listValidBusTypeByCdSign(String cdSign) {
        List<NisBusTypeVO> returnList = nisBusTypeDao.listNisBusType(CommonConstant.VALID_SIGN_YES, cdSign);
        if (!CollectionUtils.isEmpty(returnList)) {
            returnList.forEach(item -> item.setBusTypeNameShow("(" + item.getBusType() + ")" + item.getBusTypeName()));
        }
        return returnList;
    }

    /**
     * 根据业务类别获取所有正常的共享业务类型
     *
     * @param busTypeId 业务类型id
     * @return 共享业务类型集合
     */
    public List<NisShareBusType> listValidShareBusTypeByBusType(String busTypeId) {
        if (StringUtils.isEmpty(busTypeId)) {
            return Lists.newArrayList();
        }
        List<NisShareBusType> returnList = nisShareBusTypeDao.listValidShareBusTypeByBusType(busTypeId, null);
        if (!CollectionUtils.isEmpty(returnList)) {
            returnList.forEach(item -> item.setShareBusTypeName("(" + item.getShareBusType() + ")" + item.getShareBusTypeName()));
        }
        return returnList;
    }

    /**
     * 根据条件获取正常的共享业务类型
     *
     * @param busType      业务类型编码
     * @param shareBusType 共享业务类型编码
     * @return 收款单据DTO
     */
    @Override
    public List<NisShareBusTypeDTO> listValidShareBusType(String busType, String shareBusType) {
        String busTypeId = "";
        if (StringUtils.isNotEmpty(busType)) {
            NisBusTypeVO busTypeVo = nisBusTypeDao.getByBusType(busType);
            if (!ObjectUtils.isEmpty(busTypeVo)) {
                busTypeId = busTypeVo.getId();
            }
        }
        List<NisShareBusType> returnList = nisShareBusTypeDao.listValidShareBusTypeByBusType(busTypeId, shareBusType);
        return BeanUtils.copyListProperties(returnList, NisShareBusTypeDTO.class);
    }
}
