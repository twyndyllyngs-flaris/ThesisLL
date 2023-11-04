import java.util.EmptyStackException;

public class DynamicArray<T>{

    //int boundaryPercent = 75;
    private int capacity;
    private int size = 0;
    private T[] array;

    DynamicArray(int size){
        this.array = (T[]) new Object[size];
        this.capacity = size;
    }

    DynamicArray(){
        this.array = (T[]) new Object[5];
        this.capacity = 5;
    }

    public void print(){
        String str = "[";

        for(int i = 0; i < this.size; i++){
            str += this.array[i];

            if(i != this.size -1){
                str += ", ";
            }
        }

        str += "]";

        System.out.println(str);
    }

    public int size(){
        return this.size;
    }

    public void add(T item){
        if (isEmpty()){
            this.array[0] = item;
            this.size += 1;
            return;
        }

        addAfter(this.size - 1, item);
        this.size += 1;

        if(isFull()){
            enlargeArray();
        }
    }

    public T get(int index){
        if(isEmpty()){
            throw new EmptyStackException();
        }

        if(index < 0 || index > size - 1){
            throw new IndexOutOfBoundsException();
        }

        return this.array[index];
    }

    public void delete(int index){
        if(isEmpty()){
            throw new EmptyStackException();
        }

        if(index < 0 || index > size - 1){
            throw new IndexOutOfBoundsException();
        }

        moveItemsLeft(index+1);
        this.size -= 1;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    private void enlargeArray(){
        this.capacity += this.capacity / 2;
        T[] newArray = (T[]) new Object[this.capacity];
        transferItems(newArray, this.array);
        this.array = newArray;
    }

    private void transferItems(T[] newArray, T[] oldArray){
        for(int i = 0; i < oldArray.length; i++){
            newArray[i] = oldArray[i];
        }
    }

    private boolean isFull(){
        return this.array.length == this.size;
    }

    private void addAfter(int index, T item){
        if(isEmpty()){
            throw new EmptyStackException();
        }

        if(index < 0 || index > size - 1){
            throw new IndexOutOfBoundsException();
        }

        moveItemsRight(index+1);
        this.array[index+1] = item;
    }

    private void moveItemsLeft(int from){
        for(int i = from; i < this.size; i++){
            this.array[i-1] = this.array[i];
        }
    }

    // move all elements to its right side by one index
    private void moveItemsRight(int from){
        for(int i = this.size; i > from; i--){
            this.array[i] = this.array[i-1];
        }
    }
}
