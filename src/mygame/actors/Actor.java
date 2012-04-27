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
package mygame.actors;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import mygame.utils.Sprite;

/**
 *
 * @author edba
 */
public class Actor extends Node 
{
    
    public enum Orientation //This is the sprite orientation
    {
        front,
        back,
        left,
        right
    }
    
    public enum State
    {
        idle,
        walking,
        jumping,
        falling,
        knockback,
        dead,
        attacking
    }
    
    protected Sprite sprite_;
    
    private AssetManager assetManager_;
            
    protected EventHandler eventHandler_;
    
    Orientation currentOrientation_ = Orientation.front;
    State currentState_ = State.idle;
    
    String name_;
    
    public Actor (AssetManager assetManager)
    {
        this(assetManager, 1.0f, 1.0f);
    }
    
    public Actor (AssetManager assetManager, float width, float height)
    {
        assetManager_ = assetManager;
        sprite_ = new Sprite(width, height);
        this.attachChild(sprite_);
    }
    
    public void setEventHandler(EventHandler eventHandler)
    {
        eventHandler_ = eventHandler;
    }
    
    public EventHandler getEventHandler()
    {
        return eventHandler_;
    }
    
    public void update (float tpf)
    {
        updateSprite();
    }
        
    public Sprite getSprite()
    {
        return sprite_;
    }
    
    public void setOrientation(Orientation orientation)
    {
        currentOrientation_ = orientation;
    }
    
    public void setState(State state)
    {
        currentState_ = state;
    }
    
    public State getState()
    {
        return currentState_;
    }
    
    private String orientationToString(Orientation orientation)
    {
        String string = null;
        
        switch (orientation)
        {
            case front:
                string = "back";
                break;
            case back:
                string = "front";
                break;
            case right:
                string = "right";
                break;
            case left:
                string = "left";
                break;
            default:
                string = "back";
                break;
        }
        
        return string;
    }
    
    private String stateToString(State state)
    {
        String string = null;
        
        switch (state)
        {
            case walking:
                string = "walk";
                break;
            case jumping:
                string = "jump";
                break;
            case idle:
                string = "idle";
                break;
            case falling:
                string = "fall";
                break;
            case dead:
                string = "dead";
                break;
            case attacking:
                string = "attack";
                break;
            case knockback:
                string = "fall";
                break;
            default:
                string = "idle";
                break;
        }
                
        return string;
    }
    
    public void updateSprite()
    {
        String orientation = orientationToString(currentOrientation_);
        String state = stateToString(currentState_);
        if(name_ != null && orientation != null && state != null)
        {
            sprite_.showMaterial("Materials/" + name_ + "/" + state + "_" + orientation + ".j3m");
        }
    }
    
    //try to load materials for all the states
    protected void initMaterials()
    {
        loadMaterial("Materials/" + name_ + "/walk_front.j3m");
        loadMaterial("Materials/" + name_ + "/walk_back.j3m");
        loadMaterial("Materials/" + name_ + "/walk_left.j3m");
        loadMaterial("Materials/" + name_ + "/walk_right.j3m");
        
        loadMaterial("Materials/" + name_ + "/jump_front.j3m");
        loadMaterial("Materials/" + name_ + "/jump_back.j3m");
        loadMaterial("Materials/" + name_ + "/jump_left.j3m");
        loadMaterial("Materials/" + name_ + "/jump_right.j3m");
        
        loadMaterial("Materials/" + name_ + "/idle_front.j3m");
        loadMaterial("Materials/" + name_ + "/idle_back.j3m");
        loadMaterial("Materials/" + name_ + "/idle_left.j3m");
        loadMaterial("Materials/" + name_ + "/idle_right.j3m");
        
        loadMaterial("Materials/" + name_ + "/fall_front.j3m");
        loadMaterial("Materials/" + name_ + "/fall_back.j3m");
        loadMaterial("Materials/" + name_ + "/fall_left.j3m");
        loadMaterial("Materials/" + name_ + "/fall_right.j3m");
        
        loadMaterial("Materials/" + name_ + "/dead_front.j3m");
        loadMaterial("Materials/" + name_ + "/dead_back.j3m");
        loadMaterial("Materials/" + name_ + "/dead_left.j3m");
        loadMaterial("Materials/" + name_ + "/dead_right.j3m");
        
        loadMaterial("Materials/" + name_ + "/attack_front.j3m");
        loadMaterial("Materials/" + name_ + "/attack_back.j3m");
        loadMaterial("Materials/" + name_ + "/attack_left.j3m");
        loadMaterial("Materials/" + name_ + "/attacl_right.j3m");
    }
    
    private void loadMaterial(String materialName)
    {
        try
        {
            Material mat = assetManager_.loadMaterial(materialName);
            if (mat != null)
            {
                sprite_.addMaterial(materialName, mat);
            }
        }catch(Exception ex)
        {
        }
    }
}
