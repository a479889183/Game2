package px.com.game2.utils;

import java.util.ArrayList;
import java.util.List;

import px.com.game2.bean.Poker;



/**
 * Created by admin on 2016/10/12.
 */

public class PokerUtils {

    /**
     * 洗牌
     * @param list
     * @return
     */
    public Poker[] shufflecard(Poker[] list )//洗牌
    {    int  rad[]=CommonUtils.randomArray(0,53,54);
        for(int i=0;i<rad.length;i++)
        {
            int j = rad[i];
            Poker temp=list[i];
            list[i]=list[j];
            list[j]=temp;

        }
        return list;
    }

    /**
     * 发牌
     */
    public List<Poker []>  dealcard(int number, Poker [] list, int size)//发牌
    {    List<Poker []> plist=new ArrayList<>();
         Poker [] pker=new Poker[size];
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
        plist.add(pker2);
        return plist;
    }

    public Poker[] sortingBig(Poker[] pokers)
    {
        for (int i=0;i<pokers.length;i++)
        {
           for (int j=0;j<pokers.length;j++)
           {
               if (pokers[i].getPokerValue()>pokers[j].getPokerValue())
               {
                   Poker temp=pokers[i];
                   pokers[i]=pokers[j];
                   pokers[j]=temp;
               }
           }
        }
        return  pokers;
    }
}
