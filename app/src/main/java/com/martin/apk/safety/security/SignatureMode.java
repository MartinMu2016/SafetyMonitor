package com.martin.apk.safety.security;

/**
 * <p>
 * Package Name:com.martin.apk.sign
 * </p>
 * <p>
 * Class Name:SignatureMode
 * <p>
 * Description:签名枚举
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/11/23 11:06 AM Release
 * @Reviser:
 * @Modification Time:2018/11/23 11:06 AM
 */
public  enum SignatureMode {
    SHA1(0), MD5(1),CRC(2);

    int value;

    SignatureMode(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}