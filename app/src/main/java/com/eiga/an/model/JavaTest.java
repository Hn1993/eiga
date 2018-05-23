package com.eiga.an.model;

import com.eiga.an.utils.PhoneUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2018/5/4.
 */

public class JavaTest {
    private static List<String> data=new ArrayList<>();
    public static void main(String[] args) {
        System.out.println(PhoneUtils.get2Decimal(17.777777777777777777777));
        for (int i = 0; i < 4; i++) {
            data.add(i+"");
        }

        data.set(2,"222");
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
        }
    }
}
