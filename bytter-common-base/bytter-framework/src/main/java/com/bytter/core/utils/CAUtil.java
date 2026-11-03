package com.bytter.core.utils;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.Signature;
import java.security.cert.X509Certificate;
import static com.sainfo.bytter.SainfoVerify.verifySignature;
/**
 * CA验证辅助类
 *
 * @author zuoy
 * @since 1.0
 */
public class CAUtil {
    /**
     * cfca验签方法
     *
     * @param sourceData
     *            签名源
     * @param signData
     *            验签源
     * @return 验签成功 or 验签失败
     * @throws Exception
     *             异常信息
     */
    public boolean verifyCFCA(String sourceData, String signData) throws Exception {
        // 步骤1 加密库初始化
        String deviceName = JCrypto.JSOFT_LIB;
        JCrypto.getInstance().initialize(deviceName, null);
        Session session = JCrypto.getInstance().openSession(deviceName);
        // 步骤2 验签
        Signature signKit = new Signature();
        boolean result = signKit.p7VerifyMessageDetach(sourceData.getBytes("UTF8"), signData.getBytes(), session);
        return result;
    }
    public boolean verifyItrus(String sourceData, String signData) {
        boolean result = true;
        // //天威证书
        // com.itrus.cert.X509Certificate cert = null;
        // String commonName = null;
        // StringBuffer buff = new StringBuffer(); //证书日志
        // StringBuffer rsBuff = new StringBuffer(); //证书错误
        // if (!sourceData.equals("") && !signData.equals("")) {
        // buff.append("原文：" + sourceData + "<br>");
        // buff.append("签名结果：" + signData + "<p>");
        // buff.append("<hr />");
        // try {
        // java.security.cert.X509Certificate x509Cert = SVM.verifySignature(sourceData, signData);
        // cert = com.itrus.cert.X509Certificate.getInstance(x509Cert);
        // int ret = CVM.verifyCertificate(cert);
        // buff.append("Return=[" + ret + "]，");
        // if (ret != CVM.VALID) {
        // rsBuff.append("证书验证结果：");
        // switch (ret) {
        // case CVM.CVM_INIT_ERROR:
        // rsBuff.append("CVM初始化错误，请检查配置文件或给CVM增加支持的CA。");
        // break;
        // case CVM.CRL_UNAVAILABLE:
        // rsBuff.append("CRL不可用，未知状态。");
        // break;
        // case CVM.EXPIRED:
        // rsBuff.append("证书已过期。");
        // break;
        // case CVM.ILLEGAL_ISSUER:
        // rsBuff.append("非法颁发者。");
        // break;
        // case CVM.REVOKED:
        // rsBuff.append("证书已吊销。");
        // break;
        // case CVM.UNKNOWN_ISSUER:
        // rsBuff.append("不支持的颁发者。请检查cvm.xml配置文件");
        // break;
        // case CVM.REVOKED_AND_EXPIRED:
        // rsBuff.append("证书被吊销且已过期。");
        // break;
        // case CVM.ACCOUNT_MISMATCH:
        // rsBuff.append("RA账户不匹配。");
        // break;
        // }
        // }
        // buff.append(rsBuff);
        // buff.append("证书状态有效。");
        // com.itrus.cert.Names certNames = cert.getSubjectNames();
        // commonName = certNames.getItem("CN");
        // buff.append("<h3>取证书主题单项，CN=[" + commonName + "]</h3>");
        // buff.append("证书主题分解如下：<ul>");
        // for (Enumeration e = certNames.keys(); e.hasMoreElements();) {
        // String key = (String) e.nextElement();
        // Vector values = certNames.getItems(key);
        // for (int i = 0; i < values.size(); i++) {
        // buff.append("<li>" + key + "=["
        // + values.elementAt(i) + "]</li>");
        // }
        // }
        // buff.append("</ul>");
        // result = true;
        // } catch (Exception e) {
        // buff.append("签名验证失败：<br>");
        // buff.append("签名原文(Session)：" + sourceData + "<br>");
        // buff.append("签名原文(Post)：" + signData + "<br>");
        // buff.append("错误信息：" + e.toString());
        // e.printStackTrace();
        // }
        // } else {
        // buff.append("客户端没有找到数字证书，用户名+口令登陆……");
        // }
        // if(!StringUtils.isEmpty(rsBuff.toString())){
        // result = false;
        // }
        return result;
    }
    /**
     * 验签
     *
     * @param encrypt
     *            加密方式
     * @param sourceData
     *            签名源
     * @param signData
     *            验签源
     * @return 验签成功 or 验签失败
     * @throws Exception
     *             异常错误
     */
    public boolean verify(String encrypt, String sourceData, String signData) throws Exception {
        boolean result = false;
        if ("1".equals(encrypt)) {
            // 天威证书
            result = verifyItrus(sourceData, signData);
        } else if ("2".equals(encrypt)) {
            result = verifyCFCA(sourceData, signData);
        } else if ("5".equals(encrypt)) {
            result = verifySa(sourceData, signData);
        }
        return result;
    }
    /**
     * 晟安证书后端验证
     * 
     * @param sourceData
     * @param signData
     * @return
     */
    public boolean verifySa(String sourceData, String signData) throws Exception {
        boolean result = false;
        X509Certificate cert = verifySignature(sourceData, signData);
        if (!EmptyUtils.isEmpty(cert)) {
            result = true;
        }
        return result;
    }
}
