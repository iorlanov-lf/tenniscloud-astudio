package com.logiforge.tenniscloud.model.util;

/**
 * Created by iorlanov on 6/10/17.
 */

public class CmpUtil {
    static public <T> boolean eq(T v1, T v2) {
        if(v1 == null && v2 != null) {
            return false;
        } else if(v1 != null && v2 == null) {
            return false;
        } else {
            return v1.equals(v2);
        }
    }
}
