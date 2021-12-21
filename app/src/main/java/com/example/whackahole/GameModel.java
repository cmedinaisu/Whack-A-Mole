package com.example.whackahole;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

/**
 * @author Cristofer Medina Lopez
 * Game Logic Implementation Class that extends ViewModel
 */
public class GameModel extends ViewModel {
    /**
     * gamerScore - The total number of times that the player whacks a mole when it appears on-screen.
     */
    private int gamerScore;
    /**
     * numMisses - Number of times a player doesn't hit a mole when it appears on-screen in the current game session.
     */
    private int numMisses;
    /**
     * difficultyTracker - A set amount of time that determines how long a mole will appear on-screen for the player to hit.
     * Keeps track of the difficulty of the game.
     */
    private int difficultyTracker;
    /**
     * timeSetter - Keep track of when the game starts to help with timing.
     */
    private long timeSetter;
    /**
     * spawnTime - Time that is observed in the Main Activity to handler events for the game.
     */
    private MutableLiveData<Long> spawnTime;
    /**
     * moleHandler - Helps process the runnable object to keep track of time needed to determine timing of events in game.
     */
    private Handler moleHandler;
    /**
     * rand - Random object that will produce random positions for the mole to spawn.
     */
    private Random rand;


    /**
     * Constructor for this class that extends ViewModel that is used in MainActivity to help implement the game.
     */
    public GameModel() {
        this.gamerScore = 0;
        this.numMisses = 0;
        this.difficultyTracker = 1800;
        rand = new Random();
        this.spawnTime = new MutableLiveData<>();
        moleHandler = new Handler(Looper.getMainLooper());
        this.spawnTime.setValue(0L);
    }

    /**
     * Runnable object for the game which will handle moles spawning on-screen and will determine
     * new spots for the moles.
     */
    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            spawnTime.postValue(spawnTime.getValue() + currentTime());
            timeSetter = System.currentTimeMillis();
            moleHandler.postDelayed(this, 100);
            if (spawnTime.getValue() > difficultyTracker) {
                spawnTime.postValue(0L);
            }
        }
    };

    /**
     * Get MutableLiveData value that the observer on the main activity will need to
     * handle the main UI and other logic for the game.
     * @return spawnTime
     */
    public LiveData<Long> getSpawnTime() {
        if (spawnTime == null) {
            spawnTime = new MutableLiveData<>();
        }
        return spawnTime;
    }

    /**
     * Keep track of the game session time
     * @return time as long type
     */
    private long currentTime() {
        return System.currentTimeMillis() - timeSetter;
    }

    /**
     * Get the current score that the player has in the current game session.
     * @return gamerScore
     */
    public int getScore() {
        return gamerScore;
    }

    /**
     * Increments the players score after times of successfully
     * hitting moles when they appear on screen.
     */
    public void hitSuccessful(){
        gamerScore++;
    }

    /**
     * Increments the number of times a mole isn't hit when it appears on screen.
     */
    public void increaseMisses() {
        numMisses++;
    }

    /**
     * Gets the number of misses the player currently has.
     * @return numMisses
     */
    public int getMisses() {
        return numMisses;
    }

    /**
     * Increases the difficulty of the current game session by decreasing the time given to whack
     * the moles which would also increase the spawn rate for the moles.
     * Time is decreased by 0.1 seconds each time the difficulty is increased.
     */
    public void increaseDifficulty() {
        moleHandler.removeCallbacks(runnable);
        difficultyTracker -= 100;
        spawnTime.setValue(0L);
        timeSetter = System.currentTimeMillis();
        moleHandler.postDelayed(runnable, 100);
    }

    /**
     * Get the current time that the player has to whack the mole before it is counts as a miss.
     * @return difficultyTracker
     */
    public int getDifficultyTime() { return difficultyTracker; }

    /**
     * Start the game session on the Main Activity
     */
    public void Start() {
        timeSetter = System.currentTimeMillis();
        moleHandler.postDelayed(runnable, 100);
    }

    /**
     * Get the randomized number to determine the spawn location for the moles.
     * @return spot - hole that the mole with spawn at.
     */
    public int getSpot() {
        return rand.nextInt(11) + 1;
    }


    /**
     * Remove callbacks in GameModel from the Main Activity to prevent Activity startup issues
     */
    public void removeCallback() {
        moleHandler.removeCallbacks(runnable);
    }
}
