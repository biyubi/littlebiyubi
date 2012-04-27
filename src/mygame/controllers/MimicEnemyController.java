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

/**
 *
 * @author diuxddr
 */

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.renderer.Camera;
import mygame.actors.Hero;


/**
 *
 * @author normenhansen
 */
public class MimicEnemyController extends EnemyPhysicsController{
    
    public boolean[] myControls = new boolean[TOTAL_CONTROLS];

    public MimicEnemyController() {
        super();
        init();
    }

    public MimicEnemyController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        init();
    }
    
    public void init()
    {
        for(int i=0; i<myControls.length; i++){
            myControls[i] = false;
        }
    }
    
    @Override
    public void updateMovementWithCameraAndControls(Hero hero, Camera cam, boolean[] controls, float tpf){ 
        
        myControls[CONTROL_UP] = controls[CONTROL_DOWN];
        myControls[CONTROL_DOWN] = controls[CONTROL_UP];
        myControls[CONTROL_LEFT] = controls[CONTROL_LEFT];
        myControls[CONTROL_RIGHT] = controls[CONTROL_RIGHT];
        myControls[CONTROL_JUMP] = controls[CONTROL_JUMP];
        
        super.updateMovementWithCameraAndControls(hero, cam, myControls, tpf);
    }
     
}