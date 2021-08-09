public interface Set<E> {
    int size();
    boolean isEmpty();
    void clear();
    void add(E element);
    void remove(E element);
    boolean contains(E element);
    /** 遍历所有元素（必须按照元素从小到大的顺序遍历） */
    void traversal(Visitor<E> visitor);
    /** Visitor结构 */
    public static abstract class Visitor<E> {
        boolean stop;
        public abstract boolean visit(E element);
    }
}
