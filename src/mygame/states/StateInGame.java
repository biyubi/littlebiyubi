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
package mygame.states;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import mygame.utils.Level;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.CameraControl.ControlDirection;
import java.util.ArrayList;
import mygame.Defines;
import mygame.GameState;
import mygame.actors.Actor.Orientation;
import mygame.actors.Actor.State;
import mygame.actors.Enemy;
import mygame.actors.Hero;
import mygame.actors.EventHandler;
import mygame.actors.HeroHitBox;
import mygame.actors.Item;
import mygame.actors.Projectile;
import mygame.actors.ProjectilesManager;
import mygame.controllers.FaceToCamController;
import mygame.controllers.HeroController;
import mygame.utils.Hud;
import mygame.utils.SoundEffects;
import mygame.utils.Sprite;

/**
 *
 * @author diuxddr
 */
public class StateInGame extends GameState implements ActionListener, PhysicsCollisionListener {
    
    private static String current_level = "level1";
    private static boolean endLevel = false;
    private static boolean endGame = false;
    
    private BulletAppState bulletAppState;
    Level level_;
    Hero hero;
    Sprite heroShadow_;
    CameraNode playerCamera_;
    
    //Projectiles Manager for Hero
    ProjectilesManager heroProjectiles;
    ProjectilesManager enemiesProjectiles;
    
    //Hud
    Hud hud_;
    
    //Sound Effects
    SoundEffects soundEffects_;
    
    //Controls
    boolean[] controls = new boolean[HeroController.TOTAL_CONTROLS];
    
    //Failed
    boolean hasFailed = false;
   
    
    
    public static void loadNextLevel(String level)
    {        
        if(level.equalsIgnoreCase("THE_END"))
        {
            endGame = true;
            current_level = "level1";
        }else{
            current_level = level;
        }
        endLevel = true;
    }
    
    @Override
    public void initState() {
        
        bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        
        if(Defines.DEBUG_COLLISIONS)
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
            initLevel();
            initHero(level_.getHeroStartPosition());
            initEnemies();
            initItems();
            initCamera();
            initAmbientLight();
            initKeys();

        hud_ = new Hud(app.getGuiNode(), assetManager);
        
        if(endGame)
            hud_.showTheEnd();
        
        soundEffects_ = SoundEffects.getSharedInstance();
        soundEffects_.playMusicLevel(current_level);
    }
    
    public void initLevel()
    {
        level_ = new Level(assetManager, bulletAppState.getPhysicsSpace(), current_level);
        rootNode.attachChild(level_.getMap());
    }
    
    public void initEnemies()
    {  
        enemiesProjectiles = ProjectilesManager.createEnemiesProjectilesManager(assetManager, bulletAppState.getPhysicsSpace(), cam);
        rootNode.attachChild(enemiesProjectiles);
        
        ArrayList<Enemy> enemies = level_.getEnemies();
        
        for (int i = 0; i < enemies.size(); i++)
        {
            FaceToCamController faceController = new FaceToCamController(cam);
            Enemy enemy = enemies.get(i);
            enemy.getSprite().addControl(faceController);
            enemy.setProjectilesManager(enemiesProjectiles);
            enemy.setHero(hero);
        }
    }
    
    void initItems()
    {
        ArrayList<Item> items = level_.getItems();
        
        for (int i = 0; i < items.size(); i++)
        {
            FaceToCamController faceController = new FaceToCamController(cam);
            items.get(i).getSprite().addControl(faceController);
        }
    }
    
    public void initHero(Vector3f playerPos)
    {  
        hero = new Hero(assetManager, bulletAppState.getPhysicsSpace(), playerPos);
        rootNode.attachChild(hero);

        heroProjectiles = ProjectilesManager.createHeroProjectilesManager(assetManager, bulletAppState.getPhysicsSpace(), cam);
        rootNode.attachChild(heroProjectiles);
        
        HeroHitBox hitbox = new HeroHitBox(assetManager, hero);
        rootNode.attachChild(hitbox);
        bulletAppState.getPhysicsSpace().add(hitbox);
        
        Material mat = assetManager.loadMaterial("Materials/Hero/shadow.j3m");
        if (mat != null)
        {
            heroShadow_ = new Sprite(1, 1);
            heroShadow_.addMaterial("shadow", mat);
            rootNode.attachChild(heroShadow_);
        }
    }
    
    public void initCamera()
    {
        playerCamera_ = new CameraNode("Camera Node", app.getCamera());
        playerCamera_.setControlDir(ControlDirection.SpatialToCamera);
        hero.attachChild(playerCamera_);
        playerCamera_.setLocalTranslation(new Vector3f(0, 6, -10));
        playerCamera_.lookAt(hero.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    public void initAmbientLight()
    {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1f));
        rootNode.addLight(al);
    }
    
    @Override
    public void update(float tpf) {
        
        if (endLevel)
        {
            endLevel = false;
            if(!Defines.DEBUG_FPS && !Defines.DEBUG_STATS)
            {
                app.getGuiNode().detachAllChildren();
            }
            app.replaceGameState(new StateInGame());
        }
        
        heroProjectiles.update(tpf);
        enemiesProjectiles.update(tpf);
        
        hero.update(cam, controls, tpf);
        updateEnemies(tpf);
        updateHeroShadow();
           
        if(hero.getLocalTranslation().y < -20 && !hasFailed)
        {
            hero.setState(State.knockback);
            hasFailed = true;
            hud_.showLevelReset();
            SoundEffects.getSharedInstance().playDead();
        }
    }
    
    public void updateEnemies(float tpf)
    {
        ArrayList<Enemy> enemies = level_.getEnemies();
        Enemy enemy;
        
        for(int i=0 ;i<enemies.size(); i++)
        {
            enemy = enemies.get(i);
            enemy.update(hero, cam, controls,tpf);
            if(!hasFailed && enemy.getLocalTranslation().y < -20)
            {
                EventHandler eventHandler = enemy.getEventHandler();
                if(eventHandler != null)
                {
                    eventHandler.triggerEventWithType(EventHandler.EVENT_KEY_DESTROY);
                }
            }
        }
    }
    
    public void updateHeroShadow()
    {
        CollisionResults results = new CollisionResults();
        Vector3f heroLocation = hero.getLocalTranslation();
        heroShadow_.setLocalTranslation(new Vector3f(heroLocation.x, heroLocation.y + 2, heroLocation.z));
        Ray ray = new Ray(hero.getLocalTranslation(), new Vector3f(0, -1, 0));
        rootNode.collideWith(ray, results);
        if(results.size() > 0)
        {
            heroShadow_.setCullHint(CullHint.Inherit);
            CollisionResult firstResponder = results.getClosestCollision();
            
            Geometry responder = firstResponder.getGeometry();
            
            Vector3f location = firstResponder.getContactPoint();
            heroShadow_.setLocalTranslation(new Vector3f(location.x, location.y + 0.1f, location.z));
            heroShadow_.lookAt(new Vector3f(location.x, location.y + 10.0f, location.z), Vector3f.UNIT_Y);
        }
        else
        {
            heroShadow_.setCullHint(CullHint.Always);
        }
    }
   
    public void checkIfWallBehind()
    {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        rootNode.collideWith(ray, results);
        if(results.size() > 0){
            CollisionResult firstResponder = results.getClosestCollision();
            Geometry responder = firstResponder.getGeometry();
        
            if (responder == hero.getSprite()){
                System.out.println("IS SPRITE");
            }else{
                responder.getMaterial().setFloat("Alpha", 0.5f); // [1,128]  
                System.out.println("IS WALL");
            }
      
        }
    }
   
    @Override
    protected void endState() {
        heroProjectiles.end();
        enemiesProjectiles.end();
        inputManager.removeListener(this);
        app.getStateManager().detach(bulletAppState);
    }
        
    private void initKeys() {
        inputManager.addMapping("Left", 
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", 
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", 
                new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", 
                new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", 
                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("Shot", 
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addMapping("Reset", 
                new KeyTrigger(KeyInput.KEY_P));
        
        inputManager.addListener(this, "Right", "Left", "Up", "Down", "Reset");
        inputManager.addListener(this, "Jump", "Shot");
    }
    
    public void onAction(String binding, boolean value, float tpf) {
        
        if (binding.equals("Reset"))
        {
            app.replaceGameState(new StateInGame());
            return;
        }
        
        if(!hasFailed)
        {
            if (binding.equals("Left")) {
                controls[HeroController.CONTROL_LEFT] = value;
            }else if (binding.equals("Right")) {
                controls[HeroController.CONTROL_RIGHT] = value;
            }else if (binding.equals("Up")) {
                controls[HeroController.CONTROL_UP] = value;
            }else if (binding.equals("Down")) {
                controls[HeroController.CONTROL_DOWN] = value;
            }else if (binding.equals("Jump")) {
                controls[HeroController.CONTROL_JUMP] = value; 
            }else if (binding.equals("Shot")) {
                controls[HeroController.CONTROL_SHOT] = value;
                if(hero.isAbilityActive(Hero.Ability.shoot) && hero.getState() != State.knockback)
                {
                    hero.setState(State.attacking);
                    hero.setOrientation(Orientation.front);
                    if(value) 
                    {
                        heroProjectiles.shoot(hero.getLocalTranslation(),hero.getViewDirection());
                        soundEffects_.playShoot();
                    }
                }
            }
        }
        else
        {
            if (binding.equals("Shot"))
            {
                if(current_level.equalsIgnoreCase("level_bonus"))
                    current_level = "level2";
                app.replaceGameState(new StateInGame());
            }
        }
        
    }

    public void collision(PhysicsCollisionEvent event) {
        
        boolean item = ((event.getNodeA() instanceof Item) || (event.getNodeB() instanceof Item));
        boolean eventHandler = ((event.getNodeA() instanceof EventHandler) || (event.getNodeB() instanceof EventHandler));
        boolean thehero = ((event.getNodeA() instanceof HeroHitBox) || (event.getNodeB() instanceof HeroHitBox));
        boolean therealhero = ((event.getNodeA() instanceof Hero) || (event.getNodeB() instanceof Hero));
        boolean projectile = ((event.getNodeA() instanceof Projectile) || (event.getNodeB() instanceof Projectile));
        boolean enemy = ((event.getNodeA() instanceof Enemy) || (event.getNodeB() instanceof Enemy));
        
        if(projectile)
        {  
            if(enemy) collideProjectileWithEnemy(event);
            else if(thehero) collideProjectileWithHero(event);
            else collideProjectileWithWall(event);
        }else if(enemy){
            if(thehero) collideHeroWithEnemy(event);
        }
        else if (eventHandler)
        {
            if (thehero)
            {
                collideHeroWithEvent(event);
            }
        }
        else if (item)
        {
            if (thehero)
            {
                collideHeroWithItem(event);
            }
        }else if(hero.heroController.isJetPack && therealhero && !thehero){
            hero.heroController.isJetPack = false;
        } 
    }
    
    public void collideHeroWithItem(PhysicsCollisionEvent event)
    {
        Item item;
        
        if((event.getNodeA() instanceof Item))
        {
            item = (Item)event.getNodeA();
        }else{
            item = (Item)event.getNodeB();  
        }
        
        EventHandler eventHandler = item.getEventHandler();
        if(eventHandler != null)  
        {
            eventHandler.triggerEventWithType(EventHandler.EVENT_KEY_TOUCH);
        }
        
        //Dont Paint it anymore
        item.setCullHint(CullHint.Always);
        //Remove from space
        bulletAppState.getPhysicsSpace().remove(item);
        
        //play sound effecet
        SoundEffects.getSharedInstance().playItem();
        
        //Unlock ability
        String itemType = item.getType();
        if(itemType.compareTo(Item.ITEM_JUMP) == 0)
        {
            hero.activateAbility(Hero.Ability.jump);
            hud_.activateJump(true);
        }
        else if(itemType.compareTo(Item.ITEM_FIRE) == 0)
        {
            hero.activateAbility(Hero.Ability.shoot);
            hud_.activateShoot(true);
        }
        else if(itemType.compareTo(Item.ITEM_HEART) == 0)
        {
            //hero.activateAbility(Hero.Ability.jump);
        }
        else if(itemType.compareTo(Item.ITEM_DOUBLEJUMP) == 0)
        {
            hero.activateAbility(Hero.Ability.doubleJump);
            hud_.activateDoubleJump(true);
        }
        else if(itemType.compareTo(Item.ITEM_KEY) == 0)
        {
            //hero.activateAbility(Hero.Ability.jump);
        }
    }
        
    public void collideHeroWithEvent(PhysicsCollisionEvent event)
    {
        EventHandler eventHandler;
        
        if((event.getNodeA() instanceof EventHandler))
        {
            eventHandler = (EventHandler)event.getNodeA();
        }else{
            eventHandler = (EventHandler)event.getNodeB();  
        }
        
        eventHandler.triggerEventWithType(EventHandler.EVENT_KEY_TOUCH);
    }
    
    public void collideProjectileWithEnemy(PhysicsCollisionEvent event)
    {
        Projectile projectile;
        Enemy enemy;
        
        if((event.getNodeA() instanceof Projectile))
        {
            projectile = (Projectile)event.getNodeA();
            enemy = (Enemy)event.getNodeB();  
        }else{
            projectile = (Projectile)event.getNodeB();
            enemy = (Enemy)event.getNodeA();  
        }
        
        if(projectile != null && projectile.gettType() == 0)
        {
            System.out.println("hit");
            Vector3f projectilepos = projectile.getLocalTranslation();
            Vector3f enemypos = enemy.getLocalTranslation();
            Vector3f direction = new Vector3f(enemypos.x-projectilepos.x, enemypos.y-projectilepos.y, enemypos.z-projectilepos.z);

            enemy.didGetHit(direction);
            projectile.end();
        }
    }
    
    public void collideProjectileWithHero(PhysicsCollisionEvent event)
    {
        Projectile projectile;
        
        if((event.getNodeA() instanceof Projectile))
        {
            projectile = (Projectile)event.getNodeA();
        }else{
            projectile = (Projectile)event.getNodeB(); 
        }
        
        if(projectile != null && projectile.gettType() == 1)
        {
            Vector3f projectilepos = projectile.getLocalTranslation();
            Vector3f enemypos = hero.getLocalTranslation();
            Vector3f direction = new Vector3f(enemypos.x-projectilepos.x, enemypos.y-projectilepos.y, enemypos.z-projectilepos.z);

            hero.didGetHit(direction);
            projectile.end();
        }
    }
    
    public void collideProjectileWithWall(PhysicsCollisionEvent event)
    {
        Projectile projectile;
  
        if((event.getNodeA() instanceof Projectile))
        {
            projectile = (Projectile)event.getNodeA();
        }else{
            projectile = (Projectile)event.getNodeB(); 
        }

        projectile.end();
    }

    private void collideHeroWithEnemy(PhysicsCollisionEvent event) {
        
        Enemy enemy;
        
        if((event.getNodeA() instanceof Enemy))
        {
            enemy = (Enemy)event.getNodeA();
        }else{
            enemy = (Enemy)event.getNodeB(); 
        }
        
        Vector3f heropos = hero.getLocalTranslation();
        Vector3f enemypos = enemy.getLocalTranslation();
        
        Vector3f direction = new Vector3f(heropos.x-enemypos.x, heropos.y-enemypos.y, heropos.z-enemypos.z);
        
        hero.didGetHit(direction);
        
        if(enemy.getEventHandler() != null)
            enemy.getEventHandler().triggerEventWithType(EventHandler.EVENT_KEY_TOUCH);

    }
            
    
}
