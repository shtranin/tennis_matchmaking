package com.kefx.tennis_matchmaking.commands.specific_commands;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartCommand implements Command {
    private final Bot bot;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService service;
    private final Redirector redirector;
    private final String command =
            "Добро пожаловать в сервис учета рейтинговых игр в настольный теннис в Fitness one!\n"+
                    "\n"+
                    "1.Зарегистрируйтесь\n"+
                    "2.Найдите в зале соперника\n"+
                    "3.Договоритесь о рейтинговой игре в формате серии до двух побед\n"+
                    "4.Нажмите кнопку \"Cоздать матч\" и выберите Вашего соперника из списка \n"+
                    "5.После окончания партии укажите победителя в появившемся меню\n"+
                    "6.Рейтинг будет расчитан динамическим способом и записан\n"+
                    "\n"+
                    "С более подробной информацией можно ознакомиться по кнопке \"Инфо\"\n"+
                    "Приятных игр!\n"
        ;

    @Autowired
    public StartCommand(@Lazy Bot bot, DeleteMessageService deleteMessageService, SendMessageService service, Redirector redirector) {
        this.bot = bot;
        this.deleteMessageService = deleteMessageService;
        this.service = service;
        this.redirector = redirector;
    }


    @Override
    public void execute(Update update) {
        SendMessage sm = new SendMessage();
        sm.setChatId(Bot.getChatIdFromUpdate(update));
        sm.setText(command);

        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        redirector.redirectAtCommand("/menu",update);
    }
}
