package com.martin.apk.sign;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.martin.apk.sign.SignatureMode.MD5;
import static com.martin.apk.sign.SignatureMode.SHA1;


public class MainActivity extends AppCompatActivity {
    TextView signaturText;
    RadioGroup radioGroup;
    SignatureMode mode = SHA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                }
            }
        });
    }


    /**
     * 验证SHA1
     */
    public void checkSHA1(String sha1) {
        SignatureValidator signatureValidator = new SignatureValidator(this, sha1);
        if (signatureValidator.checkSHA1()) {
            showToast("ok");
        } else {
            showDialog();
        }
        String signature_sha1 = signatureValidator.getCertificateSHA1Fingerprint();
        signaturText.setText("SHA1:\t" + signature_sha1);
        Log.d(AppConfig.TAG, signature_sha1);
    }

    /**
     * 验证MD5
     */
    public void checkMD5(String md5) {
        SignatureValidator signatureValidator = new SignatureValidator(this, md5);
        if (signatureValidator.checkMD5()) {
            showToast("ok");
        } else {
            showDialog();
        }
        String signature_md5 = signatureValidator.getCertificateMD5Fingerprint();
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


    public void showDialog() {
        new AlertDialog.Builder(this).setTitle("警告")
                                     .setMessage("请前往官方渠道下载正版 app， http://.....")
                                     .setPositiveButton("确定", null)
                                     .show();
    }

}
