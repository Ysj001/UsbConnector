package com.ysj.usb.usbconnector.core;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

/**
 * USB 的广播接收器
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/18 10:46
 */
public class USBReceiver extends BroadcastReceiver {
    private static final String TAG = USBReceiver.class.getSimpleName();

    /**
     * 请求 usb 连接权限的 action
     */
    public static final String ACTION_USB_PERMISSION = "USB_PERMISSION";

    private UsbManager mUsbManager;
    private USBHolder mUsbHolder;
    private OnDeviceStateListener onDeviceStateListener;
    private OnDevicePermissionListener onDevicePermissionListener;
    // 记录未设置 OnDeviceStateListener 前的状态 action
    private String mAction;
    // device_filter.xml 中的设备中的设备插入后的行为
    @Nullable
    private Runnable doOnMyDeviceAttached;
    // 行为是否已经发生的标记（保证不会重复调用）
    private boolean isDoOnMyDeviceAttached = false;

    // 获取权限失败后的行为
    @Nullable
    private Runnable doOnDeviceHasPermission;
    private boolean isDoOnDeviceHasPermission = false;

    // 获取权限失败后的行为
    @Nullable
    private Runnable doOnDeviceNoPermission;
    private boolean isDoOnDeviceNoPermission = false;

    protected USBReceiver() {
        mUsbHolder = USBHolder.getInstance();
    }

    /**
     * 设置设备状态监听
     */
    protected void setOnDeviceStateListener(OnDeviceStateListener onDeviceStateListener) {
        this.onDeviceStateListener = onDeviceStateListener;
        // 在设置了状态监听后回调上次没回调成功的状态
        if (!TextUtils.isEmpty(mAction) && onDeviceStateListener != null) {
            sendDeviceState(mAction);
            mAction = null;
        }
    }

    /**
     * 回调设备状态
     *
     * @param action 状态 action
     */
    protected void sendDeviceState(@NonNull String action) {
        if (this.onDeviceStateListener != null) {
            this.onDeviceStateListener.onDeviceState(action);
            return;
        }
        mAction = action;
    }

    /**
     * 设置权限请求结果监听
     */
    protected void setOnDevicePermissionListener(OnDevicePermissionListener onDevicePermissionListener) {
        this.onDevicePermissionListener = onDevicePermissionListener;
    }

    /**
     * 回调权限状态
     *
     * @param hasPermission 有连接权限则为 true
     */
    private void sendPermissionState(boolean hasPermission) {
        if (this.onDevicePermissionListener != null) {
            this.onDevicePermissionListener.onDevicePermission(hasPermission);
        }
    }

    /**
     * 设置device_filter.xml 中的设备插入后的行为
     *
     * @param doOnMyDeviceAttached device_filter.xml 中的设备插入后的行为
     */
    public void setDoOnMyDeviceAttached(@Nullable Runnable doOnMyDeviceAttached) {
        this.doOnMyDeviceAttached = doOnMyDeviceAttached;
    }

    /**
     * 设置成功获取权限后的行为
     *
     * @param doOnDeviceHasPermission 成功获取权限后的行为
     */
    public void setDoOnDeviceHasPermission(Runnable doOnDeviceHasPermission) {
        this.doOnDeviceHasPermission = doOnDeviceHasPermission;
    }

    private void doOnDeviceHasPermission(boolean hasPermission) {
        if (this.doOnDeviceHasPermission != null && !isDoOnDeviceHasPermission && hasPermission) {
            this.doOnDeviceHasPermission.run();
            isDoOnDeviceHasPermission = true;
            Log.d(TAG, "doOnDeviceHasPermission");
        }
    }

    /**
     * 设置获取权限失败后的行为
     *
     * @param doOnDeviceMoPermission 获取权限失败后的行为
     */
    public void setDoOnDeviceNoPermission(Runnable doOnDeviceMoPermission) {
        this.doOnDeviceNoPermission = doOnDeviceMoPermission;
    }

    private void doOnDeviceNoPermission(boolean hasPermission) {
        if (this.doOnDeviceNoPermission != null && !isDoOnDeviceNoPermission && !hasPermission) {
            this.doOnDeviceNoPermission.run();
            isDoOnDeviceNoPermission = true;
            Log.d(TAG, "doOnDeviceNoPermission");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        switch (action) {
            case ACTION_USB_PERMISSION:
                // 获取权限结果
                boolean hasPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                doOnDeviceHasPermission(hasPermission);
                doOnDeviceNoPermission(hasPermission);
                if (hasPermission) {
                    connected(context);
                }
                sendPermissionState(hasPermission);
                break;
            // 有新设备插入
            case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                onDeviceAttached(context, usbDevice);
                break;
            // 设备拔出了
            case UsbManager.ACTION_USB_DEVICE_DETACHED:
                isDoOnMyDeviceAttached = false;
                isDoOnDeviceHasPermission = false;
                isDoOnDeviceNoPermission = false;
                mUsbHolder.onUsbDetached();
                sendDeviceState(action);
                break;
        }
    }

    private void onDeviceAttached(Context context, UsbDevice device) {
        mUsbHolder.setUsbDevice(device);
        if (!isMyDevice(device)) {
            // 回调未知设备
            sendDeviceState(UsbAction.ACTION_DEVICE_UNKNOWN);
            return;
        }
        // 回调已插入
        sendDeviceState(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        if (doOnMyDeviceAttached != null && !isDoOnMyDeviceAttached) {
            Log.d(TAG, "doOnMyDeviceAttached");
            doOnMyDeviceAttached.run();
            isDoOnMyDeviceAttached = true;
        }
        if (initMyDeviceInterface(device)) {
            connected(context);
        } else {
            // 回调未知设备接口
            sendDeviceState(UsbAction.ACTION_DEVICE_UNKNOWN_INTERFACE);
        }
    }

    /**
     * 连接 usb
     */
    public void connected(Context context) {
        if (context == null) {
            return;
        }
        if (mUsbHolder.isConnected()) {
            sendDeviceState(UsbAction.ACTION_USB_CONNECTED);
            return;
        }
        UsbDevice device = mUsbHolder.getUsbDevice();
        UsbInterface usbInterface = mUsbHolder.getUsbInterface();
        if (mUsbManager == null || device == null || usbInterface == null) {
            return;
        }
        // 判断是否有 usb 的连接权限，若没有则提示请求权限
        boolean hasPermission = mUsbManager.hasPermission(device);
        doOnDeviceHasPermission(hasPermission);
        doOnDeviceNoPermission(hasPermission);
        if (!hasPermission) {
            mUsbManager.requestPermission(device,
                    PendingIntent.getBroadcast(context, 1, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_ONE_SHOT)
            );
            return;
        }
        // 获取连接
        UsbDeviceConnection connection = mUsbManager.openDevice(device);
        if (connection == null) {
            return;
        }
        // 打开传输接口
        boolean claimInterface = connection.claimInterface(usbInterface, true);
        if (!claimInterface) {
            return;
        }
        // 获取输入输出端点
        UsbEndpoint mUsbEndpointIn = null;
        UsbEndpoint mUsbEndpointOut = null;
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint usbEndpoint = usbInterface.getEndpoint(i);
            // 判断端点的类型（USB_DIR_IN 输入）
            if (usbEndpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                mUsbEndpointIn = usbEndpoint;
            } else {
                mUsbEndpointOut = usbEndpoint;
            }
        }
        if (mUsbEndpointIn == null || mUsbEndpointOut == null) {
            return;
        }
        mUsbHolder.setUsbDeviceConnection(connection);
        mUsbHolder.setEndpointIn(mUsbEndpointIn);
        mUsbHolder.setEndpointOut(mUsbEndpointOut);
        // 回调已连接
        sendDeviceState(UsbAction.ACTION_USB_CONNECTED);
    }

    /**
     * 初始化设备的接口
     *
     * @param usbDevice *
     */
    private boolean initMyDeviceInterface(UsbDevice usbDevice) {
        for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
            UsbInterface usbInterface = usbDevice.getInterface(i);
            for (UsbFilter filter : mUsbHolder.filters) {
                if (usbInterface.getId() == filter.getInterfaceId() && usbInterface.getInterfaceSubclass() == filter.getInterfaceSubclass()) {
                    mUsbHolder.setUsbInterface(usbInterface);
                    mUsbHolder.setUsbDevice(usbDevice);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是需要的设备
     *
     * @param usbDevice *
     * @return 如果是返回 true
     */
    public boolean isMyDevice(UsbDevice usbDevice) {
        if (usbDevice == null) {
            return false;
        }
        for (UsbFilter filter : mUsbHolder.filters) {
            if (usbDevice.getProductId() == filter.getProductId() && usbDevice.getVendorId() == filter.getVendorId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注册 USB 广播
     *
     * @return 若成功则返回 true
     */
    protected boolean registerReceiver(@NonNull Context context) {
        mUsbManager = (UsbManager) context.getApplicationContext().getSystemService(Context.USB_SERVICE);
        Log.d(TAG, "mUsbManager: " + mUsbManager);
        boolean b = initDeviceFilter(context);
        if (!b || mUsbManager == null) {
            Log.e(TAG, "register receiver failure !!");
            Log.e(TAG, "UsbManager: " + mUsbManager);
            Log.e(TAG, "init device filter: " + b);
            return false;
        }
        IntentFilter filter = new IntentFilter(USBReceiver.ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        context.getApplicationContext().registerReceiver(this, filter);
        // 注册完成后进行连接
        for (UsbDevice device : mUsbManager.getDeviceList().values()) {
            onDeviceAttached(context, device);
        }
        return true;
    }

    /**
     * 初始化 usb 设备过滤配置<br>
     * 配置为：<br>
     * Application 中的 key 为 "android.hardware.usb.action.USB_DEVICE_ATTACHED" 的 meta-data 的 resource
     *
     * @return 若初始化成功则返回 true
     */
    public boolean initDeviceFilter(Context context) {
        if (!mUsbHolder.filters.isEmpty()) {
            return true;
        }
        try {
            // 获取 Application 中的 key 为 "android.hardware.usb.action.USB_DEVICE_ATTACHED" 的 meta-data 的 resource
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            int device_filter = metaData.getInt("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            try (XmlResourceParser xml = context.getResources().getXml(device_filter)) {
                int eventType;
                // 读取这个 xml 文件
                while ((eventType = xml.next()) != XmlResourceParser.END_DOCUMENT) {
                    String name = xml.getName();
                    // 如果标签是开头并且是 usb-device 则获取该标签下的 4  个值
                    if (name != null && eventType == XmlPullParser.START_TAG && name.equals("usb-device")) {
                        Log.d(TAG, "getAttributeName-0: " + xml.getAttributeName(2));
                        Log.d(TAG, "getAttributeName-1: " + xml.getAttributeName(3));
                        Log.d(TAG, "getAttributeName-2: " + xml.getAttributeName(0));
                        Log.d(TAG, "getAttributeName-3: " + xml.getAttributeName(1));
                        String product_id = xml.getAttributeValue(2);
                        String vendor_id = xml.getAttributeValue(3);
                        String interface_id = xml.getAttributeValue(0);
                        String interface_subclass = xml.getAttributeValue(1);
                        Log.d(TAG, "product-id: " + product_id);
                        Log.d(TAG, "vendor-id: " + vendor_id);
                        Log.d(TAG, "interface-id: " + interface_id);
                        Log.d(TAG, "interface-subclass: " + interface_subclass);
                        mUsbHolder.filters.add(
                                new UsbFilter(
                                        Integer.decode(product_id), Integer.decode(vendor_id),
                                        Integer.decode(interface_id), Integer.decode(interface_subclass)
                                )
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
