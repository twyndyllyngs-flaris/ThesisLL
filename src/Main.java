public class Main {
    public static void main(String[] args) {
        NormalList<Integer> list = new NormalList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        list.print();
        System.out.println("size: " + list.size());

        System.out.println("removed: " + list.remove(2));

        list.print();
        System.out.println("size: " + list.size());

        System.out.println(list.pop());
        list.pop();
        list.pop();
        list.pop();

        list.print();
        System.out.println("size: " + list.size());
    }
}

