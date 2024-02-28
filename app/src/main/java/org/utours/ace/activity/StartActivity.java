package org.utours.ace.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.utours.ace.R;

public class StartActivity extends AppCompatActivity {

    TextView startACETextView;
    Button parameterStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startACETextView = findViewById(R.id.textview_startAce);
        parameterStartButton = findViewById(R.id.btn_parameter);

        parameterStartButton.setOnClickListener(v -> openParameter());
        startACETextView.setOnClickListener(v -> redirectToGame());
    }

    private void redirectToGame() {
    }


    private void openParameter() {
    }


}