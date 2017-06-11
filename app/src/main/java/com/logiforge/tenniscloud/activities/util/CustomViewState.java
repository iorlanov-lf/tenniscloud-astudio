package com.logiforge.tenniscloud.activities.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by iorlanov on 6/9/17.
 */

public class CustomViewState extends View.BaseSavedState {
    SparseArray childrenStates;

    CustomViewState(Parcelable superState) {
        super(superState);
    }

    private CustomViewState(Parcel in, ClassLoader classLoader) {
        super(in);
        childrenStates = in.readSparseArray(classLoader);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeSparseArray(childrenStates);
    }

    public static final ClassLoaderCreator<CustomViewState> CREATOR
            = new ClassLoaderCreator<CustomViewState>() {
        @Override
        public CustomViewState createFromParcel(Parcel source, ClassLoader loader) {
            return new CustomViewState(source, loader);
        }

        @Override
        public CustomViewState createFromParcel(Parcel source) {
            return createFromParcel(source, null);
        }

        @Override
        public CustomViewState[] newArray(int size) {
            return new CustomViewState[size];
        }
    };
}
