/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class Item extends Node
{
    //types
    
    public static final String ITEM_KEY             = "Key";
    public static final String ITEM_JUMP            = "Jump";
    public static final String ITEM_FIRE            = "Shoot";
    public static final String ITEM_DOUBLEJUMP      = "DoubleJump";
    public static final String ITEM_HEART           = "Heart";
    public static final String ITEM_STAR            = "Star";
    
    String type_ = null;
    
    Sprite sprite_;
    
    EventHandler eventHandler_;
    
     public Item(AssetManager assetManager, String Type)
     {
         type_ = Type;
         
         sprite_ = new Sprite();
         this.attachChild(sprite_);
         
         Material mat = assetManager.loadMaterial("Materials/Items/"+Type+".j3m");
         sprite_.addMaterial(Type, mat);
     }
     
     public Sprite getSprite()
     {
         return sprite_;
     }
    
    public String getType()
    {
        return type_;
    }
    
    /*
    public void setType(String type)
    {
        type_ = type;
    }
     */
    
    public void setEventHandler(EventHandler eventHandler)
    {
        eventHandler_ = eventHandler;
    }
    
    public EventHandler getEventHandler()
    {
        return eventHandler_ ;
    }
}
