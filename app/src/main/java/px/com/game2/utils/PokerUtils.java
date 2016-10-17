package px.com.game2.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import px.com.game2.bean.Poker;

import static px.com.game2.bean.Poker.POKERTTYPE_F;
import static px.com.game2.bean.Poker.POKERTTYPE_R;


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
        int rad[] = CommonUtils.randomArray(0, 91, 92);
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


    /**
     * 判断大小 如果自己的牌大 返回 true
     * @param mPoker  自己出的牌
     * @param uPoker  桌面上的牌
     */
    public boolean isSize(List<Poker> mPoker,List<Poker> uPoker,int mtype)
    {
        if (mPoker.size()==1)
        {
            int type=uPoker.get(0).getPokertype();
            int value=uPoker.get(0).getPokerValue();
            int type1=mPoker.get(0).getPokertype();
            int value1=mPoker.get(0).getPokerValue();

            if (type!=mtype&&value<10)
            {   //同色
                if (type==type1)
                {
                    if (value1>value)
                    {
                        return  true;
                    }
                }
                //主色
                if (type1==mtype)
                {
                    return true;
                }
                // 大小王
                if (value1>9){return  true;}
            }
            //出的牌是2 10 或者大小王 并且不是主色
            else if (type!=mtype&&value>9)
            {   //出的牌是主色
                if (type1==mtype)
                {
                    if (value1>=value)return  true;
                }
                else if (type1!=mtype)
                {
                    if (value1>value)return  true;
                }
            }
            //出的是主色牌
            else  if (type==mtype&&value<10)
            {
                if (value1>10)
                {
                    return true;
                }
                else if (type1==mtype)
                {
                    if (value1>value)return true;
                }
            }
            //出的是2 10 大小 王
            else if (type==mtype&&value>9)
            {
                if (type1==mtype)
                {
                    if (value1>value)return  true;
                }
                if (value1>value)return  true;
                //出的牌不是大小王
                else if (value<12)
                {
                    if (value1>11)
                      return  true;
                }
                //出的是大小王
                else  if (value>11)
                {
                    if (value1>value)return true;
                }
            }
        }

        return false;
    }


    /**
     * 出牌规则
     * @param list
     * @return
     */
    public boolean pokerRulesB(List<Poker> list) {
        if (list.size() == 1) return true;
        if (list.size() == 2) {
            if (list.get(0).getPokerValue() == list.get(1).getPokerValue()) {
                if (list.get(0).getPokertype()==list.get(1).getPokertype())
                {
                    return true;
                }
                else
                {
                    return  false;
                }
            } else if (list.get(0).getPokertype() > 4 && list.get(1).getPokertype() > 4) {
                if (list.get(0).getPokertype()==list.get(1).getPokertype())
                {
                    return true;
                }
                else
                {
                    return  false;
                }
            } else {
                return false;
            }
        }
        if (list.size() == 3) return false;
        if (list.size() == 4) {

            return isOut(list,4);
           /* HashSet set = new HashSet();
            HashSet set1=new HashSet();
            for (Poker poker : list) {
                set.add(poker.getPokerValue());
                set1.add(poker.getPokertype());
                Log.e("----",poker.getPokertype()+"-----------------------------------");
            }
            if (set.size() == 2) {
                if (set1.size()>1)return false;
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
            }*/

        }
        if (list.size()==6)
        {
           return isOut(list,6);
        }
        if (list.size()==8)
        {
            return isOut(list,8);
        }
        if (list.size()==10)
        {
            return isOut(list,10);
        }
        if (list.size()==12)
        {
            return isOut(list,12);
        }
        if (list.size()==14)
        {
            return isOut(list,14);
        }
        if (list.size()==16)
        {
            return isOut(list,16);
        }
        if (list.size()==18)
        {
            return isOut(list,18);
        }
        if (list.size()==20)
        {
            return isOut(list,20);
        }
        return false;
    }


    /**
     * 判断是不是 拖拉机
     * @param list
     * @param size
     * @return
     */
    public boolean isOut(List<Poker> list,int size)
    {
        HashSet set = new HashSet();
        HashSet set1=new HashSet();
        for (Poker poker : list) {
            set.add(poker.getPokerValue());
            set1.add(poker.getPokertype());
            Log.e("----",poker.getPokertype()+"-----------------------------------");
        }
        if (set.size() == size/2) {
            if (set1.size()>1)return false;
            Poker [] pokers=new Poker[set1.size()];
            set.toArray(pokers);
            Poker[] pokers1 = sortingBig(pokers);
            boolean f=false;
            for (int i=0;i<pokers1.length-1;i++)
            {
                if (pokers1[i].getPokerValue()>pokers1[i+1].getPokerValue())
                {
                    f=true;
                }
                else {f=false;}
            }

           return f;
        } else {
            return false;
        }
    }

    /**
     * 亮牌规则
     * @return
     */
    public boolean robZhuang(List<Poker> list)
    {
         if (list.size()==2)
         {
          if (list.get(0).getPokerValue()==10&&(list.get(0).getPokertype()==Poker.POKERTTYPE_R||list.get(0).getPokertype()==Poker.POKERTTYPE_F))
          {
              if (list.get(1).getPokerValue()==11)
              {
                  return  true;
              }
          }
          else if (list.get(1).getPokerValue()==10&&(list.get(1).getPokertype()==Poker.POKERTTYPE_R||list.get(1).getPokertype()==Poker.POKERTTYPE_F))
          {
              if (list.get(0).getPokerValue()==11)
              {
                  return  true;
              }
          }
         }
        if (list.size()==3)
        {
            HashSet set=new HashSet();

            for (Poker poker : list) {
                set.add(poker.getPokerValue());
                Log.e("----",poker.getPokertype()+"-----------------------------------");
            }

            if (set.size()==1)
            {
                if(list.get(0).getPokerValue()==10)
                {
                    boolean isflag=false;
                    for (int i=0;i<list.size();i++)
                    {
                        if (list.get(i).getPokertype()==POKERTTYPE_R||list.get(i).getPokertype()==POKERTTYPE_F)
                        {
                           isflag=true;
                        }
                        else {isflag=false;}
                    }
                    return  isflag;
                }

            }
            if (set.size()==2)
            {

                Poker[] pokers = list.toArray(new Poker[list.size()]);
                Poker[] pokers1 = sortingBig(pokers);
                if (pokers1[0].getPokerValue()==11)
                {
                    if (pokers1[1].getPokerValue()==11)
                    {
                        if (pokers1[0].getPokertype()==pokers1[1].getPokertype())
                        {
                            if (pokers1[2].getPokerValue()==10&&(pokers1[2].getPokertype()==POKERTTYPE_R||pokers1[2].getPokertype()==POKERTTYPE_F))
                            {
                                return  true;
                            }
                        }
                    }
                }
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
            HashSet set;
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
