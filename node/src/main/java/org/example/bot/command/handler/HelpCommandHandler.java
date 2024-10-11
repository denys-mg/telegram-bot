package org.example.bot.command.handler;

import static org.example.enums.BotCommand.CANCEL;
import static org.example.enums.BotCommand.EMAIL_CHANGE;
import static org.example.enums.BotCommand.REGISTRATION;

import org.example.bot.command.CommandHandler;
import org.example.enums.BotCommand;
import org.example.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class HelpCommandHandler implements CommandHandler {

    private static final String HELP_MESSAGE = """
            All available commands:
            %s - cancel current operation
            %s - register a new user
            %s - change your email
            """.formatted(CANCEL, REGISTRATION, EMAIL_CHANGE);

    @Override
    public String handleCommand(AppUser appUser) {
        return HELP_MESSAGE;
    }

    @Override
    public BotCommand getSupportedCommand() {
        return BotCommand.HELP;
    }
}
