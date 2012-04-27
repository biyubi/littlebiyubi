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

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author edba
 */
public class DynamizeController extends EventController 
{
    @Override
    protected void controlUpdate(float tpf)
    {
        if(isActive_ && (repeatTotal_ == -1 || repeatCount_ < repeatTotal_) )
        {
            repeatCount_++;
            
            //Test thingy
            space_.remove(spatial);
            spatial.removeControl(RigidBodyControl.class);

            Vector3f vectorWall = ((BoundingBox) ((Node)spatial).getChild(0).getWorldBound()).getExtent(null);
            RigidBodyControl rigidControl = new RigidBodyControl(new BoxCollisionShape(new Vector3f(vectorWall.x*0.8f, vectorWall.y*0.8f, vectorWall.z*0.8f)));
            ((Node)spatial).getChild(0).addControl(rigidControl);
            space_.add(((Node)spatial).getChild(0));
        }
        else
        {
            isActive_ = false;
        }
    }
}
