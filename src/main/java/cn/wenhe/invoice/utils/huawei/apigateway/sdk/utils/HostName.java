/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils;

public class HostName {
    private static String urlHostName;

    public static void setUrlHostName(String hostName) {
        urlHostName = hostName;
    }

    public static boolean checkHostName(String SSLHostName) {
        return urlHostName.equals(SSLHostName);
    }
}
