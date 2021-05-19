package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	Fondo fondo;
	Nave nave;
	List<Alien> aliens;
	List<Bala> balasAEliminar;
	List<Alien> aliensAEliminar;
	Temporizador temporizadorNuevoAlien;
	ScoreBoard scoreboard;
	boolean gameover;


	@Override
	public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2f);

        inicializarJuego();
    }

    void inicializarJuego(){
		fondo = new Fondo();
		nave = new Nave();
		aliens = new ArrayList<>();
		temporizadorNuevoAlien = new Temporizador(120);
		balasAEliminar = new ArrayList<>();
		aliensAEliminar = new ArrayList<>();
		scoreboard = new ScoreBoard();

		gameover = false;
	}

	void update(){
		Temporizador.framesJuego += 1;

		if (temporizadorNuevoAlien.suena()) aliens.add(new Alien());

		if(!gameover) nave.update();

		for (Alien alien : aliens) alien.update();

		for (Alien alien : aliens) {
			for (Bala bala : nave.balas) {
				if (Utils.solapan(bala.x, bala.y, bala.w, bala.h, alien.x, alien.y, alien.w, alien.h)) {
					balasAEliminar.add(bala);
					aliensAEliminar.add(alien);
					nave.puntos++;
					break;
				}
			}

			if (!gameover && !nave.muerto && Utils.solapan(alien.x, alien.y, alien.w, alien.h, nave.x, nave.y, nave.w, nave.h)) {
				nave.morir();
				if (nave.vidas == 2){
					gameover = true;
				}
			}

			if (alien.x < -alien.w) aliensAEliminar.add(alien);
		}

		for (Bala bala : nave.balas)
			if (bala.x > 640)
				balasAEliminar.add(bala);

		for (Bala bala : balasAEliminar) nave.balas.remove(bala);
		for (Alien alien : aliensAEliminar) aliens.remove(alien);
		balasAEliminar.clear();
		aliensAEliminar.clear();

		if(gameover){
			int result = scoreboard.update(nave.puntos);
			if(result == 1){
				inicializarJuego();
			} else if (result == 2){
				Gdx.app.exit();
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		batch.begin();
		fondo.render(batch);
		nave.render(batch);
		for(Alien alien : aliens) alien.render(batch);
		font.draw(batch, "" + nave.vidas, 590, 440);
        font.draw(batch, "" + nave.puntos, 30, 440);

        if (gameover){
        	scoreboard.render(batch, font);
		}
        batch.end();
	}
}
