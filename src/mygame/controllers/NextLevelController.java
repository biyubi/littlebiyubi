/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

import mygame.states.StateInGame;

/**
 *
 * @author edba
 */
public class NextLevelController extends EventController 
{
    
    public static final int VALUE_NAME  = 0;
    public static String levelName;
    public String theLevel;
    
    public NextLevelController(){
         super();  
    }
    
    public void reloadValues()
  {

      Object value = getValue(VALUE_NAME);
      if(value != null && (value instanceof String)){
          levelName = "" + (String)value;
          theLevel = levelName;
      }
  }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        if(isActive_ && (repeatTotal_ == -1 || repeatCount_ < repeatTotal_) )
        {
            repeatCount_++;
            
            reloadValues();
            
            if(theLevel != null){
                StateInGame.loadNextLevel(theLevel);
            }
            
            //Test thingy
           /* space_.remove(spatial);
            spatial.removeControl(RigidBodyControl.class);

            Vector3f vectorWall = ((BoundingBox) ((Node)spatial).getChild(0).getWorldBound()).getExtent(null);
            RigidBodyControl rigidControl = new RigidBodyControl(new BoxCollisionShape(new Vector3f(vectorWall.x*0.8f, vectorWall.y*0.8f, vectorWall.z*0.8f)));
            ((Node)spatial).getChild(0).addControl(rigidControl);
            space_.add(((Node)spatial).getChild(0));*/
        }
        else
        {
            isActive_ = false;
        }
    }
}
