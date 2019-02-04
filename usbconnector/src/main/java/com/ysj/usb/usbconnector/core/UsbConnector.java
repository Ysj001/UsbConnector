package com.ysj.usb.usbconnector.core;

import android.content.Context;

/**
 * usb 连接器操作类
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/21 11:21
 */
public class UsbConnector {
    /**
     * 初始化
     *
     * @return 若成功则返回 true
     */
    public static boolean init(Context context) {
        USBReceiver usbReceiver = new USBReceiver();
        USBHolder.getInstance().setUsbReceiver(usbReceiver);
        return usbReceiver.registerReceiver(context);
    }

    /**
     * 释放资源
     */
    public static void release(Context context) {
        context.unregisterReceiver(USBHolder.getInstance().getUsbReceiver());
        USBHolder.getInstance().onUsbDetached();
    }


}
