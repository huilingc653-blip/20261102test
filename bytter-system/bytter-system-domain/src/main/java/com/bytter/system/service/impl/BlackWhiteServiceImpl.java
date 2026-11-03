package com.bytter.system.service.impl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBlackWhiteService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BlackWhiteDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.entity.BlackWhiteList;
import com.bytter.system.service.BlackWhiteListService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * Created by zhoul on 2017/12/11.
 */
@Service("blackWhiteServiceI")
@Transactional
@DubboService
public class BlackWhiteServiceImpl implements IBlackWhiteService {
    @Autowired
    private BlackWhiteListService blackWhiteListService;
    @Autowired(required=false)
    private IConfigService configServiceI;
    @Autowired(required=false)
    private IMultiLangService multiLangServiceI;
    @Override
    public BlackWhiteDTO externalCorpCheck(String name, String isControl, String corpId) {
        List<BlackWhiteDTO> params=new ArrayList<>();
        params.add(new BlackWhiteDTO(name,corpId));
        return externalCorpCheckBatch(params,isControl);
    }
    @Override
    public BlackWhiteDTO bankAcceptanceCheck(String name, String isControl,String corpId) {
        List<BlackWhiteDTO> params=new ArrayList<>();
        params.add(new BlackWhiteDTO(name,corpId));
        return bankAcceptanceCheckBatch(params,isControl);
    }
    @Override
    public BlackWhiteDTO externalCorpCheck(String name,String corpId) {
        return externalCorpCheck(name, SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_Y,corpId);
    }
    @Override
    public BlackWhiteDTO bankAcceptanceCheck(String name,String corpId) {
        return bankAcceptanceCheck(name, SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_Y,corpId);
    }
    @Override
    public BlackWhiteDTO externalCorpCheckBatch(List<BlackWhiteDTO> params, String isControl) {
        return blackWhiteCheckBatch(params,isControl,SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_01);
    }
    @Override
    public BlackWhiteDTO bankAcceptanceCheckBatch(List<BlackWhiteDTO> params, String isControl) {
        return blackWhiteCheckBatch(params,isControl,SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_02);
    }
    @Override
    public BlackWhiteDTO externalCorpCheckBatch(List<BlackWhiteDTO> params) {
        return externalCorpCheckBatch(params, SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_Y);
    }
    @Override
    public BlackWhiteDTO bankAcceptanceCheckBatch(List<BlackWhiteDTO> params) {
        return bankAcceptanceCheckBatch(params, SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_Y);
    }
    @Override
    public boolean checkExternalCorpByName(String name,String corpId) {
        return checkExternalCorpByName(name,corpId,null);
    }
    @Override
    public boolean checkExternalCorpByName(String name,String corpId, String noExternalId) {
        return checkBlackWhiteListByName(name,null,SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_01,corpId,noExternalId);
    }
    @Override
    public boolean checkBankAcceptanceByName(String name,String corpId) {
        return checkBankAcceptanceByName(name,corpId,null);
    }
    @Override
    public boolean checkBankAcceptanceByName(String name,String corpId, String noExternalId) {
        return checkBlackWhiteListByName(name,null,SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_02,corpId,noExternalId);
    }
    @Override
    public void deleteBlackWhiteByExternalId(String externalId, ICurrentUser user) {
        blackWhiteListService.deleteBlackWhiteListByExternalId(externalId,user);
    }
    /**
     * 通过名称获取黑白名单信息
     * @param name 黑白名单客商名称
     * @param listType 客商承兑行类型
     * @param corpId 单位ID
     * @return
     */
    @Transactional(readOnly = true)
    public List<BlackWhiteList> getBlackWhiteListByParams(String name, String listType, String corpId){
        List<BlackWhiteList> bwListToCorpId=new ArrayList<>();
        if(StringUtils.isBlank(name)){
            return bwListToCorpId;
        }
        List<BlackWhiteList> bwList=blackWhiteListService.getBlackWhiteListByParams(name,listType);
        if(ListUtils.isNotNullOrEmpty(bwList)) {
            for (int i = 0; i < bwList.size(); i++) {
                BlackWhiteList info=bwList.get(i);
                if (StringUtils.isEmpty(info.getCorpId())||String.valueOf(corpId).indexOf(info.getCorpId())>-1) {
                    bwListToCorpId.add(info);
                }
            }
        }
        return bwListToCorpId;
    }
    /**
     *
     * @param name 校验名称
     * @param bwType 黑白名单类型
     * @param listType 客商承兑行类型
     * @param noExternalId 需要排除外部id信息
     * @return
     */
    public boolean checkBlackWhiteListByName(String name, String bwType,String listType,String corpId, String noExternalId) {
        List<BlackWhiteList> list=getBlackWhiteListByParams(name,listType,corpId);
        if(ListUtils.isNotNullOrEmpty(list)){
            BlackWhiteList info=list.get(0);
            if(StringUtils.isNotEmpty(bwType)){
                if(bwType.equals(info.getBwType())){
                    return true;
                }else{
                    return false;
                }
            }
            if(StringUtils.isNotEmpty(noExternalId)){
                //用于判断排除此外部id数据
                if(noExternalId.equals(info.getExternalId())){
                    return false;
                }else{
                    return true;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    @CacheEvict(value = SysBlackWhiteConstant.CACHE_NAME_CHECK, key = "#dto.name")
    public void saveOrUpdateBlackWhite(BlackWhiteDTO dto, ICurrentUser user) {
        if(StringUtils.isEmpty(dto.getName())
                || StringUtils.isEmpty(dto.getExternalId())
                || StringUtils.isEmpty(dto.getBwType())) {
            return;
        }
        //客商如果变成一般客商把原关联纪录作为失效
        if(SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_00.equals(dto.getBwType())){
            blackWhiteListService.deleteBlackWhiteListByExternalId(dto.getExternalId(),user);
            return;
        }
        Map<String,Object> params=new HashMap<>();
        if(StringUtils.isNotEmpty(dto.getName())) {
            params.put("name",dto.getName());
        }else{
            return;
        }
        List<BlackWhiteList> list=blackWhiteListService.getBlackWhiteListByParams(params);
        BlackWhiteList info=null;
        if(list!=null&&list.size()>0){
            if(list.size()>1){
                //针对多个客商名称重复的黑白名单信息对已有外部ID的数据删除然后修改没有外部ID的数据关联本客商和信息保持和客商一直
                for(BlackWhiteList entity:list){
                    if(StringUtils.isEmpty(entity.getExternalId())){
                        info=entity;
                    }else{
                        blackWhiteListService.deleteBlackWhiteListByExternalId(entity.getExternalId(),user);
                    }
                }
            }else{
                info=list.get(0);
            }
            BeanUtils.copyProperties(dto,info);
            info.setUpdateDate(new Date());
            if(user!=null){
                info.setUpdateId(user.getId());
            }
        }else{new BlackWhiteList();
            info=new BlackWhiteList();
            BeanUtils.copyProperties(dto,info);
            info.setCreateDate(new Date());
            if(user!=null){
                info.setCreateId(user.getId());
            }
        }
        info.setDataSources(SysBlackWhiteConstant.SYS_BLACK_WHITE_DATASOURCES_01);
        info.setListType(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_01);
        if(StringUtils.isEmpty(dto.getValidSign())){
            info.setValidSign(SysBlackWhiteConstant.SYS_BLACK_WHITE_VALID_SIGN_1);
        }
        blackWhiteListService.saveOrUpdate(info);
    }
    /**
     * 白名批量验证服务类
     * @param params 验证信息集合
     * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     * @param listType 名单类别：01客商、02承兑行
     * @return
     */
    @Override
    public BlackWhiteDTO blackWhiteCheckBatch(List<BlackWhiteDTO> params, String isControl, String listType) {
        String reCode=SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_NOWARN;
        StringBuilder msg=new StringBuilder();
        String blacklistCheck=getBlacklistCheck();
        if(!SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_NOWARN.equals(blacklistCheck)){
            if (SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_Y.equals(isControl)|| StringUtils.isEmpty(isControl)) {
                reCode=blacklistCheck;
            } else {
                reCode=SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_WARN;
            }
            if(ListUtils.isNotNullOrEmpty(params)){
                for(BlackWhiteDTO info:params){
                    if(StringUtils.isNotEmpty(info.getName())) {
                        String[] names = info.getName().split(",");
                        for(String name:names) {
                            //校验黑名单
                            if (checkBlackWhiteListByName(name, SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_01, listType, info.getCorpId(), null)) {
                                String msgInfo = getMultiLangMsgInfo(info, name, reCode, listType);
                                if (StringUtils.isNotEmpty(msgInfo)) {
                                    msg.append(msgInfo);
                                }
                                info.setReCode(reCode);
                            }
                        }
                    }
                }
            }
            if(msg.length()==0) {
                reCode=SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_NOWARN;
            }
        }
        return new BlackWhiteDTO(reCode,msg.toString(),params);
    }
    /**
     * 获取返回贴现的国际化内容
     * @param info 检查信息
     * @param reCode 返回类型（提醒还是控制）
     * @param listType 名单类别：01客商、02承兑行
     * @return
     */
    public String getMultiLangMsgInfo(BlackWhiteDTO info,String name,String reCode,String listType){
        String msgInfo=null;
        if(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_01.equals(listType)){//客商校验内容
            if(StringUtils.isEmpty(info.getAppNo())){//没有单号则为单笔
                if(SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_WARN.equals(reCode)){
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_EXTERNALCORP_WARN,name);
                }else{
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_EXTERNALCORP_CONTROL,name);
                }
            }else{
                if(SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_WARN.equals(reCode)){
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_EXTERNALCORP_BATCH_WARN,name,info.getAppNo());
                }else{
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_EXTERNALCORP_BATCH_CONTROL,name,info.getAppNo());
                }
            }
        }else if(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_02.equals(listType)){//承兑行校验内容
            if(StringUtils.isEmpty(info.getAppNo())){//没有单号则为单笔
                if(SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_WARN.equals(reCode)){
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_WARN,name);
                }else{
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_CONTROL,name);
                }
            }else{
                if(SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_WARN.equals(reCode)){
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_BATCH_WARN,name,info.getAppNo());
                }else{
                    msgInfo=multiLangServiceI.getLang(SysBlackWhiteConstant.SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_BATCH_CONTROL,name,info.getAppNo());
                }
            }
        }
        return msgInfo;
    }
    /**
     * 获取黑名单收付限制配置信息
     * @return
     */
    @Override
    public String getBlacklistCheck(){
        ConfigDTO config=configServiceI.getConfig(SysConfigConstant.Module.SYS,SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK);
        if(config==null|| StringUtils.isEmpty(config.getValue())){
            return SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_NOWARN;
        }
        return config.getValue();
    }
}
