package org.utours.ace.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.utours.ace.R;
import org.utours.ace.profile.model.ProfileCard;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DeckFragment extends Fragment {

    static List<ProfileCard> profileCards =
            IntStream.range(0, 30)
                    .mapToObj(operand -> new ProfileCard(
                            "Übel",
                            "",
                            20,
                            10,
                            5
                    )).collect(Collectors.toList());

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deck, container, false);
        GridView recyclerView = view.findViewById(R.id.grid_cards);

        var cardGridAdapter = new CardGridAdapter(getContext(), profileCards);
        recyclerView.setAdapter(cardGridAdapter);

        return view;
    }


}