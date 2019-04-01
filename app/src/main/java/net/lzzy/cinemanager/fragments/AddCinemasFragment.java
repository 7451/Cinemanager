package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;


/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String province="广西壮族自治区";
    private String city="柳州市";
    private String area="鱼峰区";
    private TextView tvArea;
    private EditText edtName;
    private OnFragmentInteractionListener listener;
    private OnCinemaCreatedListener createdListener;

    @Override
    protected void populate() {
        //调用接口
        listener.hideSearch();
        tvArea = find(R.id.dialog_add_tv_area);
        edtName = find(R.id.dialog_add_cinema_edt_name);
        find(R.id.dialog_add_cinema_btn_cancel)
                .setOnClickListener(v -> { createdListener.cancelAddCinema();});

        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(v -> {
            JDCityPicker cityPicker = new JDCityPicker();
            cityPicker.init(getActivity());
            cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                    AddCinemasFragment.this.province=province.getName();
                    AddCinemasFragment.this.city=city.getName();
                    AddCinemasFragment.this.area=district.getName();
                    String loc=province.getName()+city.getName()+district.getName();
                    tvArea.setText(loc);
                }

                @Override
                public void onCancel() {
                }
            });
            cityPicker.showCityPicker();
        });
        find(R.id.dialog_add_cinema_btn_save).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            Cinema cinema=new Cinema();
            cinema.setName(name);
            cinema.setArea(area);
            cinema.setCity(city);
            cinema.setProvince(province);
            cinema.setLocation(tvArea.getText().toString());
            createdListener.saveCinema(cinema);
            edtName.setText("");
        });

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_addciemas;
    }

    @Override
    public void search(String kw) {

    }

    /**防止第二次进入Activity时，接口方法失效*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hideSearch();
        }
    }

    /**
     * 接口初始化
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnFragmentInteractionListener) context;
            createdListener= (OnCinemaCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "必须实现OnFragmentInteractionListener接口&OnCinemaCreatedListener接口");
        }

    }

    /**销毁接口*/
    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        createdListener=null;
    }
    public interface OnCinemaCreatedListener{
        /**取消保存数据*/
        void cancelAddCinema();
        /**保存数据
         * @param cinema rule cinema
         */
        void saveCinema(Cinema cinema);
    }

}
