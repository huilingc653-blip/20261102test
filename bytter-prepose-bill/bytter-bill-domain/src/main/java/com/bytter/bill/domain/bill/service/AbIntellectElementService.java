package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbIntellectElementDao;
import com.bytter.bill.domain.bill.entity.AbIntellectElement;
import com.bytter.bill.domain.bill.vo.AbIntellectElementVO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictGroupDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
/**
 * author: liusy
 * date: 2022/10/20 10:30
 * description: 选票要素服务类
 */
@Service
public class AbIntellectElementService extends BaseService<AbIntellectElement, String> {
    @Autowired
    private AbIntellectElementDao abIntellectElementDao;
    @DubboReference(check = false)
    private IDictService iDictService;
    /**
     * 要素列表查询
     * @param input
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input) {
        return abIntellectElementDao.datatable(input);
    }
    /**
     * 查询所有有效选票要素
     * @return
     */
    public List<AbIntellectElementVO> listElement() {
        return abIntellectElementDao.listElement();
    }
    /**
     * 保存选票要素
     * @param abIntellectElementVO
     */
    public void saveElement(AbIntellectElementVO abIntellectElementVO, ICurrentUser user) {
        verifyCodeAndName(abIntellectElementVO);
        AbIntellectElement abIntellectElement = BeanUtils.copyNewInstanceProperties(abIntellectElementVO, AbIntellectElement.class);
        abIntellectElement.setCreateBy(user.getId());
        abIntellectElement.setCreateName(user.getUsername());
        abIntellectElement.setCreateDate(new Date());
        this.saveEntity(abIntellectElement);
    }
    /**
     * 更新选票要素
     * @param abIntellectElementVO
     * @param user
     */
    public void updateElement(AbIntellectElementVO abIntellectElementVO, ICurrentUser user) {
        verifyCodeAndName(abIntellectElementVO);
        AbIntellectElement abIntellectElement = BeanUtils.copyNewInstanceProperties(abIntellectElementVO, AbIntellectElement.class);
        abIntellectElement.setUpdateBy(user.getId());
        abIntellectElement.setUpdateName(user.getUsername());
        abIntellectElement.setUpdateDate(new Date());
        AbIntellectElementVO oldVo = abIntellectElementDao.getElementById(abIntellectElementVO.getId());
        DictGroupDTO dictGroupDTO= iDictService.get(oldVo.getDictGroup());
        oldVo.setDictGroupName(dictGroupDTO.getGroupName());
        if(oldVo != null){
            abIntellectElement.setCreateBy(oldVo.getCreateBy());
            abIntellectElement.setCreateDate(oldVo.getCreateDate());
            abIntellectElement.setCreateName(oldVo.getCreateName());
        }
        this.updateEntity(abIntellectElement);
    }
    /**
     * 校验要素编码、名称是否已存在
     *
     * @param abIntellectElementVO 规则vo
     */
    public void verifyCodeAndName(AbIntellectElementVO abIntellectElementVO) {
        AbIntellectElementVO elementVO = abIntellectElementDao.queryByElementCode(abIntellectElementVO.getId(),
                abIntellectElementVO.getElementCode());
        if (elementVO != null) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_ELEMENT_CODE_IS_EXISTS);
        }
        elementVO = abIntellectElementDao.queryByElementName(abIntellectElementVO.getId(),
                abIntellectElementVO.getElementName());
        if (elementVO != null) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_ELEMENT_NAME_IS_EXISTS);
        }
    }
    /**
     * 删除
     * @param id
     */
    public void deleteElement(String id){
        // 判断是否被引用
        if (abIntellectElementDao.isQuote(id)) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_ELEMENT_IS_QUOTE);
        }
        abIntellectElementDao.delete(id);
    }
    /**
     * 通过id查询AbIntellectElementVO
     * @param id
     * @return
     */
    public AbIntellectElementVO getAbIntellectElementVOById(String id){
        AbIntellectElementVO   oldVo =abIntellectElementDao.getElementById(id);
        DictGroupDTO dictGroupDTO= iDictService.get(oldVo.getDictGroup());
        oldVo.setDictGroupName(dictGroupDTO.getGroupName());
        return oldVo;
    }
    @Override
    public BaseDao<AbIntellectElement, String> getEntityDao() {
        return abIntellectElementDao;
    }
}
