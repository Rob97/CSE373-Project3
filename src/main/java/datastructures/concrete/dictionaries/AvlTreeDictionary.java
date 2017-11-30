package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;

public class AvlTreeDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {
	private KVPair<K, V> data;
	private int height;
	private AvlTreeDictionary<K,V> root;
	private AvlTreeDictionary<K,V> left;
	private AvlTreeDictionary<K,V> right;
	private AvlTreeDictionary<K,V> parent;
	
	public AvlTreeDictionary(/*K key, V value*/) {
		this.data = null;
		this.parent = null;
		this.root = null;
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	
	public AvlTreeDictionary(K key, V value, AvlTreeDictionary<K,V> left, AvlTreeDictionary<K,V> right, AvlTreeDictionary<K,V> parent) {
		this.data = new KVPair<>(key, value);
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.height = 0;
	}
	
	private K getKey() { 
		return data.getKey();
	}
	
	public V get(K key) {
		AvlTreeDictionary<K,V> result = findHelper(root, key);
		if(result == null) {
			throw new NoSuchElementException();
		}else {
			return result.data.getValue();
		}
	}

	private AvlTreeDictionary<K,V> findHelper(AvlTreeDictionary<K,V> currentNode, K key) {
		if(currentNode == null) {
			return null;
		}
		if(currentNode.data.getKey().compareTo(key) == 0) {
			return currentNode;
		}else {
			if(currentNode.data.getKey().compareTo(key) > 0) {
				return findHelper(currentNode.left, key);
			}else {
				return findHelper(currentNode.right, key);
			}
		}
	}
	
	@Override
	public void put(K key, V value) {
		root = insert(root,key, value, null);
	}
	private AvlTreeDictionary<K,V> insert(AvlTreeDictionary<K,V> currentNode,K key,V value, AvlTreeDictionary<K,V> parentNode){
		if(currentNode == null) {
			return new AvlTreeDictionary<>(key, value, null, null, parentNode);
		}else if(currentNode.getKey().compareTo(key) > 0 ) {
			currentNode.left = insert(currentNode.left, key, value, currentNode);
		}else if(currentNode.getKey().compareTo(key) < 0 ) {
			currentNode.right = insert(currentNode.right, key, value, currentNode);
		} else {
			//overwrite the data if the key exits already 
			currentNode.data = new KVPair<>(key, value);
			return currentNode;
		}
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		if(getBalance(currentNode) >1 && currentNode.left.getKey().compareTo(key) > 0) {
			return rotateRightChild(currentNode);
		}
		if(getBalance(currentNode) <-1 && currentNode.right.getKey().compareTo(key) < 0) {
			return rotateLeftChild(currentNode);
		}
		if(getBalance(currentNode) > 1 && currentNode.right.getKey().compareTo(key) < 0) {
			currentNode.left = rotateLeftChild(currentNode.right);
			return rotateRightChild(currentNode);
		}
		if(getBalance(currentNode) < -1 && currentNode.right.getKey().compareTo(key) > 0) {
			currentNode.right = rotateRightChild(currentNode.right);
			return rotateLeftChild(currentNode);
		}
		return currentNode;
	}

	@Override
	public V remove(K key) {
		if(!containsKey(key)) {
			throw new NoSuchElementException(); 
		}else {
			AvlTreeDictionary<K,V> currentNode = findHelper(root, key);
			V result = currentNode.get(key);
			root = removeHelper(root,key);
			return result;
		}
	}
	
	private AvlTreeDictionary<K,V> removeHelper(AvlTreeDictionary<K, V> currentNode, K key) {
		if(currentNode == null) {
			return currentNode;
		}
		if(currentNode.getKey().compareTo(key) > 0) {
			currentNode.left = removeHelper(currentNode.left, key);
		}else if(currentNode.getKey().compareTo(key) < 0) {
			currentNode.right = removeHelper(currentNode.right, key);
		}else {
			if(currentNode.left == null || currentNode.right == null) {
				AvlTreeDictionary<K,V> temp = null;
				if(temp == currentNode.left) {
					temp = currentNode.right;
				}else {
						temp = currentNode.left;
					}
				if(temp == null) {
					temp = currentNode;
					currentNode = null;
				}else {
					currentNode = temp;
				}
			}else {
				// two full children
				AvlTreeDictionary<K,V> successor = currentNode.right;
				while(successor.left != null) {
					successor = successor.left;
				}
				currentNode.data = successor.data;
				currentNode.right = removeHelper(currentNode.right, successor.data.getKey());
			}
		}
		if(currentNode == null) {
			return currentNode;
		}
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		if(getBalance(currentNode) > 1 && getBalance(currentNode.left) >= 0) {
			return rotateRightChild(currentNode);
		}
		if(getBalance(currentNode) < -1 && getBalance(currentNode.left) <= 0) {
			return rotateLeftChild(currentNode);
		}
		if(getBalance(currentNode) > 1 && getBalance(currentNode.left) < 0) {
			currentNode.left = rotateLeftChild(currentNode.left);
			return rotateRightChild(currentNode);
		}
		if(getBalance(currentNode) < -1 && getBalance(currentNode.right) > 0) {
			currentNode.right = rotateRightChild(currentNode.right);
			return rotateLeftChild(currentNode);
		}
		return currentNode;
	}

	@Override
	public boolean containsKey(K key) {
		//if the return value of findHelper() is null, key is not in the tree;
		return(findHelper(root, key) != null);
		
	}

	@Override
	public int size() {
		return size(root);
	}
	
	private int size(AvlTreeDictionary<K, V> node) {
		if(node == null) {
			return 0;
		}else {
			return (1 + size(node.left) + size(node.right));
		}
	}

	@Override
	public Iterator<KVPair<K, V>> iterator() {
		return new AvlIterator<KVPair<K, V>>(root);
	}
	
	private class AvlIterator<T> implements Iterator<KVPair<K, V>> {
		private AvlTreeDictionary<K,V> next;
		
		public AvlIterator(AvlTreeDictionary<K,V> root){
			next = root;
			while(root.left != null ) {
				next = next.left;
			}
		}
		public boolean hasNext(){
			return next != null;
		}
		
		public KVPair<K, V> next(){
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			KVPair<K, V> result = next.data;
			if(next.right != null) {
				next = next.right;
				while(next.left != null) {
					next = next.left;
				}
				return result;
			}
			while(true) {
				if(next.parent == null) {
					next = null;
					return result;
				}
				if(next.parent.left == next) {
					next = next.parent;
					return result;
				}
				next = next.parent;
			}
		}
	}
    
	private int getHeight(AvlTreeDictionary<K,V> currentNode) {
		if(currentNode == null) {
			return 0;
		}
		return currentNode.height;
	}
	
	private int max(int a, int b) {
		if(a > b) {
			return a;
		}else {
			return b;
		}
	}
	
	private int getBalance(AvlTreeDictionary<K,V> currentNode) {
		if(currentNode == null) {
			return 0;
		}else {
			return getHeight(currentNode.left) - getHeight(currentNode.right);
		}
	}
	
	private AvlTreeDictionary<K,V> rotateLeftChild(AvlTreeDictionary<K,V> currentNode){
		AvlTreeDictionary<K, V> node1 = currentNode.right;
		AvlTreeDictionary<K, V> node2 = node1.left;
		node1.parent = currentNode.parent;
		currentNode.parent = node1;
		node1.left = currentNode;
		currentNode.right = node2;
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		node1.height = max(getHeight(node1.left), getHeight(node1.right)) + 1;
		return node1;
	}
	private AvlTreeDictionary<K,V> rotateRightChild(AvlTreeDictionary<K,V> currentNode){
		AvlTreeDictionary<K, V> node1 = currentNode.left;
		AvlTreeDictionary<K, V> node2 = node1.right;
		node1.parent = currentNode.parent;
		currentNode.parent = node1;
		node1.right = currentNode;
		currentNode.left = node2;
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		node1.height = max(getHeight(node1.left), getHeight(node1.right)) + 1;
		return node1;
	}
	
}
