package com.ysj.usb.usbtest;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.ysj.usb.usbconnector.core.UsbConnector;

/**
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/21 13:42
 */
public class App extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        // logger
        Logger.addLogAdapter(new AndroidLogAdapter());

        // 初始化库
        UsbConnector.init(this);

    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        UsbConnector.release(this);
    }

}
