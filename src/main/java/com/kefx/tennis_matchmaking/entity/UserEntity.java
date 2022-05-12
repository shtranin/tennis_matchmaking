package com.kefx.tennis_matchmaking.entity;


import com.kefx.tennis_matchmaking.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity()
public class UserEntity {
    @Id
    private Long id;
    private String name;
    private int rating;

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }



    public UserEntity() {
    }
    public UserEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public static UserEntity getNewEntityFromUpdate(Update update){
        Long id = Bot.getPlayerIdFromUpdate(update);
        String name;

        if(update.hasMessage()){
            name = update.getMessage().getFrom().getFirstName();
        }else{
            name = update.getCallbackQuery().getFrom().getFirstName();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        userEntity.setRating(1500);
        return userEntity;
    }

    @Override
    public String toString() {
        return getName() + " рейтинг : " + getRating();
    }
}
