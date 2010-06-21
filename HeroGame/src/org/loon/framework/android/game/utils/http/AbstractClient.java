package org.loon.framework.android.game.utils.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.StringUtils;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
abstract class AbstractClient {

	private static char[] codec_table = new char[] { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };

	class BASE64Encoder {

		public BASE64Encoder() {

		}

		public String encode(byte[] a) {
			int totalBits = a.length * 8;
			int nn = totalBits % 6;
			int curPos = 0;
			StringBuffer toReturn = new StringBuffer();
			while (curPos < totalBits) {
				int bytePos = curPos / 8;
				switch (curPos % 8) {
				case 0:
					toReturn.append(codec_table[(a[bytePos] & 0xfc) >> 2]);
					break;
				case 2:

					toReturn.append(codec_table[(a[bytePos] & 0x3f)]);
					break;
				case 4:
					if (bytePos == a.length - 1) {
						toReturn
								.append(codec_table[((a[bytePos] & 0x0f) << 2) & 0x3f]);
					} else {
						int pos = (((a[bytePos] & 0x0f) << 2) | ((a[bytePos + 1] & 0xc0) >> 6)) & 0x3f;
						toReturn.append(codec_table[pos]);
					}
					break;
				case 6:
					if (bytePos == a.length - 1) {
						toReturn
								.append(codec_table[((a[bytePos] & 0x03) << 4) & 0x3f]);
					} else {
						int pos = (((a[bytePos] & 0x03) << 4) | ((a[bytePos + 1] & 0xf0) >> 4)) & 0x3f;
						toReturn.append(codec_table[pos]);
					}
					break;
				default:

					break;
				}
				curPos += 6;
			}
			if (nn == 2) {
				toReturn.append("==");
			} else if (nn == 4) {
				toReturn.append("=");
			}
			return toReturn.toString();

		}

	}

	final static private int threadsSize = 2;

	final static String POST = "POST";

	final static String GET = "GET";

	final static String HTTPS = "HTTPS";

	protected int timeOut, responseCode, responseLength, bufferedSize;

	protected String cookie, method, postData, digest, userName, passWord;

	protected HttpURLConnection connection;

	protected HashMap cookies;

	protected HashMap headerMap;

	protected InputStream inputStream;

	private HttpHeader header;

	private URL open;

	private String urlString;

	public AbstractClient(String urlString) {
		try {
			this.urlString = urlString;
			this.open(new URL(urlString.startsWith("http://")
					|| urlString.startsWith("https://")
					|| urlString.startsWith("ftp://") ? urlString
					: ("http://" + urlString).intern()));
		} catch (MalformedURLException e) {
		}
	}

	private HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			return true;
		}
	};

	protected static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new Trust();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	public static class Trust implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	public AbstractClient(URL url) {
		open(url);
	}

	private void open(URL url) {
		this.cookies = new HashMap();
		this.open = url;
		this.header = new HttpHeader();
		this.method = "GET";
		this.headerMap = new HashMap();
		this.timeOut = 180000;
	}

	private void postCookies() {
		StringBuffer sbr = new StringBuffer();
		Iterator it = cookies.entrySet().iterator();
		do {
			if (!it.hasNext()) {
				break;
			}
			Entry entry = (Entry) it.next();
			sbr.append(entry.getKey());
			sbr.append("=");
			sbr.append(entry.getValue());
			if (it.hasNext()) {
				sbr.append("; ");
			}
		} while (true);
		if (sbr.length() > 0) {
			connection.setRequestProperty("Cookie", cookie = sbr.toString());
			header.setCookie(cookie);
		}
	}

	public void begin() {
		try {
			boolean foundRedirect;
			do {
				if (HTTPS.equalsIgnoreCase(open.getProtocol())) {
					trustAllHttpsCertificates();
					HttpsURLConnection.setDefaultHostnameVerifier(hv);
					connection = (HttpsURLConnection) open.openConnection();
				} else {
					connection = (HttpURLConnection) open.openConnection();
					// 设定Cookies
					postCookies();

				}
				if (digest != null) {
					connection.setRequestProperty("Authorization", digest);
				}
				connection.setDoOutput(POST.equalsIgnoreCase(method));
				connection.setDoInput(true);
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(false);
				// 超时设定
				if (timeOut > 0) {
					System.setProperty("sun.net.client.defaultConnectTimeout",
							String.valueOf(timeOut));
					System.setProperty("sun.net.client.defaultReadTimeout",
							String.valueOf(timeOut));
				}
				connection.setRequestMethod(method);
				connection.setRequestProperty("User-Agent", header
						.getUserAgentValue());
				connection
						.setRequestProperty(
								"Accept",
								"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
				connection.setRequestProperty("Accept-Language", "zh-CN");
				connection.setRequestProperty("Accept-Charset",
						"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
				connection.setRequestProperty("Content-type", "text/html");
				connection.setRequestProperty("Cache-Control", "no-cache");
				if (POST.equalsIgnoreCase(method)) {
					connection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					DataOutputStream out = new DataOutputStream(connection
							.getOutputStream());
					out.writeBytes(postData);
					out.flush();
					out.close();
				}
				if (headerMap != null && headerMap.size() > 0) {
					Set headersSet = headerMap.entrySet();
					for (Iterator it = headersSet.iterator(); it.hasNext();) {
						Entry entry = (Entry) it.next();
						connection.setRequestProperty((String) entry.getKey(),
								(String) entry.getValue());
					}
				}
				// 尝试连接
				connection.connect();
				cookie = connection.getHeaderField("Set-Cookie");
				// 注入Cookies
				setCookies(StringUtils.split(cookie, ';'));
				// 获得响应状态
				responseCode = connection.getResponseCode();
				// 获得返回的数据长度
				responseLength = connection.getContentLength();
				if (responseCode == 302) {
					// 重定向
					String location = connection.getHeaderField("Location");
					open = new URL(location);
					foundRedirect = true;
				} else {
					if (responseCode == 200 || responseCode == 201) {
						inputStream = connection.getInputStream();
					} else {
						inputStream = connection.getErrorStream();
					}
					bufferedSize = responseLength == -1 ? 2048 : responseLength;
					foundRedirect = false;
				}
				// 如果重定向则继续
			} while (foundRedirect);
		} catch (Exception e) {
		}
	}

	/**
	 * 返回当前请求的端口号
	 * 
	 * @return
	 */
	public int getPort() {
		return ((open != null) && (open.getPort() != -1)) ? open.getPort() : 80;
	}

	/**
	 * 获得当前链接
	 * 
	 * @return
	 */
	public HttpURLConnection getConnection() {
		try {
			if (HTTPS.equalsIgnoreCase(open.getProtocol())) {
				trustAllHttpsCertificates();
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
				connection = (HttpsURLConnection) open.openConnection();
			} else {
				connection = (HttpURLConnection) open.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 检查对应的本地文件是否存在
	 * 
	 * @return
	 */
	public boolean exists() {
		File file = new File(getFileName());
		if (!file.exists()) {
			return false;
		}
		int size = -1;
		try {
			HttpURLConnection connection = getConnection();
			connection.connect();
			size = connection.getContentLength();
			connection.disconnect();
		} catch (Exception e) {

		}
		return size == file.length();
	}

	/**
	 * 返回当前请求下载的文件名
	 * 
	 * @return
	 */
	public String getFileName() {
		if (open == null) {
			return null;
		}
		return open.getPath().substring(open.getPath().lastIndexOf("/") + 1,
				open.getPath().length());
	}

	/**
	 * 设定cookies
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setCookie(String key, String value) throws IOException {
		cookies.put(key.trim(), value.trim());
	}

	/**
	 * 设定cookies
	 * 
	 * @param cookie
	 * @throws IOException
	 */
	public void setCookies(Map cookie) throws IOException {
		if (cookie == null) {
			return;
		} else {
			cookies.putAll(cookie);
			return;
		}
	}

	/**
	 * 设定cookies
	 * 
	 * @param cookie
	 * @throws IOException
	 */
	public void setCookies(String[] cookie) throws IOException {
		if (cookie == null) {
			return;
		}
		try {
			for (int i = 0; i < cookie.length; i++) {
				String[] ret = cookie[i].split("=");
				setCookie(ret[0], ret[1]);
			}
		} catch (Exception e) {
		}
	}

	public ExternalDownload getExternalDownload() throws Exception {
		return getExternalDownload(threadsSize);
	}

	public ExternalDownload getExternalDownload(String fileName)
			throws Exception {
		return getExternalDownload(fileName, threadsSize);
	}

	public ExternalDownload getExternalDownload(int threads) throws Exception {
		return getExternalDownload(getFileName(), threadsSize);
	}

	public ExternalDownload getExternalDownload(String fileName, int threads)
			throws Exception {
		return getExternalDownload(fileName, getPort(), threadsSize);
	}

	/**
	 * 使用外部下载器下载指定文件到指定路径
	 * 
	 * @param fileName
	 * @param port
	 * @param threads
	 * @return
	 * @throws Exception
	 */
	public ExternalDownload getExternalDownload(final String fileName,
			final int port, final int threads) throws Exception {
		if (fileName == null || fileName.length() == 0) {
			throw new Exception("File address is Unrecognized !");
		}
		HttpURLConnection connection = getConnection();
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		File tmp_file = new File(fileName + ".tmp");
		DownloadInfo info = new DownloadInfo(open.getHost(), open.getFile(),
				port, file, connection.getContentLength(), threads, tmp_file);
		SocketManager sm = new SocketManager(info);
		ExternalDownload down = new ExternalDownload(header, sm);
		connection.disconnect();
		return down;
	}

	/**
	 * 返回内部资源下载器
	 * 
	 * @return
	 */
	public InternalDownload getInternalDownload() {
		return new InternalDownload(this);
	}

	public String doHTML() {
		return doHTML(null);
	}

	/**
	 * 以指定格式显示网页字符串
	 * 
	 * @param encoding
	 * @return
	 */
	public String doHTML(String encoding) {
		if (connection == null) {
			return "";
		}
		try {
			if (encoding != null) {
				return readString(inputStream, bufferedSize, encoding);
			} else {
				return readString(inputStream, bufferedSize, LSystem.encoding);
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private static String readString(final InputStream in, final int size,
			final String encoding) throws IOException {
		StringBuffer sbr = new StringBuffer();
		int nSize = size;
		if (nSize == 0) {
			nSize = 2048;
		}
		char[] buffer = new char[nSize];
		int offset = 0;
		InputStreamReader isr = new InputStreamReader(in, encoding);
		while ((offset = isr.read(buffer)) != -1) {
			sbr.append(buffer, 0, offset);
		}
		in.close();
		isr.close();
		return sbr.toString();
	}

	public void setParameter(Map parameterMap) {
		if (parameterMap != null) {
			Set entrySet = parameterMap.entrySet();
			for (Iterator it = entrySet.iterator(); it.hasNext();) {
				Entry header = (Entry) it.next();
				String key = (String) header.getKey();
				String value = (String) header.getValue();
				if ("user".equals(key)) {
					userName = value;
				} else if ("pass".equals(key)) {
					passWord = value;
				} else if ("method".equals(key)) {
					method = value;
				} else if ("data".equals(key)) {
					postData = value;
				} else {
					headerMap.put(key, value);
				}
			}
		}
		if (userName != null && passWord != null) {
			BASE64Encoder base64 = new BASE64Encoder();
			digest = "Basic "
					+ base64.encode((userName + ":" + passWord).getBytes());
		}
	}

	public String getURLString() {
		return urlString;
	}

	public URL getURL() {
		return open;
	}

	public void end() {
		if (connection != null) {
			connection.disconnect();
			connection = null;
		}
	}

}
