package stream;

import function.*;
import iterator.*;
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

    default Optional<T> reduce(BinaryOperator<T> accumulator) {
        // reducer with first element as initial value. returns Optional
        Iterator<T> it = iterator();
        if (!it.hasNext()) return Optional.empty();

        T result = it.next();
        while (it.hasNext())
            result = accumulator.apply(result, it.next());

        return Optional.of(result);
    }

    default <U> U reduce (U identity, BiFunction<U,? super T,U> accumulator) {
        // reducer with base identity (initial type U value)
        Iterator<T> it = this.iterator();
        U result = identity;

        if (it.hasNext()) result = accumulator.apply(result, it.next());
        while (it.hasNext())
            result = accumulator.apply(result, it.next());

        return result;
    }

    default <R> R collect (Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner) {
        Iterator<T> it = this.iterator();
        R result = supplier.get();

        if (it.hasNext()) accumulator.accept(result, it.next());
        while(it.hasNext())
            accumulator.accept(result, it.next());

        return result;
    }

    default Stream<T> concat(Stream<? extends T> after) {
        return () -> {
            Pair<T> pair = eval();
            if (pair == null) return (Pair<T>) after.eval();

            return new Pair<T>(
                    pair.value,
                    pair.getRest().concat(after)
            );
        };
    }

    default <S> Stream<S> flatMap(Function<T, Stream<S>> f) {
        return flatten(this.map(f));
    }

    default Iterator<T> iterator(){
        return new Iterator<T>(){
            private Pair<T> pair = Stream.this.eval();

            @Override
            public boolean hasNext() {
                return (pair != null);
            }

            @Override
            public T next() {
                T result = pair.value;
                pair = pair.getRest().eval();
                return result;
            }
        };
    }

    static <T> Stream<T> flatten(Stream<Stream<T>> ss) {
        return () -> {
          Pair<Stream<T>> streamPair = ss.eval();

          if (streamPair == null) return null;
          if (streamPair.value == null)
              return flatten(streamPair.rest).eval();

          Pair<T> tPair = streamPair.value.eval();
          if (tPair == null) return flatten(streamPair.rest).eval();

          return new Pair<T> (
                  tPair.value,
                  tPair.rest.concat(flatten(streamPair.rest))
          );

        };
    }

    static <T> Stream<T> empty() {
        return () -> null;
    }

    static <T> Stream<T> of(T x) {
        return () -> new Pair<>(x, empty());
    }

    @SafeVarargs
    static <T> Stream<T> of(T... args) {
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
