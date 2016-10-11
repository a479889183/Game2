package px.com.game2.Layer;

import android.view.MotionEvent;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */

public class GameLayer extends BaseLayer {
    String brand[]={"p13.png","p14.png","p15.png","p16.png","p17.png","p18.png","p19.png",};

    String head[]={"0.png", "4.png", "9.png", "10.png"};
    int i=1;
    public List<CCSprite> list=new ArrayList<>();
    public GameLayer() {
        init();
        this.setIsTouchEnabled(true);
    }

    private void init() {
        lodeBg();
        deal();
        lodePerson();
    }

    private void lodePerson() {
        CCSprite frist=CCSprite.sprite("0.png");
        frist.setPosition(winSize.width/2,40);
        frist.setAnchorPoint(0,0);
        this.addChild(frist);

        CCSprite second=CCSprite.sprite("4.png");
        second.setPosition(40,winSize.height/2);
        second.setAnchorPoint(0,0);
        this.addChild(second);


        CCSprite third=CCSprite.sprite("9.png");
        third.setPosition(winSize.width/2,winSize.height-100);
        third.setAnchorPoint(0,0);
        this.addChild(third);


        CCSprite fouth=CCSprite.sprite("10.png");
        fouth.setPosition(winSize.width-100,winSize.height/2);
        fouth.setAnchorPoint(0,0);
        this.addChild(fouth);
    }

    //加载背景图
    private  void lodeBg()
    {
        CCSprite sprite=CCSprite.sprite("bg.png");
        sprite.setAnchorPoint(0,0);
        this.addChild(sprite);


        for (int i=0;i<brand.length;i++)
        {
            CCSprite sprit=CCSprite.sprite(brand[i]);
            sprit.setPosition(winSize.width/2,winSize.height/2);
            sprit.setAnchorPoint(0,0);
            this.addChild(sprit);
            list.add(sprit);
        }
    }



    public void deal()
    {
        CCDelayTime ccdelay=CCDelayTime.action(2);

        CCSprite sprite = list.get(0);

        CCMoveTo ccMoveBy=CCMoveTo.action(0.25F, CGPoint.ccp(100,30));

        CCSequence sequence=CCSequence.actions(ccdelay,ccMoveBy, CCCallFunc.action(this,"lodeDeal"));

        sprite.runAction(sequence);
    }

    public void lodeDeal()
    {CCDelayTime ccdelay=CCDelayTime.action(0.1F);
           if (list.size()>i) {
               CCSprite sprite = list.get(i);

               CCMoveTo ccMoveBy = CCMoveTo.action(0.25F, CGPoint.ccp(100 + 30 * i, 30));

               CCSequence sequence = CCSequence.actions( ccMoveBy, ccdelay, CCCallFunc.action(this, "lodeDeal"));

               sprite.runAction(sequence);
               i++;
           }
    }
public void moveUpward(CCSprite sprite,int pos)
{
    CCMoveBy ccMoveTo=CCMoveBy.action(0.25f,ccp(0,30));
    CCSequence sequence=CCSequence.actions(ccMoveTo,CCCallFunc.action(this,"unLock"));
    sprite.runAction(sequence);
}

public void moveDown(CCSprite ccSprite,int pos)
{
    CCMoveBy ccMoveTo=CCMoveBy.action(0.25f,ccp(0,-30));
    CCSequence sequence=CCSequence.actions(ccMoveTo,CCCallFunc.action(this,"unLock"));
    ccSprite.runAction(sequence);
}
public void unLock()
{
    isLock=false;
}
    boolean isLock;
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint point = this.convertTouchToNodeSpace(event);
        if (!isLock) {
            for (int i = list.size() - 1; i >= 0; i--) {

                CGRect boundingBox = list.get(i).getBoundingBox();
                if (CGRect.containsPoint(boundingBox, point)) {
                    isLock = true;
                    if (boundingBox.origin.y > 31) {
                        moveDown(list.get(i), i + 1);
                        break;
                    } else {
                        moveUpward(list.get(i), i + 1);
                        break;
                    }
                }

            }
        }
        return super.ccTouchesBegan(event);
    }
}
