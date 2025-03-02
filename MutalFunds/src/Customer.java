/**
 * Customer.java
 * @author Anuttam Preetham
 * CIS 22C, Applied Lab 4
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accountNum;

    private double cash;
    private BST<MutualFundAccount> fundsValue = new BST<>();
    private BST<MutualFundAccount> fundsName = new BST<>();

    /**CONSTRUCTORS*/

    /**
     * Creates a new Customer when only email
     * and password are known
     * @param email the Customer email
     * @param password the Customer password
     * Assigns firstName to "first name unknown"
     * Assigns lastName to "last name unknown"
     * Assigns cash to 0
     * Assigns accountNum to "none"
     */
    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
        this.firstName = "first name unknown";
        this.lastName = "last name unknown";
        this.cash = 0;
        this.accountNum = "none";
    }

    /**
     * Creates a new Customer with no cash.
     * @param firstName member first name
     * @param lastName member last name
     * Assigns cash to 0
     * Calls getAccountSeed and assigns accountNum to this value
     * after converting it to a String BY USING CONCATENATION (easiest way)
     * Hint: Make sure you get no warnings or you did not call getAccountSeed
     * correctly!
     */
    public Customer(String firstName, String lastName, String email, String password) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cash = 0;
        this.accountNum = Integer.toString(MutualFundAccount.getAccountSeed());
    }

    /**
     * Creates a new Customer when all information is known.
     * @param firstName member first name
     * @param lastName member last name
     * @param cash the starting amount of cash
     * @param al the MutualFundAccounts owned by this Customer
     * (Hint: add these to the BSTs)
     * Calls getAccountSeed and assigns accountNum to this value
     * after converting it to a String BY USING CONCATENATION (easiest way)
     * Hint: Make sure you get no warnings or you did not call getAccountSeed
     * correctly!
     */
    public Customer(String firstName, String lastName, String email, String password, double cash, ArrayList<MutualFundAccount> al) {
        NameComparator compareFundName = new NameComparator();
        ValueComparator compareShareValue = new ValueComparator();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cash = cash;
        for (MutualFundAccount acc : al) {
            fundsName.insert(acc, compareFundName);
            fundsValue.insert(acc, compareShareValue);
        }
        this.accountNum = Integer.toString(MutualFundAccount.getAccountSeed());
    }

    /**ACCESORS*/

    /**
     * Accesses the customer first name
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Accesses the customer last name
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Accesses the user's account number
     * @return the account number
     */
    public String getAccountNum() {
        return this.accountNum;
    }

    /**
     * Accesses the email address
     * @return the email address
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Determines whether a given password matches the customer password.
     * @param anotherPassword the password to compare
     * @return whether the two passwords match
     */
    public boolean passwordMatch(String anotherPassword) {
        return this.password.equals(anotherPassword);
    }

    /**
     * Accesses a specific fund.
     * @param name of the chosen fund
     * @return the specified mutual fund
     */
    public MutualFundAccount getAccountByName(String name) {
        NameComparator compareFundName = new NameComparator();
        MutualFundAccount account = new MutualFundAccount(new MutualFund(name,""));
        return fundsName.search(account, compareFundName);
    }

    /**
     * Accesses the amount of cash in your account.
     * @return the amount of cash
     */
    public double getCash() {
        return this.cash;
    }

    /**
     * Accesses whether any accounts exist for this customer.
     * @return whether the customer currently holds any accounts.
     */
    public boolean hasOpenAccounts() {
        return !fundsName.isEmpty();
    }

    /**MUTATORS*/

    /**
     * Updates the customer first name
     * @param firstName a new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Updates the customer last name
     * @param lastName a new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Updates the value of the email address
     * @param  's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Updates the value of the password
     * @param password the Customer password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Increases/Decreases the amount of cash by adding to the existing cash.
     * @param cash the amount of cash to add
     */
    public void updateCash(double cash) {
        this.cash += (cash);
    }

    /**
     * Adds a new mutual fund to the user's list of funds or updates a fund to
     * increase the number of shares held.
     * @param shares the desired number of shares
     * @param mf a new or existing mutual fund
     * @return whether the fund was added to the customer's account
     * - i.e. the customer had sufficient cash to add the MutualFund.
     * Decreases the amount of cash if purchase made
     */
    public boolean addFund(double shares, MutualFund mf) {
        NameComparator compareFundName = new NameComparator();
        ValueComparator compareShareValue = new ValueComparator();
        double sharePrice = mf.getPricePerShare();
        if(cash >= sharePrice){
            MutualFundAccount acc = new MutualFundAccount(shares, mf);

            if(fundsName.isEmpty()){ // if list empty
                fundsName.insert(acc, compareFundName);
                fundsValue.insert(acc, compareShareValue);
            } else { // list is not empty
                MutualFundAccount result = fundsName.search(acc, compareFundName);
                if(result != null){ // param fund is in list
                    result.updateShares(shares);
                } else { // param fund not in list
                    fundsName.insert(acc, compareFundName);
                    fundsValue.insert(acc, compareShareValue);
                }
            }

            this.updateCash(-(sharePrice*shares));
            return true;
        }
        return false;
    }

    /**
     * Sells a Mutual Fund and returns (the price per share times
     * the number of shares to cash minus the fee).
     * The fee is % * price per share * number of shares.
     * Returns silently with no changes if the fund does not exist.
     * @param name the name of the fund
     * @throws NoSuchElementException if the fund name does not exist.
     */
    public void sellFund(String name) throws NoSuchElementException{
        NameComparator compareFundName = new NameComparator();
        ValueComparator compareShareValue = new ValueComparator();
        MutualFundAccount account = this.getAccountByName(name);
        if( account == null){
            throw new NoSuchElementException("sellFund: the fund name does not exist");
        }
        double value = account.getMf().getPricePerShare() * account.getNumShares();
        double tradingPercent = account.getMf().getTradingFee();
        double newCash = value - (value * (tradingPercent/100));
        this.updateCash(newCash);
        fundsName.remove(account,compareFundName);
        fundsValue.remove(account,compareShareValue);
    }

    /**
     * Sells a Mutual Fund and returns (the price per share times
     * the number of shares) to cash minus the fee.
     * The fee is % * price per share * number of shares
     * @param name the name of the fund
     * @throws NoSuchElementException if the fund name does not exist.
     */
    public void sellShares(String name, double shares) throws NoSuchElementException{
        NameComparator compareFundName = new NameComparator();
        ValueComparator compareShareValue = new ValueComparator();
        MutualFundAccount account = this.getAccountByName(name);

        if( account == null){
            throw new NoSuchElementException("sellShares: the fund name does not exist");
        }
        MutualFundAccount copyAcc = new MutualFundAccount(account.getMf(), account.getNumShares());

        double value = account.getMf().getPricePerShare() * shares;
        double tradingPercent = account.getMf().getTradingFee();
        double newCash = value - (value * (tradingPercent/100));
        this.updateCash(newCash);

        copyAcc.updateShares(-shares);
        fundsName.remove(account, compareFundName);
        fundsValue.remove(account, compareShareValue);
        fundsName.insert(copyAcc, compareFundName);
        fundsValue.insert(copyAcc, compareShareValue);
    }

    /**ADDITIONAL OPERATIONS*/

    /**
     * Creates a String of customer information in the form
     * Name: <firstName> <lastName>
     * Account Number: <accountNum>
     * Total Cash: $<cash>
     * Note that cash is formatted $XXX,XXX,XXX.XX
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###.00");
        String user = "Name: " + firstName + " " + lastName
                + "\nEmail: " + email + "\nAccount Number: " + accountNum
                + "\nTotal Cash: $" + df.format(cash) + "\n\nCurrent Funds:\n";
        return user;
    }

    /**
     * Prints out all the customer accounts alphabetized by name.
     */
    public void printAccountsByName() {
        System.out.println(fundsName.inOrderString());
    }

    /**
     * Prints out all the customer accounts in increasing order of value.
     */
    public void printAccountsByValue() {
        System.out.println(fundsValue.inOrderString());
    }

    /**
     * Compares this Customer to another Object for equality.
     * You must use the formula presented
     * in class for full credit. (See Lesson 4)
     * @param obj another Object
     * @return true if obj is a Customer and has a matching email and password
     * to this Customer.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } else if(!(obj instanceof Customer)) {
            return false;
        } else {
            Customer fund = (Customer) obj;
            if(fund.email.equals(this.email)){
                if(fund.password.equals(this.password)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a consistent hash code for each Customer by summing
     * the Unicode values of each character in the key.
     * Key = email + password
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int sum = 0;
        String key = email + password;
        for (int i = 0; i < key.length(); i++){
            sum += key.charAt(i);
        }
        return sum;
    }
}
