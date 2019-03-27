package net.lzzy.cinemanager.fragments;

import android.view.View;
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

    @Override
    protected void populate() {
        tvArea = find(R.id.dialog_add_tv_area);
        edtName = find(R.id.dialog_add_cinema_edt_name);
        find(R.id.dialog_add_cinema_btn_cancel)
                .setOnClickListener(v -> {});

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
            //adapter.add(cinema);
            edtName.setText("");
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_addciemas;
    }
}
