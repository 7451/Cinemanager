package net.lzzy.cinemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/26
 * Description:
 */
public class OrderFragment extends BaseFragment {

    @Override
    protected void populate() {
        TextView tv = find(R.id.fragment_order_tv);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_order;
    }
}
