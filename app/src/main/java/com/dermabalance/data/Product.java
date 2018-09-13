package com.dermabalance.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dermabalance.utils.ConvertUtils;

import java.io.Serializable;

@Entity(tableName = "products")
public class Product implements Serializable, Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "linea")
    private String linea;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "lastPrice")
    private double lastPrice;

    @ColumnInfo(name = "changed")
    private int changed;

    private double difference;

    public Product() {

    }

    public Product(final String linea, final String barcode, final String description, final String price, final int changed) {
        setLinea(linea);
        setBarcode(barcode);
        setDescription(description);
        setPrice(ConvertUtils.getDouble(price));
        setChanged(changed);
    }

    protected Product(final Parcel in) {
        barcode = in.readString();
        linea = in.readString();
        description = in.readString();
        price = in.readDouble();
        lastPrice = in.readDouble();
        changed = in.readInt();
        difference = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(barcode);
        dest.writeString(linea);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(changed);
        dest.writeDouble(lastPrice);
    }
}
