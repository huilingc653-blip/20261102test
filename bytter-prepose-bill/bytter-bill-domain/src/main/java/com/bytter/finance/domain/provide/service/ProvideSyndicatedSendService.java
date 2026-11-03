package com.bytter.finance.domain.provide.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.finance.constant.ProvideConstant;
import com.bytter.finance.domain.provide.dao.ProvideSyndicatedSendDao;
import com.bytter.finance.domain.provide.entity.ProvideSyndicatedSend;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedSendVo;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @author onlineGeneratortempOrSubmit
 * @version 1.0
 * @Title: Service
 * @Description: 银团贷款发放表
 * @date 2022-07-22 10:45:09
 */
@Service("provideSyndicatedSendService")
@Transactional
public class ProvideSyndicatedSendService extends BaseService<ProvideSyndicatedSend, String>
        implements IUserExcelVerifyHandler<ProvideSyndicatedSendVo> {
    @Autowired
    private ProvideSyndicatedSendDao provideSyndicatedSendDao;
    @DubboReference(check = false)
    private IInputBankInfoService bankInfoService;
    @Override
    public BaseDao<ProvideSyndicatedSend, String> getEntityDao() {
        return provideSyndicatedSendDao;
    }
    /**
     * 通过发放信息获取银团发放数据
     * 
     * @param provideLoanSendId
     * @return
     */
    public List<ProvideSyndicatedSendVo> getSendByProvideLoanSendId(String provideLoanSendId) {
        List<ProvideSyndicatedSendVo> list = provideSyndicatedSendDao.getSendByProvideLoanSendId(provideLoanSendId);
        return list;
    }
    /**
     * 导入校验方法
     *
     * @return
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(ProvideSyndicatedSendVo provideSyndicatedSendVo, Object... objects) {
        return null;
    }
    /**
     * 导入校验方法
     *
     * @param list
     *            当前集合对象 最后将有错误的信息调用T里的addErrorMsg方法
     */
    @Override
    public void verifyHandler(List<ProvideSyndicatedSendVo> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (ProvideSyndicatedSendVo vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 是否牵头行
                if (vo.getLeadingBank().equals("是")) {
                    vo.setLeadingBank("是");
                } else {
                    vo.setLeadingBank("否");
                }
                // 银行开户行号
                InputBankInfoDTO inputBankInfoDTO = bankInfoService.getByBankCode(vo.getBankCode());
                if (inputBankInfoDTO != null) {
                    if (EmptyUtils.isEmpty(vo.getBankCode())) {
                        errorMsgList.add(ProvideConstant.CMS_PROVIDE_BANKCODE_ISNULL);
                    }
                } else {
                    errorMsgList.add(ProvideConstant.CMS_PROVIDE_BANKCODE_NOTEXIST);
                }
                // 银行名称
                if (EmptyUtils.isEmpty(vo.getBankName())) {
                    errorMsgList.add(ProvideConstant.CMS_PROVIDE_BANKNAME_ISNULL);
                } else {
                    if (inputBankInfoDTO != null && !vo.getBankName().equals(inputBankInfoDTO.getBankName())) {
                        errorMsgList.add(ProvideConstant.CMS_PROVIDE_BANKCODE_NOTMATCH);
                    }
                }
                // 本次放款
                if (EmptyUtils.isEmpty(vo.getSendMoney())) {
                    errorMsgList.add(ProvideConstant.CMS_PROVIDE_SENDMONEY_ISNULL);
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    vo.addErrorMsg(errorMsgList);
                }
            }
        }
    }
}
