package com.bytter.biz.receipt.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.dto.BisBankReceiptImportDTO;
import com.bytter.biz.payment.api.enums.ReceiptMessageEnum;
import com.bytter.biz.receipt.dao.BisBankReceiptBlendingDao;
import com.bytter.biz.receipt.dao.BisBankReceiptImportDao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.biz.receipt.entity.BisBankReceiptImport;
import com.bytter.biz.receipt.util.OfdParserUtils;
import com.bytter.biz.receipt.util.PDFParserUtils;
import com.bytter.biz.receipt.vo.BisBankReceiptImportVO;
import com.bytter.biz.receipt.vo.BisRunBlendingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IAttachmentService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.commons.io.FilenameUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caop
 * @date 2024/8/2 15:21
 */
@Service("bisBankReceiptImportService")
@Transactional
public class BisBankReceiptImportService extends BaseService<BisBankReceiptImport, String> {

    @Autowired
    private BisBankReceiptImportDao bisBankReceiptImportDao;

    @DubboReference
    private IAttachmentService attachmentServiceImpl;

    @DubboReference
    private IBisAccDtlService bisAccDtlService;

    @Autowired
    private BisBankReceiptBlendingDao bisBankReceiptBlendingDao;

    @DubboReference
    private ICorpService corpService;

    @DubboReference
    private IBtCurrencyService currencyService;

    @Value("${bytter.bfs.files.type.active:}")
    private String activeType;

    public final static String QUERY_RESOURCE = "netbank:receipt:bisbankreceiptImport:query";

    @Override
    public BaseDao<BisBankReceiptImport, String> getEntityDao() {
        return bisBankReceiptImportDao;
    }

    public BisBankReceiptImportVO getById(String id) {
        return bisBankReceiptImportDao.getById(id);
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(QUERY_RESOURCE, user.getId());
        String corpSql = SqlUtil.getInSql(corpList, "bba.corp_id", true);
        return bisBankReceiptImportDao.getDataTablesUnion(dataTablesInput, corpSql);
    }

    public List<BisBankReceiptImport> queryListInId(String receiptId){
        String[] receiptIdArr=receiptId.split(",");
        List<String> ids = new ArrayList<>(Arrays.asList(receiptIdArr));
        return bisBankReceiptImportDao.queryListInId(ids);
    }

    public void importFile(BisBankReceiptImportVO importVO, ICurrentUser user) {
        String groupId = importVO.getAttachmentId();
        List<AttachmentDTO> fileList = attachmentServiceImpl.findAttachmentByGroupId(groupId);
        List<BisBankReceiptImport> list = new ArrayList<>();
        for (AttachmentDTO attachmentDTO : fileList) {
            BisBankReceiptImport tmp = new BisBankReceiptImport();
            BeanUtils.copyNotNullProperties(importVO, tmp);
            tmp.setStatus(CommonConstant.STATUE_CHECK);
            tmp.setAttachmentId(attachmentDTO.getId());
            tmp.setImportDate(DateUtils.getDate());
            tmp.setCreateDate(DateUtils.getDate());
            tmp.setCreateBy(user.getLoginName());
            tmp.setUpdateBy(user.getLoginName());
            tmp.setUpdateDate(DateUtils.getDate());
            tmp.setCurId(BtCurrencyConstant.RMB_INIT_ID);

            //拿到文件后缀
            String extension = attachmentDTO.getSuffix();
            if (!("pdf".equals(extension) || "PDF".equals(extension) || "ofd".equals(extension) || "OFD".equals(extension))) {
                throw new ParameterException(ReceiptMessageEnum.RECEIPT_FILE_TYPE_ERROR);
            }

            try {
                byte[] fileByte = attachmentServiceImpl.getAttachmentDataByid(attachmentDTO.getId());
                String reportText = "";
                if ("pdf".equals(extension) || "PDF".equals(extension)) {
                    reportText = PDFParserUtils.readPdfFileText(fileByte);
                    reportText = reportText.replaceAll("[\\s\\p{Zs}　]+", "");
                }
                if ("ofd".equals(extension) || "OFD".equals(extension)) {
                    reportText = OfdParserUtils.readPdfFileText(fileByte);
                }
                if (!reportText.contains(importVO.getBankAcc()) && importVO.getIsConfirm() == 0) {
                    throw new ParameterException(ReceiptMessageEnum.RECEIPT_BANKACC_NOT_MATCH, attachmentDTO.getName(), importVO.getBankAcc());
                }
                tmp.setReportText(reportText);
                list.add(tmp);
            } catch (ParameterException e) {
                throw e;
            } catch (Exception e) {
                throw new ParameterException(ReceiptMessageEnum.RECEIPT_FILE_ERROR);
            }
        }
        bisBankReceiptImportDao.batchSave(list);
    }

    public DataTablesOutput getDtlDataTablesUnion(DataTablesInput dataTablesInput) {
        return bisAccDtlService.getDataTablesUnionForReceiptBlending(dataTablesInput);
    }

    public void blendDtlBatch(List<BisBankReceiptImport> importDtlList, String dtlId) {
        List<String> fileIds = importDtlList.stream().map(BisBankReceiptImport::getAttachmentId).collect(Collectors.toList());
        List<String> importIds = importDtlList.stream().map(BisBankReceiptImport::getId).collect(Collectors.toList());
        List<AttachmentDTO> attachmentDTO= attachmentServiceImpl.findAttachmentByIds(fileIds);
            //根据流水信息生成电子回单表数据
            BisAccDtlDTO accDtlDTO = bisAccDtlService.getById(dtlId);
            BisBankReceiptDtl receiptDtl = new BisBankReceiptDtl();
            receiptDtl.setCdSign(accDtlDTO.getCdSign());
            if (NisBusTypeConstant.CD_SIGN_PAYER.equals(accDtlDTO.getCdSign())) {
                receiptDtl.setPayAccNo(accDtlDTO.getBankAcc());
                receiptDtl.setPayAccName(accDtlDTO.getAccName());
                receiptDtl.setPayBankName(accDtlDTO.getBankName());
                receiptDtl.setPayeeAccNo(accDtlDTO.getOppAccNo());
                receiptDtl.setPayeeAccName(accDtlDTO.getOppAccName());
                receiptDtl.setPayeeBankName(accDtlDTO.getOppAccBank());
            } else {
                receiptDtl.setPayAccNo(accDtlDTO.getOppAccNo());
                receiptDtl.setPayAccName(accDtlDTO.getOppAccName());
                receiptDtl.setPayBankName(accDtlDTO.getOppAccBank());
                receiptDtl.setPayeeAccNo(accDtlDTO.getBankAcc());
                receiptDtl.setPayeeAccName(accDtlDTO.getAccName());
                receiptDtl.setPayeeBankName(accDtlDTO.getBankName());
            }
            receiptDtl.setBifCode(accDtlDTO.getBifCode());
            receiptDtl.setTransAmt(accDtlDTO.getAmt());
            receiptDtl.setCurId(accDtlDTO.getCurId());
            receiptDtl.setUsefor(accDtlDTO.getUses());
            receiptDtl.setRemark(accDtlDTO.getPostscript());
            receiptDtl.setAbs(accDtlDTO.getRemark());
            receiptDtl.setBusType(accDtlDTO.getBusType());
            receiptDtl.setTransSerialNo(accDtlDTO.getBankSerialNo());
            receiptDtl.setIsFrom("2");//2为手工导入
            receiptDtl.setFiletype(attachmentDTO.get(0).getType());
            String path=attachmentDTO.stream()
                    .map(AttachmentDTO::getPath)
                    .collect(Collectors.joining(","));

            receiptDtl.setFilePath(path);
            // 离线流水，通过附件获取
            receiptDtl.setSaveType("0");
            receiptDtl.setCharageDate(accDtlDTO.getTransTime());
            bisBankReceiptImportDao.save(receiptDtl);
        //更新离线电子回单表中的回单编号和勾对标识
            for (BisBankReceiptImport receiptImport:importDtlList){
                receiptImport.setReceiptId(receiptDtl.getId());
                receiptImport.setBlendSign("1");
                receiptImport.setAccDtlId(dtlId);
                bisBankReceiptImportDao.update(receiptImport);
            }
        this.bisBankReceiptImportDao.flush();
//        bisBankReceiptImportDao.updateReceiptIdImportBatch(importIds, receiptDtl.getId(), dtlId);
        //插入流水和电子回单关系
        bisBankReceiptImportDao.saveBisReceiptDtl(dtlId, receiptDtl.getId(), null);
        //更新勾兑标志为已勾兑
        bisBankReceiptImportDao.updateBlendSignForDtl(dtlId, BtBankAccConstant.BLENDSIGN_1);
        bisBankReceiptImportDao.updateBlendSign(receiptDtl.getId(), BtBankAccConstant.BLENDSIGN_1);
    }

    public void blendDtl(BisBankReceiptImport importDtl, String dtlId) {
        String bankReceiptDtlId = importDtl.getReceiptId();
        AttachmentDTO receiptDtlDTO= attachmentServiceImpl.findAttachmentById(importDtl.getAttachmentId());
        if (EmptyUtils.isEmpty(bankReceiptDtlId)) {
            //根据流水信息生成电子回单表数据
            BisAccDtlDTO accDtlDTO = bisAccDtlService.getById(dtlId);
            BisBankReceiptDtl receiptDtl = new BisBankReceiptDtl();
            receiptDtl.setCdSign(accDtlDTO.getCdSign());
            if (NisBusTypeConstant.CD_SIGN_PAYER.equals(accDtlDTO.getCdSign())) {
                receiptDtl.setPayAccNo(accDtlDTO.getBankAcc());
                receiptDtl.setPayAccName(accDtlDTO.getAccName());
                receiptDtl.setPayBankName(accDtlDTO.getBankName());
                receiptDtl.setPayeeAccNo(accDtlDTO.getOppAccNo());
                receiptDtl.setPayeeAccName(accDtlDTO.getOppAccName());
                receiptDtl.setPayeeBankName(accDtlDTO.getOppAccBank());
            } else {
                receiptDtl.setPayAccNo(accDtlDTO.getOppAccNo());
                receiptDtl.setPayAccName(accDtlDTO.getOppAccName());
                receiptDtl.setPayBankName(accDtlDTO.getOppAccBank());
                receiptDtl.setPayeeAccNo(accDtlDTO.getBankAcc());
                receiptDtl.setPayeeAccName(accDtlDTO.getAccName());
                receiptDtl.setPayeeBankName(accDtlDTO.getBankName());
            }
            receiptDtl.setBifCode(accDtlDTO.getBifCode());
            receiptDtl.setTransAmt(accDtlDTO.getAmt());
            receiptDtl.setCurId(accDtlDTO.getCurId());
            receiptDtl.setUsefor(accDtlDTO.getUses());
            receiptDtl.setRemark(accDtlDTO.getPostscript());
            receiptDtl.setAbs(accDtlDTO.getRemark());
            receiptDtl.setBusType(accDtlDTO.getBusType());
            receiptDtl.setTransSerialNo(accDtlDTO.getBankSerialNo());
            receiptDtl.setIsFrom("2");//手工导入
            receiptDtl.setFiletype(receiptDtlDTO.getType());
            receiptDtl.setFilePath(receiptDtlDTO.getPath());
            // 离线流水，通过附件获取
            receiptDtl.setSaveType("0");
            receiptDtl.setCharageDate(accDtlDTO.getTransTime());
            bisBankReceiptImportDao.save(receiptDtl);
            bankReceiptDtlId = receiptDtl.getId();
        }
        this.bisBankReceiptImportDao.flush();
        //更新离线电子回单表中的回单编号和勾对标识
        bisBankReceiptImportDao.updateReceiptIdImport(importDtl.getId(), bankReceiptDtlId, dtlId);
        //插入流水和电子回单关系
        bisBankReceiptImportDao.saveBisReceiptDtl(dtlId, bankReceiptDtlId, null);
        //更新勾兑标志为已勾兑
        bisBankReceiptImportDao.updateBlendSignForDtl(dtlId, BtBankAccConstant.BLENDSIGN_1);
        bisBankReceiptImportDao.updateBlendSign(bankReceiptDtlId, BtBankAccConstant.BLENDSIGN_1);
    }

    public void cancelBlending(String dtlId) {
        String[] dtlIds = dtlId.split(",");
        //所有的流水id
        List<String> dtlIdList = new ArrayList<>();
        //所有的业务id
        List<String> receiptIdList = new ArrayList<>();
        for (int i = 0; i < dtlIds.length; i++) {
            List<String> tempList = bisBankReceiptBlendingDao.getReceiptId(dtlIds[i]);
            receiptIdList.addAll(tempList);
            dtlIdList.add(dtlIds[i]);
        }
        for (String receiptIdStr : receiptIdList) {
            List<String> tempList = bisBankReceiptBlendingDao.getDtlId(receiptIdStr);
            dtlIdList.addAll(tempList);
        }
        for (String dtlIdStr : dtlIdList) {
            this.bisBankReceiptBlendingDao.deleteDtlId(dtlIdStr);
            //更新勾兑标志为未勾兑
            bisBankReceiptBlendingDao.toUpdateBlendSignForDtl(dtlIdStr, BtBankAccConstant.BLENDSIGN_0);
        }
        for (String receiptIdStr : receiptIdList) {
            //删除中间关联表
            this.bisBankReceiptBlendingDao.deleteBisReceiptDtl(receiptIdStr);
            this.bisBankReceiptBlendingDao.deleteBisBankReceiptDtl(receiptIdStr);
            //更新勾兑标志为未勾兑
            this.bisBankReceiptBlendingDao.toUpdateBlendSign(receiptIdStr, BtBankAccConstant.BLENDSIGN_0);
            bisBankReceiptImportDao.cancelBlendForImport(receiptIdStr);
        }
    }

    public void deleteById(String id, ICurrentUser user) {
        BisBankReceiptImport importDtl = get(id, true);
        importDtl.setStatus(CommonConstant.STATUE_DELETE);
        importDtl.setUpdateBy(user.getLoginName());
        importDtl.setUpdateDate(DateUtils.getDate());
        saveOrUpdate(importDtl);
    }

    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.deleteById(id, user);
        }
    }

    public void previewFile(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        BisBankReceiptImport importDtl = get(id, true);
        String fileId = importDtl.getAttachmentId();
        AttachmentDTO attachmentDTO = attachmentServiceImpl.previewAttachmentFromPath(id, request.getServletContext().getRealPath("/"), request.getRequestURL());
    }

    public BisBankReceiptImportDTO getByReceiptIdAndDtlId(String receiptId, String dtlId) {
        return bisBankReceiptImportDao.getByReceiptIdAndDtlId(receiptId, dtlId);
    }

    public void dtlExportReceipt(String attachmentId,String dtlId,ICurrentUser user) {
        List<AttachmentDTO> dtoList=attachmentServiceImpl.findAttachmentByGroupId(attachmentId);
        if (dtoList.size()<1){
            throw new ParameterException(ReceiptMessageEnum.RECEIPT_FILE_ERROR);
        }
        List<BisBankReceiptImport> tmpList=new ArrayList<>();
        for (AttachmentDTO attachment:dtoList){
            BisAccDtlDTO accDtlDTO = bisAccDtlService.getById(dtlId);
            BisBankReceiptImport tmp = new BisBankReceiptImport();
            tmp.setBankAcc(accDtlDTO.getBankAcc());
            tmp.setBifCodeId(accDtlDTO.getBifCode());
            tmp.setCurId(accDtlDTO.getCurId());
            tmp.setAttachmentId(attachment.getId());
            tmp.setType(attachment.getType());
            tmp.setPath(attachment.getFilePath());
            tmp.setStatus(CommonConstant.STATUE_CHECK);
            tmp.setImportDate(DateUtils.getDate());
            tmp.setCreateDate(DateUtils.getDate());
            tmp.setCreateBy(user.getLoginName());
            tmp.setUpdateBy(user.getLoginName());
            tmp.setUpdateDate(DateUtils.getDate());
            tmp.setCurId(BtCurrencyConstant.RMB_INIT_ID);

            //拿到文件后缀
            String extension = attachment.getSuffix();
            if (!("pdf".equals(extension) || "PDF".equals(extension) || "ofd".equals(extension) || "OFD".equals(extension))) {
                throw new ParameterException(ReceiptMessageEnum.RECEIPT_FILE_TYPE_ERROR);
            }
            try {
                byte[] fileByte = attachmentServiceImpl.getAttachmentDataByid(attachment.getId());
                String reportText = "";
                if ("pdf".equals(extension) || "PDF".equals(extension)) {
                    reportText = PDFParserUtils.readPdfFileText(fileByte);
                    reportText = reportText.replaceAll("[\\s\\p{Zs}　]+", "");
                }
                if ("ofd".equals(extension) || "OFD".equals(extension)) {
                    reportText = OfdParserUtils.readPdfFileText(fileByte);
                }
                tmp.setReportText(reportText);
            } catch (ParameterException e) {
                throw e;
            } catch (Exception e) {
                throw new ParameterException(ReceiptMessageEnum.RECEIPT_FILE_ERROR);
            }
            bisBankReceiptImportDao.save(tmp);
            tmpList.add(tmp);
        }
        blendDtlBatch(tmpList, dtlId);
    }


    public void runBlending(BisRunBlendingVO bisRunBlendingVO) {
        List<BisAccDtlDTO> bisAccDtlDTOS = bisAccDtlService.getByTransTime(bisRunBlendingVO.getGed_tradeTime(), bisRunBlendingVO.getLed_tradeTime());
        List<BisBankReceiptImportVO> importDTOS = bisBankReceiptImportDao.queryBisBankReceiptImportBySign("0");

        Map<String, List<BisBankReceiptImportVO>> groupedMap = importDTOS.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.getBankAcc()
                ));

        for (BisAccDtlDTO dtl : bisAccDtlDTOS) {

            List<BisBankReceiptImportVO> groupedDto = groupedMap.get(dtl.getBankAcc());
            if (groupedDto==null || groupedDto.size()==0){
                continue;
            }
            String[] matchingRule = bisRunBlendingVO.getMatchingRule().split(",");
            BisBankReceiptImportVO dto = matchingAlgorithm(groupedDto, matchingRule, bisRunBlendingVO.getTransDays(), dtl);
            if (dto != null) {
                Iterator<BisBankReceiptImportVO> iterator = groupedDto.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId().equals(dto.getId())) {
                        iterator.remove();
                    }
                }
                BisBankReceiptImport receiptImport = new BisBankReceiptImport();
                BeanUtils.copyNotNullProperties(dto, receiptImport);
                blendDtl(receiptImport, dtl.getId());
            }
        }


    }

    public BisBankReceiptImportVO matchingAlgorithm(List<BisBankReceiptImportVO> importDTOList, String[] matchingRule, String transDays, BisAccDtlDTO dtl) {
        for (BisBankReceiptImportVO receipt : importDTOList) {
            Boolean flag = true;
            for (int i = 0; i < matchingRule.length; i++) {
                //拿到文件后缀
                String extension =  FilenameUtils.getExtension(receipt.getFileName());
                if ("pdf".equals(extension)||"PDF".equals(extension)){
                    flag = isAlgorithmPDF(receipt, matchingRule[i], transDays, dtl);
                }else if ("ofd".equals(extension) || "OFD".equals(extension)) {
                    flag = isAlgorithmOFD(receipt, matchingRule[i], transDays, dtl);
                }else{
                    flag=false;
                }
                if (!flag){
                    break;
                }
            }
            if (flag) {
                return receipt;
            }
        }
        return null;
    }

    public boolean isAlgorithmOFD(BisBankReceiptImportVO receipt, String matchingRule, String transDays, BisAccDtlDTO dtl) {
        if (StringUtils.isEmpty(receipt.getReportText())){
            return false;
        }
        JSONObject result = JSON.parseObject(receipt.getReportText());
        switch (matchingRule) {
            //交易金额
            case "1":
                String transactionAmountInFigures = result.getString("TransactionAmountInFigures");
                return isAlgorithmAmt(transactionAmountInFigures, dtl.getAmt());
            //付款账号
            case "2":
                String accountNumberOfPayer = result.getString("AccountNumberOfPayer");
                if("0".equals(dtl.getCdSign())){
                    return StringUtils.isNotEmpty(dtl.getOppAccNo()) && accountNumberOfPayer.equals(dtl.getOppAccNo());
                }else{
                    return StringUtils.isNotEmpty(dtl.getBankAcc()) && accountNumberOfPayer.equals(dtl.getBankAcc());
                }

            //收款账号
            case "3":
                String accountNumberOfPayee = result.getString("AccountNumberOfPayee");
                if("0".equals(dtl.getCdSign())){
                    return StringUtils.isNotEmpty(dtl.getBankAcc()) && accountNumberOfPayee.equals(dtl.getBankAcc());
                }else{
                    return StringUtils.isNotEmpty(dtl.getOppAccNo()) && accountNumberOfPayee.equals(dtl.getOppAccNo());
                }
            //交易日期
            case "4":
                String issueDate = result.getString("IssueDate");
                return isAlgorithmTransTime(issueDate, dtl.getTransTime(), transDays);
            //币别
            case "5":
                String currency = result.getString("Currency");
                return StringUtils.isNotEmpty(dtl.getCurId()) && isAlgorithmCur(currency, dtl.getCurId());
            //用途
            case "6":
                String notes = result.getString("Notes");
                return StringUtils.isNotEmpty(dtl.getUses()) && notes.contains(dtl.getUses());
            default:
                return false;
        }
    }


    public boolean isAlgorithmPDF(BisBankReceiptImportVO receipt, String matchingRule, String transDays, BisAccDtlDTO dtl) {
        if (StringUtils.isEmpty(receipt.getReportText())){
            return false;
        }
        switch (matchingRule) {
            //交易金额
            case "1":
                return isAlgorithmAmt(receipt.getReportText(), dtl.getAmt());
            //付款账号
            case "2":
                return isBankAcc(dtl.getBankAcc() ,dtl.getBankAcc());
            //收款账号
            case "3":
                return isBankAcc(receipt.getReportText(),dtl.getOppAccNo());
            //交易日期
            case "4":
                return isAlgorithmTransTime(receipt.getReportText(), dtl.getTransTime(), transDays);
            //币别
            case "5":
                return StringUtils.isNotEmpty(dtl.getCurId()) && isAlgorithmCur(receipt.getReportText(), dtl.getCurId());
            //用途
            case "6":
                return StringUtils.isNotEmpty(dtl.getUses()) && receipt.getReportText().contains(dtl.getUses());
            default:
                return false;
        }
    }
    private boolean isBankAcc(String reportText, String bankAcc) {
        if (bankAcc==null){
            return false;
        }
        if(reportText.contains(bankAcc))
        {
            return true;
        }
        if (bankAcc.length() == 12 && reportText.contains(formatBankAccount(bankAcc))){
            return true;
        }
        return false;
    }

    public static String formatBankAccount(String bankAcc) {
        // 使用substring进行字符串截取并拼接
        return bankAcc.substring(0, 3) + "-" +
                bankAcc.substring(3, 9) + "-" +
                bankAcc.substring(9);
    }

    private boolean isAlgorithmAmt(String reportText, BigDecimal amt) {
        if (amt==null){
            return false;
        }
        return reportText.contains(amt.toString())
                || reportText.contains(NumberUtil.formatDoubles(amt, 2))
                || reportText.contains(NumberUtil.formatNumeric(amt))
                || reportText.contains(StringUtils.chineseMoney(amt))
                ;
    }

    private boolean isAlgorithmCur(String reportText, String curId) {
        if (curId==null){
            return false;
        }
        BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(curId);
        return     reportText.contains(btCurrencyDTO.getCurName())
                || (StringUtils.isNotEmpty(btCurrencyDTO.getEnglishCode()) && reportText.contains(btCurrencyDTO.getEnglishCode()))
                || (StringUtils.isNotEmpty(btCurrencyDTO.getPbScaleCode()) && reportText.contains(btCurrencyDTO.getPbScaleCode()))
                || (StringUtils.isNotEmpty(btCurrencyDTO.getSysEnglishCode()) && reportText.contains(btCurrencyDTO.getSysEnglishCode()))
                ;
    }

    private boolean isAlgorithmTransTime(String reportText, Date transTime, String transDays) {
        if (transTime==null){
            return false;
        }
        Integer days = Integer.valueOf(transDays);
        List<Date> transTimeList = generateDateRange(transTime, days);
        for (Date time : transTimeList) {
            if (reportText.contains(DateUtils.format(time, DateUtils.PATTERN_CH_SIMPLE))
                    || reportText.contains(DateUtils.format(time, DateUtils.PATTERN_CLASSICAL_SIMPLE))
                    || reportText.contains(DateUtils.format(time, "dd/MM/yyyy"))
                    || reportText.contains(DateUtils.format(time, "yyyy/MM/dd"))) {
                return true;
            }
        }
        return false;

    }

    /**
     * 生成以指定日期为中心，前后各days天的日期列表
     *
     * @param transTime 中心日期Date对象
     * @param days      前后生成的天数
     * @return 包含所有日期的Date对象列表
     */
    public List<Date> generateDateRange(Date transTime, int days) {
        List<Date> dateList = new ArrayList<>();

        // 将Date转换为LocalDate
        LocalDate centerDate = transTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 添加中心日期之前的days天
        for (int i = days; i > 0; i--) {
            LocalDate previousDate = centerDate.minusDays(i);
            dateList.add(convertToDate(previousDate));
        }

        // 添加中心日期
        dateList.add(transTime);

        // 添加中心日期之后的days天
        for (int i = 1; i <= days; i++) {
            LocalDate nextDate = centerDate.plusDays(i);
            dateList.add(convertToDate(nextDate));
        }

        return dateList;
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
