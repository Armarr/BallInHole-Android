package be.robindeprins.ballinhole.model;

import java.util.Timer;
import java.util.TimerTask;

import android.widget.Toast;
import be.robindeprins.ballinhole.GameActivity;
import be.robindeprins.ballinhole.AccelerometerHandler;

public class Game implements Runnable{
	
	public static int UPDATES_PER_SECOND = 60;
	private Field field;
	private Chrono chrono;
	private GameActivity activity;
	private Timer updateTimer;
	private AccelerometerHandler sensor;
	
	public Game(int fieldHeight, int fieldWidth, GameActivity activity){
		field = new Field(fieldHeight, fieldWidth);
		sensor = new AccelerometerHandler(activity);
		chrono = new Chrono();
		chrono.addListener(this);
		this.activity = activity;
		updateTimer = new Timer();
		activity.updateDrawingPanel(this);
	}

	public Field getField() {
		return field;
	}
	
	public void startGame(){
		chrono.start();
		newTimer();
		long delay = 1000/Game.UPDATES_PER_SECOND;
		updateTimer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				update();
			}
			
		}, delay, delay);
	}

	public void pauseGame(){
		chrono.pause();
		newTimer();
	}
	
	public void stopGame(){
		chrono.stop();
		newTimer();
		field = new Field(field.getHeight(), field.getWidth());
		activity.updateDrawingPanel(this);
	}
	
	public void winGame(){
		stopGame();
		activity.showWinMessage();
	}
	
	public void loseGame(){
		stopGame();
		activity.showLoseMessage();
	}
	
	public void update(){
		field.update(-sensor.getAxisX(), sensor.getAxisY());
		
		for(Object o : field.getHoles().toArray()){
			Hole h = (Hole) o;
			if(field.distanceToBall(h) < 5){
				if(field.getHoles().removeFirst().equals(h)){
					if(field.getHoles().isEmpty())
						winGame();
				}else{
					loseGame();
				}
			}
		}
		activity.updateDrawingPanel(this);
	}
	
	@Override
	public void run() {
		activity.updateTimerText(chrono.getTimeInSeconds());
	}
	
	private void newTimer(){
		try{
			updateTimer.cancel();
			updateTimer = new Timer();
		}catch(IllegalStateException e){}
	}

}
