package stream;
import util.Optional;

public interface Stream<T> {
    Pair<T> eval();

    default Optional<T> findFirst() {
        Pair<T> pair = eval();
        if (pair == null) return Optional.empty(); // empty case
        return Optional.of(pair.value); // create an optional of pair's value
    }

    class Pair<T> {
        private T value;
        private Stream<T> rest;
        public Pair(T value, Stream<T> rest) {
            this.value = value; this.rest = rest;
        }

        public T getValue() {
            return this.value;
        }

        public Stream<T> getRest() {
            return this.rest;
        }
    }

    static <T> Stream<T> empty() {
        return () -> null;
    }

    static <T> Stream<T> of(T x) {
        return () -> new Pair<>(x,empty());
    }

    static <T> Stream<T> of(T[] args) {
        class State {
            int count = 0;
            Stream<T> of() {
                if (count==args.length) return empty();
                final T value = args[count++];
                return () -> new Pair<T>(value, of());
            }
        }
        return new State().of();
    }
}