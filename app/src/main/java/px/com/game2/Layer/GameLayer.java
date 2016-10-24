package px.com.game2.Layer;

import android.os.Handler;
import android.os.Message;
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
import java.util.HashSet;
import java.util.List;
import px.com.game2.bean.Poker;
import px.com.game2.utils.CommonUtils;
import px.com.game2.utils.PokerUtils;
import px.com.game2.utils.RobotUtil;
import px.com.game2.utils.ShuffleCards;



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
     * 出的牌
     */
    public List<Poker> otherList=new ArrayList<>();

    public RobotUtil rbUtil;

    /**
     * 亮庄的牌
     */
    public List<Poker> rlist=new ArrayList<>();
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

    /**
     * 设置机器能否抢庄
     */
    public boolean isZhuang;


    /**
     * 出牌的人
     */
    public int oPeople=0;
    /**
     * 那个人的牌大
     */
    public int sizePeople=0;
    /**
     * 睡眠
     */
    CCDelayTime delayTime;



    CCLabel label, playLable,showToast,roblable,buriedLable;


    PokerUtils pUtils;


    public Handler  handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getAllZhuang();
            //如果有人抢庄 判断自己又没有反庄的牌
            getRobZhuang();
            //自己的牌
            if (msg.what==POKER_M)
            {
                Poker poker= (Poker) msg.obj;
                myGameLayer.addMlist(poker);

                //added by xiaohan below
                myGameLayer.initdeal();

            }
            //第一个机器的牌
            else if(msg.what==POKER_F)
            {
                Poker poker= (Poker) msg.obj;
                fristReobot.addPoker(poker);
            }
            //第二个机器的牌
            else if(msg.what==POKER_S)
            {
                Poker poker= (Poker) msg.obj;
                secondRobot.addPoker(poker);
            }
            //第三个机器的牌
            else if(msg.what==POKER_T)
            {
                Poker poker= (Poker) msg.obj;
                thirdRobotLayer.addPoker(poker);

                if (thirdRobotLayer.getList().size()==21)
                {
                    firstOutPoker();
                }
            }

        }
    };



    public GameLayer() {
        init();
        this.setIsTouchEnabled(true);
    }


    private void init() {
        delayTime=CCDelayTime.action(3);
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

    //获取抢庄状态
    public void getAllZhuang()
    {
        if (isZhuang==true)return;
        if (fristReobot.isZhuang())
        {   rlist=fristReobot.getRlist();
            oPeople=1;
            setAllZhuang(true);
            isZhuang=true;
        }
        else if(secondRobot.isZhuang())
        {   rlist=secondRobot.getRlist();
            oPeople=2;
            setAllZhuang(true);
            isZhuang=true;
        }
        else if (thirdRobotLayer.isZhuang())
        {   rlist=secondRobot.getRlist();
            oPeople=3;
            setAllZhuang(true);
            isZhuang=true;
        }
    }

    /**
     * 设置能否抢庄
     */
    public void setAllZhuang(boolean isZhuang)
    {
        myGameLayer.setZhuang(isZhuang);

        fristReobot.setZhuang(isZhuang);

        secondRobot.setZhuang(isZhuang);

        thirdRobotLayer.setZhuang(isZhuang);
    }

    /**
     * 反庄
     */
    public void getRobZhuang()
    {   //庄是自己  直接拿底牌
        if (oPeople==0)
        {

        }
        //庄不是自己
        else
        {
         myGameLayer.setInitColor();
            if (rlist.size()>0)
         myGameLayer.getRobZHuang(rlist);
        }
    }

    /**
     * 牌发完后等待5秒 有没有人反庄 没人庄 庄出牌
     */
    public void  firstOutPoker()
    {
       CCDelayTime delayTime=CCDelayTime.action(5);

       CCSequence sequence=CCSequence.actions(delayTime,CCCallFunc.action(this,"forPoker"));

       this.runAction(sequence);

    }

    /**
     * 执行打牌程序
     */
    public void forPoker()
    {   // 牌全部出完了 跳出
        if (myGameLayer.getMlist().size()==0)
        {
            return;
        }
        cleanPoker();

        firstPoker();

    }

    /**
     * 等待5秒后出牌
     */
    public void firstPoker()
    {
       if (oPeople==0)
       {
           myGameLayer.outCards();
           otherList=myGameLayer.getmOutPoker();
           oPeople=1;
           sizePeople=0;

           //3秒后执行第二次出牌


           CCSequence sequence=CCSequence.actions(delayTime,CCCallFunc.action(this,"secondPoker"));

           this.runAction(sequence);
           return;
       }
       else if (oPeople==1)
       {
        fristReobot.OutCard();
        otherList=fristReobot.getShowList();
           oPeople=2;
           sizePeople=1;
           //3秒后执行第二次出牌


           CCSequence sequence=CCSequence.actions(delayTime,CCCallFunc.action(this,"secondPoker"));

           this.runAction(sequence);
           return;
       }
       else  if (oPeople==2)
       {
           secondRobot.OutCard();
           otherList=secondRobot.getShowList();
           oPeople=3;
           sizePeople=2;

           //3秒后执行第二次出牌


           CCSequence sequence=CCSequence.actions(delayTime,CCCallFunc.action(this,"secondPoker"));

           this.runAction(sequence);
           return;
       }
       else if (oPeople==3)
       {
           thirdRobotLayer.OutCard();
           otherList=thirdRobotLayer.getShowList();
           oPeople=0;
           sizePeople=3;

           //3秒后执行第二次出牌


           CCSequence sequence=CCSequence.actions(delayTime,CCCallFunc.action(this,"secondPoker"));

           this.runAction(sequence);
           return;
       }

    }

    /**
     * 第二次出牌
     */
    public void  secondPoker()
    {
          if (oPeople==0)
          {
              myGameLayer.exitPoker(otherList);
              if (myGameLayer.getmOutPoker()!=null&&myGameLayer.getmOutPoker().size()>0)
              {
                  otherList=myGameLayer.getmOutPoker();
                  sizePeople=0;
              }
              oPeople=1;

              //3秒后执行第四次出牌

              CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"thirdPoker"));

              this.runAction(sequence2);
              return;
          }
          else if (oPeople==1)
          {
              fristReobot.outPoker(otherList);
              if (fristReobot.getShowList()!=null&&fristReobot.getShowList().size()>0)
              {
                  otherList=fristReobot.getShowList();
                  sizePeople=1;
              }
              oPeople=2;

              //3秒后执行第四次出牌

              CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"thirdPoker"));

              this.runAction(sequence2);
              return;
          }
          else if(oPeople==2)
          {
              secondRobot.outPoker(otherList);
              if (secondRobot.getShowList()!=null&&secondRobot.getShowList().size()>0)
              {
                  otherList=secondRobot.getShowList();
                  sizePeople=2;
              }
              oPeople=3;

              //3秒后执行第四次出牌

              CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"thirdPoker"));

              this.runAction(sequence2);
              return;
          }
          else if (oPeople==3)
          {
              thirdRobotLayer.outPoker(otherList);
              if (thirdRobotLayer.getShowList()!=null&&thirdRobotLayer.getShowList().size()>0)
              {
                  otherList=thirdRobotLayer.getShowList();
                  sizePeople=3;
              }
              oPeople=0;
              //3秒后执行第四次出牌

              CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"thirdPoker"));

              this.runAction(sequence2);
              return;
          }
    }

    /**
     * 第三次出牌
     */
    public void thirdPoker()
    {
        if (oPeople==0)
        {
            myGameLayer.exitPoker(otherList);
            if (myGameLayer.getmOutPoker()!=null&&myGameLayer.getmOutPoker().size()>0)
            {
                otherList=myGameLayer.getmOutPoker();
                sizePeople=0;
            }
            oPeople=1;
            //3秒后执行第4次出牌
            CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"fouthPoker"));
            this.runAction(sequence2);
            return;
        }
        else if (oPeople==1)
        {
            fristReobot.outPoker(otherList);
            if (fristReobot.getShowList()!=null&&fristReobot.getShowList().size()>0)
            {
                otherList=fristReobot.getShowList();
                sizePeople=1;
            }
            oPeople=2;
            //3秒后执行第4次出牌
            CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"fouthPoker"));
            this.runAction(sequence2);
            return;
        }
        else if(oPeople==2)
        {
            secondRobot.outPoker(otherList);
            if (secondRobot.getShowList()!=null&&secondRobot.getShowList().size()>0)
            {
                otherList=secondRobot.getShowList();
                sizePeople=2;
            }
            oPeople=3;
            //3秒后执行第4次出牌
            CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"fouthPoker"));
            this.runAction(sequence2);
            return;
        }
        else if (oPeople==3)
        {
            thirdRobotLayer.outPoker(otherList);
            if (thirdRobotLayer.getShowList()!=null&&thirdRobotLayer.getShowList().size()>0)
            {
                otherList=thirdRobotLayer.getShowList();
                sizePeople=3;
            }
            oPeople=0;
            //3秒后执行第4次出牌
            CCSequence sequence2=CCSequence.actions(delayTime,CCCallFunc.action(this,"fouthPoker"));
            this.runAction(sequence2);
            return;
        }
    }


    /**
     * 第四次出牌
     */
  public void fouthPoker()
  {
      if (oPeople==0)
      {
           //停留15秒出牌
          this.runAction(CCDelayTime.action(15));
          //myGameLayer.exitPoker(otherList);
          if (myGameLayer.getmOutPoker()!=null&&myGameLayer.getmOutPoker().size()>0)
          {
              otherList=myGameLayer.getmOutPoker();
              oPeople=0;
          }
         else{oPeople=sizePeople;}

          //第一轮出牌结束 3秒后执行第二轮
          CCSequence sequence3=CCSequence.actions(delayTime,CCCallFunc.action(this,"forPoker"));
          this.runAction(sequence3);
          return;
      }
      else if (oPeople==1)
      {
          fristReobot.outPoker(otherList);
          if (fristReobot.getShowList()!=null&&fristReobot.getShowList().size()>0)
          {
              otherList=fristReobot.getShowList();
              oPeople=1;
          }
          else{oPeople=sizePeople;}
          //第一轮出牌结束 3秒后执行第二轮
          CCSequence sequence3=CCSequence.actions(delayTime,CCCallFunc.action(this,"forPoker"));

          this.runAction(sequence3);
          return;
      }
      else if(oPeople==2)
      {
          secondRobot.outPoker(otherList);
          if (secondRobot.getShowList()!=null&&secondRobot.getShowList().size()>0)
          {
              otherList=secondRobot.getShowList();
              oPeople=2;
          }
          else{oPeople=sizePeople;}
          //第一轮出牌结束 3秒后执行第二轮
          CCSequence sequence3=CCSequence.actions(delayTime,CCCallFunc.action(this,"forPoker"));

          this.runAction(sequence3);
          return;
      }
      else if (oPeople==3)
      {
          thirdRobotLayer.outPoker(otherList);
          if (thirdRobotLayer.getShowList()!=null&&thirdRobotLayer.getShowList().size()>0)
          {
              otherList=thirdRobotLayer.getShowList();
              oPeople=3;
          }
          else{oPeople=sizePeople;}
          //第一轮出牌结束 3秒后执行第二轮
          CCSequence sequence3=CCSequence.actions(delayTime,CCCallFunc.action(this,"forPoker"));
          this.runAction(sequence3);
          return;
      }
  }
    /**
     * 初始化lable 标签
     */
    public void SendPoker() {

        playLable = CCLabel.makeLabel("出牌", "hkbd.ttf", 24);
        playLable.setColor(ccc3(50, 0, 255));
        playLable.setPosition(winSize.width / 2, winSize.height / 2 - 50);
        this.addChild(playLable);

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
        //洗牌
        list = pUtils.shufflecard(list);

        new ShuffleCards(list,handler).start();

        List<Poker[]> dealcard = pUtils.dealcard(4, list, 21);
        remainPoker.clear();
        //底牌
        for (int i = list.length-1; i > list.length - 9; i--) {
            remainPoker.add(list[i]);
        }




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
        myGameLayer.removepoker();
        myGameLayer.mList.clear();
        addPoker();
        //initdeal();
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
        remainPoker= myGameLayer.buriedPoker();
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
        myGameLayer.exitPoker(otherList);
    }

    /**
     * 清除出的牌
     */
    public void cleanPoker()
    {  otherList.clear();
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
        myGameLayer.robPokerClick(downPoint,remainPoker);
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
