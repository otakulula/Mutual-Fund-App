/**
 * The Queue class definition
 * @author Anuttam Preetham
 * CIS 22C, Lab 5
 * @param <T> the generic data stored in the Queue
 */
import java.util.NoSuchElementException;

public class Queue<T> implements Q<T> {
    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private int size;
    private Node front;
    private Node end;

    /****CONSTRUCTORS****/

    /**
     * Default constructor for the Queue class
     * @postcondition a new Queue object with all fields
     * assigned default values
     */
    public Queue() {
        front = end = null;
        size = 0;
    }

    /**
     * Converts an array into a Queue
     * @param array the array to copy into
     * the Queue
     */
    public Queue(T[] array) {
        if(array == null){
            return;
        } else {
            for( int i = 0; i < array.length; i++){
                T data = array[i];
                enqueue(data);
            }
        }
    }

    /**
     * Copy constructor for the Queue class
     * Makes a deep copy of the parameter
     * @param original the Queue to copy
     * @postcondition <a new Queue object which is
     * an identical, but distinct, copy of original>
     */
    public Queue(Queue<T> original) {
        if(original == null){
            return;
        } else {
            Node temp = original.front;
            while( temp != null){
                enqueue(temp.data);
                temp = temp.next;
            }
            this.size = original.getSize();
        }
    }

    /****ACCESSORS****/

    /**
     * Returns the value stored at the front
     * of the Queue
     * @return the value at the front of the queue
     * @precondition !isEmpty()
     * @throws NoSuchElementException when the
     * precondition is violated
     */
    public T getFront() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException("getFront: there is nothing in queue");
        }
        return front.data;
    }

    /**
     * Returns the size of the Queue
     * @return the size from 0 to n
     */
    public int getSize() {
        return size;
    }

    /**
     * Determines whether a Queue is empty
     * @return whether the Queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /****MUTATORS****/

    /**
     * Inserts a new value at the end of the Queue
     *
     * @param data the new data to insert
     * @postcondition <The Queue list has a new value at the end of the list
     * and the size has increased by 1>
     */
    public void enqueue(T data) {
        Node newNode = new Node(data);
        if(size == 0){
            front = end = newNode;
        } else {
            end.next = newNode;
            end = newNode;
        }
        size++;
    }

    /**
     * Removes the front element in the Queue
     * @precondition <size > 0>
     * @throws NoSuchElementException when you try to access an element that doesn't exist, so the
     * the precondition is violated
     * @postcondition <The Queue list has a new Front value and size has decreased by one>
     */
    public void dequeue() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException("dequeue: you can't access an non-existent element");
        } else if ( size == 1){
            front = end = null;
        } else {
            front = front.next;
        }
        size--;
    }

    /****ADDITONAL OPERATIONS****/

    /**
     * Returns the values stored in the Queue
     * as a String, separated by a blank space
     * with a new line character at the end
     * @return a String of Queue values
     */
    @Override public String toString() {
        String result = "";
        Node temp = front;
        while(temp != null) {
            result += temp.data + " ";
            temp = temp.next;
        }

        return result + "\n";
    }

    /**
     * Determines whether two Queues contain
     * the same values in the same order
     * @param obj the Object to compare to this
     * @return whether obj and this are equal
     */

    @Override public boolean equals(Object obj)  {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Queue)) {
            return false;
        } else {
            Queue<T> list = (Queue<T>) obj;
            if (this.size != list.size) {
                return false;
            } else {
                Node temp1 = this.front;
                Node temp2 = list.front;
                while (temp1 != null) {
                    if (temp1.data == null || temp2.data == null) {
                        if (temp1.data != temp2.data) {
                            return false;
                        }
                    } else if (!(temp1.data.equals(temp2.data))) {
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
            }
        }
    }
}