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
 * @author diuxddr
 */
public class TemplateController extends AbstractControl implements Savable, Cloneable {
  
  private int index; // can have custom fields -- example 

  public TemplateController(){} // empty serialization constructor
 
  /** Optional custom constructor with arguments that can init custom fields.
    * Note: you cannot modify the spatial here yet! */
  public TemplateController(int i){ 
    // index=i; // example 
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
  public Control cloneForSpatial(Spatial spatial){
    final TemplateController control = new TemplateController();
    /* Optional: use setters to copy userdata into the cloned control */
    // control.setIndex(i); // example
    control.setSpatial(spatial);
    return control;
  }
 
  @Override
  protected void controlRender(RenderManager rm, ViewPort vp){
     /* Optional: rendering manipulation (for advanced users) */
  }
 
  @Override
  public void read(JmeImporter im) throws IOException {
      super.read(im);
      // im.getCapsule(this).read(...);
  }
 
  @Override
  public void write(JmeExporter ex) throws IOException {
      super.write(ex);
      // ex.getCapsule(this).write(...);
  }
  
}