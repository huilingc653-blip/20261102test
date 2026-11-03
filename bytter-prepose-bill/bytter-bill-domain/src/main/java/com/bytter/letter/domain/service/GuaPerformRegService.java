package com.bytter.letter.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.dao.GuaPerformRegDao;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.entity.GuaPerformReg;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.letter.domain.vo.GuaPerformRegVO;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPerformRegService
 * @Description 履行登记service
 * @date 2019年1月7日
 */
@Service("guaPerformRegService")
@Transactional
public class GuaPerformRegService extends BaseService<GuaPerformReg, String> {
    @Autowired
    GuaPerformRegDao guaPerformRegDao;
    @Autowired
    GuaGuaranteeReceiveService guaGuaranteeReceiveService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 保函领取列表
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_INFO_REGISTER, user.getId());
        return guaPerformRegDao.getDataTablesUnion(dataTablesInput, corpAuth);
    }
    /**
     * 通过保函id查询相应的保函信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaGuaranteeInfoVO getGuaGuaranteeInfoById(String id) {
        return guaPerformRegDao.getGuaGuaranteeInfoById(id);
    }
    /**
     * 保存保函履行登记的相关信息
     *
     * @param guaPerformReg
     * @param user
     */
    public void saveGuaPerformReg(GuaPerformReg guaPerformReg, ICurrentUser user) {
        if (guaPerformReg != null) {
            GuaPerformReg entity = new GuaPerformReg();
            BeanUtils.copyNotNullProperties(guaPerformReg, entity);
            entity.setCreateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());
            this.saveOrUpdate(entity);
            // 修改保函的状态为"履行完成"
            GuaGuaranteeInfo info = new GuaGuaranteeInfo();
            GuaGuaranteeInfoVO vo = guaPerformRegDao.getGuaGuaranteeInfoById(guaPerformReg.getGuaraId());
            vo.setBankName(vo.getOpenCorpName());
            BeanUtils.copyNotNullProperties(vo, info);
            info.setStatus(LetterConstant.GUARANTEE_REGISTERED);
            info.setUpdateBy(user.getId());
            info.setUpdateDate(DateUtils.getDate());
            guaGuaranteeReceiveService.saveOrUpdate(info);
        }
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(GuaPerformReg entity) {
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
    public void save(GuaPerformReg entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(GuaPerformReg entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(GuaPerformReg entity) {
        super.saveOrUpdate(entity);
        this.getSession().flush();
    }
    /**
     * 根据id查询履约信息
     *
     * @return
     */
    public GuaPerformRegVO getRegVOById(String id) {
        GuaPerformRegVO regVO = guaPerformRegDao.getGuaPerformRegByGuaId(id);
        return regVO;
    }
    @Override
    public BaseDao<GuaPerformReg, String> getEntityDao() {
        return guaPerformRegDao;
    }
}
