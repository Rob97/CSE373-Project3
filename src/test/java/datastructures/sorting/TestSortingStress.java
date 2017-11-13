package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {
		IList<Integer> list = new DoubleLinkedList<>();
		for(int i = 0; i < 1000000; i++) 
			list.add(i);
		IList<Integer> top = Searcher.topKSort(10000, list);
		assertEquals(50, top.size());
		for(int i = 0; i < top.size(); i++)
			assertEquals(990000 + i, top.get(i));
        assertTrue(true);
    }
}
