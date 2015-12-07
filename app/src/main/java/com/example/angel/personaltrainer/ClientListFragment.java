package com.example.angel.personaltrainer;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClientListFragment extends ListFragment {
    private ArrayList<Client> mClients;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ClientAdapter adapter = new ClientAdapter(mClients);
        setListAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_client_list, menu);
    }

    @Override
    public void onListItemClick(ListView l ,View v, int position, long id){
        Client c = (Client) (getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), WeighInListActivity.class);
        //i.putExtra(WeighInListFragment.EXTRA_CLIENT_ID, c.getId());
        startActivity(i);

    }

    @Override
    public void onResume()
    {
        super.onResume();
       ((ClientAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class ClientAdapter extends ArrayAdapter<Client>{
        public ClientAdapter(ArrayList<Client> clients){
            super(getActivity(), 0, clients);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_client, null);
            }
            Client c = getItem(position);

            TextView clientName = (TextView)convertView.findViewById(R.id.list_client_name);
            clientName.setText(c.getName());

            TextView clientEmail = (TextView)convertView.findViewById(R.id.list_client_email);
            clientEmail.setText(c.getEmail());

            return convertView;
        }
    }


}
