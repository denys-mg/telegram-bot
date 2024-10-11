package org.example.bot.command;

import org.example.enums.BotCommand;
import org.example.model.AppUser;

public interface CommandHandler {
    String handleCommand(AppUser appUser);

    BotCommand getSupportedCommand();
}
