package com.bmts.heating.commons.utils.common;

/**
 * @ClassName Tuple
 * @Author naming
 * @Date 2020/11/26 10:39
 **/
public class Tuple<A,B> {
    public final A first;

    public final B second;

    public Tuple(A a, B b){
        first = a;
        second = b;
    }
}
