package function;

public interface BiConsumer<T, U> {
    void accept(T var1, U var2);

    default java.util.function.BiConsumer<T, U> andThen(java.util.function.BiConsumer<? super T, ? super U> after) {
        return (l, r) -> {
            this.accept(l, r);
            after.accept(l, r);
        };
    }
}