import printer.BinaryTreeInfo;


public class TreeSet<E extends Comparable<E>> implements Set<E>, BinaryTreeInfo  {
    private int size;
    private Node<E> root;

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
    public void add(E element) {
        nullCheck(element);
        // 添加第一个节点
        if (root == null) {
            root = new Node<>(element);
            size = 1;
            return;
        }
        // 父节点
        Node<E> parent = root;
        // 当前跟element进行比较的节点
        Node<E> node = root;
        // 查找父节点
        int cmp;
        do {
            // 假设node就是父节点
            parent = node;
            cmp = element.compareTo(node.element);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else { // 相等
                node.element = element;
                // 直接返回
                return;
            }
        } while (node != null);
        // 创建新节点
        Node<E> newNode = new Node<>(element, parent);
        if (cmp > 0) { // 新添加的元素比父节点的元素大
            parent.right = newNode;
        } else { // 新添加的元素比父节点的元素小
            parent.left = newNode;
        }
        // 增加元素数量
        size++;
    }

    @Override
    public void remove(E element) {
        // 找到对应的节点
        Node<E> node = node(element);
        if (node == null) return;
        remove(node);
    }

    @Override
    public boolean contains(E element) {
        return node(element) != null;
    }

    @Override
    public void traversal(Set.Visitor<E> visitor) {
        if (visitor == null) return;
        inorderTraversal(root, visitor);
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        return ((Node<E>) node).element;
    }

    private void remove(Node<E> node) {
        // 元素数量减少
        size--;
        int degree = node.degree();
        if (degree == 0) { // 删除度为0的节点（叶子节点）
            if (node == root) { // node是根节点
                root = null;
            } else if (node == node.parent.left) { // node是左子节点
                node.parent.left = null;
            } else { // node是右子节点
                node.parent.right = null;
            }
        } else if (degree == 1) { // 删除度为1的节点
            Node<E> child = (node.left != null) ? node.left : node.right;
            if (node == root) { // node是根节点
                root = child;
                root.parent = null;
            } else {
                child.parent = node.parent;
                if (node == node.parent.left) { // node是左子节点
                    node.parent.left = child;
                } else { // node是右子节点
                    node.parent.right = child;
                }
            }
        } else { // 删除度为2的节点
            // 找到前驱节点
            Node<E> predecessor = predecessor(node);
            // 用前驱节点的值覆盖node节点的值
            node.element = predecessor.element;
            // 删除前驱节点
            remove(predecessor);
        }
    }

    private Node<E> predecessor(Node<E> node) {
        Node<E> cur = node.left;
        if (cur != null) { // 左子节点不为null
            while (cur.right != null) {
                cur = cur.right;
            }
            return cur;
        }
        // 从父节点、祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        return node.parent;
    }

    private void inorderTraversal(Node<E> root, Visitor<E> visitor) {
        // 递归基：递归的退出条件
        if (root == null || visitor.stop) return;

        // 中序遍历左子树
        inorderTraversal(root.left, visitor);
        if (visitor.stop) return;
        // 访问根节点
        visitor.stop = visitor.visit(root.element);
        // 中序遍历右子树
        inorderTraversal(root.right, visitor);
    }

    private Node<E> node(E element) {
        nullCheck(element);
        Node<E> node = root;
        do {
            int cmp = element.compareTo(node.element);
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

    private void nullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("元素不能为null");
        }
    }

    private static class Node<E> {
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        E element;
        Node(E element) {
            this.element = element;
        }
        Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }
        Node(E element, Node<E> left, Node<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
        int degree() {
            int ret = 0;
            if (left != null) ret++;
            if (right != null) ret++;
            return ret;
        }
    }
}
