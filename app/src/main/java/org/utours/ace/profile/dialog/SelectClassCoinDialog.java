package org.utours.ace.profile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.utours.ace.R;
import org.utours.ace.common.SelectableValue;
import org.utours.ace.infra.profile.android.AndroidClassCoinInformation;
import org.utours.ace.profile.deck.ClassCoinsAdapter;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.model.PlayerAccount;

import java.util.ArrayList;
import java.util.Optional;

public class SelectClassCoinDialog extends Dialog {

    private final Button cancelButton;
    private final Button selectButton;
    private final RecyclerView classCoinsRecycler;
    private final PlayerAccountService playerAccountService;
    private final ClassCoinsAdapter classCoinsAdapter;
    private final SelectableValue<ClassCoin> selectableValue;
    private Card selectedCard;
    private View selectedClassCoinView;

    public SelectClassCoinDialog(@NonNull Context context,
                                 PlayerAccountService playerAccountService,
                                 Card selectedCard) {

        super(context);
        setContentView(R.layout.dialog_select_class_coin);

        this.selectedCard = selectedCard;
        this.playerAccountService = playerAccountService;
        this.selectableValue = new SelectableValue<>();
        this.selectableValue.select(selectedCard.classCoin());

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        cancelButton = findViewById(R.id.btn_cancel);
        selectButton = findViewById(R.id.btn_select);
        classCoinsRecycler = findViewById(R.id.view_classCoins);

        var account = playerAccountService.currentPlayerAccount();
        classCoinsAdapter = new ClassCoinsAdapter(
                new ArrayList<>(account.classCoins()),
                selectedCard.classCoin(),
                new AndroidClassCoinInformation(context),
                this::onClickClassCoin);

        classCoinsRecycler.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        classCoinsRecycler.setAdapter(classCoinsAdapter);

        selectButton.setOnClickListener(v -> {
            onSelect();
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void onClickClassCoin(ClassCoin classCoin, View view) {
        if (selectedClassCoinView != null) {
            View arrowImage = selectedClassCoinView.findViewById(R.id.image_arrow);
            arrowImage.setVisibility(View.GONE);
        }
        selectedClassCoinView = view;
        View arrowImage = view.findViewById(R.id.image_arrow);
        arrowImage.setVisibility(View.VISIBLE);
        selectableValue.select(classCoin);
    }

    private void onSelect() {
        if (!selectableValue.getValue().isEmptyType()) {
            playerAccountService.setCurrentPlayerAccount(playerAccount -> {

                playerAccount.classCoins().remove(selectableValue.getValue());
                selectedCard = Card.Builder
                        .copyOf(selectedCard)
                        .withClassCoin(selectableValue.getValue())
                        .build();

                playerAccount.replaceCardInCards(selectedCard);

                return PlayerAccount.Builder.copyOf(playerAccount).build();
            });
        }
    }

}
