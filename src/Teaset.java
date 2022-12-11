public class Teaset {
    int size;
    int[] profit = { 0, 1, 3, 5, 9, 10, 15, 17, 18, 19, 22, 25, 27 };
    Set[][] dynamicTable;
    Set[][] memoizedTable;

    public Teaset() {
        this(10);
    }
    public Teaset(int size) {
        this.size = size;
        dynamicTable = new Set[profit.length][size + 1];
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < profit.length; j++) {
//                dynamicTable[i][j] = new Set(0, "");
//            }
//        }
    }

    public void printAll() {
        System.out.println("Tea Set Size: " + size);
        printAll(size, "", 1);
        System.out.println();
    }

    public void printAll(int amt, String soFar, int currentSize) {
        if (amt == 0) {
            System.out.println(soFar);
        }
        else for (int i = currentSize; i <= amt; i++) {
            printAll(amt - i, soFar + " " + i, i);
        }
    }

    public void bestSolutionRecursion() {
        long startTime = System.currentTimeMillis();
        Set result = bestSolutionRecursion(size, 1, Math.min(size, profit.length - 1), new Set(0, ""));
        System.out.println("Recursion approach: " + result);
        long endTime = System.currentTimeMillis();
        System.out.println("Recursion approach: completed in " + (endTime - startTime) + " milliseconds");
        System.out.println();
    }

    public void bestSolutionRecursion(int highestSize) {
        long startTime = System.currentTimeMillis();
        Set result = bestSolutionRecursion(size, 1, highestSize, new Set(0, ""));
        System.out.println("Recursion approach with largest set " + highestSize + ": " + result);
        long endTime = System.currentTimeMillis();
        System.out.println("Recursion approach completed in " + (endTime - startTime) + " milliseconds");
    }

    public Set bestSolutionRecursion(int amt, int lowestSize, int highestSize, Set set) {
        Set biggest = set;
        if (amt == 0) {
            return set;
        }
        else for (int i = lowestSize; i <= Math.min(highestSize, amt); i++) {
            Set newSet = bestSolutionRecursion(amt - i, i, highestSize, new Set(set.profit + profit[i], set.sets + " " + i));
            //System.out.println("Old " + set + " vs new " + newSet);
            if (newSet.compareTo(biggest) > 0) {
                //System.out.println("New biggest: " + newSet + " vs old " + biggest + " w last " + set);
                biggest = newSet;
            }
        }
        return biggest;
    }

    public int bestSolutionMemoizing() {
        long startTime = System.currentTimeMillis();

        long endTime = System.currentTimeMillis();
        System.out.println("Recursion approach: completed in " + (endTime - startTime) + " milliseconds");
        return 0;
    }

    public int bestSolutionMemoizing(int itemSize, int amt) {
        if (memoizedTable[itemSize][amt].profit > 0) {
            return memoizedTable[itemSize][amt].profit;
        }
        else return bestSolutionMemoizing();
    }

    public void bestSolutionDynamic() {
        long startTime = System.currentTimeMillis();
        if (dynamicTable[profit.length - 1][Math.min(size, dynamicTable.length) - 1] == null) {
           fillTable();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Dynamic Programming approach: " + dynamicTable[profit.length - 1][size]);
        System.out.println("Dynamic Programming approach completed in " + (endTime - startTime) + " milliseconds");
    }

    public void printDynamicTable() {
        for (int i = 0; i < dynamicTable.length; i++) {
            for (int j = 0; j < dynamicTable[i].length; j++) {
                if (i == 0) {
                    System.out.print(j + "\t");
                }
                else if (j == 0) {
                    System.out.print(i + "\t");
                }
                else {
                    System.out.print(dynamicTable[i][j].profit + "\t");
                }
            }
            System.out.println();
        }
    }

    private void fillTable() {
        for (int i = 0; i < dynamicTable.length; i++) {
            //System.out.print("! ");
            for (int j = 0; j < dynamicTable[i].length; j++) {
                //System.out.print("@ ");
                // if we're on the top row, don't worry about checking
                if (i == 0 || j == 0) {
                    dynamicTable[i][j] = new Set(0, "");
                }
                else if (i > j) {
                    Set old = dynamicTable[i - 1][j];
                    dynamicTable[i][j] = new Set(old.profit, old.sets);
                }
                else {
//                    System.out.print("# ");
                    int bestProfit = dynamicTable[i - 1][j].profit;
                    //System.out.println("Spot [" + i + "][" + j + "]");
                    //System.out.println("option 1: [" + (i-1) + "]["+ j + "] " + dynamicTable[i-1][j].profit);
                    String setList = "";
                    // increment through available coin sizes & find the one that gives the highest profit
//                    System.out.print("$ ");
                    for (int k = 1; k <= i; k++) {
//                        System.out.print("% ");
                        int testProfit = dynamicTable[i][j - k].profit + profit[k];
                        //System.out.println("option 2: [" + i + "]["+ (j-k) + "] " + dynamicTable[i][j-k].profit + " + " + profit[k]);
//                        System.out.print("^ ");
                        if (testProfit >= bestProfit) {
                            bestProfit = testProfit;
                            setList = dynamicTable[i][j - k].sets + " " + k;
                        }
                    }
//                    System.out.print("& ");
                    dynamicTable[i][j] = new Set(bestProfit, setList);
                }
            }
        }
    }

    public int bestSolutionDynamic(int setSize) {

        System.out.println("TODO: dynamic programming approach");
        int[][] solutions = new int[setSize][setSize];
        for (int i = 0; i < setSize; i++) {
            for (int j = 0; j < setSize; j++) {

            }
        }


        return solutions[1][1];
    }


    public static void main(String[] args) {
        Teaset teacups1 = new Teaset();
        System.out.println("Testing print with half the set:");
        teacups1.printAll(5, "", 1);
        System.out.println();
        //teacups1.bestSolutionDynamic();
        //teacups1.bestSolutionMemoizing();

        System.out.println("Testing default 10 items");
        Teaset teacups2 = new Teaset();
        teacups2.printAll();
        teacups2.bestSolutionRecursion();
        teacups2.bestSolutionDynamic();
        System.out.println("Table for dynamic programming method: ");
        teacups2.printDynamicTable();
        //teacups2.bestSolutionMemoizing();

        System.out.println();
        System.out.println("Testing a set of 5");
        Teaset teacups3 = new Teaset(5);
        teacups3.printAll();
        teacups3.bestSolutionRecursion(3);
        //teacups3.bestSolutionDynamic();

        System.out.println();
        System.out.println("Testing a large set - 100 teacups");
        Teaset teacups4 = new Teaset(50);
        teacups4.bestSolutionDynamic();
        teacups4.bestSolutionRecursion();
    }
}
