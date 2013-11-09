package be.robindeprins.ballinhole;

import be.robindeprins.ballinhole.model.Game;
import be.robindeprins.ballinhole.widgets.DrawingPanel;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {

	private DrawingPanel drawingPanel;
	private TextView timerText;
	
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		drawingPanel = (DrawingPanel) findViewById(R.id.drawingPanel);
		timerText = (TextView) findViewById(R.id.timerText);
		
		drawingPanel.post(new Runnable() {
		    @Override
		    public void run() {
		    	createGame(drawingPanel.getMeasuredWidth(), drawingPanel.getMeasuredHeight());
		    }
		});
	}
	
	private void createGame(int x, int y) {
    	game = new Game(x, y, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	public void playButtonOnClick(View v){
		game.startGame();
	}
	
	public void pauseButtonOnClick(View v){
		game.pauseGame();
	}
	
	public void stopButtonOnClick(View v){
		game.stopGame();
	}
	
	public void updateTimerText(int time){
		int seconds = time % 60;
		int minutes = time / 60;
		final String timeString = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				timerText.setText(timeString);			
			}}
		);
	}
	
	public void updateDrawingPanel(final Game game){
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				drawingPanel.update(game);
			}}
		);
		
	}

	public void showWinMessage() {
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_SHORT).show();
			}}
		);
	}

	public void showLoseMessage() {
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "You lost.", Toast.LENGTH_SHORT).show();
			}}
		);
	}

}
