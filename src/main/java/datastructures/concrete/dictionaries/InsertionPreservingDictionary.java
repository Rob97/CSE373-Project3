package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;

public class InsertionPreservingDictionary<K, V> implements IDictionary<K, V> {

	private IDictionary<K, Entry<K, V>> indices; 
	private Entry<K, V> first, last;


	public InsertionPreservingDictionary() {
		indices = new ChainedHashDictionary<K, Entry<K, V>>();
	}

	@Override
	public V get(K key) {
		return indices.get(key).getValue();
	}

	@Override
	public void put(K key, V value) {
		Entry<K, V> newEntry = new Entry<K, V>(key, value, last);
		if (first == null) { // Add when empty
			first = last = newEntry;
		} else if (first == last) { // Add with one item in list
			last = newEntry;
			first.after = last;
		} else { // Add with two or more items in list
			Entry<K, V> oldBack = last;
			last = newEntry;
			oldBack.after = last;
		}
		this.indices.put(key, newEntry);
	}

	@Override
	public V remove(K key) {
		Entry<K, V> toDelete = this.indices.remove(key);
		if (last == first) {
			last = first = null;
		} else if (first == toDelete) {
			first.after = first;
			first.before = null;
		} else if (toDelete == last) {
			last = last.before;
			last.after = null;
		} else {
			toDelete.before.after = toDelete.after;
			toDelete.after.before = toDelete.before;
		}
		return toDelete.getValue();
	}

	@Override
	public boolean containsKey(K key) {
		return this.indices.containsKey(key);
	}

	@Override
	public int size() {
		return indices.size();
	}

	@Override
	public Iterator<KVPair<K, V>> iterator() {
		// TODO Auto-generated method stub
		return new OrderedIterator<K, V>(this.first);
	}

	private static class OrderedIterator<K, V> implements Iterator<KVPair<K, V>> {
		private Entry<K, V> current;
		
		public OrderedIterator(Entry<K, V> first) {
			current = first;
		}
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public KVPair<K, V> next() {
			if (this.hasNext()) {
				Entry<K, V> past = current;
				current = current.after;
				return past.pair;
			} else {
				throw new NoSuchElementException();
			}
		}

	}

	private static class Entry<K, V> {
		public final KVPair<K, V> pair;
		public Entry<K, V> before;
		public Entry<K, V> after;

		public Entry(K key, V value) {
			this.pair = new KVPair<K, V>(key, value);
		}

		public Entry(K key, V value, Entry<K, V> before) {
			this(key, value);
			this.before = before;
		}

		public K getKey() {
			return pair.getKey();
		}
		
		public V getValue() {
			return pair.getValue();
		}

	}
}
