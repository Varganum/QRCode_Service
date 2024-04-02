import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Main {
    private static void printCommon(int[] firstArray, int[] secondArray) {
        // Enter your Code Here
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int n : firstArray) {
            hashMap.put(n, 0);
        }
        for (int m : secondArray) {
            if (hashMap.containsKey(m)) {
                hashMap.put(m, 1);
            }
        }
        List<Integer> resultList = hashMap.keySet().stream().filter(a -> hashMap.get(a) != 0).sorted().toList();
        for (int i : resultList) {
            System.out.print(i + " ");
        }
    }

    public static void main(String[] args) {        
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] firstArray = new int[n];
        int[] secondArray = new int[n];
        for (int i = 0; i < n; ++i) { 
            firstArray[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; ++i) { 
            secondArray[i] = scanner.nextInt();
        }

        printCommon(firstArray, secondArray);
    }
}