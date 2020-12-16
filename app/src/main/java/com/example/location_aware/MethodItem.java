package com.example.location_aware;

/**
 * Makes a new MethodItem object which can be put as an item in the combobox.
 */
public class MethodItem {
    private String methodName;
    private int methodImage;

    public MethodItem(String methodName, int methodImage){
        this.methodName = methodName;
        this.methodImage = methodImage;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getMethodImage() {
        return methodImage;
    }

    public void setMethodImage(int methodImage) {
        this.methodImage = methodImage;
    }
}
