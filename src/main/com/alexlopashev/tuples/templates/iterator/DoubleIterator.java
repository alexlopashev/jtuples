package com.alexlopashev.tuples.templates.iterator;

public interface DoubleIterator<L, R> extends TupleIterator {

    L nextLeft();

    R nextRight();

}
