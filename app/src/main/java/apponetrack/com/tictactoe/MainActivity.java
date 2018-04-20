package apponetrack.com.tictactoe;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static apponetrack.com.tictactoe.TicTacToeGame.ANDROID_PLAYER;
import static apponetrack.com.tictactoe.TicTacToeGame.HUMAN_PLAYER;
import static apponetrack.com.tictactoe.TicTacToeGame.getBoardSize;

public class MainActivity extends AppCompatActivity {

    private TicTacToeGame mGame;

    private Button mBoardButtons[];
    private Button mButtonReset;

    private TextView mInfoTextViews;
    private TextView mHumanCount;
    private TextView mAndroidCount;

    private int mHumanCounter = 0;
    private int mAndroidCounter = 0;

    private boolean mHumanFirst = true;
    private boolean mGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_constraint_layout);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mBoardButtons = new Button[getBoardSize()];
        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);
        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);
        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);

        mInfoTextViews = findViewById(R.id.information);
        mHumanCount = findViewById(R.id.humanCount);
        mAndroidCount = findViewById(R.id.androidCount);
        mButtonReset = findViewById(R.id.reset_button);

        mHumanCount.setText(Integer.toString(mHumanCounter));
        mAndroidCount.setText(Integer.toString(mAndroidCounter));

        mGame = new TicTacToeGame();
        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.newGame:
                startNewGame();
                break;
            case R.id.exitGame:
                MainActivity.this.finish();
                break;
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }

        return true;
    }

    private void startNewGame(){
        mGame.clearBoard();
        mButtonReset.setOnClickListener(new ResetButtonClickListener());

        for (int i = 0; i < mBoardButtons.length; i++){
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
            mBoardButtons[i].setBackground(getResources().getDrawable(R.drawable.button_back));
        }

        if (mHumanFirst){
            mInfoTextViews.setText(R.string.first_human);
            mHumanFirst = false;
        } else {
            mInfoTextViews.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(ANDROID_PLAYER, move);
            mHumanFirst = true;
        }

        mGameOver = false;
    }

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        if (player == HUMAN_PLAYER){
            mBoardButtons[location].setBackground(getResources().getDrawable(R.drawable.yellow_cross));

        } else  {
            mBoardButtons[location].setBackground(getResources().getDrawable(R.drawable.yellow_circle));
        }
    }

    private class ResetButtonClickListener implements  View.OnClickListener{

        @Override
        public void onClick(View view) {
            startNewGame();
        }
    }

    private class ButtonClickListener implements View.OnClickListener{

        int location;

        ButtonClickListener(int location){
            this.location = location;
        }

        @Override
        public void onClick(View view) {
            if (!mGameOver){
                if (mBoardButtons[location].isEnabled()){
                    setMove(HUMAN_PLAYER, location);

                    int winner = mGame.checkForWinner();

                    if (winner == 0){
                        mInfoTextViews.setText(R.string.turn_computer);
                        int move = mGame.getComputerMove();
                        setMove(ANDROID_PLAYER, move);
                        winner = mGame.checkForWinner();
                    }

                    if (winner == 0){
                        mInfoTextViews.setText(R.string.turn_human);
                    } else if (winner == 1){
                        mInfoTextViews.setText(R.string.result_tie);
                        mGameOver = true;
                    } else if (winner == 2){
                        mInfoTextViews.setText(R.string.result_human_wins);
                        mHumanCounter++;
                        mHumanCount.setText(Integer.toString(mHumanCounter));
                        mGameOver = true;
                    } else {
                        mInfoTextViews.setText(R.string.result_android_wins);
                        mAndroidCounter++;
                        mAndroidCount.setText(Integer.toString(mAndroidCounter));
                        mGameOver = true;
                    }
                }
            }
        }
    }
}
