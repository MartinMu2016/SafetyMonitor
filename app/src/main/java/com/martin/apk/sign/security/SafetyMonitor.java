package com.martin.apk.sign.security;

import android.content.Context;

import com.martin.apk.sign.R;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * Package Name:com.martin.apk.sign
 * </p>
 * <p>
 * Class Name:SafetyChecker
 * <p>
 * Description:App的安全检测器
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/23 2:44 PM Release
 * @Reviser:
 * @Modification Time:2018/11/23 2:44 PM
 */
public class SafetyMonitor {

    /**
     * 验证CRC的值
     * @param context
     * @return
     */
    public static boolean classesDexCheck(Context context)
    {
        String apkPath = context.getPackageCodePath();
        Long dexCrc = Long.parseLong(context.getString(R.string.classesdex_crc));
        ZipEntry dexentry = null;
        try {
            ZipFile zipfile = new ZipFile(apkPath);
            dexentry = zipfile.getEntry("classes.dex");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dexentry.getCrc() == dexCrc;
    }



    /**
     * 验证SHA1
     */
    public static boolean checkSHA1(Context context,String sha1) {
        SignatureChecker signatureChecker = new SignatureChecker(context, sha1);
        return signatureChecker.checkSHA1();
    }


    /**
     * 验证MD5
     * @param md5
     */
    public static boolean checkMD5(Context context,String md5) {
        SignatureChecker signatureChecker = new SignatureChecker(context, md5);
        return signatureChecker.checkMD5();
    }





    

}
