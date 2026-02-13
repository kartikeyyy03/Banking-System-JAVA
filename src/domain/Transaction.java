package domain;

import java.time.LocalDateTime;

public class Transaction {

    private String accountNumber;
    private double amount;
    private String id;
    private String note;
    private LocalDateTime timeStamp;
    private Type type;

    public Transaction(String accountNumber, double amount, String id, String note,
            LocalDateTime timeStamp, Type type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.id = id;
        this.timeStamp = timeStamp;
        this.note = note;
        this.type = type;

    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
