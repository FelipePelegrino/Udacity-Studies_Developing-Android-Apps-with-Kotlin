package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

enum class BuzzType(val pattern: LongArray) {
    CORRECT(CORRECT_BUZZ_PATTERN),
    GAME_OVER(GAME_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN)
}

class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }

    private lateinit var timer : CountDownTimer

    // The current word
    private var _word = MutableLiveData<String>()
    var word: LiveData<String> = _word
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()

    //encapsulando o objeto para que a classe externa possa apenas ler, e não manipular
    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _currentTime = MutableLiveData<String>()
    val currentTime : LiveData<String>
        get() = _currentTime

    private val _buzzTime= MutableLiveData<BuzzType>()
    val buzzTime : LiveData<BuzzType>
        get() = _buzzTime


    //caso minha variável fosse Long
//    val currentTimeString = Transformations.map(currentTime) { time ->
//        DateUtils.formatElapsedTime(time.toLong())
//    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        _eventGameFinish.value = false
        _buzzTime.value = BuzzType.NO_BUZZ
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = DateUtils.formatElapsedTime(millisUntilFinished/1000)
                if(millisUntilFinished/1000 == 3L) {
                    _buzzTime.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _eventGameFinish.value = true;
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
        _buzzTime.value = BuzzType.CORRECT
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
        _buzzTime.value = BuzzType.GAME_OVER
    }

    fun onBuzzComplete() {
        _buzzTime.value = BuzzType.NO_BUZZ
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }
}