package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.services.forCommands.RegistrationService;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RegistrationCommand implements Command {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationCommand(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @Override
    public void execute(Update update) {
        registrationService.registration(update);
    }
}
