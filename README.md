
先上文档 : [Android Usb 官方文档](https://developer.android.google.cn/reference/android/hardware/usb/package-summary?hl=zh-cn)

简介：由于安卓连接 USB device 颇为繁琐，因此本着不重复造轮子的原则写了一个 UsbConnector 库。运用该库可省略 USB 状态监听及 USB 连接权限的处理，三步即可轻松实现安卓连接 USB device ，以及各种状态的监听的功能！
 
# 一.基本配置
## 1.导入 USBConnector 库
### Step 1. Add the JitPack repository to your build file .
Add it in your root build.gradle at the end of repositories:
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. Add the dependency
```java
dependencies {
	        implementation 'com.github.Ysj001:UsbConnector:v1.0.1'
	}
```
## 2.创建 Usb Device 过滤文件
 - 在 res 目录下建立一个 xml 文件夹，并在其中建立一个 device_filter.xml 文件内容如下所示
 - usb-device 可设置多个，用于过滤多个连接的设备
 - product-id 和 vendor-id 为必须设置的参数，用于后面记住 usb 设备的连接权限
 - interface-id 和 interface-subclass 则用于自动过滤用于打开连接的设备的连接接口（UsbInterface），若设置正确则库会自动初始化 UsbDeviceConnection

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:android="http://schemas.android.com/apk/res/android">
    <usb-device product-id="65535" vendor-id="2663" interface-id="3" interface-subclass="0" />
    <usb-device product-id="111111" vendor-id="1212" interface-id="13" interface-subclass="10" />
    <usb-device product-id="222" vendor-id="1212" interface-id="31" interface-subclass="01" />
</resources>
```

# 二.初始化连接器 / 释放连接器
- 在任意位置都可调用 UsbConnector.init(context) 来对该库进行初始化，如下为在 Application 的 onCreate() 函数中初始化

- 在任意位置都可调用 UsbConnector.release(context) 来释放该库，如下为在 Application 的 onTerminate() 函数中释放
```java
public class App extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        // 初始化连接器
        UsbConnector.init(this);

    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        // 释放连接器
        UsbConnector.release(this);
    }

}
```
以上步骤完成后
# 三.对 USB device 进行操作
## 初始化 UsbOperate
- 在对 usb device 进行操作前要先初始化 UsbOperate 
```java
IUsbOperate mOperate = new UsbOperate();
```
## 监听 usb device 的状态
### 确定设备的基本状态：
- 是否插入
- 插入的设备是否是过滤文件中的设备
- 是否已经连接上

以上状态均可用一下方法监听：
```java
mOperate.setOnDeviceStateListener(new OnDeviceStateListener() {
            @Override
            public void onDeviceState(String action) {
                
            }
        });
```
### 确定是否具有 usb 连接权限
可在以下方法中监听：
```java
mOperate.setOnDevicePermissionListener(new OnDevicePermissionListener() {
            @Override
            public void onDevicePermission(boolean hasPermission) {
                
            }
        });
```
## 连接 USB device
- 在完成上述几个步骤后只需要如下步骤就可以连接 usb device 啦
```java
mOperate.connected(this);
```
## 其他操作
- 若想获取一下内容均可在 USBHolder. getInstance() 中

```java 
    // 用于过滤设备的 filters
    List<UsbFilter> filters = new ArrayList<>();
    // usb 的广播接收器
    USBReceiver usbReceiver;
    // usb 设备
    UsbDevice usbDevice;
    // 该设备的连接接口
    UsbInterface usbInterface;
    // 设备的连接
    UsbDeviceConnection usbDeviceConnection;
    // 输入端点
    UsbEndpoint endpointIn;
    // 输出端点
    UsbEndpoint endpointOut;
```


我的博客 : [Android 轻松连接 Usb Device —— UsbConnector](https://blog.csdn.net/qq_35365635/article/details/86743451)
