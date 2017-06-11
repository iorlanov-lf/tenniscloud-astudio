package com.logiforge.tenniscloud.model.util;

/**
 * Created by iorlanov on 6/1/17.
 */
public class Phone {
    static public boolean compareNumbers(String number1, String number2) {
        if(number1 == null && number2 == null) {
            return true;
        } else if (number1 == null || number2 == null) {
            return false;
        } else {
            String digits1 = number1.replaceAll("\\D+", "");
            String digits2 = number2.replaceAll("\\D+", "");
            return digits1.equals(digits2);
        }
    }

    public String number;
    public Integer type;

    public Phone(String number, Integer type) {
        this.number = number;
        this.type = type;
    }
}
