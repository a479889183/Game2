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
import org.cocos2d.types.ccColor3B;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import px.com.game2.bean.Poker;
import px.com.game2.utils.PokerUtils;



/**
 * Created by admin on 2016/10/19.
 *
 * 本人的牌
 */

public class MyGameLayer extends  BaseLayer {

    public List<Poker> mList;
    /**
     * 索引
     */
    int i = 0;
    /**
     * 存红色的2
     */
    List<Poker> robList;
    /**
     * 存10
     */
    List<Poker> robMlist;

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
     * 自己出的牌
     */
     private List<Poker> mOutPoker;
    /**
     * 亮牌文字
     */
    CCLabel wlabel,rlabel,mlabel,flabel,zlabel;

    PokerUtils pUtils;

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
    public List<Poker> moveSprite ;

    /**
     * 在出牌集合
     */
    public List<Poker> moveSpriteUp ;

    /**
     * 存储上一次出的牌集合
     */
    HashSet<Poker> pokerSet ;


    public MyGameLayer() {
        mList=new ArrayList<>();
        pUtils=new PokerUtils();
        mOutPoker=new ArrayList<>();
        robList=new ArrayList<>();

        moveSprite = new ArrayList<>();

        moveSpriteUp = new ArrayList<>();

        robMlist=new ArrayList<>();

        pokerSet = new HashSet<>();
        initLable();
    }

    /**
     * 获得自己的扑克
     * @return
     */
    public List<Poker> getMlist() {
        return mList;
    }



    /**
     * 设置扑克
     * @param mlist
     */
    public void setMlist(List<Poker> mlist) {
        removeLastPoker();
        removepoker();
        this.mList = mlist;
        for (int i = 0; i < mList.size(); i++) {

            CCSprite sp = mList.get(i).getPolerSprite();

            sp.setPosition(winSize.width, 30);
            sp.setAnchorPoint(0, 0);
            sp.setVisible(false);
            this.addChild(sp);
             Log.e("listsize", "数值==" + mList.get(i).getPokerValue() + "--type=" + mList.get(i).getPokertype() + "listsize==" + mList.size() + "--dijifu==" + mList.get(i).getNum());
        }
    }

    /**
     * 获取自己出的牌
     * @return
     */
    public List<Poker> getmOutPoker() {
        return mOutPoker;
    }

    /**
     * 抢庄
     */
    public void robZhuang(List<Poker> remainPoker)
    {
        if (moveSpriteUp!=null&&moveSpriteUp.size()>0)
        {
            HashSet<Poker> pokset = new HashSet<>(moveSpriteUp);
            moveSpriteUp.clear();
            moveSpriteUp.addAll(pokset);

            if(pUtils.robZhuang(moveSpriteUp))
            {
                /*showToast("亮牌成功");*/
                for (int i = 0; i < moveSpriteUp.size(); i++) {
                    CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
                    moveSpriteUp.get(i).getPolerSprite().runAction(ccMoveTo);
                }
                moveSpriteUp.clear();
                //先清除界面上的牌
                for (int i = 0; i < mList.size(); i++) {
                    this.removeChild(mList.get(i).getPolerSprite(), true);
                }
                mList.addAll(remainPoker);
                for (int i=0;i<mList.size();i++)
                {
                    CCSprite sp = mList.get(i).getPolerSprite();

                    sp.setPosition(winSize.width + 100, winSize.height / 2);
                    sp.setAnchorPoint(0, 0);
                    this.addChild(sp);
                }

                initdeal();
            }
            else
            {
                /*showToast("亮牌不符合规则");*/
                for (int i = 0; i < moveSpriteUp.size(); i++) {
                    CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
                    moveSpriteUp.get(i).getPolerSprite().runAction(ccMoveTo);
                }
                moveSpriteUp.clear();
            }
        }
    }


    /**
     * 清除桌面上的扑克
     */
    public void removepoker()
    {
        for (int i = 0; i < mList.size(); i++) {
            this.removeChild(mList.get(i).getPolerSprite(), true);
        }
        i = 0;
    }
    /**
     * 重新排序
     */
    public void initdeal() {
        if (moveSpriteUp == null) {
            moveSpriteUp = new ArrayList<>();
        }
        i = 0;
        CCDelayTime ccdelay = CCDelayTime.action(2);
        if (mList.size() == 0) return;

        CCSprite sprite = mList.get(i).getPolerSprite();
        sprite.setVisible(true);

        robList.clear();
        robMlist.clear();
        //把2加进去
        if (mList.get(i).getPokerValue()==10&&(mList.get(i).getPokertype()==Poker.POKERTTYPE_R||mList.get(i).getPokertype()==Poker.POKERTTYPE_F))
        {
            robList.add(mList.get(i));
        }
        //把10加进去
        else if (mList.get(i).getPokerValue()==11)
        {
            robMlist.add(mList.get(i));
        }

        initPokerPoint(sprite);
        CCMoveTo ccMoveBy = CCMoveTo.action(speed, CGPoint.ccp(initX, 30));
        CCSequence sequence = CCSequence.actions(ccMoveBy, CCCallFunc.action(this, "lodeDeal"));
        sprite.runAction(sequence);
    }
    /**
     * 发牌动画
     */
    public void lodeDeal() {


        if (mList.size() <= i) {
            i = 0;
            return;
        }
        //把2加进去
        if (mList.get(i).getPokerValue()==10&&(mList.get(i).getPokertype()==Poker.POKERTTYPE_R||mList.get(i).getPokertype()==Poker.POKERTTYPE_F))
        {
            robList.add(mList.get(i));
        }
        //把10加进去
        else if (mList.get(i).getPokerValue()==11)
        {
            robMlist.add(mList.get(i));
        }
        if (robList.size()>0)
        {
            for (int j=0;j<robMlist.size();j++)
            {
                int type=robMlist.get(j).getPokertype();
                switch (type)
                {
                    case Poker.POKERTTYPE_W:
                        wlabel.setColor(ccColor3B.ccRED);
                        break;
                    case Poker.POKERTTYPE_R:
                        rlabel.setColor(ccColor3B.ccRED);
                        break;
                    case Poker.POKERTTYPE_M:
                        mlabel.setColor(ccColor3B.ccRED);
                        break;
                    case Poker.POKERTTYPE_F:
                        flabel.setColor(ccColor3B.ccRED);
                        break;
                }
            }


        }
        else if (robList.size()>2)
        {
            zlabel.setColor(ccColor3B.ccRED);
        }
        CCSprite sprite = mList.get(i).polerSprite;
        sprite.setVisible(true);
        CCMoveTo ccMoveBy = CCMoveTo.action(0.25F, CGPoint.ccp(initX + addX * i, 30));

        CCSequence sequence = CCSequence.actions(ccMoveBy,  CCCallFunc.action(this, "lodeDeal"));
        sprite.runAction(sequence);
        i++;
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
     * 设置亮牌
     */
    public void initLable()
    {
        wlabel=CCLabel.labelWithString("黑","hkbd.ttf",16);
        wlabel.setPosition(winSize.width / 2-60, 220);
        this.addChild(wlabel);

        rlabel=CCLabel.labelWithString("红","hkbd.ttf",16);
        rlabel.setPosition(winSize.width / 2-30, 220);
        this.addChild(rlabel);

        mlabel=CCLabel.labelWithString("梅","hkbd.ttf",16);
        mlabel.setPosition(winSize.width / 2, 220);
        this.addChild(mlabel);

        flabel=CCLabel.labelWithString("方","hkbd.ttf",16);
        flabel.setPosition(winSize.width / 2+30, 220);
        this.addChild(flabel);

        zlabel=CCLabel.labelWithString("反","hkbd.ttf",16);
        zlabel.setPosition(winSize.width / 2+60, 220);
        this.addChild(zlabel);

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
    public void exitPoker(List<Poker> otherList,int type) {
        if (moveSpriteUp != null && moveSpriteUp.size() > 0) {

            HashSet<Poker> pokset = new HashSet<>(moveSpriteUp);
            moveSpriteUp.clear();
            moveSpriteUp.addAll(pokset);
            //获得对手出的牌
            //otherList=fristReobot.getShowList();
            //计算显示的位置
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

            //重新排序
            pokers=pUtils.sort(pokers,type);

            moveSpriteUp.clear();

            Collections.addAll(moveSpriteUp, pokers);



            if (!pUtils.pokerRulesB(moveSpriteUp)) {   //不符合出牌规则 把牌往下移

                for (int i = 0; i < moveSpriteUp.size(); i++) {
                    CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
                    moveSpriteUp.get(i).getPolerSprite().runAction(ccMoveTo);
                }
                moveSpriteUp.clear();
                return;
            }
            //判断是否大于桌面上的牌
            if (otherList!=null&&otherList.size()>0)
            {
                boolean f =pUtils.isSize(moveSpriteUp,otherList,Poker.POKERTTYPE_W);
                if (!f)
                {
                    for (int i = 0; i < moveSpriteUp.size(); i++) {
                        CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
                        moveSpriteUp.get(i).getPolerSprite().runAction(ccMoveTo);
                    }
                    moveSpriteUp.clear();
                    return;
                }
            }
            //先清除上次的牌
            mOutPoker.clear();
            //把自己出的牌放进去
            mOutPoker.addAll(moveSpriteUp);

          /*  //第一个人出牌
            robotPoker();*/

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
     * 移除扑克牌
     *
     * @param sprite
     */
    public void removePoker(CCSprite sprite) {
        this.removeChild(sprite, true);
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
     * 向上移动
     */
    public void moveUpward(Poker poker) {
        if (poker.getPolerSprite().getBoundingBox().origin.y == 30) {
            CCMoveBy ccMoveTo = CCMoveBy.action(speed, ccp(0, 30));
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
            CCMoveBy ccMoveTo = CCMoveBy.action(speed, ccp(0, -30));
            CCSequence sequence = CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "unLock"));
            poker.getPolerSprite().runAction(sequence);
            moveSpriteUp.remove(poker);
        }
    }


    public void unLock() {
        isLock = false;
    }

    boolean isLock;
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
     * 埋牌
     */
    public List<Poker> buriedPoker(int type)
    {   List<Poker> mpk=new ArrayList<>();
        if (moveSpriteUp!=null&&moveSpriteUp.size()==8)
        {    HashSet<Poker> pokset = new HashSet<>(moveSpriteUp);
            moveSpriteUp.clear();
            moveSpriteUp.addAll(pokset);

            //先移除桌面上的牌
            mpk=moveSpriteUp;
            for (int i=0;i<mList.size();i++)
            {
                removePoker(mList.get(i).getPolerSprite());
            }

            for (Poker pk : moveSpriteUp) {
                removePoker(pk.getPolerSprite());
                mList.remove((pk));
            }
            Poker[] pokers = mList.toArray(new Poker[mList.size()]);
            mList.clear();
            //重新排序
            Collections.addAll(mList, pUtils.sort(pokers,type));

            //重新加到界面上
            for (int i = 0; i < mList.size(); i++) {
                CCSprite sp = mList.get(i).getPolerSprite();

                sp.setPosition(winSize.width + 100, winSize.height / 2);
                sp.setAnchorPoint(0, 0);
                this.addChild(sp);

            }

            initdeal();
        }
      return  mpk;
    }
}
