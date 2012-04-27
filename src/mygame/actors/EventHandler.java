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

import java.util.HashMap;
import com.jme3.scene.Node;
import java.util.ArrayList;
import mygame.controllers.EventController;

/**
 *
 * @author edba
 */
public class EventHandler extends Node {
    
    public static final String EVENT_KEY_TOUCH          = "touch";
    public static final String EVENT_KEY_HIT            = "hit";
    public static final String EVENT_KEY_DESTROY        = "destroy";
    public static final String EVENT_KEY_DELAY          = "delay";
    public static final String EVENT_KEY_PROXIMITY      = "proximity";
    public static final String EVENT_KEY_DEFENSE        = "defense";
    
    HashMap<String, ArrayList<EventController>> eventType_controllers_;
    HashMap<EventController, Float> controller_value_;
    
    public EventHandler()
    {
        super();
        eventType_controllers_ = new HashMap<String, ArrayList<EventController>>();
        controller_value_ = new HashMap<EventController, Float>();
    }
    
    public void addControllerForEventType(String type, EventController controller)
    {        
       addControllerForEventType(type, controller, 0.0f);
    }
    
    public void addControllerForEventType(String type, EventController controller, float value)
    {        
        if (!eventType_controllers_.containsKey(type))
        {
            ArrayList<EventController> controllers = new ArrayList<EventController>();
            controllers.add(controller);
            eventType_controllers_.put(type, controllers);
        }
        else
        {
            ArrayList<EventController> controllers = eventType_controllers_.get(type);
            controllers.add(controller); 
        }
        
        controller_value_.put(controller, value);
    }
    
    public void triggerEventWithType(String type)
    {
        triggerEventWithType(type, 0.0f);
    }
    
    public void triggerEventWithType(String type, float value)
    {
        if (eventType_controllers_.containsKey(type))
        {
            ArrayList<EventController> controllers = eventType_controllers_.get(type);
            
            for(int i = 0; i < controllers.size(); i++)
            {
                EventController controller = controllers.get(i);
                if(controller_value_.containsKey(controller))
                {
                    if(controller_value_.get(controller) == value)
                    {
                        controller.activate();
                    }
                }
                else //asumes control value of 0.0f
                {
                    controller.activate();
                }
            }
        }
    }
}
