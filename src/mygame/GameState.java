package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

public class GameState extends AbstractAppState {
 
    protected MainSimpleApplication     app;
    protected Node                      rootNode;
    protected AssetManager              assetManager;
    protected InputManager              inputManager;
    protected ViewPort                  viewPort; 
    protected ViewPort                  guiViewPort; 
    protected Camera                    cam; 
    
    public Node getRootNode(){ return rootNode; }
        
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        super.initialize(stateManager, app); 
        
        rootNode = new Node(this.getClass().getName());
        
        this.app = (MainSimpleApplication)app;
        this.app.getRootNode().attachChild(rootNode); 
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.viewPort = app.getViewPort();
        this.guiViewPort = app.getGuiViewPort();
        this.cam = app.getCamera();
        
        this.initState();
   
   }
   
   @Override
   //Este método se manda llamar cuando se hace detach del AppState
    public void cleanup() {
        super.cleanup();
        this.app.getRootNode().detachChild(getRootNode()); // Quitar al root node
        this.endState();
   }
 
    @Override
    public void setEnabled(boolean enabled) {
        
        // Pause and unpause
        super.setEnabled(enabled);
        
        /*if(enabled){
            //Activar cosas
        } else {
            //Desactivar cosas
        }*/
    }

    @Override
    public void update(float tpf) {
        
        super.update(tpf);
        //rootNode.updateLogicalState(tpf);
        //rootNode.updateGeometricState();
        
        //Override
        //El estado puede estar activo o inactivo.
        /*if(isEnabled()){
                            
        } else {
          
        }*/
        
    }
    
   protected void initState() {
        //Override
   }
   
   protected void endState() {
       //Override
   }
 
}