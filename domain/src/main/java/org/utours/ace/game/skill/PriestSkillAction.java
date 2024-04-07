package org.utours.ace.game.skill;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.SkillAction;
import org.utours.ace.profile.feature.CardRegistry;
import org.utours.ace.profile.model.Card;

/**
 *
 */
public class PriestSkillAction implements SkillAction {

    // Le montant de heal
    public static int HEAL_POINT = 5;

    @Override
    public GameParty apply(GameParty gameParty, int slotFromPos, int slotToPos, boolean isOpponent) {

        // On recupere par default de plateur de joueur courant.
        Board boardRecepient = gameParty.currentPlayerBoard();

        // Si le joueur courant à clicker sur un slot de l'adversaire,
        // alors on change le plateau par celui l'adversaire.
        if (isOpponent) {
            boardRecepient = gameParty.opponentPlayerBoard();
        }

        // Par default on recupere le slot du joueur courant depuis le plateau.
        Slot slotFrom = gameParty.currentPlayerBoard().slots().get(slotFromPos);

        // On recupere le slot depuis le plateau assigner juste au dessus.
        Slot slotTo = boardRecepient.slots().get(slotToPos);

        // Il n'est pas normal d'appliquer ce skill alors qu'on a clicker sur case vide.
        // La case vide est géré dans org.utours.ace.game.actions.ActiveSkillViewTreatment
        // C'est aussi dans ActiveSkillViewTreatment qu'on active ce skill.
        // Mais le skill est determiné grâce au skill coin, par conséquent il existe une fonction
        // dans GameService qui prend en paramétre un card et renvoie le skill action correspondant.
        if (slotFrom instanceof Slot.SEmpty || slotTo instanceof Slot.SEmpty)
            throw new RuntimeException();

        // On converti le slotFrom à un Slot.SCard et on récupére.
        Card cardSender = ((Slot.SCard) slotFrom).card();
        Card cardRecipient = ((Slot.SCard) slotTo).card();

        // On recupere le maximum de point de vie de la carte receveur grâce au card registry
        // qui permet de recuperer la carte orignelle depuis son uid.
        int maxLifePointCardSender = CardRegistry.INSTANCE
                .fromUID(cardRecipient.uid())
                .orElseThrow(RuntimeException::new)
                .lifePoint();

        // On fait régenere les point de vie de la carte receveur.
        int newLifePointCardRecipient = cardRecipient.lifePoint() + HEAL_POINT;

        // Si les points de vie dépasse le max de point de vie de carte,
        // alors les nouveaux points de vie devient le max.
        if (newLifePointCardRecipient > maxLifePointCardSender) {
            newLifePointCardRecipient = maxLifePointCardSender;
        }

        // On copie la carte en créant un Card.Builder. ref copyOf(Card)
        // On assigne les nouveaux points de vie. ref withLifePoint(int)
        // Et enfin on construit la carte. ref build()
        Card newCardRecipient =
                Card.Builder.copyOf(cardRecipient)
                .withLifePoint(newLifePointCardRecipient)
                .build();

        // On recreer un nouveau slot depuis la nouvelle carte.
        // Et on l'assigne à la bonne position dans le tableau receveur.
        Slot newSlotRecipient = new Slot.SCard(newCardRecipient);
        boardRecepient.slots().set(slotToPos, newSlotRecipient);

        // On copie la party en créant un GameParty.Builder. ref copyOf(GameParty)
        GameParty.Builder gamePartyBuilder = GameParty.Builder.copyOf(gameParty);

        // Si le joueur courant à clicker sur un slot de l'adversaire,
        // Alors on assigne le nouveau tableau sur le plateau de l'adversaire,
        // Sinon on assigne le nouveau tableau sur le plateau du joueur courant,
        if (isOpponent) {
            gamePartyBuilder.withOpponentBoard(boardRecepient);
        } else {
            gamePartyBuilder.withCurrentPlayerBoard(boardRecepient);
        }

        // On contruit et renvoie la nouvelle partie.
        return gamePartyBuilder.build();
    }

}
