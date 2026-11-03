package com.bytter.guarantee.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaPledgeOccupyDao;
import com.bytter.guarantee.domain.entity.GuaPledgeInfo;
import com.bytter.guarantee.domain.entity.GuaPledgeOccupy;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyVO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxy
 * @Title: GuaPledgeOccupyService
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用服务层
 * @date 2020/7/7 9:45
 */
@Service("guaPledgeOccupyService")
public class GuaPledgeOccupyService extends BaseService<GuaPledgeOccupy, String> {
    @Autowired
    private GuaPledgeOccupyDao pledgeOccupyDao;
    @Autowired
    private GuaPledgeInfoService infoService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<GuaPledgeOccupy, String> getEntityDao() {
        return pledgeOccupyDao;
    }

    /**
     * 保存抵质押物占用信息
     *
     * @param list
     * @param status
     * @param sysCode
     */
    public void savePledgeOccupy(List<GuaPledgeOccupyDTO> list, Integer status, String sysCode) {
        List<GuaPledgeOccupy> occupyList = BeanUtils.copyListProperties(list, GuaPledgeOccupy.class);
        if (!(sysCode.equals(ConfigConstant.GUACONTRACT) || sysCode.equals(ConfigConstant.EXTGUACONTRACT))) {// 除担保业务外需对抵质押额度进行处理
            if (status.equals(GuaranteeConstant.GUA_PLEDGE_FROZEN)) {
                for (GuaPledgeOccupy occupy : occupyList) {
                    GuaPledgeInfo info = infoService.get(occupy.getPledgeId());
                    if (info.getFreezeMoney() != null && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        // 华南城多次抵押改造
                        this.setAddFrozenMoney(info, occupy);
                    } else {
                        info.setFreezeMoney(occupy.getOccupyMoney());// 抵质押物冻结额度增加
                    }
                    infoService.saveOrUpdate(info);
                }
            } else if (status.equals(GuaranteeConstant.GUA_PLEDGE_OCCUPY)) {
                for (GuaPledgeOccupy occupy : occupyList) {
                    GuaPledgeInfo info = infoService.get(occupy.getPledgeId());
                    if (info.getFreezeMoney() != null && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        // 华南城多次抵押改造
                        this.setSubFrozenMoney(info, occupy);
                    }
                    if (info.getOccupyMoney() != null && (info.getOccupyMoney().compareTo(BigDecimal.ZERO) == 1)) {// 抵质押物占用额度增加
                        // 华南城多次抵押改造
                        this.setAddOccupyMoney(info, occupy);
                    } else {
                        info.setOccupyMoney(occupy.getOccupyMoney());
                    }
                    infoService.saveOrUpdate(info);
                }
            }
        }
        for (GuaPledgeOccupy pledgeOccupy : occupyList) {
            pledgeOccupy.setStatus(status);
        }
        this.batchSaveOrUpdate(occupyList);// 更新占用表状态
        getEntityDao().flush();
    }

    /**
     * 根据业务id和业务编号释放抵质押物使用信息
     *
     * @param businessId
     * @param itemNo
     */
    public void releasePledgeOccupy(String businessId, String itemNo) {
        List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyDao.getOccupyList(businessId, itemNo);
        if (!EmptyUtils.isEmptyList(occupyDTOS)) {
            List<GuaPledgeOccupy> occupyList = BeanUtils.copyListProperties(occupyDTOS, GuaPledgeOccupy.class);
            if (occupyList.get(0).getStatus().equals(GuaranteeConstant.GUA_PLEDGE_FROZEN)) {// 原状态为冻结或暂存
                for (GuaPledgeOccupy occupy : occupyList) {
                    GuaPledgeInfo info = infoService.get(occupy.getPledgeId());
                    if (info.getFreezeMoney() != null && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        // 华南城多次抵押改造
                        this.setSubFrozenMoney(info, occupy);
                    }
                    infoService.saveOrUpdate(info);
                    this.delete(occupy.getId());// 删除抵质押占用信息
                }
            } else if (occupyList.get(0).getStatus().equals(GuaranteeConstant.GUA_PLEDGE_OCCUPY)) {// 原状态为占用
                for (GuaPledgeOccupy occupy : occupyList) {
                    occupy.setStatus(GuaranteeConstant.GUA_PLEDGE_RELEASE);
                    GuaPledgeInfo info = infoService.get(occupy.getPledgeId());
                    if (info.getOccupyMoney() != null && (info.getOccupyMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        // 华南城多次抵押改造
                        this.setSubOccupyMoney(info, occupy);
                    }
                    infoService.saveOrUpdate(info);
                }
                this.batchSaveOrUpdate(occupyList);
            }
        }
        getEntityDao().flush();
    }

    /**
     * 打回抵质押物占用信息
     *
     * @param businessId
     * @param itemNo
     */
    public void backPledgeOccupy(String businessId, String itemNo) {
        List<GuaPledgeOccupyDTO> occupyDTOS = this.getOccupyList(businessId, itemNo);
        if (!EmptyUtils.isEmptyList(occupyDTOS)) {
            List<GuaPledgeOccupy> occupyList = BeanUtils.copyListProperties(occupyDTOS, GuaPledgeOccupy.class);
            for (GuaPledgeOccupy occupy : occupyList) {
                occupy.setStatus(GuaranteeConstant.GUA_PLEDGE_FROZEN);
                GuaPledgeInfo info = infoService.get(occupy.getPledgeId());
                if (info.getOccupyMoney() != null && (info.getOccupyMoney().compareTo(BigDecimal.ZERO) == 1)) {// 占用额度减少
                    // 华南城多次抵押改造
                    this.setSubOccupyMoney(info, occupy);
                }
                if (info.getFreezeMoney() != null && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                    // 华南城多次抵押改造
                    this.setAddFrozenMoney(info, occupy);
                } else {
                    info.setFreezeMoney(occupy.getOccupyMoney());
                }
                infoService.saveOrUpdate(info);
            }
            this.batchSaveOrUpdate(occupyList);
        }
    }

    /**
     * 查询抵质押物使用信息
     *
     * @param businessId
     * @param itemNo
     * @return
     */
    public List<GuaPledgeOccupyDTO> getOccupyList(String businessId, String itemNo) {
        return pledgeOccupyDao.getOccupyList(businessId, itemNo);
    }

    /**
     * 删除抵质押物使用信息
     *
     * @param businessId
     * @param itemNo
     */
    public void deletePledgeOccupy(String businessId, String itemNo) {
        List<GuaPledgeOccupyDTO> oldOccupyDTOS = this.getOccupyList(businessId, itemNo);
        if (EmptyUtils.isNotEmpty(oldOccupyDTOS) && oldOccupyDTOS.size() > 0) {// 如果之前存在占用信息，先进行删除操作
            String sysCode = oldOccupyDTOS.get(0).getSysCode();
            List<GuaPledgeOccupy> occupyList = BeanUtils.copyListProperties(oldOccupyDTOS, GuaPledgeOccupy.class);
            if (!(sysCode.equals(ConfigConstant.GUACONTRACT) || sysCode.equals(ConfigConstant.EXTGUACONTRACT))) {
                for (GuaPledgeOccupy occupy : occupyList) {
                    GuaPledgeInfo info = infoService.get(occupy.getPledgeId());
                    if (info.getFreezeMoney() != null && (info.getFreezeMoney().compareTo(BigDecimal.ZERO) == 1)) {
                        // 华南城多次抵押改造
                        this.setSubFrozenMoney(info, occupy);
                    }
                    infoService.saveOrUpdate(info);
                    this.delete(occupy.getId());// 删除抵质押占用信息
                }
            } else {
                for (GuaPledgeOccupy occupy : occupyList) {
                    this.delete(occupy.getId());// 删除抵质押占用信息
                }
            }
        }
    }

    /**
     * 转换数据
     *
     * @param str
     * @return
     */
    public List<GuaPledgeOccupyDTO> convertPledgeInfoList(String str) {
        if (StringUtils.isNotEmpty(str)) {
            List<GuaPledgeOccupyDTO> occupyList = new ArrayList<GuaPledgeOccupyDTO>();
            // 去掉最后一个分号
            String strIds = str.substring(0, str.length() - 1);
            String[] idAndMoney = strIds.split(";");
            for (String ids : idAndMoney) {
                String[] tempStr = ids.split(",");
                GuaPledgeInfoVO info = infoService.getGuaPledgeInfoVOById(tempStr[0]);
                GuaPledgeOccupyDTO occupyDTO = new GuaPledgeOccupyDTO();
                BeanUtils.copyNotNullProperties(info, occupyDTO);
                occupyDTO.setOccupyMoney(new BigDecimal(tempStr[1]));
                occupyList.add(occupyDTO);
            }
            if (occupyList != null && occupyList.size() > 0) {
                return occupyList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 根据抵质押物id获取抵质押物占用数据
     *
     * @param pledgeId
     * @return
     */
    public List<GuaPledgeOccupyVO> getPledgeOccupyList(String pledgeId) {
        return this.getPledgeOccupyList(pledgeId, null, String.valueOf(GuaranteeConstant.GUA_PLEDGE_OCCUPY));
    }

    /**
     * 根据抵质押物id获取抵质押物冻结数据
     *
     * @param pledgeId
     * @return
     */
    public List<GuaPledgeOccupyVO> getPledgeOccupyListFrozen(String pledgeId) {
        return this.getPledgeOccupyList(pledgeId, null, String.valueOf(GuaranteeConstant.GUA_PLEDGE_FROZEN));
    }

    /***
     * 重载
     *
     * @param pledgeId      抵质押物id
     * @param businessId    业务id
     * @param status        状态
     * @return
     */
    public List<GuaPledgeOccupyVO> getPledgeOccupyList(String pledgeId, String businessId, String status) {
        List<GuaPledgeOccupyVO> list = pledgeOccupyDao.getPledgeOccupyList(pledgeId, businessId, status);
        // 字典：抵质押物占用业务类型
        Map<String, DictDTO> listDictMap = dictService.listMap("pledge.busType");
        list.forEach(item -> {
            Optional.ofNullable(item.getSysCode()).ifPresent(sysCode -> {
                Optional.ofNullable(listDictMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sysCode)))
                        .ifPresent(dict -> item.setBusinessName(dict.getName()));
            });
        });
        return list;
    }

    /**
     * 根据业务id和抵质押物id更新抵质押物状态
     *
     * @param businessId
     * @param pledgeId
     */
    public void updatePledgeOccupy(String businessId, String pledgeId) {
        List<GuaPledgeOccupyVO> occupyVOS = pledgeOccupyDao.getPledgeOccupyList(pledgeId, businessId, String.valueOf(GuaranteeConstant.GUA_PLEDGE_OCCUPY));
        if (!EmptyUtils.isEmptyList(occupyVOS)) {
            List<GuaPledgeOccupy> occupyList = BeanUtils.copyListProperties(occupyVOS, GuaPledgeOccupy.class);
            if (occupyList.get(0).getStatus().equals(GuaranteeConstant.GUA_PLEDGE_OCCUPY)) {// 原状态为占用
                for (GuaPledgeOccupy occupy : occupyList) {
                    occupy.setStatus(GuaranteeConstant.GUA_PLEDGE_RELEASE);
                }
                this.batchSaveOrUpdate(occupyList);
            }
        }
    }

    /**
     * 华南城多次抵押改造 设置冻结额度
     *
     * @param info
     * @param occupy
     */
    private void setAddFrozenMoney(GuaPledgeInfo info, GuaPledgeOccupy occupy) {
        // 华南城多次抵押改造
        if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
            info.setFreezeMoney(info.getPledgeMoney());// 抵质押物冻结额度增加
        } else {
            info.setFreezeMoney(info.getFreezeMoney().add(occupy.getOccupyMoney()));// 抵质押物冻结额度增加
        }
    }

    /**
     * 设置占用额度
     *
     * @param info
     * @param occupy
     */
    private void setAddOccupyMoney(GuaPledgeInfo info, GuaPledgeOccupy occupy) {
        // 华南城多次抵押改造
        if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
            info.setOccupyMoney(info.getPledgeMoney());// 抵质押物占用额度增加
        } else {
            info.setOccupyMoney(info.getOccupyMoney().add(occupy.getOccupyMoney()));// 抵质押物占用额度增加
        }
    }

    /**
     * 华南城多次抵押改造 设置冻结额度
     *
     * @param info
     * @param occupy
     */
    private void setSubFrozenMoney(GuaPledgeInfo info, GuaPledgeOccupy occupy) {
        // 华南城多次抵押改造
        if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
            List<GuaPledgeOccupyVO> guaPledgeOccupyVOS = getPledgeOccupyListFrozen(info.getId());
            if (guaPledgeOccupyVOS.size() <= 1) {
                info.setFreezeMoney(BigDecimal.ZERO);// 抵质押物冻结额度减少
            }
        } else {
            info.setFreezeMoney(info.getFreezeMoney().subtract(occupy.getOccupyMoney()));// 抵质押物冻结额度减少
        }
    }

    /**
     * 设置占用额度
     *
     * @param info
     * @param occupy
     */
    private void setSubOccupyMoney(GuaPledgeInfo info, GuaPledgeOccupy occupy) {
        // 华南城多次抵押改造
        if (GuaranteeConstant.GUA_TYPE_DY.equals(info.getPledgeAttr())) {
            List<GuaPledgeOccupyVO> guaPledgeOccupyVOS = getPledgeOccupyList(info.getId());
            if (guaPledgeOccupyVOS.size() <= 1) {
                info.setOccupyMoney(BigDecimal.ZERO);// 抵质押物占用额度减少
            }
        } else {
            info.setOccupyMoney(info.getOccupyMoney().subtract(occupy.getOccupyMoney()));// 抵质押物占用额度减少
        }
    }
}
