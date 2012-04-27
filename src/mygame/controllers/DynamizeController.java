/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controllers;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.PhysicsControl;
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
