package com.ysj.usb.usbconnector.core;

import android.hardware.usb.UsbManager;

/**
 * USB 的状态监听器
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/18 11:09
 */
public interface OnDeviceStateListener {
    /**
     * 设备状态监听
     *
     * @param action <br>
     *               插入：{@linkplain UsbManager#ACTION_USB_DEVICE_ATTACHED}<br>
     *               拔出：{@linkplain UsbManager#ACTION_USB_DEVICE_DETACHED}<br>
     *               其他：{@linkplain UsbAction}
     */
    void onDeviceState(String action);
}
