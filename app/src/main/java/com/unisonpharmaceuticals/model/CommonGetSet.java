package com.unisonpharmaceuticals.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tushar Vataliya on 4/27/2018.
 */

public class CommonGetSet implements Parcelable,Serializable
{
    String id = "";
    String text = "";
    String image = "";
    Drawable drawable;
    String description = "";
    boolean isSelected = false;
    int notifCounts = 0;
    boolean isNotifDisplay = false;


    public CommonGetSet(){}

    protected CommonGetSet(Parcel in) {
        id = in.readString();
        text = in.readString();
        image = in.readString();
        description = in.readString();
        isSelected = in.readByte() != 0;
        notifCounts = in.readInt();
        isNotifDisplay = in.readByte() != 0;
    }

    public static final Creator<CommonGetSet> CREATOR = new Creator<CommonGetSet>() {
        @Override
        public CommonGetSet createFromParcel(Parcel in) {
            return new CommonGetSet(in);
        }

        @Override
        public CommonGetSet[] newArray(int size) {
            return new CommonGetSet[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getNotifCounts() {
        return notifCounts;
    }

    public void setNotifCounts(int notifCounts) {
        this.notifCounts = notifCounts;
    }

    public boolean isNotifDisplay() {
        return isNotifDisplay;
    }

    public void setNotifDisplay(boolean notifDisplay) {
        isNotifDisplay = notifDisplay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeString(image);
        dest.writeString(description);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(notifCounts);
        dest.writeByte((byte) (isNotifDisplay ? 1 : 0));
    }
}
