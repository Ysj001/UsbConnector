package com.ysj.usb.usbtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ysj.usb.usbconnector.IUsbOperate;
import com.ysj.usb.usbconnector.core.OnDevicePermissionListener;
import com.ysj.usb.usbconnector.core.OnDeviceStateListener;
import com.ysj.usb.usbconnector.core.USBHolder;
import com.ysj.usb.usbconnector.core.UsbAction;
import com.ysj.usb.usbconnector.core.UsbOperate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnDeviceStateListener, OnDevicePermissionListener
{


    @BindView(R.id.tv_filter)
    TextView tv_filter;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_permission)
    TextView tv_permission;
    @BindView(R.id.tv_info)
    TextView tv_info;

    private IUsbOperate mOperate = new UsbOperate();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void init()
    {
        mOperate.setOnDevicePermissionListener(this);
        mOperate.setOnDeviceStateListener(this);
        tv_filter.setText(
                GsonUtil.toJson(USBHolder.getInstance().filters)
                        .replace("[", "")
                        .replace("]", "")
                        .replace("{", "")
                        .replace("}", "====================")
        );
        tv_info.setText(GsonUtil.toJson(USBHolder.getInstance().getUsbDevice()));
    }

    @OnClick(R.id.btn_connect)
    public void onConnectClicked()
    {
        mOperate.connected(this);
    }

    @OnClick(R.id.btn_disconnect)
    public void onDisConnectClicked()
    {
        mOperate.disConnect();
    }

    @Override
    public void onDevicePermission(boolean hasPermission)
    {
        tv_permission.setText("" + hasPermission);
    }

    @Override
    public void onDeviceState(String action)
    {
        tv_state.setText(action);
        switch (action)
        {
            case UsbAction.ACTION_USB_CONNECTED:
                tv_info.setText(GsonUtil.toJson(USBHolder.getInstance().getUsbDevice()));
                break;
        }
    }
}
