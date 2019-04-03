package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;
import net.lzzy.sqllib.GenericAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class AddOrderFragment extends BaseFragment {
    private OnFragmentInteractionListener listener;
    private OnOrderCreatedListener orderListener;
    private EditText edtName;
    private TextView tvDate;
    private Spinner spOrder;
    private EditText edtPrice;
    private ImageView imgQRCode;
    List<Cinema> cinemas;
    private CustomDatePicker datePicker;
    private CinemaFactory cinemaFactory;
    private ArrayAdapter<Cinema> adapter;

    @Override
    protected void populate() {
        listener.hideSearch();
        edtName = find(R.id.dialog_add_order_edt_name);
        tvDate = find(R.id.activity_add_book_tv_date);
        spOrder = find(R.id.dialog_add_order_sp_area);
        edtPrice = find(R.id.dialog_add_order_edt_price);
        imgQRCode = find(R.id.dialog_add_order_imv);
        addListener();
        initDatePicker();
        find(R.id.activity_add_book_layout).setOnClickListener(v -> datePicker.show(tvDate.getText().toString()));



    }
    /**添加数据*/
    private void addListener() {
        cinemas= CinemaFactory.getInstance().get();
        //获取cinema中的地址
        adapter = new ArrayAdapter<>
                        (getActivity(),android.R.layout.simple_spinner_dropdown_item, cinemas);
        spOrder.setAdapter(adapter);

        find(R.id.dialog_add_order_btn_cancel)
                .setOnClickListener(v -> {orderListener.cancelAddOrder(); });
        //点击保存，保存数据
        //region
        find(R.id.dialog_add_order_btn_ok).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            String strPrice=edtPrice.getText().toString();
            if (TextUtils.isEmpty(name)||TextUtils.isEmpty(strPrice)){
                Toast.makeText(getActivity(),"信息不完整，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }
            float price;
            try{
                price=Float.parseFloat(strPrice);
            }catch (NumberFormatException e){
                Toast.makeText(getActivity(),"票价格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }

            Order order=new Order();
            Cinema cinema=cinemas.get(spOrder.getSelectedItemPosition());
            order.setCinemaId(cinema.getId());
            order.setMovie(name);
            order.setPrice(price);
            order.setMovieTime(tvDate.getText().toString());
            orderListener.saveOrder(order);
            edtName.setText("");
            edtPrice.setText("");

        });
        //endregion
        //生成二维码
        //region
        find(R.id.dialog_add_order_btn_qrCode).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            String price=edtPrice.getText().toString();
            String location=spOrder.getSelectedItem().toString();
            String time=tvDate.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price)) {
                Toast.makeText(getActivity(),"信息不完整",Toast.LENGTH_SHORT).show();
                return;
            }
            String content="["+name+"]"+time+"\n"+location+"票价"+price+"元";
            imgQRCode.setImageBitmap(AppUtils.createQRCodeBitmap(content,200,200));
        });
        imgQRCode.setOnLongClickListener(v -> {
            Bitmap bitmap=((BitmapDrawable)imgQRCode.getDrawable()).getBitmap();
            Toast.makeText(getActivity(),AppUtils.readQRCode(bitmap),Toast.LENGTH_SHORT).show();
            return true;
        });
        //endregion

    }


    /**日期*/
    public void initDatePicker() {
        //region
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now=sdf.format(new Date());
        tvDate.setText(now);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,1);
        String end=sdf.format(calendar.getTime());
        datePicker=new CustomDatePicker(getActivity(), s -> tvDate.setText(s),now,end);
        datePicker.showSpecificTime(true);
        datePicker.setIsLoop(true);
        //endregion
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_addorder;
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
            cinemas.clear();
            cinemas.addAll(cinemaFactory.get());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnFragmentInteractionListener) context;
            orderListener= (OnOrderCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "必须实现OnFragmentInteractionListener接口&OnOrderCreatedListener接口");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        orderListener=null;
    }


    public interface OnOrderCreatedListener{
        /**取消保存数据*/
        void cancelAddOrder();

        /**保存数据
         * @param order rule order
         */
        void saveOrder(Order order);
    }
}
