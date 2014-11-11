package com.alexlopashev.tuples.templates;

import com.alexlopashev.tuples.templates.function.TripleFunction;
import com.alexlopashev.tuples.templates.iterator.TripleIterator;
import com.alexlopashev.tuples.templates.procedure.TripleProcedure;

public interface TripleCollection<L, M, R> extends TupleCollection {

    boolean add(L left, M middle,  R right);

    boolean addAll(L[] left, M[] middle, R[] right);

    boolean addAll(TripleCollection<L, M, R> collection);

    boolean contains(L left, M middle, R right);

    boolean containsAll(L[] left, M[] middle, R[] right);

    boolean containsAll(TripleCollection<L, M, R> collection);

    boolean forEach(TripleProcedure<L, M, R> procedure);

    TripleIterator<L, M, R> iterator();

    L[] getLeft();

    M[] getMiddle();

    R[] getRight();

    void transformValues(TripleFunction<L, M, R> function);

    boolean remove(L left, M middle, R right);

    boolean removeAll(L[] left, M[] middle, R[] right);

    boolean removeAll(TripleCollection<L, M, R> collection);

    boolean retainAll(L[] left, M[] middle, R[] right);

    boolean retainAll(TripleCollection<L, M, R> collection);

}
