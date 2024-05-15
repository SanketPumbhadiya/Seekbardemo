package com.example.seekbardemoimagedemo;

public class ModelStoreData {

    boolean isShowRowGridImage;
    int showColumnGridProgress;
    String selectedCountryText;

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

    public String getSelectedCountryText() {
        return selectedCountryText;
    }

    public void setSelectedCountryText(String selectedCountryText) {
        this.selectedCountryText = selectedCountryText;
    }
}