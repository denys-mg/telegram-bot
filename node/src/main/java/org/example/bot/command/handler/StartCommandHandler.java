package org.example.bot.command.handler;

import static org.example.enums.BotCommand.HELP;

import org.example.bot.command.CommandHandler;
import org.example.enums.BotCommand;
import org.example.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class StartCommandHandler implements CommandHandler {

    private static final String HELLO_MESSAGE = """
            Greetings! All available commands you can see by %s.
            Also, after registration you can upload your photos or documents,
            bot will save them and give you downloading link
            """.formatted(HELP);

    @Override
    public String handleCommand(AppUser appUser) {
        return HELLO_MESSAGE;
    }

    @Override
    public BotCommand getSupportedCommand() {
        return BotCommand.START;
    }
}
