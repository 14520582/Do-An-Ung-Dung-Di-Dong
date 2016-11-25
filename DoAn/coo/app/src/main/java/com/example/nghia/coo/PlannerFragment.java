package com.example.nghia.coo;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlannerFragment extends Fragment {
    private ListView listView;
    ArrayList<ScheduleObject> arraySchedule=new ArrayList<ScheduleObject>();
    ScheduleAdapter adapter=null;
    public PlannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_planner, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.planner));
        final SQLite db = new SQLite(getActivity(),"Shopping.sqlite",null,1);
        listView=(ListView) view.findViewById(R.id.listviewSchedule);
        adapter = new ScheduleAdapter(this.getContext(),R.layout.layout_schedule,arraySchedule);
        Cursor cur =db.GetData("SELECT * FROM Schedule");
        while(cur.moveToNext()) {
            ScheduleObject tem = new ScheduleObject(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3),cur.getString(4),cur.getString(5));
            arraySchedule.add(tem);
            adapter.notifyDataSetChanged();
        }

        listView.setAdapter(adapter);
        return view;
    }

}
