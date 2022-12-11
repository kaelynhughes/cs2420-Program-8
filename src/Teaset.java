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
        memoizedTable = new Set[profit.length][size + 1];
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
        long startTime = System.nanoTime();
        Set result = bestSolutionRecursion(size, 1, highestSize, new Set(0, ""));
        System.out.println("Recursion approach with largest set " + highestSize + ": " + result);
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Recursion approach completed in " + total + " nanoseconds, " + total / 1000 + " milliseconds");
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

    public void bestSolutionMemoizing() {
        long startTime = System.currentTimeMillis();

        Set result = bestSolutionMemoizing(profit.length - 1, size);

        long endTime = System.currentTimeMillis();
        System.out.println("Memoizing approach result: " + result);
        System.out.println("Memoizing approach completed in " + (endTime - startTime) + " milliseconds");
    }

    public Set bestSolutionMemoizing(int largestSetSize, int amtLeft) {
        if (largestSetSize < 0 || amtLeft < 0 || amtLeft > size) {
            return null;
        }
        else if (!(memoizedTable[largestSetSize][amtLeft] == null)) {
            return memoizedTable[largestSetSize][amtLeft];
        }
        else if (amtLeft == 0 || largestSetSize == 0) {
           Set newSet = new Set(0, "");
           memoizedTable[largestSetSize][amtLeft] = newSet;
           return newSet;
        }
        else if (largestSetSize > amtLeft) {
            return bestSolutionMemoizing(largestSetSize - 1, amtLeft);
        }
        else {
            Set useLargestSet = bestSolutionMemoizing(largestSetSize, amtLeft - largestSetSize);
            Set dontUseSet = bestSolutionMemoizing(largestSetSize - 1, amtLeft);
            Set newSet;
            if (useLargestSet.profit + profit[largestSetSize] > dontUseSet.profit) {
                newSet = new Set(useLargestSet.profit + profit[largestSetSize], useLargestSet.sets + " " + largestSetSize);
            }
            else {
                newSet = new Set(dontUseSet.profit, dontUseSet.sets);
            }
            memoizedTable[largestSetSize][amtLeft] = newSet;
            return memoizedTable[largestSetSize][amtLeft];
        }
    }

    public void printMemoizingTable() {
        for (int i = 0; i < memoizedTable.length; i++) {
            for (int j = 0; j < memoizedTable[i].length; j++) {
                if (i == 0) {
                    System.out.print(j + "\t");
                }
                else if (j == 0) {
                    System.out.print(i + "\t");
                }
                else if (memoizedTable[i][j] == null) {
                    System.out.print("--\t");
                }
                else {
                    System.out.print(memoizedTable[i][j].profit + "\t");
                }
            }
            System.out.println();
        }
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
            for (int j = 0; j < dynamicTable[i].length; j++) {
                // if we're on the top row, don't worry about checking
                if (i == 0 || j == 0) {
                    dynamicTable[i][j] = new Set(0, "");
                }
                else if (i > j) {
                    Set old = dynamicTable[i - 1][j];
                    dynamicTable[i][j] = new Set(old.profit, old.sets);
                }
                else {
                    int bestProfit = dynamicTable[i - 1][j].profit;
                    String setList = "";
                    // increment through available coin sizes & find the one that gives the highest profit
                    for (int k = 1; k <= i; k++) {
                        int testProfit = dynamicTable[i][j - k].profit + profit[k];
                        if (testProfit >= bestProfit) {
                            bestProfit = testProfit;
                            setList = dynamicTable[i][j - k].sets + " " + k;
                        }
                    }
                    dynamicTable[i][j] = new Set(bestProfit, setList);
                }
            }
        }
    }

    public static void main(String[] args) {
        Teaset teacups1 = new Teaset();
        System.out.println("Testing print with half the set:");
        teacups1.printAll(5, "", 1);
        System.out.println();
        teacups1.bestSolutionDynamic();
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
        teacups3.bestSolutionDynamic();
        teacups3.bestSolutionMemoizing();
        teacups3.printMemoizingTable();

        System.out.println();
        System.out.println("Testing a large set - 100 teacups");
        Teaset teacups4 = new Teaset(50);
        teacups4.bestSolutionDynamic();
        teacups4.bestSolutionRecursion();
        teacups4.bestSolutionMemoizing();
    }
}
