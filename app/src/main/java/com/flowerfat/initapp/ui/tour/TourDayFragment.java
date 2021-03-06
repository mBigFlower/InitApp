package com.flowerfat.initapp.ui.tour;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.flowerfat.initapp.R;
import com.flowerfat.initapp.base.BaseFragment;
import com.flowerfat.initapp.base.BasePopup;
import com.flowerfat.initapp.model.TourDay;
import com.flowerfat.initapp.model.TourDetail;
import com.flowerfat.initapp.temp.DialogManager;
import com.flowerfat.initapp.temp.TourDetailEditDialog;
import com.flowerfat.initapp.temp.TourHeaderDialog;
import com.flowerfat.initapp.temp.TourItemMoreMenuPopup;
import com.flowerfat.initapp.ui.adapter.TourDayAdapter;

import java.util.List;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * Created by 明明大美女 on 2016/9/7.
 */
public class TourDayFragment extends BaseFragment {

    public static final String PAGE_INDEX = "page_index";

    @BindView(R.id.oneday_recyclerview)
    RecyclerView mRecyclerview;

    private TourDayAdapter mAdapter;
    private TourDayFragmentModel mModel;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_tour_one_day;
    }

    @Override
    protected void main() {
        initRecyclerView();
        // 数据层
        mModel = new TourDayFragmentModel(getArguments().getInt(PAGE_INDEX));
        showList(mModel.getTourDayList());
    }

    private void initRecyclerView() {
        mAdapter = new TourDayAdapter();

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setOnClickListener((v, position) -> {
            Log.d(TAG, "initRecyclerView: " + position);
            if (position == 1000) {
                headerDialogShow();
            } else if (position >= 0)
                itemEditDialogShow(position);
            else {
                moreDialogShow(v, position);
            }
        });
        mAdapter.setOnLongClickListener(position -> {
            deleteDialogShow(position);
        });
        mRecyclerview.setAdapter(mAdapter);
    }


    public void showList(List<TourDetail> tourDetails) {
        mAdapter.clear();

        mAdapter.updateHeader(mModel.getTourDay().getHotel(), mModel.getTourDay().getPlace(), "Happy New Year  !");

        mAdapter.addAll(tourDetails);
        mAdapter.detectState();
    }

    private void headerDialogShow() {
        TourHeaderDialog tourHeaderDialog = new TourHeaderDialog(getActivity(), mModel.getTourDay());
        tourHeaderDialog.setDialogListener(new DialogManager.OnDialogListener<TourDay>() {
            @Override
            public void onSure(TourDay tourDay) {
                // 更新model
                mModel.setTourDay(tourDay);
                // 更新列表显示
                mAdapter.updateHeader(tourDay.getHotel(), tourDay.getPlace(), "Happy New Year  !");
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void moreDialogShow(View v, int position) {
        String phoneStr = mAdapter.getData(position + 1000).getPhone();
        TourItemMoreMenuPopup moreMenuPopup = new TourItemMoreMenuPopup(getActivity(), phoneStr);
        moreMenuPopup.showOnAnchor(v, BasePopup.VerticalPosition.ABOVE, BasePopup.HorizontalPosition.LEFT);
        moreMenuPopup.setOnMoreMenuItemClickListener(new TourItemMoreMenuPopup.OnMoreMenuItemClickListener() {
            @Override
            public void onCallClick(String phoneStr) {
                callDialogShow(phoneStr);
            }

            @Override
            public void onDeleteClick() {
                deleteDialogShow(position + 1000);
            }

            @Override
            public void onCancelClick() {

            }
        });
    }

    public void callDialogShow(String phoneStr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否拨打：" + phoneStr)
                .setPositiveButton("拨打", (dialog, which) -> {

                }).show();
    }

    public void deleteDialogShow(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否删除该项？")
                .setPositiveButton("删除", (dialog, which) -> {
//                    mAdapter.removeItem(position);
                    mModel.deleteTourDetail(position);
                    showList(mModel.getTourDayList());
                    mAdapter.detectState();
                }).show();
    }

    public void itemEditDialogShow(int position) {
        TourDetail tourDetail = mAdapter.getData(position);
        TourDetailEditDialog dialogManager = new TourDetailEditDialog(getActivity(), tourDetail);
        dialogManager.setDialogListener(new DialogManager.OnDialogListener<TourDetail>() {
            @Override
            public void onSure(TourDetail data) {
                mModel.editTourDetail(position, data);
                showList(mModel.getTourDayList());
                mAdapter.detectState();
            }

            @Override
            public void onCancel() {

            }
        });
    }


    void itemAddDialogShow() {
        TourDetailEditDialog dialogManager = new TourDetailEditDialog(getActivity(), null);
        dialogManager.setDialogListener(new DialogManager.OnDialogListener<TourDetail>() {
            @Override
            public void onSure(TourDetail data) {
                // 这个adapter还真是方便
                showList(mModel.addTourDetail(data));
            }

            @Override
            public void onCancel() {
            }
        });
    }

}
