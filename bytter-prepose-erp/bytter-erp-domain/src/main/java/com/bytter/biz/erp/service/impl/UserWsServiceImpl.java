package com.bytter.biz.erp.service.impl;

import com.bytter.biz.erp.base.AbstractCxfService;
import com.bytter.biz.erp.base.Result;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhoul
 */
@WebService(serviceName = "userWs")
public class UserWsServiceImpl extends AbstractCxfService implements com.bytter.erp.service.UserWsService {

    @DubboReference(check = false)
    private IUserService userService;

    @Autowired(required = false)
    private com.bytter.erp.service.WsTokenCacheService wsTokenCacheService;

//    @Resource
//    private PasswordHash passwordHash;

    @Override
    public String save(String params) {
        List<UserDTO> userDTOList = JsonUtils.json2List(params, UserDTO.class);
        if (CollectionUtils.isEmpty(userDTOList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        for (UserDTO dto : userDTOList) {
            this.checkUser(dto);//校验参数
        }
        userService.save(userDTOList);
        return success();
    }


    @Override
    public String update(String params) {
        List<UserDTO> userDTOList = JsonUtils.json2List(params, UserDTO.class);
        if (CollectionUtils.isEmpty(userDTOList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        for (UserDTO dto : userDTOList) {
            this.checkUser(dto);//校验参数
        }
        userService.update(userDTOList);
        return success();
    }

    @Override
    public String delete(String params) {
        List<UserDTO> userDTOList = JsonUtils.json2List(params, UserDTO.class);
        if (CollectionUtils.isEmpty(userDTOList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        for (UserDTO dto : userDTOList) {
            if(EmptyUtils.isEmpty(dto.getLoginName()))
                throw new RuntimeException("登录名不能为空!");
            else {
                UserDTO byLoginName = userService.getByLoginName(dto.getLoginName());
                if(byLoginName==null) throw new RuntimeException(String.format("登录名[%s]不存在!", dto.getLoginName()));
            }
        }
        //本质是根据loginName来删除用户
        userService.delete(userDTOList);
        return success();
    }

    /**
     *
     * @param params 用户名和密码字符串：eg：   user1,password
     * @return
     */
    @Override
    public String login(String params) {
//        params = Base64Util.decodeToString(params);
        String[] split=params.split(",");
        if (split.length != 2) {
            throw new RuntimeException("报文体格式不正确!");
        }

        String loginName =  split[0];
        String password = split[1];

        if (StringUtils.isAnyBlank(loginName, password)){
            throw new RuntimeException("用户名或密码不能为空");
        }

        UserDTO user = userService.getByLoginName(loginName);
        if (user == null){
            return JsonUtils.toJSONString(Result.failure("认证失败，用户名或密码错误"));
        }
        String salt = userService.getSaltByLoginName(loginName);
//        String hash = passwordHash.toHex(password, salt);
//        if (!hash.equals(user.getPassword())) {
//            return JsonUtils.toJSONString(Result.failure("认证失败，用户名或密码错误"));
//        }
//        String token = UUIDUtils.create().toString();
        String token = "";
        wsTokenCacheService.writeToken(token);
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return success(map);
    }

    /**
     * 检验参数
     * @param dto
     */
    private void checkUser(UserDTO dto){
        if(EmptyUtils.isEmpty(dto.getLoginName()))
            throw new RuntimeException("登录名不能为空");

        if(EmptyUtils.isEmpty(dto.getPassword()))
            throw new RuntimeException("密码不能为空");

        if(EmptyUtils.isEmpty(dto.getUsername()))
            throw new RuntimeException("用户名不能为空");

        if(EmptyUtils.isEmpty(dto.getSex()))
            throw new RuntimeException("性别不能为空");

        CorpDTO corp = dto.getCorp();
        if(StringUtils.isNotEmpty(corp)){
            String code = corp.getCode();
            if(EmptyUtils.isEmpty(code))
                throw new RuntimeException("单位编码不能为空");
        }else{
            throw new RuntimeException("单位集不能为空");
        }

        if(EmptyUtils.isEmpty(dto.getIsAdmin()))
            throw new RuntimeException("是否管理员不能为空");

        if(EmptyUtils.isEmpty(dto.getStatus()))
            throw new RuntimeException("状态不能为空");
    }
}
