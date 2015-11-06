package com.tistory.pflower.frontline.hud;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.BitmapText;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.object.Soldier;

public class HUDItem extends  TiledSprite{
	
	private Soldier character;
	private Text hudtext;
	
	BitmapText ammo;
	BitmapText mag;
	BitmapText price;
	private int weapon_id;
	private IEntity textContainer;
	private IEntity textContainer2;
	private IEntity textContainer3;
    private boolean amountOnly;
	
	public HUDItem(int i, int j, TiledTextureRegion _region,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(i, j, _region,vertexBufferObjectManager);
		
		setScaleCenterY(0);
		setScale(2f);
		setCurrentTileIndex(0);
		character = null;
	}

	public HUDItem(int i, int j, TiledTextureRegion _region,
			VertexBufferObjectManager vertexBufferObjectManager,
			Soldier character, int weapon_id, HUDLayer parent, boolean amountOnly) {
		super(i, j, _region,vertexBufferObjectManager);
		
		setScaleCenterY(0);
		//setScale(2f);
		setCurrentTileIndex(0);
		
		this.character = character;
		this.weapon_id = weapon_id;
		this.amountOnly = amountOnly;

        if(amountOnly) {
            ammo = new BitmapText(getWidth() / 2 + 4, getHeight() / 2 - 4, 1f, -15f, Integer.toString(character.myWeapons[weapon_id].ammo),
                    ResourceManager.getSharedInstance().getFontByName("mFont"), BaseActivity.getSharedInstance().getEngine(), null);
            textContainer = ammo.getText();
            attachChild(textContainer);
        }
        else {
            ammo = new BitmapText(8f, 20f, 1f, -15f, Integer.toString(character.myWeapons[weapon_id].ammo),
                    ResourceManager.getSharedInstance().getFontByName("mFont"), BaseActivity.getSharedInstance().getEngine(), null);
            textContainer = ammo.getText();
            attachChild(textContainer);


            if (character.myWeapons[weapon_id].IsInfinity) {
                mag = new BitmapText(8f, 30f, 1f, -15f, "XZ",
                        ResourceManager.getSharedInstance().getFontByName("mFont"), BaseActivity.getSharedInstance().getEngine(), null);
            } else {
                mag = new BitmapText(8f, 30f, 1f, -15f, Integer.toString(character.myWeapons[weapon_id].magagine),
                        ResourceManager.getSharedInstance().getFontByName("mFont"), BaseActivity.getSharedInstance().getEngine(), null);
            }

            textContainer2 = mag.getText();
            attachChild(textContainer2);

            price = new BitmapText(8f, 40f, 1f, -15f, Integer.toString(character.myWeapons[weapon_id].price),
                    ResourceManager.getSharedInstance().getFontByName("mFont"), BaseActivity.getSharedInstance().getEngine(), null);
            textContainer3 = price.getText();
            attachChild(textContainer3);
        }
    }
	
	public void update()
	{
		ammo.setText(Integer.toString(character.myWeapons[weapon_id].ammo));

        if(!amountOnly) {
            if(!character.myWeapons[weapon_id].IsInfinity)
                mag.setText(Integer.toString(character.myWeapons[weapon_id].magagine));
            price.setText(Integer.toString(character.myWeapons[weapon_id].price));
	    }
    }
}
