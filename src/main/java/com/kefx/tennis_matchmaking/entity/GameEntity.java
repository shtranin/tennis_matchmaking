package com.kefx.tennis_matchmaking.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToOne
    private UserEntity winner;
    private int accruedWinnerRating;
    @OneToOne
    private UserEntity loser;
    private int subtractedLoserRating;
    private Date date;


    public UserEntity getWinner() {
        return winner;
    }
    public int getAccruedWinnerRating() {
        return accruedWinnerRating;
    }
    public UserEntity getLoser() {
        return loser;
    }
    public int getSubtractedLoserRating() {
        return subtractedLoserRating;
    }
    public Date getDate() {
        return date;
    }
    public Long getId() {
        return id;
    }

    public GameEntity() {
    }
    public GameEntity(UserEntity winner, UserEntity loser, int[] accruedRating, Date date) {
        this.winner = winner;
        this.loser = loser;
        this.accruedWinnerRating = accruedRating[0];
        this.subtractedLoserRating = accruedRating[1];
        this.date = date;
    }


    public String textResultForUser(Long userId) {
        String result;
        String rival;
         if(userId.equals(winner.getId())){
             result = "] ПОБЕДА рейтинг + " + accruedWinnerRating;
             rival ="[" +  loser.getName();
        }else{
             result = "] ПОРАЖЕНИЕ рейтинг - " + subtractedLoserRating;
             rival = "[" +   winner.getName();
        }


        return rival + result;
    }
}
