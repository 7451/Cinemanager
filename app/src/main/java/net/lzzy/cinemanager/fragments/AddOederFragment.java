package net.lzzy.cinemanager.fragments;

import android.widget.TextView;
import net.lzzy.cinemanager.R;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class AddOederFragment extends BaseFragment {

    @Override
    protected void populate() {
        TextView tv=find(R.id.fragment_addOrder_tv);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_addorder;
    }
}
