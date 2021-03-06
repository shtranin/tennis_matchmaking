package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.other.UserBusynessManager;
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
    private final UserBusynessManager userBusynessManager;
    private final SendMessageService sendMessageService;
    private final DeleteMessageService deleteMessageService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;

    public WinnerButton(@Lazy Bot bot, UserBusynessManager userBusynessManager, SendMessageService sendMessageService, DeleteMessageService deleteMessageService, UpdateLastReceivedMessageService updateLastReceivedMessageService) {
        this.bot = bot;
        this.userBusynessManager = userBusynessManager;
        this.sendMessageService = sendMessageService;
        this.deleteMessageService = deleteMessageService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);

        String[] callBackData = update.getCallbackQuery().getData().split(" ");
        Long winnerId = Long.parseLong(callBackData[1]);
        String winnerName = callBackData[2];
        Long loserId = Long.parseLong(callBackData[3]);
        String loserName = callBackData[4];
        String playersIds = winnerId + " " + loserId;

        List<List<InlineKeyboardButton>> overList = new ArrayList<>();
        List<InlineKeyboardButton> list = new ArrayList<>();

        InlineKeyboardButton acceptButton = new InlineKeyboardButton();
        acceptButton.setText("??????????????????????");
        acceptButton.setCallbackData("accept " + playersIds);
        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        cancelButton.setText("????????????????");
        cancelButton.setCallbackData("cancel " + playersIds);
        list.add(acceptButton);
        list.add(cancelButton);
        overList.add(list);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);

        SendMessage sm = new SendMessage();
        sm.setText("?????????????????????? ???????????????????? ???????????????? ???????????? ???? ?????????? " + winnerName);
        sm.setChatId(loserId.toString());
        sm.setReplyMarkup(markup);

        deleteMessageService.deleteMessage(loserId);
        try {
            Message becameMessage =bot.execute(sm);
            updateLastReceivedMessageService.update(loserId,loserId.toString(), becameMessage.getMessageId());
            if(userId.equals(winnerId)) {
                deleteMessageService.deleteMessage(winnerId);
                sendMessageService.sendMessage(userId, "???????? ???????? ?????????? ???????????????????????????????? ?? ?????????????? ?????????? ???????????????? ?????????? ????????, ??????  " + loserName + "  ???????????????????? ??????????????????");
            }
            userBusynessManager.makeUsersBusy(winnerId,loserId);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }




    }
}
