package com.nicknathanjustin.streamercontracts.contracts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class ContractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID proposerId;

    private UUID streamerId;

    private String game;

    private String description;

    private Timestamp proposedAt;

    private Timestamp acceptedAt;

    private Timestamp expiresAt;

    private Timestamp completedAt;

    private boolean isAccepted;

    private boolean isCompleted;

    private boolean isCommunityContract;
}
