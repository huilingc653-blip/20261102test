package com.bytter.cloud.gateway.filter.xss;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
/**
 * xss过滤工具
 *
 * @author wade 2024-5-14
 * @since 1.0.0
 */
@Slf4j
public final class XssCleanRuleUtils {
    private static final Pattern[] SCRIPT_PATTERNS = {
    		 Pattern.compile("&lt;[\r\n| | ]*script[\r\n| | ]*&gt;(.*?)<!--[\r\n| | ]*script[\r\n| | ]*-->", Pattern.CASE_INSENSITIVE),
    		 Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		 Pattern.compile("<!--[\r\n| | ]*script[\r\n| | ]*-->", Pattern.CASE_INSENSITIVE),
    		 Pattern.compile("<[\r\n| | ]*script(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		 Pattern.compile("eval\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		 Pattern.compile("e-xpression\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		 Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE),
    		 Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE),
    		 Pattern.compile("onload(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    		 ///
//             Pattern.compile(">", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//             Pattern.compile("<", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
//             Pattern.compile("\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//             Pattern.compile("\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
//             Pattern.compile("on(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };
      //|grant|use|group_concat
      private static String badStrReg = "select|update|and|delete|insert|sleep|load_file|truncate|count|master|into|drop|execute|table|char|declare|sitename|xp_cmdshell|from|grant|group_concat|column_name|information_schema.columns|table_schema|union|where|by";
//    private static String badStrReg = "select|update|and|delete|insert|sleep|load_file|truncate|count|master|into|drop|execute|table|char|declare|sitename|xp_cmdshell|like|from|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|where|by";
    //获取国际化有问题，需要解决
    private static Pattern sqlPattern = Pattern.compile(badStrReg, Pattern.CASE_INSENSITIVE);//整体都忽略大小写
    
    /**
     * 清空xss
     *
     * @param value 字符串
     * @return 清空xss后的字符串
     */
    public static String xssClean(String value) {
        if (value != null) {
//            value = value.replaceAll("\0|\n|\r", "");
            for (Pattern pattern : SCRIPT_PATTERNS) {
            	if(pattern.matcher(value).find()) {
            		System.out.println("((((((((((((((((="+value);
            		value = pattern.matcher(value).replaceAll("");
            		System.out.println("value((((((((((((((((="+value);
            	}
                
            }
//            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        return value;
    }
    
    /**
     * GET请求参数过滤
     * @param value
     * @return
     */
    public static String xssGetClean(String value) throws UnsupportedEncodingException {

      //过滤xss字符集
      if (value != null) {
        value = value.replaceAll("\0|\n|\r", "");
        for (Pattern pattern : SCRIPT_PATTERNS) {
          value = pattern.matcher(value).replaceAll("");
        }
//        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
      }

      //sql关键字检查
      System.out.println("sql关键字检查1111==================="+value);
      return value;
//      return cleanGetSqlKeyWords(value);

    }


    public static String xssPostClean(String value) {

      //过滤xss字符集
      if (value != null) {
        value = value.replaceAll("\0|\n|\r", "");
        for (Pattern pattern : SCRIPT_PATTERNS) {
          value = pattern.matcher(value).replaceAll("");
        }
//        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
      }
      //sql关键字检查
      return value;
//      return cleanPostSqlKeyWords(value);

    }

    /**
     * 解析参数SQL关键字
     * @param value
     * @return
     */
    private static String cleanGetSqlKeyWords(String value) throws UnsupportedEncodingException {

      //参数需要url编码
      //这里需要将参数转换为小写来处理
      //不改变原值
      //value示例 order=asc&pageNum=1&pageSize=100&parentId=0
      String lowerValue = URLDecoder.decode(value, "UTF-8").toLowerCase();

      //获取到请求中所有参数值-取每个key=value组合第一个等号后面的值
      boolean isContains = Stream.of(lowerValue.split("\\&"))
          .map(kp -> kp.substring(kp.indexOf("=") + 1))
          .parallel()
          .anyMatch(param -> {
            if (sqlPattern.matcher(param).find())
            {
        	  if(lowerValue.indexOf("datatable")==-1 || lowerValue.indexOf("createdate")==-1 || lowerValue.indexOf("updatedate")==-1 
        			  || lowerValue.indexOf("datatables_apptable")==-1 || lowerValue.indexOf("country")==-1 || lowerValue.indexOf("tableKey")==-1) {
	              log.error("参数中包含不允许sql的关键词="+param);
	              System.out.println("============="+param);
	              return true;
        	  }
            }
            return false;
          });

      return isContains ? "forbid" : value;
    }


    /**
     * 解析参数SQL关键字
     * @param value
     * @return
     */
    private static String cleanPostSqlKeyWords(String value){

      JSONObject json = JSONObject.parseObject(value);
      Map<String, Object> map = json;
      Map<String, Object> mapjson = new HashMap<>();

      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String value1 =  entry.getValue().toString();

        //这里需要将参数转换为小写来处理-不改变原值
        String lowerValue = value1.toLowerCase();

        if (sqlPattern.matcher(lowerValue).find())
        {
          if(lowerValue.indexOf("datatable")>-1 || lowerValue.indexOf("createdate")>-1 || lowerValue.indexOf("updatedate")>-1) {
        	  continue;
          }
          log.error("参数中包含不允许sql的关键词"+lowerValue);
          value1 = "forbid";
          mapjson.put(entry.getKey(),value1);
          break;
        } else {
          mapjson.put(entry.getKey(),entry.getValue());
        }

      }

      return JSONObject.toJSONString(mapjson);
    }
}
