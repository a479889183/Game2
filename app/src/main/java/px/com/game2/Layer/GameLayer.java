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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import px.com.game2.bean.Poker;
import px.com.game2.utils.CommonUtils;
import px.com.game2.utils.PokerUtils;


/**
 * Created by admin on 2016/10/11.
 */

public class GameLayer extends BaseLayer {

    /**
     * 按下的点
     */
    public CGPoint downPoint;
    /**
     * 抬起的点
     */
    public CGPoint upPoint;
    /**
     * 存储选中的牌
     */
    public List<Poker> moveSprite = new ArrayList<>();

    /**
     * 在出牌集合
     */
    public List<Poker> moveSpriteUp = new ArrayList<>();

    /**
     * 底牌
     */
    public List<Poker> remainPoker = new ArrayList<>();

    int i = 0;
    /**
     * 扑克牌精灵
     */
    public Poker[] list;

    /**
     * 自己的牌的精灵
     */
    public List<Poker> mList = new ArrayList<>();

    /**
     * 第二个人的牌
     */
    public List<Poker> secondList = new ArrayList<>();

    /**
     * 第三个人的牌
     */
    public List<Poker> thirdList = new ArrayList<>();

    CCLabel label, playLable;

    PokerUtils pUtils;
    /**
     * 扑克牌的第一个位置
     */
    float initX;
    /**
     * 速度
     */
    float speed = 0.01F;
    /**
     * 每次扑克牌加的宽度
     */
    int addX;
    /**
     * 第一次出牌的位置
     */
    float exitX;
    /**
     * 存储上一次出的牌集合
     */
    HashSet<Poker> pokerSet = new HashSet<>();


    public GameLayer() {
        init();
        this.setIsTouchEnabled(true);
    }

    private void init() {
        pUtils = new PokerUtils();
        lodeBg();
        deal();
        lodePerson();
        SendPoker();
        outPoker();
    }

    /**
     * 出牌
     */
    private void outPoker() {
        playLable = CCLabel.makeLabel("出牌", "hkbd.ttf", 24);
        playLable.setColor(ccc3(50, 0, 255));
        playLable.setPosition(winSize.width / 2, winSize.height / 2 - 50);
        this.addChild(playLable);
    }

    /**
     * 发牌
     */
    public void SendPoker() {
        label = CCLabel.makeLabel("发牌", "hkbd.ttf", 24);
        label.setColor(ccc3(50, 0, 255));
        label.setPosition(winSize.width / 2, winSize.height / 2);
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
    public void addPoker() {

        //54张牌
        /*list = CommonUtils.getCard();
        list = pUtils.shufflecard(list);
        List<Poker[]> dealcard = pUtils.dealcard(3, list, 17);*/
        //92张牌
        list = CommonUtils.getCardB();
        list = pUtils.shufflecard(list);
        List<Poker[]> dealcard = pUtils.dealcard(4, list, 21);
        //底牌
        for (int i = list.length-1; i > list.length - 8; i--) {
            remainPoker.add(list[i]);
        }
        Log.e("---size","++++++++++"+remainPoker.size());
        mList.clear();
        Collections.addAll(mList, pUtils.sortingBig(dealcard.get(0)));

        Collections.addAll(secondList, pUtils.sortingBig(dealcard.get(1)));

        Collections.addAll(thirdList, pUtils.sortingBig(dealcard.get(2)));

        for (int i = 0; i < mList.size(); i++) {
            CCSprite sp = mList.get(i).getPolerSprite();

            sp.setPosition(winSize.width + 100, winSize.height / 2);
            sp.setAnchorPoint(0, 0);
            this.addChild(sp);
            Log.e("listsize", "数值==" + mList.get(i).getPokerValue() + "--type=" + list[i].getPokertype() + "listsize==" + mList.size() + "--dijifu==" + mList.get(i).getNum());
        }
    }

    /**
     * 移除poker
     */
    public void removePoker() {
        for (int i = 0; i < list.length; i++) {
            this.removeChild(list[i].getPolerSprite(), true);
        }
        i = 0;
        deal();
    }

    /**
     * 初始化扑克牌的位置
     *
     * @param sprite
     */
    public void initPokerPoint(CCSprite sprite) {
        int width = (int) sprite.getBoundingBox().size.getWidth();
        int sSize = 0;
        if (mList.size() / 2 != 0) {
            sSize = (mList.size() / 2) + 1;
        } else {
            sSize = mList.size() / 2;
        }
        initX = width + width + ((width / 3) * (mList.size() - 2));
        initX = (winSize.width / 2) - initX / 2;
        addX = (width / 3);
    }

    /**
     * 初始化发牌
     */
    public void deal() {
        addPoker();
        initdeal();
    }

    public void initdeal() {
        if (moveSpriteUp == null) {
            moveSpriteUp = new ArrayList<>();
        }
        i = 0;
        CCDelayTime ccdelay = CCDelayTime.action(2);
        if (mList.size() == 0) return;
        CCSprite sprite = mList.get(i).getPolerSprite();
        initPokerPoint(sprite);
        CCMoveTo ccMoveBy = CCMoveTo.action(speed, CGPoint.ccp(initX, 30));
        CCSequence sequence = CCSequence.actions(ccMoveBy, CCCallFunc.action(this, "lodeDeal"));
        sprite.runAction(sequence);
    }

    /**
     * 发牌动画
     */
    public void lodeDeal() {
        CCDelayTime ccdelay = CCDelayTime.action(speed);
        if (mList.size() <= i) {
            i = 0;
            return;
        }
        CCSprite sprite = mList.get(i).polerSprite;

        CCMoveTo ccMoveBy = CCMoveTo.action(speed, CGPoint.ccp(initX + addX * i, 30));

        CCSequence sequence = CCSequence.actions(ccMoveBy, ccdelay, CCCallFunc.action(this, "lodeDeal"));
        sprite.runAction(sequence);
        i++;
    }

    /**
     * 向上移动
     */
    public void moveUpward(Poker poker) {
        if (poker.getPolerSprite().getBoundingBox().origin.y == 30) {
            CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, 30));
            CCSequence sequence = CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "unLock"));
            poker.getPolerSprite().runAction(sequence);
            moveSpriteUp.add(poker);
        }

    }

    /**
     * 向下移动
     */
    public void moveDown(Poker poker) {
        if (poker.getPolerSprite().getBoundingBox().origin.y == 60) {
            CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
            CCSequence sequence = CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "unLock"));
            poker.getPolerSprite().runAction(sequence);
            moveSpriteUp.remove(poker);
        }
    }

    /**
     * 扑克牌的点击事件
     *
     * @param point
     */
    public void onclik(CGPoint point) {
        if (!isLock) {
            for (int i = mList.size() - 1; i >= 0; i--) {

                CGRect boundingBox = mList.get(i).getPolerSprite().getBoundingBox();
                if (CGRect.containsPoint(boundingBox, point)) {
                    isLock = true;
                    if (boundingBox.origin.y > 31) {
                        moveDown(mList.get(i));
                        break;
                    } else {
                        moveUpward(mList.get(i));
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

        for (Poker sprite : mList) {
            if (isExist(point, sprite.getPolerSprite().getBoundingBox())) {
                moveSprite.add(sprite);
            }
        }
    }

    /**
     * 清除上次的牌
     */
    public void removeLastPoker() {
        if (pokerSet != null && pokerSet.size() > 0) {
            for (Poker pk : pokerSet) {
                removePoker(pk.getPolerSprite());
            }
            pokerSet = null;
            pokerSet = new HashSet<>();
        }
    }

    /**
     * 出牌
     */
    public void exitPoker() {
        if (moveSpriteUp != null || moveSpriteUp.size() > 0) {

            HashSet<Poker> pokset = new HashSet<>(moveSpriteUp);
            moveSpriteUp.clear();
            moveSpriteUp.addAll(pokset);


            int length = moveSpriteUp.size();
            int width = (int) moveSpriteUp.get(0).getPolerSprite().getBoundingBox().size.getWidth();
            int sSize = 0;
            if (length / 2 != 0) {
                sSize = (length / 2) + 1;
            } else {
                sSize = length / 2;
            }
            exitX = width + width + ((width / 3) * (length - 2));
            exitX = (winSize.width / 2) - exitX / 2;


            //先移除扑克牌

            Poker[] pokers = moveSpriteUp.toArray(new Poker[moveSpriteUp.size()]);
            pokers = pUtils.sortingBig(pokers);
            moveSpriteUp.clear();
            Collections.addAll(moveSpriteUp, pokers);

            if (!pUtils.pokerRules(moveSpriteUp)) {   //不符合出牌规则 把牌往下移

                for (int i = 0; i < moveSpriteUp.size(); i++) {
                    CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
                    moveSpriteUp.get(i).getPolerSprite().runAction(ccMoveTo);
                }
                moveSpriteUp.clear();
                return;
            }
            removeLastPoker();
            for (Poker pk : moveSpriteUp) {
                removePoker(pk.getPolerSprite());
                mList.remove((pk));
            }
            i = 0;
            pokerSet.add(moveSpriteUp.get(i));
            exitMoveAnimation(exitX, winSize.height / 2, moveSpriteUp.get(i).getPolerSprite());
            speed = 0.001F;
            initdeal();
        }
    }

    /**
     * 移除扑克牌
     *
     * @param sprite
     */
    public void removePoker(CCSprite sprite) {
        this.removeChild(sprite, true);
    }

    /**
     * 初始化出牌动作
     *
     * @param x
     * @param y
     * @param sprite
     */
    public void exitMoveAnimation(float x, float y, CCSprite sprite) {
        sprite.setPosition(x, y);
        sprite.setAnchorPoint(0, 0);
        CCCallFunc call = CCCallFunc.action(this, "addChildPoker");
        this.addChild(sprite);
        sprite.runAction(call);

    }

    /**
     * 把出的牌放到屏幕中间
     */
    public void addChildPoker() {
        i++;
        if (i >= moveSpriteUp.size()) {
            moveSpriteUp.clear();
            moveSpriteUp = null;
            return;
        }
        CCSprite sprite = moveSpriteUp.get(i).getPolerSprite();

        sprite.setPosition(exitX + (addX) * i, winSize.getHeight() / 2);

        this.addChild(sprite);
        pokerSet.add(moveSpriteUp.get(i));
        addChildPoker();
    }

    /**
     * 多选
     */
    public void MoveSpriteTo(List<Poker> Sprite) {

        for (int i = 0; i < Sprite.size(); i++) {

            if (Sprite.get(i).getPolerSprite().getBoundingBox().origin.y == 30) {
                moveUpward(Sprite.get(i));

            } else if (Sprite.get(i).getPolerSprite().getBoundingBox().origin.y == 60) {
                moveDown(Sprite.get(i));

            }
        }
    }

    /**
     * 触摸抬起
     *
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        upPoint = this.convertTouchToNodeSpace(event);
        if (CGPoint.equalToPoint(downPoint, upPoint)) {
            onclik(upPoint);
        } else {
            //处理滑动时选中的牌
            HashSet set = new HashSet(moveSprite);
            moveSprite.clear();
            moveSprite.addAll(set);
            MoveSpriteTo(moveSprite);
            moveSprite.clear();
        }
        return super.ccTouchesEnded(event);
    }

    /**
     * 触摸移动事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        CGPoint point = this.convertTouchToNodeSpace(event);
        //得到滑动时选中的扑克牌
        getMoveSprite(point);
        return super.ccTouchesMoved(event);
    }

    /**
     * 触摸按下事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        downPoint = this.convertTouchToNodeSpace(event);
        Log.e("按下的点", "---------" + downPoint);
        if (CGRect.containsPoint(label.getBoundingBox(), downPoint)) {
            removePoker();
        } else if (CGRect.containsPoint(playLable.getBoundingBox(), downPoint)) {
            exitPoker();
        }
        return super.ccTouchesBegan(event);
    }

    public void unLock() {
        isLock = false;
    }

    boolean isLock;

}
