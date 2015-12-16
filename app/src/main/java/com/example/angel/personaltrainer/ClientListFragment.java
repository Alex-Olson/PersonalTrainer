package com.example.angel.personaltrainer;


import android.app.ListFragment;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClientListFragment extends ListFragment
//display/add/edit/remove clients.
{
    private ArrayList<Client> mClients;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mClients = ClientManager.get(getActivity()).getClients();
        ClientAdapter adapter = new ClientAdapter(mClients);
        setListAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_client_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        ClientAdapter adapter = (ClientAdapter)getListAdapter();
        Client client = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_client:
                ClientManager.get(getActivity()).deleteClient(client);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.menu_item_edit_client:
                Intent i = new Intent(getActivity(), ClientActivity.class);
                i.putExtra(ClientFragment.EXTRA_CLIENT_ID, client.getId().toString());
                startActivityForResult(i, 0);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_item_new_client:
                Client c = new Client();

                ClientManager.get(getActivity()).addClient(c);
                Intent i = new Intent(getActivity(), ClientActivity.class);
                i.putExtra(ClientFragment.EXTRA_CLIENT_ID, c.getId().toString());
                startActivityForResult(i, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.client_list_item_context, menu);
    }

    @Override
    public void onListItemClick(ListView l ,View v, int position, long id){
        Client c = (Client) (getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), WeighInListActivity.class);
        i.putExtra(WeighInListFragment.EXTRA_CLIENT_ID, c.getId().toString());
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
