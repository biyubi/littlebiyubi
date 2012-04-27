/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.utils;


import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import mygame.actors.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;
import mygame.actors.Enemy;
import mygame.actors.Item;
import mygame.controllers.EnemyPhysicsController;
import mygame.controllers.EventController;
import mygame.controllers.ValueController;
/**
 *
 * @author edba
 */
public class Level
{
    private Node mapSpatial_;
    private Vector3f heroVector_;
    private PhysicsSpace space_;
    private ArrayList<Enemy> enemies_;
    private ArrayList<Item> items_;
    
    public Level(AssetManager assetManager, PhysicsSpace space, String level)
    {
        enemies_ = new ArrayList<Enemy>();
        items_ = new ArrayList<Item>();
        
        space_ = space;
        mapSpatial_ = (Node)assetManager.loadModel("Scenes/"+level+"/"+level+".j3o");
        
        Spatial hero = mapSpatial_.getChild("0_Hero");
        hero.setCullHint(Spatial.CullHint.Always);
        heroVector_ = hero.getLocalTranslation();
        hero.removeFromParent();
        
        List<Spatial> elements = mapSpatial_.getChildren();
        
        ArrayList<Node> eventElements = new ArrayList<Node>();
        ArrayList<Node> enemyElements = new ArrayList<Node>();
        ArrayList<Node> itemElements = new ArrayList<Node>();
        ArrayList<Node> optimizeElements = new ArrayList<Node>();
        
        for(int i = 0; i < elements.size(); i++)
        {
            Node element = (Node)elements.get(i);
            
            Node entity = (Node)element.getChild(0);
            Node ogremesh = (Node)entity.getChild(0);
            Geometry geom = (Geometry)ogremesh.getChild(0);
            
            Material material = geom.getMaterial();
            
            
            MatParam param = material.getParam("DiffuseMap");
            if(param!= null && param.getValue() != null){
                Texture texture = (Texture)param.getValue();
                texture.setMagFilter(Texture.MagFilter.Nearest);
                texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            }else{
                System.out.println(material.getParams().toString());
            }
            String enemyName = element.getUserData("enemy");
            String eventType = element.getUserData("event");
            String itemType = element.getUserData("item");
            
            if(enemyName != null)  
            {
                Enemy enemy = loadEnemy(assetManager, space, element, enemyName);
                
                Integer defense = element.getUserData("defense");
                Integer recover = element.getUserData("recover");
                Float   proximity = element.getUserData("proximity");
                String  backupController = element.getUserData("backup_controller");
                
                if(defense != null){
                    enemy.setDefense(defense);
                }
                
                if(recover != null){
                    enemy.setRecover(recover);
                }
                
                if(proximity != null){
                    enemy.setProximity(proximity);
                }
                
                if(backupController != null){
                    enemy.setBackupController(backupController);
                }
                
                enemy.prepareSharedHelpers();

                updateElementWithUserData(space, enemy, element); 
                element.setUserData("enemy_instance", enemy);
                enemyElements.add(element);
                
                if(eventType != null)
                {
                    eventElements.add(element);
                }
                
            }else if(eventType != null)
            {
                eventElements.add(element);
            }
            else if (itemType != null)
            {
                //Todo: set scale of item
                //Vector3f vScale = element.getLocalScale();
                Item item = new Item(assetManager, itemType);
                item.setLocalTranslation(element.getLocalTranslation());
                updateElementWithUserData(space, item, element);
                element.setUserData("item_instance", item);
                items_.add(item);
                itemElements.add(element);
            }
            else
            {
                updateElementWithUserData(space, element);
                
                Integer isRigid = element.getUserData("rigid");
                
                if(isRigid != null && isRigid == 1)
                {
                    optimizeElements.add(element);
                }
            }
        }
        
        for(int i = 0; i < eventElements.size(); i++)
        {
            
            Node element = eventElements.get(i);
            element.removeFromParent();
            
            Enemy enemy = element.getUserData("enemy_instance");
            Item item = element.getUserData("item_instance");
            
            EventHandler eventHandler = new EventHandler();
            
            if(enemy != null)
            {
                enemy.setEventHandler(eventHandler);
            }
            else if (item != null)
            {
                item.setEventHandler(eventHandler);
            }
            else
            {
                eventHandler.setName(element.getName());
                element.setName(element.getName() + "_child");
                eventHandler.attachChild(element);
            
                mapSpatial_.attachChild(eventHandler);
            
                updateElementWithUserData(space, eventHandler, element);
            }

            //Events

            boolean checkForMoreEventsTypes = true;
            int eventTypeCount = 1;
            
            //Values
            boolean checkForMoreControllerValues = true;
            int valuesCount = 1;

            while(checkForMoreEventsTypes)
            {
                String eventType;
                String eventString;
                
                if(eventTypeCount == 1)
                {
                    eventString = "event";
                }
                else
                {
                    eventString = "event" + eventTypeCount;
                }
                
                eventType = element.getUserData(eventString);
                
                if(eventType != null)
                {
                    String eventTarget = element.getUserData(eventString + "_target");
                    String eventController = element.getUserData(eventString + "_controller");
                                        
                    if(eventTarget != null && eventController != null)
                    {
                        EventController controller = null;
                        
                        try 
                        {
                            Class controllerClass = Class.forName("mygame.controllers."+eventController);
                            controller = (EventController) controllerClass.newInstance();
                            controller.setSpace(space);
                        } 
                        catch (InstantiationException ex) 
                        {
                            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } 
                        catch (IllegalAccessException ex) 
                        {
                            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        }
                        catch (ClassNotFoundException ex)
                        {
                            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        }
                        
                        if(controller != null)
                        {
                       
                            //Check for values
                            while (checkForMoreControllerValues){

                                Object value;

                                if(valuesCount == 1)
                                {
                                    value = element.getUserData(eventString + "_controller_value");
                                }
                                else
                                {
                                    value = element.getUserData(eventString + "_controller_value" + valuesCount);
                                }

                                if(value != null)
                                {
                                    controller.addValue(value);
                                    valuesCount++;
                                }else{
                                    checkForMoreControllerValues = false;
                                }
                            }
                            
                            Spatial eventTargetSpatial;
                            
                            
                            
                            if(eventTarget.equalsIgnoreCase("self")){
                                if(enemy != null){
                                    eventTargetSpatial = enemy;
                                }else{
                                    eventTargetSpatial = eventHandler;
                                }
                            }else{
                                eventTargetSpatial = mapSpatial_.getChild(eventTarget);
                            }
                            
                            if(eventTargetSpatial != null)
                            {
                                eventTargetSpatial.addControl(controller);

                                Float eventValue = element.getUserData(eventString+"_value");

                                if (eventValue == null)
                                {
                                    eventValue = 0.0f; //Default value 0.0
                                }
                                
                                eventHandler.addControllerForEventType(eventType, controller, eventValue);
                                
                                Integer eventRepeat = element.getUserData(eventString+"_controller_repeat");

                                if (eventRepeat != null)
                                {
                                    controller.setTotalRepeats(eventRepeat);
                                }
                            }
                               
                        } 

                    }
                    eventTypeCount++;
                }
                else
                {
                    checkForMoreEventsTypes = false;
                }
            }
        }
        
        //Enemies
        for(int i = 0; i < enemies_.size(); i++)
        {
            mapSpatial_.attachChild(enemies_.get(i));
        }
        
        for(int i = 0; i < enemyElements.size(); i++)
        {
            enemyElements.get(i).removeFromParent();
        }
        
        //Items
        for(int i = 0; i < items_.size(); i++)
        {
            mapSpatial_.attachChild(items_.get(i));
        }
        
        for(int i = 0; i < itemElements.size(); i++)
        {
            itemElements.get(i).removeFromParent();
        }
        
        //Optimize elements
        Node batchNode = new Node();
        for(int i = 0; i < optimizeElements.size(); i++)
        {
            optimizeElements.get(i).removeFromParent();
            Integer isVisible = optimizeElements.get(i).getUserData("visible");
            if(isVisible == null || isVisible == 1)
            {
                batchNode.attachChild(optimizeElements.get(i));
            }
        }
        
        mapSpatial_.attachChild(batchNode);
        
        GeometryBatchFactory.optimize(batchNode);

    }
    
    Enemy loadEnemy(AssetManager assetManager, PhysicsSpace space, Node element, String name)
    {
        Vector3f vScale = element.getLocalScale();
        Enemy enemy = new Enemy(assetManager, name, vScale.x, vScale.y, space);
        enemies_.add(enemy);
        enemy.setLocalTranslation(element.getLocalTranslation());
        element.setCullHint(Spatial.CullHint.Always);
        
        String spikes = element.getUserData("enemy");
        
            //diux hack
        Vector3f vectorWall = ((BoundingBox) element.getWorldBound()).getExtent(null);
        vectorWall.multLocal(0.5f);
        RigidBodyControl rigidControl = new RigidBodyControl(new BoxCollisionShape(vectorWall),0);
        //rigidControl.setKinematic(true);
        //rigidControl.setKinematicSpatial(true);
        enemy.addControl(rigidControl);
        space.add(enemy);

        return enemy;
    }
    
    private void updateElementWithUserData(PhysicsSpace space, Node element)
    {
        updateElementWithUserData(space, element, element);
    }
    
    private void updateElementWithUserData(PhysicsSpace space, Node element, Node elementWithUserData )
    {
        Integer isRigid = elementWithUserData.getUserData("rigid");
        Integer isVisible = elementWithUserData.getUserData("visible");

        boolean checkForMoreControllers = true;
        int controllerCount = 1;
        
        boolean checkForMoreControllerValues = true;
        int valuesCount = 1;

        while (checkForMoreControllers)
        {
            checkForMoreControllerValues = true;
            
            String controllerName;
            
            if(controllerCount == 1)
            {
                controllerName = elementWithUserData.getUserData("controller");
            }
            else
            {
                controllerName = elementWithUserData.getUserData("controller" + controllerCount);
            }
            
            if (controllerName != null)
            {
                Control control = null;
                
                try {
                    Class controllerClass = Class.forName("mygame.controllers."+controllerName);
                    control = (Control) controllerClass.newInstance(); 
                    
                    if(control instanceof EnemyPhysicsController){
                        //Remove old Rigid control
                        space.remove(element);
                        element.removeControl(RigidBodyControl.class);
                        //diux hack
                    }
                    
                } catch (InstantiationException ex) {
                    Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                
                //Check for values
                while (checkForMoreControllerValues){
                   
                    Object value;
                    
                    if(valuesCount == 1)
                    {
                        if(controllerCount == 1)
                        {
                            value = elementWithUserData.getUserData("controller_value");
                        }
                        else
                        {
                            value = elementWithUserData.getUserData("controller" + controllerCount + "_value");
                        }
                    }
                    else
                    {
                        if(controllerCount == 1)
                        {
                            value = elementWithUserData.getUserData("controller_value" + valuesCount);
                        }
                        else
                        {
                            value = elementWithUserData.getUserData("controller" + controllerCount + "_value" + valuesCount);
                        }
                    }
                    
                    if(value != null && (control instanceof ValueController))
                    {
                        ValueController _control = (ValueController)control;
                        _control.addValue(value);
                        valuesCount++;
                    }else{
                        checkForMoreControllerValues = false;
                    }
                }
                
                if(control != null){
                    element.addControl(control);
                    if (control instanceof EnemyPhysicsController){
                        space.add(element);
                    }
                }
                
                controllerCount++;
            }
            else
            {
                checkForMoreControllers = false;
            }
        }
        
        
        
        if ( isRigid != null)
        {
            if (isRigid == 1) //Static
            {
                if (element == null)
                {
                    System.out.println("WHATT");
                }
                 element.addControl(new RigidBodyControl(0));
            }

            //Test thingy
            else if (isRigid == 2) //Dynamic
            {
                Vector3f vectorWall = ((BoundingBox) element.getWorldBound()).getExtent(null);
                RigidBodyControl rigidControl = new RigidBodyControl(new BoxCollisionShape(new Vector3f(vectorWall.x, vectorWall.z, vectorWall.y)));
                rigidControl.setPhysicsLocation(element.getLocalTranslation());
                rigidControl.setKinematic(true);
                rigidControl.setKinematicSpatial(true);
                element.addControl(rigidControl);
            }

             //Test thingy
            else if (isRigid == 3) //Physics
            {
                 Vector3f vectorWall = ((BoundingBox) element.getWorldBound()).getExtent(null);
                 RigidBodyControl rigidControl = new RigidBodyControl(new BoxCollisionShape(new Vector3f(vectorWall.x, vectorWall.y, vectorWall.z)));
                 element.addControl(rigidControl);
                 space.add(element);
                 rigidControl.setMass(1.0f);                   
                 rigidControl.setLinearVelocity(new Vector3f(0, 1, 0));
            }

            space.add(element);
        }
        
        

        if( isVisible != null )
        {
            if (isVisible == 0)
                element.setCullHint(Spatial.CullHint.Always);
        }
    }
    
    public Node getMap()
    {
        return mapSpatial_;
    }
    
    public Vector3f getHeroStartPosition()
    {
        return heroVector_;
    }
    
    public ArrayList<Enemy> getEnemies()
    {
        return enemies_;
    }
    
        public ArrayList<Item> getItems()
    {
        return items_;
    }
    
}
