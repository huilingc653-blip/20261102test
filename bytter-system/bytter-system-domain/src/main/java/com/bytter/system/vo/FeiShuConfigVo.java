package com.bytter.system.vo;
import com.bytter.core.utils.EncryptionUtils;
import org.apache.commons.codec.digest.DigestUtils;
public class FeiShuConfigVo {
    private static String formatVer = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
    private String appId;
    private long timestamp;
    private String signature;
    private String noncestr;
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getNoncestr() {
        return noncestr;
    }
    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }
    public static FeiShuConfigVo buildByTicket(String url, String ticket, String appId) {
        String randomString = EncryptionUtils.getRandomString(16);
        long timestamp = System.currentTimeMillis();
        FeiShuConfigVo feiShuConfigVo = new FeiShuConfigVo();
        feiShuConfigVo.setAppId(appId);
        feiShuConfigVo.setTimestamp(timestamp);
        feiShuConfigVo.setNoncestr(randomString);
        String verifyStr = String.format(formatVer, ticket, randomString, timestamp, url);
        feiShuConfigVo.setSignature(DigestUtils.sha1Hex(verifyStr));
        return feiShuConfigVo;
    }
}
