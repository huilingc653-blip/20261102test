package com.bytter.cloud.gateway.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ObjectUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * SQL注入处理工具类
 *
 * @author : wade
 */
@Slf4j
public class SqlInjectionUtil {

    private static final String SQL_REGEX = "\\b(and|or)\\b.{1,6}?(=|>|<|\\bin\\b|\\blike\\b)|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)";

    private static final Pattern SQL_PATTERN = Pattern.compile(SQL_REGEX, Pattern.CASE_INSENSITIVE);
    
    private static final String IS_SELECT = "isselect";
    
    private static final String IS_DELETE = "isdelete";
    
    private static final String IS_UPDATE = "isupdate";
    
    private static final String IS_INSERT= "isinsert";

    private static boolean matching(String lowerValue, String param) {
        if (SQL_PATTERN.matcher(param).find()) {
            log.error("the parameter contains keywords {} that do not allow sql!", lowerValue);
            return true;
        }
        return false;
    }

    private static String toLowerCase(Object obj) {
        return Optional.ofNullable(obj)
                .map(Object::toString)
                .map(String::toLowerCase)
                .orElse("");
    }

    private static boolean checking(Object value) {
        String lowerValue = toLowerCase(value);
        return matching(lowerValue, lowerValue);
    }

    /**
     * get请求sql注入校验
     *
     * @param value 具体的检验
     * @return 是否存在不合规内容
     */
    public static boolean checkForGet(String value) {
        String lowerValue = URLDecoder.decode(value, StandardCharsets.UTF_8).toLowerCase();
        return Stream.of(lowerValue.split("\\&")).map(kp -> kp.substring(kp.indexOf(StringPool.EQUALS) + 1))
                .parallel()
                .anyMatch(param -> matching(lowerValue, param));
    }
    
    public static void main(String[] args) {
//       	String str = "%7B%22draw%22%3A1%2C%22columns%22%3A%5B%7B%22data%22%3A%22id%22%2C%22name%22%3A%22id%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Afalse%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22billNo%22%2C%22name%22%3A%22billNo%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22schedulingElementsName%22%2C%22name%22%3A%22schedulingElementsName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22schedulingDate%22%2C%22name%22%3A%22schedulingDate%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22corpName%22%2C%22name%22%3A%22corpName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22voucherName%22%2C%22name%22%3A%22voucherName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payerAcc%22%2C%22name%22%3A%22payerAcc%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payerAccName%22%2C%22name%22%3A%22payerAccName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payerBank%22%2C%22name%22%3A%22payerBank%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22curName%22%2C%22name%22%3A%22curName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payMoney%22%2C%22name%22%3A%22payMoney%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22billTypeName%22%2C%22name%22%3A%22billTypeName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22billFlagName%22%2C%22name%22%3A%22billFlagName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payeeAcc%22%2C%22name%22%3A%22payeeAcc%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payeeAccName%22%2C%22name%22%3A%22payeeAccName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payeeBankCode%22%2C%22name%22%3A%22payeeBankCode%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22payeeBank%22%2C%22name%22%3A%22payeeBank%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22blRcvpymtpsUnnIdNo%22%2C%22name%22%3A%22blRcvpymtpsUnnIdNo%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22wishPayday%22%2C%22name%22%3A%22wishPayday%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22splitBillNo%22%2C%22name%22%3A%22splitBillNo%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22busTypeName%22%2C%22name%22%3A%22busTypeName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22bursarBillNo%22%2C%22name%22%3A%22bursarBillNo%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22purpose%22%2C%22name%22%3A%22purpose%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22addWord%22%2C%22name%22%3A%22addWord%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22isPayedName%22%2C%22name%22%3A%22isPayedName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22billFromName%22%2C%22name%22%3A%22billFromName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22status%22%2C%22name%22%3A%22status%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22isForindividualName%22%2C%22name%22%3A%22isForindividualName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22isSelectedAcceBillName%22%2C%22name%22%3A%22isSelectedAcceBillName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22createDate%22%2C%22name%22%3A%22createDate%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22opCode%22%2C%22name%22%3A%22opCode%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3Anull%2C%22name%22%3A%22%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Afalse%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%5D%2C%22order%22%3A%5B%7B%22column%22%3A1%2C%22dir%22%3A%22asc%22%7D%2C%7B%22column%22%3A13%2C%22dir%22%3A%22desc%22%7D%5D%2C%22start%22%3A0%2C%22length%22%3A10%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%2C%22formdata%22%3A%7B%22opType%22%3A%22list%22%2C%22showIsPayed%22%3A%221%22%2C%22editPayerAcc%22%3A%221%22%2C%22isOpenLimit%22%3A%220%22%2C%22accountOver%22%3A%22%22%2C%22bankaccOver%22%3A%22%22%2C%22acceBillCorpId%22%3A%22%22%2C%22acceBillMoney%22%3A%22%22%2C%22ins_corpId%22%3A%22%22%2C%22geg_payMoney%22%3A%22%22%2C%22leg_payMoney%22%3A%22%22%2C%22eqs_payerAcc%22%3A%22%22%2C%22likes_payeeAcc%22%3A%22%22%2C%22likes_payeeAccName%22%3A%22%22%2C%22likes_billNo%22%3A%22%22%2C%22eqs_billFrom%22%3A%22%22%2C%22ged_wishPayday%22%3A%22%22%2C%22led_wishPayday%22%3A%222024-08-10%22%2C%22eqs_busType%22%3A%22%22%2C%22eqi_status%22%3A%22%22%2C%22eqs_curId%22%3A%22%22%2C%22likes_bursarBillNo%22%3A%22%22%2C%22eqs_isForindividual%22%3A%22%22%2C%22likes_purpose%22%3A%22%22%2C%22eqi_billType%22%3A%22%22%2C%22eqi_billFlag%22%3A%22%22%2C%22eqs_isPayed%22%3A%22%22%2C%22likes_opCode%22%3A%22%22%7D%7D";
    	String str = "isSelectedAcceBillName%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22createDate%22%2C%22name%22%3A%22createDate%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3A%22opCode%22%2C%22name%22%3A%22opCode%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Atrue%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%2C%7B%22data%22%3Anull%2C%22name%22%3A%22%22%2C%22searchable%22%3Atrue%2C%22orderable%22%3Afalse%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%7D%5D%2C%22order%22%3A%5B%7B%22column%22%3A1%2C%22dir%22%3A%22asc%22%7D%2C%7B%22column%22%3A13%2C%22dir%22%3A%22desc%22%7D%5D%2C%22start%22%3A0%2C%22length%22%3A10%2C%22search%22%3A%7B%22value%22%3A%22%22%2C%22regex%22%3Afalse%7D%2C%22formdata%22%3A%7B%22opType%22%3A%22list%22%2C%22showIsPayed%22%3A%221%22%2C%22editPayerAcc%22%3A%221%22%2C%22isOpenLimit%22%3A%220%22%2C%22accountOver%22%3A%22%22%2C%22bankaccOver%22%3A%22%22%2C%22acceBillCorpId%22%3A%22%22%2C%22acceBillMoney%22%3A%22%22%2C%22ins_corpId%22%3A%22%22%2C%22geg_payMoney%22%3A%22%22%2C%22leg_payMoney%22%3A%22%22%2C%22eqs_payerAcc%22%3A%22%22%2C%22likes_payeeAcc%22%3A%22%22%2C%22likes_payeeAccName%22%3A%22%22%2C%22likes_billNo%22%3A%22%22%2C%22eqs_billFrom%22%3A%22%22%2C%22ged_wishPayday%22%3A%22%22%2C%22led_wishPayday%22%3A%222024-08-10%22%2C%22eqs_busType%22%3A%22%22%2C%22eqi_status%22%3A%22%22%2C%22eqs_curId%22%3A%22%22%2C%22likes_bursarBillNo%22%3A%22%22%2C%22eqs_isForindividual%22%3A%22%22%2C%22likes_purpose%22%3A%22%22%2C%22eqi_billType%22%3A%22%22%2C%22eqi_billFlag%22%3A%22%22%2C%22eqs_isPayed%22%3A%22%22%2C%22likes_opCode%22%3A%22%22%7D%7D";
//        String str2 = "4%B3%E8%AF%B7%E4%BA%BA%E7%BC%96%E7%A0%81%22%2C%22align%22%3A%22center%22%2C%22idx%22%3A26%2C%22visible%22%3Atrue%2C%22name%22%3A%22opCode%22%2C%22width%22%3A%2262.428571000000005px%22%7D%5D";
		System.out.println("str="+checkForGet(str));
		System.out.println("str="+checkForPost(str));
//		System.out.println("str2="+checkForGet(str2));
//		System.out.println("str2="+checkForPost(str2));
    }
    /**
     * post请求sql注入校验
     *
     * @param value 具体的检验
     * @return 是否存在不合规内容
     */
    public static boolean checkForPost(String value) {
        List<JsonElement> result = new ArrayList<>();
        JsonElement jsonElement = GsonUtil.toJsonElement(value);
        iterator(jsonElement, result);
        return CollectionUtil.isNotEmpty(result);
    }

    private static void iterator(JsonElement jsonElement, List<JsonElement> result) {
        if (jsonElement.isJsonNull()) {
            return;
        }

        if (jsonElement.isJsonPrimitive()) {
            boolean hasInjection = checking(jsonElement.toString());
            if (hasInjection) {
                result.add(jsonElement);
            }
            return;
        }

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (ObjectUtil.isNotEmpty(jsonArray)) {
                for (JsonElement je : jsonArray) {
                    iterator(je, result);
                }
            }
            return;
        }

        if (jsonElement.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> es = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> en : es) {
                iterator(en.getValue(), result);
            }
        }
    }
    
    public static boolean checkExpectValue(String toLowerStr) {
    	 if(toLowerStr.toLowerCase().indexOf(IS_SELECT)>-1 || toLowerStr.toLowerCase().indexOf(IS_UPDATE)>-1 
    		|| toLowerStr.toLowerCase().indexOf(IS_DELETE)>-1 || toLowerStr.toLowerCase().indexOf(IS_INSERT)>-1) {
    		 return true;
    	 }
    	 return false;
    }

}