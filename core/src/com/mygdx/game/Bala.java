package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bala {
    static Texture texture = new Texture("bala.png");
    float x, y, w, h, v;

    Bala(float xNave, float yNave){
        w = 8 * 6;
        h = 3 * 2;
        x = xNave;
        y = yNave;
        v = 12;
    }

    void update() {
        x += v;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, x, y, w, h);
    }
}
