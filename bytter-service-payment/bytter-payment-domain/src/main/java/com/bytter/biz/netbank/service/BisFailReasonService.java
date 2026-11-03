package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.BisFailReasonDao;
import com.bytter.biz.netbank.entity.BisFailReason;
import com.bytter.biz.netbank.vo.BisFailReasonVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: Dao
 * @Description: 交易失败原因表
 * @author huangx
 * @date 2019-05-17 14:26:13
 * @version 1.0
 *
 */
@Service("bisFailReasonService")
@Transactional
public class BisFailReasonService extends BaseService<BisFailReason, String> {
    @Autowired
    private BisFailReasonDao bisFailReasonDao;
    @DubboReference(check = false)
	private Ii18nService ii18nService;
    @DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
    @Override
    public BaseDao<BisFailReason, String> getEntityDao() {
        return bisFailReasonDao;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisFailReason entity) {
        super.deleteEntity(entity);
        //删除多语言
        ii18nService.deleteBusinessBaseContext(entity.getId());
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BisFailReason entity, String locale) {
        super.saveEntity(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getFailName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisFailReason entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(BisFailReason entity, String locale) {
        super.merge(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getFailName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 根据id获取交易失败原因
     *
     * @param id
     * @return
     */
    public BisFailReason getById(String id) {
        return bisFailReasonDao.findById(id);
    }
    /**
     * 根据编码获取交易失败原因
     *
     * @param failCode
     * @return
     */
    public BisFailReason findByCode(String failCode) {
        return bisFailReasonDao.findByCode(failCode);
    }
    /**
     * 重复校验
     *
     * @param propertyName
     * @param propertyValue
     * @return
     */
    public BisFailReason validByPropertyName(String propertyName, String propertyValue, String id) {
        return bisFailReasonDao.validByPropertyName(propertyName, propertyValue, id);
    }
    /**
     * 关键字重复校验
     *
     * @param keyWord
     * @return
     */
    public Boolean isExsitKeyWord(String keyWord, String id) {
        List<BisFailReason> list = this.bisFailReasonDao.listByKeyWord(keyWord, id);
        if(!EmptyUtils.isEmptyList(list)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 根据银企接口获取交易失败原因
     * @param bifCode
     * @return
     */
    public List<BisFailReasonVO> listBisFailReasonByBifCode(String bifCode){
        return this.bisFailReasonDao.listBisFailReasonByBifCode(bifCode);
    }
    /**
     * 查询交易失败原因
     * @param input
     * @return
     */
    public DataTablesOutput<BisFailReasonVO> getDataTablesUnion(DataTablesInput input){
        DataTablesOutput<BisFailReasonVO> datas = this.bisFailReasonDao.getDataTablesUnion(input);
        if (datas != null && datas.getData() != null) {
            List<BisFailReasonVO> list = datas.getData();
            for (BisFailReasonVO vo : list) {
                if(StringUtils.isNotBlank(vo.getBifCode())) {
                    String[] bifCodes = vo.getBifCode().split(CommonConstant.COMMA_SPLIT);
                    String bifName = "";
                    for(String bifCode : bifCodes) {
                        //设置接口代码名称
                        BisBifInitDTO bisBif = bisBifInitService.getObjectById(bifCode);
                        if (bisBif != null) {
                            bifName += bisBif.getName() + CommonConstant.COMMA_SPLIT;
                        }
                    }
                    if(StringUtils.isNotBlank(bifName)){
                        bifName = bifName.substring(0, bifName.length()-1);
                    }
                    vo.setBifName(bifName);
                }
            }
        }
        return datas;
    }
    /**
     * 根据ID查询交易失败原因
     * @param id
     * @return
     */
    public BisFailReasonVO getByUnion(String id){
        BisFailReasonVO vo = this.bisFailReasonDao.getByUnion(id);
        if(StringUtils.isNotBlank(vo.getBifCode())) {
            String[] bifCodes = vo.getBifCode().split(CommonConstant.COMMA_SPLIT);
            String bifName = "";
            for(String bifCode : bifCodes) {
                //设置接口代码名称
                BisBifInitDTO bisBif = bisBifInitService.getObjectById(bifCode);
                if (bisBif != null) {
                    bifName += bisBif.getName() + CommonConstant.COMMA_SPLIT;
                }
            }
            if(StringUtils.isNotBlank(bifName)){
                bifName = bifName.substring(0, bifName.length()-1);
            }
            vo.setBifName(bifName);
        }
        return vo;
    }
}
