package com.example.whackahole;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * End Game activity that displays after the player has reached the maximum number of misses or decides to
 * end the game.
 * This activity will display the final player score and and the highest score achieve from playing the game.
 * An option to play the game again is available at the bottom of the screen as a button - this option will
 * start the Main Activity.
 * @author Cristofer Medina Lopez
 */
public class EndGame extends AppCompatActivity {
    /**
     * display - textview that will display the score achieved from the last game.
     */
    TextView display;
    /**
     * highscore - textview that will display the highest score achieved over all games
     */
    TextView highScore;
    /**
     * playAgain - Clickable button that will switch to the main activity to play the game again.
     */
    Button playAgain;
    /**
     * Intent that is used to help switch to the Main Activity from the EndGame Activity.
     */
    Intent startMain;
    /**
     * sp - Shared preferences object that will save high score data and help it persist over many iterations of playing the game.
     */
    SharedPreferences sp;
    /**
     * spKey - key for accessing the high score data from Shared Preferences.
     */
    String spKey = "highScore";

    /**
     * onCreate method for the end game activity. Intents are used to handle switching between Main and EndGame.
     * The onCreate method will run when the EndGame activity starts.
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        sp = getSharedPreferences("highScore", MODE_PRIVATE);
        String temp = sp.getString(spKey, "0");
        startMain = new Intent(this, MainActivity.class);

        Intent extra = getIntent();
        int gameScoreNum = extra.getIntExtra("key", 0);

        //Integer highScoreNum = Integer.valueOf(temp);
        int highScoreNum = Integer.parseInt(temp);
        if (gameScoreNum > highScoreNum)
            highScoreNum  = gameScoreNum;

        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString(spKey,"" + highScoreNum).commit();

        highScore = findViewById(R.id.HighScore);
        display = findViewById(R.id.FinalScore);
        highScore.setText("High Score: " + highScoreNum);
        display.setText("Final Score: " + gameScoreNum);

        playAgain = findViewById(R.id.GameOver_Button);
        playAgain.setOnClickListener(view -> startActivity(startMain));

    }
}