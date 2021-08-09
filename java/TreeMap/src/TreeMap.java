import printer.BinaryTreeInfo;
import java.util.LinkedList;
import java.util.Queue;


public class TreeMap<K extends Comparable<K>, V> implements Map<K, V>, BinaryTreeInfo {
    private int size;
    private Node<K, V> root;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        nullCheck(key);
        if (root == null) {
            root = new Node<>(key, value, null);
            size = 1;
            return null;
        }
        Node<K, V> parent;
        Node<K, V> node = root;
        int cmp;
        do {
            parent = node;
            cmp = key.compareTo(node.key);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        } while (node != null);
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        Node<K, V> node = node(key);
        if (node == null) return null;
        return remove(node);
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (root == null) return false;
        Queue<Node<K, V>> queue = new LinkedList<>();
        queue.offer(root);
        // 用层序遍历
        while (!queue.isEmpty()) {
            Node<K, V> node = queue.poll();
            if (value.equals(node.value)) return true;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        traversal(root, visitor);
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<K, V>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<K, V>) node).right;
    }

    @Override
    public Object string(Object node) {
        return ((Node<K, V>) node).key;
    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node == null || visitor.stop) return;
        traversal(node.left, visitor);
        if (visitor.stop) return;
        visitor.stop = visitor.visit(node.key, node.value);
        traversal(node.right, visitor);

    }

    private Node<K, V> predecessor(Node<K, V> node) {
        Node<K, V> cur = node.left;
        if (cur != null) {
            while (cur.right != null) {
                cur = cur.right;
            }
            return cur;
        }
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        return node.parent;
    }

    private V remove(Node<K, V> node) {
        size--;
        V oldValue = node.value;
        int degree = node.degree();
        if (degree == 0) {
            if (node == root) {
                root = null;
            } else if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
        } else if (degree == 1) {
            Node<K, V> child = (node.left != null) ? node.left : node.right;
            if (node == root) {
                root = child;
                root.parent = null;
            } else {
                child.parent = node.parent;
                if (node == node.parent.left) {
                    node.parent.left = child;
                } else {
                    node.parent.right = child;
                }
            }
        } else {
            Node<K, V> predecessor = predecessor(node);
            node.key = predecessor.key;
            node.value = predecessor.value;
            remove(predecessor);
        }
        return oldValue;
    }

    private void nullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("元素不能为null");
        }
    }

    private Node<K, V> node(K key) {
        nullCheck(key);
        Node<K, V> node = root;
        do {
            int cmp = key.compareTo(node.key);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                return node;
            }
        } while (node != null);
        return null;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        int degree() {
            int ret = 0;
            if (left != null) ret++;
            if (right != null) ret++;
            return ret;
        }
    }
}
