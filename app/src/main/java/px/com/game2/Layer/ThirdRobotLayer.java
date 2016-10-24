package px.com.game2.Layer;

import android.util.Log;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.ccColor3B;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;
import px.com.game2.utils.RobotUtil;

import static android.R.attr.type;

/**
 * Created by admin on 2016/10/17.
 */

public class ThirdRobotLayer extends BaseLayer {

    /**
     * 自己的牌
     */
    private List<Poker> list;

    private List<Poker> showList;

    private CCLabel label;
    /**
     * 存台面上的牌
     */
    private List<Poker> tList;
    public  boolean isBig=true;

    public boolean isZhuang;

    /**
     * 存红色的2
     */
    public List<Poker> robList;
    /**
     * 存10
     */
    public List<Poker> robMlist;

    /**
     * 存抢庄的牌
     */
    public List<Poker> rlist;

    public ThirdRobotLayer() {
        tList=new ArrayList<>();
        list = new ArrayList<>();
        showList = new ArrayList<>();
        robList=new ArrayList<>();
        robMlist=new ArrayList<>();
        rlist=new ArrayList<>();


        label=CCLabel.makeLabel("黑","hkbd.ttf",24);
        label.setAnchorPoint(0,0);
        label.setColor(ccColor3B.ccBLUE);
        label.setPosition(170,winSize.height/2+60);
        label.setVisible(false);
        this.addChild(label);

    }


    private void show(CCSprite sprite, int with) {

        sprite.setAnchorPoint(0, 0);
        sprite.setPosition(with, winSize.getHeight() / 2);
        this.addChild(sprite);
    }

    /**
     * 自己出牌
     */
    public void OutCard()
    {  Log.e("第三个机器", "进入出牌界面");
        if (showList != null && showList.size() > 0) {
            for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
        }
        showList.add(list.get(list.size()-1));

        for (int i =0 ; i <showList.size() ; i++) {
            Log.e("第三个机器", "出的牌" + showList.get(i).getPokerValue() + "----" + showList.get(i).getPokertype());
            show(showList.get(i).getPolerSprite(),((30 * i) + 150));
            list.remove(showList.get(i));
        }
    }

    /**
     * 清除桌面上的牌
     */
    public void removeShowChild() {
        if (showList != null && showList.size() > 0) {
            for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
            tList.clear();
        } else if (tList != null && tList.size() > 0) {
            for (int i = 0; i < tList.size(); i++) {
                this.removeChild(tList.get(i).getPolerSprite(), true);
            }

            tList.clear();
        }
        this.removeAllChildren(true);
    }

    /**
     * poker桌面上出的牌
     *
     * @param poker
     */
    public void outPoker(List<Poker> poker) {
        Log.e("第三个机器", "进入打牌界面");
        int psize=poker.size();
        isBig=true;
       //先判读上次有没有牌在桌面上 有就干掉
        if (showList != null && showList.size() > 0) {
            for (int i = 0; i < showList.size(); i++) {
                this.removeChild(showList.get(i).getPolerSprite(), true);
            }
            showList.clear();
        }
        showList = RobotUtil.outPoker(list, poker, Poker.POKERTTYPE_W);
        Log.e("第三个机器", "showsize:"+showList.size()+"psize:"+psize);
       //出的牌不一致先清空
        if(showList.size() != poker.size())showList.clear();
        Log.e("第三个机器", "牌showList="+showList.size());
        //如果出的牌数目与桌面上的牌数目不一致直接退出
        if (showList==null||showList.size()==0||showList.size() != poker.size()) {
            //showLable();
            for (int i=0;i<psize;i++)
            {
                showList.add(list.get(list.size()-1-i));
                Log.e("第三个机器", "没有大的的牌");
            }

            isBig=false;
        }
        Log.e("第三个机器", "牌showList="+showList.size());
        for (int i =0 ; i <showList.size() ; i++) {
            Log.e("第三个机器", "打的牌" + showList.get(i).getPokerValue() + "----" + showList.get(i).getPokertype());
            show(showList.get(i).getPolerSprite(),((30 * i) + 150));
            list.remove(showList.get(i));
            if (!isBig)
            {   tList.add(showList.get(i));

            }
        }
        if(!isBig){ showList.clear();}

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
     * 是否是庄
     * @return
     */
    public boolean isZhuang() {
        return isZhuang;
    }

    /**
     * 设置能否抢庄
     * @param zhuang
     */
    public void setZhuang(boolean zhuang) {
        isZhuang = zhuang;
    }
    /**
     * 获取抢庄的牌
     * @return
     */
    public List<Poker> getRlist() {
        return rlist;
    }
    /**
     * 添加扑克
     * @param poker
     */
    public void addPoker(Poker poker)
    {
        if (poker.getPokerValue()==10&&(poker.getPokertype()==2||poker.getPokertype()==4))
        {
            robList.add(poker);
        }
        else if (poker.getPokerValue()==11)
        {
            robMlist.add(poker);
        }
        list.add(poker);



        if (robList.size()>0)
        {
            for (int j=0;j<robMlist.size();j++)
            {    if (isZhuang)
                {
                 return;
                }
                int type=robMlist.get(j).getPokertype();
                label.setVisible(true);
                switch (type)
                {
                    case Poker.POKERTTYPE_W:
                        rlist.add(robMlist.get(j));
                        label.setString("黑");
                        break;
                    case Poker.POKERTTYPE_R:
                        rlist.add(robMlist.get(j));
                        label.setString("红");
                        break;
                    case Poker.POKERTTYPE_M:
                        rlist.add(robMlist.get(j));
                        label.setString("梅");
                        break;
                    case Poker.POKERTTYPE_F:
                        rlist.add(robMlist.get(j));
                        label.setString("方");
                        break;
                }

                isZhuang=true;
            }


        }

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
