import java.lang.reflect.Array;

public class DynamicArray<T>{
    int boundaryPercent = 75;
    int initialSize;
    T[] array;

    DynamicArray(int size){
        this.array = (T[]) new Object[size];
        this.initialSize = size;
    }

    DynamicArray(){
        this.array = (T[]) new Object[5];
        this.initialSize = 5;
    }

    public void add(T item){

    }
}
