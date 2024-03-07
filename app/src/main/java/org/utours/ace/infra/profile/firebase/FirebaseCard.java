package org.utours.ace.infra.profile.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;

@Bean
@FirebaseModel
class FirebaseCard {

    private String id;
    private String name;
    private String pictureId;
    private Integer lifePoint;
    private Integer attackPoint;
    private Integer manaPoint;
    private String rarity;

    public FirebaseCard() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public Integer getLifePoint() {
        return lifePoint;
    }

    public void setLifePoint(Integer lifePoint) {
        this.lifePoint = lifePoint;
    }

    public Integer getAttackPoint() {
        return attackPoint;
    }

    public void setAttackPoint(Integer attackPoint) {
        this.attackPoint = attackPoint;
    }

    public Integer getManaPoint() {
        return manaPoint;
    }

    public void setManaPoint(Integer manaPoint) {
        this.manaPoint = manaPoint;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

}
