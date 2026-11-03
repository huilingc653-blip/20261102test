package com.bytter.biz.receipt.util;

import api.VoucherFileInfo;
import api.VoucherFileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class OfdParserUtils {

    public static String readPdfFileText(byte[] fileByte) throws Exception {
        // 创建临时文件写入流数据
        String fileId = UUID.randomUUID().toString().replace("-","");
        Path tempFile = Files.createTempFile("temp_"+fileId, ".ofd");
        try (OutputStream os = Files.newOutputStream(tempFile)) {
            os.write(fileByte); // 假设ofdByteArray是传入的字节流
        }
        String json= readPdfFileText(tempFile.toString());

        // 最后删除临时文件
        Files.deleteIfExists(tempFile);
        return json;
    }

    public static String readPdfFileText(String ofdPath) throws Exception {
        VoucherFileInfo vfi = VoucherFileUtil.extractXBRLFromOFD(ofdPath, ofdPath.replaceAll(".ofd", ".xml"));
        String xbrlPath = ofdPath.replaceAll(".ofd", ".xml");
        String configId = vfi.getVoucherType();
        String xmlStr = readFile(xbrlPath, "UTF-8");
        JSONObject json = VoucherFileUtil.xbrl2Json(xmlStr, configId);
        JSONObject result = JSON.parseObject(json.toJSONString());
        return result.toJSONString();
    }

//        // 收款人账户名称
//        String accountNameOfPayee = result.getString("AccountNameOfPayee");
//        // 付款人的账户名称
//        String accountNameOfPayer = result.getString("AccountNameOfPayer");
//        // 收款人账号
//        String accountNumberOfPayee = result.getString("AccountNumberOfPayee");
//        // 付款人账号
//        String accountNumberOfPayer = result.getString("AccountNumberOfPayer");
//        // 收件人帐号
//        String accountNumberOfRecipient = result.getString("AccountNumberOfRecipient");
//        // 业务产品类型
//        String businessProductType = result.getString("BusinessProductType");
//        String channelType = result.getString("ChannelType");
//        // 币种
//        String currency = result.getString("Currency");
//        // 记账货币日期
//        String dateOfBookkeeping = result.getString("DateOfBookkeeping");
//        // 发卡机构识别码
//        String identificationCodeOfIssuer = result.getString("IdentificationCodeOfIssuer");
//        // 收件人标识号
//        String identificationNumberOfRecipient = result.getString("IdentificationNumberOfRecipient");
//        // 现金或转账的识别
//        String identificationOfCashOrTransfer = result.getString("IdentificationOfCashOrTransfer");
//        // 贷方或借方的识别
//        String cdSign = result.getString("IdentificationOfCreditOrDebit");
//        // 识别代码
//        String identifyingCode = result.getString("IdentifyingCode");
//        // 交易日期
//        String issueDate = result.getString("IssueDate");
//        // 记账日记账
//        String journalAccountOfBookkeeping = result.getString("JournalAccountOfBookkeeping");
//        // 备注用途
//        String notes = result.getString("Notes");
//        // 银行电子收据编号
//        String numberOfBankElectronicReceipt = result.getString("NumberOfBankElectronicReceipt");
//        // 收款人开户银行
//        String openingBankOfPayee = result.getString("OpeningBankOfPayee");
//        // 付款人开户银行
//        String openingBankOfPayer = result.getString("OpeningBankOfPayer");
//        // 交易金额
//        String transactionAmountInFigures = result.getString("TransactionAmountInFigures");
//        //银行电子收据类型
//        String typeOfBankElectronicReceipt = result.getString("TypeOfBankElectronicReceipt");
//        System.out.println(result.toJSONString());



    public static String readFile(String path, String charSet) {
        StringBuffer sb = null;
        FileInputStream fin = null;
        BufferedReader in = null;
        try {
            sb = new StringBuffer();
            fin = new FileInputStream(path);
             in = new BufferedReader(new InputStreamReader(fin,
                    charSet));
            char[] temp = new char[1024];

            int count = 0;
            while ((count = in.read(temp)) != -1) {
                sb.append(temp, 0, count);
            }

            return new String(sb);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
