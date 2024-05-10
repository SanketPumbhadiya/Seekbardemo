package com.example.seekbardemoimagedemo;

public class ModelStoreData {

    boolean isShowRowGridImage;
    int showColumnGridProgress;

    public boolean isShowRowGridImage() {
        return isShowRowGridImage;
    }

    public int getShowColumnGridProgress() {
        return showColumnGridProgress;
    }

    public void setShowColumnGridProgress(int showColumnGridProgress) {
        this.showColumnGridProgress = showColumnGridProgress;
    }

    public void setShowRowGridImage(boolean showRowGridImage) {
        isShowRowGridImage = showRowGridImage;
    }
}