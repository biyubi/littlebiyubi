/* ========================================================================
 * Copyright 2012 Barragan Corte Edgar Tonatiuh & Figueroa Salido Jesus Armando
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================
 */
package mygame.utils;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import mygame.MainSimpleApplication;

/**
 *
 * @author edba
 */
public class SoundEffects {
    
    AudioNode bgm3_;
    AudioNode bgm_diux_;
    
    AudioNode shoot_;
    AudioNode jump_;
    AudioNode doublejump_;
    AudioNode hit_;
    AudioNode hitEnemy_;
    AudioNode hitEnemy2_;
    AudioNode land_;
    AudioNode item_;
    AudioNode dead_;
    
    private static SoundEffects shareInstance_ = null;
    
    public SoundEffects ()
    {
        AssetManager assetManager = MainSimpleApplication.app.getAssetManager();
        
        bgm3_ = new AudioNode(assetManager, "Sounds/sfx/bgm3.ogg", false);
        bgm3_.setLooping(true);
        bgm3_.setPositional(true);
        bgm3_.setLocalTranslation(Vector3f.ZERO.clone());
        bgm3_.setVolume(0.2f);
        
        bgm_diux_ = new AudioNode(assetManager, "Sounds/sfx/diux_2.ogg", false);
        bgm_diux_.setLooping(true);
        bgm_diux_.setPositional(true);
        bgm_diux_.setLocalTranslation(Vector3f.ZERO.clone());
        bgm_diux_.setVolume(0.2f);
        

        shoot_ = new AudioNode(assetManager, "Sounds/sfx/shoot.wav", false);
        shoot_.setLooping(false);
        shoot_.setVolume(0.4f);
        
        jump_ = new AudioNode(assetManager, "Sounds/sfx/jump.wav", false);
        jump_.setLooping(false);
        jump_.setVolume(0.8f);
        
        doublejump_ = new AudioNode(assetManager, "Sounds/sfx/doublejump.wav", false);
        doublejump_.setLooping(false);
        doublejump_.setVolume(0.4f);
        
        hit_ = new AudioNode(assetManager, "Sounds/sfx/sfx_enemydeath.wav", false);
        hit_.setLooping(false);
        hit_.setVolume(0.4f);
        
        hitEnemy_ = new AudioNode(assetManager, "Sounds/sfx/sfx_enemy_hurt.wav", false);
        hitEnemy_.setLooping(false);
        hitEnemy_.setVolume(0.8f);
        
        hitEnemy2_ = new AudioNode(assetManager, "Sounds/sfx/sfx_enemydeath.wav", false);
        hitEnemy2_.setLooping(false);
        hitEnemy2_.setVolume(0.4f);
        
        land_ = new AudioNode(assetManager, "Sounds/sfx/land.wav", false);
        land_.setLooping(false);
        land_.setVolume(0.8f);
        
        item_ = new AudioNode(assetManager, "Sounds/sfx/item.wav", false);
        item_.setLooping(false);
        item_.setVolume(1.0f);
        
        dead_ = new AudioNode(assetManager, "Sounds/sfx/sfx_herodeath.wav", false);
        dead_.setLooping(false);
        dead_.setVolume(1.0f);
        
        MainSimpleApplication.app.getRootNode().attachChild(dead_);
    }
    
    public static SoundEffects getSharedInstance()
    {
        if (shareInstance_ == null)
        {
            shareInstance_ = new SoundEffects();
        }
        
        return shareInstance_;
    }
    
    public void playMusicLevel(String level)
    {
        if(level.compareTo("level1") == 0)
        {
            bgm_diux_.stop();
            bgm3_.play();
        }
        else if(level.compareTo("level2") == 0)
        {
            bgm_diux_.stop();
            bgm3_.play();
        }
        else if(level.compareTo("level3") == 0)
        {
            bgm_diux_.stop();
            bgm3_.play();
        }
        else if(level.compareTo("level4") == 0)
        {
            bgm3_.stop();
            bgm_diux_.play();
        }else if(level.compareTo("level_bonus") == 0)
        {
            bgm3_.stop();
            bgm_diux_.play();
        }
    }
    
    public void playShoot()
    {
        shoot_.playInstance();
    }
    
    public void playJump()
    {
        jump_.playInstance();
    }
    
    public void doubleJump()
    {
        doublejump_.playInstance();
    }
    
    public void playHit()
    {
        hit_.playInstance();
    }
    
    public void playLand()
    {
        land_.playInstance();
    }
    
    //hack temporal...
    static float lastItemPlay = 0.0f;
    
    public void playItem()
    {
        if(System.currentTimeMillis() - lastItemPlay > 0.5f){
            item_.playInstance();
            lastItemPlay = System.currentTimeMillis() ;
        }
        
    }
    
    public void playDead()
    {
        dead_.playInstance();
    }
    
    public void playEnemyHit()
    {
        hitEnemy_.playInstance();
    }
    
    public void playEnemyHit2()
    {
        hitEnemy2_.playInstance();
    }
    
    public void playEnemyShoot()
    {
        shoot_.playInstance();
    }
}
