import printer.BinaryTrees;


public class Main {
    public static void main(String[] args) {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("d", 2);
        map.put("a", 5);
        map.put("a", 2);
        map.put("b", 6);
        map.put("f", 8);
        map.put("t", 8);

        // 分别测试size、isEmpty、get、containsKey、containsValue
        System.out.println("size : "+map.size());
        System.out.println("isempty : "+map.isEmpty());
        System.out.println("get value \"a\" : "+map.get("a"));
        System.out.println("containsKey \"t\" : "+map.containsKey("t"));
        System.out.println("containsValue \"100\" : "+map.containsValue(100));

        // 测试traversal，按从小到大输出键值对
        System.out.println("\n traversal:");
        map.traversal(new Map.Visitor<String, Integer>() {
            @Override
            public boolean visit(String key, Integer value) {
                System.out.println(key+":"+value+"");
                return false;
            }
        });

        // 测试remove函数
        System.out.println("-----------------------------");
        BinaryTrees.println(map);

        System.out.println("-----------------------------");
        map.remove("a");
        BinaryTrees.println(map);

        // 测试clear函数
        map.clear();
    }
}
