package com.jch.aidlserver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ACER on 2014/12/6.
 */
public class Location implements Parcelable {

    private String latitude;
    private String longitude;
    private String addrStr;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.addrStr);
    }

    public Location() {
    }

    private Location(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.addrStr = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
