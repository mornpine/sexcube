package com.pashkov.sexcubefull;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.pashkov.sexcubefull.view_pager.ForegroundLinearLayout;

public class MyPageItem implements Parcelable {
	 private String mTitle;
	    private int mLevelTitle;
	    private Drawable myImage;

	    public MyPageItem(String title, Drawable image, int levelTitle) {
	        mTitle = title;
	        myImage = image;
	        mLevelTitle = levelTitle;
	    }

	    private MyPageItem(Parcel in) {
	        mTitle = in.readString();
	    }

	    public String getTitle() {
	        return mTitle;
	    }

//TODO my methhods get set for PageItem
	    public Drawable getMyImage () {
	        return myImage;
	    }
	    public int getmLevelTitle () {
	        return mLevelTitle;
	    }

	    public void setMyImage (Drawable newMyImage) {
	        myImage = newMyImage;
	    }

	    public void setMyTitle (int levelTitle) {
	        mLevelTitle = levelTitle;
	    }

	    public static final Parcelable.Creator<MyPageItem> CREATOR =
	            new Parcelable.Creator<MyPageItem>() {
	                @Override
	                public MyPageItem createFromParcel(Parcel in) {
	                    return new MyPageItem(in);
	                }

	                @Override
	                public MyPageItem[] newArray(int size) {
	                    return new MyPageItem[size];
	                }
	            };

	    @Override
	    public int describeContents() {
	        return 0;
	    }

	    @Override
	    public void writeToParcel(Parcel out, int flags) {
	        out.writeString(mTitle);
	    }
	}