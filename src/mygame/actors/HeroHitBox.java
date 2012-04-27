/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.actors;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;

/**
 *
 * @author diuxddr
 */
public class HeroHitBox extends Actor
{
    HeriHitBoxController controller;
    Hero hero;

    protected boolean shouldEnd;
    
    public HeroHitBox(AssetManager assetManager, Hero hero){
        super(assetManager);
        sprite_.setCullHint(CullHint.Always); //Never show
        name_ = "Hero";
        initMaterials();
        
        Vector3f size = new Vector3f(0.70f, 1.0f,0.70f);
        CollisionShape shape = new BoxCollisionShape(size);
        controller = new HeriHitBoxController(shape, hero);
        this.addControl(controller);
    }
  
}


class HeriHitBoxController extends GhostControl
{
    Hero hero;
    
    public HeriHitBoxController(CollisionShape shape, Hero hero) {
        super(shape);
        //setKinematic(true);
        //setKinematicSpatial(true);
        this.hero = hero;
    }
    
 
    @Override
    public void update(float tpf) {
        super.update(tpf);  
        spatial.setLocalTranslation(hero.getLocalTranslation());
    }
    
}