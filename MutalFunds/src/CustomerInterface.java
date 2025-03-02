/**
 * CustomerInterface.java
 * @author Anuttam Preetham
 * CIS 22C, Applied Lab 4
 */
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerInterface {

    /**
     * The main method that runs the Mutual Fund Tracker application.
     * It initializes the mutual funds and customers databases, prompts the user for login or account creation,
     * and then allows the user to perform various operations based on their choice listed in Options() method.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        final int NUM_MUTUAL_FUNDS = 7;
        final int NUM_CUSTOMERS = 100;
        HashTable<MutualFund> funds;
        HashTable<Customer> customers;
        DecimalFormat df = new DecimalFormat("###,##0.00");


        File file1 = new File("mutual_funds.txt");
        File file2 = new File("customers.txt");
        funds = fillFundListDataBase(file1, NUM_MUTUAL_FUNDS);
        customers = fillCustomerDataBase(file2, NUM_CUSTOMERS, funds);



        Scanner myObj = new Scanner(System.in);
        System.out.print("Welcome to Mutual Fund InvestorTrack (TM)!\n\n");
        System.out.print("Please enter your email address: ");
        String email = myObj.next();
        System.out.print("Please enter your password: ");
        String password = myObj.next();
        Customer isAccReal = doesAccountExist(email, password, customers);

        if(isAccReal != null){
            System.out.print("\nWelcome, " + isAccReal.getFirstName() + " " + isAccReal.getLastName() + "!\n\n\n");
            performOption(myObj, isAccReal, funds, df);
        } else {
            System.out.print("\nWe don't have your account on file...\n\n" +
                    "Let's create an account for you!\n");
            System.out.print("Enter your first name: ");
            String firstName = myObj.next();
            System.out.print("Enter your last name: ");
            String lastName = myObj.next();
            Customer newCus = new Customer(firstName, lastName, email, password);
            System.out.println();
            System.out.print("Welcome, " + newCus.getFirstName() + " " + newCus.getLastName() + "!\n\n\n");
            performOption(myObj, newCus, funds, df);
        }
    }

    /**
     * Displays the menu options and performs the selected operation based on user input.
     *
     * @param obj   The Scanner object to read user input.
     * @param cus   The Customer object representing the current user.
     * @param funds The HashTable containing all mutual funds.
     * @param df    The DecimalFormat object used to format currency values.
     */
    public static void performOption ( Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df){
        String choice = options(obj);
        String c = choice.toUpperCase();
        if(c.equals("A")){
            purchaseFundOptionA(obj, cus, funds, df);
        } else if (c.equals("B")){
            sellFundOptionB(obj, cus, funds, df);
        } else if (c.equals("C")){
            addCashOptionC(obj, cus, funds, df);
        } else if (c.equals("D")){
            displayCurrentFundsOptionD(obj, cus, funds, df);
        } else if ( c.equals("X")){
            exitOptionX(obj);
        } else {
            invalidChoice(obj, cus, funds, df);
        }
    }

    /**
     * Handles the purchase of a mutual fund by the customer.
     * Prompts the user to select a fund and the number of shares to purchase.
     *
     * @param obj   The Scanner object to read user input.
     * @param cus   The Customer object representing the current user.
     * @param funds The HashTable containing all mutual funds.
     * @param df    The DecimalFormat object used to format currency values.
     */
    public static void purchaseFundOptionA(Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df){
        System.out.print("\nPlease select from the options below:\n\n" + funds);
        System.out.print("\nEnter the ticker of the fund to purchase: ");
        String ticker = obj.next();
        System.out.print("\nEnter the number of shares to purchase: ");
        double numOfShares = Double.parseDouble(obj.next());
        System.out.println();
        // obj.next();
        purchaseFund(funds, numOfShares, ticker, cus, obj, df);
    }

    /**
     * Attempts to purchase a mutual fund for the customer.
     * Checks if the customer has enough cash to make the purchase and updates the customer's account accordingly.
     *
     * @param funds        The HashTable containing all mutual funds.
     * @param numOfShares  The number of shares to purchase.
     * @param ticker       The ticker symbol of the fund to purchase.
     * @param cus          The Customer object representing the current user.
     * @param obj          The Scanner object to read user input.
     * @param df           The DecimalFormat object used to format currency values.
     */
    public static void purchaseFund(HashTable<MutualFund> funds, double numOfShares, String ticker, Customer cus, Scanner obj, DecimalFormat df) {
        double cusCash = cus.getCash();
        String upperTicker = ticker.toUpperCase();
        MutualFund mf = new MutualFund(upperTicker);
        MutualFund fund = funds.get(mf);
        double totalValue = fund.getPricePerShare() * numOfShares;
        if(cusCash < totalValue){ //cant buy
            System.out.print("You don't have enough cash to purchase that fund.\n" +
                    "Please add cash to make a purchase\n\n\n");
        } else {// enough to buy
            cus.addFund(numOfShares,fund);
            System.out.print("You successfully added shares of the following fund:\n\n");
            System.out.print(fund);
            System.out.print("\nNumber of shares added: " + numOfShares);
            System.out.println();
            System.out.println();
        }
        performOption(obj, cus, funds, df);
    }

    /**
     * Handles the selling of a mutual fund by the customer.
     * Prompts the user to select a fund and the number of shares to sell.
     *
     * @param obj   The Scanner object to read user input.
     * @param cus   The Customer object representing the current user.
     * @param funds The HashTable containing all mutual funds.
     * @param df    The DecimalFormat object used to format currency values.
     */
    public static void sellFundOptionB(Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df){
        if(!cus.hasOpenAccounts()){
            System.out.println("\nYou don't have any funds to sell at this time.\n");
            performOption(obj, cus, funds, df);
        } else {
            System.out.print("\nYou own the following mutual funds:\n\n");
                    cus.printAccountsByName();
            System.out.print("Enter the name of the fund to sell: ");
            obj.nextLine();
            String fundToSell = obj.nextLine();
            MutualFundAccount mutualFundAccount = cus.getAccountByName(fundToSell);
           if(mutualFundAccount == null){
               System.out.print("Sorry you don't own an account by that name.");
               System.out.println();
               System.out.println();
               System.out.println();
               performOption(obj, cus, funds, df);
           } else {
               System.out.print("Enter the number of shares to sell or \"all\" to sell everything: ");
               String numToSell = obj.next();
               System.out.println();
               sellFunds(funds, fundToSell, numToSell, obj, cus, df);
           }
        }
    }

    /**
     * Sells the specified number of shares of a mutual fund for the customer.
     * If the user enters "all", it sells all shares of the specified fund.
     *
     * @param funds      The HashTable containing all mutual funds.
     * @param fundToSell The name of the fund to sell.
     * @param numToSell  The number of shares to sell or "all" to sell everything.
     * @param obj        The Scanner object to read user input.
     * @param cus        The Customer object representing the current user.
     * @param df         The DecimalFormat object used to format currency values.
     */
    public static void sellFunds(HashTable<MutualFund> funds, String fundToSell, String numToSell, Scanner obj, Customer cus, DecimalFormat df) {
        if( numToSell.equals("all")){
            cus.sellFund(fundToSell);
        } else {
            double numSell = Double.parseDouble(numToSell);
            cus.sellShares(fundToSell, numSell);
        }
        System.out.print("\nYou own the following funds: \n\n");
        cus.printAccountsByName();
        System.out.println();
        System.out.print("Your current cash balance is $" + df.format(cus.getCash()));
        System.out.println();
        System.out.println();
        System.out.println();
        performOption(obj, cus, funds, df);
    }

    /**
     * Handles adding cash to the customer's account.
     * Prompts the user to enter the amount of cash to add and updates the customer's cash balance.
     *
     * @param obj   The Scanner object to read user input.
     * @param cus   The Customer object representing the current user.
     * @param funds The HashTable containing all mutual funds.
     * @param df    The DecimalFormat object used to format currency values.
     */
    public static void addCashOptionC(Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df) {
        System.out.print("\nYour current cash balance is $" + df.format(cus.getCash()));
        System.out.print("\n\nEnter the amount of cash to add: $");
        double cashToAdd = obj.nextDouble();
        cus.updateCash(cashToAdd);
        System.out.print("\nYour current cash balance is $" + df.format(cus.getCash()));
        System.out.println();
        System.out.println();
        System.out.println();
        performOption(obj, cus, funds, df);
    }

    /**
     * Displays the customer's current mutual funds, sorted either by name or by value.
     *
     * @param obj   The Scanner object to read user input.
     * @param cus   The Customer object representing the current user.
     * @param funds The HashTable containing all mutual funds.
     * @param df    The DecimalFormat object used to format currency values.
     */
    public static void displayCurrentFundsOptionD(Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df){
        if(!cus.hasOpenAccounts()){
            System.out.println("\nYou don't have any funds to display at this time.\n");
            performOption(obj, cus, funds, df);
        } else {
            System.out.print("\nView Your Mutual Funds By:\n\n" +
                    "1. Name\n2. Value\n\n" +
                    "Enter your choice (1 or 2): ");
            int ans = Integer.parseInt(obj.next());
            // obj.next();
            if(ans > 2 || ans <= 0){
                System.out.println("\n\nInvalid Choice!\n\n");
            } else if ( ans == 1){  // name list
                System.out.println();
                cus.printAccountsByName();
                System.out.println();
            } else { // value list
                System.out.println();
                cus.printAccountsByValue();
                System.out.println();
            }
            performOption(obj, cus, funds, df);
        }
    }

    /**
     * Displays an error message when an invalid menu option is selected.
     *
     * @param obj   The Scanner object to read user input.
     * @param cus   The Customer object representing the current user.
     * @param funds The HashTable containing all mutual funds.
     * @param df    The DecimalFormat object used to format currency values.
     */
    public static void invalidChoice(Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df) {
        System.out.println("\nInvalid menu option. Please enter A-D or X to exit.\n");
        performOption(obj, cus, funds, df);
    }

    /**
     * Handles the exit option, closing the scanner and displaying a goodbye message.
     *
     * @param obj The Scanner object to read user input.
     */
    public static void exitOptionX(Scanner obj){
        System.out.print("\nGoodbye!");
        obj.close();
    }

    /**
     * Displays the menu options and returns the user's choice.
     *
     * @param obj The Scanner object to read user input.
     * @return The user's menu choice as a String.
     */
    public static String options(Scanner obj){
        System.out.print("Please select from the following options:\n\n" +
                "A. Purchase a Fund\n" +
                "B. Sell a Fund\n" +
                "C. Add Cash\n" +
                "D. Display Your Current Funds\n" +
                "X. Exit\n\n" +
                "Enter your choice: ");
        return obj.next();
    }

    /**
     * Checks if a customer account exists in the database based on the provided email and password.
     *
     * @param email     The email address of the customer.
     * @param password  The password of the customer.
     * @param customers The HashTable containing all customer accounts.
     * @return The Customer object if the account exists, otherwise null.
     */
    public static Customer doesAccountExist(String email, String password, HashTable<Customer> customers) {
        Customer cus = new Customer(email,password);
        return customers.get(cus);
    }

    /**
     * Populates the customer database from a file. The file must follow a specific format for the data to be parsed correctly:
     * <ul>
     *   <li>Each customer's data must be on separate lines.</li>
     *   <li>The first line should contain the customer's full name (first name and last name separated by a space).</li>
     *   <li>The next line should contain the customer's email address.</li>
     *   <li>The next line should contain the customer's password.</li>
     *   <li>The next line should contain the customer's cash balance as a double value.</li>
     *   <li>The next line should contain the number of mutual funds owned by the customer.</li>
     *   <li>For each mutual fund owned, the following lines should contain:
     *     <ul>
     *       <li>The ticker symbol of the fund.</li>
     *       <li>The number of shares owned as a double value.</li>
     *     </ul>
     *   </li>
     * </ul>
     * If the file is not in the correct format, the method may not parse the data correctly, leading to unexpected results.
     *
     * @param file     The file containing customer data. Must follow the specified format.
     * @param size     The expected number of customers.
     * @param allFunds The HashTable containing all mutual funds.
     * @return A HashTable containing all customer accounts.
     */
    public static  HashTable<Customer> fillCustomerDataBase( File file, int size, HashTable<MutualFund> allFunds) {
        HashTable<Customer> customers = new HashTable<>(size);
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String name = input.nextLine();
                String email = input.next();
                String password = input.next();
                double cashBalance = input.nextDouble();
                int numOfFunds = input.nextInt();
                ArrayList<MutualFundAccount> ownedFunds = new ArrayList<>();
                for (int i = 0; i < numOfFunds ; i++) {

                    String ticker = input.next();
                    double numShares = input.nextDouble();
                    MutualFund mf = new MutualFund(ticker);
                    MutualFund fund = allFunds.get(mf);
                    ownedFunds.add(new MutualFundAccount(numShares, fund));
                    input.nextLine();
                }
                int spaceLoc = name.indexOf(' ');
                String firstName = name.substring(0, spaceLoc);
                String lastName = name.substring(spaceLoc + 1);
                customers.add(new Customer(firstName, lastName, email, password, cashBalance, ownedFunds));
                if(input.hasNextLine()){
                    input.nextLine();
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Populates the mutual funds database from a file. The file must follow a specific format for the data to be parsed correctly:
     * <ul>
     *   <li>Each mutual fund's data must be on separate lines.</li>
     *   <li>The first line should contain the name of the mutual fund.</li>
     *   <li>The next line should contain the ticker symbol of the fund.</li>
     *   <li>The next line should contain the price per share as a double value.</li>
     *   <li>The next line should contain the trading fee as a double value.</li>
     * </ul>
     * If the file is not in the correct format, the method may not parse the data correctly, leading to unexpected results.
     *
     * @param file The file containing mutual fund data. Must follow the specified format.
     * @param size The expected number of mutual funds.
     * @return A HashTable containing all mutual funds.
     */
    public static HashTable<MutualFund> fillFundListDataBase(File file, int size) {
        HashTable<MutualFund> funds = new HashTable<>(size * 2);
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String fundName = input.nextLine();
                String ticker = input.nextLine();
                double sharePrice = input.nextDouble();
                double tradingFee = input.nextDouble();
                input.nextLine();
                funds.add(new MutualFund(fundName, ticker, sharePrice, tradingFee));
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return funds;
    }
}
