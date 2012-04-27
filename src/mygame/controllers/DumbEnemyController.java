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
import java.util.Random;
import mygame.actors.Hero;


/**
 *
 * @author normenhansen
 */
public class DumbEnemyController extends EnemyPhysicsController{
    
    public boolean[] myControls = new boolean[TOTAL_CONTROLS];
    float currentTime;
    
    public DumbEnemyController() {
        super();
        init();
    }

    public DumbEnemyController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        init();
    }
     
    public void init()
    {
        this.speed = 0.3f;
        for(int i=0; i<myControls.length; i++){
            myControls[i] = false;
        }
    }
    
    @Override
    public void updateMovementWithCameraAndControls(Hero hero, Camera cam, boolean[] controls, float tpf){ 
        
        currentTime += tpf;
        
        if(currentTime > 6.0)
        {
            myControls[CONTROL_UP] = new Random().nextInt()%3==0;
            myControls[CONTROL_LEFT] = new Random().nextInt()%3==0;
            myControls[CONTROL_RIGHT] = new Random().nextInt()%3==0;
            myControls[CONTROL_DOWN] = new Random().nextInt()%3==0;
            myControls[CONTROL_JUMP] = new Random().nextInt()%10==0;
            currentTime = 0;
        }else if(currentTime > 3.0)
        {
            myControls[CONTROL_UP] = !myControls[CONTROL_UP];
            myControls[CONTROL_LEFT] = !myControls[CONTROL_LEFT];
            myControls[CONTROL_RIGHT] = !myControls[CONTROL_RIGHT];
            myControls[CONTROL_DOWN] = !myControls[CONTROL_DOWN];
            myControls[CONTROL_UP] = !myControls[CONTROL_UP];
        }
        
        super.updateMovementWithCameraAndControls(hero, myControls, tpf);
    }
     
}