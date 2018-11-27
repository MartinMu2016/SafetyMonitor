package com.martin.apk.safety.security.https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * <p>
 * Package Name:com.moor.im_ctcc.security.https
 * </p>
 * <p>
 * Class Name:HttpsHostnameVerifier
 * <p>
 * Description:主机域名检查配置
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/26 11:51 AM Release
 * @Reviser:
 * @Modification Time:2018/11/26 11:51 AM
 */
public abstract class HttpsHostnameVerifier {

    /**
     * 不做任何校验
     */
    public static HostnameVerifier NONE = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    /**
     * 发布版本校验
     *
     * 如果我们的证书是经过CA机构认证的，则直接做下面的代码就行。
     *
     */
    public static HostnameVerifier RELEASE = new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
        }
    };


}
