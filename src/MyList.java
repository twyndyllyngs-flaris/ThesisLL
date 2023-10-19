import java.util.EmptyStackException;

public class MyList<T>{
    private int size = 0;

    private final int frequency = 1000;

    private Node<T> head;
    private Node<T> tail;

   /* private Node<T> headPointer;
    private Node<T> tailPointer;
    int headPointerIndex;
    int tailPointerIndex;*/

    MyList(){

    }

    private static class Node<T>{
        T item;

        Node<T> prev, next, prevNode, nextNode;

        Node(T item){
            this.item = item;
        }
    }

    private static class ReturnObject<T>{
        Node<T> node;
        int index;

        boolean isLowerClosest;

        ReturnObject(Node<T> node, int index, boolean isLowerClosest){
            this.node = node;
            this.index = index;
            this.isLowerClosest = isLowerClosest;
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

    // 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
    // 1 3 5 7 10
    // 1 3 6 8 11
    // 1 3 6 9 12
    // 1 4 7 10 13
    // 1 4 7 10 14
    // 1 4 8 11 15
    private void updatePointers(){
        Node<T> currentPointer = this.head;

        if(currentPointer.prevNode != null){

        }else if(currentPointer.nextNode != null){

        }else{

        }
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
        ReturnObject<T> returnObject = getClosest(index);

        Node<T> pointer = returnObject.node;
        int closestIndex = returnObject.index;
        boolean isLowerClosest = returnObject.isLowerClosest;

        if(isLowerClosest){
            for(int i = closestIndex; i < index; i++){
                pointer = pointer.next;
            }
        }else{
            for(int i = closestIndex; i > index; i--){
                pointer = pointer.prev;
            }
        }

        return pointer;
    }

    // gets the closest node pivot pointer from a given index
    private ReturnObject<T> getClosest(int index){
        if(isEmpty()){
            throw new EmptyStackException();
        }

        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException();
        }

        Node<T> lowerPointer = this.head;
        Node<T> higherPointer = this.tail;
        int lowerIndex = 0;
        int higherIndex = this.size-1;

        while(isInRange(index, lowerIndex, higherIndex) && lowerPointer.next != null){
            int sumRange = lowerIndex + higherIndex;

            if((index - lowerIndex) < (higherIndex - index)){
                // head is closer
                higherPointer = higherPointer.prevNode;
                higherIndex = higherIndex - (sumRange - index);

                if(index == lowerIndex) {
                    return new ReturnObject<>(lowerPointer, lowerIndex, true);
                }
            }else{
                lowerPointer = lowerPointer.nextNode;
                lowerIndex = lowerIndex + (sumRange - index);

                if(index == higherIndex){
                    return new ReturnObject<>(higherPointer, higherIndex, false);
                }
            }
        }

        if(lowerPointerIsCloser(index, lowerIndex, higherIndex)){
            return new ReturnObject<>(lowerPointer, lowerIndex, true);
        } else{
            return new ReturnObject<>(higherPointer, higherIndex, false);
        }

    }

    private boolean lowerPointerIsCloser(int index, int lowerIndex, int higherIndex){
        return (index - lowerIndex) < (higherIndex - index);
    }

    private boolean isInRange(int index, int lower, int higher){
        return index >= lower && index <= higher;
    }

    private boolean isEmpty(){
        return this.size == 0;
    }
}