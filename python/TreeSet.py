class TreeSet(object):
    def __init__(self):
        self.num = 0
        self.root = None

    def size(self):
        return self.num

    def isEmpty(self):
        return self.num == 0

    def clear(self):
        self.root = None
        self.num = 0

    def add(self, element):
        self._nullCheck(element)

        if self.root is None:
            self.root = self.Node(element)
            self.num = 1
            return

        parent = self.root
        node = self.root

        while node is not None:
            cmp = self._cmp(element, node.element)
            parent = node
            if cmp > 0:
                node = node.right
            elif cmp < 0:
                node = node.left
            else:
                node.element = element
                return

        newNode = self.Node(element, parent)
        if cmp > 0:
            parent.right = newNode
        else:
            parent.left = newNode
        self.num += 1

    def remove(self, element):
        node = self._node(element)
        if node is None:
            return
        self._remove(node)

    def contains(self, element):
        return self._node(element) is not None

    def traversal(self, visitor):
        if visitor is None:
            return
        self._inorderTraversal(self.root, visitor)

    # 中序遍历
    def _inorderTraversal(self, root, visitor):
        if root is None or visitor.stop:
            return
        self._inorderTraversal(root.left, visitor)
        if visitor.stop:
            return
        visitor.stop = visitor.visit(root.element)
        self._inorderTraversal(root.right, visitor)

    # 删除对应节点
    def _remove(self, node):
        self.num -= 1

        degree = node.degree()
        if degree == 0:
            if node == self.root:
                self.root = None
            elif node == node.parent.left:
                node.parent.left = None
            else:
                node.parent.right = None
        elif degree == 1:
            child = node.left if node.left is not None else node.right
            if node == self.root:
                self.root = child
                self.root.parent = None
            else:
                child.parent = node.parent
                if node == node.parent.left:
                    node.parent.left = child
                else:
                    node.parent.right = child
        else:
            predecessor = self._predecessor(node)
            node.element = predecessor.element
            self._remove(predecessor)

    # 查找前驱节点
    def _predecessor(self, node):
        cur = node.left
        if cur is not None:
            while cur.right is not None:
                cur = cur.right
            return cur
        while node.parent is not None and node == node.parent.left:
            node = node.parent
        return node.parent

    # 检查是否为空
    def _nullCheck(self, element):
        if element is None:
            raise SystemExit('It failed!')

    # 查找节点
    def _node(self, element):
        self._nullCheck(element)
        node = self.root
        while node is not None:
            cmp = self._cmp(element, node.element)
            if cmp > 0:
                node = node.right
            elif cmp < 0:
                node = node.left
            else:
                return node
        return None

    # 比较函数
    def _cmp(self, a, b):
        if a == b:
            return 0
        elif a < b:
            return -1
        else:
            return 1

    # 定义node结构
    class Node(object):
        def __init__(self, element, parent=None, right=None, left=None):
            self.element = element
            self.right = right
            self.left = left
            self.parent = parent

        # 返回对应度数
        def degree(self):
            ret = 0
            if self.left is not None:
                ret += 1
            if self.right is not None:
                ret += 1
            return ret


if __name__ == "__main__":
    # 定义Visitor结构
    class Visitor:
        def __init__(self):
            self.stop = False

        def visit(self, key):
            # 遇到1时停止
            # if key == 1:
            #     return True
            print(str(key))
            return False

    # 添加对应元素
    Set = TreeSet()
    Set.add(5)
    Set.add(1)
    Set.add(3)
    Set.add(12)
    Set.add(1)
    Set.add(2)

    # 删除元素
    Set.remove(12)

    # 分别测试size、isEmpty、get、containsKey、containsValue
    print('size : ', Set.size())
    print('isEmpty : ', Set.isEmpty())
    print('contains \"1\" : ', Set.contains(1))

    # 测试遍历元素
    print('\ntraversal:')
    Vis = Visitor()
    Set.traversal(Vis)
