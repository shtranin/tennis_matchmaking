package com.kefx.tennis_matchmaking.commands.specific_commands;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import com.kefx.tennis_matchmaking.services.withDB.UpdateLastReceivedMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
@Transactional
public class MenuCommand implements Command {
    private final Bot bot;
    private final UserDBService userDBService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    private final DeleteMessageService deleteMessageService;
    private final UserStatementRepo userStatementRepo;
    @Autowired
    public MenuCommand(@Lazy Bot bot, UserDBService userDBService, UpdateLastReceivedMessageService updateLastReceivedMessageService, DeleteMessageService deleteMessageService, UserStatementRepo userStatementRepo) {
        this.bot = bot;
        this.userDBService = userDBService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
        this.deleteMessageService = deleteMessageService;
        this.userStatementRepo = userStatementRepo;
    }

    @Override
    public void execute(Update update) {
        realExecute(Bot.getPlayerIdFromUpdate(update));
    }

    public void realExecute(Long userId){
        String chatId = userId.toString();
        if(deleteMessageService.isExistWithOwnerId(userId) && !deleteMessageService.isClearingBanned(userId)){
            deleteMessageService.deleteMessage(userId);
        }

        UserStatementDocument userStatementDocument = userStatementRepo.findByOwnerId(userId);
        if(userStatementDocument != null){
            userStatementRepo.delete(userStatementDocument);
        }

        List<List<InlineKeyboardButton>> overlist = new ArrayList<>();
        if(!userDBService.isIdPresent(userId)){
            InlineKeyboardButton registrationButton = new InlineKeyboardButton();
            registrationButton.setText("Регистрация");
            registrationButton.setCallbackData("/registration");
            List<InlineKeyboardButton> listWithRegistrationButton = new ArrayList<>();
            listWithRegistrationButton.add(registrationButton);
            overlist.add(listWithRegistrationButton);
        }

        InlineKeyboardButton tableButton = new InlineKeyboardButton();
        tableButton.setText("Таблица игроков");
        tableButton.setCallbackData("/showTable");
        List<InlineKeyboardButton> listWithTable = new ArrayList<>();
        listWithTable.add(tableButton);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Создать матч");
        button1.setCallbackData("/createGame");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Моя статистика");
        button2.setCallbackData("/statistic");

        List<InlineKeyboardButton> list1 = new ArrayList<>();
        list1.add(button1);
        list1.add(button2);

        int currentUserRating = 0;
        if(userDBService.isIdPresent(userId)){
            currentUserRating = userDBService.getById(userId).getRating();
        }
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(currentUserRating+"");
        button3.setCallbackData("/rating");

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Настройки");
        button4.setCallbackData("/settings");

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Инфо");
        button5.setCallbackData("/info");

        List<InlineKeyboardButton> list2 = new ArrayList<>();
        list2.add(button3);
        list2.add(button4);
        list2.add(button5);

        overlist.add(listWithTable);
        overlist.add(list1);
        overlist.add(list2);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overlist);





        SendMessage sm = new SendMessage();
        sm.setText("Выберите действие:");
        sm.setChatId(chatId);
        sm.setReplyMarkup(markup);
        try{
            Message becameMessage = bot.execute(sm);
            updateLastReceivedMessageService.update(userId,chatId, becameMessage.getMessageId());
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
