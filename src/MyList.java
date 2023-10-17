import java.util.EmptyStackException;

public class MyList<T>{
    private int size = 0;

    private int frequency = 1000;

    private Node<T> head;
    private Node<T> tail;

    MyList(){

    }

    static class Node<T>{
        T item;

        Node<T> prev, next, prevNode, nextNode;

        Node(T item){
            this.item = item;
        }
    }

    public void print(){
        Node<T> head = this.head;

        for(int i = 0; i < this.size; i++){
            System.out.println(head.item);
            head = head.next;
        }
    }

    public int size(){
        return this.size;
    }

    // adds a node at the end of the list
    // returns a boolean if successful
    public void add(T item){
        Node<T> nodeToBeAdded = new Node<>(item);

        if(size == 0){
            this.head = nodeToBeAdded;
            this.tail = head;
            this.size += 1;
            return;
        }

        addAfter(this.tail, nodeToBeAdded);
        this.tail = nodeToBeAdded;
        this.size += 1;
    }

    // removes a node in the list and returns its value
    public T remove(int index){
        Node<T> nodeToBeDeleted = getNode(index);
        Node<T> nextNode = nodeToBeDeleted.next;
        Node<T> prevNode = nodeToBeDeleted.prev;

        // 1-2
        if(this.size == 1){
            this.head = null;
            this.tail = null;
        } else if(nodeToBeDeleted == this.head){
            nextNode.prev = null;
            this.head = nextNode;
        }else if(nodeToBeDeleted == tail){
            prevNode.next = null;
            this.tail = prevNode;
        }else{
            nextNode.prev = prevNode;
            prevNode.next = nextNode;
        }

        this.size -= 1;
        return nodeToBeDeleted.item;
    }

    public T pop(){
        return remove(this.size-1);
    }

    // returns the value/item of the given index node
    public T get(int index){
        return getNode(index).item;
    }

    // add a node after the given index or node
    private void addAfter(Node<T> node, Node<T> nodeToBeAdded){
        Node<T> next = node.next;

        node.next = nodeToBeAdded;

        nodeToBeAdded.prev = node;
        nodeToBeAdded.next = next;

        if(node != this.tail){
            next.prev = nodeToBeAdded;
        }
    }

    // gets a node object in the list
    private Node<T> getNode(int index){
        Node<T> pointer = getClosest(index);

        if(pointer == tail){
            for(int i = this.size-1; i > index; i--){
                pointer = pointer.prev;
            }
        }else{
            for(int i = 0; i < index; i++){
                pointer = pointer.next;
            }
        }

        return pointer;
    }

    // gets the closest node pivot pointer from a given index
    // 1 2 3 4 5
    private Node<T> getClosest(int index){
        if(isEmpty()){
            throw new EmptyStackException();
        }

        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException();
        }

        Node<T> lowerPointer = this.head;
        Node<T> higherPointer = this.tail;
        int lowerIndex = 0;
        int higherIndex = this.size - 1;

        double nextPointerGap = (double)(this.size-1) / 2;

        while(isInRange(index, lowerIndex, higherIndex) && lowerPointer.next != null){
            if((index - lowerIndex) < (higherIndex - index)){
                // head is closer
                higherPointer = higherPointer.prevNode;
                higherIndex = higherIndex - (int)Math.floor(nextPointerGap);

            }else{
                higherPointer = higherPointer.prevNode;
                higherIndex -= nextPointerGap;
                nextPointerGap /= 2;
            }
        }

        return null;
    }

    private boolean isInRange(int index, int lower, int higher){
        return (index - lower) < (higher - index);
    }

    private boolean isEmpty(){
        return this.size == 0;
    }
}