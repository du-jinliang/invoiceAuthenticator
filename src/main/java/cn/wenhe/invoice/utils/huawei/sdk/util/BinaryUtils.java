package cn.wenhe.invoice.utils.huawei.sdk.util;

import java.util.Locale;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class BinaryUtils {
    public BinaryUtils() {
    }

    public static String toHex(byte[] data) {
        StringBuffer sbuff = new StringBuffer(data.length * 2);
        byte[] var2 = data;
        int var3 = data.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte bye = var2[var4];
            String hexStr = Integer.toHexString(bye);
            if (hexStr.length() == 1) {
                sbuff.append("0");
            } else if (hexStr.length() == 8) {
                hexStr = hexStr.substring(6);
            }

            sbuff.append(hexStr);
        }

        return sbuff.toString().toLowerCase(Locale.getDefault());
    }
}
