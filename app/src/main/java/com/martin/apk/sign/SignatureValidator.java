package com.martin.apk.sign;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * <p>
 * Package Name:com.martin.apk.sign
 * </p>
 * <p>
 * Class Name:SignatureValidator
 * <p>
 * Description:签名检查器
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/23 10:49 AM Release
 * @Reviser:
 * @Modification Time:2018/11/23 10:49 AM
 */
public class SignatureValidator {
    private Context context;
    private String cer = null;
    private String realCer = null;//传入的签名
    private static final String TAG = "SignCheck";
 

    public SignatureValidator(Context context, String realCer) {
        this.context = context;
        this.realCer = realCer;
    }

    /**
     * 获取应用的SHA1-数字指纹
     *
     * @return
     */
    public String getCertificateSHA1Fingerprint() {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
 
        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = context.getPackageName();
 
        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
 
        PackageInfo packageInfo = null;
 
        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
 
        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
 
        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
 
        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
 
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        //X509 证书，X.509 是一种非常通用的证书格式
        X509Certificate c = null;
 
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        String hexString = null;
 
        try {
            //加密算法的类，这里的参数可以使 MD4,MD5 等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");
 
            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());
 
            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
 
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }


    /**
     *
     * 获取应用的MD5数字指纹
     *
     * @return
     */
    public String getCertificateMD5Fingerprint() {
        //获取包管理器
        PackageManager pm = context.getPackageManager();

        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = context.getPackageName();

        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();

        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);

        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;

        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //X509 证书，X.509 是一种非常通用的证书格式
        X509Certificate c = null;

        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String hexString = null;

        try {
            //加密算法的类，这里的参数可以使 MD4,MD5 等加密算法
            MessageDigest md = MessageDigest.getInstance("MD5");

            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());

            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }


    /**
     * 这里是将获取到得编码进行16 进制转换
     *
     * @param arr
     * @return
     */
    private String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
 
        for (int i = 0; i <arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l =h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }
 
    /**
     * 检测SHA1签名是否正确
     * @return true 签名正常 false 签名不正常
     */
    public boolean checkSHA1() {
        if (this.realCer != null) {
            cer = EncryptUtil.MD5(getCertificateSHA1Fingerprint().trim());//再次MD5加密
            realCer = realCer.trim();
            if (this.cer.equals(this.realCer)) {
                return true;
            }
        }else {
            Log.e(TAG, "未给定真实的签名 SHA-1 值");
        }
        return false;
    }

    /**
     * 检测MD5签名是否正确
     *
     * @return
     */
    public boolean checkMD5() {
        if (this.realCer != null) {
            cer = EncryptUtil.MD5(getCertificateMD5Fingerprint().trim());//再次MD5加密
            realCer = realCer.trim();
            if (this.cer.equals(this.realCer)) {
                return true;
            }
        }else {
            Log.e(TAG, "未给定真实的签名 MD5 值");
        }
        return false;
    }

}
