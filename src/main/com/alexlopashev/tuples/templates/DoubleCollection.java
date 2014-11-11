package com.alexlopashev.tuples.templates;

import com.alexlopashev.tuples.templates.function.DoubleFunction;
import com.alexlopashev.tuples.templates.iterator.DoubleIterator;
import com.alexlopashev.tuples.templates.procedure.DoubleProcedure;

public interface DoubleCollection<L, R> extends TupleCollection {

    boolean add(L left, R right);

    boolean addAll(L[] left, R[] right);

    boolean addAll(DoubleCollection<L, R> collection);

    boolean contains(L left, R right);

    boolean containsAll(L[] left, R[] right);

    boolean containsAll(DoubleCollection<L, R> collection);

    boolean forEach(DoubleProcedure<L, R> procedure);

    DoubleIterator<L, R> iterator();

    L[] getLeft();

    R[] getRight();

    void transformValues(DoubleFunction<L, R> function);

    boolean remove(L left, R right);

    boolean removeAll(L[] left, R[] right);

    boolean removeAll(DoubleCollection<L, R> collection);

    boolean retainAll(L[] left, R[] right);

    boolean retainAll(DoubleCollection<L, R> collection);

}
