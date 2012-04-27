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

import com.jme3.bullet.PhysicsSpace;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

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
