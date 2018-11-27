package com.martin.apk.safety.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.martin.apk.safety.AppConfig;
import com.martin.apk.safety.R;
import com.martin.apk.safety.security.SafetyMonitor;
import com.martin.apk.safety.security.SignatureChecker;
import com.martin.apk.safety.security.SignatureMode;
import com.martin.apk.safety.util.Utils;

import static com.martin.apk.safety.security.SignatureMode.CRC;
import static com.martin.apk.safety.security.SignatureMode.MD5;
import static com.martin.apk.safety.security.SignatureMode.SHA1;

/**
 * <p>
 * Package Name:com.martin.apk.safety.activity
 * </p>
 * <p>
 * Class Name:MainActivity
 * <p>
 * Description:Main entrance
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/27 4:21 PM Release
 * @Reviser:
 * @Modification Time:2018/11/27 4:21 PM
 */
public class MainActivity extends AppCompatActivity {
    TextView signaturText;
    RadioGroup radioGroup;
    SignatureMode mode = SHA1;
    SafetyMonitor safetyMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this.getApplication());
        this.safetyMonitor = new SafetyMonitor();
        this.radioGroup = findViewById(R.id.radiogroup);
        this.signaturText = findViewById(R.id.signaturText);
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sha1:
                        mode = SHA1;
                        break;
                    case R.id.rb_md5:
                        mode = MD5;
                        break;
                    case R.id.rb_crc:
                        mode = CRC;
                        break;
                }
            }
        });

        findViewById(R.id.signaturBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case SHA1:
                        checkSHA1(getString(R.string.certificate_sha1));
                        break;
                    case MD5:
                        checkMD5(getString(R.string.certificate_md5));
                        break;
                    case CRC:
                        classesDexCheck();
                        break;

                }
            }
        });
    }

    /**
     * crc验证
     */
    private void classesDexCheck() {
        if (SafetyMonitor.classesDexCheck(this)) {
            showToast("ok");
        } else {
            SafetyMonitor.showDialog();
        }
    }

    /**
     * 验证SHA1
     * <p>
     *     if (SafetyMonitor.checkSHA1(this, sha1)) {
     *                  showToast("ok");
     *              } else {
     *                  showDialog();
     *              }
     * </p>
     **/
    public void checkSHA1(String sha1) {
        SignatureChecker signatureChecker = new SignatureChecker(this, sha1);
        if (signatureChecker.checkSHA1()) {
            showToast("ok");
        } else {
            SafetyMonitor.showDialog();
        }
        String signature_sha1 = signatureChecker.getCertificateSHA1Fingerprint();
        signaturText.setText("SHA1:\t" + signature_sha1);
        Log.d(AppConfig.TAG, signature_sha1);

    }

    /**
     * 验证MD
     *
     * <p>
     *     if (SafetyMonitor.checkMD5(this, md5)) {
     *             showToast("ok");
     *         } else {
     *             showDialog();
     *         }
     *
     * </p>
     *
     */
    public void checkMD5(String md5) {
        SignatureChecker signatureChecker = new SignatureChecker(this, md5);
        if (signatureChecker.checkMD5()) {
            showToast("ok");
        } else {
            SafetyMonitor.showDialog();
        }
        String signature_md5 = signatureChecker.getCertificateMD5Fingerprint();
        signaturText.setText("MD5:\t" + signature_md5);
        Log.d(AppConfig.TAG, signature_md5);
    }

    /**
     * 显示Toast
     *
     * @param str
     */
    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }


}
