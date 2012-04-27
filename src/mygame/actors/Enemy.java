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
package mygame.actors;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.Control;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.controllers.DummyEnemyController;
import mygame.controllers.EnemyPhysicsController;
import mygame.controllers.ProximityController;
import mygame.utils.SoundEffects;

/**
 *
 * @author edba
 */
public class Enemy extends Actor
{
    
    public static CollisionShape s_shared_shape;
    public static float          s_shared_height;
    public static float          s_shared_proximity;
    
    int defense;
    int recover;
    int originalDefense;
    int damage = 0;
    float attackTime = 0.0f;
    float proximity = 0;
    
    //Needed to update physics state
    PhysicsSpace space;
    
    boolean physics; //If enemy interacts with stage, this is set by the controller.
    EnemyPhysicsController enemyController;
    String backupController;
    
    //Knockback
    float knockBackTime = 0.0f;
    Vector3f knockBackDirection;
    
    //Projectiles
    ProjectilesManager projectilesManager;
    
    //Hero
    Hero hero;
    
    public Enemy(AssetManager assetManager, String name, PhysicsSpace space)
    {
        super(assetManager);
        this.space = space;
        name_ = name;
        originalDefense = 5;
        defense = 5;
        recover = 0;
        physics = false; //Default false, if controller is EnemyPhysicsController then is true
        super.setOrientation(Orientation.back);
        initMaterials();
    }
    
    public Enemy (AssetManager assetManager, String name, float width, float height, PhysicsSpace space)
    {
        super(assetManager, width, height);
        this.space = space;
        name_ = name;
        defense = 5;
        super.setOrientation(Orientation.back);
        initMaterials();
    }
    
    public void setDefense(int defense){
        this.defense = defense;
        this.originalDefense = defense;
    }
    
    public void setProximity(Float proximity) {
        this.proximity = proximity;
    }
    
    public void setRecover(int recover){
        this.recover = recover;
    }
    
    public void setBackupController(String backupController)
    {
        this.backupController = backupController;
    }
    
    public void setProjectilesManager(ProjectilesManager projectilesManager)
    {
        this.projectilesManager = projectilesManager;
    }
    
    public ProjectilesManager getProjectilesManager()
    {
        return this.projectilesManager;
    }
    
    public void setHero(Hero hero)
    {
        this.hero = hero;
    }
    
    public Hero getHero()
    {
        return this.hero;
    }
    
    public EnemyPhysicsController getEnemyPhysicsController ()
    {
        return this.enemyController;
    }
    
    @Override
    public void addControl(Control control) {
        super.addControl(control);
        
        if(control instanceof EnemyPhysicsController)
        {
            enemyController = (EnemyPhysicsController)control;
            enemyController.setProximity(proximity);
            physics = true;
        }
    }
    
    @Override
    public void setState(State state)
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
        }
        
        if (attackTime <= 0.0f && knockBackTime <= 0.0f)
        {
            super.setState(state);
        }
     
    }
    
    public void update (Hero hero, Camera cam, boolean[] controls, float tpf)
    { 
        
        if(currentState_ == State.knockback){
            if (knockBackTime > 0.0f)
            {
                if(physics)
                {
                    enemyController.setWalkDirection(knockBackDirection);
                }
                knockBackTime -= tpf;
            }else{
                
                if(physics)
                {
                   enemyController.setWalkDirection(Vector3f.ZERO);
                    if(enemyController.onGround()){
                        setState(State.idle);
                    } 
                }else{
                    setState(State.idle);
                }
            } 
        }else{
            if(physics){
                enemyController.updateMovementWithCameraAndControls(hero, cam, controls, tpf);
            }else{
                
                float distance = hero.getLocalTranslation().distance(getLocalTranslation());
                        
                if(distance < proximity && getControl(ProximityController.class) != null){
                    defense = 0;
                    updateToBackupController();
                }        
            }
        }
        
        if (attackTime > 0.0f)
        {
             attackTime -= tpf;
        }

        super.update(tpf);
    }
    
    public void prepareSharedHelpers()
    {
        s_shared_proximity = proximity;
        s_shared_shape = new CapsuleCollisionShape(sprite_.width/2, sprite_.height);
    }
    
    public void didGetHit(Vector3f direction)
    {
        if(currentState_ != State.knockback){ 
            
            if(defense > 0)
            {
                SoundEffects.getSharedInstance().playEnemyHit();
                defense--;
            }else{
                SoundEffects.getSharedInstance().playEnemyHit2();
                System.out.println("defense 0");
                setState(State.knockback);
                knockBackDirection = direction.normalize();
                knockBackDirection = new Vector3f(knockBackDirection.x, 1.0f, knockBackDirection.z);
                damage++;
                
                updateToBackupController();
                
                if(recover > 0){
                    recover--;
                    defense = originalDefense;
                }
            }
            
            if(eventHandler_ != null){
                eventHandler_.triggerEventWithType(EventHandler.EVENT_KEY_DEFENSE, defense);
                eventHandler_.triggerEventWithType(EventHandler.EVENT_KEY_HIT);
            }
            
            if(defense == 0)
            {
                //Activate event of 0 defense
            }
            
            
            
        }
    }

    private void updateToBackupController(){
        if(defense == 0 && !physics)
            {
                //If event hasnt added physics, we'll do it manually
                
                if(getControl(RigidBodyControl.class) != null){
                    space.remove(this);
                    this.removeControl(RigidBodyControl.class);
                }
                
                System.out.println("backupcontroller is " +backupController );
                
                if(backupController != null){
                    
                    EnemyPhysicsController controller = null;

                    try 
                    {
                        Class controllerClass = Class.forName("mygame.controllers."+backupController);
                        prepareSharedHelpers();
                        controller = (EnemyPhysicsController) controllerClass.newInstance();
                    } 
                    catch (InstantiationException ex) 
                    {
                        Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } 
                    catch (IllegalAccessException ex) 
                    {
                        Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    catch (ClassNotFoundException ex)
                    {
                        Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    
                    if(controller != null) 
                        enemyController = controller;
                    else{
                        enemyController = new DummyEnemyController (new CapsuleCollisionShape(sprite_.width/2, sprite_.height), .1f);
                    }

                }else{
                    enemyController = new DummyEnemyController (new CapsuleCollisionShape(sprite_.width/2, sprite_.height), .1f);
                }
                
                this.addControl(enemyController);
                space.add(this);
            }
    }
    
}
