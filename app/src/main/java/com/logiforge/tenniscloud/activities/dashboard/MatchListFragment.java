package com.logiforge.tenniscloud.activities.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.db.MatchTbl;
import com.logiforge.tenniscloud.model.Match;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Match>> listDataChild;

    public MatchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchListFragment newInstance(String param1, String param2) {
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_dashboard_frag_match_list, container, false);

        ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.match_list);

        // preparing list data
        prepareListData();

        MatchListAdapter listAdapter = new MatchListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            expListView.expandGroup(i);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMatchListInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMatchListInteraction(Uri uri);
    }

    protected void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Match>>();

        MatchTbl matchTbl = new MatchTbl();
        List<Match> matches = matchTbl.getAll();
        LocalDate today = LocalDate.now();
        LocalDate todayPlusEight = LocalDate.now().plusDays(8);
        for(Match match : matches) {
            if(match.getScheduledDt().equals(today)) {
                List<Match> todayList = listDataChild.get("Scheduled Today");
                if(todayList == null) {
                    todayList = new ArrayList<Match>();
                    listDataChild.put("Scheduled Today", todayList);
                }
                todayList.add(match);
            } else if(match.getScheduledDt().isAfter(today) && match.getScheduledDt().isBefore(todayPlusEight) ) {
                List<Match> nextSevenDaysList = listDataChild.get("Scheduled Next 7 Days");
                if(nextSevenDaysList == null) {
                    nextSevenDaysList = new ArrayList<Match>();
                    listDataChild.put("Scheduled Next 7 Days", nextSevenDaysList);
                }
                nextSevenDaysList.add(match);
            }
        }

        if(listDataChild.containsKey("Scheduled Today")) {
            listDataHeader.add("Scheduled Today");
        }

        if(listDataChild.containsKey("Scheduled Next 7 Days")) {
            listDataHeader.add("Scheduled Next 7 Days");
        }
    }
}
