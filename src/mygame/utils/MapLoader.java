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
package mygame.utils;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import mygame.controllers.SinController;

/**
 *
 * @author edba
 */
public class MapLoader 
{
    
    static final float tileSize = 1.0f;
    
    static private int [][][] levels =
    {
        //level1
        {
            {2, 1, 1, 1}, 
            {1, 1, 1, 2},
            {1, 1, 1, 1},
            {1, 0, 0, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 1},
            {1, 2, 2, 1},
        },
    };
    
    private static BoxCollisionShape createSingleBoxShape(Spatial spatial, Spatial parent) {
        spatial.setModelBound(new BoundingBox());
        //TODO: using world bound here instead of "local world" bound...
         BoxCollisionShape shape = new BoxCollisionShape(
            ((BoundingBox) spatial.getWorldBound()).getExtent(null));
        return shape;
    }
    
    static Node loadMap(int level, AssetManager assetManager, PhysicsSpace space)
    {
        Node node = new Node();
        
        int [][] levelData = levels[level];
       
        Material mat_floor = new Material( 
                        assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_floor.setTexture("ColorMap", assetManager.loadTexture("Textures/floor3.png"));
                    
        Material mat_wall = new Material( 
                        assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_wall.setTexture("ColorMap", assetManager.loadTexture("Textures/floor4.png"));
                    
        for ( int i = 0; i < levelData.length; i++ )
        {
            for ( int j = 0; j < levelData[i].length; j++ )
            {
                if (levelData[i][j] != 0)
                {
                    Spatial floor = assetManager.loadModel("Models/tile_cube.j3o");
                    floor.setLocalTranslation(new Vector3f(i*tileSize*2, 0, j*tileSize*2));
                   
                    //floor.setModelBound(new BoundingBox());
                    //Vector3f vectorFloor = ((BoundingBox) floor.getWorldBound()).getExtent(null);
                    //System.out.println("Floor " + vectorFloor.toString());
                    
                    RigidBodyControl control2 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(tileSize, tileSize, tileSize)));
                    control2.setKinematic(true);
                    control2.setKinematicSpatial(true);
                    floor.addControl(control2);
                    //floor.addControl(new SinController());
                    //ninja.scale(0.05f, 0.05f, 0.05f);
                    //ninja.rotate(0.0f, -3.0f, 0.0f);
                    //ninja.setLocalTranslation(0.0f, -5.0f, -2.0f);
                    node.attachChild(floor);
                    space.add(floor);
        
                    /*.
                    Box box = new Box(new Vector3f(i*tileSize*2, 0, j*tileSize*2), tileSize, tileSize, tileSize);
                    Spatial wall = new Geometry("Box", box );
                    wall.setMaterial(mat_floor);
                    //wall.setLocalTranslation(2.0f,-2.5f,0.0f);
                    node.attachChild(wall);
             */
                     
                    /*
                    Spatial teapot = assetManager.loadModel("Models/cubon.obj");
                    teapot.setLocalTranslation(new Vector3f(i*tileSize*2, 0, j*tileSize*2));
                    Material mat_default = new Material( 
                        assetManager, "Models/cubon.mtl");
                    teapot.setMaterial(mat_default);
                    node.attachChild(teapot);
                    */
                    if (levelData[i][j] == 2)
                    {
                        Box boxZ = new Box(/*new Vector3f(i*tileSize*2, tileSize*2, j*tileSize*2),*/ tileSize, tileSize, tileSize);
                        Geometry wallZ = new Geometry("Box", boxZ );
                        wallZ.setLocalTranslation(i*tileSize*2, tileSize*2, j*tileSize*2);
                        wallZ.setMaterial(mat_wall);
                        

                        //wallZ.setModelBound(new BoundingBox());
                        Vector3f vectorWall = ((BoundingBox) wallZ.getWorldBound()).getExtent(null);
                        
                        //System.out.println("wallZ " + vectorWall.toString());
                       
                        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(vectorWall));
 
                        //RigidBodyControl control = new RigidBodyControl(shape);
                        control.setKinematic(true);
                        control.setKinematicSpatial(true);
                        
                        
                        //control.setFriction(1.0f);
                        //control.setRestitution(0f);
                        //control.setKinematic(true);
                        //control.setKinematicSpatial(true);
                        //control.setKinematicSpatial(true);
                        wallZ.addControl(control);
                        wallZ.addControl(new SinController());
                        node.attachChild(wallZ);
                        space.add(wallZ);
                    }
                }
            }
        }

        return node;
    }
}
