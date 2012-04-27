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

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.HashMap;

/**
 *
 * @author edba
 */
public class Sprite extends Geometry
{
    HashMap<String, Material> materials_;
    public float width;
    public float height;
    
    public Sprite ()
    {
        this(1.0f, 1.0f);
    }
    
    public Sprite (float witdh, float height)
    {   
        super("sprite");
        this.width = witdh;
        this.height = height;
        this.mesh = new Box(Vector3f.ZERO, witdh, height, 0.0f);
        materials_ = new HashMap<String, Material>();
    }
    
    public Sprite (Mesh mesh)
    {   
        super("sprite");
        this.mesh = mesh;
        materials_ = new HashMap<String, Material>();
    }
    
    public void addMaterial(String stringID, Material material)
    {
        MatParam param = material.getParam("AniTexMap");
        Texture texture = (Texture)param.getValue();
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        materials_.put(stringID, material);
        if(this.getMaterial() == null)
        {
            this.setMaterial(material);
        }
    }
    
    public void showMaterial(String stringID)
    {
        Material mat = materials_.get(stringID);
        if (mat != null)
        {
           this.setMaterial(mat);
        }
    }
}
