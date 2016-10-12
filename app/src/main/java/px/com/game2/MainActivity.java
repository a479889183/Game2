package px.com.game2;

import android.app.Activity;
import android.os.Bundle;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import px.com.game2.Layer.GameLayer;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView surfaceView=new CCGLSurfaceView(this);

        setContentView(surfaceView);

        CCDirector director=CCDirector.sharedDirector();
        //开启线程
        director.attachInView(surfaceView);
        director.setDisplayFPS(true);
        director.setScreenSize(900,680);
        director.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
        CCScene scene=CCScene.node();
        scene.addChild(new GameLayer());
        director.runWithScene(scene);

    }
}
