package org.utours.ace.infra.profile.firebase;

import org.utours.ace.common.annotation.Bean;

import java.util.List;

@Bean
class FirebasePlayerAccount {

    private String uid;
    private String username;
    private String email;
    private String description;
    private Integer level;
    private List<String> cards;
    private List<String> deck;

    public FirebasePlayerAccount() { }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public void setUsername(String username) {
        this.username = username;
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
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public List<String> getDeck() {
        return deck;
    }

    public void setDeck(List<String> deck) {
        this.deck = deck;
    }
}
