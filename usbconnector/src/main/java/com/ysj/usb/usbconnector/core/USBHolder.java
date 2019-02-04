package com.ysj.usb.usbconnector.core;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * USB 信息（单例）
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/3 14:20
 */
public class USBHolder {

    private USBHolder() {
    }

    private static class Holder {
        private static USBHolder INSTANCE = new USBHolder();
    }

    /**
     * 获取 USBHolder 实例
     */
    public static USBHolder getInstance() {
        return Holder.INSTANCE;
    }

    // 用于过滤设备的 filters
    public List<UsbFilter> filters = new ArrayList<>();

    // usb 的广播接收器
    private USBReceiver usbReceiver;
    // usb 设备
    private UsbDevice usbDevice;
    // 该设备的连接接口
    private UsbInterface usbInterface;
    // 设备的连接
    private UsbDeviceConnection usbDeviceConnection;
    // 输入端点
    private UsbEndpoint endpointIn;
    // 输出端点
    private UsbEndpoint endpointOut;

    /**
     * 设备拔出时
     */
    protected void onUsbDetached() {
        this.usbDevice = null;
        this.usbInterface = null;
        this.usbDeviceConnection = null;
        this.endpointIn = null;
        this.endpointOut = null;
    }

    /**
     * 当断开连接时
     */
    protected void onDisConnected() {
        this.usbDeviceConnection = null;
        this.endpointIn = null;
        this.endpointOut = null;
    }

    /**
     * 检查是否连接
     *
     * @return 若已连接则返回 true
     */
    public boolean isConnected() {
        return usbDeviceConnection != null && endpointOut != null && endpointIn != null;
    }

    protected USBReceiver getUsbReceiver() {
        return usbReceiver;
    }

    protected void setUsbReceiver(USBReceiver usbReceiver) {
        this.usbReceiver = usbReceiver;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }

    public UsbInterface getUsbInterface() {
        return usbInterface;
    }

    public void setUsbInterface(UsbInterface usbInterface) {
        this.usbInterface = usbInterface;
    }

    public UsbDeviceConnection getUsbDeviceConnection() {
        return usbDeviceConnection;
    }

    public void setUsbDeviceConnection(UsbDeviceConnection usbDeviceConnection) {
        this.usbDeviceConnection = usbDeviceConnection;
    }

    public UsbEndpoint getEndpointIn() {
        return endpointIn;
    }

    public void setEndpointIn(UsbEndpoint endpointIn) {
        this.endpointIn = endpointIn;
    }

    public UsbEndpoint getEndpointOut() {
        return endpointOut;
    }

    public void setEndpointOut(UsbEndpoint endpointOut) {
        this.endpointOut = endpointOut;
    }
}
