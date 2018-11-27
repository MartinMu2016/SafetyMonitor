package com.martin.apk.safety.security;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.martin.apk.safety.R;
import com.martin.apk.safety.util.Utils;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.martin.apk.safety.util.Utils.*;

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


    /**
     *
     */
    public static void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getApp()).setTitle("警告")
                                                           .setMessage("请前往官方渠道下载正版 app， http://.....")
                                                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                              @SuppressLint("MissingPermission")
                                                              @Override
                                                              public void onClick(DialogInterface dialog, int which) {
                                                                  ActivityManager manager = (ActivityManager) getApp().getSystemService(ACTIVITY_SERVICE);
                                                                  manager.killBackgroundProcesses(getApp().getPackageName());
                                                              }
                                                          })
                                                           .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        dialog.show();
    }

    

}
