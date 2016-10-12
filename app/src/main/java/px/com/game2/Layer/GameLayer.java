package px.com.game2.Layer;

import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;
import px.com.game2.utils.CommonUtils;
import px.com.game2.utils.PokerUtils;

/**
 * Created by admin on 2016/10/11.
 */

public class GameLayer extends BaseLayer {
    String brand[] = {"brand/p13.png", "brand/p14.png", "brand/p15.png", "brand/p16.png", "brand/p17.png", "brand/p18.png", "brand/p19.png",};

    String head[] = {"head/0.png", "head/4.png", "head/9.png", "head/10.png"};
    /**
     * 按下的点
     */
    public CGPoint downPoint;
    /**
     * 抬起的点
     */
    public CGPoint upPoint;
    /**
     * 移动点的集合
     */
    public List<CCSprite> moveSprite = new ArrayList<>();

    /**
     * 在上面的移动点的集合
     */
    public List<CCSprite> moveSpriteUp = new ArrayList<>();

    int i = 0;
    /**
     * 扑克牌精灵
     */
    public Poker [] list;

    CCLabel label;

    PokerUtils pUtils;

    public boolean isMoveUp;

    public GameLayer() {
        init();
        this.setIsTouchEnabled(true);
    }

    private void init() {
        pUtils=new PokerUtils();
        lodeBg();
        deal();
        lodePerson();
        SendPoker();
    }

    public void  SendPoker()
    {
        label=CCLabel.makeLabel("发牌","hkbd.ttf",24);
        label.setColor(ccc3(50, 0, 255));
        label.setPosition(winSize.width/2,winSize.height/2);
        this.addChild(label);
    }

    private void lodePerson() {
        CCSprite frist = CCSprite.sprite("head/0.png");
        frist.setPosition(20, 40);
        frist.setAnchorPoint(0, 0);
        this.addChild(frist);

        CCSprite second = CCSprite.sprite("head/4.png");
        second.setPosition(40, winSize.height / 2);
        second.setAnchorPoint(0, 0);
        this.addChild(second);


        CCSprite third = CCSprite.sprite("head/9.png");
        third.setPosition(winSize.width / 2, winSize.height - 100);
        third.setAnchorPoint(0, 0);
        this.addChild(third);


        CCSprite fouth = CCSprite.sprite("head/10.png");
        fouth.setPosition(winSize.width - 100, winSize.height / 2);
        fouth.setAnchorPoint(0, 0);
        this.addChild(fouth);
    }

    //加载背景图
    private void lodeBg() {
        CCSprite sprite = CCSprite.sprite("blue_room_bg.jpg");
        sprite.setAnchorPoint(0, 0);
        this.addChild(sprite);

    }

    /**
     * 添加poker
     */
    public void addPoker()
    {    list= CommonUtils.getCard();
        list=pUtils.shufflecard(list);
        List<Poker[]> dealcard = pUtils.dealcard(3, list, 17);
        list=pUtils.sortingBig(dealcard.get(0));
        for (int i=0;i<list.length;i++)
        {
            CCSprite sp=list[i].getPolerSprite();

            sp.setPosition(winSize.width+100,winSize.height/2);
            sp.setAnchorPoint(0,0);
            this.addChild( sp);
            Log.e("listsize","数值=="+list[i].getPokerValue()+"--type="+list[i].getPokertype());
        }
    }

    /**
     * 移除poker
     */
    public void removePoker()
    {
        for (int i=0;i<list.length;i++)
        {
            this.removeChild(list[i].getPolerSprite(),true);
        }
        i = 0;
        deal();
    }
    /**
     * 初始化发牌
      */
    public void deal() {
        addPoker();
        CCDelayTime ccdelay = CCDelayTime.action(2);

        CCSprite sprite = list[i].getPolerSprite();

        CCMoveTo ccMoveBy = CCMoveTo.action(0.25F, CGPoint.ccp(120, 30));

        CCSequence sequence = CCSequence.actions(ccdelay, ccMoveBy, CCCallFunc.action(this, "lodeDeal"));

        sprite.runAction(sequence);
    }

    /**
     * 发牌动画
     */
    public void lodeDeal() {
        CCDelayTime ccdelay = CCDelayTime.action(0.1F);

            CCSprite sprite = list[i].polerSprite;

            CCMoveTo ccMoveBy = CCMoveTo.action(0.25F, CGPoint.ccp(120 + 30 * i, 30));

            CCSequence sequence = CCSequence.actions(ccMoveBy, ccdelay, CCCallFunc.action(this, "lodeDeal"));

            sprite.runAction(sequence);
            i++;

    }

    /**
     * 向上移动
     *
     * @param sprite
     */
    public void moveUpward(CCSprite sprite) {
        if (sprite.getBoundingBox().origin.y<31)
        {
            CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, 30));
            CCSequence sequence = CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "unLock"));
            sprite.runAction(sequence);
        }

    }

    /**
     * 向下移动
     *
     * @param ccSprite
     */
    public void moveDown(CCSprite ccSprite) {
        if (ccSprite.getBoundingBox().origin.y>31) {
            CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
            CCSequence sequence = CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "unLock"));
            ccSprite.runAction(sequence);
        }
    }

    /**
     * 扑克牌的点击事件
     *
     * @param point
     */
    public void onclik(CGPoint point) {
        if (!isLock) {
            for (int i = list.length - 1; i >= 0; i--) {

                CGRect boundingBox = list[i].getPolerSprite().getBoundingBox();
                if (CGRect.containsPoint(boundingBox, point)) {
                    isLock = true;
                    if (boundingBox.origin.y > 31) {
                        moveDown(list[i].getPolerSprite());
                        break;
                    } else {
                        moveUpward(list[i].getPolerSprite());
                        break;
                    }
                }

            }
        }
    }

    /**
     * 判断点是否在矩形上
     *
     * @param point
     * @param rect
     * @return
     */
    public boolean isExist(CGPoint point, CGRect rect) {
        if (CGRect.containsPoint(rect, point)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移动的扑克牌
     */
    public void getMoveSprite(CGPoint point) {
        if (isMoveUp) {
            for (CCSprite sprite : moveSprite) {
                if (isExist(point, sprite.getBoundingBox())) {
                    moveSpriteUp.add(sprite);
                }
            }

        } else {
            for (Poker pk : list) {
                if (isExist(point, pk.getPolerSprite().getBoundingBox())) {
                    moveSprite.add(pk.getPolerSprite());
                }
            }
        }

    }

    /**
     * 多选
     */
    public void MoveSpriteTo(List<CCSprite> Sprite) {
        for (CCSprite sprite : Sprite) {
            if (isMoveUp) {
                moveDown(sprite);
            } else {
                moveUpward(sprite);
            }

        }
    }

    /**
     * 触摸抬起
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        upPoint = this.convertTouchToNodeSpace(event);
        if (CGPoint.equalToPoint(downPoint, upPoint)) {
            onclik(upPoint);
        } else {
            if (!isMoveUp) {
                MoveSpriteTo(moveSprite);
                isMoveUp = true;

            } else {
                MoveSpriteTo(moveSpriteUp);
                isMoveUp = false;
                moveSprite.clear();
                moveSpriteUp.clear();
            }

        }
        return super.ccTouchesEnded(event);
    }

    /**
     * 触摸移动事件
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        CGPoint point = this.convertTouchToNodeSpace(event);

        getMoveSprite(point);
        return super.ccTouchesMoved(event);
    }

    /**
     * 触摸按下事件
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        downPoint = this.convertTouchToNodeSpace(event);
        Log.e("按下的点", "---------" + downPoint);
        if (CGRect.containsPoint(label.getBoundingBox(),downPoint))
        {
            removePoker();
        }
        return super.ccTouchesBegan(event);
    }

    public void unLock() {
        isLock = false;
    }

    boolean isLock;


}
