package px.com.game2.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import px.com.game2.bean.Poker;


/**
 * Created by admin on 2016/10/17.
 */

public class RobotUtil {


    /**
     * 出牌 mPoker 自己的牌<br>
     * uPoker 桌面上的牌
     * mtype  什么色为主
     *
     * @param mPoker
     * @param uPoker
     */
    public static List<Poker> outPoker(List<Poker> mPoker, List<Poker> uPoker, int mtype) {
        List<Poker> rPoker = new ArrayList<>();
        //单牌
        if (uPoker.size() == 1) {
            int type = uPoker.get(0).getPokertype();
            int value = uPoker.get(0).getPokerValue();
            //自己
            //出的牌不是主色牌
            if (type != mtype) {
                //先找同色的副牌
                //并且出的牌是2以下的
                if (value < 10) {   //先找同色牌出
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //同色牌
                        if (mPoker.get(i).getPokertype() == type) {
                            if (mPoker.get(i).getPokerValue() > value) {
                                rPoker.add(mPoker.get(i));
                                return rPoker;
                            }
                        }
                    }
                    //找主色牌
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //同色牌
                        if (mPoker.get(i).getPokertype() == mtype) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }
                    }

                    // 找大小王

                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //同色牌
                        if (mPoker.get(i).getPokerValue() > 9) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }
                    }


                }
                //出的牌是2以上的
                if (value > 9) {
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //值大的
                        if (mPoker.get(i).getPokerValue() > value) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }
                        //值相同 主色的
                        if (mPoker.get(i).getPokerValue() == value && mPoker.get(i).getPokertype() == mtype) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }

                    }
                }
            }
            //出的牌是主色牌
            if (type == mtype) {
                //2以下的
                if (value < 10) {
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //值大的
                        if (mPoker.get(i).getPokerValue() > value && mPoker.get(i).getPokertype() == mtype) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }

                        if (mPoker.get(i).getPokerValue() > 9) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }

                    }
                }

                if (value > 9) {
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //值大的
                        if (mPoker.get(i).getPokerValue() > value) {
                            rPoker.add(mPoker.get(i));
                            return rPoker;
                        }
                    }
                }

            }
        }


        //对牌
        if (uPoker.size()==2)
        {
            int type = uPoker.get(0).getPokertype();
            int value = uPoker.get(0).getPokerValue();
            //放第一副牌
            List<Poker> pk=new ArrayList<>();
            //放第二副牌


            //先找出所有的对牌

            //出的牌不是主色牌
            if (type != mtype) {
                //先找同色的副牌
                //并且出的牌是2以下的
                if (value < 10) {   //先找同色牌出
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //同色牌
                        if (mPoker.get(i).getPokertype() == type) {
                            if (mPoker.get(i).getPokerValue() > value) {
                                pk.add(mPoker.get(i));

                            }
                        }
                    }
                    //找主色牌
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //同色牌
                        if (mPoker.get(i).getPokertype() == mtype) {
                            pk.add(mPoker.get(i));

                        }
                    }

                    // 找大小王

                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //同色牌
                        if (mPoker.get(i).getPokerValue() > 9) {
                            pk.add(mPoker.get(i));

                        }
                    }


                }
                //出的牌是2以上的
                if (value > 9) {
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //值大的
                        if (mPoker.get(i).getPokerValue() > value) {
                            pk.add(mPoker.get(i));

                        }
                        //值相同 主色的
                        if (mPoker.get(i).getPokerValue() == value && mPoker.get(i).getPokertype() == mtype) {
                            pk.add(mPoker.get(i));

                        }

                    }
                }
            }
            //出的牌是主色牌
            if (type == mtype) {
                //2以下的
                if (value < 10) {
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //值大的
                        if (mPoker.get(i).getPokerValue() > value && mPoker.get(i).getPokertype() == mtype) {
                            pk.add(mPoker.get(i));

                        }

                        if (mPoker.get(i).getPokerValue() > 9) {
                            pk.add(mPoker.get(i));

                        }

                    }
                }
                if (value > 9) {
                    for (int i = mPoker.size() - 1; i >= 0; i--) {   //值大的
                        if (mPoker.get(i).getPokerValue() > value) {
                            pk.add(mPoker.get(i));
                        }
                    }
                }

            }
            HashSet<Poker> set=new HashSet<>(pk);
            pk.clear();
            pk.addAll(set);
            //找出符合的对牌
            for (int i=0;i<pk.size();i++)
            {    int t=pk.get(i).getPokertype();
                 int v=pk.get(i).getPokerValue();

                 List<Poker> pk1=new ArrayList<>();
                for (int j=0;j<mPoker.size();j++)
                {   int t1=mPoker.get(j).getPokertype();
                    int v1=mPoker.get(j).getPokerValue();
                    if (t==t1&&v1==v)
                    {
                        pk1.add(mPoker.get(j));
                    }
                }
                Log.e("pk","------"+pk.get(i).getPokerValue()+"---pk1.size:"+pk1.size());
                if (pk1.size()==2)
                {   Log.e("pk","------"+pk1.get(0).getPokerValue());
                    rPoker.addAll(pk1);
                    return rPoker;
                }
                if (pk1.size()==1)
                {   Log.e("pk1","------1"+pk1.get(0).getPokerValue());
                    pk1.clear();
                    pk1=null;
                }

            }

        }

        return rPoker;
    }
}
