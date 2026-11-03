package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.entity.AccountGrant;
import com.bytter.system.inneraccount.service.FcAccService;
import com.bytter.system.service.AccountGrantService;
import com.bytter.system.service.CorpService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 内部帐号、银行账号、离线帐号授权
 * Created by zhoul on 2017/7/3.
 * todo 双管理员问题 循环依赖
 */
@Controller
@RequestMapping("/sys/accountGrant")
public class AccountGrantController extends BaseController {
    @Autowired
    private AccountGrantService accountGrantService;
    @Autowired
    private IBtBankAccService btBankAccServiceI;
    @Autowired
    private FcAccService fcAccService;
    @Autowired
    private CorpService corpService;
    @Autowired
    private IConfigService configServiceI;
    @GetMapping("/list")
    public String list(Model model) {
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            model.addAttribute("msg", "sys.accountgrant.permission.controlType");
            return "sys/accountGrant/error";
        }
        return "sys/accountGrant/list";
    }
    @GetMapping("/listUser")
    public void listUser() {
    }
    /**
     * 已分配的用户
     *
     * @param accountId
     * @param accountType
     */
    @GetMapping("/assigned")
    @ResponseBody
    public Object assigned(String accountId, String accountType, String userId) {
        return accountGrantService.listAssigned(accountId, accountType, userId);
    }
    /**
     * 根据帐号分配 用户
     *
     * @param accountId     帐号ID
     * @param accountType   帐号类型
     * @param accountCorpId 帐号所在单位ID
     * @param model         模型
     * @return
     */
    @GetMapping("/assignUser/accountId/{accountId}/accountType/{accountType}/accountCorpId/{accountCorpId}")
    public String assignUser(@PathVariable("accountId") String accountId, @PathVariable("accountType") String accountType, @PathVariable("accountCorpId") String accountCorpId, Model model) {
        model.addAttribute("accountId", accountId);
        model.addAttribute("accountType", accountType);
        model.addAttribute("accountCorpId", accountCorpId);
        if (AccountGrantConstant.ONLINE_BANK_TYPE.equalsIgnoreCase(accountType)
                || AccountGrantConstant.OFFLINE_BANK_TYPE.equalsIgnoreCase(accountType)) {
            //银行账号授权网点
            List<String> accountCorp = corpService.getUserIdsByCorpId(SysResourceConstant.SYS_ACCOUNTGRANT_ACCOUNT, accountCorpId);
            model.addAttribute("userIds", CollectionUtils.isEmpty(accountCorp) ? "" : StringUtils.join(accountCorp, ","));
        } else {
            //银行账号授权网点
            List<String> accountCorp = corpService.getUserIdsByCorpId(SysResourceConstant.SYS_ACCOUNTGRANT_INNERACCOUNT, accountCorpId);
            model.addAttribute("userIds", CollectionUtils.isEmpty(accountCorp) ? "" : StringUtils.join(accountCorp, ","));
        }
        return "sys/accountGrant/assignUser";
    }
    /**
     * 根据用户分配帐号
     *
     * @param userId 用户ID
     * @param model
     * @return
     */
    @GetMapping("/assignAccount/userId/{userId}")
    public String assignAccount(@PathVariable("userId") String userId, Model model) {
        //银行账号授权网点
        List<String> accountCorp = corpService.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_ACCOUNT, userId);
        model.addAttribute("accountCorp", CollectionUtils.isEmpty(accountCorp) ? "-1" : StringUtils.join(accountCorp, ","));
        //内部账号授权网点
        List<String> innerAccountCorp = corpService.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_INNERACCOUNT, userId);
        model.addAttribute("innerAccountCorp", CollectionUtils.isEmpty(innerAccountCorp) ? "-1" : StringUtils.join(innerAccountCorp, ","));
        model.addAttribute("userId", userId);
        return "sys/accountGrant/assignAccount";
    }
    /**
     * 账号授权方法
     * 2018-12-11 update by zhoul
     * 适配双管理员
     *
     * @param accountGrantList
     * @return
     */
    @PostMapping("/assignUser")
    @ResponseBody
    public Object assignUserPost(@RequestBody List<AccountGrant> accountGrantList) {
        List<String> accountIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        for (AccountGrant accountGrant : accountGrantList) {
            accountIds.add(accountGrant.getAccountId());
            userIds.add(accountGrant.getUserId());
        }
        if (accountIds.size() > 0 && userIds.size() > 0) {
            //查询账号是否已在授权表中
            List<AccountGrant> dbAccountGrants = accountGrantService.listByAccountIdAndUserIds(accountIds, userIds);
            List<Map<String, Object>> list = new ArrayList<>();
            for (AccountGrant accountGrant : accountGrantList) {
                if (accountGrant.getCanQueryBalance() == null) {
                    accountGrant.setCanQueryBalance(0);
                }
                if (accountGrant.getCanQueryDetail() == null) {
                    accountGrant.setCanQueryDetail(0);
                }
                if (accountGrant.getCanTransferAccounts() == null) {
                    accountGrant.setCanTransferAccounts(0);
                }
                AccountGrant dbAccountGrant = isDbHad(accountGrant, dbAccountGrants);
                if (dbAccountGrant != null) {
                    //排除和数据库相同的账号授权信息
//                if (dbAccountGrant.getCanTransferAccounts().equals(accountGrant.getCanTransferAccounts()) &&
//                        dbAccountGrant.getCanQueryDetail().equals(accountGrant.getCanQueryDetail()) &&
//                        dbAccountGrant.getCanQueryBalance().equals(accountGrant.getCanQueryBalance())) {
//                    continue;
//                }
                    //修正空指针异常
                    if (accountGrant.getCanTransferAccounts().equals(dbAccountGrant.getCanTransferAccounts()) &&
                            accountGrant.getCanQueryDetail().equals(dbAccountGrant.getCanQueryDetail()) &&
                            accountGrant.getCanQueryBalance().equals(dbAccountGrant.getCanQueryBalance()) &&
                            accountGrant.getAccountType().equals(dbAccountGrant.getAccountType())) {
                        continue;
                    }
                    Map<String, Object> tempMap = makeAccountGrantMap(accountGrant);
                    tempMap.put("canQueryBalanceHistory", dbAccountGrant.getCanQueryBalance());
                    tempMap.put("canQueryDetailHistory", dbAccountGrant.getCanQueryDetail());
                    tempMap.put("canTransferAccountsHistory", dbAccountGrant.getCanTransferAccounts());
                    list.add(tempMap);
                } else {
                    if (accountGrant.getCanQueryBalance() == 1 || accountGrant.getCanQueryDetail() == 1 || accountGrant.getCanTransferAccounts() == 1) {
                        Map<String, Object> tempMap = makeAccountGrantMap(accountGrant);
                        tempMap.put("canQueryBalanceHistory", 0);
                        tempMap.put("canQueryDetailHistory", 0);
                        tempMap.put("canTransferAccountsHistory", 0);
                        list.add(tempMap);
                    }
                }
            }
            Map<String, Object> map = Maps.newHashMap();
            map.put("userId", getUserId());
            map.put("accountGrantList", list);
            List<AccountGrant> accountGrants = BeanUtils.copyMapListProperties(list, AccountGrant.class);
            accountGrantService.save(accountGrants, getUserId());
        }
        return success();
    }
    private Map<String, Object> makeAccountGrantMap(AccountGrant accountGrant) {
        Map<String, Object> tempMap = Maps.newHashMap();
        tempMap.put("accountId", accountGrant.getAccountId());
        tempMap.put("userId", accountGrant.getUserId());
        tempMap.put("corpId", accountGrant.getCorpId());
        tempMap.put("accountType", accountGrant.getAccountType());
        tempMap.put("canQueryBalance", accountGrant.getCanQueryBalance());
        tempMap.put("canQueryDetail", accountGrant.getCanQueryDetail());
        tempMap.put("canTransferAccounts", accountGrant.getCanTransferAccounts());
        return tempMap;
    }
    /**
     * 检查新增的数据是否存在数据库中
     *
     * @param accountGrant    验证是否存在对象
     * @param dbAccountGrants 数据库集合
     * @return 有则返回对象，没有则返回null
     */
    private AccountGrant isDbHad(AccountGrant accountGrant, List<AccountGrant> dbAccountGrants) {
        if (CollectionUtils.isEmpty(dbAccountGrants)) {
            return null;
        }
        for (AccountGrant dbAccountGrant : dbAccountGrants) {
            if (accountGrant.getAccountId().equals(dbAccountGrant.getAccountId()) && accountGrant.getUserId().equals(dbAccountGrant.getUserId())) {
                return dbAccountGrant;
            }
        }
        return null;
    }
    /**
     * @param corpId      单位id
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     */
    @GetMapping("/listBy")
    @ResponseBody
    public Object listBy(String corpId, String accountType, String permission) {
        return accountGrantService.listBy(corpId, accountType, permission);
    }
    @PostMapping("/getListBy")
    @ResponseBody
    public List<AccountDTO> getListBy(AccountSearchDTO accountSearchDTO) {
        accountSearchDTO.setUserId(getUserId());
        return this.accountGrantService.listBy(accountSearchDTO);
//        JSONArray json = JSONUtil.readJSONArray(new File("D:\\bytter\\file\\accountList.json"), StandardCharsets.UTF_8);
//        List<AccountDTO> bankAccList = json.toList(AccountDTO.class);
//        return bankAccList;
    }
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        // 查询银行表(bt_bank_acc)时,bankAccOffline查找不到银行账号
        Map<String, Object> formData = dataTablesInput.getFormdata();
        if (null != formData.get("likes_bankAccOffline") && !"".equals(formData.get("likes_bankAccOffline"))) {
            formData.put("likes_bankAcc", formData.get("likes_bankAccOffline"));
        }
        //增加唯一排序解决翻页数据重复问题。
        dataTablesInput.addOrder("bankAcc", true);
        dataTablesInput.getColumn("bankAcc").setOrderable(true);
        return btBankAccServiceI.getDataTablesUnionSYS(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 内部账户信息表 table列表
     *
     * @return
     */
    @PostMapping("/dataTableAllNormal")
    @ResponseBody
    public DataTablesOutput dataTableAllNormal(@RequestBody DataTablesInput dataTablesInput) {
        return fcAccService.getDataTableAllNormalSYS(dataTablesInput, getCurrentUser());
    }
}
