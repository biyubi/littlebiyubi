/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.actors;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.util.Random;
import mygame.Defines;
import mygame.controllers.HeroController;
import mygame.utils.SoundEffects;
/**
 *
 * @author edba
 */
public class Hero extends Actor
{      
    float attackTime = 0.0f;
    int damage = 0;
    
    //Knockback
    float knockBackTime = 0.0f;
    Vector3f knockBackDirection;
    
    //Hero controller
    public HeroController heroController;
    
    public enum Ability
    {
        jump,
        star,
        shoot,
        doubleJump
    }
    
    boolean abilitiesFlags[] = {false, false, false, false};
    
    public Hero (AssetManager assetManager, PhysicsSpace space, Vector3f startPos)
    {
        super(assetManager);
        name_ = "Hero";
        initMaterials();
        damage = 0; 
        heroController = new HeroController(new CapsuleCollisionShape(0.5f, 1.0f), .1f);
        heroController.setPhysicsLocation(new Vector3f(2, 2 - (1 - 31.f/25.0f) + 1, 1));
        setLocalTranslation(startPos);
        addControl(heroController);
        space.add(heroController); 
        
                //Cheats
        if(Defines.CHEAT_ALL_ABILITIES)
        {
            abilitiesFlags[0] = true;
            abilitiesFlags[1] = true;
            abilitiesFlags[2] = true;
            abilitiesFlags[3] = true;
        }
    }
      
    public void update (Camera cam, boolean[] controls, float tpf)
    {
        
        if(currentState_ == State.knockback){
            if (knockBackTime > 0.0f)
            {
                heroController.setWalkDirection(knockBackDirection);
                knockBackTime -= tpf;
            }else{
                heroController.setWalkDirection(Vector3f.ZERO);
                if(heroController.onGround()){
                    setState(State.idle);
                }
            } 
            super.setOrientation(Orientation.back);
        }else{
            heroController.updateMovementWithCameraAndControls(cam, controls, tpf);
        }
        if (attackTime > 0.0f)
        {
            super.setOrientation(Orientation.front);
             attackTime -= tpf;
        }
        
        
        super.update(tpf);
    }
    
    public boolean isAbilityActive(Ability ability)
    {
        return abilitiesFlags[ability.ordinal()];
    }
    
    public void activateAbility(Ability ability)
    {
        abilitiesFlags[ability.ordinal()] = true;
    }
    
        public void deactivateAbility(Ability ability)
    {
        abilitiesFlags[ability.ordinal()] = false;
    }
    
    @Override
    public void setState(State state)
    {
        if(currentState_ != State.dead)
        {
            if(state == State.knockback && currentState_ != State.knockback)
            {
                super.setState(state);
                knockBackTime = 0.01f*damage;
            }

            if(state == State.attacking)
            {
                super.setState(state);
                attackTime = 0.2f;
                knockBackTime = 0.0f;
            }

            if (attackTime <= 0.0f && knockBackTime <= 0.0f)
            {
                super.setState(state);
            }
        } 
    }
    
    @Override
    public void setOrientation(Orientation orientation)
    {
        if(currentState_ != State.dead)
        {
            super.setOrientation(orientation);
        }
    }
     
    public HeroController getHeroController()
    {
        return heroController;
    }
    
    public Vector3f getViewDirection()
    {
        return heroController.getViewDirection();
    }
    
    public void didGetHit(Vector3f direction)
    {
        if(currentState_ != State.knockback){
            setState(State.knockback);
            SoundEffects.getSharedInstance().playHit();
            knockBackDirection = direction.normalize();
            knockBackDirection = new Vector3f(knockBackDirection.x, 1.0f, knockBackDirection.z);
            damage++;
        }
    }
}
