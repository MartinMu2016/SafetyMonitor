package com.martin.apk.safety;

import com.martin.apk.safety.security.https.HttpsConfig;
import com.martin.apk.safety.security.https.HttpsHostnameVerifier;

import okhttp3.OkHttpClient;

import static org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;

/**
 * <p>
 * Package Name:com.martin.apk.sign
 * </p>
 * <p>
 * Class Name:HttpManager
 * <p>
 * Description:网络访问配置
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/26 4:34 PM Release
 * @Reviser:
 * @Modification Time:2018/11/26 4:34 PM
 */
public class HttpManager {
    private static HttpManager instance;
    public OkHttpClient client;


    private HttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        try {
            client = builder.hostnameVerifier(STRICT_HOSTNAME_VERIFIER)
                            .hostnameVerifier(HttpsHostnameVerifier.RELEASE)
                            .sslSocketFactory(HttpsConfig.getSslSocketFactory2())
                            .build();

        } catch (Exception e) {
        }
    }


    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }


}
