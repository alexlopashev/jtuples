package com.alexlopashev.tuples.templates.list;

import com.alexlopashev.tuples.templates.DoubleCollection;

public interface DoubleList<L, R> extends DoubleCollection<L, R> {

    int indexOf(L leftElement, R rightElement);

    L getLeft(int index);

    R getRight(int index);

    boolean remove(int index);

    DoubleList<L, R> subList(int begin, int end);

}
