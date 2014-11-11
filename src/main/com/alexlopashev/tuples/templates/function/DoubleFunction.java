package com.alexlopashev.tuples.templates.function;

public interface DoubleFunction<L, R> {

    L executeLeft(L value);

    R executeRight(R value);

}
