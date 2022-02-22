package com.example.triviaapp.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.triviaapp.R;
import com.example.triviaapp.listener.StartGameListener;
import com.example.triviaapp.model.Game;
import com.example.triviaapp.model.QuestionCategory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

public class NewGameBottomSheetFragment extends BottomSheetDialogFragment {

    private QuestionCategory category = QuestionCategory.GENERAL_KNOWLEDGE;
    private int numberOfQuestions = 15;
    private String difficulty = "easy";

    EditText usernameInput;
    ChipGroup questionsNumberRg;
    Chip questions15rb, questions30rb, questions60rb;
    Spinner themeSpinner;
    ChipGroup difficultyRg;
    Chip easyRb, mediumRb, hardRb;
    Button startGameBtn;

    private StartGameListener startGameListener;

    public NewGameBottomSheetFragment() {}

    public NewGameBottomSheetFragment(StartGameListener startGameListener) {
        this.startGameListener = startGameListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_fragment_new_game, container, false);

        usernameInput = view.findViewById(R.id.new_game_user_input);
        questionsNumberRg = view.findViewById(R.id.new_game_questions_number_rg);
        questions15rb = view.findViewById(R.id.new_game_questions_rb_15);
        questions30rb = view.findViewById(R.id.new_game_questions_rb_30);
        questions60rb = view.findViewById(R.id.new_game_questions_rb_60);
        themeSpinner = view.findViewById(R.id.new_game_theme_spinner);
        difficultyRg = view.findViewById(R.id.new_game_difficulty_rg);
        easyRb = view.findViewById(R.id.new_game_difficulty_easy);
        mediumRb = view.findViewById(R.id.new_game_difficulty_medium);
        hardRb = view.findViewById(R.id.new_game_difficulty_hard);
        startGameBtn = view.findViewById(R.id.new_game_start_game);

        startGameBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (!TextUtils.isEmpty(username)) {
                Game myGame = new Game(
                        username,
                        numberOfQuestions,
                        category,
                        difficulty);
                usernameInput.setText("");
                if (this.isVisible()) {
                    startGameListener.startGame(myGame);
                    this.dismiss();
                }
            } else {
                Snackbar.make(startGameBtn, "Username field must not be empty", Snackbar.LENGTH_LONG).show();
            }

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSpinner();
        setDifficultyRadios();
        setNumberOfQuestionsRadios();
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence selected = (CharSequence) parent.getSelectedItem();
                category = QuestionCategory.getQuestionCategoryFromLabel(selected.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDifficultyRadios() {
        difficultyRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == easyRb.getId()) {
                difficulty = "easy";
            } else if (checkedId == mediumRb.getId()) {
                difficulty = "medium";
            } else {
                difficulty = "hard";
            }

            Log.d("RADIO", "onCheckedChanged: " + difficulty);
        });
    }

    private void setNumberOfQuestionsRadios() {

        questionsNumberRg.check(questions15rb.getId());
        questionsNumberRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == questions15rb.getId()) {
                numberOfQuestions = 15;
            } else if (checkedId == questions30rb.getId()) {
                numberOfQuestions = 30;
            } else {
                numberOfQuestions = 60;
            }
        });
    }
}