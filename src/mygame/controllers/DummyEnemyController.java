/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

/**
 *
 * @author diuxddr
 */

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.renderer.Camera;
import mygame.actors.Hero;


/**
 *
 * @author normenhansen
 */
public class DummyEnemyController extends EnemyPhysicsController{
    
    public boolean[] myControls = new boolean[TOTAL_CONTROLS];

    public DummyEnemyController() {
    }

    public DummyEnemyController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        
        for(int i=0; i<myControls.length; i++){
            myControls[i] = false;
        }
        
    }
    
    @Override
    public void updateMovementWithCameraAndControls(Hero hero, Camera cam, boolean[] controls, float tpf){ 
        super.updateMovementWithCameraAndControls(hero, cam, myControls, tpf);
    }
     
}