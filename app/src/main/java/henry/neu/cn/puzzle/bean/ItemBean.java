package henry.neu.cn.puzzle.bean;

import android.graphics.Bitmap;

/**
 * 拼图Item逻辑实体类：封装逻辑相关属性
 *
 * Created by henryneu on 2017/2/24.
 */

public class ItemBean {

    // Item的ID
    private int mItemId;
    // Bitmap
    private int mBitmapId;
    // mBitmap
    private Bitmap mBitmap;

    public ItemBean(){
    }

    public ItemBean(Bitmap mBitmap, int mBitmapId, int mItemId) {
        this.mBitmapId = mBitmapId;
        this.mItemId = mItemId;
        this.mBitmap = mBitmap;
    }

    public int getmItemId() {
        return mItemId;
    }

    public void setmItemId(int mItemId) {
        this.mItemId = mItemId;
    }

    public int getmBitmapId() {
        return mBitmapId;
    }

    public void setmBitmapId(int mBitmapId) {
        this.mBitmapId = mBitmapId;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
