package org.utours.ace.infra.profile.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;
import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.StarDust;
import org.utours.ace.profile.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

@Bean
@FirebaseModel
class FirebasePlayerAccount {

    private String uid;
    private String username = PlayerAccount.DEFAULT_NAME;
    private String email;
    private String description = PlayerAccount.DEFAULT_DESCRIPTION;
    private Integer level = PlayerAccount.DEFAULT_LEVEL;
    private Integer starDustQuantity = StarDust.ZERO;
    private Integer ticketQuantity = Ticket.ZERO;
    private List<FirebaseCard> cards = List.of();
    private List<String> deck = List.of();
    private List<String> classCoins = PlayerAccount.DEFAULT_CLASS_COINS.stream()
            .map(ClassCoin::type)
            .map(ClassCoin.Type::name)
            .collect(Collectors.toList());

    public FirebasePlayerAccount() { }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username;
        }
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLevel(Integer level) {
        if (level != null) {
            this.level = level;
        }
    }

    public List<FirebaseCard> getCards() {
        return cards;
    }

    public void setCards(List<FirebaseCard> cards) {
        if (cards != null) {
            this.cards = cards;
        }
    }

    public List<String> getDeck() {
        return deck;
    }

    public void setDeck(List<String> deck) {
        if (deck != null) {
            this.deck = deck;
        }
    }

    public Integer getStarDustQuantity() {
        return starDustQuantity;
    }

    public void setStarDustQuantity(Integer starDustQuantity) {
        if (starDustQuantity != null) {
            this.starDustQuantity = starDustQuantity;
        }
    }

    public Integer getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(Integer ticketQuantity) {
        if (ticketQuantity != null) {
            this.ticketQuantity = ticketQuantity;
        }
    }

    public List<String> getClassCoins() {
        return classCoins;
    }

    public void setClassCoins(List<String> classCoins) {
        if (classCoins != null) {
            this.classCoins = classCoins;
        }
    }

}
