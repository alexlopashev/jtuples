package com.alexlopashev.tuples.templates.list;

import com.alexlopashev.tuples.templates.TripleCollection;
import com.alexlopashev.tuples.templates.function.TripleFunction;
import com.alexlopashev.tuples.templates.iterator.TripleIterator;
import com.alexlopashev.tuples.templates.procedure.TripleProcedure;

public class TripleArrayList<L, M, R> implements TripleList<L, M, R> {

    private static final int DEFAULT_CAPACITY = 8;
    private static final double LOG_RATIO = StrictMath.log10(2);

    private L[] left;

    private M[] middle;

    private R[] right;

    private int capacity;

    private int size;

    public TripleArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public TripleArrayList(int initialCapacity) {
        initialize(0, nearestPowerOf2(initialCapacity));
    }

    public TripleArrayList(L[] leftInit, M[] middleInit, R[] rightInit) {
        checkParameters(leftInit, middleInit, rightInit);
        initialize(leftInit.length, nearestPowerOf2(size));
        System.arraycopy(leftInit, 0, this.left, 0, size);
        System.arraycopy(middleInit, 0, this.middle, 0, size);
        System.arraycopy(rightInit, 0, this.right, 0, size);
    }

    @Override
    public boolean add(L left, M middle, R right) {
        if (size == Integer.MAX_VALUE) return false;
        extendBy(1);
        this.left[size] = left;
        this.middle[size] = middle;
        this.right[size] = right;
        size++;
        return true;
    }

    @Override
    public boolean addAll(L[] leftAdd, M[] middleAdd, R[] rightAdd) {
        checkParameters(leftAdd, middleAdd, rightAdd);
        if (size >= Integer.MAX_VALUE - leftAdd.length) return false;
        extendBy(leftAdd.length);
        System.arraycopy(leftAdd, 0, this.left, size, leftAdd.length);
        System.arraycopy(middleAdd, 0, this.middle, size, middleAdd.length);
        System.arraycopy(rightAdd, 0, this.right, size, rightAdd.length);
        size += leftAdd.length;
        return true;
    }

    @Override
    public boolean addAll(TripleCollection<L, M, R> collection) {
        return addAll(collection.getLeft(), collection.getMiddle(), collection.getRight());
    }

    @Override
    public void clear() {
        initialize(0, DEFAULT_CAPACITY);
    }

    @Override
    public boolean contains(L leftElement, M middleElement, R rightElement) {
        for (int i = 0; i < size; i++)
            if (isSame(left[i], leftElement, middle[i], middleElement, right[i], rightElement))
                return true;
        return false;
    }

    @Override
    public boolean containsAll(L[] left, M[] middle, R[] right) {
        checkParameters(left, middle, right);
        for (int i = 0; i < left.length; i++)
            if (!contains(left[i], middle[i], right[i]))
                return false;
        return true;
    }

    @Override
    public boolean containsAll(TripleCollection<L, M, R> collection) {
        return containsAll(collection.getLeft(), collection.getMiddle(), collection.getRight());
    }

    @Override
    public boolean forEach(TripleProcedure<L, M, R> procedure) {
        for (int i = 0; i < size; i++)
            if (!procedure.execute(left[i], middle[i], right[i]))
                return false;
        return true;
    }

    @Override
    public int indexOf(L leftElement, M middleElement, R rightElement) {
        for (int i = 0; i < size; i++)
            if (isSame(left[i], leftElement, middle[i], middleElement, right[i], rightElement))
                return i;
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public TripleIterator<L, M, R> iterator() {
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
    public M getMiddle(int index) {
        return middle[index];
    }

    @Override
    public M[] getMiddle() {
        return middle;
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
            middle[i]   = middle[i - 1];
            right[i]    = right[i - 1];
        }
        size--;
        return true;
    }

    @Override
    public boolean remove(L leftElement, M middleElement, R rightElement) {
        boolean removed = false;
        int index;
        while ((index = indexOf(leftElement, middleElement, rightElement)) >= 0)
            removed |= remove(index);
        shrink();
        return removed;
    }

    @Override
    public boolean removeAll(L[] left, M[] middle, R[] right) {
        checkParameters(left, middle, right);
        boolean totallyRemoved = true;
        for (int i = 0; i < left.length; i++)
            if (!remove(left[i], middle[i], right[i]))
                totallyRemoved = false;
        return totallyRemoved;
    }

    @Override
    public boolean removeAll(TripleCollection<L, M, R> collection) {
        return removeAll(collection.getLeft(), collection.getMiddle(), collection.getRight());
    }

    @Override
    public boolean retainAll(L[] leftToRetain, M[] middleToRetain, R[] rightToRetain) {
        checkParameters(leftToRetain, middleToRetain, rightToRetain);
        TripleIterator<L, M, R> it = iterator();
        boolean modified = false;
        while (it.hasNext()) {
            for (int i = 0; i < leftToRetain.length; i++)
                if (isSame(it.nextLeft(), leftToRetain[i], it.nextMiddle(), middleToRetain[i], it.nextRight(), rightToRetain[i])) {
                    it.remove();
                    modified = true;
                }
            it.iterate();
        }
        return modified;
    }

    @Override
    public boolean retainAll(TripleCollection<L, M, R> collection) {
        TripleIterator<L, M, R> it = iterator();
        boolean modified = false;
        while (it.hasNext()) {
            if (!collection.contains(it.nextLeft(), it.nextMiddle(), it.nextRight())) {
                it.remove();
                modified = true;
            }
            it.iterate();
        }
        return modified;
    }

    @Override
    public void transformValues(TripleFunction<L, M, R> function) {
        for (int i = 0; i < size; i++) {
            left[i]     = function.executeLeft(left[i]);
            middle[i]   = function.executeMiddle(middle[i]);
            right[i]    = function.executeRight(right[i]);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public TripleArrayList<L, M, R> subList(int begin, int end) {
        int subSize = end - begin;
        int subCapacity = nearestPowerOf2(end - begin);
        L[] subLeft   = initLeft(capacity);
        M[] subMiddle = initMiddle(capacity);
        R[] subRight  = initRight(capacity);
        System.arraycopy(left, begin, subLeft, 0, subSize);
        System.arraycopy(middle, begin, subMiddle, 0, subSize);
        System.arraycopy(right, begin, subRight, 0, subSize);
        return initialize(subLeft, subMiddle, subRight, subCapacity, subSize);
    }

    private void initialize(int initialSize, int initialCapacity) {
        size = initialSize;
        capacity = initialCapacity;
        left    = initLeft(capacity);
        middle  = initMiddle(capacity);
        right   = initRight(capacity);
    }

    private TripleArrayList<L, M, R> initialize(L[] left, M[] middle, R[] right, int capacity, int size) {
        checkParameters(left, middle, right);
        TripleArrayList<L, M, R> list = new TripleArrayList<L, M, R>();
        list.size = size;
        list.capacity = capacity;
        list.left = left;
        list.middle = middle;
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
        M[] newMiddle = initMiddle(newCapacity);
        R[] newRight  = initRight(newCapacity);
        System.arraycopy(left, 0, newLeft, 0, size);
        System.arraycopy(middle, 0, newMiddle, 0, size);
        System.arraycopy(right, 0, newRight, 0, size);
        left    = newLeft;
        middle  = newMiddle;
        right   = newRight;
    }

    private int nearestPowerOf2(int capacity) {
        return Math.max(DEFAULT_CAPACITY, 1 << ((int) (StrictMath.log10(capacity) / LOG_RATIO) + 1));
    }

    private L[] initLeft(int capacity) {
        return (L[]) new Object[capacity];
    }

    private M[] initMiddle(int capacity) {
        return (M[]) new Object[capacity];
    }

    private R[] initRight(int capacity) {
        return (R[]) new Object[capacity];
    }

    private boolean isSame(L l1, L l2, M m1, M m2, R r1, R r2) {
        return l1.equals(l2) && m1.equals(m2) && r1.equals(r2);
    }

    private void checkParameters(L[] left, M[] middle, R[] right) {
        if (left == null || middle == null || right == null) throw new NullPointerException();
        if (left.length != middle.length || middle.length != right.length) throw new IllegalArgumentException();
    }

    private class Iterator implements TripleIterator<L, M, R> {

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
            return TripleArrayList.this.remove(current);
        }

        @Override
        public L nextLeft() {
            return left[current];
        }

        @Override
        public M nextMiddle() {
            return middle[current];
        }

        @Override
        public R nextRight() {
            return right[current];
        }
    }
}
