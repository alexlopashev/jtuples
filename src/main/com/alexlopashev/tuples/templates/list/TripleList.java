package com.alexlopashev.tuples.templates.list;

import com.alexlopashev.tuples.templates.TripleCollection;

public interface TripleList<L, M, R> extends TripleCollection<L, M, R> {

    int indexOf(L leftElement, M middleElement, R rightElement);

    L getLeft(int index);

    M getMiddle(int index);

    R getRight(int index);

    boolean remove(int index);

    TripleList<L, M, R> subList(int begin, int end);

}
