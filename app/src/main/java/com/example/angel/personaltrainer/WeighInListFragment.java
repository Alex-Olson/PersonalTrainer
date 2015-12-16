package com.example.angel.personaltrainer;


import android.app.ListFragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

public class WeighInListFragment extends ListFragment
//displays weigh-in date/weight/photo thumbnail, and allows you to edit/delete them. small glitch in
    //deleting them; the listview doesn't populate correctly on deletion but it fixed after exiting and reentering listview. probably has something
    //to do with how i had to use the adapter to only display only the weigh-ins of the certain client.
{
    private ArrayList<WeighIn> mWeighIns;
    public static final String EXTRA_CLIENT_ID = "extra client id weigh in list";
    public UUID clientId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        clientId = UUID.fromString(getArguments().getSerializable(EXTRA_CLIENT_ID).toString());

        setHasOptionsMenu(true);
        mWeighIns = WeighInManager.get(getActivity(), clientId).getWeighIns();

        WeighInAdapter adapter = new WeighInAdapter(mWeighIns);
        setListAdapter(adapter);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weighin_list, menu);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        ((WeighInAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l ,View v, int position, long id){
        WeighIn w = (WeighIn) (getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), WeighInActivity.class);
        i.putExtra(WeighInFragment.EXTRA_WEIGHIN_DATE, w.getDate());
        i.putExtra(WeighInFragment.EXTRA_CLIENT_ID, w.getClientId().toString());
        startActivity(i);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.weighin_list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_item_new_weighin:
                WeighIn w = new WeighIn();
                w.setClientId(clientId);
                WeighInManager.get(getActivity(), clientId).addWeighIn(w);
                Intent i = new Intent(getActivity(), WeighInActivity.class);
                i.putExtra(WeighInFragment.EXTRA_WEIGHIN_DATE, w.getDate());
                i.putExtra(WeighInFragment.EXTRA_CLIENT_ID, w.getClientId().toString());
                startActivityForResult(i, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        WeighInAdapter adapter = (WeighInAdapter)getListAdapter();
        WeighIn weighIn = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_weighin:
                WeighInManager.get(getActivity(), clientId).deleteWeighIn(weighIn);
                adapter.notifyDataSetChanged();
                return true;
        }
        return  super.onContextItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ListView listview = (ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listview);
        return v;
    }

    private class WeighInAdapter extends ArrayAdapter<WeighIn>{

        public WeighInAdapter(ArrayList<WeighIn> weighIns){
            super(getActivity(), 0, weighIns);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        //In order to fix the problem I described in the WeighInManager, I inflated a completely blank view
                //for each object in the json file that didnt match the client id of the client. Probably introduced the bug I mentioned above.
        {


                if (convertView == null) {


                    if (getItem(position).getClientId().equals(clientId)) {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_weighin, null);
                        WeighIn w = getItem(position);

                        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.list_weighin_picture);
                        showPhoto(photoImageView, w);

                        TextView weightTextView = (TextView) convertView.findViewById(R.id.list_weighin_weight);
                        weightTextView.setText(String.valueOf(w.getWeight()) + " lbs");

                        TextView dateTextView = (TextView) convertView.findViewById(R.id.list_weighin_date);
                        dateTextView.setText(w.getDate().toString());

                    } else {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_blank, null);
                    }
                }


                return convertView;

        }

        private void showPhoto(ImageView v, WeighIn w){
            Photo p = w.getPhoto();
            BitmapDrawable b = null;
            if (p != null) {
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                b = PictureUtils.getScaledDrawable(getActivity(), path);
            }
            v.setImageDrawable(b);
        }
    }


    public static WeighInListFragment newInstance(UUID clientId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CLIENT_ID, clientId.toString());

        WeighInListFragment fragment = new WeighInListFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
