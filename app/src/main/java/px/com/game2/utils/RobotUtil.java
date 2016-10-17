package px.com.game2.utils;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;


/**
 * Created by admin on 2016/10/17.
 */

public class RobotUtil {


    /**
     *  出牌 mPoker 自己的牌<br>
     *      uPoker 桌面上的牌
     *      mtype  什么色为主
     * @param mPoker
     * @param uPoker
     */
    public static List<Poker> outPoker(List<Poker> mPoker,List<Poker> uPoker,int mtype)
    {
        List<Poker> rPoker=new ArrayList<>();
        //单牌
       if (uPoker.size()==1)
       {
           int type=uPoker.get(0).getPokertype();
           int value=uPoker.get(0).getPokerValue();
           //先找同色的
           for (int i=mPoker.size()-1;i>0;i--)
           {
               if (mPoker.get(i).getPokertype()==type)
               {
                   if (mPoker.get(i).getPokerValue()>value)
                   {
                       rPoker.add(mPoker.get(i));
                       return  rPoker;
                   }
               }
           }
           //找主色的
           for (int i=mPoker.size()-1;i>0;i--)
           {
               if (mPoker.get(i).getPokertype()==mtype)
               {
                   if (value<10)
                   {
                       rPoker.add(mPoker.get(i));
                       return  rPoker;
                   }

               }
           }

           //找2和10
           for (int i=mPoker.size()-1;i>0;i--)
           {   if (value>9)
               {
                   if (mPoker.get(i).getPokerValue()>value)
                   {
                       if (type==mtype)
                       {
                          if (mPoker.get(i).getPokerValue()==mtype)
                          {
                              rPoker.add(mPoker.get(i));
                              return  rPoker;
                          }
                       }
                       if (type!=mtype)
                       {
                           rPoker.add(mPoker.get(i));
                           return  rPoker;
                       }

                   }

                   if (mPoker.get(i).getPokerValue()<value)
                   {
                       if (type!=mtype)
                       {
                           if (mPoker.get(i).getPokertype()==mtype)
                           {
                               rPoker.add(mPoker.get(i));
                               return  rPoker;
                           }

                       }
                   }
               }

           }

       }
        return  rPoker;
    }
}
