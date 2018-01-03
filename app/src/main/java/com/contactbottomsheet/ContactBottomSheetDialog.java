package com.contactbottomsheet;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.contactbottomsheet.contactutils.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisarg W on 1/2/2018.
 */

public class ContactBottomSheetDialog extends BottomSheetDialogFragment {

    View v;
    private List<Contact> modelArrayList=new ArrayList<>();
    private List<Contact> buffer=new ArrayList<>();
    ContactAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.dialog_bottom_sheet, container, false);

        showData();

        return v;
    }

    private void showData()
    {
        if (MainActivity.DataModelHolder.hasData()) {
            modelArrayList = MainActivity.DataModelHolder.getData();
            buffer.addAll(modelArrayList);
        }

        RecyclerView lvSearch= v.findViewById(R.id.lvContact);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        lvSearch.setLayoutManager(linearLayoutManager);
        lvSearch.setHasFixedSize(true);
        adapter = new ContactAdapter(getActivity(),
                modelArrayList, new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Contact data) {

            }
        });
        lvSearch.setAdapter(adapter);

        EditText editText=v.findViewById(R.id.edt_search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equalsIgnoreCase("")) {
                    List<Contact> categoryModels = filter(buffer, s.toString());
                    modelArrayList.clear();
                    View view = getView();
                    View parent = (View) view.getParent();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                    CoordinatorLayout.Behavior behavior = params.getBehavior();
                    BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                    bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());

                    parent.setBackgroundColor(Color.WHITE);
                    modelArrayList.addAll(categoryModels);
                    adapter.notifyDataSetChanged();
                }else
                {
                    if(!modelArrayList.isEmpty()) {
                        modelArrayList.clear();
                    }
                    modelArrayList.addAll(buffer);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private static List<Contact> filter(List<Contact> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Contact> filteredModelList = new ArrayList<>();
        for (Contact model : models) {
            final String text = model.displayName.toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


}
