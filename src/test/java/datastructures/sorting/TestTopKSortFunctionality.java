package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.event.ListSelectionEvent;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test()
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
    
    @Test(timeout=SECOND)
    public void testSmallerThanN() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for(int i = 5; i > 0; i--) {
    			list.add(i);
    		}
    		IList<Integer> top = Searcher.topKSort(10, list);
    		assertEquals(5, top.size());
    		for(int i = 0; i < top.size(); i++) {
    			assertEquals(i, top.get(i));
    		}
    }
    
    @Test(timeout=SECOND)
    public void testLargeThanN() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for(int i = 1000; i > 0; i--) {
    			list.add(i);
    		}
    		IList<Integer> top = Searcher.topKSort(100, list);
    		assertEquals(100, top.size());
    		for(int i = 0; i < top.size(); i++) {
    			assertEquals(900 + i, top.get(i));
    		}
    }
    
    @Test(timeout=SECOND)
    public void testSortEmpty() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		try {
    			IList<Integer> sorted = Searcher.topKSort(100, list);
    		} catch(Exception e) {
    			fail("No exception should be thrown sorting an empty list");
    		}    		
    }
    
    @Test(timeout=SECOND)
    public void testAllDuplicates() {
	    	IList<Integer> list = new DoubleLinkedList<>();
	    	for(int i = 0; i < 100; i++)  {
			list.add(7);
	    	}
	    	IList<Integer> sorted = Searcher.topKSort(50, list);
	    	assertEquals(50, sorted.size());
	    	for(int i = 0; i < sorted.size(); i++) {
    			assertEquals(7, sorted.get(i));
    		}
    }
	
    @Test(timeout=SECOND)
    public void testCompleteSortRandom() {
    		Random rand = new Random("mY rAndOM SeED".hashCode());
    		IList<Integer> iList = new DoubleLinkedList<>();
    		List<Integer> list = new ArrayList<Integer>();
    		for(int i = 0; i < 100; i++) {
    			int random = rand.nextInt((1000 - -1000) + 1) + -1000; //Create random number between -1000 and 1000
    			iList.add(random);
    			list.add(random);
    		}
    		IList<Integer> iSorted = Searcher.topKSort(iList.size(), iList);
    		Collections.sort(list);
    		for(int i = 0; i < list.size(); i++) {
    			assertEquals(iSorted.get(i), list.get(i));
    		}
    }
    
    
}
