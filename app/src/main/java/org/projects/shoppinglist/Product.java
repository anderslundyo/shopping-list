package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anders on 23-11-2017.
 */

public class Product implements Parcelable {

    String name;
    int quantity;

    public Product() {} //Empty constructor we will need later!

    public Product(String name, int quantity)
    {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name+" "+quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(quantity);
    }

    // "De-parcel object
    public Product(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
