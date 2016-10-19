package px.com.game2.Layer;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;

/**
 * Created by admin on 2016/10/19.
 *
 * 本人的牌
 */

public class MyGameLayer extends  BaseLayer {

    public List<Poker> mList;
    /**
     * 在出牌集合
     */
    public List<Poker> moveSpriteUp = new ArrayList<>();
    /**
     * 索引
     */
    int i = 0;
    /**
     * 存红色的2
     */
    List<Poker> robList=new ArrayList<>();
    /**
     * 存10
     */
    List<Poker> robMlist=new ArrayList<>();



    public MyGameLayer() {
        mList=new ArrayList<>();
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
        this.mList = mlist;
        for (int i = 0; i < mList.size(); i++) {


            CCSprite sp = mList.get(i).getPolerSprite();

            sp.setPosition(winSize.width, 30);
            sp.setAnchorPoint(0, 0);
            sp.setVisible(false);
            this.addChild(sp);// Log.e("listsize", "数值==" + mList.get(i).getPokerValue() + "--type=" + list[i].getPokertype() + "listsize==" + mList.size() + "--dijifu==" + mList.get(i).getNum());
        }
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
}
