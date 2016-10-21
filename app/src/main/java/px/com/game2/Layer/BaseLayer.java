package px.com.game2.Layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

import px.com.game2.utils.Constant;

/**
 * Created by admin on 2016/10/11.
 */

public class BaseLayer extends CCLayer implements Constant{

    protected CGSize winSize;

    public  BaseLayer(){
        winSize = CCDirector.sharedDirector().winSize();

    }
}
