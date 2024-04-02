import java.util.*;
import java.util.stream.Collectors;

class Main {
    private static void printMaxKey(HashMap<Integer, Integer> map) {
        // implement me
        List<Integer> keys = map.keySet().stream().sorted(Comparator.reverseOrder()).toList();
        System.out.printf(String.valueOf(map.get(keys.get(0))));
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; ++i) {
            map.put(scanner.nextInt(), scanner.nextInt());
        }

        printMaxKey(map);
    }
}