package com.kefx.tennis_matchmaking.logger;

import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class Logger {
    private final long CHAT_WITH_LOGS_ID = -571154905;
    private final SendLogsService sendLogsService;
    @Autowired
    public Logger(SendLogsService sendLogsService) {
        this.sendLogsService = sendLogsService;
    }

    public void log(Update update) {
        String firstName;
        String lastName;
        String message;

        if(update.hasMessage()){
            message = update.getMessage().getText();
            firstName = update.getMessage().getFrom().getFirstName();
            lastName = update.getMessage().getFrom().getLastName();
        }else if(update.hasCallbackQuery()){
           firstName = update.getCallbackQuery().getFrom().getFirstName();
           lastName = update.getCallbackQuery().getFrom().getLastName();
           message = update.getCallbackQuery().getData();

        }else{
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");
        Date date = new Date();

        String content = firstName + " " + lastName + " " + message + " " + dateFormat.format(date);
        sendLogsService.sendLogs(CHAT_WITH_LOGS_ID,content);
 //       FileOutputStream fop = null;
        //      File file;
//
//        try {
//            file = new File("/app/logs/logs.txt");
//            fop = new FileOutputStream(file,true);
//
//
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//
//            String content = name + " " + message  + " " + dateFormat.format(date) + " \\\n";
//            byte[] contentInBytes = content.getBytes();
//
//            fop.write(contentInBytes);
//            fop.flush();
//            fop.close();
//
//        } catch (IOException e) {
//           e.printStackTrace();
//        } finally {
//            try {
//                if (fop != null) {
//                    fop.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
