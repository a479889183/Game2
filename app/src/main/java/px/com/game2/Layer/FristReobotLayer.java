package px.com.game2.Layer;

import android.util.Log;

import org.cocos2d.nodes.CCSprite;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;
import px.com.game2.utils.RobotUtil;

/**
 * Created by admin on 2016/10/17.
 */

public class FristReobotLayer extends BaseLayer{

    /**
     * 自己的牌
     */
    private List<Poker> list;

    private List<Poker> showList;


    public FristReobotLayer() {

        list=new ArrayList<>();
        showList=new ArrayList<>();

    }


    private void show(CCSprite sprite,int with) {

        sprite.setAnchorPoint(0,0);
        sprite.setPosition(with,winSize.getHeight()/2);
        this.addChild(sprite);
    }


    /**
     * poker桌面上出的牌
     * @param poker
     */
    public void outPoker(List<Poker> poker)
    {
       showList=RobotUtil.outPoker(list,poker,Poker.POKERTTYPE_W);
       for (int i=0;i<showList.size();i++)
       {
           Log.e("first","out"+showList.get(i).getPokerValue()+"----"+showList.get(i).getPokertype());
           show(showList.get(i).getPolerSprite(),(30*i)+150);
           list.remove(showList.get(i));
       }

    }


    /**
     * 获得自己的牌
     * @return
     */
    public List<Poker> getList() {
        return list;
    }

    /**
     * 设置牌
     * @param list
     */
    public void setList(List<Poker> list) {
        this.list = list;
    }

    /**
     * 获得出的牌
     * @return
     */
    public List<Poker> getShowList() {
        return showList;
    }
}
