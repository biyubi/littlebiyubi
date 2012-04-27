/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.utils;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import mygame.Defines;

/**
 *
 * @author edba
 */
public class Hud 
{
    Node guiNode_;
    
    private final int tileSize = 16;
    
    Picture mouse_left_;
    Picture mouse_right_;
    Picture shoot_;
    Picture jump_;
    Picture double_jump_;
    Picture star_;
    Picture end_;
    
    public Hud(Node guiNode, AssetManager assetManager)
    {
        guiNode_ = guiNode;
        
        mouse_left_ = new Picture("mouse_left");
        mouse_left_.setImage(assetManager, "Textures/Hud/mouse_left.png", true);
        mouse_left_.setWidth(tileSize);
        mouse_left_.setHeight(tileSize);
        mouse_left_.setPosition(5, 5 + tileSize + 5);
        //guiNode_.attachChild(mouse_left_);
        
        mouse_right_ = new Picture("mouse_right");
        mouse_right_.setImage(assetManager, "Textures/Hud/mouse_right.png", true);
        mouse_right_.setWidth(tileSize);
        mouse_right_.setHeight(tileSize);
        mouse_right_.setPosition(5 + tileSize + 5, 5 + tileSize + 5);
        //guiNode_.attachChild(mouse_right_);
        
        shoot_ = new Picture("shoot");
        shoot_.setImage(assetManager, "Textures/Items/Shoot.png", true);
        shoot_.setWidth(tileSize);
        shoot_.setHeight(tileSize);
        shoot_.setPosition( 5, 5 );
        //guiNode_.attachChild(shoot_);
        
        jump_ = new Picture("jump");
        jump_.setImage(assetManager, "Textures/Items/Jump.png", true);
        jump_.setWidth(tileSize);
        jump_.setHeight(tileSize);
        jump_.setPosition(5 + tileSize + 5, 5);
        //guiNode_.attachChild(jump_);
        
        double_jump_ = new Picture("double_jump");
        double_jump_.setImage(assetManager, "Textures/Items/DoubleJump.png", true);
        double_jump_.setWidth(tileSize);
        double_jump_.setHeight(tileSize);
        double_jump_.setPosition(5 + tileSize*2 + 5*2, 5);
        //guiNode_.attachChild(double_jump_);
        
        star_ = new Picture("star");
        star_.setImage(assetManager, "Textures/Items/Star.png", true);
        star_.setWidth(tileSize);
        star_.setHeight(tileSize);
        star_.setPosition(5 + tileSize*3 + 5*3, 5);
        //guiNode_.attachChild(star_);
        
        end_ = new Picture("end");
        end_.setImage(assetManager, "Textures/Hud/end.png", true);
        end_.setWidth(Defines.WINDOW_WIDTH);
        end_.setHeight(Defines.WINDOW_HEIGHT);
        end_.setPosition(0, 0);
        
        if(!Defines.DEBUG_FPS && !Defines.DEBUG_STATS)
        {
            guiNode_.detachAllChildren();
        }
    }
    
    public void activateJump(boolean activate)
    {
        if(activate)
        {
            guiNode_.attachChild(mouse_right_);
            guiNode_.attachChild(jump_);
        }
        else
        {
            mouse_right_.removeFromParent();
            jump_.removeFromParent();
        }
    }
    
    public void activateDoubleJump(boolean activate)
    {
        if(activate)
        {
            guiNode_.attachChild(double_jump_);
        }
        else
        {
            double_jump_.removeFromParent();
        }
    }
    
    public void activateShoot(boolean activate)
    {
        if(activate)
        {
            guiNode_.attachChild(mouse_left_);
            guiNode_.attachChild(shoot_);
        }
        else
        {
            mouse_left_.removeFromParent();
            shoot_.removeFromParent();
        }
    }
    
    public void activateStar(boolean activate)
    {
        if(activate)
        {
            guiNode_.attachChild(star_);
        }
        else
        {
            star_.removeFromParent();
        }
    }
    
    public void showLevelReset()
    {
        if(!Defines.DEBUG_FPS && !Defines.DEBUG_STATS)
        {
            guiNode_.detachAllChildren();
        }
        guiNode_.attachChild(mouse_left_);
        mouse_left_.setPosition( 5, 5 );
    }
    
    public void showTheEnd()
    {
        if(!Defines.DEBUG_FPS && !Defines.DEBUG_STATS)
        {
            guiNode_.detachAllChildren();
        }
        guiNode_.attachChild(end_);
    }
}
