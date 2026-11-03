package com.bytter.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 判断IP是否处于一个网段
 *
 * @author zhoul
 * @since 2017/9/27
 */
@Slf4j
public class IpUtil {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";

    /**
     * 获取客户端IP地址。
     *
     * @param request HttpServletRequest对象。
     * @return 客户端IP地址。
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = "";
        // 检查各种可能的代理头
        for (String header : IP_HEADER_CANDIDATES) {
            ipAddress = request.getHeader(header);
            if (ObjectUtil.isNotEmpty(ipAddress) && !UNKNOWN.equalsIgnoreCase(ipAddress)) {
                // X-Forwarded-For可能有多个IP，第一个是客户端真实IP
                ipAddress = ipAddress.split(",")[0].trim();
                if (!ipAddress.isEmpty()) {
                    return ipAddress;
                }
            }
        }
        // 如果没有转发的ip，则取当前通信的请求端的ip
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCALHOST.equals(ipAddress) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)){
                ipAddress= getServerIpAddress();
            }
            if (ObjectUtil.isNotEmpty(ipAddress)){
                return ipAddress;
            }
        }
        return ipAddress;
    }

    /**
     * 获取服务器IP地址。
     *
     * @return 服务器IP地址。
     */
    public static String getServerIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return LOCALHOST;
        }
    }

    /**
     * 获取本机 IP（优先取非回环地址）
     */
    public static String getLocalIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            if (!ip.startsWith("127.") && !ip.equals("0:0:0:0:0:0:0:1")) {
                return ip;
            }
        } catch (UnknownHostException e) {
            log.warn("获取本机IP失败", e);
        }
        // 备用方案：使用 Hutool 工具
        return Convert.toStr(NetUtil.getLocalhostStr(), LOCALHOST);
    }

    /**
     * 获取服务器主机名。
     *
     * @return 服务器主机名。
     */
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    /**
     * 验证IP是否属于某个IP段
     *
     * @param ip        所验证的IP号码
     * @param ipSection IP段（以'-'分隔）
     * @return Boolean
     */
    public static boolean ipExistsInRange(String ip, String ipSection) {
        ipSection = ipSection.trim();
        ip = ip.trim();
        int idx = ipSection.indexOf('-');
        String beginIP = ipSection.substring(0, idx);
        String endIP = ipSection.substring(idx + 1);
        return getIp2long(beginIP) <= getIp2long(ip) && getIp2long(ip) <= getIp2long(endIP);
    }

    /**
     * 将IP转为Long类型
     *
     * @param ip IP号码
     * @return 转换后的IP
     */
    public static long getIp2long(String ip) {
        ip = ip.trim();
        String[] ips = ip.split("\\.");
        long ip2long = 0L;
        for (int i = 0; i < 4; ++i) {
            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
        }
        return ip2long;
    }

    /**
     * 将IP转为Long类型
     *
     * @param ip IP号码
     * @return 转换后的IP
     */
    public static long getIp2long2(String ip) {
        ip = ip.trim();
        String[] ips = ip.split("\\.");
        long ip1 = Integer.parseInt(ips[0]);
        long ip2 = Integer.parseInt(ips[1]);
        long ip3 = Integer.parseInt(ips[2]);
        long ip4 = Integer.parseInt(ips[3]);
        return ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;
    }
}
