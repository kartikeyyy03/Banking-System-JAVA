package app;

import java.util.Scanner;

import service.BankService;
import service.impl.BankServiceImpl;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        boolean running = true;
        System.out.println("Welcome to Console Bank");
        while (running == true) {
            System.out.println("""
                        1) Open Account
                        2) Deposit
                        3) Withdraw
                        4) Transfer
                        5) Account Statement
                        6) List Accounts
                        7) Search Accounts by Customer Name
                        0) Exit
                    """);

            System.out.print("CHOOSE: ");
            String choice = sc.nextLine().trim();
            System.out.println("CHOICE: " + choice);
            switch (choice) {
                case "0" -> running = false;
                case "1" -> openAccount(sc, bankService);
                case "2" -> deposit(sc, bankService);
                case "3" -> withdraw(sc, bankService);
                case "4" -> transfer(sc, bankService);
                case "5" -> statement(sc, bankService);
                case "6" -> listAccount(sc, bankService);
                case "7" -> searchAccounts(sc, bankService);
            }

        }
    }

    private static void openAccount(Scanner sc, BankService bankService) {

        System.out.println("Customer name: ");
        String name = sc.nextLine().trim();

        System.out.println("Customer email: ");
        String email = sc.nextLine().trim();

        System.out.println("Account Type (SAVINGS/CURRENT): ");
        String type = sc.nextLine().trim();

        System.out.println("Initial deposit (optional, blank for 0): ");
        String amountStr = sc.nextLine().trim();
        Double initial = amountStr.isEmpty() ? 0.0 : Double.parseDouble(amountStr);

        String accountNumber = bankService.openAccount(name, email, type, initial);

        // if (initial > 0) {
        // bankService.deposit(accountNumber, initial, "Initial Deposit");
        // }

        // CONFIRMATION:
        System.out.println("Account opened: " + accountNumber);

    }

    private static void deposit(Scanner sc, BankService bankService) {

        System.out.println("Account number: ");
        String accountNumber = sc.nextLine().trim();

        System.out.println("Amount: ");
        double amount = Double.valueOf(sc.nextLine().trim());

        bankService.deposit(accountNumber, amount, "Cash Deposit");
        System.out.println("Deposited successfully");

    }

    private static void withdraw(Scanner sc, BankService bankService) {

        System.out.println("Account number: ");
        String accountNumber = sc.nextLine().trim();

        System.out.println("Amount: ");
        double amount = Double.valueOf(sc.nextLine().trim());

        bankService.withdraw(accountNumber, amount, "Cash withdrawal");
        System.out.println("Withdrawn successfully");

    }

    private static void transfer(Scanner sc, BankService bankService) {

        System.out.println("From Account: ");
        String from = sc.nextLine().trim();

        System.out.println("To Account: ");
        String to = sc.nextLine().trim();

        System.out.println("Amount: ");
        double amount = Double.valueOf(sc.nextLine().trim());

        bankService.transfer(from, to, amount, "Cash transfer");
        System.out.println("Transferred successfully");

    }

    private static void statement(Scanner sc, BankService bankService) {

        System.out.println("Account Number: ");
        String account = sc.nextLine().trim();
        bankService.getStatement(account).forEach(t -> {
            System.out.println(
                    t.getTimeStamp() + " | " +
                            t.getType() + " | " +
                            t.getAmount() + " | " +
                            t.getNote());
        });
    }

    private static void listAccount(Scanner sc, BankService bankService) {
        bankService.listAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });

    }

    // SEARCHING ACCOUNT BY NAME:
    private static void searchAccounts(Scanner sc, BankService bankService) {
        System.out.println("Customer name contains: ");
        String q = sc.nextLine().trim();
        bankService.searchAccountsByCustomerName(q).forEach(account -> System.out
                .println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance()));

    }

}
