/*
Program #4
Nhan Phan
cssc0928
*/

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
    protected LinkedList<Wrapper<K, V>>[] list;
    protected int modCounter;
    protected int currentSize, maxSize;
    protected int tableSize;

    class Wrapper<K, V> implements Comparable<Wrapper<K, V>> {
        K key;
        V value;

        public Wrapper(K k, V v) {
            key = k;
            value = v;
        }

        public int compareTo(Wrapper<K, V> w) {
            return ((Comparable<K>) key).compareTo((K) w.key);
        }
    } // End of Wrapper class
    
    // Unordered Linked List
    class LinkedList<T extends Comparable<T>> implements Iterable<T> {
        protected class Node<E> {
            E data;
            Node<E> next;

            public Node(E info) {
                data = info;
            }
        } // End of Node class

        protected Node<T> head;
        protected int currentLinkedListSize;

        public LinkedList() {
            head = null;
            currentLinkedListSize = 0;
        }

        public boolean add(T object) {
            Node<T> newNode = new Node<T>(object);
            newNode.next = head;
            head = newNode;
            currentLinkedListSize++;
            return true;
        }

        public boolean delete(T obj) {
            Node<T> current = head, previous = null;
            int temp = currentLinkedListSize;
            while (current != null) {
                if (current.data.compareTo(obj) == 0) {
                    if (previous == null) { // The head CASE
                        head = head.next;
                        current = head;
                    } else { // The middle CASE
                        current = current.next;
                        previous.next = current;
                    }
                    currentLinkedListSize--;
                    break;
                } else {
                    previous = current;
                    current = current.next;
                }
            }
            // Size changes meaning deletion is successful
            return temp > currentLinkedListSize;
        }

        public void clear() {
            currentLinkedListSize = 0;
            head = null;
        }

        public T find(T object) {
            if (head == null)
                return null;

            Node<T> temp = head;
            while (temp != null) {
                if (object.compareTo(temp.data) == 0)
                    return temp.data;
                temp = temp.next;
            }
            return null;
        }

        public Iterator<T> iterator() {
            return new LinkedListIteratorHelper();
        }
        
        class LinkedListIteratorHelper implements Iterator<T> {
            Node<T> itrPtr;

            public LinkedListIteratorHelper() {
                itrPtr = head;
            }

            public boolean hasNext() {
                return itrPtr != null;
            }

            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                T tmp = itrPtr.data;
                itrPtr = itrPtr.next;
                return tmp;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        } // End of LinkedListIteratorHelper class
    } // End of LinkedList class

    public Hashtable(int size) {
        currentSize = modCounter = 0;
        maxSize = size;
        tableSize = (int) (maxSize * 1.3f);
        list = new LinkedList[tableSize];
        for (int i = 0; i < tableSize; i++)
            list[i] = new LinkedList<Wrapper<K, V>>();
    } // End of constructor

    public boolean contains(K key) {
        return list[getIndex(key)].find(new Wrapper<K, V>(key, null)) != null;
    }

    public boolean add(K key, V value) {
        if (isFull())
            return false;
        if (list[getIndex(key)].find(new Wrapper<K, V>(key, null)) != null)
            return false;
        list[getIndex(key)].add(new Wrapper<K, V>(key, value));
        currentSize++;
        modCounter++;
        return true;
    }

    public boolean delete(K key) {
        V tempValue;
        int tempSize = currentSize;
        if ((tempValue = getValue(key)) == null)
            return false;
        list[getIndex(key)].delete(new Wrapper<K, V>(key, tempValue));
        currentSize--;
        modCounter++;
        
        // Size changes meaning deletion is successful
        return tempSize > currentSize;
    }

    public V getValue(K key) {
        Wrapper<K, V> temp = list[getIndex(key)].find(new Wrapper<K, V>(key, null));
        if (temp == null)
            return null;
        return temp.value;
    }
    
    public K getKey(V value) {
        for (int i = 0; i < tableSize; i++)
            for (Wrapper<K, V> w : list[i])
                if (((Comparable<V>) value).compareTo(w.value) == 0)
                    return w.key;
        return null;
    }

    public int size() {
        return currentSize;
    }

    public boolean isFull() {
        return currentSize == maxSize;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void clear() {
        for (int i = 0; i < tableSize; i++)
            list[i].clear();
        currentSize = 0;
        modCounter++;
    }

    public Iterator<K> keys() {
        return new KeyIteratorHelper();
    }

    public Iterator<V> values() {
        return new ValueIteratorHelper();
    }

    private int getIndex(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % tableSize;
    }

    abstract class IteratorHelper<E> implements Iterator<E> {
        protected Wrapper<K,V>[] nodes;
        protected int idx;
        protected long modCheck;

        public IteratorHelper() {
            nodes = new Wrapper[currentSize];
            idx = 0;
            int j = 0;
            modCheck = modCounter;
            for (int i = 0; i < tableSize; i++)
                for (Wrapper<K,V> n : list[i])
                    nodes[j++] = n;
            sort(nodes);
        }

        public boolean hasNext() {
            if (modCheck != modCounter)
                throw new ConcurrentModificationException();
            return idx < currentSize;
        }

        // Shell sort
        private void sort(Wrapper<K,V>[] array) {
            Wrapper<K,V> temp = null;
            int h = 1;
            int in, out;
            while (h <= array.length / 3)
                h = h * 3 + 1;
            while (h > 0) {
                for (out = h; out < array.length; out++) {
                    temp = array[out];
                    in = out;
                    while (in > h - 1 && array[in - h].compareTo(temp) >= 0) {
                        array[in] = array[in - h];
                        in -= h;
                    } // End of while loop
                    array[in] = temp;
                } // End of for loop
                h = (h - 1) / 3;
            } // End of while loop
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
