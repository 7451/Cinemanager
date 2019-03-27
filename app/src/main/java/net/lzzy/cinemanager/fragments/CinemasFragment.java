package net.lzzy.cinemanager.fragments;

import android.view.View;
import android.widget.ListView;
import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/26
 * Description:
 */
public class CinemasFragment extends BaseFragment {

    private ListView lv;
    private View empty;
    private CinemaFactory factory=CinemaFactory.getInstance();
    private List<Cinema> cinemas;
    private GenericAdapter<Cinema> adapter;

    @Override
    protected void populate() {
        lv = find(R.id.fragment_cinema_lv);
        empty = find(R.id.fragment_cinemas_tv_none);
        lv.setEmptyView(empty);
        cinemas=factory.get();
        adapter = new GenericAdapter<Cinema>(getActivity(),R.layout.cinemas_item,cinemas) {
            @Override
            public void populate(ViewHolder viewHolder, Cinema cinema) {
                viewHolder.setTextView(R.id.cinemas_items_name,cinema.getName())
                        .setTextView(R.id.cinemas_items_location,cinema.getLocation());

            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);


    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }
}
