/*
Program #4
Nhan Phan
cssc0928
*/  


package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K extends Comparable<K>,V> implements DictionaryADT<K,V>{
    protected int modCounter;
    protected int currentSize;
    protected Node<K,V> root;
    private K reverseLookUpKey;
    
    class Node<K,V> {
        K key;
        V value;
        Node <K,V> leftChild;
        Node <K,V> rightChild;
        
        public Node(K k, V v) {
            key = k;
            value = v;
            leftChild = rightChild = null;
        } // End of constructor
    } // End of Node class 
    
    public BinarySearchTree() {
        modCounter = currentSize = 0;
        root = null;
    }// End of constructor
    
    public boolean contains(K key) {
        return findValue(key, root) != null;
    }
    
    public boolean add(K key, V value) {
        if (root == null)
            root = new Node<K,V>(key,value);
        else
            insert(key, value, root, null, false);
        currentSize++;
        modCounter++;
        return true;
    }
    
    public boolean delete(K key) {
        if (root == null)
            return false;
        if (remove(key, root, null, false)) {
            currentSize--;
            modCounter++;
            return true;
        }
        return false;
    }
    
    public V getValue(K key) {
        return findValue(key, root);
    }

    public K getKey(V value) {
        findKey(value, root);
        return reverseLookUpKey;
    }
    
    public int size() {
        return currentSize;
    }
    
    // Hardcoded
    public boolean isFull() {return false;}

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
        currentSize = 0;
        modCounter++;
    }
    
    public Iterator<K> keys() {
        return new KeyIteratorHelper();
    }

    public Iterator<V> values() {
        return new ValueIteratorHelper();
    }
    
    private void insert(K key, V value, Node<K,V> node, Node<K,V> parent, boolean left) {
        if (node == null)
            if (left) 
                parent.leftChild = new Node<K,V>(key, value);
            else
                parent.rightChild = new Node<K,V>(key, value);
        else if (key.compareTo(node.key) < 0)
            insert(key, value, node.leftChild, node, true);
        else
            insert(key, value, node.rightChild, node, false);
    }
    
    private V findValue(K key, Node<K,V> node) {
        if (node == null)
            return null;
        int comp = key.compareTo(node.key);
        if (comp < 0)
            return findValue(key, node.leftChild);
        else if (comp > 0)
            return findValue(key, node.rightChild);
        else
            return node.value;
    }
    
    private void findKey(V value, Node<K, V> node) {
        if (node == null)
            return;
        if (((Comparable<V>) value).compareTo(node.value) == 0) {
            reverseLookUpKey = node.key;
            return;
        } 
        else {
            findKey(value, node.leftChild);
            findKey(value, node.rightChild);
        }
    }

    private boolean remove(K key, Node<K,V> node, Node<K,V> parent, boolean left) {
        if (node == null)
            return false;
        int comp = key.compareTo(node.key);
        if (comp < 0)             // Left branch case
            return remove(key, node.leftChild, parent, true);
        else if (comp > 0)        // Right branch case
            return remove(key, node.rightChild, parent, left);
        else {        
            if (node.rightChild == null 
                    && node.leftChild == null) {        // No child case
                if (parent == null) {       // Root case
                    root = null;
                }
                else {                      // Leaf case
                    if (left)               
                        parent.leftChild = null;
                    else
                        parent.rightChild = null;
                }
            }
            else if (node.rightChild == null) {     // 1 left child case
                if (parent == null) {       // Root case
                    root = node.leftChild;
                }
                else {                      // Location of node case
                    if (left)
                        parent.rightChild = node.leftChild;
                    else
                        parent.leftChild = node.leftChild;
                }
            }
            else if (node.leftChild == null) {      // 1 right child case
                if (parent == null) {       // Root case
                    root = node.rightChild;
                }
                else {                      // Location of node case
                    if (left)
                        parent.leftChild = node.rightChild;
                    else
                        parent.rightChild = node.rightChild;
                }               
            }  
            else {                                  // 2 children case
                Node<K,V> tempChild = getSuccessor(node.rightChild);
                if (tempChild == null) {    // 2 leaves case
                    node.key = node.rightChild.key;
                    node.value = node.rightChild.value;
                    node.rightChild = node.rightChild.rightChild;
                }   
                else {
                    node.key = tempChild.key;
                    node.value = tempChild.value;
                }
            }
        }
        return true;
    }
    
    private Node<K,V> getSuccessor(Node<K,V> node) {
        Node<K,V> parent = null;
        while (node.leftChild != null) {
            parent = node;
            node = node.leftChild;
        }
        if (parent == null)
            return null;
        else
            parent.leftChild = node.rightChild;
        return node;
    }
    
    abstract class IteratorHelper<E> implements Iterator<E> {
        protected Node<K,V>[] nodes;
        protected int idx, index;
        protected long modCheck;

        public IteratorHelper() {
            nodes = new Node[currentSize];
            idx = index = 0;
            modCheck = modCounter;
            fillArray(root);
        }

        public boolean hasNext() {
            if (modCheck != modCounter)
                throw new ConcurrentModificationException();
            return idx < currentSize;
        }

        private void fillArray(Node<K,V> n) {
            if (n == null) return;
            fillArray(n.leftChild);
            nodes[index++] = n;
            fillArray(n.rightChild);
        }

        public abstract E next();

        public void remove() {
            throw new UnsupportedOperationException();
        }
    } // End of IteratorHelper class    

    class KeyIteratorHelper<K> extends IteratorHelper<K> {
        public KeyIteratorHelper() {
            super();
        }

        public K next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (K) nodes[idx++].key;
        }
    } // End of KeyIteratorHelper class

    class ValueIteratorHelper<V> extends IteratorHelper<V> {
        public ValueIteratorHelper() {
            super();
        }

        public V next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (V) nodes[idx++].value;
        }
    } // End of ValueIteratorHelper class
}
