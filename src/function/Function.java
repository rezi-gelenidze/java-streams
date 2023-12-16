package function;

public interface Function<T,R> {
    R apply (T arg);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before){
        return (V v) -> this.apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return (T t) -> after.apply(this.apply(t));
    }
}