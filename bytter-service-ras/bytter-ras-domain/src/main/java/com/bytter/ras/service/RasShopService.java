package com.bytter.ras.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.system.api.dto.DictDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.dao.RasShopDao;
import com.bytter.ras.entity.RasShop;
import com.bytter.ras.vo.RasShopVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核门店维护Service
 * @date 2019/5/6 8:52
 */
@Service("rasShopService")
@Transactional
public class RasShopService extends BaseService<RasShop, String> implements IExcelVerifyHandler<RasShopVO> {

    @Autowired
    public RasShopDao rasShopDao;
    @Autowired(required=false)
    private ICorpService corpServiceImpl;
    @Autowired(required=false)
    private IDictService dictServiceI;
    @Autowired
    private RasLogService rasLogService;

    /**
     * 删除对象
     * @param entity
     */
    public void deleteEntity(RasShop entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     * @param entity
     */
    public void save(RasShop entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     * @param entity
     */
    public void update(RasShop entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     * @param entity
     */
    public void saveOrUpdate(RasShop entity) {
        super.saveOrUpdate(entity);
    }
    
    @Override
    public BaseDao<RasShop, String> getEntityDao() {
        return rasShopDao;
    }

    /**
     * 加载门店列表
     */
    @SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpServiceImpl.getUserCorpIds(RasConstant.RAS_RESOURCE_PARAM_OP,dataTablesInput.getCurrentUserId());
        return rasShopDao.getDataTablesUnion(dataTablesInput,corpAuthList);
    }

    /**
     * 门店授权加载门店列表
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionShopGrant(DataTablesInput dataTablesInput) {
        return rasShopDao.getDataTablesUnionShopGrant(dataTablesInput);
    }
    /**
     * 新增门店数据保存
     */
    public void save(RasShopVO shopVO, ICurrentUser currentUser) {
        RasShop shop = new RasShop();
        //数据转换
        BeanUtils.copyNotNullProperties(shopVO,shop);
        //默认状态启用
        shop.setvSign(RasConstant.RAS_VALID_SIGN_1);
        shop.setCreateBy(currentUser.getLoginName());
        shop.setCreateName(currentUser.getUsername());
        shop.setCreateDate(DateUtils.getDate());
        this.saveEntity(shop);
        //添加日志
        rasLogService.saveRasLog(shop.getId(),RasConstant.RAS_LOGTYPE_ADD,"营收稽核门店数据新增",currentUser);
    }
    /**
     * //根据门户编码是否存在
     * @param code 门店编码
     * @return 返回门店id
     */
    public String checkShopByCorpAndCode(String code) {
        return rasShopDao.checkShopByCorpAndCode(code);
    }
    /**
     * 批量更新状态
     * @param ids （门店1id|门店1状态值,门店2id|门店2状态值）拼接
     * @param opType 操作类型（04启用 05停用）
     */
    public void updateStatus(String ids,String opType,ICurrentUser currentUser) {
        //启用改为禁用；禁用改为启用；
        for (String id : ids.split(",")) {
            String[] split = id.split("\\|");
            RasShop shop = this.get(split[0]);
            //此处校验更新时页面传来的状态跟数据库中的状态是否一致
            if(split[1].equals(shop.getvSign())){
                if(RasConstant.RAS_LOGTYPE_STOP.equals(opType)) {
                    shop.setvSign(RasConstant.RAS_VALID_SIGN_0);
                }else {
                    shop.setvSign(RasConstant.RAS_VALID_SIGN_1);
                }
                this.update(shop);
                //添加日志
                rasLogService.saveRasLog(shop.getId(),opType,"营收稽核门店状态修改",currentUser);
            }else{
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
    }
    /**
     * 根据id查找门店信息
     */
    public RasShopVO getByIdUnion(String id) {
        RasShopVO shopVO = rasShopDao.getByIdUnion(id);
        return shopVO;
    }
    /**
     * 编辑门店数据保存
     */
    public void update(RasShopVO shopVO, ICurrentUser currentUser) {
        RasShop shop = this.get(shopVO.getId());
        //数据转换
        BeanUtils.copyNotNullProperties(shopVO,shop);
        shop.setUpdateBy(currentUser.getLoginName());
        shop.setUpdateName(currentUser.getUsername());
        shop.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(shop);
        //添加日志
        rasLogService.saveRasLog(shop.getId(),RasConstant.RAS_LOGTYPE_UPDATE,"营收稽核门店数据修改",currentUser);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(RasShopVO vo) {
        return null;
    }
    /**
     * 导入数据前校验数据
     */
    @Override
    public void verifyHandler(List<RasShopVO> list) {
        //获取该用户有权限的单位id集
        ICurrentUser principal = UserContextHelper.helper().getCurrentUser();
        List<String> corpList = corpServiceImpl.getUserCorpIds(RasConstant.RAS_RESOURCE_PARAM_OP,principal.getId());

        //单位缓存
        Map<String,String> corpIds = new HashMap<>();
        //数据字典缓存
        Map<String,Map<String,String>> dircts = new HashMap<>();
        //单位级别门店缓存（将数据库中corp_id的加载进来）
        List<String> isCorpIds = rasShopDao.getCacheDatas("ras_shop","corp_id","is_corp=1");
        //门店编码缓存（将数据库中code的加载进来）
        List<String> shopCodes = rasShopDao.getCacheDatas("ras_shop","code","");

        for (RasShopVO vo : list) {
            List<String> msgList = new ArrayList<>();
            //判断单位缓存是否存在
            String corpId = "";
            if(StringUtils.isNotEmpty(vo.getCorpName())&& corpIds.get(vo.getCorpName())!=null){
                corpId = corpIds.get(vo.getCorpName());
            }else{
                //根据名称查找
                CorpDTO corp = this.corpServiceImpl.getCorpIdByName(vo.getCorpName());
                if(EmptyUtils.isEmpty(corp)){
                    //根据编码查找
                    corp = this.corpServiceImpl.getCorpByCode(vo.getCorpName());
                }
                //缓存单位id
                if(!EmptyUtils.isEmpty(corp)) {
                    corpIds.put(vo.getCorpName(),corp.getId());
                    corpId = corp.getId();
                }
            }
            if (EmptyUtils.isBlank(corpId)) {
                msgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORPID_ISNOTEXIST);
            } else {
                //判断该单位是否有权限
                if(corpList.contains(corpId))
                    vo.setCorpId(corpId);
                else
                    msgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORP_ISNOTPOWER);
            }

            //根据门店编码校验唯一性
            if(EmptyUtils.isNotEmpty(vo.getCode())){
                //检查缓存
                if(shopCodes.contains(vo.getCode())){
                    msgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPCODE_ISEXIST);
                }else
                    //将单位级别门店的单位id保存
                    shopCodes.add(vo.getCode());
            }

            //门店编码不能为空
            if (EmptyUtils.isEmpty(vo.getCode())) {
                msgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPCODE_ISNOTNULL);
            }

            //门店名称不能为空
            if (EmptyUtils.isEmpty(vo.getName())) {
                msgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPNAME_ISNOTNULL);
            }

            //门店类别校验
            String shopTypeCode = "";
            if(dircts.get("RAS_SHOP_TYPE")!=null){
                shopTypeCode = dircts.get("RAS_SHOP_TYPE").get(vo.getShopTypeName());
            }else{
                Map<String,String> temp = new HashMap<>();
                List<DictDTO> dictDto = dictServiceI.list("RAS_SHOP_TYPE");
                for (DictDTO dictDTO : dictDto) {
                    temp.put(dictDTO.getName(),dictDTO.getCode());
                }
                dircts.put("RAS_SHOP_TYPE",temp);
                shopTypeCode = dircts.get("RAS_SHOP_TYPE").get(vo.getShopTypeName());
            }
            if(StringUtils.isEmpty(shopTypeCode)){
                msgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPTYPE_ISNOTEXIST);
            }else{
                vo.setShopType(shopTypeCode);
            }

            //是否单位级别门店校验
            String isCorpCode = "";
            if(dircts.get("sf_yn")!=null){
                isCorpCode = dircts.get("sf_yn").get(vo.getIsCorpName());
            }else{
                Map<String,String> temp = new HashMap<>();
                List<DictDTO> dictDto = dictServiceI.list("sf_yn");
                for (DictDTO dictDTO : dictDto) {
                    temp.put(dictDTO.getName(),dictDTO.getCode());
                }
                dircts.put("sf_yn",temp);
                isCorpCode = dircts.get("sf_yn").get(vo.getIsCorpName());
            }
            if(StringUtils.isEmpty(isCorpCode)){
                msgList.add(RasConstant.RAS_EXCEL_MESSAGE_ISCORP_ISNOTNULL);
            }else{
                vo.setIsCorp(isCorpCode);
                //校验该单位下是否存在单位级别门店
                if(StringUtils.isNotEmpty(vo.getCorpId())&&"1".equals(isCorpCode)){
                    if(isCorpIds.contains(vo.getCorpId())){
                        msgList.add(RasConstant.RAS_EXCEL_MESSAGE_ISCORP_REPEAT);
                    }
//                    else{
//                        String id = checkShopByIsCorp(vo);
//                        if(StringUtils.isNotEmpty(id)) {
//                            msgList.add(RasConstant.RAS_EXCEL_MESSAGE_ISCORP_REPEAT);
//                        }
//                    }
                    //将单位级别门店的单位id缓存起来
                    if(!isCorpIds.contains(vo.getCorpId())) isCorpIds.add(vo.getCorpId());
                }
            }

            //开始稽核日期不能为空
            if (EmptyUtils.isEmpty(vo.getAsDate())) {
                msgList.add(RasConstant.RAS_EXCEL_MESSAGE_AUDITSTARTDATE_ISNOTNULL);
            }
            if (!EmptyUtils.isEmptyList(msgList)) {
                vo.addErrorMsg(msgList);
            }
        }
    }
    /**
     * 导入保存数据
     */
    public void saveImport(List<RasShopVO> list, ICurrentUser currentUser) {
        for (RasShopVO shopVO : list) {
            this.save(shopVO,currentUser);
        }
        //添加日志
        rasLogService.saveRasLog(DateUtils.getDate("yyyyMMddHHmmss"),RasConstant.RAS_LOGTYPE_IMP,"营收稽核门店数据导入",currentUser);
    }

    /**
     * 根据门店名称和单位id获取门店信息
     * @param name
     * @param corpId
     * @return
     */
    public RasShopVO getRasShopByName(String name,String corpId) {
        RasShop rasShop = rasShopDao.getRasShopByName(name,corpId);
        if (EmptyUtils.isEmpty(rasShop)) {
            return null;
        }
        RasShopVO rasShopVO = BeanUtils.copyNewInstanceProperties(rasShop, RasShopVO.class);
        return rasShopVO;
    }

    /**
     * 根据门店编码和单位id获取门店信息
     * @param code
     * @param corpId
     * @return
     */
    public RasShopVO getRasShopByCode(String code,String corpId) {
        RasShop rasShop = rasShopDao.getRasShopByCode(code,corpId);
        if (EmptyUtils.isEmpty(rasShop)) {
            return null;
        }
        RasShopVO rasShopVO = BeanUtils.copyNewInstanceProperties(rasShop, RasShopVO.class);
        return rasShopVO;
    }
    
    /**
     * 
    * @Title: getRasShopByName  
    * @Description: TODO()  
    * @param @param name
    * @param @param corpId
    * @param @return    参数  
    * @return RasShopVO    返回类型  
    * @throws
     */
    public RasShopVO getRasInfoByShopName(String shopName) {
        RasShop rasShop = rasShopDao.getRasInfoByShopName(shopName);
        if (EmptyUtils.isEmpty(rasShop)) {
            return null;
        }
        RasShopVO rasShopVO = BeanUtils.copyNewInstanceProperties(rasShop, RasShopVO.class);
        return rasShopVO;
    }
    
    
    /**
     * 根据参数获取门店数据
     * @param params
     * @return
     */
    public List<RasShop> getRasShopByParams(Map<String,Object> params){
    	return rasShopDao.getRasShopByParams(params);
    }
    
    /**
     * 根据参数获取有效的门店数据
     * @param params
     * @return
     */
    public List<RasShop> getRasShopValidByParams(Map<String,Object> params){
    	params.put("wValidSign", RasConstant.RAS_VALID_SIGN_1);
    	return rasShopDao.getRasShopByParams(params);
    }

    /**
     * 校验该门店是否存在待稽核的数据
     */
    public boolean checkShopNotAudit(String ids) {
        return rasShopDao.checkShopNotAudit(ids);
    }

    /**
     * 校验是否单位级别门店（一个单位只能存在一个单位级别门店）
     * @param shopVO
     * @return
     */
    public String checkShopByIsCorp(RasShopVO shopVO) {
        return rasShopDao.checkShopByIsCorp(shopVO);
    }
    
    /*
     * 获取所有门店信息
     */
    public List<RasShopVO> hisRasShop(String userId){
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("userId", userId);
    	return rasShopDao.hisRasShop(params);
    }
}
