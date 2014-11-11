package com.alexlopashev.tuples.templates.list;

import com.alexlopashev.tuples.templates.DoubleCollection;
import com.alexlopashev.tuples.templates.function.DoubleFunction;
import com.alexlopashev.tuples.templates.iterator.DoubleIterator;
import com.alexlopashev.tuples.templates.procedure.DoubleProcedure;

/**
 * This file is correct java class and also plays role of template for
 * generating double array list classes for pair of primitive (this is primary goal
 * but you it also can be used for generating <int, String> 2-tuple list.
 */
public class DoubleArrayList<L, R> implements DoubleList<L, R> {

    private static final int DEFAULT_CAPACITY = 8;
    private static final double LOG_RATIO = StrictMath.log10(2);

    private L[] left;

    private R[] right;

    private int capacity;

    private int size;

    public DoubleArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public DoubleArrayList(int initialCapacity) {
        initialize(0, nearestPowerOf2(initialCapacity));
    }

    public DoubleArrayList(L[] leftInit, R[] rightInit) {
        checkParameters(leftInit, rightInit);
        initialize(leftInit.length, nearestPowerOf2(size));
        System.arraycopy(leftInit, 0, this.left, 0, size);
        System.arraycopy(rightInit, 0, this.right, 0, size);
    }

    @Override
    public boolean add(L left, R right) {
        if (size == Integer.MAX_VALUE) return false;
        extendBy(1);
        this.left[size] = left;
        this.right[size] = right;
        size++;
        return true;
    }

    @Override
    public boolean addAll(L[] leftAdd, R[] rightAdd) {
        checkParameters(leftAdd, rightAdd);
        if (size >= Integer.MAX_VALUE - leftAdd.length) return false;
        extendBy(leftAdd.length);
        System.arraycopy(leftAdd, 0, this.left, size, leftAdd.length);
        System.arraycopy(rightAdd, 0, this.right, size, rightAdd.length);
        size += leftAdd.length;
        return true;
    }

    @Override
    public boolean addAll(DoubleCollection<L, R> collection) {
        return addAll(collection.getLeft(), collection.getRight());
    }

    @Override
    public void clear() {
        initialize(0, DEFAULT_CAPACITY);
    }

    @Override
    public boolean contains(L leftElement, R rightElement) {
        for (int i = 0; i < size; i++)
            if (left[i]  == leftElement && right[i] == rightElement)
                return true;
        return false;
    }

    @Override
    public boolean containsAll(L[] left, R[] right) {
        checkParameters(left, right);
        for (int i = 0; i < left.length; i++)
            if (!contains(left[i], right[i]))
                return false;
        return true;
    }

    @Override
    public boolean containsAll(DoubleCollection<L, R> collection) {
        return containsAll(collection.getLeft(), collection.getRight());
    }

    @Override
    public boolean forEach(DoubleProcedure<L, R> procedure) {
        for (int i = 0; i < size; i++)
            if (!procedure.execute(left[i], right[i]))
                return false;
        return true;
    }

    @Override
    public int indexOf(L leftElement, R rightElement) {
        for (int i = 0; i < size; i++)
            if (left[i] == leftElement && right[i] == rightElement)
                return i;
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public DoubleIterator<L, R> iterator() {
        return new Iterator();
    }

    @Override
    public L getLeft(int index) {
        return left[index];
    }

    @Override
    public L[] getLeft() {
        return left;
    }

    @Override
    public R getRight(int index) {
        return right[index];
    }

    @Override
    public R[] getRight() {
        return right;
    }

    @Override
    public boolean remove(int index) {
        if (index < 0) throw new IllegalArgumentException();
        if (index >= size) return false;
        for (int i = index + 1; i < size; i++) {
            left[i]     = left[i - 1];
            right[i]    = right[i - 1];
        }
        size--;
        return true;
    }

    @Override
    public boolean remove(L leftElement, R rightElement) {
        boolean removed = false;
        int index;
        while ((index = indexOf(leftElement, rightElement)) >= 0)
            removed |= remove(index);
        shrink();
        return removed;
    }

    @Override
    public boolean removeAll(L[] left, R[] right) {
        checkParameters(left, right);
        boolean totallyRemoved = true;
        for (int i = 0; i < left.length; i++)
            if (!remove(left[i], right[i]))
                totallyRemoved = false;
        return totallyRemoved;
    }

    @Override
    public boolean removeAll(DoubleCollection<L, R> collection) {
        return removeAll(collection.getLeft(), collection.getRight());
    }

    @Override
    public boolean retainAll(L[] leftToRetain, R[] rightToRetain) {
        checkParameters(leftToRetain, rightToRetain);
        DoubleIterator<L, R> it = iterator();
        boolean modified = false;
        while (it.hasNext()) {
            for (int i = 0; i < leftToRetain.length; i++)
                if (isSame(it.nextLeft(), leftToRetain[i], it.nextRight(), rightToRetain[i])) {
                    it.remove();
                    modified = true;
                }
            it.iterate();
        }
        return modified;
    }

    @Override
    public boolean retainAll(DoubleCollection<L, R> collection) {
        DoubleIterator<L, R> it = iterator();
        boolean modified = false;
        while (it.hasNext()) {
            if (!collection.contains(it.nextLeft(), it.nextRight())) {
                it.remove();
                modified = true;
            }
            it.iterate();
        }
        return modified;
    }

    @Override
    public void transformValues(DoubleFunction<L, R> function) {
        for (int i = 0; i < size; i++) {
            left[i]     = function.executeLeft(left[i]);
            right[i]    = function.executeRight(right[i]);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public DoubleArrayList<L, R> subList(int begin, int end) {
        int subSize = end - begin;
        int subCapacity = nearestPowerOf2(end - begin);
        L[] subLeft   = initLeft(capacity);
        R[] subRight  = initRight(capacity);
        System.arraycopy(left, begin, subLeft, 0, subSize);
        System.arraycopy(right, begin, subRight, 0, subSize);
        return initialize(subLeft, subRight, subCapacity, subSize);
    }

    private void initialize(int initialSize, int initialCapacity) {
        size = initialSize;
        capacity = initialCapacity;
        left    = initLeft(capacity);
        right   = initRight(capacity);
    }

    private DoubleArrayList<L, R> initialize(L[] left, R[] right, int capacity, int size) {
        checkParameters(left, right);
        DoubleArrayList<L, R> list = new DoubleArrayList<L, R>();
        list.size = size;
        list.capacity = capacity;
        list.left = left;
        list.right = right;
        return list;
    }

    private void extendBy(int offset) {
        if (size + offset > capacity)
            resize(size + offset);
    }

    private void shrink() {
        if (size < capacity >> 2)
            resize(capacity >> 1);
    }

    private void resize(int newCapacity) {
        newCapacity = nearestPowerOf2(newCapacity);
        L[] newLeft   = initLeft(newCapacity);
        R[] newRight  = initRight(newCapacity);
        System.arraycopy(left, 0, newLeft, 0, size);
        System.arraycopy(right, 0, newRight, 0, size);
        left    = newLeft;
        right   = newRight;
    }

    private int nearestPowerOf2(int capacity) {
        return Math.max(DEFAULT_CAPACITY, 1 << ((int) (StrictMath.log10(capacity) / LOG_RATIO) + 1));
    }

    private L[] initLeft(int capacity) {
        return (L[]) new Object[capacity];
    }

    private R[] initRight(int capacity) {
        return (R[]) new Object[capacity];
    }

    private boolean isSame(L l1, L l2, R r1, R r2) {
        return l1.equals(l2) && r1.equals(r2);
    }

    private void checkParameters(L[] left, R[] right) {
        if (left == null || right == null) throw new NullPointerException();
        if (left.length != right.length) throw new IllegalArgumentException();
    }

    private class Iterator implements DoubleIterator<L, R> {

        private int current;

        private Iterator() {
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public void iterate() {
            current++;
        }

        @Override
        public boolean remove() {
            return DoubleArrayList.this.remove(current);
        }

        @Override
        public L nextLeft() {
            return left[current];
        }

        @Override
        public R nextRight() {
            return right[current];
        }
    }
}
