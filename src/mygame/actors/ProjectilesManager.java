/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.actors;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author cuacCuacPC
 */
public class ProjectilesManager extends Node{
    
    public Projectile[] projectiles;
    
    protected float warmup;
    protected float speed;
    protected float currentTime;
    protected boolean canShoot;
    
    //Queue
    protected boolean didShoot;
    protected Vector3f direction;
    protected Vector3f position;
    
    
    public static ProjectilesManager createHeroProjectilesManager(AssetManager assetManager, PhysicsSpace space, Camera cam)
    {
        ProjectilesManager manager = new ProjectilesManager(assetManager, space, cam, 3);
        manager.warmup = 0.1f;
        return manager;
    }
    
    public static ProjectilesManager createEnemiesProjectilesManager(AssetManager assetManager, PhysicsSpace space, Camera cam)
    {
        ProjectilesManager manager = new ProjectilesManager(assetManager, space, cam, 50, 1);
        manager.warmup = 0.1f;
        return manager;
    }
    
    public ProjectilesManager(AssetManager assetManager, PhysicsSpace space, Camera cam, int capacity)
    {
        this(assetManager, space, cam, capacity, 0);
        
    }
    
    public ProjectilesManager(AssetManager assetManager, PhysicsSpace space, Camera cam, int capacity, int type)
    {
        super();
        warmup = 0;
        currentTime = 0;
        projectiles = new Projectile[capacity];
        canShoot = true;
      
        for(int i=0; i<projectiles.length; i++){
            projectiles[i] = new Projectile(assetManager, space, cam, type);
            attachChild(projectiles[i]);
        }
        
    }
    
    public void shoot(Vector3f position, Vector3f direction)
    {
        this.shoot(position, direction, 20.0f, 20.0f);
    }
    
    public void shoot(Vector3f position, Vector3f direction, float speed, float distanceLife)
    {
        if( canShoot )
        {
            for(int i=0; i<projectiles.length; i++)
            {
                if(!projectiles[i].isAlive)
                {
                    projectiles[i].start(position, direction, speed, distanceLife);
                    canShoot = false;
                    currentTime = 0;
                    break;
                }
            }
        }else{
            didShoot = true;
        }
        
        this.direction = direction;
        this.position = position;
    }
    
    public void update(float tpf)
    {
        for(int i=0; i<projectiles.length; i++){
            projectiles[i].update(tpf);
        }
        
        if(!canShoot){
            if(currentTime > warmup){
                canShoot = true;
            }else{
                currentTime += tpf;
            }
        }
        
        if(didShoot && canShoot)
        {
            shoot(position,direction);
            didShoot = false;
        }
        
    }
    
    
    public void end()
    {
        for(int i=0; i<projectiles.length; i++){
            projectiles[i].end();
        }
    }
}
