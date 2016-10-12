package px.com.game2.bean;

import org.cocos2d.nodes.CCSprite;


/**
 * Created by admin on 2016/10/12.
 */

public class Poker {
    /**
     * 黑桃
     */
    public static final int POKERTTYPE_W=1;

    /**
     * 红桃
     */
    public static final int POKERTTYPE_R=2;

    /**
     * 梅花
     */
    public static final int POKERTTYPE_M=3;

    /**
     * 方块
     */
    public static final int POKERTTYPE_F=4;
    /**
     * 小王
     */
    public static final int POKERTTYPE_S=5;

    /**
     * 大王
     */
    public static final int POKERTTYPE_B=6;

    /**
     * 扑克牌的值
     */
    public int PokerValue;
    /**
     * 扑克牌对应的精灵
     */
    public CCSprite  polerSprite;
    /**
     * 扑克牌的类型 黑、红、梅、方
     */
    public int pokertype;

    public void  Poker()
    {

    }

    public int getPokerValue() {
        return PokerValue;
    }

    public void setPokerValue(int pokerValue) {
        PokerValue = pokerValue;
    }

    public CCSprite getPolerSprite() {
        return polerSprite;
    }

    public void setPolerSprite(CCSprite polerSprite) {
        this.polerSprite = polerSprite;
    }

    public int getPokertype() {
        return pokertype;
    }

    public void setPokertype(int pokertype) {
        this.pokertype = pokertype;
    }
}
