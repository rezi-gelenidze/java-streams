import collection.List;
import function.*;
import stream.Stream;

public class Main {
    public static void main(String[] args) {
        Function<String, Integer> a = Integer::parseInt;
        Function<Integer, Double> b = i -> (double) (i / 2);

        Stream.of(new String[]{"1", "2", "3", "4"})
                .map(b.compose(a))
                .forEach(System.out::println);
    }
}