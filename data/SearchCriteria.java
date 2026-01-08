package data;

public class SearchCriteria {
    public double getMaxPrice() {
        return maxPrice;
    }

    public boolean isHasEnsuite() {
        return hasEnsuite;
    }

    public int getMinBeds() {
        return minBeds;
    }

    private double maxPrice;
    private boolean hasEnsuite;
    private int minBeds;


    public SearchCriteria(double maxPrice, boolean hasEnsuite, int minBeds){
        this.maxPrice = maxPrice;
        this.hasEnsuite = hasEnsuite;
        this.minBeds = minBeds;
    }

}
