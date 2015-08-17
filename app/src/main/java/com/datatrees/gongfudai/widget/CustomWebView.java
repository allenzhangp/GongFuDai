package com.datatrees.gongfudai.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.StringUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * use customLoadUrl(url,endUrls) when visit endUrls will callback onVisitEndUrl.
 * Created by zhangping on 15/7/25.
 */
public class CustomWebView extends WebView {

    private String[] endUrls;
    private List<String> cookies;
    private OnVisitEndUrl onVisitEndUrl;
    private String headerJSONObj;
    private String cssStr;
    private StringBuilder jsCssStr;
    private boolean usePCUA;

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * @param url     visit url
     * @param endUrls end urls
     */
    public void customLoadUrl(String url, String[] endUrls, String cssStr, boolean usePCUA) {
        if (cookies != null)
            cookies.clear();
        else
            cookies = new ArrayList<>();
        this.endUrls = endUrls;
        this.cssStr = cssStr;
        this.usePCUA = usePCUA;

        if (StringUtils.isNotTrimBlank(cssStr)) {
            jsCssStr = new StringBuilder();
            jsCssStr.append("var newscript = document.createElement(\"script\");");
            jsCssStr.append("newscript.onload=function(){");
            jsCssStr.append("var newstyle = document.createElement(\"style\");");
            jsCssStr.append("newstyle.type=\"text/css\"");
            jsCssStr.append("newstyle.appendChild(document.createTextNode(");
            jsCssStr.append(cssStr);
            jsCssStr.append("))");
            jsCssStr.append("};");
        }

        if (usePCUA) {
            getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0");
        }

        loadUrl(url);
    }

    private boolean isEnd(String url) {
        boolean isEnd = false;
        if (StringUtils.isNotTrimBlank(url) && endUrls != null && endUrls.length > 0) {
            for (int i = 0; i < endUrls.length; i++) {
                if (url.contains(endUrls[i])) {
                    isEnd = true;
                    break;
                }
            }
        }
        return isEnd;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String url = (String) msg.obj;
            if (isEnd(url) && onVisitEndUrl != null) {
                LogUtil.i("TAG", "end url-->" + url);
                onVisitEndUrl.onVisitEndUrl(url, (String[]) cookies.toArray(new String[cookies.size()]), headerJSONObj);
            } else {
                loadUrl(url);
            }
        }
    };

    private void init() {
        cookies = new ArrayList<>();
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDefaultTextEncodingName("utf-8");
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                LogUtil.d("info", "===>>> shouldOverrideUrlLoading method is called!-->" + url);
                Thread theard = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = null;
                            URL murl = new URL(url);
                            URI uri = new URI(murl.getProtocol(), murl.getHost(), murl.getPath(), murl.getQuery(), null);

                            // 参数
                            HttpParams httpParameters = new BasicHttpParams();
                            // 设置连接超时
                            HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
                            // 设置socket超时
                            HttpConnectionParams.setSoTimeout(httpParameters, 3000);
                            // 获取HttpClient对象 （认证）
                            HttpClient hc = initHttpClient(httpParameters);
                            HttpPost post = new HttpPost(uri);
                            // 发送数据类型
                            post.addHeader("Content-Type", "application/json;charset=utf-8");
                            // 接受数据类型
                            post.addHeader("Accept", "application/json");
                            post.setParams(httpParameters);
                            HttpResponse response = null;
                            try {
                                response = hc.execute(post);
                            } catch (UnknownHostException e) {
                                throw new Exception("Unable to access " + e.getLocalizedMessage());
                            } catch (SocketException e) {
                                e.printStackTrace();
                            }
                            int sCode = response.getStatusLine().getStatusCode();
                            if (sCode == HttpStatus.SC_OK) {
                                Header[] headers = response.getAllHeaders();
                                for (Header header : headers) {
                                    String name = header.getName();
                                    String value = header.getValue();
                                    jsonObject = new JSONObject();
                                    jsonObject.put(name, value);

                                }
                                if (jsonObject != null)
                                    headerJSONObj = jsonObject.toString();

                                Message msg = handler.obtainMessage();
                                msg.obj = url;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                theard.start();


                return true;
            }

            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                LogUtil.e("TAG", "Cookies = " + cookieStr);
                if (StringUtils.isNotTrimBlank(cookieStr)) {
                    cookies.add(cookieStr);
                }
                if (jsCssStr != null && StringUtils.isNotTrimBlank(jsCssStr.toString()))
                    loadUrl("javascript:" + jsCssStr.toString());
                super.onPageFinished(view, url);
            }

        });
    }

    public String convertToString(InputStream inputStream) {
        StringBuffer string = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                string.append(line + "\n");
            }
        } catch (IOException e) {
        }
        return string.toString();
    }

    private String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }

        return "";
    }

    private static HttpClient client = null;

    /**
     * 初始化HttpClient对象
     *
     * @param params
     * @return
     */
    public static synchronized HttpClient initHttpClient(HttpParams params) {
        if (client == null) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
                //允许所有主机的验证
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                // 设置http和https支持
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultHttpClient(params);
            }
        }
        return client;
    }

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType)
                        throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    public void setOnVisitEndUrl(OnVisitEndUrl onVisitEndUrl) {
        this.onVisitEndUrl = onVisitEndUrl;
    }

    public interface OnVisitEndUrl {
        void onVisitEndUrl(String endUrl, String[] cookies, String headerStr);
    }

}
