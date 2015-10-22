package com.gt.bmf.util;

import java.io.File;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

@Component("httpClientUtils")
public class HttpClientUtils {

	/** 连接超时时间（默认3秒 3000ms） 单位毫秒（ms） */
	private int connectionTimeout = 10000;

	/** 字符集设置，默认UTF-8 */
	private String charset = "UTF-8";

	private Header[] httpsCookieHeaders;

	private static X509TrustManager tm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	/**
	 * 获取一个针对https的HttpClient
	 */
	private CloseableHttpClient getHttpsClient() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { tm }, null);
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	/**
	 * 创建post请求
	 * 
	 * @param url
	 * @return HttpPost
	 */
	private HttpPost getHttpPost(String url) {
		// 创建post请求
		HttpPost post = new HttpPost(url);
		if (httpsCookieHeaders != null && httpsCookieHeaders.length > 0) {
			post.setHeaders(httpsCookieHeaders);
		}
		return post;
	}

	/**
	 * 创建get请求
	 * 
	 * @param url
	 * @return HttpGet
	 */
	private HttpGet getHttpGet(String url) {
		HttpGet get = new HttpGet(url);
		if (httpsCookieHeaders != null && httpsCookieHeaders.length > 0) {
			get.setHeaders(httpsCookieHeaders);
		}
		return get;
	}

	/**
	 * 获取response里的cookies
	 * 
	 * @param response
	 */
	private void getRequestCookieHeader(HttpResponse response) {
		Header[] responseHeaders = response.getHeaders("Set-Cookie");
		if (responseHeaders == null || responseHeaders.length <= 0) {
			return;
		}
		httpsCookieHeaders = new BasicHeader[responseHeaders.length];
		for (int i = 0; i < responseHeaders.length; i++) {
			httpsCookieHeaders[i] = new BasicHeader("Cookie", responseHeaders[i].getValue());
		}

	}

	/**
	 * 获取地址指向的图片并保存在指定位置
	 * 
	 * @param url
	 * @param imageFilePath
	 * @return
	 * @throws java.security.KeyManagementException
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public synchronized File getImage(String url, String imageFilePath) throws Exception {
		File image = new File(imageFilePath);

		// 创建客户端
		CloseableHttpClient httpclient = getHttpsClient();
		CloseableHttpResponse response = null;
		InputStream instream = null;
		HttpGet get = getHttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(connectionTimeout)
                    .setConnectTimeout(connectionTimeout)
                    .setConnectionRequestTimeout(connectionTimeout)
                    .setExpectContinueEnabled(false).build();
			get.setConfig(requestConfig);
			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				String fileType = FilenameUtils.getExtension(imageFilePath);
				ImageIO.write(ImageIO.read(instream), fileType, image);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
			if(instream!=null){
				instream.close();
			}
			if(response!=null){
				response.close();
			}
			httpclient.close();
		}
		return image;
	}

	/**
	 * 以get方式请求，返回String型结果
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception {
		CloseableHttpClient httpclient = getHttpsClient();
		CloseableHttpResponse response = null;
		HttpGet get = getHttpGet(url);
		String responseBody = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(connectionTimeout)
                    .setConnectTimeout(connectionTimeout)
                    .setConnectionRequestTimeout(connectionTimeout)
                    .setExpectContinueEnabled(false).build();
			get.setConfig(requestConfig);
			response = httpclient.execute(get);
			getRequestCookieHeader(response);

			responseBody = IOUtils.toString(response.getEntity().getContent(), charset);

		} catch (java.net.SocketTimeoutException ste) {
			responseBody = ste.getMessage();
		} catch (Exception e) {
			responseBody = e.getMessage();
			e.printStackTrace();
		} finally {
			get.releaseConnection();
			if(response!=null){
				response.close();
			}
			httpclient.close();
		}
		return responseBody;
	}

	/**
	 * 以post方式请求，返回String型结果
	 * 
	 * @param url
	 * @param nvps
	 * @return
	 * @throws Exception
	 */
	public String doPost(String url, List<NameValuePair> nvps) throws Exception {
		CloseableHttpClient httpclient = getHttpsClient();
		CloseableHttpResponse response = null;
		HttpPost post = getHttpPost(url);
		String responseBody = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(connectionTimeout)
					.setConnectTimeout(connectionTimeout)
					.setConnectionRequestTimeout(connectionTimeout)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
            //post.addHeader("Cookie","Hm_lvt_07e1b3469e412552a15451441d5e3973=1439734622; Hm_lpvt_07e1b3469e412552a15451441d5e3973=1439734622; name=value; dse_sessionId=56A5B3FE465BE70906227DE501237C0E; userId=; gfportalsid=s%3A7ac8e070-4422-11e5-8dad-755dfc9f7fd6_29417_157741_180.FCUvXzBiIlrofNC2lyCexWoATITb0bzQa8wqMbOG17A; JSESSIONID=2F3FA824311F6303DC527099ADC12EBA");
			post.setEntity(new UrlEncodedFormEntity(nvps, charset));
			response = httpclient.execute(post);
			getRequestCookieHeader(response);
			responseBody = IOUtils.toString(response.getEntity().getContent(), charset);

		} catch (java.net.SocketTimeoutException ste) {
			responseBody = ste.getMessage();
		} catch (Exception e) {
			responseBody = e.getMessage();
			e.printStackTrace();
		} finally {
			post.releaseConnection();
			if(response!=null){
				response.close();
			}
			httpclient.close();
		}
		return responseBody;
	}

	public static void main(String[] args) throws Exception {
	/*	Map<String, String> params = new HashMap<String, String>();
		params.put("language", "zh_TW");
		params.put("account", "51080624345");
		params.put("pin", "123456");
		String result = u.doPost("https://210.3.95.105/api/account/authUser", nvps);
		// String result =
		// HttpClientUtils.post("https://210.3.95.105/api/account/authUser",
		// params);*/

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        NameValuePair vp1 = new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control");
        NameValuePair vp2 = new BasicNameValuePair("method", "nxbQueryPrice");
        NameValuePair vp3 = new BasicNameValuePair("fund_code", "878003");
        NameValuePair vp4 = new BasicNameValuePair("dse_sessionId", "56A5B3FE465BE70906227DE501237C0E");
        nvps.add(vp1);
        nvps.add(vp2);
        nvps.add(vp3);
        nvps.add(vp4);
        HttpClientUtils u = new HttpClientUtils();
        String result =   u.doPost("https://trade.gf.com.cn/entry",nvps);

		System.out.println(result);
	}
}
