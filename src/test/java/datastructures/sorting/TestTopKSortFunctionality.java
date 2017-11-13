package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    public void testSmallData() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for(int i = 0; i < 5; i++) 
    			list.add(i);
    		IList<Integer> top = Searcher.topKSort(10, list);
    		assertEquals(5, top.size());
    		for(int i = 0; i < top.size(); i++)
    			assertEquals(i, top.get(i));
    }
    
    public void testLargeData() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for(int i = 0; i < 1000; i++) 
    			list.add(i);
    		IList<Integer> top = Searcher.topKSort(100, list);
    		assertEquals(50, top.size());
    		for(int i = 0; i < top.size(); i++)
    			assertEquals(950 + i, top.get(i));
    }
}
