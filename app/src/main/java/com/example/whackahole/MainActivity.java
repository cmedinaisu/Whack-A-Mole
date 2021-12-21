package com.example.whackahole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main Activity for the Whack-A-Mole game
 * Player is given two button options; one to play the game and the other to end the game.
 * The game will display 12 spots for the mole to spawn; each spawn point will be random.
 * The game will continue to go faster as the game goes on.
 * There are three ways for the game to end. Either the player chooses to end the game through the end game button,
 * the player exceeds the total allowable number of misses the player can make in a game, or the observed time goes negative.
 * @author Cristofer Medina Lopez
 */
public class MainActivity extends AppCompatActivity {
    /**
     * gamemodel - Instance of the model that helps implement the some of the game logic and its necessary variables.
     * Include the handler and runnable for time as MutableLiveData that will be observed in the Main activity.
     */
    GameModel gamemodel;
    /**
     * playerScore - Textview that will display the real time score of the player in the game.
     */
    TextView playerScore;
    /**
     * numLives - Textview that will display the real time number of misses that the player has made in the game.
     */
    TextView numLives;
    /**
     * MoleHole - Clickable imageView that will display either a hole image or a mole image.
     */
    ImageView MoleHole;
    /**
     * Start - Button that will start the Whack-A-Mole game.
     */
    Button Start;
    /**
     * End - Button that will end the game and switch over to the EndGame activity.
     */
    Button End;
    /**
     * missLimit - Maximum number of misses that the player can make that will end the game.
     */
    final int missLimit = 6;
    /**
     * gameOver - Intent used to switch to the EndGame activity from the Main activity.
     */
    Intent gameOver;

    /**
     * onCreate Android method that will run when the Main Activity starts.
     * Method handlers the game functionality.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameOver = new Intent(this, EndGame.class);
        gamemodel = new ViewModelProvider(this).get(GameModel.class);

        playerScore = findViewById(R.id.PlayerScore);
        numLives = findViewById(R.id.PlayerLives);
        Start = findViewById(R.id.PlayBtn);
        End = findViewById(R.id.EndGameBtn);

        // Start game button
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamemodel.Start();
                view.setVisibility(view.GONE);
            }
        });

        // End game button
        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to switch EndGame activity
                gameOver.putExtra("key", gamemodel.getScore());
                gamemodel.removeCallback();
                startActivity(gameOver);
            }
        });

        // Create observer which will update the UI
        final Observer<Long> theObserver = spawnTime -> {

            // Update the UI - determine a random spot for mole to appear.
            if (spawnTime % gamemodel.getDifficultyTime() == 0) {
                switch(gamemodel.getSpot()) {
                    case 1: MoleHole = (ImageView) findViewById(R.id.MoleHole_1);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 2: MoleHole = (ImageView) findViewById(R.id.MoleHole_2);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 3: MoleHole = (ImageView) findViewById(R.id.MoleHole_3);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 4: MoleHole = (ImageView) findViewById(R.id.MoleHole_4);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 5: MoleHole = (ImageView) findViewById(R.id.MoleHole_5);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 6: MoleHole = (ImageView) findViewById(R.id.MoleHole_6);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 7: MoleHole = (ImageView) findViewById(R.id.MoleHole_7);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 8: MoleHole = (ImageView) findViewById(R.id.MoleHole_8);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 9: MoleHole = (ImageView) findViewById(R.id.MoleHole_9);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 10: MoleHole = (ImageView) findViewById(R.id.MoleHole_10);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 11: MoleHole = (ImageView) findViewById(R.id.MoleHole_11);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                    case 12: MoleHole = (ImageView) findViewById(R.id.MoleHole_12);
                        MoleHole.setImageResource(R.drawable.picmole);
                        break;
                }
            }

            // Increase score and trigger sound if mole is hit, no score increase if hole is hit.
            MediaPlayer mp = MediaPlayer.create(this, R.raw.hitsound);
            MoleHole.setOnClickListener(view -> {
                Bitmap currentImage = ((BitmapDrawable)MoleHole.getDrawable()).getBitmap();
                Drawable thisDrawable = getResources().getDrawable(R.drawable.picmole);
                Bitmap moleImage = ((BitmapDrawable) thisDrawable).getBitmap();
                if (currentImage.sameAs(moleImage)){
                    mp.start();
                    gamemodel.hitSuccessful();
                    MoleHole.setImageResource(R.drawable.hole);
                    // Game difficulty increases every 8 points of player score
                    if (gamemodel.getScore() % 8 == 0) {
                        gamemodel.increaseDifficulty();
                    }
                }
            });

            // When mole isn't hit - miss count is increased and mole image changes to a hole
            Bitmap currentImage = ((BitmapDrawable)MoleHole.getDrawable()).getBitmap();
            Drawable thisDrawable = getResources().getDrawable(R.drawable.picmole);
            Bitmap moleImage = ((BitmapDrawable) thisDrawable).getBitmap();
            if (spawnTime > (gamemodel.getDifficultyTime() -  50)) {
                if (currentImage.sameAs(moleImage)){
                    gamemodel.increaseMisses();
                    MoleHole.setImageResource(R.drawable.hole);
                }
            }

            String ps = "Player Score: " + gamemodel.getScore();
            String ms = "Misses: " + gamemodel.getMisses();
            playerScore.setText(ps);
            numLives.setText(ms);

            // Switch to EndGame activity if player meets max limit for misses or spawning time goes below zero.
            if (gamemodel.getMisses() == missLimit || spawnTime < 0) {
                // Intent to switch EndGame activity
                gameOver.putExtra("key", gamemodel.getScore());
                gamemodel.removeCallback();
                startActivity(gameOver);
            }
        };
        // Observe the LiveData passing into the activity
        gamemodel.getSpawnTime().observe(this, theObserver);
    }

}