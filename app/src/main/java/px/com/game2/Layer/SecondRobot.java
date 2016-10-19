package px.com.game2.Layer;

import android.util.Log;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;
import px.com.game2.utils.RobotUtil;


/**
 * Created by admin on 2016/10/18.
 */

public class SecondRobot extends BaseLayer {

    /**
     * 自己的牌
     */
    private List<Poker> list;

    private List<Poker> showList;

    private CCLabel label;

    public SecondRobot() {
        list=new ArrayList<>();

        showList=new ArrayList<>();

        label=CCLabel.makeLabel("要不起","hkbd.ttf",24);
        label.setAnchorPoint(0,0);
        label.setPosition(winSize.width/2,winSize.height-160);
        label.setVisible(false);
        this.addChild(label);

    }



    private void show(CCSprite sprite, int with) {

        sprite.setAnchorPoint(0, 0);
        sprite.setPosition(with, winSize.getHeight()-160);
        this.addChild(sprite);
    }

    /**
     * 自己出牌
     */
    public void OutCard()
    {   //计算位置
        int first;
        int bsize=showList.size();
        int swith=(int) showList.get(0).getPolerSprite().getBoundingBox().size.getWidth();
        if (showList.size() / 2 != 0) {
            bsize = (showList.size() / 2) + 1;
        } else {
            bsize = showList.size() / 2;
        }
        first = swith + swith + ((swith / 3) * (showList.size() - 2));
        first =(int) (winSize.width / 2) - first / 2;
        //先清除上一次的牌

        if (showList != null && showList.size() > 0) {
            for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
        }
        showList.add(list.get(list.size()-1));

        for (int i =0; i<showList.size() ; i++) {
            Log.e("first", "out" + showList.get(i).getPokerValue() + "----" + showList.get(i).getPokertype());
            show(showList.get(i).getPolerSprite(), (swith/3)*i+first);
            list.remove(showList.get(i));
        }

    }
    /**
     * 清除桌面上的牌
     */
    public void removeShowChild()
    {
        if (showList != null && showList.size() > 0) {
            for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
        }
    }

    /**
     * poker桌面上出的牌
     *
     * @param poker
     */
    public void outPoker(List<Poker> poker) {
        int psize=poker.size();
        for (int i = 0; i < list.size(); i++) {
            Log.e("outPoker", "---值： " + list.get(i).getPokerValue() + "---类型： " + list.get(i).getPokertype());
        }
        //先判读上次有没有牌在桌面上 有就干掉
        if (showList != null && showList.size() > 0) {
            for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
        }
        showList = RobotUtil.outPoker(list, poker, Poker.POKERTTYPE_W);

        //如果出的牌数目与桌面上的牌数目不一致直接退出
        //如果出的牌数目与桌面上的牌数目不一致直接退出
        if (showList==null||showList.size()==0||showList.size() != poker.size()) {
            showLable();
            for (int i=0;i<psize;i++)
            {
                showList.add(list.get(list.size()-1-i));
            }
         /*   for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
            return;*/
        }
        int first;
        int bsize=showList.size();
        int swith=(int) showList.get(0).getPolerSprite().getBoundingBox().size.getWidth();
        if (showList.size() / 2 != 0) {
            bsize = (showList.size() / 2) + 1;
        } else {
            bsize = showList.size() / 2;
        }
        first = swith + swith + ((swith / 3) * (showList.size() - 2));
        first =(int) (winSize.width / 2) - first / 2;



        for (int i =0; i<showList.size() ; i++) {
            Log.e("first", "out" + showList.get(i).getPokerValue() + "----" + showList.get(i).getPokertype());
            show(showList.get(i).getPolerSprite(), (swith/3)*i+first);
            list.remove(showList.get(i));
        }

    }
    public void showLable()
    {
        label.setVisible(true);
        CCDelayTime delayTime=CCDelayTime.action(2);

        CCSequence sequence=CCSequence.actions(delayTime, CCCallFunc.action(this,"hindlable"));

        label.runAction(sequence);
    }

    public void  hindlable()
    {
        label.setVisible(false);
    }

    /**
     * 获得自己的牌
     *
     * @return
     */
    public List<Poker> getList() {
        return list;
    }

    /**
     * 设置牌
     *
     * @param list
     */
    public void setList(List<Poker> list) {
        this.list = list;
    }

    /**
     * 获得出的牌
     *
     * @return
     */
    public List<Poker> getShowList() {
        return showList;
    }
}
