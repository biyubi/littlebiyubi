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
public class SinController extends ValueController{
  
  public static final int VALUE_MOVE_X      = 0; //value - int
  public static final int VALUE_MOVE_Y      = 1; //value2 - int
  public static final int VALUE_DISTANCE    = 2; //value3 - float
  public static final int VALUE_SPEED       = 3; //value4 - float
  public static final int VALUE_DELAY       = 4; //value5

    
  protected float speed;
  protected float distance;
  protected float delay;
  protected boolean sinX;
  protected boolean sinY;
  
  //Helpers
  protected float currentTime;
  protected float oldValueX;
  protected float oldValueY;
  
    
  public SinController(){
      super();
      speed = 1.0f;
      delay = 0;
      oldValueY = 0;
      oldValueX = 0;
      distance = 1.0f;
      sinX = false;
      sinY = true;
  } 
  
  @Override
  public void reloadValues()
  {
      Object value = getValue(VALUE_MOVE_X);
      if(value != null && (value instanceof Integer)){
          sinX = ((Integer)value) > 0;
      }
      value = getValue(VALUE_MOVE_Y);
      if(value != null && (value instanceof Integer)){
          sinY = ((Integer)value) > 0;
      }
        
      value = getValue(VALUE_DISTANCE);
      if(value != null && (value instanceof Float)){
          distance = ((Float)value);
      }
      
      value = getValue(VALUE_SPEED);
      if(value != null && (value instanceof Float)){
          speed = ((Float)value);
      }
      
      value = getValue(VALUE_DELAY);
      if(value != null && (value instanceof Float)){
          delay = ((Float)value);
      }
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
        
        if(delay > 0){
            delay -= tpf;
            return;
        }
        
        if(sinX){
            valueX = FastMath.cos(currentTime*speed*FastMath.PI)*distance;
     
        }
        if(sinY){
            //System.out.println("Control update");
            valueY = FastMath.sin(currentTime*speed*FastMath.PI)*distance;
            //valueY = (valueY > FastMath.PI*2)? 0 : valueY;   
        }
       
        spatial.move( valueX - oldValueX, valueY -oldValueY, 0);
        //spatial.rotate()
        currentTime += tpf;
        currentTime = (currentTime*speed*FastMath.PI > FastMath.PI*2)? 0 : currentTime;
        oldValueX = valueX;
        oldValueY = valueY;
    }
  }

 @Override
  public Control cloneForSpatial(Spatial spatial){
    final SinController control = new SinController();
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