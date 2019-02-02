package com.ysj.usb.usbconnector.core;

/**
 * 自定义的 USB 相关 action
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/18 14:37
 */
public interface UsbAction
{
    /**
     * usb 连接上的 action
     */
    String ACTION_USB_CONNECTED = "ACTION_USB_CONNECTED";

    /**
     * usb 断开连接的 action
     */
    String ACTION_USB_DISCONNECTED = "ACTION_USB_DISCONNECTED";

    /**
     * 未知的 usb 设备 action
     */
    String ACTION_DEVICE_UNKNOWN = "ACTION_DEVICE_UNKNOWN";

}
