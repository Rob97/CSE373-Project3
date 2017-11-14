package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Arrays;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_SIZE = 5;
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(DEFAULT_SIZE);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        T result = this.peekMin();
        heap[0] = heap[this.size -1];
		heap[this.size -1] = null;
        removeMinHelper(0);
        this.size--;
        return result;
    }
    
    private void removeMinHelper(int position) {
    		T min = null;  
    		int order = 0;
    		for(int i = 1; i <= NUM_CHILDREN; i++ ) {
    			//If child is valid and less than parent
    			if( position * NUM_CHILDREN + i < heap.length && heap[position * NUM_CHILDREN + i] != null 
    					&& heap[position * NUM_CHILDREN + i].compareTo(heap[position]) < 0) {
    				//If we have found a child smaller than the parent or if we found an even smaller child
    				if(min == null || min != null && heap[position * NUM_CHILDREN + i].compareTo(min) < 0) { 
    					min = heap[position * NUM_CHILDREN + i];
    					order = i;
    				}
    			}
    		}
    		if(min != null) {
    			heap[position * NUM_CHILDREN + order] = heap[position];
    			heap[position] = min;
    			removeMinHelper(position * NUM_CHILDREN + order);
    		}	
    }

    @Override
    public T peekMin() {
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if(heap.length == size) {
        		T[] newHeap = makeArrayOfT(this.size * 2);
        		for(int i = 0; i < size; i++) 
        			newHeap[i] = heap[i];
        		heap = newHeap;
        }
        heap[size] = item;
        insertionHelper(size);
    		size++;
    }
    
    private void insertionHelper(int position) {
    		if(heap[(position-1) / NUM_CHILDREN].compareTo(heap[position]) > 0) { //If parent is larger than child
    			T item = heap[(position-1) / NUM_CHILDREN];
    			heap[(position-1) / NUM_CHILDREN] = heap[position];
    			heap[position] = item;
    			insertionHelper((position-1) / NUM_CHILDREN);
    		}
    }
    
    @Override
    public int size() {
    		return this.size;
    }
}
