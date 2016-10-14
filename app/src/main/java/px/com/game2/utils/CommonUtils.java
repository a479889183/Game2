package px.com.game2.utils;

import android.util.Log;

import org.cocos2d.nodes.CCSprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import px.com.game2.bean.Poker;

import static android.R.id.list;


/**
 * Created by admin on 2016/10/12.
 */

public class CommonUtils {
    /**
     * 获取扑克牌精灵
     *  54张
     * @return
     */
    public static Poker[] getCard() {
        Poker[] list = new Poker[54];

        String foramt = "brand/p%d.png";
        for (int i = 1; i < 55; i++) {
            Poker pk = new Poker();
            if (i == 53) {
                pk.setPokertype(Poker.POKERTTYPE_S);
            } else if (i == 54) {
                pk.setPokertype(Poker.POKERTTYPE_B);
            } else {
                if ((i / 13) >= 4) {
                    pk.setPokertype(4);
                } else {
                    pk.setPokertype((i / 13) + 1);
                }
            }
            CCSprite sprite = CCSprite.sprite(String.format(foramt, i));
            pk.setPolerSprite(sprite);
            if (i % 13 == 1) {

                if (pk.getPokertype() == Poker.POKERTTYPE_S) {
                    pk.setPokerValue(13 + 3);
                }
                else {
                    pk.setPokerValue(13 + 1);
                }
            }
            else if (i % 13 == 0) {
                pk.setPokerValue(13);

            }
            else if (i % 13 == 2) {
                if (pk.getPokertype() == Poker.POKERTTYPE_B) {
                    pk.setPokerValue(13 + 4);
                }
                else
                {
                    pk.setPokerValue(13 + 2);
                }
            }  else {
                pk.setPokerValue(i % 13);
            }
            Log.e("----", String.format(foramt, i) + "=====" + "i%13====" + i % 13 + "值===" + pk.getPokerValue());
            pk.setNum(1);
            list[i - 1] = pk;
        }
        return list;
    }


    public  static  Poker [] getCardC(int j)
    {
        Poker poker[]=new Poker[46];
        String foramt = "poker/p%d.png";
        for (int i=1;i<47;i++)
        {
            Poker pk=new Poker();
            if (i == 45) {
                pk.setPokertype(Poker.POKERTTYPE_S);
                pk.setPokerValue(12);
            } else if (i ==46 ) {
                pk.setPokertype(Poker.POKERTTYPE_B);
                pk.setPokerValue(13);
            }
            else if (i/11>4)
            {

            }
            else
            {
                pk.setPokertype(i/11);
                pk.setPokerValue(i%11);
                if (i%11==0)
                {
                    pk.setPokerValue(11);
                }
            }
            CCSprite sprite = CCSprite.sprite(String.format(foramt, i));
            pk.setPolerSprite(sprite);
            pk.setNum(j);
            Log.e("----", String.format(foramt, i) + "=====" + "i%11====" + i % 11 + "--值===" + pk.getPokerValue()+"--type--"+pk.getPokertype()+"--"+pk.getNum());
            poker[i - 1] = pk;
        }
        return poker;
    }

    /**
     * 巴斗牌精灵92张牌
     * @return
     */
    public static  Poker[] getCardB()
    {
        Poker pokers[]= getCardC(1);
        Poker poke[]= getCardC(2);
        List<Poker> pokerList=new ArrayList<>();
        Collections.addAll(pokerList,pokers);

        Collections.addAll(pokerList,poke);
       /* int j=0;
        for (int i=0;i<pokerList.size();i++)
        {

            if ((i+j)/54==0)
           {
               pokerList.get(i).setNum(1);
           }
             if((i+j)/54==1)
           {
               pokerList.get(i).setNum(2);
           }
            if (pokerList.get(i).getPokerValue()==3)
            {
                pokerList.remove(i);
                j++;
            }
            if (pokerList.get(i).getPokerValue()==4)
            {
                pokerList.remove(i);
                j++;
            }
             if (pokerList.get(i).getPokerValue()==17)
            {
                pokerList.get(i).setPokerValue(20);
            }
             if ((pokerList.get(i).getPokerValue()==16))
            {
                pokerList.get(i).setPokerValue(19);
            }
             if ((pokerList.get(i).getPokerValue()==10))
            {
                pokerList.get(i).setPokerValue(18);
            }

        }*/
        Poker[] pokers1 = pokerList.toArray(new Poker[pokerList.size()]);
        return pokers1;
    }

    /**
     * 生成不重复的随机数
     *
     * @param min
     * @param max
     * @param n
     * @return
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
}
