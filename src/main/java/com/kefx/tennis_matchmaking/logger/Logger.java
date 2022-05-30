package com.kefx.tennis_matchmaking.logger;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class    Logger {

    public void log(Update update) {
        String name;
        String message;

        if(update.hasMessage()){
            message = update.getMessage().getText();
            name = update.getMessage().getFrom().getFirstName();
        }else if(update.hasCallbackQuery()){
           name = update.getCallbackQuery().getFrom().getFirstName();
           message = update.getCallbackQuery().getData();

        }else{
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");
        Date date = new Date();

        FileOutputStream fop = null;
        File file;

        try {
            file = new File("/app/logs/logs.txt");
            fop = new FileOutputStream(file,true);


            if (!file.exists()) {
                file.createNewFile();
            }


            String content = name + " " + message  + " " + dateFormat.format(date) + " \\\n";
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
