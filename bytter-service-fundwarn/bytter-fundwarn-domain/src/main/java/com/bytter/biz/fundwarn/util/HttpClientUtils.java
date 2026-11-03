package com.bytter.biz.fundwarn.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static okhttp3.ConnectionSpec.CLEARTEXT;


public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * json传输方式
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static volatile OkHttpClient client;

    public static Headers doLoginRequest(HttpClientConfig config, Map<String, String> params) {
        try {
            OkHttpClient client = getInstance(config);
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.add(param.getKey(), param.getValue());
            }
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(config.getUrl()).post(formBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.headers();
            } else if (response.body() != null){
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doFormRequest(HttpClientConfig config, Map<String, String> params, String cookie) {
        try {
            OkHttpClient client = getInstance(config);
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.add(param.getKey(), param.getValue());
            }
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(config.getUrl()).header("Cookie", cookie).post(formBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else if (response.body() != null){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doRequest(HttpClientConfig config, HttpMethod method, Map<String, String> headers, String body) {
        try {
            OkHttpClient client = getInstance(config);
            //创建请求
            RequestBody requestBody = RequestBody.create(JSON, StringUtils.isEmpty(body) ? "" : body);

            Request.Builder builder = new Request.Builder();
            if (!CollectionUtils.isEmpty(headers)) {
                logger.info("headers : " + headers);
                builder.headers(Headers.of(headers));
            }

            Request request = builder.method(method.name(), requestBody).url(config.getUrl()).build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else if (response.body() != null){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 双重检查单例
     * @param config OkHttpClient配置
     * @return okHttpClient
     */
    public static OkHttpClient getInstance(HttpClientConfig config) {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = createHttpClient(config);
                }
            }
        }
        //拿到client之后把认证信息重新加一遍
        client.newBuilder().addInterceptor(chain -> {
            Request request = chain.request();
            if (StringUtils.hasText(config.getUsername()) && StringUtils.hasText(config.getPassword())) {
                Request authReq = chain.request().newBuilder().addHeader("Authorization", Credentials.basic(config.getUsername(), config.getPassword())).build();
                return chain.proceed(authReq);
            } else if (StringUtils.hasText( config.getOauthToken())) {
                Request authReq = chain.request().newBuilder().addHeader("Authorization", "Bearer " + config.getOauthToken()).build();
                return chain.proceed(authReq);
            }
            return chain.proceed(request);
        });
        return client;
    }

    private static OkHttpClient createHttpClient(final HttpClientConfig config) {
        try {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.followRedirects(true);
            httpClientBuilder.followSslRedirects(true);
            if (config.getConnectionTimeout() > 0) {
                httpClientBuilder.connectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
            }
            if (config.getRequestTimeout() > 0) {
                httpClientBuilder.readTimeout(config.getRequestTimeout(), TimeUnit.MILLISECONDS);
            }
            if (config.getWebSocketPingInterval() > 0) {
                httpClientBuilder.pingInterval(config.getWebSocketPingInterval(), TimeUnit.MILLISECONDS);
            }
            if (config.getMaxConcurrentRequestsPerHost() > 0) {
                Dispatcher dispatcher = new Dispatcher();
                dispatcher.setMaxRequestsPerHost(config.getMaxConcurrentRequestsPerHost());
                httpClientBuilder.dispatcher(dispatcher);
            }
            if (config.getMaxConnection() > 0) {
                ConnectionPool connectionPool = new ConnectionPool(config.getMaxConnection(), 60, TimeUnit.SECONDS);
                httpClientBuilder.connectionPool(connectionPool);
            }
            // Only check proxy if it's a full URL with protocol
            if (config.getUrl().toLowerCase().startsWith(HttpClientConfig.HTTP_PROTOCOL_PREFIX) || config.getUrl().startsWith(HttpClientConfig.HTTPS_PROTOCOL_PREFIX)) {
                try {
                    URL proxyUrl = getProxyUrl(config);
                    if (proxyUrl != null) {
                        httpClientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(), proxyUrl.getPort())));
                        if (config.getProxyUsername() != null) {
                            httpClientBuilder.proxyAuthenticator((route, response) -> {
                                String credential = Credentials.basic(config.getProxyUsername(), config.getProxyPassword());
                                return response.request().newBuilder().header("Proxy-Authorization", credential).build();
                            });
                        }
                    }
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException("Invalid proxy server configuration", e);
                }
            }
            if (config.getUserAgent() != null && !config.getUserAgent().isEmpty()) {
                httpClientBuilder.addNetworkInterceptor(chain -> {
                    Request agent = chain.request().newBuilder().header("User-Agent", config.getUserAgent()).build();
                    return chain.proceed(agent);
                });
            }
            if (config.getTlsVersions() != null && config.getTlsVersions() !=null) {
                ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(config.getTlsVersions())
                        .build();
                httpClientBuilder.connectionSpecs(Arrays.asList(spec, CLEARTEXT));
            }
            return httpClientBuilder.build();
        } catch (Exception e) {
            throw new IllegalArgumentException("创建OKHTTPClient错误", e);
        }
    }

    private static URL getProxyUrl(HttpClientConfig config) throws MalformedURLException {
        URL master = new URL(config.getUrl());
        String host = master.getHost();
        if (config.getNoProxy() != null) {
            for (String noProxy : config.getNoProxy()) {
                if (host.endsWith(noProxy)) {
                    return null;
                }
            }
        }
        String proxy = config.getHttpsProxy();
        String http = "http";
        if (http.equals(master.getProtocol())) {
            proxy = config.getHttpProxy();
        }
        if (proxy != null) {
            return new URL(proxy);
        }
        return null;
    }

}
