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

    public static void invalidChoice(Scanner obj, Customer cus, HashTable<MutualFund> funds, DecimalFormat df) {
        System.out.println("\nInvalid menu option. Please enter A-D or X to exit.\n");
        performOption(obj, cus, funds, df);
    }

    public static void exitOptionX(Scanner obj){
        System.out.print("\nGoodbye!");
        obj.close();
    }

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

    public static Customer doesAccountExist(String email, String password, HashTable<Customer> customers) {
        Customer cus = new Customer(email,password);
        return customers.get(cus);
    }

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
