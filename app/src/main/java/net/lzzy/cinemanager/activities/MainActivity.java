package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.AddOrderFragment;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OnFragmentInteractionListener;
import net.lzzy.cinemanager.fragments.OrderFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.ViewUtils;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OnFragmentInteractionListener,AddCinemasFragment.OnCinemaCreatedListener,
AddOrderFragment.OnOrderCreatedListener{

    private View layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private FragmentManager manger=getSupportFragmentManager();
    private SparseArray<String> titleArray=new SparseArray<>();
    private SparseArray<Fragment> fragmentArray=new SparseArray<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 去掉标题栏 **/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment=manger.findFragmentById(R.id.fragment_container);
                if (fragment!=null){
                    if (fragment instanceof BaseFragment){
                        ((BaseFragment)fragment).search(kw);
                    }
                }
                return true;
            }
        });
    }

    /** 标题栏 **/
    private void setTitleMenu() {
        titleArray.put(R.id.bar_title_tv_add_cinema,"添加影院");
        titleArray.put(R.id.bar_title_tv_view_cinema,"影院列表");
        titleArray.put(R.id.bar_title_tv_add_order,"添加订单");
        titleArray.put(R.id.bar_title_tv_view_order,"我的订单");
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v -> {
            int visible=layoutMenu.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
         tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(R.id.main_sv_search);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v -> System.exit(0));
    }

    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));
        // fragment的创建和托管的另一种方式
        //region
        FragmentTransaction transaction=manger.beginTransaction();
        Fragment fragment=fragmentArray.get(v.getId());
        if (fragment==null){
            fragment=createFragment(v.getId());
            fragmentArray.put(v.getId(),fragment);
            transaction.add(R.id.fragment_container,fragment);
        }
        //把所有的fragment隐藏
        for (Fragment f:manger.getFragments()){
            transaction.hide(f);
        }
        //把当前的fragment现示出来
        transaction.show(fragment).commit();
        //endregion
    }

    private Fragment createFragment(int id) {
        switch (id){
            case R.id.bar_title_tv_add_cinema:
                return new AddCinemasFragment();
            case R.id.bar_title_tv_view_cinema:

                //fragment的创建和托管的一种方式
                //region
                //manger.beginTransaction()
                //  .replace(R.id.fragment_container,new CinemasFragment())
                //  .commit();
                //endregion
                //fragment的创建和托管的另一种方式
                return new CinemasFragment();
            case R.id.bar_title_tv_add_order:
                return new AddOrderFragment();

            case R.id.bar_title_tv_view_order:
                //fragment的创建和托管的一种方式
                //region
                // manger.beginTransaction().
                //      replace(R.id.fragment_container,new OrderFragment())
                // .commit();
                //endregion
                //fragment的创建和托管的另一种方式
                return new OrderFragment();
            default:
                break;

        }
        return null;
    }


    @Override
    public void hideSearch() {
        search.setVisibility(View.GONE);
    }

    @Override
    public void cancelAddCinema() {
        Fragment addCinemasFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemasFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manger.beginTransaction();
        if (cinemasFragment==null){
            cinemasFragment=new CinemasFragment();
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);
        }
        transaction.hide(addCinemasFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemasFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemasFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manger.beginTransaction();
        if (cinemasFragment==null){
        //创建CinemasFragment同时要传Cinema对象进来
            cinemasFragment=new CinemasFragment(cinema);
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);
        }else {
            ((CinemasFragment)cinemasFragment).save(cinema);

        }
        transaction.hide(addCinemasFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);


    }

    @Override
    public void cancelAddOrder() {
        Fragment addOrderFragment=fragmentArray.get(R.id.bar_title_tv_add_order);
        if (addOrderFragment==null){
            return;
        }
        Fragment orderFragment=fragmentArray.get(R.id.bar_title_tv_view_order);
        FragmentTransaction transaction=manger.beginTransaction();
        if (orderFragment==null){
            orderFragment=new OrderFragment();
            fragmentArray.put(R.id.bar_title_tv_view_order,orderFragment);
            transaction.add(R.id.fragment_container,orderFragment);
        }
        transaction.hide(addOrderFragment).show(orderFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_order));
        search.setVisibility(View.VISIBLE);

    }

    @Override
    public void saveOrder(Order order) {
        Fragment addOrderFragment=fragmentArray.get(R.id.bar_title_tv_add_order);
        if (addOrderFragment==null){
            return;
        }
        Fragment orderFragment=fragmentArray.get(R.id.bar_title_tv_view_order);
        FragmentTransaction transaction=manger.beginTransaction();
        if (orderFragment==null){
            //创建CinemasFragment同时要传Cinema对象进来
            orderFragment=new OrderFragment(order);
            fragmentArray.put(R.id.bar_title_tv_view_order,orderFragment);
            transaction.add(R.id.fragment_container,orderFragment);
        }else {
            ((OrderFragment)orderFragment).svae(order);

        }
        transaction.hide(addOrderFragment).show(orderFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_order));
        search.setVisibility(View.VISIBLE);

    }
}
