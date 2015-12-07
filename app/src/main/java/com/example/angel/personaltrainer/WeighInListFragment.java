package com.example.angel.personaltrainer;


import android.app.ListFragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeighInListFragment extends ListFragment {
    private ArrayList<WeighIn> mWeighIns;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mWeighIns = WeighInManager.get(getActivity()).getWeighIns();

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
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_item_new_weighin:
                WeighIn w = new WeighIn();
                WeighInManager.get(getActivity()).addWeighIn(w);
                Intent i = new Intent(getActivity(), WeighInActivity.class);
                i.putExtra(WeighInFragment.EXTRA_WEIGHIN_DATE, w.getDate());
                startActivityForResult(i, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class WeighInAdapter extends ArrayAdapter<WeighIn>{

        public WeighInAdapter(ArrayList<WeighIn> weighIns){
            super(getActivity(), 0, weighIns);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_weighin, null);
            }

            WeighIn w = getItem(position);

            ImageView photoImageView = (ImageView)convertView.findViewById(R.id.list_weighin_picture);
            showPhoto(photoImageView, w);

            TextView weightTextView = (TextView)convertView.findViewById(R.id.list_weighin_weight);
            weightTextView.setText(String.valueOf(w.getWeight()) + " lbs");

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

}
