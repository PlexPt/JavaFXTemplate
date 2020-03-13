package net.w2p.Shared.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import net.w2p.Shared.util.ajax.AjaxCallback;
import net.w2p.Shared.util.ajax.ErrorCallBack;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 基于 httpclient 的 http工具类
 *
 * @author mcSui
 *
 */
public class HttpTookit {

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    static Logger logger= LoggerFactory.getLogger("HttpTookit");
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * HTTP Get 获取内容
     * @param url请求的url地址 ?之前的地址
     * @param params请求的参数
     * @param charset编码格式
     * @return 页面内容
     */
    public static String sendGet(String url, Map<String, Object> params) {
        if(ValidateUtils.isEmpty(url)){
            return "";
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            HttpGet httpGet = new HttpGet(url);
            // 设置通用的请求属性

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void ajaxGet(String url,
                               Map<String, Object> params,
                                 AjaxCallback successCallBack,
                                 ErrorCallBack failedCallback) {
        if(ValidateUtils.isEmpty(url)){
            if(failedCallback!=null){
                failedCallback.run(0,"调用的url为空！");
            }
            return;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
            httpclient.start();

            final CountDownLatch latch = new CountDownLatch(1);
            final HttpGet request = new HttpGet(url);

            System.out.println(" caller thread id is : " + Thread.currentThread().getId());

            httpclient.execute(request, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    latch.countDown();
                    System.out.println(" callback thread id is : " + Thread.currentThread().getId());
                    System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                    try {
                        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                        System.out.println(" response content is : " + content);
                        if(successCallBack!=null){
                            successCallBack.run(200,content);
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    logger.info(request.getRequestLine() + "->" + ex);
                    logger.info(" callback thread id is : " + Thread.currentThread().getId());
                    if(failedCallback!=null){
                        failedCallback.run(500,ex.getMessage());
                    }

                }

                public void cancelled() {
                    latch.countDown();
                    logger.info(request.getRequestLine() + " cancelled");
                    logger.info(" callback thread id is : " + Thread.currentThread().getId());
                    if(failedCallback!=null){
                        failedCallback.run(500,"访问被取消");
                    }
                }

            });


            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

            try {
                httpclient.close();
            } catch (IOException ignore) {
                ignore.printStackTrace();

            }

        }
        catch (Exception ed){
            ed.printStackTrace();

            return ;
        }
    }

    /**
     * HTTP Post 获取内容
     * @param url请求的url地址 ?之前的地址
     * @param params请求的参数
     * @param charset编码格式
     * @return 页面内容
     */
    public static String sendPost(String url, Map<String, Object> params) {
        if(ValidateUtils.isEmpty(url)){
            return "";
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void ajaxPost(String url,
                               Map<String, Object> params,
                               AjaxCallback successCallBack,
                               ErrorCallBack failedCallback) {
        if(ValidateUtils.isEmpty(url)){
            if(failedCallback!=null){
                failedCallback.run(0,"调用的url为空！");
            }
            return;
        }
        try {

            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }

            CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
            httpclient.start();

            final CountDownLatch latch = new CountDownLatch(1);

            System.out.println(" caller thread id is : " + Thread.currentThread().getId());

            httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    latch.countDown();
                    System.out.println(" callback thread id is : " + Thread.currentThread().getId());
                    System.out.println(httpPost.getRequestLine() + "->" + response.getStatusLine());
                    try {
                        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                        System.out.println(" response content is : " + content);
                        if(successCallBack!=null){
                            successCallBack.run(200,content);
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    logger.info(httpPost.getRequestLine() + "->" + ex);
                    logger.info(" callback thread id is : " + Thread.currentThread().getId());
                    if(failedCallback!=null){
                        failedCallback.run(500,ex.getMessage());
                    }

                }

                public void cancelled() {
                    latch.countDown();
                    logger.info(httpPost.getRequestLine() + " cancelled");
                    logger.info(" callback thread id is : " + Thread.currentThread().getId());
                    if(failedCallback!=null){
                        failedCallback.run(500,"访问被取消");
                    }
                }

            });


            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

            try {
                httpclient.close();
            } catch (IOException ignore) {
                ignore.printStackTrace();

            }

        }
        catch (Exception ed){
            ed.printStackTrace();

            return ;
        }
    }

    public static InputStream downFile(String src) throws IOException {
        return downFile(URI.create(src));
    }

    /**
     * 从网络上下载文件
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public static InputStream downFile(URI uri) throws IOException {
        HttpResponse httpResponse;
        try {
            Request request = Request.Get(uri);
            HttpHost httpHost = URIUtils.extractHost(uri);
            if (StringUtils.isNotEmpty(httpHost.getHostName())) {
                request.setHeader("Host", httpHost.getHostName());
            }
            request.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

            httpResponse = request.execute().returnResponse();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }

        int code = httpResponse.getStatusLine().getStatusCode();
        if (code != 200) {
            throw new FileNotFoundException();
        }

        return httpResponse.getEntity().getContent();
    }


}