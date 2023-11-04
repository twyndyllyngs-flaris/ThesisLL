import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        DynamicArray<Integer> a = new DynamicArray<>();

        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        a.add(5);
        a.add(6);
        a.add(7);
        a.add(8);

        a.delete(3);
        System.out.println(a.get(3));

        System.out.println(a.size());
        a.print();
    }
}

