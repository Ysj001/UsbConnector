package com.ysj.usb.usbtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ysj.usb.usbconnector.core.USBHolder;

public class SendTestActivity extends AppCompatActivity {

    ScrollView sv;
    TextView tv;
    EditText edt;

    USBHolder mUsbHolder = USBHolder.getInstance();

    // 用于发送数据的包
    byte[] outPackage = new byte[mUsbHolder.getEndpointOut() == null ? 0 : mUsbHolder.getEndpointOut().getMaxPacketSize()];
    // 用于接受数据的包
    byte[] inPackage = new byte[mUsbHolder.getEndpointIn() == null ? 0 : mUsbHolder.getEndpointIn().getMaxPacketSize()];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_test);
        sv = findViewById(R.id.sv);
        tv = findViewById(R.id.tv);
        edt = findViewById(R.id.edt);
        sendClick();
    }


    private void sendClick() {
        findViewById(R.id.btn).setOnClickListener(v -> {
            if (!mUsbHolder.isConnected()) {
                tv.setText("usb device not connected !");
                return;
            }
            String content = edt.getText().toString().replace(" ", "");
            Log.d("sendClick: ", content);
            if (TextUtils.isEmpty(content)) {
                return;
            }
            new Thread(() -> {
                for (int i = 0; i < outPackage.length; i++) {
                    outPackage[i] = inPackage[i] = 0;
                }
                String[] split = content.split(",");
                for (int i = 0; i < split.length; i++) {
                    outPackage[i] = Byte.decode(split[i]);
                }
                int outLength = mUsbHolder.getUsbDeviceConnection().bulkTransfer(mUsbHolder.getEndpointOut(), outPackage, outPackage.length, 500);
                int inLength = mUsbHolder.getUsbDeviceConnection().bulkTransfer(mUsbHolder.getEndpointIn(), inPackage, inPackage.length, 500);
                runOnUiThread(() -> {
                    tv.setText(getText(outLength, inLength));
                    sv.fullScroll(View.FOCUS_DOWN);
                });
            }).start();

        });
    }

    private String getText(int outLength, int inLength) {
        return tv.getText().toString() +
                "\n==============================" +
                "\nout length: " + outLength +
                "\nout package:\n" + arr2str(outPackage) +
                "\nin length: " + inLength +
                "\nout package:\n" + arr2str(inPackage);
    }

    private String arr2str(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String s = Integer.toHexString(b);
            if (s.length() == 1)
                sb.append("0");
            sb.append(s).append(",");
        }
        return sb.toString();
    }
}
