package com.alexlopashev.tuples.templates.iterator;

/**
 * iterating will be ugly, right:
 * while (it.hasNext()) {
 *     T1 t1 = it.nextTth();
 *     ...
 *     it.iterate();
 * }
 */
public interface TupleIterator {

    boolean hasNext();

    void iterate();

    boolean remove();

}
