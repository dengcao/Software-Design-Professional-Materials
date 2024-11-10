package com.mz.nlpservice.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Created by gaozhenan on 2016/9/5.
 */
public class HttpUtil {
    private static Gson gson = new GsonBuilder().create();
    private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

        public boolean retryRequest(
                IOException exception,
                int executionCount,
                HttpContext context) {
            if (executionCount >= 3) {
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                // Connection refused
                return false;
            }
            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        }

    };

    private static CloseableHttpClient httpclient = HttpClients.custom()
            .setRetryHandler(retryHandler)
            .build();
    private static RequestConfig requestConfig = RequestConfig.custom()
                                                .setSocketTimeout(15000)
                                                .setConnectTimeout(2000)
                                                .build();

    public static String doPost(String url, String message) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(message, ContentType.create("text/plain", "UTF-8"));
        httpPost.setEntity(postEntity);
        CloseableHttpResponse response1 = httpclient.execute(httpPost);
        try {
            HttpEntity entity = response1.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
            return null;
        } finally {
            response1.close();
        }
    }

    public static <T> T doPost(String url, String message, Class<T> t) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(message, ContentType.create("text/plain", "UTF-8"));
        httpPost.setEntity(postEntity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        return changeRes2Cls(response, t);
    }

    private static <T> T changeRes2Cls(CloseableHttpResponse response, Class<T> t)
        throws IOException{
        try {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            if (entity == null) {
                throw new ClientProtocolException("Response contains no content");
            }
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            Reader reader = new InputStreamReader(entity.getContent(), charset);
            try {
                return gson.fromJson(reader, t);
            } finally {
                reader.close();
            }
        } finally {
            response.close();
        }
    }

    public static String doGet(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);

        CloseableHttpResponse response1 = httpclient.execute(httpget);
        try {
            HttpEntity entity = response1.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
            return null;
        } finally {
            response1.close();
        }
    }

    public static <T> T doGet(String url, Class<T> t) throws IOException {
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            if (entity == null) {
                throw new ClientProtocolException("Response contains no content");
            }
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            Reader reader = new InputStreamReader(entity.getContent(), charset);
            try {
                return gson.fromJson(reader, t);
            } finally {
                reader.close();
            }

        } finally {
            response.close();
        }
    }


    public static void main(String[] args) throws IOException {
//        System.out.println(HttpUtil.doGet("http://120.27.120.241:8100/music/news?channel=1"));
        System.out.println(HttpUtil.doPost("http://172.21.7.14:9400/recordservice/testdialog/updateresult", "this is a test message"));
    }
}
