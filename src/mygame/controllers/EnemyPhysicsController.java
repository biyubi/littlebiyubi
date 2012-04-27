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
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mygame.actors.Actor;
import mygame.actors.Enemy;
import mygame.actors.Hero;


/**
 *
 * @author normenhansen
 */
public class EnemyPhysicsController extends CharacterControl{
    
    //Controls
    public static final int TOTAL_CONTROLS        = 8;
    public static final int CONTROL_UP            = 0;
    public static final int CONTROL_DOWN          = 1;
    public static final int CONTROL_LEFT          = 2;
    public static final int CONTROL_RIGHT         = 3;
    public static final int CONTROL_JUMP          = 4;
    public static final int CONTROL_SHOT          = 5;
    public static final int CONTROL_CAMERA_LEFT   = 6;
    public static final int CONTROL_CAMERA_RIGHT  = 7;
        
    public boolean isJetPack = false;
    public boolean active = true;
    public float proximity = 0.0f;
    public float speed = 1.0f;
    public boolean canDoJetpack = false;
    
    public boolean[] exControls = new boolean[TOTAL_CONTROLS];

    public EnemyPhysicsController() {
        super(Enemy.s_shared_shape, Enemy.s_shared_height);
        
        this.proximity = Enemy.s_shared_proximity;
        
        if(proximity > 0){
            active = false;
        }
    }

    public EnemyPhysicsController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
    }
    
    public void updateMovementWithCameraAndControls(Hero hero, Camera cam, boolean[] controls, float tpf){ 
        
        if(!active){
            float distance = hero.getLocalTranslation().distance(spatial.getLocalTranslation());
            if(distance < proximity){
                active = true;
            }
            return;
        }
        
        //Update logic
        Vector3f camDir = cam.getDirection().mult(0.1f*speed);
        Vector3f upDir = new Vector3f(0,0.3f*speed,0);
        Vector3f camLeftSlow = cam.getLeft().mult(0.1f*speed);
        Vector3f camLeft = cam.getLeft().mult(0.1f*speed);
        camDir.y = 0;
        camLeft.y = 0;
        viewDirection.set(camDir);
        walkDirection.set(0, 0, 0);
        
        Enemy enemy = (Enemy)spatial;
        enemy.setState(Actor.State.idle);
        
        if (controls[CONTROL_LEFT]) {
            walkDirection.addLocal(camLeft);
            if(onGround())
            {
              enemy.setState(Actor.State.walking); 
            }
        }else if (controls[CONTROL_RIGHT]) {
            walkDirection.addLocal(camLeft.negate());
            if(onGround())
            {
               enemy.setState(Actor.State.walking); 
            }
        }
        if (controls[CONTROL_CAMERA_RIGHT]) {
            viewDirection.addLocal(camLeftSlow.mult(0.01f));
        }else
        if (controls[CONTROL_CAMERA_LEFT]) {
            viewDirection.addLocal(camLeftSlow.mult(0.01f).negate());
        }
        if (controls[CONTROL_UP]) {
            walkDirection.addLocal(camDir);
            if(onGround())
            {
               enemy.setState(Actor.State.walking); 
            }
        }else
        if (controls[CONTROL_DOWN]) {
            walkDirection.addLocal(camDir.negate());
            if(onGround())
            {
              enemy.setState(Actor.State.walking); 
            }
        }
       
        if (controls[CONTROL_JUMP]){
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
    
    public void updateMovementWithCameraAndControls(Hero hero, boolean[] controls, float tpf){ 
        
        
        if(!active){
            float distance = hero.getLocalTranslation().distance(spatial.getLocalTranslation());
            if(distance < proximity){
                active = true;
            }
            return;
        }
        
        //Update logic
        Vector3f right = new Vector3f(0.1f*speed,0,0);
        Vector3f through = new Vector3f(0,0,0.1f*speed);
        Vector3f upDir = new Vector3f(0,0.3f*speed,0);

        walkDirection.set(0, 0, 0);
        
        Enemy enemy = (Enemy)spatial;
        enemy.setState(Actor.State.idle);
        
        if (controls[CONTROL_LEFT]) {
            walkDirection.addLocal(right.negate());
            if(onGround())
            {
              enemy.setState(Actor.State.walking); 
            }
        }else if (controls[CONTROL_RIGHT]) {
            walkDirection.addLocal(right);
            if(onGround())
            {
               enemy.setState(Actor.State.walking); 
            }
        }

        if (controls[CONTROL_UP]) {
            walkDirection.addLocal(through.negate());
            if(onGround())
            {
               enemy.setState(Actor.State.walking); 
            }
        }else
        if (controls[CONTROL_DOWN]) {
            walkDirection.addLocal(through);
            if(onGround())
            {
              enemy.setState(Actor.State.walking); 
            }
        }
       
        if (controls[CONTROL_JUMP]){
            if(!controlWasPressed(CONTROL_JUMP)){
                if(onGround()){
                    jump();
                }else{
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
    
    public boolean controlWasPressed(int control){
        return exControls[control];
    }

    public void setProximity(float proximity) {
        this.proximity = proximity;
        if(proximity > 0){
            active = false;
        }
    }
    
    
}