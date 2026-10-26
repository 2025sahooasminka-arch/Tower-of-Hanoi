import java.util.Scanner;

/**
 * Tower of Hanoi using Recursion and Stack
 * Data Structures & Algorithms (DSA) Mini-Project
 *
 * Course: Data Structures and Algorithms
 * Language: Java (Console-Based Application)
 */

/**
 * CustomStack represents a custom LIFO (Last-In-First-Out) stack data structure
 * implemented using a primitive fixed-size integer array.
 * Each stack instance represents one peg/rod in the Tower of Hanoi puzzle.
 */
class CustomStack {
    private final int capacity;     // Maximum number of disks the rod can hold
    private final int[] elements;    // Array to store disks (smaller number = smaller disk)
    private int top;                 // Index of top element (-1 when empty)
    private final String rodName;    // Label for the rod (e.g., "Rod A")

    /**
     * Constructor to initialize custom stack with a specified capacity and rod name.
     *
     * @param rodName Name/label of the rod
     * @param capacity Maximum number of disks allowed
     */
    public CustomStack(String rodName, int capacity) {
        this.rodName = rodName;
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.top = -1; // -1 represents an empty stack
    }

    /**
     * Pushes a disk onto the top of the stack.
     *
     * @param disk The disk number to push (1 is smallest disk, n is largest)
     */
    public void push(int disk) {
        if (isFull()) {
            System.err.println("Error: Stack Overflow on " + rodName);
            return;
        }
        elements[++top] = disk;
    }

    /**
     * Pops and returns the top disk from the stack.
     *
     * @return The removed top disk, or -1 if stack is empty (underflow)
     */
    public int pop() {
        if (isEmpty()) {
            System.err.println("Error: Stack Underflow on " + rodName);
            return -1;
        }
        return elements[top--];
    }

    /**
     * Peeks at the top disk of the stack without removing it.
     *
     * @return The top disk, or -1 if the stack is empty
     */
    public int peek() {
        if (isEmpty()) {
            return -1;
        }
        return elements[top];
    }

    /**
     * Checks whether the stack has no disks.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return (top == -1);
    }

    /**
     * Checks whether the stack has reached its maximum capacity.
     *
     * @return true if full, false otherwise
     */
    public boolean isFull() {
        return (top == capacity - 1);
    }

    /**
     * Returns the current number of disks on the rod.
     *
     * @return Current disk count
     */
    public int size() {
        return top + 1;
    }

    /**
     * Gets the rod name.
     *
     * @return Rod name string
     */
    public String getRodName() {
        return rodName;
    }

    /**
     * Returns a formatted string representing the disks on this rod from bottom to top.
     * Example: "[3, 2, 1]" means disk 3 is at the bottom and disk 1 is at the top.
     *
     * @return String representation of disks
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= top; i++) {
            sb.append(elements[i]);
            if (i < top) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

/**
 * Main application class containing the interactive console menu,
 * recursive solver, iterative solver using stacks, and input validation.
 */
public class Main {

    // Counter to track the sequence of moves made in each game session
    private static int moveCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("============================================================");
        System.out.println("         WELCOME TO TOWER OF HANOI DSA MINI-PROJECT         ");
        System.out.println("               (Recursion vs Iterative Stack)               ");
        System.out.println("============================================================");

        while (running) {
            displayMenu();
            int choice = getValidMenuChoice(scanner);

            switch (choice) {
                case 1:
                    int recursiveDisks = getValidDiskCount(scanner);
                    runRecursiveSolver(recursiveDisks);
                    break;

                case 2:
                    int iterativeDisks = getValidDiskCount(scanner);
                    runIterativeSolver(iterativeDisks);
                    break;

                case 3:
                    System.out.println("\nExiting program. Thank you for using Tower of Hanoi Solver!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid selection. Please choose an option between 1 and 3.");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu choices to the user.
     */
    private static void displayMenu() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("                         MAIN MENU                          ");
        System.out.println("------------------------------------------------------------");
        System.out.println("1. Solve using recursion");
        System.out.println("2. Solve using stacks (iterative)");
        System.out.println("3. Exit");
        System.out.println("------------------------------------------------------------");
        System.out.print("Enter your choice (1-3): ");
    }

    /**
     * Reads and validates the user's menu choice, handling non-integer inputs gracefully.
     *
     * @param scanner Scanner instance for standard input
     * @return Valid choice integer (1, 2, or 3)
     */
    private static int getValidMenuChoice(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    return choice;
                } else {
                    System.out.print("Invalid choice! Please enter 1, 2, or 3: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a valid number (1-3): ");
            }
        }
    }

    /**
     * Prompts for and validates the number of disks (must be between 1 and 10).
     * Rejects invalid inputs gracefully without causing infinite loops or crashes.
     *
     * @param scanner Scanner instance for standard input
     * @return Valid number of disks between 1 and 10
     */
    private static int getValidDiskCount(Scanner scanner) {
        System.out.print("\nEnter the number of disks (1 to 10): ");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int disks = Integer.parseInt(input);
                if (disks >= 1 && disks <= 10) {
                    return disks;
                } else {
                    System.out.print("Out of range! Please enter a number between 1 and 10: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter an integer between 1 and 10: ");
            }
        }
    }

    // =========================================================================
    // METHOD 1: RECURSIVE SOLVER
    // =========================================================================

    /**
     * Sets up rods and runs the recursive Tower of Hanoi solver.
     *
     * @param n Number of disks
     */
    private static void runRecursiveSolver(int n) {
        CustomStack rodA = new CustomStack("Rod A", n);
        CustomStack rodB = new CustomStack("Rod B", n);
        CustomStack rodC = new CustomStack("Rod C", n);

        // Place disks on Rod A: largest disk (n) at the bottom, smallest (1) on top
        for (int disk = n; disk >= 1; disk--) {
            rodA.push(disk);
        }

        moveCount = 0; // Reset move counter

        System.out.println("\n============================================================");
        System.out.println("         SOLVING TOWER OF HANOI USING RECURSION             ");
        System.out.printf("         Number of Disks: %d%n", n);
        System.out.println("============================================================");
        System.out.println("Initial State (Disks shown from [Bottom -> Top]):");
        displayRodStates(rodA, rodB, rodC);
        System.out.println("------------------------------------------------------------");

        // Execute recursive algorithm
        // Rod A is Source, Rod C is Destination, Rod B is Auxiliary
        solveHanoiRecursive(n, rodA, rodC, rodB, rodA, rodB, rodC);

        // Display summary and verification
        printSummary("Recursion", n, moveCount);
    }

    /**
     * Classic recursive algorithm for Tower of Hanoi.
     *
     * Algorithm logic:
     * 1. Base case: If n == 1, move disk 1 directly from source to destination.
     * 2. Step 1: Move top (n - 1) disks from source to auxiliary rod using destination as helper.
     * 3. Step 2: Move the nth (largest remaining) disk from source to destination rod.
     * 4. Step 3: Move (n - 1) disks from auxiliary rod to destination rod using source as helper.
     *
     * @param n Number of disks to move in this sub-problem
     * @param source Rod from which disks are being moved
     * @param destination Target rod to which disks are being moved
     * @param auxiliary Helper rod used for intermediate storage
     * @param rodA Reference to Rod A for state printing
     * @param rodB Reference to Rod B for state printing
     * @param rodC Reference to Rod C for state printing
     */
    private static void solveHanoiRecursive(int n, CustomStack source, CustomStack destination, CustomStack auxiliary,
                                            CustomStack rodA, CustomStack rodB, CustomStack rodC) {
        // Base case: Only one disk to move
        if (n == 1) {
            executeAndLogMove(source, destination, rodA, rodB, rodC);
            return;
        }

        // Step 1: Move top (n - 1) disks from source to auxiliary rod
        solveHanoiRecursive(n - 1, source, auxiliary, destination, rodA, rodB, rodC);

        // Step 2: Move the nth disk from source to destination rod
        executeAndLogMove(source, destination, rodA, rodB, rodC);

        // Step 3: Move top (n - 1) disks from auxiliary to destination rod
        solveHanoiRecursive(n - 1, auxiliary, destination, source, rodA, rodB, rodC);
    }

    // =========================================================================
    // METHOD 2: ITERATIVE SOLVER USING STACKS
    // =========================================================================

    /**
     * Sets up rods and runs the iterative Tower of Hanoi solver using custom stacks.
     *
     * @param n Number of disks
     */
    private static void runIterativeSolver(int n) {
        CustomStack rodA = new CustomStack("Rod A", n);
        CustomStack rodB = new CustomStack("Rod B", n);
        CustomStack rodC = new CustomStack("Rod C", n);

        // Place disks on Rod A: largest disk (n) at the bottom, smallest (1) on top
        for (int disk = n; disk >= 1; disk--) {
            rodA.push(disk);
        }

        moveCount = 0; // Reset move counter

        System.out.println("\n============================================================");
        System.out.println("      SOLVING TOWER OF HANOI USING STACKS (ITERATIVE)       ");
        System.out.printf("         Number of Disks: %d%n", n);
        System.out.println("============================================================");
        System.out.println("Initial State (Disks shown from [Bottom -> Top]):");
        displayRodStates(rodA, rodB, rodC);
        System.out.println("------------------------------------------------------------");

        // Execute iterative algorithm
        solveHanoiIterative(n, rodA, rodB, rodC);

        // Display summary and verification
        printSummary("Stacks (Iterative)", n, moveCount);
    }

    /**
     * Iterative algorithm for Tower of Hanoi using custom stacks without any recursion.
     *
     * Theoretical Basis:
     * - The total number of required moves is 2^n - 1.
     * - If n is EVEN:
     *     Move 1 (i % 3 == 1): Make the legal move between Rod A and Rod B.
     *     Move 2 (i % 3 == 2): Make the legal move between Rod A and Rod C.
     *     Move 3 (i % 3 == 0): Make the legal move between Rod B and Rod C.
     * - If n is ODD:
     *     Move 1 (i % 3 == 1): Make the legal move between Rod A and Rod C.
     *     Move 2 (i % 3 == 2): Make the legal move between Rod A and Rod B.
     *     Move 3 (i % 3 == 0): Make the legal move between Rod B and Rod C.
     *
     * Between any two selected rods, there is always exactly ONE legal move:
     * - If one rod is empty, the top disk of the other rod moves to it.
     * - If both have disks, the smaller top disk moves onto the larger top disk.
     *
     * @param n Number of disks
     * @param rodA Stack representing Rod A (Source)
     * @param rodB Stack representing Rod B (Auxiliary)
     * @param rodC Stack representing Rod C (Destination)
     */
    private static void solveHanoiIterative(int n, CustomStack rodA, CustomStack rodB, CustomStack rodC) {
        int totalMoves = (int) Math.pow(2, n) - 1;
        boolean isEven = (n % 2 == 0);

        for (int i = 1; i <= totalMoves; i++) {
            if (i % 3 == 1) {
                if (isEven) {
                    makeLegalMoveBetween(rodA, rodB, rodA, rodB, rodC);
                } else {
                    makeLegalMoveBetween(rodA, rodC, rodA, rodB, rodC);
                }
            } else if (i % 3 == 2) {
                if (isEven) {
                    makeLegalMoveBetween(rodA, rodC, rodA, rodB, rodC);
                } else {
                    makeLegalMoveBetween(rodA, rodB, rodA, rodB, rodC);
                }
            } else { // i % 3 == 0
                makeLegalMoveBetween(rodB, rodC, rodA, rodB, rodC);
            }
        }
    }

    /**
     * Determines and performs the single valid move between two rods according to
     * Tower of Hanoi constraints (smaller disk placed on larger disk).
     *
     * @param rod1 First rod candidate
     * @param rod2 Second rod candidate
     * @param rodA Reference to Rod A
     * @param rodB Reference to Rod B
     * @param rodC Reference to Rod C
     */
    private static void makeLegalMoveBetween(CustomStack rod1, CustomStack rod2,
                                             CustomStack rodA, CustomStack rodB, CustomStack rodC) {
        // Case 1: rod1 is empty -> must move from rod2 to rod1
        if (rod1.isEmpty()) {
            executeAndLogMove(rod2, rod1, rodA, rodB, rodC);
        }
        // Case 2: rod2 is empty -> must move from rod1 to rod2
        else if (rod2.isEmpty()) {
            executeAndLogMove(rod1, rod2, rodA, rodB, rodC);
        }
        // Case 3: Both have disks -> compare top disks with peek()
        else if (rod1.peek() < rod2.peek()) {
            // rod1 has the smaller disk, so it moves onto rod2
            executeAndLogMove(rod1, rod2, rodA, rodB, rodC);
        } else {
            // rod2 has the smaller disk, so it moves onto rod1
            executeAndLogMove(rod2, rod1, rodA, rodB, rodC);
        }
    }

    // =========================================================================
    // HELPER & OUTPUT DISPLAY METHODS
    // =========================================================================

    /**
     * Executes a physical disk move by popping from source rod and pushing onto
     * destination rod, then displays the step details and current contents of all rods.
     *
     * @param source Rod from which disk is popped
     * @param destination Rod onto which disk is pushed
     * @param rodA Reference to Rod A
     * @param rodB Reference to Rod B
     * @param rodC Reference to Rod C
     */
    private static void executeAndLogMove(CustomStack source, CustomStack destination,
                                          CustomStack rodA, CustomStack rodB, CustomStack rodC) {
        int movedDisk = source.pop();
        destination.push(movedDisk);
        moveCount++;

        // Print move details
        System.out.printf("Move %-2d : Move Disk %d from %s to %s%n",
                moveCount, movedDisk, source.getRodName(), destination.getRodName());

        // Display current contents of all three rods
        displayRodStates(rodA, rodB, rodC);
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Prints the current contents of Rod A, Rod B, and Rod C.
     *
     * @param rodA Rod A stack
     * @param rodB Rod B stack
     * @param rodC Rod C stack
     */
    private static void displayRodStates(CustomStack rodA, CustomStack rodB, CustomStack rodC) {
        System.out.printf("   %s (Source)      : %s%n", rodA.getRodName(), rodA);
        System.out.printf("   %s (Auxiliary)   : %s%n", rodB.getRodName(), rodB);
        System.out.printf("   %s (Destination) : %s%n", rodC.getRodName(), rodC);
    }

    /**
     * Displays a formatted summary after solving, verifying actual moves against 2^n - 1.
     *
     * @param method Algorithm method name
     * @param n Number of disks
     * @param totalMoves Total moves made
     */
    private static void printSummary(String method, int n, int totalMoves) {
        int theoreticalMin = (int) Math.pow(2, n) - 1;

        System.out.println("\n============================================================");
        System.out.println("              SUMMARY & THEORETICAL VERIFICATION            ");
        System.out.println("============================================================");
        System.out.printf("Algorithm Used              : %s%n", method);
        System.out.printf("Number of Disks (n)         : %d%n", n);
        System.out.printf("Total Moves Executed        : %d%n", totalMoves);
        System.out.printf("Theoretical Minimum (2^n - 1): 2^%d - 1 = %d%n", n, theoreticalMin);

        if (totalMoves == theoreticalMin) {
            System.out.println("Formula Verification Check  : PASSED (Match 2^n - 1)");
        } else {
            System.out.println("Formula Verification Check  : FAILED (Count mismatch)");
        }
        System.out.println("============================================================\n");
    }
}
