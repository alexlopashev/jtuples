package com.alexlopashev.tuples;

import com.alexlopashev.tuples.iterator.IntIntIterator;
import com.alexlopashev.tuples.procedure.IntIntTupleProcedure;

public interface IntIntTupleCollection {

    boolean add(int left, int right);

    boolean addAll(int[] left, int[] right);

    boolean addAll(IntIntTupleCollection collection);

    void clear();

    boolean contains(int left, int right);

    boolean containsAll(int[] left, int[] right);

    boolean containsAll(IntIntTupleCollection collection);

    boolean forEach(IntIntTupleProcedure procedure);

    boolean isEmpty();

    IntIntIterator iterator();

    int[] getLeft();

    int[] getRight();

    boolean remove(int left, int right);

    boolean removeAll(int[] left, int[] right);

    boolean removeAll(IntIntTupleCollection collection);

    boolean retainAll(int[] left, int[] right);

    boolean retainAll(IntIntTupleCollection collection);

    int size();

}
