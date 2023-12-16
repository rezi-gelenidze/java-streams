import collection.List;
import function.Consumer;
import function.Predicate;
import stream.Stream;

public class Main {
    public static void main(String[] args) {
        Function<String, Integer>
        Stream.of(new String[]{"1 ", "2", "3", " 4"})
                .map(String::trim)
                .map(Integer::parseInt)
                .map((a) -> 2 * a)
                .forEach(System.out::println);
    }
}