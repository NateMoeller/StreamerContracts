package com.nicknathanjustin.streamercontracts.emails;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class CreateEmailRequest {
    @NonNull private String name;
    @NonNull private String email;
}
