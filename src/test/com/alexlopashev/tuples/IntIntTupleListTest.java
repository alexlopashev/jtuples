package com.alexlopashev.tuples;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author alexlopashev
 */
public class IntIntTupleListTest {

    @Test
    public void testEmptyElement() throws Exception {
        IntIntTupleList list = new IntIntTupleList();
        Assert.assertEquals(0, list.size());
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testSingleIlement() throws Exception {
        IntIntTupleList list = new IntIntTupleList();
        list.add(42, 257);
        Assert.assertEquals(1, list.size());
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(42, list.getLeft(0));
        Assert.assertEquals(257, list.getRight(0));
    }

    @Test
    public void testIndexOf() throws Exception {
        IntIntTupleList list = new IntIntTupleList();
        for (int i = 0; i < 10; i++)
            list.add(i, 100 * i);
        Assert.assertEquals(4, list.indexOf(4, 400));
    }

}
