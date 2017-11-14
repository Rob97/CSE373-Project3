package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
	
    @Test()
    public void stressTestRandomSort() {
    		Random rand = new Random("mY rAndOM SeED".hashCode());
    		IList<Integer> iList = new DoubleLinkedList<>();
    		List<Integer> list = new ArrayList<Integer>();
    		for(int i = 0; i < 130000; i++) {
    			int random = rand.nextInt((1000 - -1000) + 1) + -1000; //Create random number between -1000 and 1000
    			iList.add(random);
    			list.add(random);
    		}
    		IList<Integer> iSorted = Searcher.topKSort(130000, iList);
    		Collections.sort(list);
    		for(int i = 0; i < iSorted.size(); i++) {
    			assertEquals(iSorted.get(i), list.get((list.size()-iSorted.size()) + i));
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertionEfficiency() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for(int i = 1000000; i > 0; i--) 
    	        heap.insert(i);	
    		assertEquals(1000000,heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveMinEfficiency() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
		for(int i = 1000000; i > 0; i--) 
	        heap.insert(i);
		assertEquals(1000000, heap.size());
		for(int i = 0; i < 1000000; i++)
			heap.removeMin();
		assertEquals(0, heap.size());
    }
}
