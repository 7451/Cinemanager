package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.widget.TextView;
import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class AddOrderFragment extends BaseFragment {
    private OnFragmentInteractionListener listener;


    @Override
    protected void populate() {
        listener.hideSearch();
        TextView tv=find(R.id.fragment_addOrder_tv);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_addorder;
    }

    /**防止第二次进入Activity时，接口方法失效*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnFragmentInteractionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "必须实现OnFragmentInteractionListener接口");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
    public interface OnCinemaCreatedListener{
        /**取消保存数据*/
        void cancelAddCinema();
        /**保存数据*/
        void saveCinema(Cinema cinema);
    }
}
