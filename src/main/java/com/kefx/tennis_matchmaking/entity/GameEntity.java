package com.kefx.tennis_matchmaking.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long ownerId;
    private String ownerName;
    private Long rivalId;
    private String rivalName;
    private boolean isWin;
    private int accruedRating;
    private Date date;

    public GameEntity() {

    }

    public Long getId() {
        return id;
    }

    public GameEntity(Long ownerId,String ownerName, Long rivalId,String rivalName, boolean isWin, int accruedRating, Date date) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.rivalId = rivalId;
        this.rivalName = rivalName;
        this.isWin = isWin;
        this.accruedRating = accruedRating;
        this.date = date;
    }

    @Override
    public String toString() {
        String result = isWin ? " ПОБЕДА рейтинг + " : " ПОРАЖЕНИЕ рейтинг - ";
        return "Игра против " + rivalName + result + accruedRating;
    }
}
