package com.pashkov.sexcubefull.view_pager;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class CarouselConfig implements Parcelable{
    public static final int LOOPS = 1000;
    public static final float DEFAULT_BIG_SCALE = 1.0f;
    public static final float DEFAULT_SMALL_SCALE = 0.7f;

    public float bigScale = DEFAULT_BIG_SCALE;
    public float smallScale = DEFAULT_SMALL_SCALE;

    public static final int HORIZONTAL = 1;

    public static final int SCROLL_MODE_BIG_CURRENT = 0;

    public int orientation = HORIZONTAL;
    public boolean infinite = true;
    public int scrollScalingMode = SCROLL_MODE_BIG_CURRENT;

    public int pageMargin = 0;
    public int pageLimit = 5;
    public int pagerId = View.NO_ID;

    private static CarouselConfig sConfig;

    public static CarouselConfig getInstance() {
        if (sConfig == null) {
            sConfig = new CarouselConfig();
        }
        return sConfig;
    }

    private CarouselConfig() {}


    private CarouselConfig(Parcel in) {
        orientation = in.readInt();
        infinite = in.readInt() == 1;
        scrollScalingMode = in.readInt();
        pageMargin = in.readInt();
        pageLimit = in.readInt();
        pagerId = in.readInt();
    }

    public static final Parcelable.Creator<CarouselConfig> CREATOR =
            new Parcelable.Creator<CarouselConfig>() {
                @Override
                public CarouselConfig createFromParcel(Parcel in) {
                    return new CarouselConfig(in);
                }

                @Override
                public CarouselConfig[] newArray(int size) {
                    return new CarouselConfig[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(orientation);
        out.writeInt(infinite ? 1 : 0);
        out.writeInt(scrollScalingMode);
        out.writeInt(pageMargin);
        out.writeInt(pageLimit);
        out.writeInt(pagerId);
    }

    public float getDiffScale() {
        return bigScale - smallScale;
    }

    public String getPageFragmentTag(int position) {
        return "android:switcher:" + pagerId + ":" + position;
    }

    @Override
    public String toString() {
        return "CarouselConfig{" +
                "bigScale=" + bigScale +
                ", smallScale=" + smallScale +
                ", orientation=" + orientation +
                ", infinite=" + infinite +
                ", scrollScalingMode=" + scrollScalingMode +
                ", pageMargin=" + pageMargin +
                ", pageLimit=" + pageLimit +
                ", pagerId=" + pagerId +
                '}';
    }
}