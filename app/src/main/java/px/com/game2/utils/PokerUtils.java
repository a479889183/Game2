package px.com.game2.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
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
     * @param number 几副牌
     * @param list   牌
     * @param size 每副牌的大小
     * @return
     */
    public List<Poker[]> dealcard(int number, Poker[] list, int size)//发牌
    {


        List<Poker[]> plist = new ArrayList<>();
        for (int j=0;j<number;j++)
        {   Poker [] pokers=new Poker[size];
            plist.add(pokers);
        }
        int a=0,b=0,c=0,d=0;
        for (int i=0;i<list.length-8;i++)
        {
            if (i/size==0)
            {
                plist.get(i/size)[a]=list[i];
                a++;
            }
            if (i/size==1)
            {
                plist.get(i/size)[b]=list[i];
                b++;
            }
            if (i/size==2)
            {
                plist.get(i/size)[c]=list[i];
                c++;
            }
            if (i/size==3)
            {
                plist.get(i/size)[d]=list[i];
                d++;
            }
        }
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
     * 排序 按主色牌排序
     * @param pokers：牌
     * @param type：主色的类型
     * @return
     */
    public Poker[] sort(Poker[] pokers,int type)
    {   List<Poker> mplist=new ArrayList<>();
         //黑桃
        List<Poker> wlist=new ArrayList<>();
        //红桃
        List<Poker> rlist=new ArrayList<>();
        //梅花
        List<Poker> mlist=new ArrayList<>();
        //方块
        List<Poker> flist=new ArrayList<>();
         Poker[] pk=new Poker[pokers.length];


          for (int i=0;i<pokers.length;i++)
          {
              if (pokers[i].getPokerValue()>9)
              {
                  mplist.add(pokers[i]);
              }
              if (pokers[i].getPokerValue()<10)
              {   //黑
                  if (pokers[i].getPokertype()==Poker.POKERTTYPE_W)
                  {
                      wlist.add(pokers[i]);
                  }
                  //红
                  if (pokers[i].getPokertype()== POKERTTYPE_R)
                  {
                      rlist.add(pokers[i]);
                  }
                  //梅花
                  if (pokers[i].getPokertype()==Poker.POKERTTYPE_M)
                  {
                      mlist.add(pokers[i]);
                  }
                  //方块
                  if (pokers[i].getPokertype()== POKERTTYPE_F)
                  {
                      flist.add(pokers[i]);
                  }
              }
          }
        Poker[] bpoker = mplist.toArray(new Poker[mplist.size()]);
        //先把常主从大到小排序
        for (int i=0;i<mplist.size();i++)
        {
            for (int j=0;j<mplist.size();j++)
            {
                if (bpoker[i].getPokerValue()>bpoker[j].getPokerValue())
                {
                    Poker temp = bpoker[i];
                    bpoker[i] = bpoker[j];
                    bpoker[j] = temp;
                }
                else if(bpoker[i].getPokerValue()==bpoker[j].getPokerValue()&&bpoker[i].getPokertype()==type)
                {
                    Poker temp = bpoker[i];
                    bpoker[i] = bpoker[j];
                    bpoker[j] = temp;
                }
            }
        }
        //把常主加进去
        for (int i=0;i<bpoker.length;i++)
        {
            pk[i]=bpoker[i];
        }

        Poker[] wpokers = sortingBig(wlist.toArray(new Poker[wlist.size()]));

        Poker[] rpokers = sortingBig(rlist.toArray(new Poker[rlist.size()]));

        Poker[] mpokers = sortingBig(mlist.toArray(new Poker[mlist.size()]));

        Poker[] fpokers= sortingBig(flist.toArray(new Poker[flist.size()]));
        //主色为黑色
        if (type==Poker.POKERTTYPE_W)
        {
            //把主色的追加到主牌后面
            for (int i=0;i<wpokers.length;i++)
            {
                pk[mplist.size()+i]=wpokers[i];
            }

            // 把红色牌追加到牌后面

            for (int i=0;i<rpokers.length;i++)
            {
                pk[mplist.size()+wpokers.length+i]=rpokers[i];
            }

            //把梅花牌加到后面
            for (int i=0;i<mpokers.length;i++)
            {
                pk[mplist.size()+wpokers.length+rpokers.length+i]=mpokers[i];
            }

            //把方块加到后面

            for (int i=0;i<fpokers.length;i++)
            {
                pk[mplist.size()+wpokers.length+rpokers.length+mpokers.length+i]=fpokers[i];
            }

        }
        //主色为红色
        else if(type== POKERTTYPE_R)
        {
            // 把红色牌追加到牌后面

            for (int i=0;i<rpokers.length;i++)
            {
                pk[mplist.size()+i]=rpokers[i];
            }

            //把黑色加进去

            for (int i=0;i<wpokers.length;i++)
            {
                pk[mplist.size()+rpokers.length+i]=wpokers[i];
            }

            //把方块加进去
            for (int i=0;i<fpokers.length;i++)
            {
                pk[mplist.size()+wpokers.length+rpokers.length+i]=fpokers[i];
            }
            //把梅花牌加到后面
            for (int i=0;i<mpokers.length;i++)
            {
                pk[mplist.size()+wpokers.length+rpokers.length+fpokers.length+i]=mpokers[i];
            }

        }
        //主色为梅花
        else if (type==Poker.POKERTTYPE_M)
        {
            //把梅花牌加到后面
            for (int i=0;i<mpokers.length;i++)
            {
                pk[mplist.size()+i]=mpokers[i];
            }

            // 把红色牌追加到牌后面

            for (int i=0;i<rpokers.length;i++)
            {
                pk[mplist.size()+mpokers.length+i]=rpokers[i];
            }
            //把黑色加进去
            for (int i=0;i<wpokers.length;i++)
            {
                pk[mplist.size()+mpokers.length+rpokers.length+i]=wpokers[i];
            }
            //把方块加进去
            for (int i=0;i<fpokers.length;i++)
            {
                pk[mplist.size()+mpokers.length+wpokers.length+rpokers.length+i]=fpokers[i];
            }
        }
        else if (type== POKERTTYPE_F)
        {   //把方块加进去
            for (int i=0;i<fpokers.length;i++)
            {
                pk[mplist.size()+i]=fpokers[i];
            }

            //把黑色加进去
            for (int i=0;i<wpokers.length;i++)
            {
                pk[mplist.size()+fpokers.length+i]=wpokers[i];
            }
            // 把红色牌追加到牌后面
            for (int i=0;i<rpokers.length;i++)
            {
                pk[mplist.size()+fpokers.length+wpokers.length+i]=rpokers[i];
            }
            //把梅花加进去
            for (int i=0;i<mpokers.length;i++)
            {
                pk[mplist.size()+fpokers.length+wpokers.length+rpokers.length+i]=mpokers[i];
            }
        }

        return  pk;
    }


    /**
     * 判断大小 如果自己的牌大 返回 true
     * @param mPoker  自己出的牌
     * @param uPoker  桌面上的牌
     */
    public boolean isSize(List<Poker> mPoker,List<Poker> uPoker,int mtype)
    {
        if (mPoker.size()!=uPoker.size())return false;
        if (mPoker.size()==1) {
            int type = uPoker.get(0).getPokertype();
            int value = uPoker.get(0).getPokerValue();

            int type1 = mPoker.get(0).getPokertype();
            int value1 = mPoker.get(0).getPokerValue();

            //先判断出的牌是不是主色牌
            //不是主色牌
            if (type != mtype) {   //值不是2以上的值
                if (value < 10) {   //先判断两个牌是不是同色
                    if (type1 == type) {
                        if (value1 > value) return true;
                    }
                    //如果自己的牌是主色的 或者是2/10大小王
                    if (type1 == mtype || value1 > 9) {
                        return true;
                    }
                }
                //值是2以上的
                if (value > 9) {   //主色牌
                    if (value1 == value && type1 == mtype) return true;
                    if (value1 > value) return true;
                }
            }
            //出的是主色牌
            if (type == mtype) {   //值是2以下的主牌
                if (value < 10) {   //主色牌并且要大于value
                    if (type1 == mtype) {
                        if (value1 > value) return true;
                    }
                    if (value1 > 9) {
                        return true;
                    }
                }
                //值是2以上的
                if (value > 9) {
                    if (value1 > value) return true;
                }
            }
        }
        //对牌
        if (mPoker.size()==2)
        {
            int type = uPoker.get(0).getPokertype();
            int value = uPoker.get(0).getPokerValue();

            int type1 = mPoker.get(0).getPokertype();
            int value1 = mPoker.get(0).getPokerValue();

            //先判断出的牌是不是主色牌
            //不是主色牌
            if (type != mtype) {   //值不是2以上的值
                if (value < 10) {   //先判断两个牌是不是同色
                    if (type1 == type) {
                        if (value1 > value) return true;
                    }
                    //如果自己的牌是主色的 或者是2/10大小王
                    if (type1 == mtype || value1 > 9) {
                        return true;
                    }
                }
                //值是2以上的
                if (value > 9) {   //主色牌
                    if (value1 == value && type1 == mtype) return true;
                    if (value1 > value) return true;
                }
            }
            //出的是主色牌
            if (type == mtype) {   //值是2以下的主牌
                if (value < 10) {   //主色牌并且要大于value
                    if (type1 == mtype) {
                        if (value1 > value) return true;
                    }
                    if (value1 > 9) {
                        return true;
                    }
                }
                //值是2以上的
                if (value > 9) {
                    if (value1 > value) return true;
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
        if (list.size()%2==0&&list.size()>2) {

            return isOut(list,list.size());
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
        HashSet<Integer> set = new HashSet();
        HashSet set1=new HashSet();

        for (Poker poker : list) {
            set.add(poker.getPokerValue());
            set1.add(poker.getPokertype());

        }
        if (set.size() == size/2) {
            if (set1.size()>1)return false;
            //Poker [] pokers=new Poker[set.size()];
            Integer pokers[]=new Integer[set.size()];
            set.toArray(pokers);
            Integer[] pokers1 = sortBig(pokers);
            boolean f=false;
            for (int i=0;i<pokers1.length-1;i++)
            {
                if (pokers1[i]>pokers1[i+1])
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
     * 排序
     * @param temp
     * @return
     */
    public Integer[] sortBig(Integer [] temp)
    {

        for (int i=0;i<temp.length;i++)
        {

            for(int j=0;j<temp.length;j++)
            {
                if (temp[i]>temp[j])
                {  int  a=temp[j];
                    temp[j]=temp[i];
                    temp[i]=a;
                }
            }

        }

        return temp;
    }

    /**
     * 亮牌规则
     * @return
     */
    public boolean robZhuang(List<Poker> list)
    {
         if (list.size()==2)
         {
          if (list.get(0).getPokerValue()==10&&(list.get(0).getPokertype()== POKERTTYPE_R||list.get(0).getPokertype()== POKERTTYPE_F))
          {
              if (list.get(1).getPokerValue()==11)
              {
                  return  true;
              }
          }
          else if (list.get(1).getPokerValue()==10&&(list.get(1).getPokertype()== POKERTTYPE_R||list.get(1).getPokertype()== POKERTTYPE_F))
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
                            if (pokers1[2].getPokerValue()==10&&(pokers1[2].getPokertype()==Poker.POKERTTYPE_R||pokers1[2].getPokertype()==Poker.POKERTTYPE_F))
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
