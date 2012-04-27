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
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.app.state.AbstractAppState;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mygame.states.StateInGame;

/**
 * test
 * @author normenhansen
 */
public class MainSimpleApplication extends SimpleApplication {
    
        
    public AbstractAppState currentGameState;
    
    public static MainSimpleApplication app = null;
    
    public static void main(String[] args) {
        app = new MainSimpleApplication();
        app.start();
    }
    
    @Override
    public void start(){
        
        AppSettings settings = new AppSettings(true);
        settings.setResolution(Defines.WINDOW_WIDTH, Defines.WINDOW_HEIGHT);
        settings.setTitle(Defines.APP_NAME);
        
        try {
          settings.setIcons(new BufferedImage[]{
                  ImageIO.read(getClass().getResourceAsStream("/icons/logo_256.png")),
                  ImageIO.read(getClass().getResourceAsStream("/icons/logo_128.png")),
                  ImageIO.read(getClass().getResourceAsStream("/icons/logo_32.png")),
                  ImageIO.read(getClass().getResourceAsStream("/icons/logo_16.png"))
          });
        }
        catch (IOException e) {
          //log.log(java.util.logging.Level.WARNING, "Unable to load program icons", e);
        }
        
        setSettings(settings);
        this.setShowSettings(Defines.ASK_FOR_STATS);
        if(!Defines.DEBUG_JME_MESSAGES)
            Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
        super.start();
    }
        
    @Override
    public void simpleInitApp() {
        setDisplayStatView(Defines.DEBUG_STATS);
        setDisplayFps(Defines.DEBUG_FPS);
        replaceGameState(StateInGame.class);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
    }
    

    public void replaceGameState(Class stateClass){ 
        
        GameState state = null;
        
        try{
            state = (GameState) stateClass.newInstance();
        }catch(Exception e){
            return;
        }
        
        replaceGameState(state);
    }
    
    public void replaceGameState(GameState state){ 
        
        if(currentGameState == null){
            stateManager.attach(state);
            currentGameState = state;
            return;
        }
        
        if(currentGameState != state){
            stateManager.detach(currentGameState);
            stateManager.attach(state);
            currentGameState = state;
        }
    }
}
