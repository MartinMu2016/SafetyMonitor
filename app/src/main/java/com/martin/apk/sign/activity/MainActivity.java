package com.martin.apk.sign.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.martin.apk.sign.R;
import com.martin.apk.sign.security.SafetyMonitor;
import com.martin.apk.sign.security.SignatureMode;

import static com.martin.apk.sign.security.SignatureMode.CRC;
import static com.martin.apk.sign.security.SignatureMode.MD5;
import static com.martin.apk.sign.security.SignatureMode.SHA1;


public class MainActivity extends AppCompatActivity {
    TextView signaturText;
    RadioGroup radioGroup;
    SignatureMode mode = SHA1;
    SafetyMonitor safetyMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            showDialog();
        }
    }

    /**
     * 验证SHA1
     */
    public void checkSHA1(String sha1) {
        if (SafetyMonitor.checkSHA1(this, sha1)) {
            showToast("ok");
        } else {
            showDialog();
        }
/*        SignatureChecker signatureChecker = new SignatureChecker(this, sha1);
        if (signatureChecker.checkSHA1()) {
            showToast("ok");
        } else {
            showDialog();
        }
        String signature_sha1 = signatureChecker.getCertificateSHA1Fingerprint();
        signaturText.setText("SHA1:\t" + signature_sha1);
        Log.d(AppConfig.TAG, signature_sha1);*/
    }

    /**
     * 验证MD5
     */
    public void checkMD5(String md5) {
        if (SafetyMonitor.checkMD5(this, md5)) {
            showToast("ok");
        } else {
            showDialog();
        }
        /*SignatureChecker signatureChecker = new SignatureChecker(this, md5);
        if (signatureChecker.checkMD5()) {
            showToast("ok");
        } else {
            showDialog();
        }
        String signature_md5 = signatureChecker.getCertificateMD5Fingerprint();
        signaturText.setText("MD5:\t" + signature_md5);
        Log.d(AppConfig.TAG, signature_md5);*/
    }

    /**
     * 显示Toast
     *
     * @param str
     */
    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }


    public void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("警告")
                                                          .setMessage("请前往官方渠道下载正版 app， http://.....")
                                                          .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                              @SuppressLint("MissingPermission")
                                                              @Override
                                                              public void onClick(DialogInterface dialog, int which) {
                                                                  ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                                                  manager.killBackgroundProcesses(getPackageName());
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

    //1f3182f8b415125feb345e98e7b3a331
    //1f3182f8b415125feb345e98e7b3a331
    //1f3182f8b415125feb345e98e7b3a331




}
