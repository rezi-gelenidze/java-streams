package stream;

import function.*;
import util.*;

public interface Stream<T> {
    Pair<T> eval();

    class Pair<T> {
        private T value;
        private Stream<T> rest;

        public Pair(T value, Stream<T> rest) {
            this.value = value;
            this.rest = rest;
        }

        public T getValue() {
            return this.value;
        }

        public Stream<T> getRest() {
            return this.rest;
        }
    }

    default Optional<T> findFirst() {
        Pair<T> pair = eval();
        if (pair == null) return Optional.empty(); // empty case
        return Optional.of(pair.value); // create an optional of pair's value
    }

    default void forEach(Consumer<? super T> action) {
        for(Pair<T> pair = eval(); pair != null; pair = pair.getRest().eval()) {
            action.accept(pair.value);
        }
    }

    default Stream<T> filter(Predicate<? super T> p) {
        return () -> {
            Pair<T> pair = eval();

            if (pair == null) return null;

            if(p.test(pair.value))
                return new Pair<T>(pair.value, pair.rest.filter(p));

            return pair.rest.filter(p).eval();
        };
    }

    default <S> Stream<S> map(Function<T, S> f) {
        return () -> {
            Pair<T> pair = eval();

            if(pair == null) return null;
            return new Pair<S>(
                    f.apply(pair.value),
                    pair.getRest().map(f)
            );
        };
    }

    static <T> Stream<T> empty() {
        return () -> null;
    }

    static <T> Stream<T> of(T x) {
        return () -> new Pair<>(x, empty());
    }

    static <T> Stream<T> of(T[] args) {
        class State {
            int count = 0;

            Stream<T> of() {
                if (count == args.length) return empty();
                final T value = args[count++];
                return () -> new Pair<T>(value, of());
            }
        }
        return new State().of();
    }
}
