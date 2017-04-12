package com.example.pc.folms;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String[] mCatTitles;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private ListView mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DrawerLayout");

        mTitle = mDrawerTitle = getTitle();
        mCatTitles = getResources().getStringArray(R.array.cats_array_ru);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        // Тень при открытии панели. По желанию
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // подключим адаптер для списка
        mDrawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCatTitles));
        // установим слушатель для щелчков по элементам списка
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        // Включаем значок у ActionBar для управления выдвижной панелью щелчком
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* значок-гамбургер для замены стрелки 'Up' */
                R.string.drawer_open,  /* добавьте строку "open drawer" - описание для  accessibility */
                R.string.drawer_close  /* добавьте "close drawer" - описание для accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    //  Слушатель для элементов списка в выдвижной панели
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Обновляем содержимое экрана, заменяя фрагменты
        Fragment fragment = new CatFragment();
        Bundle args = new Bundle();
        args.putInt(CatFragment.ARG_CAT_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // обновим выбранный элемент списка и закрываем панель
        mDrawerListView.setItemChecked(position, true);
        setTitle(mCatTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        }else {
            setTitle(mTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListView);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this cat
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Фрагмент, который появится в основной части разметки и покажет кота
     */
    public static class CatFragment extends Fragment {
        public static final String ARG_CAT_NUMBER = "cat_number";

        public CatFragment() {
            // Для фрагмента требуется пустой конструктор
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cat, container, false);
//            int i = getArguments().getInt(ARG_CAT_NUMBER);
//            // имена котов на англ. для нахождения имен файлов
//            String catName = getResources().getStringArray(R.array.cats_array)[i];
//
//            String catNameTitle = getResources().getStringArray(R.array.cats_array_ru)[i];
//
//            int imageId = getResources().getIdentifier(catName.toLowerCase(Locale.ROOT),
//                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.imageViewCat)).setImageResource(R.drawable.qwe);
//            getActivity().setTitle(catNameTitle);
            return rootView;
        }
    }
}