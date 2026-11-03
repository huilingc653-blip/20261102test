package com.bytter.biz.erp.controller;


import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.FileCacheManagerUtils;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.erp.constant.CommonConstant;
import com.bytter.erp.enums.ResourceTemplateEnum;
import com.bytter.erp.service.IResourceTemplateEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/erp/bankReceipt")
public class BisBankReceiptErpController {

    @DubboReference(check = false)
    private IBisBankReceiptDtlService bisBankReceiptDtlService;

    /**
     * 跳转回单查看页面
     *
     * @return
     */
    @GetMapping("/showImg")
    public String showImg(String id,String payId, Model model) {
        List<String> listId = new ArrayList<>();
        if(StringUtils.isEmpty(id)&&StringUtils.isNotEmpty(payId)){
            List<String> idList =bisBankReceiptDtlService.getIdsByPayId(payId);
            listId.addAll(idList);
            if(listId.size()==0){
                listId.add(payId);
            }
        }else{
            for (String idInfo : id.split(CommonConstant.COMMA_SPLIT)) {
                listId.add(idInfo);
            }
        }
        model.addAttribute("listId", listId);
        return "erp/bankbill/viewEBankBillBatch";
    }

    /**
     * 预览页面加载图片
     *
     * @return
     */
    @GetMapping("/initImg")
    public void initImg(String id, ServletRequest request, ServletResponse response) {
        bisBankReceiptDtlService.initImg(id,(HttpServletRequest)request,(HttpServletResponse)response);
    }


    /**
     * 电子打印控件
     *
     * @return
     */
    @GetMapping("/download/resource")
    public void downloadResource(String code, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(code)
                ||!(ResourceTemplateEnum.EXPORT_LODOP_CLODOP_SETUP.getCode().equals(code)
                    ||ResourceTemplateEnum.EXPORT_LODOP_INSTALL_LODOP32.getCode().equals(code)
                    ||ResourceTemplateEnum.EXPORT_LODOP_INSTALL_LODOP64.getCode().equals(code)
                    )) {
            return;
        }
        IResourceTemplateEnum templateEnum = ResourceTemplateEnum.getResourceTemplate(code);
        if (templateEnum==null) {
            //logger.error("模板文件未找到..");
            return;
        }
        //获取相对路径
        String path = templateEnum.getPath();
        if (StringUtils.isBlank(path)) {
            return;
        }

        String fName = path.trim();
        String fileName = fName.substring(fName.lastIndexOf("/") + 1);
        String codedFileName = templateEnum.getChineseName();
        if(StringUtils.isBlank(codedFileName)){
            codedFileName = FileUtils.getFilePrefix(fileName);
        }
        String suffix = FileUtils.getExtend(fileName);
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + "." + suffix);
        InputStream is = FileCacheManagerUtils.getFile(path);
        if (is == null) {
            return;
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
