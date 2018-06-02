package com.nicknathanjustin.streamercontracts.exampleapi;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "exampleTable")
@Builder
@AllArgsConstructor(access  = AccessLevel.PACKAGE)
@NoArgsConstructor(access  = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class ExampleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID exampleId;

    private String exampleValue;
}
