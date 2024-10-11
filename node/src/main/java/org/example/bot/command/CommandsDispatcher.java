package org.example.bot.command;

import static org.example.enums.BotCommand.HELP;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.enums.BotCommand;
import org.example.model.AppUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandsDispatcher {

    private static final String UNKNOWN_COMMAND_MESSAGE =
            "Unknown command! All available commands you can see by " + HELP;

    private final List<CommandHandler> commandHandlers;

    public String dispatchToHandler(BotCommand command, AppUser appUser) {
        if (command == null) {
            return UNKNOWN_COMMAND_MESSAGE;
        }
        Optional<CommandHandler> commandHandler = commandHandlers.stream()
                .filter(handler -> handler.getSupportedCommand() == command)
                .findAny();

        if (commandHandler.isPresent()) {
            return commandHandler.get().handleCommand(appUser);
        }
        return UNKNOWN_COMMAND_MESSAGE;
    }
}
