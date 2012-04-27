/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.ResourceBundle.Control;
import mygame.actors.Enemy;
import mygame.actors.Hero;
import mygame.actors.ProjectilesManager;

/**
 *
 * @author edba
 */
public class ShootController extends ValueController {
 
  public static final int VALUE_SHOOT_HERO  = 0; //value - int
  public static final int VALUE_SHOOT_DELAY  = 1; //value - float
  public static final int VALUE_SHOOT_SPEED  = 2; //value - float
  public static final int VALUE_SHOOT_LIFE  = 3; //value - float

  protected boolean shootHero;
  protected float shootDelay;
  protected float shootSpeed;
  protected float shootDistanceLife;
  
  //Helpers
  private float timeSinceLastProjectile = 0.0f;
    
  public ShootController()
  {
      super();
      shootHero = false;
      shootDelay = 0.5f;
      shootSpeed = 12.0f;
      shootDistanceLife = 40.0f;
  } 
  
  @Override
  public void reloadValues()
  {
      Object value = getValue(VALUE_SHOOT_HERO);
      if(value != null && (value instanceof Integer))
      {
          shootHero = ((Integer)value) > 0;
      }
      
      Object value2 = getValue(VALUE_SHOOT_DELAY);
      if(value2 != null && (value2 instanceof Float))
      {
          shootDelay = ((Float)value2);
      }
      
      Object value3 = getValue(VALUE_SHOOT_SPEED);
      if(value3 != null && (value3 instanceof Float))
      {
          shootSpeed = ((Float)value3);
      }
      
      Object value4 = getValue(VALUE_SHOOT_LIFE);
      if(value4 != null && (value4 instanceof Float))
      {
          shootDistanceLife = ((Float)value4);
      }
  }

  /** This is your init method. Optionally, you can modify 
    * the spatial from here (transform it, initialize userdata, etc). */
  @Override
  public void setSpatial(Spatial spatial) 
  {
    super.setSpatial(spatial);
    // spatial.setUserData("index", i); // example
  }
 
 
  /** Implement your spatial's behaviour here.
    * From here you can modify the scene graph and the spatial
    * (transform them, get and set userdata, etc).
    * This loop controls the spatial while the Control is enabled. */
  @Override
  protected void controlUpdate(float tpf)
  {  
        Enemy enemy = (Enemy)spatial;
        if(spatial != null)
        {
            //ystem.out.println("timeSinceLastProjectile = " + timeSinceLastProjectile);
            if(timeSinceLastProjectile < shootDelay)
            {
                timeSinceLastProjectile += tpf;
            }
            else
            {
               timeSinceLastProjectile = 0.0f;
               
               ProjectilesManager projectilesManagaer = enemy.getProjectilesManager();
               if (projectilesManagaer != null)
               {
                   Vector3f location = enemy.getLocalTranslation();
                   Vector3f direction = null;
                   
                   if(shootHero)
                   {
                       Vector3f heroLocation = enemy.getHero().getLocalTranslation();
                       direction = heroLocation.subtract(location);
                   }
                   else
                   {
                       EnemyPhysicsController physicsController = enemy.getEnemyPhysicsController();
                       direction = physicsController.getViewDirection();
                   }

                   if (location != null && direction != null)
                   {
                       projectilesManagaer.shoot(location.add(new Vector3f(0,0,2.0f)), direction, shootSpeed, shootDistanceLife);
                   }
               }
            }
        }
  }
}
