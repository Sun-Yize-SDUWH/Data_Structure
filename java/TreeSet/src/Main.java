import printer.BinaryTrees;


public class Main {
    public static void main(String[] args) {
        TreeSet<Integer> ts = new TreeSet<>();
        ts.add(7);
        ts.add(4);
        ts.add(2);
        ts.add(1);
        ts.add(3);
        ts.add(5);
        ts.add(9);
        ts.add(8);
        ts.add(11);
        ts.add(10);
        ts.add(12);

        // 分别测试size、isEmpty、get、containsKey、containsValue
        System.out.println("size : "+ts.size());
        System.out.println("isempty : "+ts.isEmpty());
        System.out.println("contains value \"8\" : "+ts.contains(8));

        // 测试traversal，按从小到大输出键值对
        System.out.println("\n traversal:");
        ts.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element+"__");
                return false;
            }
        });


        // 测试remove函数
        System.out.println("-----------------------------");
        BinaryTrees.println(ts);

        System.out.println("-----------------------------");
        ts.remove(7);
        BinaryTrees.println(ts);

        // 测试clear函数
        ts.clear();
    }
}
