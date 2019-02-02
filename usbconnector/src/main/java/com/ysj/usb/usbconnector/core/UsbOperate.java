package com.ysj.usb.usbconnector.core;

import android.content.Context;

import com.ysj.usb.usbconnector.IUsbOperate;

/**
 * usb 的相关操作类
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/21 14:33
 */
public class UsbOperate implements IUsbOperate
{

    @Override
    public void setOnDeviceStateListener(OnDeviceStateListener listener)
    {
        USBHolder.getInstance().getUsbReceiver().setOnDeviceStateListener(listener);
    }

    @Override
    public void setOnDevicePermissionListener(OnDevicePermissionListener listener)
    {
        USBHolder.getInstance().getUsbReceiver().setOnDevicePermissionListener(listener);
    }

    @Override
    public void connected(Context context)
    {
        USBHolder.getInstance().getUsbReceiver().connected(context);
    }

    @Override
    public void disConnect()
    {
        if (USBHolder.getInstance().isConnected())
        {
            USBHolder.getInstance().getUsbDeviceConnection().close();
            USBHolder.getInstance().onDisConnected();
            USBHolder.getInstance().getUsbReceiver().sendDeviceState(UsbAction.ACTION_USB_DISCONNECTED);
        }
    }

}
