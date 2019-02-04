package com.ysj.usb.usbconnector.core;

/**
 * 定义 usb 过滤器类
 * <p>
 *
 * @author Ysj
 * Created time: 2019/1/21 11:04
 */
public class UsbFilter {
    private int productId;

    private int vendorId;

    private int interfaceId;

    private int interfaceSubclass;

    public UsbFilter(int productId, int vendorId) {
        this.productId = productId;
        this.vendorId = vendorId;
    }

    public UsbFilter(int productId, int vendorId, int interfaceId, int interfaceSubclass) {
        this.productId = productId;
        this.vendorId = vendorId;
        this.interfaceId = interfaceId;
        this.interfaceSubclass = interfaceSubclass;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public int getInterfaceSubclass() {
        return interfaceSubclass;
    }

    public void setInterfaceSubclass(int interfaceSubclass) {
        this.interfaceSubclass = interfaceSubclass;
    }
}
