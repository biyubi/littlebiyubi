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

package mygame.controllers;

/**
 *
 * @author diuxddr
 */

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mygame.actors.Actor;
import mygame.actors.Enemy;
import mygame.actors.Hero;


/**
 *
 * @author normenhansen
 */
public class FollowEnemyController extends EnemyPhysicsController{
    
    public boolean[] myControls = new boolean[TOTAL_CONTROLS];
    float currentTime;
    
    public FollowEnemyController() {
        super();
        init();
    }

    public FollowEnemyController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        init();
    }
     
    public void init()
    {
        this.speed = 0.5f;
        for(int i=0; i<myControls.length; i++){
            myControls[i] = false;
        }
        myControls[CONTROL_UP] = true;
    }
    
    @Override
    public void updateMovementWithCameraAndControls(Hero hero, Camera cam, boolean[] controls, float tpf){ 
        
        Vector3f newDirection = hero.getLocalTranslation().subtract(spatial.getLocalTranslation());
        
        newDirection.y = 0;
        
        if(!active){
            float distance = hero.getLocalTranslation().distance(spatial.getLocalTranslation());
            if(distance < proximity){
                active = true;
            }
            return;
        }
        
        //Update logic
        Vector3f camDir = newDirection.normalize().mult(0.1f*speed);
        Vector3f upDir = new Vector3f(0,0.3f*speed,0);
        Vector3f camLeftSlow = cam.getLeft().mult(0.1f*speed);
        Vector3f camLeft = cam.getLeft().mult(0.1f*speed);
        camDir.y = 0;
        camLeft.y = 0;
        viewDirection.set(camDir);
        walkDirection.set(0, 0, 0);
        
        Enemy enemy = (Enemy)spatial;
        enemy.setState(Actor.State.idle);
        
        if (myControls[CONTROL_LEFT]) {
            walkDirection.addLocal(camLeft);
            if(onGround())
            {
              enemy.setState(Actor.State.walking); 
            }
        }else if (myControls[CONTROL_RIGHT]) {
            walkDirection.addLocal(camLeft.negate());
            if(onGround())
            {
               enemy.setState(Actor.State.walking); 
            }
        }
        if (myControls[CONTROL_CAMERA_RIGHT]) {
            viewDirection.addLocal(camLeftSlow.mult(0.01f));
        }else
        if (myControls[CONTROL_CAMERA_LEFT]) {
            viewDirection.addLocal(camLeftSlow.mult(0.01f).negate());
        }
        if (myControls[CONTROL_UP]) {
            walkDirection.addLocal(camDir);
            if(onGround())
            {
               enemy.setState(Actor.State.walking); 
            }
        }else
        if (myControls[CONTROL_DOWN]) {
            walkDirection.addLocal(camDir.negate());
            if(onGround())
            {
              enemy.setState(Actor.State.walking); 
            }
        }
       
        if (myControls[CONTROL_JUMP]){
            if(!controlWasPressed(CONTROL_JUMP)){
                if(onGround()){
                    jump();
                }else if(canDoJetpack){
                    isJetPack = true;
                }
            }
            
        }
        
        if(isJetPack){
            if(onGround()){
                isJetPack = false;
            }else{
                walkDirection.addLocal(upDir);
            }   
        }
        
        for(int i=0; i<controls.length; i++){
            exControls[i] = controls[i];
        }
        
        setWalkDirection(walkDirection);
        setViewDirection(Vector3f.UNIT_Z); 
    }
     
}