package function;

public interface Function<T,R> {
    R apply (T arg);

//    default Function<T, R> compose(Function<? super T, ? extends R> before){
//        return (R t) -> this.apply(before.apply(t));
//    }
//
//    default Function<T, R> andThen(Function<? super T, ? extends R> after) {
//        return (T t) -> after.apply(this.apply(t));
//    }
}