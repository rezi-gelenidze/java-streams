import collection.List;
import stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Integer> l = new List<>(5);
        l.next = new List<Integer>(10);
        l.next.next = new List<Integer>(12);
        l.next.next.next = new List<Integer>(16);
        l.next.next.next.next = new List<Integer>(125);

        System.out.println(l.stream().findFirst().orElse(null));;
    }
}
