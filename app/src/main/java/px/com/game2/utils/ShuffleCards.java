package px.com.game2.utils;

import android.os.Handler;
import android.os.Message;
import px.com.game2.bean.Poker;

/**
 * Created by admin on 2016/10/21.
 * 发牌
 */

public class ShuffleCards extends Thread implements Constant{

    public Poker [] list;

    public Handler handler;

    public ShuffleCards(Poker[] list,Handler handler)
    {
        this.list=list;
        this.handler=handler;
    }

    @Override
    public void run() {
         int size=4;

        for (int i=0;i<list.length-8;i++)
        {
            try {

             if (i%size==0)
            {
                Message message=new Message();
                message.what=POKER_M;
                message.obj=list[i];
                handler.sendMessage(message);
            }
            else if (i%size==1)
            {
                Message message=new Message();
                message.what=POKER_F;
                message.obj=list[i];
                handler.sendMessage(message);
            }
           else if (i%size==2)
            {
                Message message=new Message();
                message.what=POKER_S;
                message.obj=list[i];
                handler.sendMessage(message);
            }
           else  if (i%size==3)
            {
                Message message=new Message();
                message.what=POKER_T;
                message.obj=list[i];
                handler.sendMessage(message);
            }

                sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
