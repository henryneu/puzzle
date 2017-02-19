package henry.neu.cn.puzzle;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import henry.neu.cn.puzzle.utils.ScreenUtil;

public class MainActivity extends AppCompatActivity {

    private PopupWindow mPopupWindow;
    private View mPopupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_main);
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
