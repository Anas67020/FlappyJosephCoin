package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	int manX;
	int manY;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	Texture[] man;
	ArrayList<Integer> coinXs = new ArrayList<Integer>();//x Positionen
	ArrayList<Integer> coinYs = new ArrayList<Integer>();// y Positionen
	ArrayList<Integer> bombXs = new ArrayList<Integer>();// x Position der Bomben
	ArrayList<Integer> bombYs = new ArrayList<Integer>(); // y Position der Bomben
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>(); //Körper zum Überlappen
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>(); //Bombenkörper
	Texture bomb;//Bild Bombe
	Texture coin; //Bild Münze
	Random random; //Zufallsgenerator
	int gamestate = 0;

	int coinCount=0;
	int bombCount;
	BitmapFont font;
	int score = 0;


	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		man = new Texture[8];
		man[0] = new Texture("josephRunning.png");
		man[1] = new Texture("josephR.png");
		man[2] = new Texture("josephRunning3.png");
		man[3] = new Texture("josephR4.png");
		man[4] = new Texture("josephR5.png");
		man[5] = new Texture("josephR6.png");
		man[6] = new Texture("josephR7.png");
		man[7] = new Texture("josephR8.png");


		manX = Gdx.graphics.getWidth() / 2 - man[0].getWidth() / 2;
		manY = Gdx.graphics.getHeight() / 7 - man[0].getHeight() / 7;
		random = new Random();
		coin = new Texture("coin.png");
		bomb = new Texture("kars.png");
		createCoin();

	}

	public void createBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int) height);
		//die Münze erscheint ganz rechts
		bombXs.add(Gdx.graphics.getWidth());
	}

	private void createCoin() {
		//Zufällige Höhe der Münze maximal wäre ganz oben
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int) height);
		//die Münze erscheint ganz rechts
		coinXs.add(Gdx.graphics.getWidth());
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10.0f);

	}



	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				if (pause < 7) { //Nur bei jedem 8. Frame machen wir etwas
					pause++;
				} else {
					pause = 0;
					if (manState < man.length - 1) {
						manState++;
					} else {
						manState = 0;
					}
				}
				batch.draw(man[manState], manX, manY);
				if (Gdx.input.justTouched()) {
					velocity -= 10;
				}
				velocity += gravity;
				manY -= velocity;
				if (manY < 0) {
					manY = 0;
				} if(manY > Gdx.graphics.getHeight()- man[manState].getHeight()){
					manY = Gdx.graphics.getHeight()- man[manState].getHeight();
				}


				if (gamestate == 0) {
					if (Gdx.input.justTouched()) {
						gamestate = 1; //wir beginnen das Spiel
					}
				} else if (gamestate == 1) {

					if (coinCount < 100) { //alle 100 Millisekunden soll eine Münze erscheinen
						coinCount++;
					} else {
						coinCount = 0; //wir fangen neu an zu zählen und erstellen eine Münze
						createCoin();
					}

					 if (bombCount < 250) {
						bombCount++;
					} else {
						bombCount = 0;
						createBomb();
					}




					coinRectangles.clear(); // alle Coin-Körper löschen
					bombRectangles.clear();

					//Münzen bewegen
					if (coinXs.size() > 0) { // wenn es Münzen gibt.
						//Log.(TAG,coinXs.size()+ " Anzahl Münzen");
						//für alle Münzen in der Arraylist
						for (int i = 0; i < coinXs.size(); i++) {
							batch.draw(coin, coinXs.get(i), coinYs.get(i));
							//Münze um 4 Pixel nach links verschieben
							coinXs.set(i, coinXs.get(i) - 4);

							//die Rectangles als Körper zur Collision Detection ermitteln
							coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));

						}
					}

					//Bomben bewegen
					for (int i = 0; i < bombXs.size(); i++) {
						batch.draw(bomb, bombXs.get(i), bombYs.get(i));
						//Münze um 4 Pixel nach links verschieben
						bombXs.set(i, bombXs.get(i) - 4);

						//die Rectangles als Körper zur Collision Detection ermitteln
						bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));

					}

					Rectangle manRectangle = new Rectangle(manX, manY, man[manState].getWidth(), man[manState].getWidth());


					for (int i = 0; i < bombRectangles.size(); i++) {
						if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
							Gdx.app.log("game", "bombe");
						}
					}

					for (int i = 0; i < coinRectangles.size(); i++) {
						if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
							Gdx.app.log("game", "coin");
						}
					}
					for (int i = 0; i < coinRectangles.size(); i++) {
						if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
							//Gdx.app.log("game","coin");
							score++;
							coinRectangles.remove(i);
							coinXs.remove(i);
							coinYs.remove(i);
							break;
						}
					}
					font.draw(batch, "Score: " + String.valueOf(score), 100, 200);
					for (int i = 0; i < bombRectangles.size(); i++) {
						if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
							//Gdx.app.log("game","bombe");
							gamestate = 2; //Spiel ist beendet
							bombRectangles.remove(i);
							bombXs.remove(i);
							bombYs.remove(i);
							break;
						}
					}
					/*if( manX < Gdx.graphics.getWidth() && manY < Gdx.graphics.getHeight()) {
						gamestate = 2;
					}*/

				}

				// Neustart des Spiels
				if (gamestate == 2) {
					score = 0;
					gamestate = 1;
					velocity = 0;
					coinXs.clear();
					coinYs.clear();
					coinRectangles.clear();
					bombXs.clear();
					bombYs.clear();
					coinCount = 0;
					bombCount = 0;
					bombRectangles.clear();
					manY = Gdx.graphics.getHeight() / 2;
				}

			batch.end();

		}

		@Override
		public void dispose() {
			batch.dispose();
			//Texturen freigeben
			for (int i = 0; i < man.length; i++) {
				man[i].dispose();
			}
			bomb.dispose();
			coin.dispose();
			font.dispose();
		}

	}