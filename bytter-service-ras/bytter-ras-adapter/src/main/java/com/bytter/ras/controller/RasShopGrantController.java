package com.bytter.ras.controller;

import com.bytter.ras.entity.RasShopGrant;
import com.bytter.ras.service.RasShopGrantService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.Globals;
import com.bytter.ras.api.constant.RasConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhenghj
 * @Title: Controller
 * 营收稽核门店授权入口类
 * @date 2019-05-13
 */

@Controller
@RequestMapping("/ras/shopGrant")
public class RasShopGrantController extends BaseController {

    @Autowired
    private RasShopGrantService rasShopGrantService;


    /**
     * 根据单位ID和资源代码获取对应的用户信息
     *
     * @param corpId    单位ID
     * @param grantType 授权类型 query查询权限audit稽核权限
     */
    @PostMapping("/loadShopByCorpIdOrGrantType")
    @ResponseBody
    public Object loadShopByCorpIdOrGrantType(String corpId, String grantType) {
        return rasShopGrantService.loadShopByCorpIdOrGrantType(corpId, grantType, this.getCurrentUser());
    }

    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @GetMapping("/list")
    public String list(Model model) {
        return "ras/listShopGrantByUser";
    }

    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @GetMapping("/listShop")
    public String listShop(Model model) {
        return "ras/listShopGrantByShop";
    }


    /**
     * 根据用户分配门店
     *
     * @param userId 用户ID
     * @param model
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @GetMapping("/assginShop/userId/{userId}")
    public String assginShop(@PathVariable("userId") String userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("shopId", null);
        return "ras/assignShop";
    }

    /**
     * 根据门店分配用户
     *
     * @param shopId 门店ID
     * @param model
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @GetMapping("/assginUser/shopId/{shopId}")
    public String assginUser(@PathVariable("shopId") String shopId, Model model) {
        model.addAttribute("userId", null);
        model.addAttribute("shopId", shopId);
        return "ras/assignUser";
    }

    /**
     * 已授权分配的门店或用户
     *
     * @param shopId 门店id
     * @param userId 用户id
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @GetMapping("/assigned")
    @ResponseBody
    public Object assigned(String shopId, String userId) {
        return rasShopGrantService.listAssigned(shopId, userId);
    }

    /**
     * 授权方法
     *
     * @param shopGrantList
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @LogMessage(content = "营收稽核门店授权确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHCONSENT)
    @PostMapping("/assignShop")
    @ResponseBody
    public Object assignShopPost(@RequestBody List<RasShopGrant> shopGrantList) {
        List<String> shopIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        for (RasShopGrant rasShopGrant : shopGrantList) {
            shopIds.add(rasShopGrant.getShopId());
            userIds.add(rasShopGrant.getUserId());
        }
        if (shopIds.size() > 0 && userIds.size() > 0) {
            //查询门店是否已在授权表中
            List<RasShopGrant> dbShopGrants = rasShopGrantService.listByShopIdsAndUserIds(shopIds, userIds);
            List<RasShopGrant> list = new ArrayList<>();
            for (RasShopGrant rasShopGrant : shopGrantList) {
                if (rasShopGrant.getCanQuery() == null) {
                    rasShopGrant.setCanQuery("0");
                }
                if (rasShopGrant.getCanAudit() == null) {
                    rasShopGrant.setCanAudit("0");
                }
                //检查新增的数据是否存在数据库中
                RasShopGrant dbshopGrant = isDbHad(rasShopGrant, dbShopGrants);
                if (dbshopGrant != null) {
                    //修正空指针异常
                    if (rasShopGrant.getCanAudit().equals(dbshopGrant.getCanAudit()) &&
                            rasShopGrant.getCanQuery().equals(dbshopGrant.getCanQuery())) {
                        continue;
                    }

                }
                list.add(setRasShopGrat(rasShopGrant, this.getCurrentUser()));
            }
            rasShopGrantService.save(list);
        }
        return success();
    }


    /**
     * 检查新增的数据是否存在数据库中
     *
     * @param rasShopGrant    验证是否存在对象
     * @param dbRasShopGrants 数据库集合
     * @return 有则返回对象，没有则返回null
     */
    private RasShopGrant isDbHad(RasShopGrant rasShopGrant, List<RasShopGrant> dbRasShopGrants) {
        if (CollectionUtils.isEmpty(dbRasShopGrants)) {
            return null;
        }
        for (RasShopGrant dbShopGrant : dbRasShopGrants) {
            if (rasShopGrant.getShopId().equals(dbShopGrant.getShopId()) && rasShopGrant.getUserId().equals(dbShopGrant.getUserId())) {
                return dbShopGrant;
            }
        }
        return null;
    }

    private RasShopGrant setRasShopGrat(RasShopGrant rasShopGrant, ICurrentUser userVo) {
        RasShopGrant newRasShopGrant = new RasShopGrant();
        newRasShopGrant.setShopId(rasShopGrant.getShopId());
        newRasShopGrant.setUserId(rasShopGrant.getUserId());
        newRasShopGrant.setCanQuery(rasShopGrant.getCanQuery());
        newRasShopGrant.setCanAudit(rasShopGrant.getCanAudit());
        if (userVo != null) {
            newRasShopGrant.setCreateBy(userVo.getLoginName());
            newRasShopGrant.setCreateName(userVo.getUsername());
        }
        newRasShopGrant.setCreateDate(new Date());
        return newRasShopGrant;
    }
}
