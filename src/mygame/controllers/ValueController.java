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

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.ArrayList;

/**
 *
 * @author diuxddr
 */
public class ValueController extends AbstractControl{
  
  private ArrayList<Object> values;
 
  public ValueController(){} // empty serialization constructor

  public void addValue(Object value)
  {
  
      if(values == null)
      {
          values = new ArrayList<Object>();
      }
      
      values.add(value);
      reloadValues();
  }
  
    public Object getValue(int id){

        if(values != null && values.size() > id){
            return values.get(id);
        }

        return null;
    }
    
    public void reloadValues()
    {
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
        if(spatial != null) {
          // spatial.rotate(tpf,tpf,tpf); // example behaviour
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Control cloneForSpatial(Spatial spatial) {
        final ValueController control = new ValueController();
        /* Optional: use setters to copy userdata into the cloned control */
        // control.setIndex(i); // examplevalue
        
        for(int i=0 ;i<values.size(); i++){
            control.addValue(values.get(i));
        }

        control.setSpatial(spatial);
        return control;
    }
  
}