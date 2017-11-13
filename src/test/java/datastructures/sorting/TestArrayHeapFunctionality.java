package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        heap.insert(4);
        assertEquals(2,heap.size());
        heap.insert(5);
        assertEquals(3,heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMin() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(3);
    		heap.insert(4);
    		heap.insert(5);
    		heap.removeMin();
    		assertEquals(2, heap.size());
    		assertEquals(4, heap.peekMin());
    		heap.removeMin();
    		assertEquals(1, heap.size());
    		assertEquals(5, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertionEfficiency() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for(int i = 10000; i > 0; i--) 
    	        heap.insert(i);	
    		assertEquals(100000,heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinEfficiency() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
		for(int i = 0; i < 1000; i++) 
	        heap.insert(i);
		assertEquals(1000, heap.size());
		for(int i = 0; i < 1000; i++)
			heap.removeMin();
		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMin() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(3);
    		assertEquals(3, heap.peekMin());
    		heap.insert(4);
    		assertEquals(3, heap.peekMin());
    		heap.insert(1);
    		assertEquals(1, heap.peekMin());
    }
   
    @Test(timeout=SECOND)
    public void testIsEmpty() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		assertTrue(heap.isEmpty());
    }
}
