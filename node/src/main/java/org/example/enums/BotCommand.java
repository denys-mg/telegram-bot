package org.example.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BotCommand {

    HELP("/help"),
    REGISTRATION("/registration"),
    EMAIL_CHANGE("/emailchange"),
    CANCEL("/cancel"),
    START("/start");

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    public static BotCommand fromValue(String value) {
        for (BotCommand command : BotCommand.values()) {
            if (command.value.equalsIgnoreCase(value)) {
                return command;
            }
        }
        return null;
    }
}
