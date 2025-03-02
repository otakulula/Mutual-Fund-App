///**
// * oldCustInterface.java
// * @author Anuttam Preetham
// * CIS 22C, Applied Lab 3
// */
//import java.io.*;
//import java.util.Scanner;
//
//
//public class oldCustInterface {
//
//    public static void main(String[] args) {
//        BST<MutualFundAccount> accountValue = new BST<>();
//        BST<MutualFundAccount> accountName = new BST<>();
//        LinkedList<MutualFund> funds = mutualFundListCreator( new File("mutual_funds.txt"));
//        NameComparator compareFundName = new NameComparator();
//        ValueComparator compareShareValue = new ValueComparator();
//
//
//        Scanner myObj = new Scanner(System.in);
//        System.out.print("Welcome to Mutual Fund InvestorTrack (TM)!\n\n");
//        performOption(funds,accountName,accountValue,compareFundName, compareShareValue, myObj);
//
//    }
//
//    public static void performOption(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value, NameComparator compareFundName, ValueComparator compareShareValue, Scanner obj) {
//        String choice = options(obj);
//        String c = choice.toUpperCase();
//        if(c.equals("A")){
//            purchaseAFundOptionA(funds, name, value, compareFundName, compareShareValue, obj);
//        } else if (c.equals("B")){
//            sellAFundOptionB(funds, name, value, compareFundName, compareShareValue, obj);
//        } else if (c.equals("C")){
//            displayYourCurrentFundsOptionC(funds, name, value, compareFundName, compareShareValue, obj);
//        } else if (c.equals("X")){
//            exitOptionX(obj);
//        } else {
//            invalidChoice(funds, name, value, compareFundName, compareShareValue, obj);
//        }
//    }
//
//    public static void invalidChoice(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value, NameComparator compareFundName, ValueComparator compareShareValue, Scanner obj) {
//        System.out.println("\nInvalid menu option. Please enter A-C or X to exit.\n");
//        performOption(funds,name, value, compareFundName, compareShareValue, obj);
//    }
//
//
//    public static void purchaseAFundOptionA(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value,  NameComparator compareFundName, ValueComparator compareShareValue, Scanner obj){
//        System.out.print("\nPlease select from the options below:\n\n" + funds.numberedListString());
//        System.out.print("Enter your choice: (1-7): ");
//        int fundNumLoc = Integer.parseInt(obj.next());
//        System.out.print("\nEnter the number of shares to purchase: ");
//        int numOfShares = Integer.parseInt(obj.next());
//        System.out.println();
//        // obj.next();
//        purchaseFund( funds, name, value, compareFundName, compareShareValue, fundNumLoc, numOfShares);
//        performOption(funds,name, value, compareFundName, compareShareValue, obj);
//    }
//
//    public static void purchaseFund(LinkedList<MutualFund> funds, BST<MutualFundAccount> accountName, BST<MutualFundAccount> accountValue, NameComparator compareFundName, ValueComparator compareShareValue, int fundNumLoc, int numOfShares) {
//        MutualFundAccount account;
//        funds.positionIterator();
//        funds.advanceIteratorToIndex(fundNumLoc - 1);
//        MutualFund fName = funds.getIterator();
//        MutualFundAccount accToSearch = new MutualFundAccount(fName);
//
//        if(!accountName.isEmpty()) {
//            MutualFundAccount result = accountName.search(accToSearch, compareFundName);
//            if(result != null){
//                result.updateShares(numOfShares);
//            } else {
//                if (numOfShares == 0) {
//                    account = new MutualFundAccount(fName);
//                } else {
//                    account = new MutualFundAccount(fName, numOfShares);
//                }
//                accountName.insert(account, compareFundName);
//                accountValue.insert(account, compareShareValue);
//            }
//        } else {
//            if (numOfShares == 0) {
//                account = new MutualFundAccount(fName);
//            } else {
//                account = new MutualFundAccount(fName, numOfShares);
//            }
//            accountName.insert(account, compareFundName);
//            accountValue.insert(account, compareShareValue);
//        }
//    }
//
//    public static void sellAFundOptionB(LinkedList<MutualFund> funds, BST<MutualFundAccount> nameList, BST<MutualFundAccount> valueList,  NameComparator compareFundName,  ValueComparator compareShareValue ,  Scanner obj){
//        if(nameList.isEmpty() && valueList.isEmpty()){
//            System.out.println("\nYou don't have any funds to sell at this time.\n");
//            performOption(funds, nameList, valueList, compareFundName, compareShareValue, obj);
//        } else {
//            System.out.print("\nYou own the following mutual funds:\n" + nameList.inOrderString() +
//                    "\nEnter the name of the fund to sell: ");
//            obj.nextLine();
//            String fundToSell = obj.nextLine();
//
//            System.out.print("Enter the number of shares to sell or \"all\" to sell everything: ");
//            String numToSell = obj.next();
//            System.out.println();
//            sellFunds( funds, nameList, valueList, compareFundName, compareShareValue, fundToSell, numToSell);
//            performOption(funds, nameList, valueList, compareFundName, compareShareValue, obj);
//        }
//    }
//
//    public static void sellFunds(LinkedList<MutualFund> funds, BST<MutualFundAccount> nameList, BST<MutualFundAccount> valueList, NameComparator compareFundName, ValueComparator compareShareValue, String nameOfFund, String numToSell) {
//        funds.positionIterator();
//        while(!funds.getIterator().getFundName().equals(nameOfFund)){
//            funds.advanceIterator();
//        }
//        MutualFundAccount accToSearch = new MutualFundAccount(funds.getIterator());
//        String num = numToSell.toLowerCase();
//        if( num.equals("all")){
//            nameList.remove(accToSearch, compareFundName);
//            valueList.remove(accToSearch,compareFundName);
//        } else {
//            double numSell = Double.parseDouble(numToSell);
//            MutualFundAccount acc = valueList.search(accToSearch, compareFundName);
//            acc.updateShares(-numSell);
//            nameList.remove(acc, compareFundName);
//            valueList.remove(acc,compareFundName);
//            nameList.insert(acc, compareFundName);
//            valueList.insert(acc, compareShareValue);
//        }
//    }
//
//
//    public static void displayYourCurrentFundsOptionC(LinkedList<MutualFund> funds, BST<MutualFundAccount> nameList, BST<MutualFundAccount> valueList,  NameComparator compareFundName,  ValueComparator compareShareValue ,  Scanner obj){
//        if(nameList.isEmpty() && valueList.isEmpty()){
//            System.out.println("\nYou don't have any funds to display at this time.\n");
//            performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
//        } else {
//            System.out.print("\nView Your Mutual Funds By:\n\n" +
//                    "1. Name\n2. Value\n\n" +
//                    "Enter your choice (1 or 2): ");
//            int ans = Integer.parseInt(obj.next());
//            // obj.next();
//            if(ans > 2 || ans <= 0){
//                System.out.println("\nInvalid Choice!\n");
//                performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
//            } else if ( ans == 1){  // name list
//                System.out.println("\n" + nameList.inOrderString());
//                performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
//            } else { // value list
//                System.out.println("\n" + valueList.inOrderString());
//                performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
//            }
//        }
//    }
//
//    public static void exitOptionX( Scanner obj){
//        System.out.println("\nGoodbye!");
//        obj.close();
//    }
//
//    public static String options(Scanner obj){
//        System.out.print("Please select from the following options:\n\n" +
//                "A. Purchase a Fund\n" +
//                "B. Sell a Fund\n" +
//                "C. Display Your Current Funds\n" +
//                "X. Exit\n\n" +
//                "Enter your choice: ");
//        return obj.next();
//    }
//
//    public static LinkedList<MutualFund> mutualFundListCreator(File file){
//        LinkedList<MutualFund> funds = new LinkedList<>();
//        try {
//            Scanner input = new Scanner(file);
//            while (input.hasNextLine()) {
//                String fundName = input.nextLine();
//                String ticker = input.nextLine();
//                Double sharePrice = input.nextDouble();
//                input.nextLine();
//                //   funds.addLast(new MutualFund(fundName, ticker, sharePrice));
//            }
//            input.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return funds;
//    }
//}
//
//
//
//
/////**
//// * oldCustInterface.java
//// * @author Anuttam Preetham
//// * CIS 22C, Applied Lab 3
//// */
////import java.io.*;
////        import java.util.Scanner;
////
////
////public class oldCustInterface {
////
////    public static void main(String[] args) {
////        BST<MutualFundAccount> accountValue = new BST<>();
////        BST<MutualFundAccount> accountName = new BST<>();
////        LinkedList<MutualFund> funds = mutualFundListCreator( new File("mutual_funds.txt"));
////        NameComparator compareFundName = new NameComparator();
////        ValueComparator compareShareValue = new ValueComparator();
////
////        String mutualName, ticker; //
////        //String first, last, email, password; //<-to be used in lab 6
////        double sharePrice, numShares;
////
////        Scanner myObj = new Scanner(System.in);
////        System.out.print("Welcome to Mutual Fund InvestorTrack (TM)!\n\n");
////        performOption(funds,accountName,accountValue,compareFundName, compareShareValue, myObj);
////
////
////    }
////
////    public static void performOption(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value, NameComparator compareFundName, ValueComparator compareShareValue, Scanner obj) {
////        String choice = options(obj);
////        String c = choice.toUpperCase();
////        if(c.equals("A")){
////            purchaseAFundOptionA(funds, name, value, compareFundName, compareShareValue, obj);
////        } else if (c.equals("B")){
////            sellAFundOptionB(funds, name, value, compareFundName, compareShareValue, obj);
////        } else if (c.equals("C")){
////            displayYourCurrentFundsOptionC(funds, name, value, compareFundName, compareShareValue, obj);
////        } else if (c.equals("X")){
////            exitOptionX(obj);
////        } else {
////            invalidChoice(funds, name, value, compareFundName, compareShareValue, obj);
////        }
//////        switch (choice.toUpperCase()) {
//////            case "A" -> purchaseAFundOptionA(funds, name, value, compareFundName, compareShareValue, obj);
//////            case "B" -> sellAFundOptionB(funds, name, value, compareFundName, compareShareValue, obj);
//////            case "C" -> displayYourCurrentFundsOptionC(funds, name, value, compareFundName, compareShareValue, obj);
//////            case "X" -> exitOptionX(obj);
//////            default ->  invalidChoice(funds, name, value, compareFundName, compareShareValue, obj);
//////        }
////    }
////
////    public static void invalidChoice(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value, NameComparator compareFundName, ValueComparator compareShareValue, Scanner obj) {
////        System.out.println("\nInvalid menu option. Please enter A-C or X or exit.\n");
////        performOption(funds,name, value, compareFundName, compareShareValue, obj);
////    }
////
////
////    public static void purchaseAFundOptionA(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value,  NameComparator compareFundName, ValueComparator compareShareValue, Scanner obj){
////        System.out.print("\nPlease select from the options below:\n\n" + funds.numberedListString());
////        System.out.print("Enter your choice: (1-7): ");
////        int fundNumLoc = obj.nextInt();
////        System.out.print("\nEnter the number of shares to purchase: ");
////        int numOfShares = obj.nextInt();
////        System.out.println();
////        obj.nextLine();
////        purchaseFund( funds, name, value, compareFundName, compareShareValue, fundNumLoc, numOfShares);
////        performOption(funds,name, value, compareFundName, compareShareValue, obj);
////    }
////
////    public static void purchaseFund(LinkedList<MutualFund> funds, BST<MutualFundAccount> name, BST<MutualFundAccount> value, NameComparator compareFundName, ValueComparator compareShareValue, int fundNumLoc, int numOfShares) {
////        MutualFundAccount account;
////        funds.positionIterator();
////        funds.advanceIteratorToIndex(fundNumLoc - 1);
////        MutualFund fName = funds.getIterator();
////        MutualFundAccount accToSearch = new MutualFundAccount(fName);
////
////        if(!name.isEmpty()) {
////            MutualFundAccount result = name.search(accToSearch, compareFundName);
////            if(result != null){
////                result.updateShares(numOfShares);
////            } else {
////                if (numOfShares == 0) {
////                    account = new MutualFundAccount(fName);
////                } else {
////                    account = new MutualFundAccount(fName, numOfShares);
////                }
////                name.insert(account, compareFundName);
////                value.insert(account, compareShareValue);
////            }
////        } else {
////            if (numOfShares == 0) {
////                account = new MutualFundAccount(fName);
////            } else {
////                account = new MutualFundAccount(fName, numOfShares);
////            }
////            name.insert(account, compareFundName);
////            value.insert(account, compareShareValue);
////        }
////    }
////
////    public static void sellAFundOptionB(LinkedList<MutualFund> funds, BST<MutualFundAccount> nameList, BST<MutualFundAccount> valueList,  NameComparator compareFundName,  ValueComparator compareShareValue ,  Scanner obj){
////        if(nameList.isEmpty() && valueList.isEmpty()){
////            System.out.println("\nYou don't have any funds to sell at this time.\n");
////            performOption(funds, nameList, valueList, compareFundName, compareShareValue, obj);
////        } else {
////            System.out.print("\nYou own the following mutual funds:\n" + nameList.inOrderString() +
////                    "\nEnter the name of the fund to sell: ");
////            String fundToSell = obj.nextLine();
////            System.out.print("Enter the number of shares to sell or \"all\" to sell everything: ");
////            String numToSell = obj.nextLine();
////            System.out.println();
////            sellFunds( funds, nameList, valueList, compareFundName, fundToSell, numToSell);
////            performOption(funds, nameList, valueList, compareFundName, compareShareValue, obj);
////        }
////    }
////
////    public static void sellFunds(LinkedList<MutualFund> funds, BST<MutualFundAccount> nameList, BST<MutualFundAccount> valueList, NameComparator compareFundName, String nameOfFund, String numToSell) {
////        funds.positionIterator();
////        while(!funds.getIterator().getFundName().equals(nameOfFund)){
////            funds.advanceIterator();
////        }
////        MutualFundAccount accToSearch = new MutualFundAccount(funds.getIterator());
////        String num = numToSell.toLowerCase();
////        if( num.equals("all")){
////            nameList.remove(accToSearch, compareFundName);
////            valueList.remove(accToSearch,compareFundName);
////        } else {
////            int numSell = Integer.parseInt(numToSell);
////            valueList.search(accToSearch, compareFundName).updateShares(-numSell);
////        }
////    }
////
////    public static void displayYourCurrentFundsOptionC(LinkedList<MutualFund> funds, BST<MutualFundAccount> nameList, BST<MutualFundAccount> valueList,  NameComparator compareFundName,  ValueComparator compareShareValue ,  Scanner obj){
////        if(nameList.isEmpty() && valueList.isEmpty()){
////            System.out.println("\nYou don't have any funds to display at this time.\n");
////            performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
////        } else {
////            System.out.print("\nView Your Mutual Funds by:\n\n" +
////                    "1. Name\n2. Value\n\n" +
////                    "Enter your choice (1 or 2): ");
////            int ans = obj.nextInt();
////            obj.nextLine();
////            if(ans > 2 || ans <= 0){
////                System.out.println("\nInvalid Choice!\n");
////                performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
////            } else if ( ans == 1){  // name list
////                System.out.println("\n" + nameList.inOrderString());
////                performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
////            } else { // value list
////                System.out.println("\n" + valueList.inOrderString());
////                performOption(funds,nameList, valueList, compareFundName, compareShareValue, obj);
////            }
////        }
////    }
////
////    public static void exitOptionX( Scanner obj){
////        System.out.println("Goodbye!");
////        obj.close();
////    }
////
////    public static String options(Scanner obj){
////        System.out.print("Please select from the following options:\n\n" +
////                "A. Purchase a Fund\n" +
////                "B. Sell a Fund\n" +
////                "C. Display Your Current Funds\n" +
////                "X. Exit\n\n" +
////                "Enter your choice: ");
////        return obj.nextLine();
////    }
////
////    public static LinkedList<MutualFund> mutualFundListCreator(File file){
////        LinkedList<MutualFund> funds = new LinkedList<>();
////        try {
////            Scanner input = new Scanner(file);
////            while (input.hasNextLine()) {
////                String fundName = input.nextLine();
////                String ticker = input.nextLine();
////                Double sharePrice = input.nextDouble();
////                input.nextLine();
////                funds.addLast(new MutualFund(fundName, ticker, sharePrice));
////            }
////            input.close();
////        } catch (FileNotFoundException e) {
////            System.out.println("An error occurred.");
////            e.printStackTrace();
////        }
////        return funds;
////    }
////
////}
//
