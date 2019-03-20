package com.ysj.usb.usbconnector.core;

import android.app.Application;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * usb 连接器操作类
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/21 11:21
 */
public class UsbConnector {

    private UsbConnector() {
    }

    /**
     * 注册连接器
     *
     * @return 若成功则返回 true
     */
    public static boolean register(Context context) {
        if (USBHolder.getInstance().getUsbReceiver() == null) {
            USBHolder.getInstance().setUsbReceiver(new USBReceiver());
        }
        return USBHolder.getInstance().getUsbReceiver().registerReceiver(context);
    }

    /**
     * 释放资源
     */
    public static void unregister(Context context) {
        context.getApplicationContext().unregisterReceiver(USBHolder.getInstance().getUsbReceiver());
        USBHolder.getInstance().onUsbDetached();
    }

    /**
     * 设置 device_filter.xml 中的设备插入后的行为<br>
     * 此方法应在 {@linkplain Application#onCreate()}中调用
     *
     * @param doOnMyDeviceAttached device_filter.xml 中的设备插入后要做的行为
     */
    public static void setDoOnMyDeviceAttached(Context context, Runnable doOnMyDeviceAttached) {
        if (USBHolder.getInstance().getUsbReceiver() != null) {
            USBHolder.getInstance().getUsbReceiver().setDoOnMyDeviceAttached(doOnMyDeviceAttached);
            return;
        }
        USBReceiver usbReceiver = new USBReceiver();
        usbReceiver.setDoOnMyDeviceAttached(doOnMyDeviceAttached);
        USBHolder.getInstance().setUsbReceiver(usbReceiver);
        UsbManager usbManager = (UsbManager) context.getApplicationContext().getSystemService(Context.USB_SERVICE);
        Log.d("setDoOnMyDeviceAttached", "usbManager: " + usbManager);
        if (usbManager == null)
            return;
        usbReceiver.initDeviceFilter(context.getApplicationContext());
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            if (usbReceiver.isMyDevice(device)) {
                doOnMyDeviceAttached.run();
                Log.d("setDoOnMyDeviceAttached", "success");
            }
        }
    }

    /**
     * 设置成功获取权限后的行为<br>
     * 此方法应在 {@linkplain Application#onCreate()}中调用
     *
     * @param doOnDeviceHasPermission 获取权限失败后的行为
     */
    public static void setDoOnDeviceHasPermission(Runnable doOnDeviceHasPermission) {
        if (USBHolder.getInstance().getUsbReceiver() != null) {
            USBHolder.getInstance().getUsbReceiver().setDoOnDeviceHasPermission(doOnDeviceHasPermission);
            return;
        }
        USBReceiver usbReceiver = new USBReceiver();
        usbReceiver.setDoOnDeviceHasPermission(doOnDeviceHasPermission);
        USBHolder.getInstance().setUsbReceiver(usbReceiver);
    }

    /**
     * 设置获取权限失败后的行为<br>
     * 此方法应在 {@linkplain Application#onCreate()}中调用
     *
     * @param doOnDeviceNoPermission 获取权限失败后的行为
     */
    public static void setDoOnDeviceNoPermission(Runnable doOnDeviceNoPermission) {
        if (USBHolder.getInstance().getUsbReceiver() != null) {
            USBHolder.getInstance().getUsbReceiver().setDoOnDeviceNoPermission(doOnDeviceNoPermission);
            return;
        }
        USBReceiver usbReceiver = new USBReceiver();
        usbReceiver.setDoOnDeviceNoPermission(doOnDeviceNoPermission);
        USBHolder.getInstance().setUsbReceiver(usbReceiver);
    }
}
