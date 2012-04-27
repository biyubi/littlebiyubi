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
public class MimicEnemyController extends EnemyPhysicsController{
    
    public boolean[] myControls = new boolean[TOTAL_CONTROLS];

    public MimicEnemyController() {
        super();
        init();
    }

    public MimicEnemyController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        init();
    }
    
    public void init()
    {
        for(int i=0; i<myControls.length; i++){
            myControls[i] = false;
        }
    }
    
    @Override
    public void updateMovementWithCameraAndControls(Hero hero, Camera cam, boolean[] controls, float tpf){ 
        
        myControls[CONTROL_UP] = controls[CONTROL_DOWN];
        myControls[CONTROL_DOWN] = controls[CONTROL_UP];
        myControls[CONTROL_LEFT] = controls[CONTROL_LEFT];
        myControls[CONTROL_RIGHT] = controls[CONTROL_RIGHT];
        myControls[CONTROL_JUMP] = controls[CONTROL_JUMP];
        
        super.updateMovementWithCameraAndControls(hero, cam, myControls, tpf);
    }
     
}