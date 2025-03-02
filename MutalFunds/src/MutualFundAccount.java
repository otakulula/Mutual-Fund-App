/**
 * MutualFundAccount.java
 * @author Anuttam Preetham
 * CIS 22C, Applied Lab 4
 */
import java.text.DecimalFormat;
import java.util.Comparator;

public class MutualFundAccount {
    private double numShares;
    private MutualFund mf;

    private static int accountSeed = 100000000;

    /**CONSTRUCTORS*/

    /**
     * One-argument constructor
     * @param mf the mutual fund for this account
     * Assigns 0 to numShares
     */
    public MutualFundAccount(MutualFund mf) {
        this.mf = mf;
        this.numShares = 0;
    }

    /**
     * Two-argument constructor
     * @param numShares total shares of the mutual fund
     * @param mf the mutual fund
     */
    public MutualFundAccount(double numShares, MutualFund mf) {
        this.numShares = numShares;
        this.mf = mf;
    }

    /**
     * Two-argument constructor
     * @param mf the mutual fund
     * @param numShares total shares of the mutual fund
     */
    public MutualFundAccount(MutualFund mf, double numShares) {
        this.mf = mf;
        this.numShares = numShares;
    }

    /**ACCESSORS*/

    /**
     * Accesses the total number of shares
     * @return the total shares
     */
    public double getNumShares() {
        return this.numShares;
    }

    /**
     * Accesses the mutual fund
     * @return the mutual fund
     */
    public MutualFund getMf() {
        return this.mf;
    }

    /**
     * Increments the account seed and returns
     * the new value
     * @return the incremented account seed
     */
    public static int getAccountSeed() {
        return accountSeed++;
    }

    /**MUTATORS*/

    /**
     * Increases/Decreases the total shares
     * by the given amount
     * @param numShares the amount to increase or decrease.
     */
    public void updateShares(double numShares) {
        this.numShares += (numShares);
    }

    /**
     * Deducts fees in the form of reduced number of shares
     * Calcuates the dollar reduction as a percentage of
     * total shares, then divides this number by price per share
     * to determine the reduction in share amount.
     * Finally, reduces the shares by this amount.
     */
    public void deductFee() {
        double totalValue = this.numShares * this.getMf().getPricePerShare();
        double dollarReduction = totalValue * this.getMf().getTradingFee();
        double shareReduction = dollarReduction/ this.getMf().getPricePerShare();
        this.numShares -= shareReduction;
    }

    /**
     * Creates a String of the mutual fund
     * account information in the following format:
     * <mf>
     * Total Shares: <numShares>
     * Value: $<numShares>*<pricePerShare>
     * @return The account information.
     */
    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return this.mf.toString() + "\n\n" +
                "Total Shares: " + this.numShares + "\n" +
                "Value: $" + formatter.format(this.numShares * this.mf.getPricePerShare()) + "\n";
    }
} // end class MutualFundAccount

class NameComparator implements Comparator<MutualFundAccount> {
    /**
     * Compares the two mutual fund accounts by name of the fund
     * uses the String compareTo method to make the comparison
     * @param account1 the first MutualFundAccount
     * @param account2 the second MutualFundAccount
     * @return The comparison.
     */
    @Override
    public int compare(MutualFundAccount account1, MutualFundAccount account2) {
        int result = 0;
        if (account1.getMf().getFundName().equals(account2.getMf().getFundName())) {
            return result;
        } else if (account1.getMf().getFundName().compareTo(account2.getMf().getFundName()) > 0) {
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }
} // end class NameComparator

class ValueComparator implements Comparator<MutualFundAccount> {
    /**
     * Compares the two mutual fund accounts by total value
     * determines total value as number of shares times price
     * per share
     * uses the static Double compare method to make the
     * comparison
     * @param account1 the first MutualFundAccount
     * @param account2 the second MutualFundAccount
     * @return The comparison.
     */
    @Override
    public int compare(MutualFundAccount account1, MutualFundAccount account2) {
        double account1totalValue = account1.getNumShares() * account1.getMf().getPricePerShare();
        double account2totalValue = account2.getNumShares() * account2.getMf().getPricePerShare();
        int result = 0;
        if(account1totalValue == account2totalValue){
            return result;
        } else if ( Double.compare(account1totalValue,account2totalValue) > 0){
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }
}
