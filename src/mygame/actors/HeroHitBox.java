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
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;

/**
 *
 * @author diuxddr
 */
public class HeroHitBox extends Actor
{
    HeriHitBoxController controller;
    Hero hero;

    protected boolean shouldEnd;
    
    public HeroHitBox(AssetManager assetManager, Hero hero){
        super(assetManager);
        sprite_.setCullHint(CullHint.Always); //Never show
        name_ = "Hero";
        initMaterials();
        
        Vector3f size = new Vector3f(0.70f, 1.0f,0.70f);
        CollisionShape shape = new BoxCollisionShape(size);
        controller = new HeriHitBoxController(shape, hero);
        this.addControl(controller);
    }
  
}


class HeriHitBoxController extends GhostControl
{
    Hero hero;
    
    public HeriHitBoxController(CollisionShape shape, Hero hero) {
        super(shape);
        //setKinematic(true);
        //setKinematicSpatial(true);
        this.hero = hero;
    }
    
 
    @Override
    public void update(float tpf) {
        super.update(tpf);  
        spatial.setLocalTranslation(hero.getLocalTranslation());
    }
    
}