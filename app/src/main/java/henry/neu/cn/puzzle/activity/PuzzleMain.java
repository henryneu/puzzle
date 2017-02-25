package henry.neu.cn.puzzle.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import henry.neu.cn.puzzle.R;

/**
 * 拼图游戏主界面
 * <p>
 * Created by henryneu on 2017/2/19.
 */

public class PuzzleMain extends Activity {

    // 设置游戏的难度系数
    public static int TYPE = 2;
    // 步数
    public static int STEP_COUNT = 0;
    // 时间
    public static int TIME_COUNT = 0;

    // 在拼图完成时填充最后一个Item的bitmap
    public static Bitmap mLastBitmap;
    // 选择的图片
    private Bitmap mPicSelected;

    // TextView 显示步数
    private TextView mTextViewStep;
    // TextView 显示时间
    private TextView mTextViewTime;
    // GridView 显示拼图游戏界面
    private GridView mGridViewGameMain;
    // Button 显示原图
    private Button mButtonImage;
    // Button 重置
    private Button mButtonReset;
    // Button 后退
    private Button mButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_game_main_interface);
        initViews();
    }

    /**
     * 初始化View相关
     */

    private void initViews() {
        // TextView 显示步数和时间
        mTextViewStep = (TextView) findViewById(R.id.textview_puzzle_main_step);
        mTextViewTime = (TextView) findViewById(R.id.textview_puzzle_main_time);
        mTextViewStep.setText("" + STEP_COUNT);
        mTextViewTime.setText("" + TIME_COUNT);
        // GridView
        mGridViewGameMain = (GridView) findViewById(R.id.gridview_puzzle_game_main);
        // 由游戏难度设置GridView
        mGridViewGameMain.setNumColumns(TYPE);
        //RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(mPicSelected.getWidth(), mPicSelected.getHeight());
        //gridParams.addRule(RelativeLayout.BELOW, R.id.linearlayout_puzzle_main_spinner);
        // Grid 显示
       // mGridViewGameMain.setLayoutParams(gridParams);
        mGridViewGameMain.setHorizontalSpacing(0);
        mGridViewGameMain.setVerticalSpacing(0);
        // Button
        mButtonImage = (Button) findViewById(R.id.button_puzzle_main_img);
        mButtonReset = (Button) findViewById(R.id.button_puzzle_main_restart);
        mButtonBack = (Button) findViewById(R.id.button_puzzle_main_back);

    }
}
