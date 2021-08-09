class TreeMap(object):
    def __init__(self):
        self.num = 0
        self.root = None
        self.stop = False

    def size(self):
        return self.num

    def isEmpty(self):
        return self.num == 0

    def clear(self):
        self.root = None
        self.num = 0

    def put(self, key, value):
        self._nullCheck(key)

        if self.root is None:
            self.root = self.Node(key, value)
            self.num = 1
            return None

        parent = self.root
        node = self.root

        while node is not None:
            cmp = self._cmp(key, node.key)
            parent = node
            if cmp > 0:
                node = node.right
            elif cmp < 0:
                node = node.left
            else:
                node.key = key
                old = node.value
                node.value = value
                return old

        newNode = self.Node(key, value, parent)
        if cmp > 0:
            parent.right = newNode
        else:
            parent.left = newNode
        self.num += 1

    def get(self, key):
        node = self._node(key)
        return node.value if node is not None else None

    def remove(self, key):
        node = self._node(key)
        if node is None:
            return
        self._remove(node)

    def containsKey(self, key):
        return self._node(key) is not None

    def containsValue(self, value):
        if self.root is None:
            return False
        self._inorderTraversalReturn(self.root, value)
        if self.stop:
            self.stop = False
            return True
        else:
            return False

    def traversal(self, visitor):
        if visitor is None:
            return
        self._inorderTraversal(self.root, visitor)

    # 中序遍历(containsValue时用)
    def _inorderTraversalReturn(self, root, value):
        if root is None or self.stop:
            return
        self._inorderTraversalReturn(root.left, value)
        if root.value == value:
            self.stop = True
        self._inorderTraversalReturn(root.right, value)

    # 中序遍历
    def _inorderTraversal(self, root, visitor):
        if root is None or visitor.stop:
            return
        self._inorderTraversal(root.left, visitor)
        if visitor.stop:
            return
        visitor.stop = visitor.visit(root.key, root.value)
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
            node.key = predecessor.key
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
    def _nullCheck(self, key):
        if key is None:
            raise SystemExit('It failed!')

    # 根据key查找节点
    def _node(self, key):
        self._nullCheck(key)
        node = self.root
        while node is not None:
            cmp = self._cmp(key, node.key)
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
        def __init__(self, key, value, parent=None, right=None, left=None):
            self.key = key
            self.value = value
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

        def visit(self, key, value):
            # 遇到e时停止
            # if key == 'e':
            #     return True
            print(str(key)+':'+str(value))
            return False

    # 添加对应元素
    Map = TreeMap()
    Map.put('c', 1)
    Map.put('a', 3)
    Map.put('b', 7)
    Map.put('e', 2)
    Map.put('b', 8)

    # 删除元素
    Map.remove('b')

    # 分别测试size、isEmpty、get、containsKey、containsValue
    print('size : ', Map.size())
    print('isEmpty : ', Map.isEmpty())
    print('get value \"e\" : ', Map.get('e'))
    print('contains Key \"a\" : ', Map.containsKey('a'))
    print('contains Value \"20\" : ', Map.containsValue(20))

    # 测试遍历元素
    print('\ntraversal:')
    Vis = Visitor()
    Map.traversal(Vis)
