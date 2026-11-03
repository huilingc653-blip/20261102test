package com.bytter.guarantee.domain.service.impl;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.domain.dao.GuaWayDao;
import com.bytter.guarantee.domain.entity.GuaWay;
import com.bytter.guarantee.domain.service.GuaWayService;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author luxy
 * @Title: GuaWayServieImpl
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/7/24 11:11
 */
@Service("guaWayServieImpl")
@DubboService
@Transactional
public class GuaWayServieImpl implements IGuaWayServie {
    @Autowired
    private GuaWayService guaWayService;
    @Autowired
    private GuaWayDao guaWayDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /**
     * 根据担保方式code获取担保方式名称
     */
    @Override
    public String getGuaWayNameByCodes(String codes) {
        List<String> codeList = new ArrayList<>();
        String guaWayName = "";
        if (StringUtils.isNotEmpty(codes)) {
            if (codes.contains(",")) {
                String[] codeArr = codes.split(",");
                for (int i = 0; i < codeArr.length; i++) {
                    codeList.add(codeArr[i]);
                }
            } else {
                codeList.add(codes);
            }
            List<GuaWay> guaWayList = guaWayService.queryGuaWayByCodes(codeList);
            int i = 0;
            for (GuaWay guaWay : guaWayList) {
                String mulLan = ii18nService.getBusinessBaseContext(guaWay.getId());// 多语言
                if (StringUtils.isNotEmpty(mulLan)) {
                    if (i == 0) {
                        guaWayName = mulLan;
                    } else {
                        guaWayName += "," + mulLan;
                    }
                } else {
                    if (i == 0) {
                        guaWayName = guaWay.getName();
                    } else {
                        guaWayName += "," + guaWay.getName();
                    }
                }
                i++;
            }
        }
        return guaWayName;
    }
    @Override
    public List<GuaWayDTO> getAllGuaWay() {
        List<GuaWay> guaWayList = guaWayService.listAll();
        List<GuaWayDTO> guaWayDTOS = new ArrayList<>();
        guaWayDTOS = BeanUtils.copyListProperties(guaWayList, GuaWayDTO.class);
        return guaWayDTOS;
    }
    @Override
    public String getGuaWayNameByIds(String ids) {
        List<String> idList = new ArrayList<>();
        String guaWayName = "";
        if (StringUtils.isNotEmpty(ids)) {
            if (ids.contains(",")) {
                String[] codeArr = ids.split(",");
                for (int i = 0; i < codeArr.length; i++) {
                    idList.add(codeArr[i]);
                }
            } else {
                idList.add(ids);
            }
            List<GuaWay> guaWayList = guaWayDao.list(idList);
            int i = 0;
            for (GuaWay guaWay : guaWayList) {
                String mulLan = ii18nService.getBusinessBaseContext(guaWay.getId());// 多语言
                if (StringUtils.isNotEmpty(mulLan)) {
                    if (i == 0) {
                        guaWayName = mulLan;
                    } else {
                        guaWayName += "," + mulLan;
                    }
                } else {
                    if (i == 0) {
                        guaWayName = guaWay.getName();
                    } else {
                        guaWayName += "," + guaWay.getName();
                    }
                }
                i++;
            }
        }
        return guaWayName;
    }
    @Override
    public List<GuaWayDTO> findAllByCode(String code, String id) {
        return null;
    }
    @Override
    public Map<String, GuaWayDTO> getGuaWayNameByIds(List<String> ids) {
        List<GuaWay> guaWayList = guaWayDao.list(ids);
        return BeanUtils.copyListProperties(guaWayList, GuaWayDTO.class).stream().collect(Collectors.toMap(GuaWayDTO::getId, Function.identity()));
    }
}
