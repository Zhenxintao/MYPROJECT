package com.bmts.heating.commons.utils.common;

/**
 * @author naming
 * @date 2020/12/30 17:41
 **/
public class Tuple3<A, B, C> {
    public final A first;

    public final B second;
    public final C three;

    public Tuple3(A a, B b, C c) {
        first = a;
        second = b;
        three = c;
    }
}