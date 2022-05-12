package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UpdateLastReceivedMessageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class WinnerButton implements Command {
    private final Bot bot;
    private final SendMessageService sendMessageService;
    private final DeleteMessageService deleteMessageService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;

    public WinnerButton(@Lazy Bot bot, SendMessageService sendMessageService, DeleteMessageService deleteMessageService, UpdateLastReceivedMessageService updateLastReceivedMessageService) {
        this.bot = bot;
        this.sendMessageService = sendMessageService;
        this.deleteMessageService = deleteMessageService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
    }

    @Override
    public void execute(Update update) {
        deleteMessageService.deleteMessage(update);
        String[] callBackData = update.getCallbackQuery().getData().split(" ");
        Long winnerId = Long.parseLong(callBackData[1]);
        String winnerName = callBackData[2];
        Long loserId = Long.parseLong(callBackData[3]);
        String loserName = callBackData[4];
        String infoAboutPlayers = winnerId + " " + winnerName + " "+ loserId + " " + loserName;

        List<List<InlineKeyboardButton>> overList = new ArrayList<>();
        List<InlineKeyboardButton> list = new ArrayList<>();

        InlineKeyboardButton acceptButton = new InlineKeyboardButton();
        acceptButton.setText("Подтверждаю");
        acceptButton.setCallbackData("accept " + infoAboutPlayers);
        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        cancelButton.setText("Отменить");
        cancelButton.setCallbackData("cancel " + infoAboutPlayers);
        list.add(acceptButton);
        list.add(cancelButton);
        overList.add(list);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);

        SendMessage sm = new SendMessage();
        sm.setText("Подтвердите пожалуйста проигрыш игроку по имени " + winnerName);
        sm.setChatId(loserId.toString());
        sm.setReplyMarkup(markup);

        try {
            Message becameMessage =bot.execute(sm);
            updateLastReceivedMessageService.update(loserId,loserId.toString(), becameMessage.getMessageId());
            sendMessageService.sendMessage(update,"Ваша игра будет зарегистрирована и рейтинг будет расчитан после того, как  " + loserName + "  подтвердит поражение");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }




    }
}
