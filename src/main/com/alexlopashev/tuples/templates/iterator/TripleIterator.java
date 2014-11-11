package com.alexlopashev.tuples.templates.iterator;

public interface TripleIterator<L, M, R> extends TupleIterator {

    L nextLeft();

    M nextMiddle();

    R nextRight();

}
