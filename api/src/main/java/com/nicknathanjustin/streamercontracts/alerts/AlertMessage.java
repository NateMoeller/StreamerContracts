package com.nicknathanjustin.streamercontracts.alerts;

public class AlertMessage {
    private String username;
    private String bounty;
    private int amount;

    public AlertMessage() {

    }

    public AlertMessage(String username, String bounty, int amount) {
        this.username = username;
        this.bounty = bounty;
        this.amount = amount;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBounty() {
        return this.bounty;
    }

    public void setBounty(String bounty) {
        this.bounty = bounty;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
