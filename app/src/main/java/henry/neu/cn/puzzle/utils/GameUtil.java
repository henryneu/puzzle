package henry.neu.cn.puzzle.utils;

import java.util.ArrayList;
import java.util.List;

import henry.neu.cn.puzzle.activity.PuzzleMain;
import henry.neu.cn.puzzle.bean.ItemBean;

/**
 * 拼图工具类：实现拼图的交换与生成算法
 * <p>
 * Created by henryneu on 2017/2/24.
 */

public class GameUtil {

    // 游戏信息单元格Bean
    public static List<ItemBean> mItemBeans = new ArrayList<ItemBean>();
    // 空的单元格Bean
    public static ItemBean mBlankItemBeans = new ItemBean();

    /**
     * 生成随机的Item
     */

    public static void getPuzzleGenerator() {
        int index = 0;
        // 随机打乱顺序
        for (int i = 0; i < mItemBeans.size(); i++) {
            index = (int) Math.random() * PuzzleMain.TYPE * PuzzleMain.TYPE;
            swapItems(mItemBeans.get(index), GameUtil.mBlankItemBeans);
        }
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < mItemBeans.size(); i++) {
            data.add(mItemBeans.get(i).getmBitmapId());
        }
        // 判断生成是否有解
        if (canSolve(data)) {
            return;
        } else {
            getPuzzleGenerator();
        }
    }

    /**
     * 判断点击的Item是否可移动
     *
     * @param position position
     * @return 能否移动
     */

    public static boolean isMoveable(int position) {
        int type = PuzzleMain.TYPE;
        // 获取空格Item
        int blankId = GameUtil.mBlankItemBeans.getmItemId() - 1;
        // 不同行相差为type
        if (Math.abs(blankId - position) == type) {
            return true;
        }
        // 相同行相差为1
        if (blankId / type == position / type && Math.abs(blankId - position) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 交换空格与所点击Item的位置
     *
     * @param from  交换图
     * @param blank 空白图
     */

    public static void swapItems(ItemBean from, ItemBean blank) {
        ItemBean tempItemBean = new ItemBean();
        // 交换BitmapId
        tempItemBean.setmBitmapId(from.getmBitmapId());
        from.setmBitmapId(blank.getmBitmapId());
        blank.setmBitmapId(tempItemBean.getmBitmapId());
        // 交换Bitmap
        tempItemBean.setmBitmap(from.getmBitmap());
        from.setmBitmap(blank.getmBitmap());
        blank.setmBitmap(tempItemBean.getmBitmap());
        // 设置新的Blank
        GameUtil.mBlankItemBeans = from;
    }

    /**
     * 是否拼图完成
     *
     * @return 是否拼图完成
     */

    public static boolean isSuccess() {
        for (ItemBean tempBean : GameUtil.mItemBeans) {
            if (tempBean.getmBitmapId() != 0 && (tempBean.getmItemId()) == tempBean.getmBitmapId()) {
                continue;
            } else if (tempBean.getmBitmapId() == 0 && tempBean.getmItemId() == PuzzleMain.TYPE * PuzzleMain.TYPE) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成数据是否有解
     *
     * @param data 拼图数组数据
     * @return 该数据是否有解
     */

    public static boolean canSolve(List<Integer> data) {
        // 获取空格ID
        int blankId = GameUtil.mBlankItemBeans.getmItemId();
        // 可行性原则
        if (data.size() % 2 == 1) {
            return getInversions(data) % 2 == 0;
        } else {
            // 从下往上数，空格位于奇数行
            if (((blankId - 1) / PuzzleMain.TYPE) % 2 == 1) {
                return getInversions(data) % 2 == 0;
            } else {
                // 从下往上数，空格位于偶数行
                return getInversions(data) % 2 == 1;
            }
        }
    }

    /**
     * 计算倒置和算法
     *
     * @param data 生成的拼图数组数据
     * @return 该序列的倒置和
     */

    public static int getInversions(List<Integer> data) {
        int inversions = 0;
        int inversionCount = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                int index = data.get(i);
                if (data.get(j) != 0 && data.get(j) < index) {
                    inversionCount++;
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }
}
