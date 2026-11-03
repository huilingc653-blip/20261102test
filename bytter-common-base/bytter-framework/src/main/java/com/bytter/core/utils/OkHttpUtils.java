package com.bytter.core.utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Slf4j
public class OkHttpUtils {
    private static final long CONNECT_TIMEOUT;
    private static final long READ_TIMEOUT;
    private static final long WRITE_TIMEOUT;
    private static final MediaType MEDIA_TYPE_JSON;
    private static final String APPLICATION_JSON;
    private static OkHttpClient httpClient = null;
    static {
        CONNECT_TIMEOUT = 10L;
        READ_TIMEOUT = 15L;
        WRITE_TIMEOUT = 15L;
        APPLICATION_JSON = "application/json";
        MEDIA_TYPE_JSON = MediaType.parse(APPLICATION_JSON);
        httpClient = new OkHttpClient().newBuilder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
    }
    private OkHttpUtils() {
    }
    /**
     * get 请求没有请求头
     * 
     * @param url
     *            请求路径
     * @param param
     *            参数
     * @return 响应结果
     */
    public static String get(String url, Map<String, Object> param) {
        return response(buildGetRequest(url, param).build());
    }
    public static String get(String url, Map<String, Object> param, Map<String, Object> header) {
        Request.Builder builder = buildGetRequest(url, param);
        setHeader(builder, header);
        return response(builder.build());
    }
    private static Request.Builder buildGetRequest(String url, Map<String, Object> param) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), (String) entry.getValue());
            }
        }
        return new Request.Builder().url(urlBuilder.build()).get();
    }
    private static Request.Builder buildPostRequest(String url, Map<String, Object> body) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        RequestBody requestBody = CollectionUtils.isEmpty(body) ? RequestBody.create(MEDIA_TYPE_JSON, "")
                : RequestBody.create(MEDIA_TYPE_JSON, JsonUtils.toJSONString(body));
        return new Request.Builder().url(urlBuilder.build()).post(requestBody);
    }
    /**
     * get 请求没有请求头
     * 
     * @param url
     *            请求路径
     * @param body
     *            参数
     * @return 响应结果
     */
    public static String postJson(String url, Map<String, Object> body) {
        return response(buildPostRequest(url, body).build());
    }

    public static String postJson(String url, String json){

        RequestBody requestBodybody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBodybody)
                .build();

        String result = null;
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                    log.info("请求成功返回结果：" + result);
                }
            } else {
                log.info("请求失败: " + response.message());
            }
        } catch (IOException e) {
            log.error("请求失败，：", e);
        }
        return result;
    }
    /**
     * get 请求没有请求头
     * 
     * @param url
     *            请求路径
     * @param body
     *            参数
     * @return 响应结果
     */
    public static String postJson(String url, Map<String, Object> body, Map<String, Object> header) {
        Request.Builder request = buildPostRequest(url, body);
        setHeader(request, header);
        return response(request.build());
    }
    private static void setHeader(Request.Builder request, Map<String, Object> header) {
        request.addHeader("Content-Type", APPLICATION_JSON);
        if (!CollectionUtils.isEmpty(header)) {
            for (Map.Entry<String, Object> entry : header.entrySet()) {
                request.addHeader(entry.getKey(), (String) entry.getValue());
            }
        }
    }
    private static String response(Request request) {
        String result = null;
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                    log.info("请求成功返回结果:::" + result);
                }
            } else {
                log.info("请求失败: " + response.message());
            }
        } catch (IOException e) {
            log.error("请求失败，：", e);
        }
        return result;
    }
}
