package henry.neu.cn.puzzle.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import henry.neu.cn.puzzle.MainActivity;
import henry.neu.cn.puzzle.R;
import henry.neu.cn.puzzle.adapter.GridItemsAdapter;
import henry.neu.cn.puzzle.bean.ItemBean;
import henry.neu.cn.puzzle.utils.GameUtil;
import henry.neu.cn.puzzle.utils.ImagesUtil;
import henry.neu.cn.puzzle.utils.ScreenUtil;

/**
 * 拼图游戏主界面
 * <p>
 * Created by henryneu on 2017/2/19.
 */

public class PuzzleMain extends Activity implements View.OnClickListener {

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
    // 切图后的图片集
    private List<Bitmap> mBitmapItemLists = new ArrayList<Bitmap>();
    // GridView数据适配器
    private GridItemsAdapter mAdapter;
    // TextView 显示步数
    private TextView mTextViewStep;
    // TextView 显示时间
    private TextView mTextViewTime;
    // GridView 显示拼图游戏界面
    private GridView mGridViewGameMain;
    private int mResId;
    private String mPicPath;
    private ImageView mImageView;
    // 是否已显示原图
    private boolean mShowImg;
    // Button 显示原图
    private Button mButtonImage;
    // Button 重置
    private Button mButtonReset;
    // Button 后退
    private Button mButtonBack;
    // 计时器类
    private Timer mTimer;
    // 计时器线程
    private TimerTask mTimerTask;

    // Handler 处理UI更新消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新计时器
                    TIME_COUNT++;
                    mTextViewTime.setText("" + TIME_COUNT);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_game_main_interface);
        // 获取选择的图片
        Bitmap picSelectedTemp;
        // 选择默认图片还是自定义图片
        mResId = getIntent().getExtras().getInt("picSelectedID");
        mPicPath = getIntent().getExtras().getString("mPicPath");
        if (mResId != 0) {
            // 选择默认图片
            picSelectedTemp = BitmapFactory.decodeResource(getResources(), mResId);
        } else {
            // 选择自定义图片
            picSelectedTemp = BitmapFactory.decodeFile(mPicPath);
        }
        TYPE = getIntent().getExtras().getInt("mType", 2);
        // 处理图片
        handlerImage(picSelectedTemp);
        // 初始化View相关
        initViews();
        // 生成游戏数据
        generateGame();
        mGridViewGameMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断是否可以移动
                if (GameUtil.isMoveable(position)) {
                    // 交换点击Item与空格的位置
                    GameUtil.swapItems(GameUtil.mItemBeans.get(position), GameUtil.mBlankItemBeans);
                    // 重新获取图片
                    recreateData();
                    // 通知GridView更改UI
                    mAdapter.notifyDataSetChanged();
                    // 更新步数
                    STEP_COUNT++;
                    mTextViewStep.setText("" + STEP_COUNT);
                    // 判断是否拼图完成
                    if (GameUtil.isSuccess()) {
                        // 将最后一张图显示出
                        recreateData();
                        mBitmapItemLists.remove(TYPE * TYPE - 1);
                        mBitmapItemLists.add(mLastBitmap);
                        // 更新GridView的UI
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(PuzzleMain.this, "拼图成功", Toast.LENGTH_LONG).show();
                        mGridViewGameMain.setEnabled(false);
                        mTimer.cancel();
                        mTimerTask.cancel();
                    }
                }
            }
        });

        mButtonImage.setOnClickListener(this);
        mButtonReset.setOnClickListener(this);
        mButtonBack.setOnClickListener(this);
    }

    /**
     * Button点击事件
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 显示原图Button点击事件
            case R.id.button_puzzle_main_img:
                Animation animationShow = AnimationUtils.loadAnimation(PuzzleMain.this, R.anim.image_show_anim);
                Animation animationHide = AnimationUtils.loadAnimation(PuzzleMain.this, R.anim.image_hide_anim);
                if (mShowImg) {
                    mImageView.startAnimation(animationHide);
                    mImageView.setVisibility(View.GONE);
                    mShowImg = false;
                } else {
                    mImageView.startAnimation(animationShow);
                    mImageView.setVisibility(View.VISIBLE);
                    mShowImg = true;
                }
                break;
            // 重置Button点击事件
            case R.id.button_puzzle_main_restart:
                cleanConfig();
                generateGame();
                recreateData();
                // GridView 更新UI
                mTextViewStep.setText("" + STEP_COUNT);
                mAdapter.notifyDataSetChanged();
                mGridViewGameMain.setEnabled(true);
                break;
            // 返回Button点击事件
            case R.id.button_puzzle_main_back:
                PuzzleMain.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 生成游戏相关数据
     */

    private void generateGame() {
        // 切图 获取初始拼图数据 正常顺序
        new ImagesUtil().createInitBitmap(TYPE, mPicSelected, PuzzleMain.this);
        // 生成随机数据
        GameUtil.getPuzzleGenerator();
        // 获取Bitmap集合
        for (ItemBean temp : GameUtil.mItemBeans) {
            mBitmapItemLists.add(temp.getmBitmap());
        }
        // 数据适配器
        mAdapter = new GridItemsAdapter(mBitmapItemLists, this);
        mGridViewGameMain.setAdapter(mAdapter);
        // 启用计时器
        mTimer = new Timer(true);
        // 计时器线程
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /**
     * 重新获取图片列表
     */

    private void recreateData() {
        mBitmapItemLists.clear();
        for (ItemBean temp : GameUtil.mItemBeans) {
            mBitmapItemLists.add(temp.getmBitmap());
        }
    }

    /**
     * 处理图片相关 自适应大小
     *
     * @param bitmap
     */

    private void handlerImage(Bitmap bitmap) {
        // 将图片放大到固定尺寸
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeight = ScreenUtil.getScreenSize(this).heightPixels;
        mPicSelected = new ImagesUtil().resizaBitmap(screenWidth * 0.8f, screenHeight * 0.6f, bitmap);
    }

    /**
     * 清空相关参数设置
     */

    private void cleanConfig() {
        // 清空相关参数设置
        GameUtil.mItemBeans.clear();
        // 停止计时器
        mTimer.cancel();
        mTimerTask.cancel();
        STEP_COUNT = 0;
        TIME_COUNT = 0;
        // 清除拍摄的照片
        if (mPicPath != null) {
            // 删除照片
            File file = new File(MainActivity.TEMP_IMAGE_PATH);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 游戏主界面添加显示原图ImageView
     */

    private void addImgView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.ralativelayout_puzzle_game_main_interface);
        mImageView = new ImageView(PuzzleMain.this);
        mImageView.setImageBitmap(mPicSelected);
        int imgViewWith = (int) (mPicSelected.getWidth() * 0.9F);
        int imgViewHeight = (int) (mPicSelected.getHeight() * 0.9F);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imgViewWith, imgViewHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(params);
        relativeLayout.addView(mImageView);
        mImageView.setVisibility(View.GONE);
    }

    /**
     * 返回时调用
     */

    @Override
    protected void onStop() {
        super.onStop();
        // 清空相关参数设置
        cleanConfig();
        this.finish();
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
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(mPicSelected.getWidth(), mPicSelected.getHeight());
        // 水平居中
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gridParams.addRule(RelativeLayout.BELOW, R.id.linearlayout_puzzle_main_spinner);
        gridParams.addRule(RelativeLayout.ABOVE, R.id.linearlayout_puzzle_main_buttons);
        // Grid 显示
        mGridViewGameMain.setLayoutParams(gridParams);
        mGridViewGameMain.setHorizontalSpacing(0);
        mGridViewGameMain.setVerticalSpacing(0);
        // Button
        mButtonImage = (Button) findViewById(R.id.button_puzzle_main_img);
        mButtonReset = (Button) findViewById(R.id.button_puzzle_main_restart);
        mButtonBack = (Button) findViewById(R.id.button_puzzle_main_back);
        // Flag 是否已显示原图
        mShowImg = false;
        // 添加ImageView
        addImgView();
    }
}
