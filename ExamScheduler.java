import java.util.*;

class ExamScheduler {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of subjects: ");
        int n = sc.nextInt();
        sc.nextLine();

        if (n <= 0) {
            System.out.println("Invalid number of subjects.");
            return;
        }

        String[] subjects = new String[n];
        int[][] graph = new int[n][n];

        // Input subjects
        for (int i = 0; i < n; i++) {
            System.out.print("Enter subject " + (i + 1) + ": ");
            subjects[i] = sc.nextLine();
        }

        // Input conflicts
        System.out.println("\nEnter conflicts (1 = both taken, 0 = no):");

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                System.out.print("Do students take BOTH " + subjects[i] + " and " + subjects[j] + "? (1/0): ");
                int val = sc.nextInt();
                graph[i][j] = val;
                graph[j][i] = val;
            }
        }

        // STEP 1: calculate degree (number of conflicts)
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) -> {
            int degA = 0, degB = 0;
            for (int k = 0; k < n; k++) {
                if (graph[a][k] == 1) degA++;
                if (graph[b][k] == 1) degB++;
            }
            return degB - degA; // descending order
        });

        int[] result = new int[n];
        Arrays.fill(result, -1);

        boolean[] available = new boolean[n];
        Arrays.fill(available, true);

        // STEP 2: greedy coloring with ordering
        for (int idx = 0; idx < n; idx++) {

            int u = order[idx];

            for (int i = 0; i < n; i++) {
                if (graph[u][i] == 1 && result[i] != -1) {
                    available[result[i]] = false;
                }
            }

            int slot;
            for (slot = 0; slot < n; slot++) {
                if (available[slot]) break;
            }

            result[u] = slot;
            Arrays.fill(available, true);
        }

        // OUTPUT
        System.out.println("\n===== FINAL EXAM TIMETABLE =====");

        int maxSlot = 0;
        for (int i = 0; i < n; i++) {
            if (result[i] > maxSlot) maxSlot = result[i];
        }

        for (int slot = 0; slot <= maxSlot; slot++) {
            System.out.println("\nTime Slot " + (slot + 1) + ":");
            for (int i = 0; i < n; i++) {
                if (result[i] == slot) {
                    System.out.println(" - " + subjects[i]);
                }
            }
        }

        System.out.println("\nTotal Time Slots Used (Minimum Required): " + (maxSlot + 1));

        sc.close();
    }
}