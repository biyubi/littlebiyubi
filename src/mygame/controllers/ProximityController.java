/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author cuacCuacPC
 */
public class ProximityController extends ValueController{
   
  public ProximityController(){
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
  }

 @Override
  public Control cloneForSpatial(Spatial spatial){
    final ProximityController control = new ProximityController();
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