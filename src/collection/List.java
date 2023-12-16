package collection;

import iterator.Iterable;
import iterator.Iterator;
import stream.Stream;

import java.util.NoSuchElementException;

public class List<T> implements Iterable<T> {
    public T info;
    public List<T> next;

    public List(T x, List<T> l) {
        info = x;
        next = l;
    }

    public List(T x) {
        info = x;
        next = null;
    }

    //public methods
    public void insert(T x) {
        next = new List<T>(x);
    }

    public void delete() {
        if (next != null) {
            next = next.next;
        }
    }

    public static boolean isEmpty(List l) {
        return l == null;
    }

    public Stream<T> stream() {
        class State {
            Iterator<T> it = iterator();

            Stream<T> stream() {
                return () -> (it.hasNext()) ?
                        new Stream.Pair<T>(it.next(), State.this.stream()) :
                        null;
            }
        }
        return new State().stream();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private List<T> current = List.this;

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T val = current.info;
                current = current.next;
                return val;
            }
        };
    }
}