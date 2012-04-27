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
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author cuacCuacPC
 */
public class FollowController extends AbstractControl{
  
  float speed;
  Vector3f direction;
    
  public FollowController(Vector3f direction){
      super();
      speed = 1.0f;
      this.direction = direction.normalize();
  } 

 
  /** This is your init method. Optionally, you can modify 
    * the spatial from here (transform it, initialize userdata, etc). */
  @Override
  public void setSpatial(Spatial spatial) {
    super.setSpatial(spatial);
    // spatial.setUserData("index", i); // example
  }
 
 
  /** Implement your spatial's behaviour here.
    * From here you can modify the scene graph and the spatial
    * (transform them, get and set userdata, etc).
    * This loop controls the spatial while the Control is enabled. */
  @Override
  protected void controlUpdate(float tpf){
      
    float valueY = 0;
    float valueX = 0;
    
    if(spatial != null) {
        spatial.move(direction.x * speed * tpf, direction.y * speed * tpf, direction.z * speed * tpf);
    }
  }

 @Override
  public Control cloneForSpatial(Spatial spatial){
    final FollowController control = new FollowController(direction);
    /* Optional: use setters to copy userdata into the cloned control */
    // control.setIndex(i); // example
    control.setSpatial(spatial);
    return control;
  }
 
  @Override
  protected void controlRender(RenderManager rm, ViewPort vp){
     /* Optional: rendering manipulation (for advanced users) */
  }

  
}