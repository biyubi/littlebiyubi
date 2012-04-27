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

import mygame.actors.Hero;
import mygame.actors.ProjectilesManager;
import mygame.utils.SoundEffects;

/**
 *
 * @author normenhansen
 */
public class HeroController extends CharacterControl{
    
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
    private float previousJetPackHeroYPosition = 0;
    private boolean didDoubleJump = false;
    
    private boolean[] exControls = new boolean[TOTAL_CONTROLS];

    public HeroController() {
    }

    public HeroController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
    }
    
    public void updateMovementWithCameraAndControls(Camera cam, boolean[] controls, float tpf){ 
        //Update logic
        Vector3f camDir = cam.getDirection().mult(0.1f);
        Vector3f upDir = new Vector3f(0,0.3f,0);
        Vector3f camLeftSlow = cam.getLeft().mult(0.1f);
        Vector3f camLeft = cam.getLeft().mult(0.1f);
        camDir.y = 0;
        camLeft.y = 0;
        viewDirection.set(camDir);
        walkDirection.set(0, 0, 0);
        
        Hero hero = (Hero)spatial;
        
        SoundEffects soundEffects = SoundEffects.getSharedInstance();
        
        //Landed to ground
        if(hero.getState() == Actor.State.jumping)
        {
            if(onGround())
            {
                soundEffects.playLand();
            }
        }
        
        if(onGround())
            hero.setState(Actor.State.idle);
        
        if (controls[CONTROL_LEFT]) {
            walkDirection.addLocal(camLeft);
            hero.setOrientation(Actor.Orientation.left);
            if(onGround())
            {
              hero.setState(Actor.State.walking); 
            }
        }else if (controls[CONTROL_RIGHT]) {
            walkDirection.addLocal(camLeft.negate());
            hero.setOrientation(Actor.Orientation.right);
            if(onGround())
            {
               hero.setState(Actor.State.walking); 
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
            hero.setOrientation(Actor.Orientation.front);
            if(onGround())
            {
               hero.setState(Actor.State.walking); 
            }
        }else
        if (controls[CONTROL_DOWN]) {
            walkDirection.addLocal(camDir.negate());
            hero.setOrientation(Actor.Orientation.back);
            if(onGround())
            {
              hero.setState(Actor.State.walking); 
            }
        }
       
        if(hero.isAbilityActive(Hero.Ability.jump))
        {

            if (controls[CONTROL_JUMP]){
                if(!controlWasPressed(CONTROL_JUMP)){
                    if(onGround()){
                        hero.setState(Actor.State.jumping); 
                        soundEffects.playJump();
                        jump();
                    }else if(hero.isAbilityActive(Hero.Ability.doubleJump)) {
                            isJetPack = true;
                    }
                }

            }
        }
        
        
        
        if(isJetPack){
            if(onGround()){
                isJetPack = false;
                didDoubleJump = false;
            }else{
                
                if(didDoubleJump == false)
                {
                    //Handel double jump here
                    //previousJetPackHeroYPosition = hero.getLocalTranslation().y;
                    didDoubleJump = true;
                    soundEffects.doubleJump();
                }
                
                /*if(hero.getLocalTranslation().y < previousJetPackHeroYPosition){
                    isJetPack = false;
                    didDoubleJump = false;
                }*/
                
                walkDirection.addLocal(upDir);
                
                
                
            }   
        }
        
        for(int i=0; i<controls.length; i++){
            exControls[i] = controls[i];
        }
        
        setWalkDirection(walkDirection);
        setViewDirection(viewDirection); 
    }
    
    public boolean controlWasPressed(int control){
        return exControls[control];
    }
    
    
}