package henry.neu.cn.puzzle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import henry.neu.cn.puzzle.adapter.GridPicListAdapter;
import henry.neu.cn.puzzle.utils.ScreenUtil;

public class MainActivity extends AppCompatActivity {

    /**
     * Created by henryneu on 2017/2/19.
     */

    // GridView 显示图片
    private GridView mGridViewPicList;
    private List<Bitmap> mPicList;
    // 主页图片资源ID
    private int[] mResPicId;

    private PopupWindow mPopupWindow;
    private View mPopupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_main);
        mPicList = new ArrayList<Bitmap>();
        mGridViewPicList = (GridView) findViewById(R.id.gridview_puzzle_main_pic_list);
        // 初始化Bitmap数据
        mResPicId = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8, R.drawable.pic9, R.drawable.pic10,
                R.drawable.pic11, R.drawable.pic12, R.drawable.pic13, R.drawable.pic14, R.drawable.pic15, R.mipmap.ic_launcher};
        Bitmap[] bitmaps = new Bitmap[mResPicId.length];
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), mResPicId[i]);
            mPicList.add(bitmaps[i]);
        }
        mGridViewPicList.setAdapter(new GridPicListAdapter(MainActivity.this, mPicList));
    }

    /**
     * 显示popup window
     *
     * @param view popup window
     */

    private void popupShow(View view) {
        int density = (int) ScreenUtil.getDeviceDensity(this);
        // 显示 popup window
        mPopupWindow = new PopupWindow(mPopupView, 200 * density, 50 * density);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        // 透明背景
        Drawable transpent = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(transpent);
        // 获取位置
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - 40 * density, location[1] + 30 * density);
    }
}
