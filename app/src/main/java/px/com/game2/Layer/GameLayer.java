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
import px.com.game2.utils.CommonUtils;
import px.com.game2.utils.PokerUtils;
import px.com.game2.utils.RobotUtil;



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



    CCLabel label, playLable,showToast,roblable,buriedLable;
    /**
     * 亮牌文字
     */
    CCLabel wlabel,rlabel,mlabel,flabel,zlabel;


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
    /**
     * 存红色的2
     */
    List<Poker> robList=new ArrayList<>();
    /**
     * 存10
     */
    List<Poker> robMlist=new ArrayList<>();




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
        SendPoker();
        outPoker();
    }


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
        initLable();

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
        for (int i = 0; i < mList.size(); i++) {
            this.removeChild(mList.get(i).getPolerSprite(), true);
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
        CCDelayTime ccdelay = CCDelayTime.action(speed);

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

        CCSequence sequence = CCSequence.actions(ccMoveBy, ccdelay, CCCallFunc.action(this, "lodeDeal"));
        sprite.runAction(sequence);
        i++;
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
     * 埋牌
     */
    public void buriedPoker()
    {
        if (moveSpriteUp!=null&&moveSpriteUp.size()==8)
        {    HashSet<Poker> pokset = new HashSet<>(moveSpriteUp);
            moveSpriteUp.clear();
            moveSpriteUp.addAll(pokset);
            remainPoker.clear();
            //底牌=埋的牌
            remainPoker=moveSpriteUp;

            //先移除桌面上的牌

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
            Collections.addAll(mList, pUtils.sortingBig(pokers));

            //重新加到界面上
            for (int i = 0; i < mList.size(); i++) {
                CCSprite sp = mList.get(i).getPolerSprite();

                sp.setPosition(winSize.width + 100, winSize.height / 2);
                sp.setAnchorPoint(0, 0);
                this.addChild(sp);
                // Log.e("listsize", "数值==" + mList.get(i).getPokerValue() + "--type=" + list[i].getPokertype() + "listsize==" + mList.size() + "--dijifu==" + mList.get(i).getNum());
            }

            initdeal();
        }
        else
        {
            showToast("埋的牌只能是8张");
        }
    }


    /**
     * 抢庄
     */
    public void robZhuang()
    {
        if (moveSpriteUp!=null&&moveSpriteUp.size()>0)
        {
            HashSet<Poker> pokset = new HashSet<>(moveSpriteUp);
            moveSpriteUp.clear();
            moveSpriteUp.addAll(pokset);

            if(pUtils.robZhuang(moveSpriteUp))
            {
                showToast("亮牌成功");
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
                showToast("亮牌不符合规则");
                for (int i = 0; i < moveSpriteUp.size(); i++) {
                    CCMoveBy ccMoveTo = CCMoveBy.action(0.25f, ccp(0, -30));
                    moveSpriteUp.get(i).getPolerSprite().runAction(ccMoveTo);
                }
                moveSpriteUp.clear();
            }
        }
    }


    /**
     * 出牌
     */
    public void exitPoker() {
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
           /* pokers = pUtils.sortingBig(pokers);*/
            //重新排序
            pokers=pUtils.sort(pokers,Poker.POKERTTYPE_W);

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

            //第一个人出牌
            robotPoker();

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

    public void robotPoker()
    {
        fristReobot.outPoker(moveSpriteUp);

        List<Poker> showList = fristReobot.getShowList();
        //第一个人有大的牌
        if (showList!=null&&showList.size()>0)
        {
            otherList=showList;

            secondRobot.outPoker(otherList);

            List<Poker> showList1 = secondRobot.getShowList();
           //第二个人 有大的牌
            if (showList1!=null&&showList1.size()>0)
            {
                otherList=showList1;
                thirdRobotLayer.outPoker(otherList);

                List<Poker> showList2 = thirdRobotLayer.getShowList();
                //第三个人 有大的牌
                if (showList2!=null&&showList2.size()>0)
                {
                    otherList=showList2;
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
                    otherList=showList2;
                }
            }


        }
        else
        {
            secondRobot.outPoker(moveSpriteUp);

            List<Poker> showList1 = secondRobot.getShowList();
            //第二个人 有大的牌
            if (showList1!=null&&showList1.size()>0)
            {
                otherList=showList1;
                thirdRobotLayer.outPoker(otherList);

                List<Poker> showList2 = thirdRobotLayer.getShowList();
                //第三个人 有大的牌
                if (showList2!=null&&showList2.size()>0)
                {
                    otherList=showList2;
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
                    otherList=showList2;
                }
            }

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

    public void unLock() {
        isLock = false;
    }

    boolean isLock;
}
