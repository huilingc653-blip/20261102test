package com.bytter.biz.fundtransfer.util;

import com.bytter.biz.fundtransfer.enums.TradeTypeEnum;
import com.google.common.base.Strings;

import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * 调拨工具类
 *
 * @author : ZDB
 * @version : v1.0
 * @createTime : 2023/3/15 18:22
 */
public class TransferUtil {

    /**
     * 字符串md5加密
     *
     * @param str
     * @return String
     */
    public static String toMD5(String str) {
        byte[] digesta = null;
        String md5 = "";
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(str.getBytes());
            digesta = alga.digest();
            String[] Digit = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
            for (int i = 0; i < digesta.length; i++) {
                md5 += Digit[(digesta[i] & 0XF0) / 16];
                md5 += Digit[digesta[i] & 0X0F];
            }
        } catch (Exception e) {
            md5 = "";
        }
        return md5;
    }

    /**
     * 获取UUID
     * @param null
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }


    /**
     * 设置sql点位符对应值。默认参数索引从1开始
     *
     * @param preparedStatement
     * @param values
     * @throws SQLException
     */
    public static void setPlaceHolderValue(PreparedStatement preparedStatement, Object... values) throws SQLException {
        setPlaceHolderValue(preparedStatement, 1, values);
    }

    /**
     * 设置sql点位符对应值。
     *
     * @param preparedStatement
     * @param startIndex        开始位置
     * @param values
     * @throws SQLException
     */
    public static void setPlaceHolderValue(PreparedStatement preparedStatement, int startIndex, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + startIndex, values[i]);
        }
    }

    /**
     * 由归集相关类中提取。<br>
     * 据id和指定凭证号类型（交易类型，--凭证号的第4位字符）<br>
     * 格式：<Strong>yMM + voucherNoType + id（不足6位前面补0）</Strong>
     *
     * @param id
     * @param tradeType 凭证号类型（交易类型，--凭证号的第4位字符）
     * @return
     */
    public static String getVoucherNo(int id, TradeTypeEnum tradeType) {
        id = id % 99999;
        String ymm = DateUtil.getFormattingDateTime(new Date(), 0).substring(1, 4);
        String prefix = ymm + tradeType.value; //截取年月日(第2位起)3位，加上第4位(为交易类型)
        String endNo = id + "";
        if (endNo.getBytes().length > 8) {
            endNo = endNo.substring(0, 8);
        }
        else {
            endNo = Strings.padStart(id + "", 8, '0');
        }

        String voucherNo = prefix + endNo;//TODO??超出也要截取补上0
        return voucherNo;
    }

    /**
     * 获取金额，保留到十位
     *
     * @param amt
     * @return
     * @throws Exception
     */
    public static double getAmt(String amt) {
        double money = 0;
        try {
            int index = amt.indexOf(".");
            amt = amt.substring(0, index);
            if (amt.length() == 3) {
                amt = amt.substring(0, 1);
            }
            else if (amt.length() == 4) {
                amt = amt.substring(0, 2);
            }
            else if (amt.length() == 5) {
                amt = amt.substring(0, 3);
            }
            else if (amt.length() == 6) {
                amt = amt.substring(0, 4);
            }
            else if (amt.length() == 7) {
                amt = amt.substring(0, 5);
            }
            else if (amt.length() == 8) {
                amt = amt.substring(0, 6);
            }
            else if (amt.length() == 9) {
                amt = amt.substring(0, 7);
            }
            else if (amt.length() == 10) {
                amt = amt.substring(0, 8);
            }
            else if (amt.length() == 11) {
                amt = amt.substring(0, 9);
            }
            else {
                amt = amt.substring(0, 10);
            }
            amt += "00";
            money = Double.parseDouble(amt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return money;
    }

}
