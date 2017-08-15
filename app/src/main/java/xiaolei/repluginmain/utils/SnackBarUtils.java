package xiaolei.repluginmain.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import xiaolei.repluginmain.R;
import xiaolei.repluginmain.base.BaseApplication;

/**
 * Created by sunxl8 on 2017/8/11.
 */

public class SnackBarUtils {

    public static void showShort(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar, Color.WHITE, BaseApplication.mContext.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    public static void showShort(View view, String message, String action, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar, Color.WHITE, BaseApplication.mContext.getResources().getColor(R.color.colorAccent));
        snackbar.setAction(action, listener);
        snackbar.show();
    }

    public static void showLong(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar, Color.WHITE, BaseApplication.mContext.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    public static void showLong(View view, String message, String action, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar, Color.WHITE, BaseApplication.mContext.getResources().getColor(R.color.colorAccent));
        snackbar.setAction(action, listener);
        snackbar.show();
    }

    /**
     * 设置Snackbar文字和背景颜色
     *
     * @param snackbar
     * @param messageColor
     * @param backgroundColor
     */
    public static void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
        View view = snackbar.getView();
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
            ((TextView) view.findViewById(R.id.snackbar_action)).setTextColor(messageColor);
        }
    }

    /**
     * 向Snackbar中添加view
     *
     * @param snackbar
     * @param layoutId
     * @param index    新加布局在Snackbar中的位置
     */
    public static void SnackbarAddView(Snackbar snackbar, int layoutId, int index) {
        View snackbarview = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarview;

        View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId, null);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;

        snackbarLayout.addView(add_view, index, p);
    }

}
