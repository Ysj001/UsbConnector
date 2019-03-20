package com.ysj.usb.usbtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ysj.usb.usbconnector.IUsbOperate;
import com.ysj.usb.usbconnector.core.OnDevicePermissionListener;
import com.ysj.usb.usbconnector.core.OnDeviceStateListener;
import com.ysj.usb.usbconnector.core.USBHolder;
import com.ysj.usb.usbconnector.core.UsbAction;
import com.ysj.usb.usbconnector.core.UsbOperate;


public class MainActivity extends AppCompatActivity
        implements OnDeviceStateListener, OnDevicePermissionListener {

    TextView tv_filter;
    TextView tv_state;
    TextView tv_permission;
    TextView tv_info;
    Button btn_connect;
    Button btn_disconnnect;

    private IUsbOperate mOperate = new UsbOperate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_filter = (TextView) findViewById(R.id.tv_filter);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_permission = (TextView) findViewById(R.id.tv_permission);
        tv_info = (TextView) findViewById(R.id.tv_info);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_disconnnect = (Button) findViewById(R.id.btn_disconnect);
        // 跳转到发送测试页
        findViewById(R.id.btn_send_test).setOnClickListener(
                v -> startActivity(new Intent(this, SendTestActivity.class))
        );
        init();
    }

    private void init() {
        btn_connect.setOnClickListener(v -> mOperate.connected(this));
        btn_disconnnect.setOnClickListener(v -> mOperate.disConnect());
        mOperate.setOnDevicePermissionListener(this);
        mOperate.setOnDeviceStateListener(this);
        tv_filter.setText(
                GsonUtil.toJson(USBHolder.getInstance().filters)
                        .replace("[", "")
                        .replace("]", "")
                        .replace("{", "")
                        .replace("}", "====================")
        );
    }

    @Override
    public void onDevicePermission(boolean hasPermission) {
        tv_permission.setText("" + hasPermission);
    }

    @Override
    public void onDeviceState(String action) {
        tv_info.setText(GsonUtil.toJson(USBHolder.getInstance().getUsbDevice()));
        switch (action) {
            case UsbAction.ACTION_USB_DEVICE_ATTACHED:
                tv_state.setText("USB device 插入: " + action);
                break;
            case UsbAction.ACTION_USB_DEVICE_DETACHED:
                tv_state.setText("USB device 拔出: " + action);
                break;
            case UsbAction.ACTION_USB_NO_CONNECTED:
                tv_state.setText("USB device 没有建立连接: " + action);
                break;
            case UsbAction.ACTION_DEVICE_UNKNOWN:
                tv_state.setText("插入的设备未在 device_filter.xml 中找到: " + action);
                break;
            case UsbAction.ACTION_DEVICE_UNKNOWN_INTERFACE:
                tv_state.setText("device_filter.xml 中设置的 interface-id ，interface-subclass 未在 插入的设备的 interface 枚举中找到: " + action);
                break;
            case UsbAction.ACTION_USB_CONNECTED:
                tv_state.setText("USB device 建立连接成功: " + action);
                break;
        }
    }
}
