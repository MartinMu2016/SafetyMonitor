package com.martin.apk.safety.security.https;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * <p>
 * Package Name:com
 * </p>
 * <p>
 * Class Name:HttpsConfig
 * <p>
 * Description:
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/26 4:30 PM Release
 * @Reviser:
 * @Modification Time:2018/11/26 4:30 PM
 */
public class HttpsConfig {

    private final static String X_509 = "X.509";
    private final static String TSL = "TLS";
    private final static String CA = "ca";

    /**
     * 默认获取发布的
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLSocketFactory getSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[]{HttpsTrustManager.RELEASE};
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        return sslContext.getSocketFactory();
    }

    /**
     * 自定义证书受信用
     *
     * <href>https://developer.android.com/training/articles/security-ssl</href>
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     */
    public static SSLSocketFactory getSslSocketFactory2() throws NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, IOException {
        //以X.509格式获取证书
        CertificateFactory cf = CertificateFactory.getInstance(X_509);
        InputStream caInput = null;
        try {
             caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));//一般是存放在Assert下
        } catch (IOException e) {
            e.printStackTrace();
        }
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // 创建包含受信任CA的KeyStore
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry(CA, ca);

        // 创建一个信任我们的KeyStore中的CA的TrustManager
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // 创建一个使用我们的TrustManager的SSLContext
        SSLContext context = SSLContext.getInstance(TSL);
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }


}
