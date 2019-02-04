package com.ysj.usb.usbconnector.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ysj.usb.usbconnector.R;

/**
 * 用于 usb 权限请求的 activity ，该 activity 可以防止每次 usb 都要请求连接权限
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/18 11:55
 */
public class UsbPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_permission);
        finish();
    }

}
