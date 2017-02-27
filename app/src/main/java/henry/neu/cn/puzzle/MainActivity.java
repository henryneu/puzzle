package henry.neu.cn.puzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import henry.neu.cn.puzzle.activity.PuzzleMain;
import henry.neu.cn.puzzle.adapter.GridPicListAdapter;
import henry.neu.cn.puzzle.utils.ScreenUtil;

public class MainActivity extends Activity implements View.OnClickListener {

    /**
     * Created by henryneu on 2017/2/19.
     */
    // 返回码：本地图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：本地图库
    private static final int RESULT_CAMERA = 200;
    // image type
    private static final String IMAGE_TYPE = "image/*";
    // Temp照片路径
    public static String TEMP_IMAGE_PATH;

    // GridView 显示图片
    private GridView mGridViewPicList;
    private List<Bitmap> mPicList;
    // 主页图片资源ID
    private int[] mResPicId;
    // 游戏的难度等级
    private int mType = 2;

    private String[] mCustomItems = new String[]{"本地相册", "相机拍照"};
    // PopupWindow
    private PopupWindow mPopupWindow;
    private View mPopupView;
    // TextView 所选拼图等级显示
    private TextView textViewTypeSelected;

    private LayoutInflater mLayoutInflater;
    private TextView mTextViewType2;
    private TextView mTextViewType3;
    private TextView mTextViewType4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_main);
        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/temp.png";
        initViews();
        mGridViewPicList.setAdapter(new GridPicListAdapter(MainActivity.this, mPicList));
        mGridViewPicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mResPicId.length - 1) {
                    // 选择本地图库或相机
                    showDialogCustom();
                } else {
                    // 选择默认图片
                    Intent intent = new Intent(MainActivity.this, PuzzleMain.class);
                    intent.putExtra("picSelectedID", mResPicId[position]);
                    intent.putExtra("mType", mType);
                    startActivity(intent);
                }
            }
        });

        // 显示难度Type
        textViewTypeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出popup window
                popupShow(v);
            }
        });
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

    /**
     * 初始化View相关
     */

    private void initViews() {
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
        // 显示所选拼图难度类型
        textViewTypeSelected = (TextView) findViewById(R.id.textview_puzzle_main_type_selected);
        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // type select view
        mPopupView = mLayoutInflater.inflate(R.layout.puzzle_main_type_select, null);
        mTextViewType2 = (TextView) mPopupView.findViewById(R.id.textview_main_type_2);
        mTextViewType3 = (TextView) mPopupView.findViewById(R.id.textview_main_type_3);
        mTextViewType4 = (TextView) mPopupView.findViewById(R.id.textview_main_type_4);
        // 添加监听事件
        mTextViewType2.setOnClickListener(this);
        mTextViewType3.setOnClickListener(this);
        mTextViewType4.setOnClickListener(this);
    }

    /**
     * popup window items点击事件
     *
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_main_type_2:
                mType = 2;
                textViewTypeSelected.setText("2 X 2");
                break;
            case R.id.textview_main_type_3:
                mType = 3;
                textViewTypeSelected.setText("3 X 3");
                break;
            case R.id.textview_main_type_4:
                textViewTypeSelected.setText("4 X 4");
                break;
            default:
                break;
        }
        mPopupWindow.dismiss();
    }

    /**
     * 显示选择系统图库或相机对话框
     */

    private void showDialogCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (0 == which) {
                    // 本地相册
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                    startActivityForResult(intent, RESULT_IMAGE);
                } else if (1 == which) {
                    // 系统相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, RESULT_CAMERA);
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                Cursor cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                Intent intent = new Intent(MainActivity.this, PuzzleMain.class);
                intent.putExtra("mPicPath", imagePath);
                intent.putExtra("mType", mType);
                cursor.close();
                startActivity(intent);
            } else if (requestCode == RESULT_CAMERA) {
                Intent intent = new Intent(MainActivity.this, PuzzleMain.class);
                intent.putExtra("mPicPath", TEMP_IMAGE_PATH);
                intent.putExtra("mType", mType);
                startActivity(intent);
            }
        }
    }
}
