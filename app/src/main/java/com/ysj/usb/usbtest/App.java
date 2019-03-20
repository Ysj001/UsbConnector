package com.ysj.usb.usbtest;

import android.app.Application;
import android.widget.Toast;

import com.ysj.usb.usbconnector.core.UsbConnector;

/**
 * <p>
 *
 * @author Ysj
 * Create time: 2019/3/20 11:06
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UsbConnector.setDoOnMyDeviceAttached(this,
                () -> Toast.makeText(this, "DoOnMyDeviceAttached !", Toast.LENGTH_SHORT).show());
        UsbConnector.setDoOnDeviceHasPermission(
                () -> Toast.makeText(this, "DoOnDeviceHasPermission !", Toast.LENGTH_SHORT).show());
        UsbConnector.setDoOnDeviceNoPermission(
                () -> Toast.makeText(this, "DoOnDeviceNoPermission !", Toast.LENGTH_SHORT).show());
        UsbConnector.register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        UsbConnector.unregister(this);
    }
}
