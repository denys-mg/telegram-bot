package org.example.bot.command.handler;

import lombok.RequiredArgsConstructor;
import org.example.bot.command.CommandHandler;
import org.example.enums.BotCommand;
import org.example.model.AppUser;
import org.example.service.AppUserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailChangeCommandHandler implements CommandHandler {

    private final AppUserService appUserService;

    @Override
    public String handleCommand(AppUser appUser) {
        return appUserService.handleEmailChange(appUser);
    }

    @Override
    public BotCommand getSupportedCommand() {
        return BotCommand.EMAIL_CHANGE;
    }
}
