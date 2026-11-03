package com.bytter.biz.fundwarn.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.TlsVersion;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpClientConfig {

	private String url;
    private String username;
    private String password;
    private String oauthToken;
    private int connectionTimeout = 60000;
    private int requestTimeout = 60000;
    private int webSocketPingInterval;
    private int maxConcurrentRequestsPerHost = 30;
    private int maxConnection = 40;

    private String httpProxy;
    private String httpsProxy;
    private String proxyUsername;
    private String proxyPassword;
    private String userAgent;
    private TlsVersion tlsVersions = TlsVersion.TLS_1_2;
    private String[] noProxy;

    public static final String HTTP_PROTOCOL_PREFIX = "http://";
    public static final String HTTPS_PROTOCOL_PREFIX = "https://";
    
}
