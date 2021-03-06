package com.kefx.tennis_matchmaking.entity;


import com.kefx.tennis_matchmaking.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;
import java.util.List;


@Entity()
public class UserEntity {
    @Id
    private Long id;
    private String name;
    private int rating;
    private boolean isDeleted;


    @OneToMany(cascade = CascadeType.ALL)
    private List<GameEntity> games;

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
    public void setDeleted(){
        isDeleted = true;
    }
    public void setIsNotDeleted(){isDeleted = false;}
    public boolean isDeleted(){
        return isDeleted;
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
        userEntity.setRating(0);
        return userEntity;
    }

    @Override
    public String toString() {
        return getName() + " рейтинг : " + getRating();
    }
}
