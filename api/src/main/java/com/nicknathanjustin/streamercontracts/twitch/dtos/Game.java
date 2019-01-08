package com.nicknathanjustin.streamercontracts.twitch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    private String gameName;
    private String boxArtUrl;
}
