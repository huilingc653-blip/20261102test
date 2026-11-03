package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationDisDetailDao;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.dto.RatSysItemDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 授信分配明细表
 * @author onlineGenerator
 * @date 2017-07-31 16:07:49
 * @version 1.0
 *
 */
@Service("ratBkrationDisDetailService")
@Transactional
public class RatBkrationDisDetailService extends BaseService<RatBkrationDisDetail, String> {
    @Autowired
    private RatBkrationDisDetailDao ratBkrationDisDetailDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatBkrationDisDetail entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(RatBkrationDisDetail entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatBkrationDisDetail entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatBkrationDisDetail entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationDisDetail, String> getEntityDao() {
        return ratBkrationDisDetailDao;
    }
    /**
     * 功能介绍 : 分页。<br>
     * <b>日期：</b>2017年10月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput setDisOutputPageData(DataTablesOutput output) {
        List<String> disCodes = new ArrayList<String>();
        List<RatBkrationVO> data = output.getData();
        if (null == data) {
            return output;
        }
        Date nowDate = DateUtils.getDate();// 当前日期
        for (RatBkrationVO bkrationVO : data) {
            if (DateUtils.dayDiffs(bkrationVO.getEndDate(), nowDate) < 0) {
                bkrationVO.setOverdueSign(RationConstant.RAT_COMMON_Y);// 结束日期小于当前日期标识过期
            }
            disCodes.add(bkrationVO.getDisCode());
        }
        List<RatBkRationDtlVm> dtlDTOs = getBkrationDtlByRatCodes(disCodes);
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<String, List<RatBkRationDtlVm>>();
        if (!CollectionUtils.isEmpty(dtlDTOs)) {
            // 授信品种明细按授信编码分组
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationDtlVm> ratList = ratMap.computeIfAbsent(dtlDTO.getRatCode(), k -> new ArrayList<RatBkRationDtlVm>());
                ratList.add(dtlDTO);
            }
        }
        for (RatBkrationVO bkrationVO : data) {
            String disCode = bkrationVO.getDisCode();
            List<RatBkRationDtlVm> ratList = ratMap.get(disCode);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            // 综合品种条数
            Integer compItemSize = Integer.valueOf(0);
            boolean compFlag = false;
            // Integer itemOrder = Integer.valueOf(0);
            for (RatBkRationDtlVm dtlDTO : ratList) {
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singItemSize++;
                } else {
                    if (!compFlag) {// 没有设置过，则设置第一个品种的综合标识
                        dtlDTO.setFirstComp("1");
                        compFlag = true;
                    }
                    compItemSize++;
                }
                // 分配显示额度 = 分配额度+被调剂额度-调剂额度
                dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()));
            }
            bkrationVO.setItemSize(ratList.size());
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setCompItemSize(compItemSize);// 综合品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }
    /**
     * 功能介绍 : 获取银行授信已分配授信品种对应的系统品种。<br>
     * <b>日期：</b>2017年10月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatSysItemDTO> getRationDisSysItem(String disCode) {
        return ratBkrationDisDetailDao.getRationDisSysItem(disCode);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationDtlVm> getBkrationDtlByRatCodes(List<String> disCodes) {
        return ratBkrationDisDetailDao.getBkrationDtlByRatCodes(disCodes);
    }
    /**
     * 功能介绍 : 保存授信分配品种明细金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRatBkrationDisDetail(RatBkrationDisDetail detail) {
        Class userCla = (Class) detail.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(detail);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(detail, BigDecimal.ZERO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.save(detail);
    }
    /**
     * 功能介绍 : 删除原授信分配明细。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteBkrationDisDtlByDisCode(RatBkrationDisListDTO disDTO) {
        ratBkrationDisDetailDao.deleteBkrationDisDtlByDisCode(disDTO);
    }
    /**
     * 功能介绍 : 更新原授信分配中追加额度。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlDisMoney(RatBkrationDisDtlDTO dto) {
        ratBkrationDisDetailDao.updateRationDtlDisMoney(dto);
    }
    /**
     * 功能介绍 : 更新原授信分配中冻结额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisFreezeMoney(RatBkrationDisDtlDTO dto) {
        ratBkrationDisDetailDao.updateRationDisFreezeMoney(dto);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDtlDTO> getBkrationDisDtlByRatCodes(List<String> disCodes) {
        return ratBkrationDisDetailDao.getBkrationDisDtlByRatCodes(disCodes);
    }
    /**
     * 功能介绍 : 获取总额共享下授信单的所有授信分配明细。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDtlDTO> getAllShareCorpRationDisDtl(String ratCode) {
        return ratBkrationDisDetailDao.getAllShareCorpRationDisDtl(ratCode);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已分配的授信品种额度。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByItemCode(String ratCode, String corpId, String itemFlag,
            String itemCode) {
        return ratBkrationDisDetailDao.getRationDisDtlByItemCode(ratCode, corpId, itemFlag, itemCode);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDtlDTO> getRationDisDtlByRatCodes(String disCode, String itemFlag, String itemCode) {
        return ratBkrationDisDetailDao.getRationDisDtlByRatCodes(disCode, itemFlag, itemCode);
    }
    /**
     * 功能介绍 : 获取总额共享下其他授信分配单位的同品种额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDtlDTO> getAmountShareRationDisDtl(String ratCode, String disCode, String itemFlag,
            String itemCode, String corpId) {
        return ratBkrationDisDetailDao.getAmountShareRationDisDtl(ratCode, disCode, itemFlag, itemCode, corpId);
    }
    /**
     * 功能介绍 : 更新原授信分配中最高额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisMaxMoney(RatBkrationDisDtlDTO dto) {
        ratBkrationDisDetailDao.updateRationDisMaxMoney(dto);
    }
    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDetail> getRationDisDtlByDisCode(String disCode, String itemFlag, String itemCode) {
        return ratBkrationDisDetailDao.getRationDisDtlByDisCode(disCode, itemFlag, itemCode);
    }
    /**
     * 功能介绍 : 获取总额共享下其他单位的品种分配详情。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDetail> getOtherRationDisDtlByDisCode(String ratCode, String disCode, String itemFlag,
            String itemCode) {
        return ratBkrationDisDetailDao.getOtherRationDisDtlByDisCode(ratCode, disCode, itemFlag, itemCode);
    }
    /**
     * 功能介绍 : 根据授信信息获取单位已分配的授信明细集合。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDisDtlDTO> getRationDisDtlByCorpItem(String ratCode, String corpId, String itemFlag,
            String itemCode) {
        return ratBkrationDisDetailDao.getRationDisDtlByCorpItem(ratCode, corpId, itemFlag, itemCode);
    }
    /**
     * 功能介绍 : 根据授信ID获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2021年12月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkRationDtlVm> getBkrationDtlByRatCode(String ratCode) {
        return ratBkrationDisDetailDao.getBkrationDtlByRatCode(ratCode);
    }
    /**
     * <br>
     * <b>功能：</b> 更新授信分配明细占用和在途金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/20 0020 <br>
     */
    public void updateRationDisDetail(RatUrationDisBean disBean) {
        ratBkrationDisDetailDao.updateRationDisDetail(disBean);
    }
    /**
     * <br>
     * <b>功能：</b> 更新综合授信分配明细占用和在途金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/20 0020 <br>
     */
    public void updateCompRationDisDetail(RatUrationUseDtlVO disBean) {
        ratBkrationDisDetailDao.updateFirstCompRationDisDetail(disBean);
    }
}
