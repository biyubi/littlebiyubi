/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author edba
 */
public class EventController extends ValueController
{
   
    protected PhysicsSpace space_;
    
    protected int repeatTotal_ = 1;
    protected int repeatCount_ = 0;
    protected boolean isActive_ = false;

    public EventController(){} // empty serialization constructor

    /** This is your init method. Optionally, you can modify 
    * the spatial from here (transform it, initialize userdata, etc). */
    @Override
    public void setSpatial(Spatial spatial) {
    super.setSpatial(spatial);
    // spatial.setUserData("index", i); // example
    }

    public void setTotalRepeats(int repeat)
    {
      repeatTotal_ = repeat;
    }

    public void setSpace(PhysicsSpace space)
    {
      space_ = space;
    }
    
    public void activate()
    {
        isActive_ = true;
    }

    /** Implement your spatial's behaviour here.
    * From here you can modify the scene graph and the spatial
    * (transform them, get and set userdata, etc).
    * This loop controls the spatial while the Control is enabled. */
    @Override
    protected void controlUpdate(float tpf){
        if(spatial != null) {
          // spatial.rotate(tpf,tpf,tpf); // example behaviour
        }
    }


    @Override
    protected void controlRender(RenderManager rm, ViewPort vp){
     /* Optional: rendering manipulation (for advanced users) */
    }


}
