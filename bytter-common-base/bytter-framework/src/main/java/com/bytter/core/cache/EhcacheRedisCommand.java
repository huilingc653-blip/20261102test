package com.bytter.core.cache;
import java.io.Serializable;
import java.util.Random;
/**
 * 用在订阅、广播模式，清除一级缓存的命令类 Created by zhoul on 2017/12/22.
 */
public class EhcacheRedisCommand implements Serializable {
    private final static int SRC_ID = genRandomSrc(); // 命令源标识，随机生成
    public final static byte OPERATOR_DELETE_KEY = 0x01; // 删除缓存
    public final static byte OPERATOR_CLEAR_KEY = 0x02; // 清除缓存
    private int src;
    private byte operator;
    private String region;
    private Object key;
    private static int genRandomSrc() {
        long ct = System.currentTimeMillis();
        Random rnd_seed = new Random(ct);
        return (int) (rnd_seed.nextInt(10000) * 1000 + ct % 1000);
    }
    public EhcacheRedisCommand(byte operator, String region, Object key) {
        this.operator = operator;
        this.region = region;
        this.key = key;
        this.src = SRC_ID;
    }
    public boolean isLocalCommand() {
        return this.src == SRC_ID;
    }
    public byte getOperator() {
        return operator;
    }
    public void setOperator(byte operator) {
        this.operator = operator;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public Object getKey() {
        return key;
    }
    public void setKey(Object key) {
        this.key = key;
    }
    public int getSrc() {
        return src;
    }
}
