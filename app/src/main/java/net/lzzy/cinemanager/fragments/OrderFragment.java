package net.lzzy.cinemanager.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import net.lzzy.cinemanager.utils.AppUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/26
 * Description:
 */
public class OrderFragment extends BaseFragment {

    private ListView lv;
    private View empty;
    private OrderFactory factory=OrderFactory.getInstance();
    private List<Order> orders;
    private Order order;
    private List<Cinema> cinemas;
    private GenericAdapter<Order> adapter;
    private Spinner spOrder;
    private Button but;
    private float touchX1;
    private float touchX2;
    private boolean isDelete;
    public static final int MIN_DISTANCE = 100;

    public OrderFragment(){}
    public OrderFragment(Order order){
        this.order=order;
    }

    @Override
    protected void populate() {
        lv = find(R.id.fragment_cinema_lv);
        empty = find(R.id.fragment_cinemas_tv_none);
        spOrder = find(R.id.dialog_add_order_sp_area);
        lv.setEmptyView(empty);
        orders=factory.get();
        adapter = new GenericAdapter<Order>(getActivity(),R.layout.order_item,orders) {
            @Override
            public void populate(ViewHolder viewHolder, Order order) {
                String location= String.valueOf(CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()));
                viewHolder.setTextView(R.id.order_item_movieName,order.getMovie())
                        .setTextView(R.id.order_item_area,location);


                but = viewHolder.getView(R.id.order_item_btn);
                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("删除确认")
                                .setMessage("要删除订单吗？")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", (dialog, which) -> adapter.remove(order)).show();
                    }
                });
                viewHolder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                    @Override
                    public boolean handleTouch(MotionEvent event) {
                        slideToDelete(event,order,but);
                        return true;
                    }
                });
            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }
        };
        lv.setAdapter(adapter);
        if (order!=null){
            svae(order);
        }

    }

    private void slideToDelete(MotionEvent event, Order order, Button but) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX1=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                touchX2 =event.getX();
                if (touchX1-touchX2> MIN_DISTANCE){
                    if (!isDelete){
                        but.setVisibility(View.VISIBLE);
                        isDelete=true;
                    }

                }else {
                    if (but.isShown()){
                        but.setVisibility(View.GONE);
                        isDelete=false;
                    }else {
                        clickOrder(order);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void clickOrder(Order order) {
        Cinema cinema=CinemaFactory.getInstance()
                .getById(order.getCinemaId().toString().toString());
        String content="["+order.getMovie()+"]"+order.getMovieTime()+"\n"+cinema+"票价"+order.getPrice()+"元";
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_qrcode,null);
        ImageView img=view.findViewById(R.id.dialog_qrcode_img);
        img.setImageBitmap(AppUtils.createQRCodeBitmap(content,300,300));
        new AlertDialog.Builder(getActivity())
                .setView(view).show();
    }
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_order;
    }

    public void svae(Order order){
        adapter.add(order);
    }

    @Override
    public void search(String kw) {

    }

}
