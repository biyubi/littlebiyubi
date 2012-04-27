/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author cuacCuacPC
 */
public class FaceToCamController extends AbstractControl{
  
  protected Camera cam;
  protected Vector3f faceDirection;
    
  public FaceToCamController(Camera cam){
      this(cam, new Vector3f());
  } 
  
  public FaceToCamController(Camera cam, Vector3f faceDirection)
  {
      this.faceDirection = faceDirection;
      this.cam = cam;
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

        spatial.lookAt(new Vector3f(cam.getLocation().x, 
                                    //spatial.getLocalTranslation().y, 
                                    cam.getLocation().y, 
                                    cam.getLocation().z),
                       Vector3f.UNIT_Y);

  }

 @Override
  public Control cloneForSpatial(Spatial spatial){
    final FaceToCamController control = new FaceToCamController(cam);
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