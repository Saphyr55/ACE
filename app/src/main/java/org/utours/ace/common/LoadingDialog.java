package org.utours.ace.common;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.utours.ace.R;


public class LoadingDialog extends Dialog {

    private final ValueAnimator animator;

    record State(String nameState) { }

    private State state = new State("1");

    public LoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);
        TextView loadingText = findViewById(R.id.text_loading);
        animator = ValueAnimator.ofFloat(0.f);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> state = stateMachine(loadingText, state));
    }

    @Override
    public void show() {
        super.show();
        animator.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private State stateMachine(TextView textView, State state) {
        String loadingString = getContext().getResources().getString(R.string.loading);
        return switch (state.nameState()) {
            case "1" -> {
                textView.setText(loadingString + ".");
                yield new State("2");
            }
            case "2" -> {
                textView.setText(loadingString + "..");
                yield new State("3");
            }
            case "3" -> {
                textView.setText(loadingString + "...");
                yield new State("1");
            }
            default -> null;
        };
    }

}
