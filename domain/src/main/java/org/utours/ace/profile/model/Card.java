package org.utours.ace.profile.model;

public record Card(
        String id,
        String name,
        String pictureId,
        Integer lifePoint,
        Integer attackPoint,
        Integer manaPoint,
        CardRarity rarity
) {

    public static class Builder {
        private String id;
        private String name;
        private String pictureId;
        private Integer lifePoint;
        private Integer attackPoint;
        private Integer manaPoint;
        private CardRarity rarity;

        private Builder() {

        }

        public static Builder of(Card card) {
            return of()
                    .withId(card.id)
                    .withName(card.name)
                    .withAttackPoint(card.attackPoint)
                    .withLifePoint(card.lifePoint)
                    .withManaPoint(card.manaPoint)
                    .withPictureId(card.pictureId)
                    .withRarity(card.rarity);
        }

        public static Builder of() {
            return new Builder();
        }

        public static Card copy(Card card) {
            return of(card).build();
        }

        public Builder withAttackPoint(Integer attackPoint) {
            this.attackPoint = attackPoint;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withLifePoint(Integer lifePoint) {
            this.lifePoint = lifePoint;
            return this;
        }

        public Builder withManaPoint(Integer manaPoint) {
            this.manaPoint = manaPoint;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPictureId(String pictureId) {
            this.pictureId = pictureId;
            return this;
        }

        public Builder withRarity(CardRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Card build() {
            return new Card(
                    id,
                    name,
                    pictureId,
                    lifePoint,
                    attackPoint,
                    manaPoint,
                    rarity
            );
        }

    }

}
