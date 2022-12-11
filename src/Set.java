public class Set implements Comparable<Set> {
    int profit;
    String sets;

    public Set(int profit, String sets) {
        this.profit = profit;
        this.sets = sets;
    }

    @Override
    public int compareTo(Set o) {
        return Integer.compare(this.profit, o.profit);
    }

    public String toString() {
        return "$" + profit + " " + sets;
    }
}
