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
public class Logger {

    public void log(Update update) {
        String name;
        String message;

        if(update.hasMessage()){
            message = update.getMessage().getText();
            name = update.getMessage().getFrom().getFirstName();
        }else{
           name = update.getCallbackQuery().getFrom().getFirstName();
           message = update.getCallbackQuery().getData();

        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        FileOutputStream fop = null;
        File file;

        try {
            file = new File("C:\\Users\\Kefx\\Desktop\\projects\\tennis_matchmaking\\src\\main\\resources\\logs.txt");
            fop = new FileOutputStream(file,true);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            String content = name + " " + message  + " " + dateFormat.format(date) + " \\\n";
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

           // System.out.println("Done");

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
