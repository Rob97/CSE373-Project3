package datastructures.sorting;

import static org.junit.Assert.assertTrue;

import java.util.Random;

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

	@Test(timeout = SECOND)
	public void testBasicSize() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		assertEquals(0, heap.size());
		heap.insert(3);
		assertEquals(1, heap.size());
		heap.insert(4);
		assertEquals(2, heap.size());
		heap.insert(5);
		assertEquals(3, heap.size());
	}

	@Test(timeout = SECOND)
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

	@Test(timeout = SECOND)
	public void testPeekMin() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		heap.insert(3);
		assertEquals(3, heap.peekMin());
		heap.insert(4);
		assertEquals(3, heap.peekMin());
		heap.insert(1);
		assertEquals(1, heap.peekMin());
	}

	@Test(timeout = SECOND)
	public void testIsEmpty() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		assertTrue(heap.isEmpty());
	}

	@Test(timeout = SECOND)
	public void testInsertInReverseOrder() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = 100; i > 0; i--) {
			heap.insert(i);
		}
		assertEquals(100, heap.size());
		for (int i = 1; i <= 100; i++) {
			assertEquals(i, heap.removeMin());
		}
	}

	@Test(timeout = SECOND)
	public void testInsertHalfInOrder() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = 100; i > 50; i--) {
			heap.insert(i);
			heap.insert(100 - i);
		}
		heap.insert(50);
		assertEquals(101, heap.size());
		for (int i = 0; i <= 100; i++) {
			assertEquals(i, heap.removeMin());
		}
	}

	@Test(timeout = SECOND)
	public void testInsertRandom() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		Random rand = new Random("thIS is  A  cOOl SeEd".hashCode());
		for (int i = 0; i < 100; i++) {
			heap.insert(rand.nextInt((1000 - -1000) + 1) + -1000);
		}
		int lastValue = heap.removeMin();
		while (!heap.isEmpty()) {
			int current = heap.removeMin();
			assertTrue(lastValue <= current);
			lastValue = current;
		}
	}

	@Test(timeout = SECOND)
	public void testRemoveMinException() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		boolean thrown = false;
		try {
			heap.removeMin();
		} catch (EmptyContainerException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test(timeout = SECOND)
	public void testPeekMinException() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		boolean thrown = false;
		try {
			heap.peekMin();
		} catch (EmptyContainerException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test(timeout = SECOND)
	public void testInsertException() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		boolean thrown = false;
		try {
			heap.insert(null);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}
}
