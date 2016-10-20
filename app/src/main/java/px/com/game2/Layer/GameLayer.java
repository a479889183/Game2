package px.com.game2.Layer;

import android.util.Log;
import android.view.MotionEvent;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
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
import px.com.game2.utils.RobotUtil;

import static android.R.attr.label;


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
     * 第一个电脑
     */
    public FristRobotLayer fristReobot;
    /**
     * 第二个电脑
     */
    public SecondRobot secondRobot;

    /**
     * 第三个电脑
     */
    public ThirdRobotLayer thirdRobotLayer;
    /**
     * 自己
     */
    public MyGameLayer myGameLayer;

    /**
     * 第二个人的牌
     */
    public List<Poker> secondList = new ArrayList<>();

    /**
     * 第三个人的牌
     */
    public List<Poker> thirdList = new ArrayList<>();
    /**
     * 第4个人的牌
     */
    public List<Poker> fouthList=new ArrayList<>();

    /**
     * 对手出的牌
     */
    public List<Poker> otherList=new ArrayList<>();

    public RobotUtil rbUtil;
    /**
     * 第几个人出牌
     *
     * 1为自己
     */
    public int sendPokerPeople=1;

    /**
     * 主色
     */
    public int mainPoker=1;

    public int spoker=1;



    CCLabel label, playLable,showToast,roblable,buriedLable;


    PokerUtils pUtils;



    public GameLayer() {
        init();
        this.setIsTouchEnabled(true);
    }

    private void init() {
        pUtils = new PokerUtils();
        rbUtil=new RobotUtil();
        fristReobot=new FristRobotLayer();
        secondRobot=new SecondRobot();
        thirdRobotLayer=new ThirdRobotLayer();
        myGameLayer=new MyGameLayer();
        lodeBg();
        deal();
        lodePerson();

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

        playLable = CCLabel.makeLabel("出牌", "hkbd.ttf", 24);
        playLable.setColor(ccc3(50, 0, 255));
        playLable.setPosition(winSize.width / 2, winSize.height / 2 - 50);
        this.addChild(playLable);

        label = CCLabel.makeLabel("发牌", "hkbd.ttf", 24);
        label.setColor(ccc3(50, 0, 255));
        label.setPosition(winSize.width / 2, winSize.height / 2);
        this.addChild(label);

        showToast=CCLabel.labelWithString("你的牌不符合规则", "hkbd.ttf", 24);
        showToast.setColor(ccc3(100, 0, 255));
        showToast.setPosition(winSize.width / 2, winSize.height / 2+100);
        showToast.setVisible(false);
        this.addChild(showToast);


        roblable=CCLabel.labelWithString("抢庄", "hkbd.ttf", 24);
        roblable.setColor(ccc3(50, 60, 255));
        roblable.setPosition(winSize.width-100,60);
        this.addChild(roblable);

        buriedLable=CCLabel.labelWithString("埋牌", "hkbd.ttf", 24);
        buriedLable.setColor(ccc3(255, 60, 129));
        buriedLable.setPosition(winSize.width/2,200);
        this.addChild(buriedLable);
    }

    /**
     * 显示提示信息
     * @param str
     */
    public void showToast(String str)
    {
      label.setString(str);
      label.setVisible(true);
      CCDelayTime delayTime=CCDelayTime.action(2);
      CCSequence sequence=CCSequence.actions(delayTime,CCCallFunc.action(this,"hindToast"));
      label.runAction(sequence);
    }

    public void hindToast()
    {
        label.setVisible(false);
    }
    private void lodePerson() {
       /* CCSprite frist = CCSprite.sprite("head/0.png");
        frist.setPosition(20, 40);
        frist.setAnchorPoint(0, 0);
        this.addChild(frist);*/

        this.addChild(fristReobot);
        this.addChild(secondRobot);
        this.addChild(thirdRobotLayer);
        this.addChild(myGameLayer);
        SendPoker();
        outPoker();

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
        remainPoker.clear();
        //底牌
        for (int i = list.length-1; i > list.length - 9; i--) {
            remainPoker.add(list[i]);
        }

        //把牌发给4个人
        mList.clear();
        Collections.addAll(mList, pUtils.sort(dealcard.get(0),1));
        myGameLayer.setMlist(mList);


        secondList.clear();

        Collections.addAll(secondList, pUtils.sortingBig(dealcard.get(1)));

        fristReobot.setList(secondList);

        thirdList.clear();

        Collections.addAll(thirdList, pUtils.sortingBig(dealcard.get(2)));
        secondRobot.setList(thirdList);

        fouthList.clear();

        Collections.addAll(fouthList, pUtils.sortingBig(dealcard.get(3)));

        thirdRobotLayer.setList(fouthList);



    }

    /**
     * 移除poker
     */
    public void removePoker() {
        otherList.clear();
        myGameLayer.removepoker();
        deal();
    }


    /**
     * 初始化发牌
     */
    public void deal() {
        addPoker();
        initdeal();
    }

    /**
     * 重新排序
     */
    public void initdeal() {
        myGameLayer.initdeal();

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
     * 埋牌
     */
    public void buriedPoker()
    {   remainPoker.clear();
        //底牌=埋的牌
        remainPoker= myGameLayer.buriedPoker(1);
    }


    /**
     * 抢庄
     */
    public void robZhuang()
    {
        myGameLayer.robZhuang(remainPoker);
    }


    /**
     * 出牌
     */
    public void exitPoker() {

        myGameLayer.exitPoker(otherList,mainPoker);
        List<Poker> pokerList = myGameLayer.getmOutPoker();
        if (pokerList==null||pokerList.size()==0) return;
        otherList=pokerList;
        if (sendPokerPeople==1) {
            Log.e("---sendPokerPeople:",sendPokerPeople+":-----dijige");
            CCDelayTime delayTime = CCDelayTime.action(2);
            CCCallFunc callFunc = CCCallFunc.action(this, "robotPoker");
            CCSequence sequence = CCSequence.actions(delayTime, callFunc);
            this.runAction(sequence);
        }
        //第一个出牌的人是第2个机器和第3个机器
        else if (spoker==5||spoker==4)
        {   Log.e("---sendPokerPeople111:",sendPokerPeople+":-----dijige---spoker:"+spoker);
            CCDelayTime delayTime = CCDelayTime.action(2);
            CCCallFunc callFunc = CCCallFunc.action(this, "robotOut");
            CCSequence sequence = CCSequence.actions(delayTime, callFunc);
            this.runAction(sequence);
        }
        //第一个出牌的人是第1个机器
        else if(spoker==3)
        {
         if (pokerList!=null&&pokerList.size()>0)
         {   sendPokerPeople=1;
             CCDelayTime delayTime = CCDelayTime.action(2);
             CCCallFunc callFunc = CCCallFunc.action(this, "cleanPoker");
             CCSequence sequence = CCSequence.actions(delayTime, callFunc);
             this.runAction(sequence);
         }
         else
         {
             spoker=sendPokerPeople;
             CCDelayTime delayTime = CCDelayTime.action(1);
             CCCallFunc callFunc = CCCallFunc.action(this, "cleanPoker");
             CCSequence sequence = CCSequence.actions(delayTime, callFunc);
             this.runAction(sequence);

             //回调出牌的机器
             otherList.clear();
             CCDelayTime delayTime1=CCDelayTime.action(2);
             CCCallFunc callFunc1=CCCallFunc.action(this,"soutPoker");
             CCSequence sequence1=CCSequence.actions(delayTime1,callFunc1);
             this.runAction(sequence);


         }
        }

    }



    /**
     * 出牌
     */
    public  void  robotOut()
    {   //出牌的是第4个人
        if (spoker==5)
        {

           /* if (myGameLayer.getmOutPoker().size()>0) otherList=myGameLayer.getmOutPoker();*/

            sendPokerPeople=1;
            fristReobot.outPoker(otherList);
            List<Poker> showList = fristReobot.getShowList();
            //第一个人的牌有大的
            if (showList!=null&&showList.size()>0)
            {   sendPokerPeople=2;
                otherList=showList;

                secondRobot.outPoker(otherList);
                Log.e("robotOut 2","-----");
                List<Poker> showList1 = secondRobot.getShowList();
                if (showList1!=null&&showList1.size()>0)
                {
                    sendPokerPeople=3;
                    otherList.clear();
                    CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
                    this.runAction(sequence1);

                    Log.e("robotOut3","-----");
                    CCDelayTime delayTime=CCDelayTime.action(3);
                    CCCallFunc callFunc=CCCallFunc.action(this,"soutPoker");
                    CCSequence sequence=CCSequence.actions(delayTime,callFunc);
                    this.runAction(sequence);

                }
                else
                {   CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
                    this.runAction(sequence1);

                    otherList.clear();
                    CCDelayTime delayTime=CCDelayTime.action(3);
                    CCCallFunc callFunc=CCCallFunc.action(this,"soutPoker");
                    CCSequence sequence=CCSequence.actions(delayTime,callFunc);
                    this.runAction(sequence);


                }

            }
            //第一个人没有大的
            else
            {
                secondRobot.outPoker(otherList);
                List<Poker> showList1 = secondRobot.getShowList();
                if (showList1!=null&&showList1.size()>0)
                {
                    sendPokerPeople=3;
                    otherList.clear();
                    CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
                    this.runAction(sequence1);

                    CCDelayTime delayTime=CCDelayTime.action(3);
                    CCCallFunc callFunc=CCCallFunc.action(this,"soutPoker");
                    CCSequence sequence=CCSequence.actions(delayTime,callFunc);
                    this.runAction(sequence);



                }
                else
                {
                    otherList.clear();

                    CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
                    this.runAction(sequence1);

                    CCDelayTime delayTime=CCDelayTime.action(1);
                    CCCallFunc callFunc=CCCallFunc.action(this,"soutPoker");
                    CCSequence sequence=CCSequence.actions(delayTime,callFunc);
                    this.runAction(sequence);


                }
            }

        }


        //出牌的是第三个人
        else if (spoker==4)
        {
            if ( myGameLayer.getmOutPoker().size()>0) otherList=myGameLayer.getmOutPoker();

            sendPokerPeople=1;
            fristReobot.outPoker(otherList);
            List<Poker> showList = fristReobot.getShowList();

            if (showList!=null&&showList.size()>1)
            {
                sendPokerPeople=2;
                otherList.clear();

                CCDelayTime delayTime=CCDelayTime.action(2);
                CCCallFunc callFunc=CCCallFunc.action(this,"soutPoker");
                CCSequence sequence=CCSequence.actions(delayTime,callFunc);
                this.runAction(sequence);

                CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
                this.runAction(sequence1);
            }
            else
            {
                otherList.clear();
                CCDelayTime delayTime=CCDelayTime.action(2);
                CCCallFunc callFunc=CCCallFunc.action(this,"soutPoker");
                CCSequence sequence=CCSequence.actions(delayTime,callFunc);
                this.runAction(sequence);

                CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
                this.runAction(sequence1);
            }

        }



    }

    /***
     * 回调出牌方法
     */
    public void  soutPoker()
    {
        spoker=sendPokerPeople;
       /* Log.e("ss","---soutPoket"+spoker);*/
        robotOutPoker();
    }

    /**
     * 其他人出牌
     */
    public void robotPoker()
    {
         //Log.e("daxiao",myGameLayer.getmOutPoker().size()+"-----");
        sendPokerPeople=1;
        fristReobot.outPoker(myGameLayer.getmOutPoker());

        List<Poker> showList = fristReobot.getShowList();
       // Log.e("showList",showList.size()+"-----"+showList.get(0).getPokerValue());

        //第一个人有大的牌
        if (showList!=null&&showList.size()>0)
        {
            otherList=showList;
            secondRobot.outPoker(otherList);
            sendPokerPeople=2;
            List<Poker> showList1 = secondRobot.getShowList();
           //第二个人 有大的牌
            if (showList1!=null&&showList1.size()>0)
            {
                otherList=showList1;
                sendPokerPeople=3;
                thirdRobotLayer.outPoker(otherList);

                List<Poker> showList2 = thirdRobotLayer.getShowList();
                //第三个人 有大的牌
                if (showList2!=null&&showList2.size()>0)
                {
                    otherList=showList2;
                    sendPokerPeople=4;
                }


            }
            //第二个人没有大的牌
            else
            {
                thirdRobotLayer.outPoker(otherList);

                List<Poker> showList2 = thirdRobotLayer.getShowList();
                 //第三个人有大的牌
                if (showList2!=null&&showList2.size()>0)
                {
                    otherList.clear();
                    sendPokerPeople=4;
                }

            }


        }
        else
        {
            secondRobot.outPoker(myGameLayer.getmOutPoker());

            List<Poker> showList1 = secondRobot.getShowList();
            //第二个人 有大的牌
            if (showList1!=null&&showList1.size()>0)
            {   sendPokerPeople=3;
                otherList=showList1;
                thirdRobotLayer.outPoker(otherList);

                List<Poker> showList2 = thirdRobotLayer.getShowList();
                //第三个人 有大的牌
                if (showList2!=null&&showList2.size()>0)
                {
                    otherList.clear();
                    sendPokerPeople=4;

                }


            }
            //第二个人没有大的牌
            else
            {
                thirdRobotLayer.outPoker(otherList);

                List<Poker> showList2 = thirdRobotLayer.getShowList();
                //第三个人有大的牌
                if (showList2!=null&&showList2.size()>0)
                {
                    otherList.clear();
                    sendPokerPeople=4;
                }
            }

        }

        Log.e("第几个人",sendPokerPeople+"----");

        CCDelayTime delayTime=CCDelayTime.action(2);
        otherList.clear();
        CCCallFunc callFunc=CCCallFunc.action(this,"robotOutPoker");
        CCSequence sequence=CCSequence.actions(delayTime,callFunc);
        this.runAction(sequence);


        CCSequence sequence1=CCSequence.actions(CCDelayTime.action(1), CCCallFunc.action(this,"cleanPoker"));
        this.runAction(sequence1);

        spoker=sendPokerPeople;
    }

    /**
     * 回调机器出牌函数
     */
    public void  rPoker()
    {   //第4个人出牌
        if (spoker==4)
        {
            //
            thirdRobotLayer.outPoker(otherList);

            List<Poker> showList = thirdRobotLayer.getShowList();

            if (showList!=null&&showList.size()>0)
            {   sendPokerPeople=4;
                otherList=showList;
            }
        }

        else if (spoker==3)
        {
            secondRobot.outPoker(otherList);

            List<Poker> showList = secondRobot.getShowList();
            //第2个机器有大的牌
            if (showList!=null&&showList.size()>0)
           {   sendPokerPeople=3;
                otherList=showList;
               //第三个机器出牌
                thirdRobotLayer.outPoker(otherList);

               List<Poker> showList1=thirdRobotLayer.getShowList();

               if (showList1!=null&&showList1.size()>0)
               {
                   otherList=showList1;
                   sendPokerPeople=4;
               }

           }
        }
    }

    /**
     * 自己出牌
     */
    public void  robotOutPoker()
    {
       /* Log.e("spoker4","-----so:"+spoker);*/
        if (spoker==2)
        {
            fristReobot.OutCard();
            otherList=fristReobot.getShowList();
            sendPokerPeople=2;
            spoker=3;

            //回调
            CCDelayTime delayTime=CCDelayTime.action(1);
            CCCallFunc callFunc=CCCallFunc.action(this,"rPoker");
            CCSequence sequence=CCSequence.actions(delayTime,callFunc);
            this.runAction(sequence);
            return;
        }

        else if (spoker==3)
        {
            secondRobot.OutCard();
            //Log.e("spoker3","-----so:"+spoker);
            otherList= secondRobot.getShowList();
            sendPokerPeople=3;
            spoker=4;

            //回调
            CCDelayTime delayTime=CCDelayTime.action(1);
            CCCallFunc callFunc=CCCallFunc.action(this,"rPoker");
            CCSequence sequence=CCSequence.actions(delayTime,callFunc);
            this.runAction(sequence);
            return;
        }

        else if (spoker==4)
        {   //Log.e("spoker4","-----so4:"+spoker);
            spoker=5;
            sendPokerPeople=4;
            thirdRobotLayer.OutCard();
            otherList=thirdRobotLayer.getShowList();
            return;
        }
    }

    /**
     * 清除出的牌
     */
    public void cleanPoker()
    {
        myGameLayer.removeLastPoker();
        fristReobot.removeShowChild();
        secondRobot.removeShowChild();
        thirdRobotLayer.removeShowChild();
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
     * 触摸抬起
     *
     * @param event
     * @return
     */

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        upPoint = this.convertTouchToNodeSpace(event);
        myGameLayer.upPoint=upPoint;
        if (CGPoint.equalToPoint(downPoint, upPoint)) {
            myGameLayer.onclik(upPoint);
        } else {
            //处理滑动时选中的牌
            HashSet set = new HashSet(myGameLayer. moveSprite);
            myGameLayer. moveSprite.clear();
            myGameLayer. moveSprite.addAll(set);
            myGameLayer. MoveSpriteTo(myGameLayer. moveSprite);
            myGameLayer. moveSprite.clear();
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
        myGameLayer.getMoveSprite(point);
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
        myGameLayer.downPoint=downPoint;
        Log.e("按下的点", "---------" + downPoint);
        if (CGRect.containsPoint(label.getBoundingBox(), downPoint)) {
            removePoker();
        } else if (CGRect.containsPoint(playLable.getBoundingBox(), downPoint)) {
           exitPoker();
        }
        else if (CGRect.containsPoint(roblable.getBoundingBox(), downPoint))
        {
            robZhuang();
        }
        else if(CGRect.containsPoint(buriedLable.getBoundingBox(), downPoint))
        {
            buriedPoker();
        }
        return super.ccTouchesBegan(event);
    }

}
