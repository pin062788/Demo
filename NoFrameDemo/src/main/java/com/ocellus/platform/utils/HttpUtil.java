package com.ocellus.platform.utils;

import com.ocellus.platform.exception.PostException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

    private final static Logger logger = Logger.getLogger(HttpUtil.class);
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url             发送请求的 URL
     * @param param           请求参数，请求参数name=value&name=value 的形式。
     * @param requestProperty 请求参数，设置request属性。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, Map<String, String> requestProperty) {
        PrintWriter out = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setRequestMethod(POST_METHOD);
            conn.setRequestProperty("Charset", "UTF-8");

            setRequestProperty(conn, requestProperty);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            conn.connect();
            result = IOUtil.getInputStreamAsString(conn.getInputStream());
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }
        //使用finally块来关闭输出流、输入流
        finally {
            if (out != null)
                out.close();
        }
        return result;
    }

    public static String sendPost(String url, String param) {
        return sendPost(url, param, null);
    }

    private static void setRequestProperty(URLConnection connection, Map<String, String> property) {
        if (property != null) {
            for (Entry<String, String> e : property.entrySet()) {
                connection.setRequestProperty(e.getKey(), e.getValue());
            }
        }
    }

    public static String post(String url, String xmlStr) throws PostException {
        HttpClient httpClient = null;
        PostMethod postMethod = null;
        String rsp = "";
        try {
            httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
            postMethod = new PostMethod(url);
            RequestEntity requestEntity = new ByteArrayRequestEntity(xmlStr.getBytes("UTF-8"));
            postMethod.setRequestEntity(requestEntity);
            postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                throw new PostException("Send message to " + url + " failed:" + postMethod.getStatusLine());
            }
            rsp = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            throw new PostException("Send message to " + url + " failed", e);
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
        }

        return rsp;
    }
}