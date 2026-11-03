//package com.bytter.biz.inneraccount.service;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.bytter.core.base.BaseDao;
//import com.bytter.core.base.BaseService;
//import com.bytter.core.base.entity.ShiroUser;
//import com.bytter.core.common.datatable.DataTablesInput;
//import com.bytter.core.common.datatable.DataTablesOutput;
//import com.bytter.system.api.ITaskService;
//import com.bytter.system.api.IUserService;
//import com.bytter.system.api.dto.UserDTO;
//import com.bytter.biz.inneraccount.dao.BisAccDtlBudgetRuleDao;
//import com.bytter.system.vo.BisAccDtlBudgetRuleVO;
//TODO 放到资金核对
//@Service("bisAccDtlBudgetRulePayService")
//@Transactional
//public class BisAccDtlBudgetRulePayService extends BaseService<BisAccDtlBudgetRuleVO, String> implements ITaskService {
//    @Autowired
//    private BisAccDtlBudgetRuleDao bisAccDtlBudgetRuleDao;
//    @Autowired
//    private BisAccDtlBudgetRuleService bisAccDtlBudgetRuleService;
//    @Autowired(required=false)
//    private IUserService userService;
//    @Override
//    public BaseDao<BisAccDtlBudgetRuleVO, String> getEntityDao() {
//        return bisAccDtlBudgetRuleDao;
//    }
//
//    @Override
//@XxlJob("BisAccDtlBudgetRulePayService")
//    public void run(String arguments) {
//        try {
//            this.logger.info("---启用自动认领其他付款流水定时任务---");
//            DataTablesInput dataTablesInput = new DataTablesInput();
//            Map<String, Object> map = new HashMap<>();
//            map.put("eqs_cdSign","1");
//            map.put("eqs_fbsStatus","0");
//            dataTablesInput.setFormdata(map);
//            DataTablesOutput output = bisAccDtlBudgetRuleDao.getDataTablesUnion(dataTablesInput, null);
//            //默认管理员执行
//            UserDTO userDTO = this.userService.getByLoginName("admin");
//            ShiroUser user = new ShiroUser(userDTO.getId(),userDTO.getLoginName(),userDTO.getUsername());
//            if (null != output.getData()) {
//                for (int i = 0; i < output.getData().size(); i++) {
//                    BisAccDtlBudgetRuleVO vo = (BisAccDtlBudgetRuleVO) output.getData().get(i);
//                    vo.setProcessDateTime(vo.getTransTime());
//                    bisAccDtlBudgetRuleService.autoRuleOther(vo,user);
//                }
//            }
//            this.logger.info("---结束自动认领其他付款流水定时任务---");
//        } catch (Exception e) {
//            this.logger.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
//    }
//}
