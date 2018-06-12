package com.fellowcar.android.ui;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fellowcar.android.R;

public class EmptyDataFragment extends Fragment {
    private static final String TITLE_MESSAGE = "titleView";
    private static final String SUBTITLE_MESSAGE = "subtitleView";

    private String titleData;
    private String subTitleData;

    public EmptyDataFragment() {
    }

    public static EmptyDataFragment newInstance(String title, String subTitle) {
        EmptyDataFragment fragment = new EmptyDataFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_MESSAGE, title);
        args.putString(SUBTITLE_MESSAGE, subTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleData = getArguments().getString(TITLE_MESSAGE);
            subTitleData = getArguments().getString(SUBTITLE_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_data, container, false);

        TextView titleView = (TextView) view.findViewById(R.id.title_no_data_fragment);
        TextView subtitleView = (TextView) view.findViewById(R.id.subtitle_no_data_fragment);

        titleView.setText(titleData);
        subtitleView.setText(subTitleData);

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}