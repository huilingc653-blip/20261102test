package com.bytter.system.web.controller;
import com.bytter.basic.entity.BankType;
import com.bytter.basic.service.BtBankTypeCustomService;
import com.bytter.basic.service.BtBankTypeOtherCustomService;
import com.bytter.basic.service.BtBankTypeService;
import com.bytter.basic.vo.BankTypeCustomVO;
import com.bytter.basic.vo.BankTypeOtherCustomVO;
import com.bytter.basic.vo.BankTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.constant.BankConstant;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.api.enums.AttachmentMessageEnum;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.system.service.SysAttachmentService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行类型维护
 * @date 2017-05-17 18:07:34
 */
@Controller
@RequestMapping("/basic/business/btbanktype")
public class BtBankTypeController extends BaseController {
    @Autowired
    private BtBankTypeService btBankTypeService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private Ii18nService i18nServiceI;


    @Autowired
    private BtBankTypeOtherCustomService btBankTypeOtherCustomService;


    @Autowired
    private BtBankTypeCustomService btBankTypeCustomService;
    /**
     * 银行类型维护列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询银行类型维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtBankType";
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BankTypeVO> getListByValidSign() {
        List<BankTypeVO> bankTypeVOS = BeanUtils.copyListProperties(btBankTypeService.getListByValidSign("1"), BankTypeVO.class);
        if (EmptyUtils.isNotEmpty(bankTypeVOS)){
            bankTypeVOS.sort(Comparator.comparing(BankTypeVO::getBankType));
        }
        return bankTypeVOS;
    }
    /**
     * 根据是否境内获取银行类型列表
     *
     * @return
     */
    @GetMapping("/json/{isDomesticBank}")
    @ResponseBody
    public List<BankTypeVO> getListByIsDomestic(@PathVariable String isDomesticBank) {
        List<BankTypeVO> bankTypeVOS = BeanUtils.copyListProperties(btBankTypeService.getListByIsDomesticBank(isDomesticBank), BankTypeVO.class);
        if (EmptyUtils.isNotEmpty(bankTypeVOS)){
            bankTypeVOS.sort(Comparator.comparing(BankTypeVO::getBankType));
        }
        return bankTypeVOS;
    }
    /**
     * 银行类型维护 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankTypeService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 银行类型维护新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(BankType bankType, Model model) {
        if (StringUtils.isNotEmpty(bankType.getId())) {
            bankType = btBankTypeService.get(bankType.getId());
            model.addAttribute("btBankTypePage", bankType);
        }
        return "basic/business/addBtBankType";
    }
    /**
     * 银行类型维护编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(BankType bankType, Model model) {
        if (StringUtils.isNotEmpty(bankType.getId())) {
            bankType = btBankTypeService.get(bankType.getId());
            if (bankType.getIsDomesticBank() == null) {
                bankType.setIsDomesticBank("1");
            }
            BankTypeVO bankTypeVO = new BankTypeVO();
            BeanUtils.copyNotNullProperties(bankType, bankTypeVO);
            bankTypeVO.setRemitMethodName(btBankTypeService.getRemitMethodName(bankTypeVO.getRemitMethod()));
            model.addAttribute("btBankTypePage", bankTypeVO);
        }
        return "basic/business/updateBtBankType";
    }
    /**
     * 银行类型维护详情页
     *
     * @return
     */
    @LogMessage(content = "查询银行类型维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BankType bankType = btBankTypeService.get(id);
        BankTypeVO bankTypeVO = new BankTypeVO();
        BeanUtils.copyNotNullProperties(bankType, bankTypeVO);
        bankTypeVO.setRemitMethodName(btBankTypeService.getRemitMethodName(bankTypeVO.getRemitMethod()));
        model.addAttribute("btBankTypePage", bankTypeVO);
        return "basic/business/viewBtBankType";
    }
    /**
     * 添加银行类型维护
     *
     * @param bankType
     * @return
     */
    @LogMessage(content = "添加银行类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BankType bankType, String fileId) {
        if (btBankTypeService.isExistMulByBankType(bankType.getBankType(), CommonConstant.SUBMIT_ADD, bankType.getId())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKTYPE_UNIQUE);
        }
        if (btBankTypeService.isExistMulByBankPrefix(bankType, CommonConstant.SUBMIT_ADD)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKPREFIX_AND_BANKNAMEPREFIX_UNIQUE);
        }
        // 创建人信息
        if (EmptyUtils.isBlank(bankType.getCreateBy())) {
            bankType.setCreateBy(this.getUserLoginName());
            bankType.setCreateDate(DateUtils.getDate());
        }
        if (org.springframework.util.StringUtils.hasText(bankType.getBankNamePrefix())) {
            bankType.setBankNamePrefix(bankType.getBankNamePrefix().trim());
        }
        btBankTypeService.saveBankType(bankType, fileId);
        return success();
    }
    /**
     * 修改银行类型维护
     *
     * @param bankType
     * @return
     */
    @LogMessage(content = "修改银行类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BankType bankType, String fileId) {
        if (btBankTypeService.isExistMulByBankType(bankType.getBankType(), CommonConstant.SUBMIT_MODIFY, bankType.getId())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKTYPE_UNIQUE);
        }
        if (btBankTypeService.isExistMulByBankPrefix(bankType, CommonConstant.SUBMIT_MODIFY)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKPREFIX_AND_BANKNAMEPREFIX_UNIQUE);
        }
        BankType t = btBankTypeService.get(bankType.getId());
        BeanUtils.copyNotNullProperties(bankType, t);
        // 修改人信息
        t.setUpdateBy(this.getUserLoginName());
        t.setUpdateDate(DateUtils.getDate());
        //按钮控制，清空时间
        if (EmptyUtils.isEmpty(bankType.getNetbankCFCADate())) {
            t.setNetbankCFCADate(null);
        }
        if (org.springframework.util.StringUtils.hasText(t.getBankNamePrefix())) {
            t.setBankNamePrefix(t.getBankNamePrefix().trim());
        }
        btBankTypeService.updateBankType(t, fileId);
        return success();
    }
    /**
     * 删除银行类型维护
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除银行类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BankType bankType = btBankTypeService.get(id);
        btBankTypeService.deleteEntity(bankType);
        return success();
    }
    /**
     * 批量删除银行类型维护
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除银行类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BankType bankType = btBankTypeService.get(id);
            btBankTypeService.deleteEntity(bankType);
        }
        return success();
    }
    /**
     * 上传附件
     */
    @PostMapping("/upload")
    @ResponseBody
    public List<AttachmentDTO> upload(HttpServletRequest request) throws Exception {
        validFileFormat(request);
        List<AttachmentDTO> list = sysAttachmentService.uploadAttachment(request, AttachmentCategoryEnum.CATEGORY_COMMON);
        return list;
    }
    /**
     * 校验文件格式u
     *
     * @param request
     */
    private void validFileFormat(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                //取得当前上传文件的文件名称
                String originalFilename = StringUtils.isNotBlank(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName();
                if (StringUtils.isNotBlank(originalFilename)) {
                    String suffix = FileUtils.getExtend(originalFilename);
                    if (!"png".equals(StringUtils.lowerCase(suffix))) {
                        // 只允许上次png图片
                        throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_FORMAT);
                    }
                }
                try {
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    if (image == null) {
                        // 图片格式不正确
                        throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_FORMAT);
                    }
                } catch (IOException e) {
                    throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_FORMAT);
                }
            }
        }
    }
    /**
     * 银行类型维护列表(根据账号过滤)
     *
     * @return
     */
    @GetMapping("/listFilter")
    @ResponseBody
    public List<BankTypeVO> listBankTypeFilterByBankAcc() {
        List<BankTypeVO> listBankType = btBankTypeService.listBankTypeFilterByBankAcc();
        for (BankTypeVO bankTypeVO : listBankType) {
            String baseContext = i18nServiceI.getBusinessBaseContext(bankTypeVO.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                bankTypeVO.setTypeName(baseContext);
            }
        }
        return listBankType;
    }


    /**
     * 查询单个银行类别信息
     *
     * @return
     */
    @RequestMapping("/getBankType")
    @ResponseBody
    public BankType getBankType(String bankTypeId) {
        return btBankTypeService.get(bankTypeId, true);
    }


    /**
     * 根据银行类型和接口代码获取自定义配置
     *
     * @return
     */
    @RequestMapping("/listBankTypeCustom")
    @ResponseBody
    public List<BankTypeCustomVO> listBankTypeCustom(String bankType, String bifCode, String countryId, String bankPayType, String customType) {
        List<BankTypeCustomVO> bankTypeCustoms = new ArrayList<>();
        if (StringUtils.isBlank(bankType) || StringUtils.isBlank(bifCode)) {
            return bankTypeCustoms;
        }
        if (BankConstant.CUSTOM_TYPE_PAYEETYPE.equals(customType)) {
            bankTypeCustoms = btBankTypeCustomService.listByBankTypeAndBifCode(bankType, bifCode, countryId, bankPayType, customType);
        } else {
            bankTypeCustoms = btBankTypeCustomService.listByBankTypeAndBifCode(bankType, bifCode, countryId, customType);
        }
        return bankTypeCustoms;
    }




    @GetMapping("/listByCondition")
    @ResponseBody
    public List<BankTypeCustomVO> listByCondition(String bankType,
                                                  String bifCode,
                                                  String countryId,
                                                  String isDomesticBank,
                                                  String bankCode,    String payTypeId) {
        if (EmptyUtils.isNotEmpty(countryId)){
            countryId=countryId.replace("_"+getCurrentUser().getTenantId(),"");
        }
        return btBankTypeCustomService.listByCondition(bankType, bifCode, countryId,isDomesticBank,bankCode,payTypeId);
    }


    /**
     * 获取自定义配置
     *
     * @return
     */
    @RequestMapping("/getBankTypeOtherCustom")
    @ResponseBody
    public BankTypeOtherCustomVO getBankTypeOtherCustom(String bankType, String bifCode, String countryId, String remitType, String applayScope) {
        BankTypeOtherCustomVO otherCustomVO = btBankTypeOtherCustomService.getByBankTypeOtherCustom(bankType, bifCode, applayScope, countryId, remitType);
        return otherCustomVO;
    }

    @GetMapping( "/listByBankTypeAndBifCodeAndCountryId")
    @ResponseBody
    public List<BankTypeCustomVO> listByBankTypeAndBifCodeAndCountryId(String bankType, String bifCode, String countryId) {
        if(StringUtils.isEmpty(bankType)||StringUtils.isEmpty(bifCode)||StringUtils.isEmpty(bifCode)){
            return Collections.emptyList();
        }
        return btBankTypeCustomService.listByBankTypeIdAndBifCodeAndCountryId(bankType, bifCode, countryId);
    }
}
