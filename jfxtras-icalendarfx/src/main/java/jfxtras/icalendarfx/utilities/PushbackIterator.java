package jfxtras.icalendarfx.utilities;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Decorates an iterator to support pushback of elements.
 * <p>
 * The decorator stores the pushed back elements in a LIFO manner: the last element
 * that has been pushed back, will be returned as the next element in a call to {@link #next()}.
 * <p>
 * The decorator does not support the removal operation. Any call to {@link #remove()} will
 * result in an {@link UnsupportedOperationException}.
 *
 * @since 4.0
 * @version $Id: PushbackIterator.java.html 972397 2015-11-14 15:01:49Z tn $
 */
public class PushbackIterator<E> implements Iterator<E> {

    /** The iterator being decorated. */
    private final Iterator<? extends E> iterator;

    /** The LIFO queue containing the pushed back items. */
    private Deque<E> items = new ArrayDeque<E>();

    //-----------------------------------------------------------------------
    /**
     * Decorates the specified iterator to support one-element lookahead.
     * <p>
     * If the iterator is already a {@link PushbackIterator} it is returned directly.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to decorate
     * @return a new peeking iterator
     * @throws NullPointerException if the iterator is null
     */
    public static <E> PushbackIterator<E> pushbackIterator(final Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        if (iterator instanceof PushbackIterator<?>) {
            @SuppressWarnings("unchecked") // safe cast
            final PushbackIterator<E> it = (PushbackIterator<E>) iterator;
            return it;
        }
        return new PushbackIterator<E>(iterator);
    }

    //-----------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param iterator  the iterator to decorate
     */
    public PushbackIterator(final Iterator<? extends E> iterator) {
        super();
        this.iterator = iterator;
    }

    /**
     * Push back the given element to the iterator.
     * <p>
     * Calling {@link #next()} immediately afterwards will return exactly this element.
     *
     * @param item  the element to push back to the iterator
     */
    public void pushback(final E item) {
        items.push(item);
    }

    @Override
    public boolean hasNext() {
        return !items.isEmpty() ? true : iterator.hasNext();
    }

    @Override
    public E next() {
        return !items.isEmpty() ? items.pop() : iterator.next();
    }

    /**
     * This iterator will always throw an {@link UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
