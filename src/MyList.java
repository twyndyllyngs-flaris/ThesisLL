import java.util.EmptyStackException;

public class MyList<T>{
    private int size = 0;

    private final int frequency = 1000;

    private Node<T> head;
    private Node<T> tail;

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
    // 1 2 3 4 5p 6 7 8 9 10p 11 12 13 14p 15 16 17 18 19 20p 21 22 23 24 25 26 27 28 29 30
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

        int currentIndex = (this.size - 2) / 2;

        if(isTimeToAdd()){
            addPointers(this.tail, this.tail.prevNode, 0, this.size, currentIndex, this.size);
        }else{
            updatePointers(this.tail, this.tail.prevNode, 0, this.size, currentIndex, this.size);
        }
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

    // removes the last item and returns its value
    public T pop(){
        return remove(this.size-1);
    }

    // returns the value/item of the given index node
    public T get(int index){
        return getNode(index).item;
    }

    // updates and adds new pointers
    private void addPointers(Node<T> parentPointer, Node<T> currentPointer, int lowIndex, int highIndex, int currentIndex, int parentIndex){
        int range = lowIndex + highIndex;

        if(currentIndex != (range / 2)){
            Node<T> newPointer = currentPointer.next;
            newPointer.nextNode = currentPointer.nextNode;
            newPointer.prevNode = currentPointer.prevNode;

            currentPointer.nextNode = null;
            currentPointer.prevNode = null;

            currentPointer = currentPointer.next;
            currentIndex = currentIndex + 1;

            if(currentIndex == (this.size - 1) / 2) {
                this.head.nextNode = newPointer;
                this.tail.prevNode = newPointer;
            }else if(currentIndex <= parentIndex){
                parentPointer.prevNode = newPointer;
            }else{
                parentPointer.nextNode = newPointer;
            }
        }

        if(currentPointer.prevNode != null){
            Node<T> prevNode = currentPointer.prevNode;
            highIndex = currentIndex;
            currentIndex = currentIndex / 2;
            addPointers(currentPointer, prevNode, lowIndex, highIndex, currentIndex, highIndex);
        }

        if(currentPointer.nextNode != null){
            Node<T> nextNode = currentPointer.nextNode;
            lowIndex = currentIndex;
            currentIndex = currentIndex + (currentIndex / 2);
            addPointers(currentPointer, nextNode, lowIndex, highIndex, currentIndex, lowIndex);
        }

        if(isLeaf(currentPointer)){
            addPointerFrom(currentPointer, 0, currentIndex, true);
            addPointerFrom(currentPointer, currentIndex, parentIndex, false);
        }
    }

    // add pointer to left
    private void addPointerFrom(Node<T> parent, int lowIndex, int highIndex, boolean isLeft){
        int gap = (lowIndex + highIndex) / 2;

        Node<T> node = parent;

        if(isLeft){
            for(int i = 0; i < gap; i++){
                node = node.prev;
            }

            parent.prevNode = node;
        }else{
            for(int i = 0; i < gap; i++){
                node = node.next;
            }

            parent.nextNode = node;
        }
    }

    private boolean isLeaf(Node<T> node){
        return node.prevNode == null && node.nextNode == null;
    }

    // determines if it is time to add more pointers
    private boolean isTimeToAdd(){
        return this.size % this.frequency == 0;
    }

    // 0h 1 2p 3 4m 5 6p 7 8 9 10t               11 12 13 14 15
    // 1 3 5 7 10
    // 1 3 6 8 11
    // 1 3 6 9 12
    // 1 4 7 10 13
    // 1 4 7 10 14
    // 1 4 8 11 15
    // size = 11
    // parentPointer = tail (10)
    // currentPointer = 4
    // currentIndex = 4
    // lowIndex = 0
    // highIndex = 10
    // updates the middle pointers (forwards)
    private void updatePointers(Node<T> parentPointer, Node<T> currentPointer, int lowIndex, int highIndex, int currentIndex, int parentIndex){
        int range = lowIndex + highIndex;

        if(currentIndex != (range / 2)){
            Node<T> newPointer = currentPointer.next;
            newPointer.nextNode = currentPointer.nextNode;
            newPointer.prevNode = currentPointer.prevNode;

            currentPointer.nextNode = null;
            currentPointer.prevNode = null;

            currentPointer = currentPointer.next;
            currentIndex = currentIndex + 1;

            if(currentIndex == (this.size - 1) / 2) {
                this.head.nextNode = newPointer;
                this.tail.prevNode = newPointer;
            }else if(currentIndex <= parentIndex){
                parentPointer.prevNode = newPointer;
            }else{
                parentPointer.nextNode = newPointer;
            }
        }

        if(currentPointer.prevNode != null){
            Node<T> prevNode = currentPointer.prevNode;
            highIndex = currentIndex;
            currentIndex = currentIndex / 2;
            updatePointers(currentPointer, prevNode, lowIndex, highIndex, currentIndex, highIndex);
        }

        if(currentPointer.nextNode != null){
            Node<T> nextNode = currentPointer.nextNode;
            lowIndex = currentIndex;
            currentIndex = currentIndex + (currentIndex / 2);
            updatePointers(currentPointer, nextNode, lowIndex, highIndex, currentIndex, lowIndex);
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