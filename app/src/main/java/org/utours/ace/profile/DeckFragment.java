package org.utours.ace.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.utours.ace.R;
import org.utours.ace.profile.model.ProfileCard;
import org.utours.ace.profile.model.ProfileCardRarity;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DeckFragment extends Fragment {

    List<ProfileCard> profileCards;

    public DeckFragment() {
        Supplier<ProfileCard> getUbel = () ->
                new ProfileCard(
                        "Übel",
                        "",
                        20,
                        10,
                        5,
                        ProfileCardRarity.UR);

        profileCards = IntStream.range(0, 30)
                .mapToObj(value -> getUbel.get())
                .collect(Collectors.toList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deck, container, false);
        GridView gridView = view.findViewById(R.id.grid_cards);

        var cardGridAdapter = new CardGridAdapter(getContext(), profileCards);
        gridView.setAdapter(cardGridAdapter);

        return view;
    }


}