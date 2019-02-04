package com.ysj.usb.usbconnector.core;

import android.hardware.usb.UsbManager;

/**
 * 自定义的 USB 相关 action
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/18 14:37
 */
public interface UsbAction {

    /**
     * USB device 插入时
     * <br>
     * Activity intent sent when user attaches a USB device.
     * <p>
     * This intent is sent when a USB device is attached to the USB bus when in host mode.
     * <ul>
     * <li> {@link UsbManager#EXTRA_DEVICE} containing the {@link android.hardware.usb.UsbDevice}
     * for the attached device
     * </ul>
     */
    String ACTION_USB_DEVICE_ATTACHED = UsbManager.ACTION_USB_DEVICE_ATTACHED;

    /**
     * USB device 拔出时
     * <br>
     * Broadcast Action:  A broadcast for USB device detached event.
     * <p>
     * This intent is sent when a USB device is detached from the USB bus when in host mode.
     * <ul>
     * <li> {@link UsbManager#EXTRA_DEVICE} containing the {@link android.hardware.usb.UsbDevice}
     * for the detached device
     * </ul>
     */
    String ACTION_USB_DEVICE_DETACHED = UsbManager.ACTION_USB_DEVICE_DETACHED;

    /**
     * USB device 建立连接成功的 action
     * <p>
     * 此时会自动初始化
     * {@linkplain USBHolder#usbDeviceConnection} ,<br>
     * {@linkplain USBHolder#endpointIn} ,<br>
     * {@linkplain USBHolder#endpointOut}
     */
    String ACTION_USB_CONNECTED = "ACTION_USB_CONNECTED";

    /**
     * USB device 没有建立连接的 action
     */
    String ACTION_USB_NO_CONNECTED = "ACTION_USB_NO_CONNECTED";

    /**
     * 未知的 USB device 的 action
     * <p>
     * 及插入的设备未在 device_filter.xml 中找到
     */
    String ACTION_DEVICE_UNKNOWN = "ACTION_DEVICE_UNKNOWN";

}
