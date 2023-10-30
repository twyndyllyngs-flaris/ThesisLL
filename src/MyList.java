import java.util.EmptyStackException;

public class MyList<T>{
    private int size = 0;

    private final int frequency = 5;

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
        Node<T> node = this.head;

        for(int i = 0; i < this.size; i++){
            String cell = "[";
            cell += node.prevNode == null ? "X|" : node.prevNode.item + "|";
            cell += node.prev == null ? "X|" : node.prev.item + "|";
            cell += node.item + "|";
            cell += node.next == null ? "X|" : node.next.item + "|";
            cell += node.nextNode == null ? "X" : node.nextNode.item + "";
            cell += "]";

            node = node.next;

            System.out.println(cell);
        }
    }

    public int size(){
        return this.size;
    }

    // adds a node at the end of the list
    public void add(T item){
        Node<T> nodeToBeAdded = new Node<>(item);

        if(size == 0){
            this.head = nodeToBeAdded;
            this.tail = head;
            this.size += 1;
            return;
        }

        addAfter(this.tail, nodeToBeAdded);
        nodeToBeAdded.prevNode = this.tail.prevNode;
        this.tail.prevNode = null;
        this.tail = nodeToBeAdded;

        //index of current middle pointer
        int currentIndex = (this.size - 1) / 2;

        this.size += 1;

        if(isTimeToAdd()){
            if(this.tail.prevNode == null){
                Node<T> temp = this.tail;

                int gap = (this.size-1) / 2;

                for(int i = 0; i < gap; i++){
                    temp = temp.prev;
                }

                this.head.nextNode = temp;
                this.tail.prevNode = temp;
            }else{
                addPointers(this.tail, this.tail.prevNode, 0, this.size-1, currentIndex, this.size-1, 0, this.size-2, currentIndex);
            }
        }else if (this.size > this.frequency){
            updatePointers(this.tail, this.tail.prevNode, 0, this.size-1, currentIndex, this.size-1, 0, this.size-2, currentIndex);
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
    private void addPointers(Node<T> parentPointer, Node<T> currentPointer, int lowIndex, int highIndex, int currentIndex, int parentIndex, int originalLow, int originalHigh, int originalIndex){
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

        //System.out.println("b " + lowIndex + " " + currentIndex + " " + highIndex + " " + originalLow + " " + originalIndex + " " + originalHigh);

        if(currentPointer.prevNode != null){
            Node<T> prevNode = currentPointer.prevNode;
            int prevIndex = (originalLow + originalIndex) / 2;
            addPointers(currentPointer, prevNode, lowIndex, currentIndex, prevIndex, highIndex, originalLow, originalIndex, prevIndex);
        }

        if(currentPointer.nextNode != null){
            Node<T> nextNode = currentPointer.nextNode;
            int nextIndex = (originalIndex + originalHigh) / 2;
            addPointers(currentPointer, nextNode, currentIndex, highIndex, nextIndex, lowIndex, originalIndex, originalHigh, nextIndex);
        }

        if(isLeaf(currentPointer)){
            addPointerFrom(currentPointer, lowIndex, currentIndex, true);
            addPointerFrom(currentPointer, currentIndex, highIndex, false);
        }
    }

    // add pointer to left
    private void addPointerFrom(Node<T> parent, int lowIndex, int highIndex, boolean isLeft){
        Node<T> node = parent;

        System.out.println(lowIndex + " " + highIndex + " " + isLeft);

        if(isLeft){
            int gap = (int)Math.ceil((double) (highIndex - lowIndex) / 2);

            for(int i = 0; i < gap; i++){
                node = node.prev;
            }

            parent.prevNode = node;
        }else{
            int gap = ((lowIndex + highIndex) / 2) - lowIndex;

            for(int i = 0; i < gap; i++){
                node = node.next;
            }

            parent.nextNode = node;
        }
    }

    // if a node does not have prevNode or nextNode
    private boolean isLeaf(Node<T> node){
        return node.prevNode == null && node.nextNode == null;
    }

    // determines if it is time to add more pointers
    private boolean isTimeToAdd(){
        return this.size % this.frequency == 0;
    }

    // updates the middle pointers (moves right side)
    private void updatePointers(Node<T> parentPointer, Node<T> currentPointer, int lowIndex, int highIndex, int currentIndex, int parentIndex, int originalLow, int originalHigh, int originalIndex){
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

        //System.out.println("b " + lowIndex + " " + currentIndex + " " + highIndex + " " + originalLow + " " + originalIndex + " " + originalHigh);

        if(currentPointer.prevNode != null){
            Node<T> prevNode = currentPointer.prevNode;
            int prevIndex = (originalLow + originalIndex) / 2;
            updatePointers(currentPointer, prevNode, lowIndex, currentIndex, prevIndex, highIndex, originalLow, originalIndex, prevIndex);
        }

        if(currentPointer.nextNode != null){
            Node<T> nextNode = currentPointer.nextNode;
            int nextIndex = (originalIndex + originalHigh) / 2;
            updatePointers(currentPointer, nextNode, currentIndex, highIndex, nextIndex, lowIndex, originalIndex, originalHigh, nextIndex);
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

    // given two indexes, finds the index closer to given value
    private boolean lowerPointerIsCloser(int index, int lowerIndex, int higherIndex){
        return (index - lowerIndex) < (higherIndex - index);
    }

    // returns true when given index is in between or equal to given range value
    private boolean isInRange(int index, int lower, int higher){
        return index >= lower && index <= higher;
    }

    private boolean isEmpty(){
        return this.size == 0;
    }
}