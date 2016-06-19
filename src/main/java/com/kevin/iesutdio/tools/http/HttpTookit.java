package com.kevin.iesutdio.tools.http;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpTookit implements Closeable {

	private CloseableHttpClient httpClient;
	public static final String CHARSET = "UTF-8";

	private void init(int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout)
				.build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	public HttpTookit() {
		init(15000, 15000, 15000);
	}

	public HttpTookit(int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
		init(connectionRequestTimeout, connectTimeout, socketTimeout);
	}

	public byte[] doGet(String url, String charset) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		byte[] bs = null;
		try {
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				bs = EntityUtils.toByteArray(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException("HttpClient,Close Response ERROR ");
				}
			}
		}
		return bs;
	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url
	 *            请求的url地址 ?之前的地址
	 * @param params
	 *            请求的参数
	 * @param contentType
	 *            编码格式
	 * @return 页面内容
	 */
	public byte[] doPost(String url, Map<String, String> params, ContentType contentType, byte[] post) {
		ArrayList pairs = null;
		if(params != null && !params.isEmpty()) {
			pairs = new ArrayList(params.size());
			Iterator httpPost = params.entrySet().iterator();

			while(httpPost.hasNext()) {
				Map.Entry bae = (Map.Entry)httpPost.next();
				String response = (String)bae.getValue();
				if(response != null) {
					pairs.add(new BasicNameValuePair((String)bae.getKey(), response));
				}
			}
		}

		HttpPost httpPost1 = new HttpPost(url);
//        ByteArrayEntity bae1 = new ByteArrayEntity(bytes);
		ByteArrayEntity bae1 = new ByteArrayEntity(post,contentType);//解决中文乱码问题
//		bae1.setContentEncoding("UTF-8");
//		bae1.setContentType("application/json");
		httpPost1.setEntity(bae1);
		CloseableHttpResponse response1 = null;
		byte[] bs = null;

		try {
			response1 = this.httpClient.execute(httpPost1);
			int e = response1.getStatusLine().getStatusCode();
			if(e != 200 && e != 201) {
				httpPost1.abort();
				throw new RuntimeException("HttpClient,error status code :" + e);
			}

			HttpEntity entity = response1.getEntity();
			if(entity != null) {
				bs = EntityUtils.toByteArray(entity);
			}
		} catch (Exception var21) {
			var21.printStackTrace();
		} finally {
			if(response1 != null) {
				try {
					response1.close();
				} catch (IOException var20) {
					throw new RuntimeException("HttpClient,Close Response ERROR ");
				}
			}

		}

		return bs;
	}

	@Override
	public void close() throws IOException {
		this.httpClient.close();

	}

}