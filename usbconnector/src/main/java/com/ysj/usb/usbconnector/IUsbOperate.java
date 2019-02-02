package com.ysj.usb.usbconnector;

import android.content.Context;

import com.ysj.usb.usbconnector.core.OnDevicePermissionListener;
import com.ysj.usb.usbconnector.core.OnDeviceStateListener;

/**
 * 定义 usb 的操作接口
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/21 14:33
 */
public interface IUsbOperate
{

    /**
     * 设置设备状态监听
     */
    void setOnDeviceStateListener(OnDeviceStateListener listener);

    /**
     * 设置权限请求结果监听
     */
    void setOnDevicePermissionListener(OnDevicePermissionListener listener);

    /**
     * 连接 usb
     */
    void connected(Context context);

    /**
     * 断开 usb 连接
     */
    void disConnect();

}
