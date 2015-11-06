package com.tistory.pflower.frontline.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.R;
import com.tistory.pflower.frontline.ResourceManager;

//placeHolder scene class for the main menu, currently only includes a start menu item 
public class MainMenuScene extends MenuScene implements
		IOnMenuItemClickListener {
	BaseActivity activity;
	final int MENU_START = 0;
	final int MENU_CREDIT = 1;
	final int MENU_RANKING = 2;
	final int MENU_EXIT = 3;

	public MainMenuScene() {
		
		super(BaseActivity.getSharedInstance().mCamera);
		
		setBackground(new Background(0.0f, 0.0f, 0.0f));
		
		activity = BaseActivity.getSharedInstance();
		BuildableBitmapTextureAtlas gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048,2048, TextureOptions.BILINEAR);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("Title/");
		// �÷��̾� �ൿ
		TextureRegion _sButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "StartButton.png");
		TextureRegion _cButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "CreditsButton.png");
		TextureRegion _rButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "RankingButton.png");
		TextureRegion _eButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ExitButton.png");
		
		TextureRegion _bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "TitleBack.bmp");
		TextureRegion _logo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "TitleLogo.png");
		//TextureRegion _mouse = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Mouse.png");
		
		
		try{
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
			gameTextureAtlas.load();
		}catch(Exception e){ e.printStackTrace();	}
		
		Sprite background = new Sprite(0, 0, _bg, activity.getVertexBufferObjectManager());
		background.setPosition(mCamera.getWidth() / 2 - background.getWidth() / 2, 0);
		attachChild(background);
		
		Sprite logo = new Sprite(0, 0, _logo, activity.getVertexBufferObjectManager());
		logo.setPosition(mCamera.getWidth() / 2 - logo.getWidth() / 2, 0);
		attachChild(logo);
		
		IMenuItem startButton = new SpriteMenuItem(MENU_START, _sButton, activity.getVertexBufferObjectManager());
		startButton.setPosition(mCamera.getWidth() / 2 - startButton.getWidth(), mCamera.getHeight() / 2 + 2 * startButton.getHeight());
		addMenuItem(startButton);
		
		IMenuItem creditButton = new SpriteMenuItem(MENU_CREDIT, _cButton, activity.getVertexBufferObjectManager());
		creditButton.setPosition(mCamera.getWidth() / 2, mCamera.getHeight() / 2 + 2 * creditButton.getHeight());
		addMenuItem(creditButton);
		
		IMenuItem rankButton = new SpriteMenuItem(MENU_RANKING, _rButton, activity.getVertexBufferObjectManager());
		rankButton.setPosition(mCamera.getWidth() / 2 - rankButton.getWidth(), mCamera.getHeight() / 2 + 3 * rankButton.getHeight());
		addMenuItem(rankButton);
		
		IMenuItem exitButton = new SpriteMenuItem(MENU_RANKING, _eButton, activity.getVertexBufferObjectManager());
		exitButton.setPosition(mCamera.getWidth() / 2, mCamera.getHeight() / 2 + 3 * exitButton.getHeight());
		addMenuItem(exitButton);
		
		setOnMenuItemClickListener(this);
		
		ResourceManager.getSharedInstance().getSoundByName("TitleBGM").play();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		
		ResourceManager.getSharedInstance().getSoundByName("TitleBGM").stop();
		switch (arg1.getID()) {
		case MENU_START:
			activity.setCurrentScene(new GameScene());
			return true;
		case MENU_CREDIT :
			activity.setCurrentScene(new CreditScene(mCamera));
			return true;
		case MENU_RANKING :
			activity.setCurrentScene(new RankScene(mCamera));
			return true;
		case MENU_EXIT :
			BaseActivity.getSharedInstance().onBackPressed();
			return true;
		default:
			break;
		}
		return false;
	}

}
