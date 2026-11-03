package com.bytter.system.service.impl;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.CAUtil;
import com.bytter.system.api.ICAService;
import com.bytter.system.api.dto.SysCaInfoDTO;
import com.bytter.system.entity.Config;
import com.bytter.system.entity.SysCaInfo;
import com.bytter.system.service.ConfigService;
import com.bytter.system.service.SdCertService;
import com.bytter.system.service.SysCaInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhoul on 2017/10/23.
 */
@Service("caServiceI")
@DubboService
public class CAServiceImpl implements ICAService {
    @Autowired
    private ConfigService configService;
    @Autowired
    private SysCaInfoService sysCaInfoService;
    @Autowired
    private SdCertService sdCertService;
    /**
     * 后台验签方法
     *
     * @param userCadn
     * @param userCode
     * @param sourceData
     * @param signData
     * @return
     */
    @Override
    public boolean verify(String userCadn, String userCode, String sourceData, String signData) {
        //加密方式
        Config encrypt = configService.getGlobalConfig("sys", "encrypt");
        if (null == encrypt) {
            encrypt = new Config();
            encrypt.setValue("0");
        }
        boolean reslut = false;
        CAUtil ca = new CAUtil();
        try {
            if (!encrypt.getValue().equals("4")) {
                reslut = ca.verify(encrypt.getValue(), sourceData, signData);
            } else {//山地证书验签
                reslut = sdCertService.check(userCadn, sourceData, signData, userCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return reslut;
    }
    /**
     * 保存加密信息
     *
     * @param dto
     */
    @Transactional
    @Override
    public String saveOrUpdateCaInfo(SysCaInfoDTO dto) {
        SysCaInfo info = new SysCaInfo();
        BeanUtils.copyNotNullProperties(dto, info);
        info.setCaCoding(dto.getCaCoding().getBytes());
        sysCaInfoService.saveOrUpdate(info);
        dto.setId(info.getId());
        return info.getId();
    }
}
