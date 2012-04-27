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
package mygame.actors;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mygame.controllers.FaceToCamController;
import mygame.utils.Sprite;
import com.jme3.scene.Node;

/**
 *
 * @author diuxddr
 */
public class Projectile extends Node
{
    Sprite sprite_;
    boolean isAlive;
    ProjectileLinearController controller;
    FaceToCamController faceController;
    PhysicsSpace space;
    Camera cam;
    float offsetSize;
    
    private int type = 0; //TODO: change to an enmun or something
    
    protected boolean shouldEnd;
    
    public Projectile(AssetManager assetManager, PhysicsSpace space, Camera cam)
    {
        this(assetManager, space, cam, 0);
    }
    
    public Projectile(AssetManager assetManager, PhysicsSpace space, Camera cam, int type){
        
        super();
        
        sprite_ = new Sprite(0.5f, 0.5f);
        this.attachChild(sprite_);
        
        Material mat;
        
        if(type == 0)
        {
            mat = assetManager.loadMaterial("Materials/Bullet/bullet1.j3m");
        }
        else
        {
            mat = assetManager.loadMaterial("Materials/Bullet/bullet2.j3m");
        }
        
        sprite_.addMaterial("projectile", mat);
        sprite_.showMaterial("projectile");
        isAlive = false;
        shouldEnd = false;
        this.space = space;
        this.cam = cam;
        offsetSize = 1;
        end();
        
        this.type = type;
    }
    
    public int gettType()
    {
        return this.type;
    }
    
    public boolean isAlive()
    {
        return isAlive;
    }
    
    public void setOffsetSize(float offsetSize)
    {
        this.offsetSize = offsetSize;
    }
    
    public void start(Vector3f position, Vector3f direction)
    {
        this.start(position, direction, 20.0f, 20.0f);
    }
    
    public void start(Vector3f position, Vector3f direction, float speed, float distanceLife)
    {    
        //Vector3f size = ((BoundingBox)getWorldBound()).getExtent(null);
        Vector3f size = new Vector3f(0.5f,0.5f,0.5f);
        Vector3f ndir = direction.normalize();
        CollisionShape shape = new BoxCollisionShape(size);
        
        Vector3f projectilePos = new Vector3f(position.x, position.y, position.z);
        setLocalTranslation(projectilePos);
        move((ndir.x * size.x) + (ndir.x * offsetSize), 
             (ndir.y * size.y) + (ndir.y * offsetSize), 
             (ndir.z * size.z) + (ndir.z * offsetSize));
  
        controller = new ProjectileLinearController(shape, ndir, speed, distanceLife);
        controller.setKinematic(true);
        controller.setKinematicSpatial(true);
        
        this.addControl(controller);
        isAlive = true;
        shouldEnd = false;
        sprite_.setCullHint(CullHint.Inherit);
        space.add(this);
        
        faceController = new FaceToCamController(cam);
        this.addControl(faceController);
        
    }
    
    public void update(float tpf)
    {
        if(shouldEnd)
        {
            end();
        }
    }
    
    public void end()
    {
        if(isAlive){
            space.remove(this);
            removeControl(controller);
            removeControl(faceController);
        }
        
        isAlive = false;
        sprite_.setCullHint(CullHint.Always);
    }
    
}


class ProjectileLinearController extends RigidBodyControl
{
    Vector3f direction;
    Camera cam;
    float speed;
    float distanceLife;
    float currentDistance;
    
    public ProjectileLinearController(CollisionShape shape, Vector3f direction, float speed,  float distanceLife) {
        super(shape);
        this.direction = direction;
        this.speed = speed;
        this.distanceLife = distanceLife;
        currentDistance = 0;
    }
    
    public void setSpeed(float speed){
        this.speed = speed;
    }
    
    public void update(float tpf) {
        super.update(tpf);  
        spatial.move(direction.x * speed * tpf, direction.y * speed * tpf, direction.z * speed * tpf);
        distanceLife -= tpf*speed;
        if(distanceLife < 0)
            ((Projectile)spatial).shouldEnd = true;
    }
    
}