package com.bytter.core.utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.Map;
/**
 * 用于模拟HTTP请求中GET/POST方式
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final String APPLICATION_JSON = "application/json";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String getParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();// 存储参数
        // 编码请求参数
        if (parameters.size() == 1) {
            for (String name : parameters.keySet()) {
                sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), DEFAULT_CHARSET));
            }
            return sb.toString();
        } else {
            for (String name : parameters.keySet()) {
                sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), DEFAULT_CHARSET)).append("&");
            }
            String temp_params = sb.toString();
            return temp_params.substring(0, temp_params.length() - 1);
        }
    }
    private static HttpURLConnection getHttpConnection(String url) throws IOException {
        // 创建URL对象
        java.net.URL connURL = new java.net.URL(url);
        // 打开URL连接
        HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
        // 设置通用属性
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
        return httpConn;
    }
    private static String getResult(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_CHARSET));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("getResult异常：" + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
    /**
     * 发送GET请求
     *
     * @param url        目的地址
     * @param parameters 请求参数，Map类型。
     * @return 远程响应结果
     */
    public static String sendGet(String url, Map<String, String> parameters) {
        try {
            String full_url = url + "?" + getParameters(parameters);
            HttpURLConnection httpConn = getHttpConnection(full_url);
            // 建立实际的连接
            httpConn.connect();
            return getResult(httpConn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送GET请求异常：" + e.getMessage(), e);
        }
        return null;
    }
    /**
     * 发送GET请求
     *
     * @param url        目的地址
     * @return 远程响应结果
     */
    public static String sendGet(String url) {
        try {
            HttpURLConnection httpConn = getHttpConnection(url);
            // 建立实际的连接
            httpConn.connect();
            return getResult(httpConn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送GET请求异常：" + e.getMessage(), e);
        }
        return null;
    }
    /**
     * 发送POST请求
     *
     * @param url        目的地址
     * @param parameters 请求参数，Map类型。
     * @return 远程响应结果
     */
    public static String sendPost(String url, Map<String, String> parameters) {
        PrintWriter out = null;
        try {
            HttpURLConnection httpConn = getHttpConnection(url);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(getParameters(parameters));
            // flush输出流的缓冲
            out.flush();
            return getResult(httpConn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送POST请求 异常：" + e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }
    public static String postJson(String url, Object obj) {
        try {
            return postJson(url, objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            logger.error("{}对象转换为JSON失败", obj);
        }
        return null;
    }
    public static String postJson(String url, String json) {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String response = null;
        try {
            StringEntity stringEntity = new StringEntity(json, DEFAULT_CHARSET);
            stringEntity.setContentEncoding(DEFAULT_CHARSET);
            //发送json数据需要设置contentType
            stringEntity.setContentType(APPLICATION_JSON);
            post.setEntity(stringEntity);
            HttpResponse res = httpclient.execute(post);
            int statusCode = res.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 返回json格式
                response = EntityUtils.toString(res.getEntity());
            } else {
                logger.warn("{}地址状态码为：{}", url, statusCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
