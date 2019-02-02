package com.ysj.usb.usbconnector.core;

/**
 * Usb 权限获取监听
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/18 11:12
 */
public interface OnDevicePermissionListener
{
    /**
     * 权限请求结果监听
     *
     * @param hasPermission 成功获取权限则为 true
     */
    void onDevicePermission(boolean hasPermission);
}
