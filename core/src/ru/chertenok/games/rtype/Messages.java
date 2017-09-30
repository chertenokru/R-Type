package ru.chertenok.games.rtype;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import ru.chertenok.games.rtype.config.GameConfig;
import ru.chertenok.games.rtype.level.Level;
import ru.chertenok.games.rtype.level.LevelEvents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 13th on 06-Jul-17.
 */
public class Messages implements Level.ILevelEvent {
    private static Logger log = new Logger(Messages.class.getSimpleName(), Logger.DEBUG);

    @Override
    public void event(LevelEvents.LevelEvent event) {
        log.debug("Messages: event - " + event);
        if (event.Name.equals(GameConfig.MESSAGE_ADD)) {
            if (event.param.length > 5)
                addMessageAndWait(Global.levelBundle.get(event.param[5]), Float.valueOf(event.param[3]), Float.valueOf(event.param[4]),
                        Float.valueOf(event.param[0]), Color.valueOf(event.param[1]), Color.valueOf(event.param[2]));
            return;
        }

    }

    @Override
    public void registerLevelEvents(Map<String, Level.ILevelEvent> eventMap) {
        eventMap.put(GameConfig.MESSAGE_ADD, this);
    }

    class Message {
       String text;
       float x,y;
       float dtTime;
       float dtCounter;
       boolean isWait = false;
       boolean isActive = false;
       Color color;
       Color colorShadow = null;

           public Message(String text, float x, float y, float dtTime,Color color) {
           this(text,x,y,dtTime,color,null);

           }
           public Message(String text, float x, float y, float dtTime,Color color,Color colorShadow) {
               this.text = text;
               this.x = x;
               this.y = y;
               this.dtTime = dtTime;
               isActive = true;
               dtCounter = 0;
               this.color = color;
               this.colorShadow = colorShadow;
           }
       }


   List<Message> messages = new ArrayList<Message>();


      public void addMessage(String text,float x, float y,float dtShowTime,Color color,Color colorShadow){
          messages.add(new Message(text,x,y,dtShowTime,color,colorShadow));
      }

    public void addMessage(String text,float x, float y,float dtShowTime,Color color){
        messages.add(new Message(text,x,y,dtShowTime,color));
    }
     public void addMessageAndWait(String text,float x, float y,float dtShowTime,Color color,Color colorShadow){
        messages.add(new Message(text,x,y,dtShowTime,color,colorShadow));
    }


      public void update (float dt){
          Iterator<Message>  it = messages.iterator();
          while (it.hasNext()){
              Message m = it.next();
              m.dtCounter+=dt;
              if (m.dtCounter>m.dtTime) it.remove();
          }
      }

      public void restart(){
          messages.clear();
      }

        public void render (SpriteBatch batch){
            for (Message m:messages) {
//                //тень
//                if (m.colorShadow != null){
//                    font.setColor(m.colorShadow.r,m.colorShadow.g,m.colorShadow.b,1.00f-1.00f/(m.dtTime/m.dtCounter));
//                    font.draw(batch,m.text,m.x-1,m.y-1);
//                }
                Global.font.setColor(m.color.r, m.color.g, m.color.b, 1.00f - 1.00f / (m.dtTime / m.dtCounter));
                Global.font.draw(batch, m.text, m.x, m.y);
            }


    }


}
