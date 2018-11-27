package com.martin.apk.sign.security.https;

import com.moor.im_ctcc.common.utils.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * <p>
 * Package Name:com.moor.im_ctcc.security.https
 * </p>
 * <p>
 * Class Name:HttpsTrustManager
 * <p>
 * Description:证书验证配置
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/26 11:51 AM Release
 * @Reviser:
 * @Modification Time:2018/11/26 11:51 AM
 */
public abstract class HttpsTrustManager {

    /**
     * 不做校验
     */
    public static TrustManager NONE = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };


    /**
     * 自定义证书校验
     */
    public static TrustManager RELEASE = new X509TrustManager() {

        private X509Certificate serverCert;
        private final String CRT_NAME = "7moor.crt";

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException{
            if (x509Certificates == null) {
                throw new IllegalArgumentException("check Server X509Certificates is null");
            }

            if (x509Certificates.length < 0) {
                throw new IllegalArgumentException("check Server X509Certificates is empty");
            }
            InputStream caInput = null;
            try {
                caInput = new BufferedInputStream(Utils.getApp()
                                                       .getAssets()
                                                       .open(CRT_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            serverCert = (X509Certificate) cf.generateCertificate(caInput);


            for (X509Certificate cert : x509Certificates) {
                try {
                    cert.checkValidity();
                    cert.verify(serverCert.getPublicKey());//和App预埋证书做对比
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };


}
