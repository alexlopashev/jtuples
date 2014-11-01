package com.alexlopashev.tuples;

import com.alexlopashev.tuples.function.IntIntTupleFunction;
import com.alexlopashev.tuples.iterator.IntIntIterator;
import com.alexlopashev.tuples.procedure.IntIntTupleProcedure;

/**
 * @author alexlopashev
 */
public class IntIntTupleList implements IntIntTupleCollection {

    private static final int DEFAULT_CAPACITY = 8;
    private static final double LOG_RATIO = StrictMath.log10(2);

    private int[] left;

    private int[] right;

    private int capacity;

    private int size;

    public IntIntTupleList() {
        this(DEFAULT_CAPACITY);
    }

    public IntIntTupleList(int initialCapacity) {
        initialize(nearestPowerOf2(initialCapacity));
    }

    public IntIntTupleList(int[] leftInit, int[] rightInit) {
        if (leftInit.length != rightInit.length) throw new IllegalArgumentException();
        size = leftInit.length;
        capacity = nearestPowerOf2(size);
        left    = new int[capacity];
        right   = new int[capacity];
        System.arraycopy(leftInit, 0, this.left, 0, size);
        System.arraycopy(rightInit, 0, this.right, 0, size);
    }

    @Override
    public boolean add(int left, int right) {
        if (size == Integer.MAX_VALUE) return false;
        extendBy(1);
        this.left[size]     = left;
        this.right[size]    = right;
        size++;
        return true;
    }

    @Override
    public boolean addAll(int[] leftAdd, int[] rightAdd) {
        if (leftAdd.length != rightAdd.length) throw new IllegalArgumentException();
        if (size >= Integer.MAX_VALUE - leftAdd.length) return false;
        extendBy(leftAdd.length);
        System.arraycopy(leftAdd, 0, this.left, size, leftAdd.length);
        System.arraycopy(rightAdd, 0, this.right, size, rightAdd.length);
        size += leftAdd.length;
        return true;
    }

    @Override
    public boolean addAll(IntIntTupleCollection collection) {
        return addAll(collection.getLeft(), collection.getRight());
    }

    @Override
    public void clear() {
        initialize(DEFAULT_CAPACITY);
    }

    @Override
    public boolean contains(int leftElement, int rightElement) {
        for (int i = 0; i < size; i++)
            if (left[i]  == leftElement && right[i] == rightElement)
                return true;
        return false;
    }

    @Override
    public boolean containsAll(int[] left, int[] right) {
        if (left == null || right == null) throw new NullPointerException();
        if (left.length != right.length) throw new IllegalArgumentException();
        for (int i = 0; i < left.length; i++)
            if (!contains(left[i], right[i]))
                return false;
        return true;
    }

    @Override
    public boolean containsAll(IntIntTupleCollection collection) {
        return containsAll(collection.getLeft(), collection.getRight());
    }

    public boolean forEach(IntIntTupleProcedure procedure) {
        for (int i = 0; i < size; i++)
            if (!procedure.execute(left[i], right[i]))
                return false;
        return true;
    }

    public int indexOf(int leftElement, int rightElement) {
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
    public IntIntIterator iterator() {
        return null;
    }

    public int getLeft(int index) {
        return left[index];
    }

    @Override
    public int[] getLeft() {
        return left;
    }

    public int getRight(int index) {
        return right[index];
    }

    @Override
    public int[] getRight() {
        return right;
    }

    @Override
    public boolean remove(int leftElement, int rightElement) {
        boolean removed = false;
        int index;
        while ((index = indexOf(leftElement, rightElement)) >= 0) {
            for (int i = index + 1; i < size; i++) {
                left[i]     = left[i - 1];
                right[i]    = right[i - 1];
            }
            size--;
            removed |= true;
        }
        shrink();
        return removed;
    }

    @Override
    public boolean removeAll(int[] left, int[] right) {
        if (left == null || right == null) throw new NullPointerException();
        if (left.length != right.length) throw new IllegalArgumentException();
        boolean totallyRemoved = true;
        for (int i = 0; i < left.length; i++)
            if (!remove(left[i], right[i]))
                totallyRemoved = false;
        return totallyRemoved;
    }

    @Override
    public boolean removeAll(IntIntTupleCollection collection) {
        return removeAll(collection.getLeft(), collection.getRight());
    }

    @Override
    public boolean retainAll(int[] left, int[] right) {
        return false;
    }

    @Override
    public boolean retainAll(IntIntTupleCollection collection) {
        return false;
    }

    public void transformValues(IntIntTupleFunction function) {
        for (int i = 0; i < size; i++) {
            left[i]     = function.executeLeft(left[i]);
            right[i]    = function.executeRight(right[i]);
        }
    }

    @Override
    public int size() {
        return size;
    }

    public IntIntTupleList subList(int begin, int end) {
        int subSize = end - begin;
        int subCapacity = nearestPowerOf2(end - begin);
        int[] subLeft   = new int[subCapacity];
        int[] subRight  = new int[subCapacity];
        System.arraycopy(left, begin, subLeft, 0, subSize);
        System.arraycopy(right, begin, subRight, 0, subSize);
        return IntIntTupleList.initialize(subLeft, subRight, subCapacity, subSize);
    }

    private void initialize(int initialCapacity) {
        size = 0;
        capacity = initialCapacity;
        left    = new int[capacity];
        right   = new int[capacity];
    }

    private static IntIntTupleList initialize(int[] left, int[] right, int capacity, int size) {
        IntIntTupleList list = new IntIntTupleList();
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
        int[] newLeft   = new int[newCapacity];
        int[] newRight  = new int[newCapacity];
        System.arraycopy(left, 0, newLeft, 0, size);
        System.arraycopy(right, 0, newRight, 0, size);
        left    = newLeft;
        right   = newRight;
    }

    private int nearestPowerOf2(int capacity) {
        return Math.max(DEFAULT_CAPACITY, 1 << ((int) (StrictMath.log10(capacity) / LOG_RATIO) + 1));
    }

}
