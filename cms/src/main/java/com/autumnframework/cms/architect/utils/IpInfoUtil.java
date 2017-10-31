package com.autumnframework.cms.architect.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by Mr SJL on 2016/12/8.
 *
 * @Author Junlan Shuai
 */
public class IpInfoUtil
{
    private final static String IP_INFO_URL = "http://ip.taobao.com/service/getIpInfo.php"; //获取ip信息URL，返回JSON格式的数据

    /**
     * 通过ip，查取ip的基本信息
     *
     * @param ip 参数ip
     * @return 返回ip的基本信息，以JSONObject格式返回
     */
    public static JSONObject getIpInforByIp(String ip)
    {
        String tmpURL = IP_INFO_URL + "?ip=" + ip;
        String str = HttpMethodUtil.doGet(tmpURL);
        return JSONObject.parseObject(str);
    }

    /**
     * 根据请求对象，获取请求发送请求的机器的IP
     *
     * @param req HttpServletRequest对象
     * @return 返回获取的ip， String类型
     */
    public static String getIpByReq(HttpServletRequest req)
    {
        String ip = req.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = req.getRemoteAddr();
        }
        return ip;

    }

    /**
     * 根据请求的HttpServletRequest对象，获取ip的基本信息
     *
     * @param req
     * @return
     */
    public static JSONObject getIpInforByReq(HttpServletRequest req)
    {
        return getIpInforByIp(getIpByReq(req));
//        return getIpInforByIp("115.28.61.173");
    }

    /**
     * 根据请求对象，获取请求头的所有信息
     *
     * @param req 请求的对象
     * @return 返回Map格式的数据
     */
    public static Map<String, List<String>> getHeaderInfor(HttpServletRequest req)
    {
        return getConnectByIp(getIpByReq(req), IP_INFO_URL).getHeaderFields();
    }

    /**
     * 根据ip和url获得连接
     *
     * @param ip  指定的ip
     * @param url 待连接url
     * @return 返回HttpURLConnection对象
     */
    private static HttpURLConnection getConnectByIp(String ip, String url)
    {
        String tmpURL = IP_INFO_URL + "?ip=" + ip;
        HttpURLConnection con = null;
        try
        {
            URL url1 = new URL(tmpURL);
            con = (HttpURLConnection) url1.openConnection();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return con;
    }

    public static void main(String[] args) {
        System.out.println(getIpInforByIp("115.28.61.173"));
    }
}