package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class InfoCommand implements Command {
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final Redirector redirector;
    private final String info = "Tennis_matchmaking_bot FAQ" +
           " \n" +
            "Сервис пока не предполагает подбор соперника, Вам необходимо договориться о рейтинговой игре с кем то лично.\n" +
            "Формат игры определяют игроки самостоятельно, это может быть партия до 11 очков, до 21 очков или серия партий.\n" +
            "Игрокам необходимо явно договориться о предстоящей рейтинговой игре.\n" +
            " \n" +
            "По принятию решения один из игроков нажимает \"Создать матч\" и выбирает оппонента из списка. \n" +
            "После окончания партии, игрок создавший игру указывает победителя в предложенном меню. \n" +
            "Проигравшей стороне будет предложено подтвердить свое поражение во избежание ошибки. \n" +
            "Просим проигравшую сторону быть мудрой и уметь проигрывать :) \n" +
            " \n" +
            "После подтверждения поражения рейтинг будет расчитан динамически и записан в таблице. \n" +
            "Чем больше рейтинг побежденного оппонента, тем больше Вы получите за победу.\n" +
            "Соответственно, проиграв сопернику с рейтингом ниже Вашего - вы потеряете большое количество рейтинга.  \n" +
            " \n" +
            "Пример:\n" +
            "Игрок1 (рейтинг 50) побеждает Игрок2(рейтинг 150) \n" +
            "Игрок1 получает +42 \n" +
            "Игрок2 теряет -42 \n" +
            " \n" +
            "Обратная ситуация: \n" +
            "Игрок1(рейтинг 50) проигрывает Игрок2(рейтинг 150) \n" +
            "Игрок1 теряет -22 \n"+
            "Игрок2 получает +22 \n"+
            " \n"+
            "Число потерянного и приобретенного рейтинга в рамках одного матча всегда равно. \n"+
            "Исключение: рейтинг проигравшего достиг 0. \n"+
            " \n"+
            " \n"+
            "Сервис не имеет прямого отношения к администрации Fitness One, создан один из посетителей клуба на добровольной основе :) \n"+
            "С радостью отвечу на возникшие вопросы, приму пожелания по новому функционалу или жалобы на недочеты @xkefx  \n";










    @Autowired
    public InfoCommand(DeleteMessageService deleteMessageService, SendMessageService sendMessageService, Redirector redirector) {
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        deleteMessageService.deleteMessage(userId);
        sendMessageService.sendMessage(userId,info);
        deleteMessageService.banClearing(userId);


        redirector.redirectAtCommand("/menu",update);
    }
}
