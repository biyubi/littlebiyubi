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
