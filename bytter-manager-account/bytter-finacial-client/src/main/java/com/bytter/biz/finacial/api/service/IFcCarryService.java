package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.vo.FcCarryVO;
import java.util.Date;
import java.util.List;
/**
 * 结转
 *
 * @author chenlw
 * @since 2020-11-26
 */
public interface IFcCarryService {
    /**
     * 获取 网点指定日期操作日结的用户
     *
     * @param netId 网点ID
     * @param date  日期
     * @return 用户
     */
    UserDTO getDayOverOperatorUser(String netId, Date date);
    public List<FcCarryVO> getFcCarryList(FcCarryVO vo) ;
}
