package com.nicknathanjustin.streamercontracts.votes;

public enum VoteOutcome {
    COMPLETED(true),
    FAILED(false),
    //Default to paying streamer in the event of a dispute
    DISPUTE(true);

    private final boolean payStreamer;

    private VoteOutcome(boolean payStreamer) {
        this.payStreamer = payStreamer;
    }

    public boolean isPayStreamer() {
        return payStreamer;
    }
}
