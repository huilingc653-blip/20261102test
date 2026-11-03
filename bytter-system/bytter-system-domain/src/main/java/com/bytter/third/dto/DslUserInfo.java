package com.bytter.third.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 大参林员工信息同步。
 *
 * @author zhaohc
 * @since 2024/5/28 10:04
 */
@Data
public class DslUserInfo {
    /**
     * 编号
     */
    private Long id;
    /**
     * 员工编号
     */
    private String emplid;
    /**
     * 员工姓名
     */
    private String nameDisplay;
    /**
     * 上次更新时间
     */
    private String hpsLastupddttm;
    /**
     * 变动事务0 离职 ,1入职 ,2 调动 ,3 退休,4其它
     */
    private String hpsMemo;
    /**
     * 员工状态：A-有效 I-无效
     */
    private String hpsHrStatus;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String hpsMail;
    /**
     * 工作邮箱
     */
    private String workMail;
    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 辅助方法：将大参林员工信息转换为用户信息。
     *
     * @param userInfo 大参林员工信息。
     * @return 资金系统用户信息。
     */
    public static UserDTO generateUser(DslUserInfo userInfo, CorpDTO defaultCorp) {
        UserDTO userDTO = new UserDTO();
        userDTO.setCorp(defaultCorp);
        userDTO.setLoginName(userInfo.getEmplid());
        userDTO.setEmployeeId(userInfo.getEmplid());
        userDTO.setUsername(userInfo.getNameDisplay());
        userDTO.setPhone(userInfo.getPhone());
        // 添加企业微信
        userDTO.setWeiXinUserId(userInfo.getEmplid());
        // 优先取工作邮箱
        userDTO.setEmail(ObjectUtil.isNotEmpty(userInfo.getWorkMail()) ? userInfo.getWorkMail() : userInfo.getHpsMail());
        // 变动事务：0 离职 ,1入职 ,2 调动 ,3 退休,4其它
        // 用户状态：1-正常、2-禁用、3-锁定、0-已删除
        // 员工状态（1，2）对应用户状态1，员工状态（0，3，4）对应用户状态2；
        // userDTO.setStatus(StrUtil.containsAny(userInfo.getHpsMemo(), "1", "2") ? 1 : 2);
        // 根据用户状态来同步用户状态
        userDTO.setStatus(StrUtil.equals(userInfo.getHpsHrStatus(), "A") ? 1 : 2);
        // 登陆方式为：3-SSO单点登陆
        userDTO.setLoginType(3);
        userDTO.setIsAdmin(0);
        return userDTO;
    }

    public static void main(String[] args) {
        System.out.println(StrUtil.containsAny("1", "1", "2"));
        System.out.println(StrUtil.containsAny(null, "1", "2"));
        String status = "I";
        int count = StrUtil.equals(status, "A") ? 1 : 2;
        System.out.println("count = " + count);
    }

    /**
     * 辅助方法：将大参林员工列表转换为资金系统用户列表。
     *
     * @param list 大参林员工信息列表。
     * @return 资金系统用户列表。
     */
    public static List<UserDTO> toUserList(List<DslUserInfo> list, CorpDTO defaultCorp) {
        List<UserDTO> listData = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(item -> listData.add(generateUser(item, defaultCorp)));
        }
        return listData;
    }

}
