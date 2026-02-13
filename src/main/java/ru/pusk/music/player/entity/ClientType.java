package ru.pusk.music.player.entity;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClientType {
    BOX_PLAYER("boxPlayer"),
    WEB("web"),
    MOBILE("mobile"),
    UNKNOWN("unknown")
    ;

    @JsonValue
    private final String value;
}
