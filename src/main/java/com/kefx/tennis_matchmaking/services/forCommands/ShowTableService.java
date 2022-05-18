package com.kefx.tennis_matchmaking.services.forCommands;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.withDB.UpdateLastReceivedMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowTableService {
    private final Bot bot;
    private final UserDBService userDBService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    private final Redirector redirector;
    private final SendMessageService sendMessageService;
    private final UserStatementRepo userStatementRepo;

    @Autowired
    public ShowTableService(@Lazy Bot bot, UserDBService userDBService, UpdateLastReceivedMessageService updateLastReceivedMessageService, Redirector redirector, SendMessageService sendMessageService, UserStatementRepo userStatementRepo) {
        this.bot = bot;
        this.userDBService = userDBService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
        this.redirector = redirector;
        this.sendMessageService = sendMessageService;
        this.userStatementRepo = userStatementRepo;
    }

    public void showTable(Update update){
        Long userId = Bot.getPlayerIdFromUpdate(update);
        String textOntable;
        String callBackDataAtPlayerButton;
        if(userStatementRepo.findByOwnerId(userId) != null){
            textOntable = "Выберите соперника";
            callBackDataAtPlayerButton = "rival ";
        }else{
            textOntable = "Общая таблица игроков";
            callBackDataAtPlayerButton = "player ";
        }

        List<UserEntity> allUsers = userDBService.getSortedByRatingUsers();
        if(allUsers.isEmpty()){
            sendMessageService.sendMessage(userId,"Таблица пока пуста");
            redirector.redirectAtCommand("/menu",update);
        }else{
            List<List<InlineKeyboardButton>> overList = new ArrayList<>();
            for(UserEntity entity : allUsers){
                if(entity.getId().equals(userId) && textOntable.equals("Выберите соперника")){
                    continue;
                }

                List<InlineKeyboardButton> innerList = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();

                button.setText(entity.toString());
                button.setCallbackData(callBackDataAtPlayerButton + entity.getId());

                innerList.add(button);
                overList.add(innerList);
            }

            InlineKeyboardButton backToMenuButton = new InlineKeyboardButton();
            backToMenuButton.setText("Назад к меню");
            backToMenuButton.setCallbackData("/menu");
            List<InlineKeyboardButton> listWithBacktoMenuButton = new ArrayList<>();
            listWithBacktoMenuButton.add(backToMenuButton);
            overList.add(listWithBacktoMenuButton);


            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);
            String chatId = Bot.getChatIdFromUpdate(update);

            SendMessage sm = new SendMessage();
            sm.setText(textOntable);
            sm.setChatId(chatId);
            sm.setReplyMarkup(markup);

            try {
                Message becameMessage = bot.execute(sm);
                updateLastReceivedMessageService.update(userId,chatId, becameMessage.getMessageId());
                userStatementRepo.deleteByOwnerId(userId);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

}
