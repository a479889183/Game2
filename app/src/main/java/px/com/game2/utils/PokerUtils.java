package px.com.game2.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import px.com.game2.bean.Poker;


/**
 * Created by admin on 2016/10/12.
 */

public class PokerUtils {

    /**
     * 洗牌
     *
     * @param list
     * @return
     */
    public Poker[] shufflecard(Poker[] list)//洗牌
    {
        int rad[] = CommonUtils.randomArray(0, 53, 54);
        for (int i = 0; i < rad.length; i++) {
            int j = rad[i];
            Poker temp = list[i];
            list[i] = list[j];
            list[j] = temp;

        }
        return list;
    }

    /**
     * 发牌
     */
    public List<Poker[]> dealcard(int number, Poker[] list, int size)//发牌
    {
        List<Poker[]> plist = new ArrayList<>();
        for (int j = number; j > 0; j--) {
            Poker[] pker = new Poker[size];

            for (int i = 0; i < size; i++) {
                pker[i] = list[i * j];
            }
            plist.add(pker);
        }
       /*  Poker [] pker=new Poker[size];
        Poker [] pker1=new Poker[size];
        Poker [] pker2=new Poker[size];
        int temp=0,temp1=0,temp2=0;
        for(int i=0;i<size*number;i++)
        {
           if (i%number==0)
           {
               pker[temp]=list[i];
               temp++;
           }
            else  if (i%number==1)
           {
               pker[temp1]=list[i];
               temp1++;
           }
           else  if (i%number==2)
           {
               pker[temp2]=list[i];
               temp2++;
           }

        }
        plist.add(pker);
        plist.add(pker1);
        plist.add(pker2);*/
        return plist;
    }

    /**
     * 排序从大到小
     *
     * @param pokers
     * @return
     */
    public Poker[] sortingBig(Poker[] pokers) {
        for (int i = 0; i < pokers.length; i++) {
            for (int j = 0; j < pokers.length; j++) {
                if (pokers[i].getPokerValue() > pokers[j].getPokerValue()) {
                    Poker temp = pokers[i];
                    pokers[i] = pokers[j];
                    pokers[j] = temp;
                }
            }
        }
        return pokers;
    }

    public boolean pokerRulesB(List<Poker> list) {
        if (list.size() == 1) return true;
        if (list.size() == 2) {
            if (list.get(0).getPokerValue() == list.get(1).getPokerValue()) {
                return true;
            } else if (list.get(0).getPokertype() > 4 && list.get(1).getPokertype() > 4) {
                return true;
            } else {
                return false;
            }
        }
        if (list.size() == 3) return false;
        if (list.size() == 4) {
            HashSet set = new HashSet();
            for (Poker poker : list) {
                set.add(poker.getPokerValue());
            }
            if (set.size() == 2) {
                List<Integer> list1 = new ArrayList<>();
                list1.addAll(set);
                if ((list1.get(0) == list1.get(1) + 1) || (list1.get(0) == list1.get(1) - 1)) {
                    return true;
                }
                else {
                    return false;
                }
            } else {
                return false;
            }

        }
        return false;
    }


    /**
     * 斗地主出牌规则
     *
     * @param list
     * @return
     */
    public boolean pokerRules(List<Poker> list) {
        Log.e("-------", "====================daxiao===" + list.size());
        boolean isflag = false;
        if (list.size() == 2) {
            if (list.get(0).getPokerValue() != list.get(1).getPokerValue()) {
                isflag = false;
            } else {
                isflag = true;
            }
        } else if (list.size() == 3) {
            if (list.get(0).getPokerValue() == list.get(1).getPokerValue() && list.get(0).getPokerValue() == list.get(2).getPokerValue()) {
                isflag = true;
            } else {
                isflag = false;
            }
        } else if (list.size() == 4) {
            HashSet hashSet = new HashSet();
            for (Poker poker : list) {
                hashSet.add(poker.getPokerValue());
            }
            Log.e("hasHSet", "====================daxiao===" + hashSet.size());
            if (hashSet.size() == 2) {
                isflag = true;
            } else {
                isflag = false;
            }
        }
        return isflag;
    }
}
