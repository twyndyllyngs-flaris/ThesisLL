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

    private static class RemoveObject<T>{
        Node<T> node, parent;
        int originalLower, originalHigher, lowerIndex, higherIndex;
        boolean isLowerClosest;

        RemoveObject(Node<T> node, Node<T> parent, int originalLower, int originalHigher, int lowerIndex, int higherIndex, boolean isLowerClosest){
            this.node = node;
            this.parent = parent;
            this.originalLower = originalLower;
            this.originalHigher = originalHigher;
            this.lowerIndex = lowerIndex;
            this.higherIndex = higherIndex;
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
    // 1 2 3 4 5 6 7 8 9 10
    public T remove(int index){
        if(index < 0){
            throw new EmptyStackException();
        }

        if(index >= this.size){
            throw new IndexOutOfBoundsException();
        }

        RemoveObject<T> details = getNodeDetails(index);
        Node<T> nodeToBeDeleted = details.node;
        Node<T> next = nodeToBeDeleted.next;
        Node<T> prev = nodeToBeDeleted.prev;
        Node<T> nextNode = nodeToBeDeleted.nextNode;
        Node<T> prevNode = nodeToBeDeleted.prevNode;
        Node<T> parent = details.parent;
        int lowIndex = details.originalLower;
        int highIndex = details.originalHigher;
        boolean isLowerClosest = details.isLowerClosest;

        if(this.size == 1){
            this.head = null;
            this.tail = null;
        } else if(nodeToBeDeleted == this.head){
            next.prev = null;
            next.nextNode = this.head.nextNode;
            this.head = next;
        }else if(nodeToBeDeleted == tail){
            prev.next = null;
            prev.prevNode = this.tail.prevNode;
            this.tail = prev;
        }else if(parent.prevNode == nodeToBeDeleted || parent.nextNode == nodeToBeDeleted){
            next.prev = prev;
            prev.next = next;

            int newMiddleIndex = (lowIndex + highIndex - 1 ) / 2;

            if(newMiddleIndex != index){
                // move left
                prev.nextNode = nextNode;
                prev.prevNode = prevNode;

                if(index == (this.size-1) / 2){
                    // index is the first middle, have to move both head and tail pointer
                    this.head.nextNode = prev;
                    this.tail.prevNode = prev;
                }else if(parent.prevNode == nodeToBeDeleted){
                    parent.prevNode = prev;
                }else{
                    parent.nextNode = prev;
                }
            }else{
                // move right
                next.nextNode = nextNode;
                next.prevNode = prevNode;

                if(index == (this.size-1) / 2){
                    // index is the first middle, have to move both head and tail pointer
                    this.head.nextNode = next;
                    this.tail.prevNode = next;
                }else if(parent.prevNode == nodeToBeDeleted){
                    parent.prevNode = next;
                }else{
                    parent.nextNode = next;
                }
            }
        }else{
            next.prev = prev;
            prev.next = next;
        }

        boolean isTimeToRemove = isTimeToAdd();

        this.size -= 1;

        if(isTimeToRemove){
            if(this.size == this.frequency){
                this.head.nextNode = null;
                this.tail.prevNode = null;
            }else{
                removePointers(this.head.nextNode);
            }
        }

        return nodeToBeDeleted.item;
    }

    // removes the last item and returns its value
    public T pop(){
        return remove(this.size-1);
    }

    // returns the value/item of the given index node
    public T get(int index){
        if(isEmpty()){
            throw new EmptyStackException();
        }

        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException();
        }

        return getNode(index).item;
    }

    private void removePointers(Node<T> currentPointer){
        // recursions
        if(currentPointer.prevNode != null && currentPointer.prevNode.prevNode != null){
            removePointers(currentPointer.prevNode);
        }else{
            currentPointer.prevNode = null;
        }

        if(currentPointer.nextNode != null && currentPointer.nextNode.nextNode != null){
            removePointers(currentPointer.nextNode);
        }else{
            currentPointer.nextNode = null;
        }
    }

    private RemoveObject<T> getClosestNodeDetails(int index){
        Node<T> lowerPointer = this.head;
        Node<T> higherPointer = this.tail;
        Node<T> nextPointer = lowerPointer.nextNode;
        Node<T> parent = this.head;
        int lowerIndex = 0;
        int higherIndex = this.size-1;
        int originalLower = lowerIndex;
        int originalHigher = higherIndex;

        while(isInRange(index, lowerIndex, higherIndex) && nextPointer != null){
            int sumRange = lowerIndex + higherIndex;

            if(lowerPointerIsCloser(index, lowerIndex, higherIndex)){
                if(index == lowerIndex) {
                    if(parent == this.head){
                        parent = higherPointer;
                    }
                    return new RemoveObject<>(lowerPointer, parent, originalLower, originalHigher, lowerIndex, higherIndex, true);
                }

                parent = higherPointer;
                higherPointer = nextPointer;
                nextPointer = nextPointer.prevNode;

                originalHigher = higherIndex;
                originalLower = lowerIndex;
                higherIndex = sumRange / 2;
            }else{
                if(index == higherIndex){
                    if(parent == this.tail){
                        parent = lowerPointer;
                    }
                    return new RemoveObject<>(higherPointer, parent, originalLower, originalHigher, lowerIndex, higherIndex, false);
                }

                parent = lowerPointer;
                lowerPointer = nextPointer;
                nextPointer = nextPointer.nextNode;

                originalLower = lowerIndex;
                originalHigher = higherIndex;
                lowerIndex = sumRange / 2;
            }
        }

        if(lowerPointerIsCloser(index, lowerIndex, higherIndex)){
            return new RemoveObject<>(lowerPointer, parent, originalLower, originalHigher, lowerIndex, higherIndex, true);
        } else{
            return new RemoveObject<>(higherPointer, parent, originalLower, originalHigher, lowerIndex, higherIndex, false);
        }
    }

    private RemoveObject<T> getNodeDetails(int index){
        RemoveObject<T> returnObject = getClosestNodeDetails(index);

        Node<T> closestNode = returnObject.node;
        boolean isLowerClosest = returnObject.isLowerClosest;

        if(isLowerClosest){
            int closestIndex = returnObject.lowerIndex;
            for(int i = closestIndex; i < index; i++){
                closestNode = closestNode.next;
            }
        }else{
            int closestIndex = returnObject.higherIndex;
            for(int i = closestIndex; i > index; i--){
                closestNode = closestNode.prev;
            }
        }

        returnObject.node = closestNode;

        return returnObject;
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

        // recursions
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
        //System.out.println("c" + pointer.item + " " + closestIndex + " " + isLowerClosest);
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
    // 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
    // gets the closest node pivot pointer from a given index
    private ReturnObject<T> getClosest(int index){
        Node<T> lowerPointer = this.head;
        Node<T> higherPointer = this.tail;
        Node<T> nextPointer = lowerPointer.nextNode;
        int lowerIndex = 0;
        int higherIndex = this.size-1;

        while(isInRange(index, lowerIndex, higherIndex) && nextPointer != null){
            int sumRange = lowerIndex + higherIndex;

            if(lowerPointerIsCloser(index, lowerIndex, higherIndex)){
                higherPointer = nextPointer;
                nextPointer = nextPointer.prevNode;

                higherIndex = sumRange / 2;

                if(index == lowerIndex) {
                    return new ReturnObject<>(lowerPointer, lowerIndex, true);
                }
            }else{
                lowerPointer = nextPointer;
                nextPointer = nextPointer.nextNode;

                lowerIndex = sumRange / 2;

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

    // add isEmpty function

}