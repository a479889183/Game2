package px.com.game2.Layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

/**
 * Created by admin on 2016/10/11.
 */

public class BaseLayer extends CCLayer{

    protected CGSize winSize;

    public  BaseLayer(){
        winSize = CCDirector.sharedDirector().winSize();

    }
}
