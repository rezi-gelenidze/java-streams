package function;

public interface Predicate<T> {
    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        return (t) -> this.test(t) && other.test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        return (t) -> this.test(t) || other.test(t);
    }

    default Predicate<T> negate() {
        return (t) -> !this.test(t);
    }
}
