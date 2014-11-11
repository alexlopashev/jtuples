package com.alexlopashev.tuples.templates.function;

public interface TripleFunction<L, M, R> {

    L executeLeft(L value);

    M executeMiddle(M value);

    R executeRight(R value);

}