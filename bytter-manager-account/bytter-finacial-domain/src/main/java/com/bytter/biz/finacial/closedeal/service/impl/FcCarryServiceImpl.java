package com.bytter.biz.finacial.closedeal.service.impl;
import com.bytter.biz.finacial.api.vo.FcCarryVO;
import com.bytter.biz.finacial.closedeal.entity.FcCarry;
import com.bytter.biz.finacial.closedeal.service.FcCarryService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import com.bytter.biz.finacial.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import java.util.Date;
import java.util.List;
/**
 * 结转
 *
 * @author chenlw
 * @since 2020-11-26
 */
@Service("fcCarryServiceImpl")
@Transactional
@DubboService
public class FcCarryServiceImpl implements IFcCarryService {
    @Autowired
    private FcCarryService fcCarryService;
    @DubboReference(check = false)
	private IUserService userService;
    @Override
    public UserDTO getDayOverOperatorUser(String netId, Date date) {
        List<FcCarry> fcCarryList = fcCarryService.getDayOverFcCarry(netId, date);
        if (!CollectionUtils.isEmpty(fcCarryList)) {
            FcCarry fcCarry = fcCarryList.get(0);
            com.bytter.system.api.dto.UserDTO userDTO = userService.getByUserId(fcCarry.getOperatorCode());
            UserDTO newUserDTO = new UserDTO();
            BeanUtils.copyProperties(userDTO, newUserDTO);
            return newUserDTO;
        }
        return null;
    }
    public List<FcCarryVO> getFcCarryList(FcCarryVO vo){
        return fcCarryService.getFcCarryList(vo);
    }
}
