import java.util.HashMap;
import java.util.Scanner;

class Main {
    private static void printMode(int[] map) {
        // Enter your Code Here
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        int newValue;
        int mode = 0;
        int currentModeValue = 0;

        for (int n : map) {
            if (hashMap.containsKey(n)) {
                newValue = hashMap.get(n) + 1;
                hashMap.put(n, newValue);
            } else {
                hashMap.put(n, 1);
            }
            if (hashMap.get(n) > currentModeValue) {
                mode = n;
                currentModeValue = hashMap.get(n);
            }
        }

        System.out.println(mode + " " + currentModeValue);

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] map = new int[n];
        for (int i = 0; i < n; ++i) { 
            map[i] = scanner.nextInt();
        }

        printMode(map);
    }
}