package com.example.jemmycalak.thisismymarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jemmycalak.thisismymarket.Adapter.ViewPagerAdapter;
import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.fragment.FragmentProduct;
import com.example.jemmycalak.thisismymarket.interfacesComunicator.Comunicator;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.view.AboutActivity;
import com.example.jemmycalak.thisismymarket.view.DaftarPesanan;
import com.example.jemmycalak.thisismymarket.view.DetailProduct;
import com.example.jemmycalak.thisismymarket.view.Keranjang;
import com.example.jemmycalak.thisismymarket.view.Login;
import com.example.jemmycalak.thisismymarket.view.ProfileActivity;
import com.example.jemmycalak.thisismymarket.view.Register;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Comunicator {

    private Context c;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle toogle;
    private NavigationView navigationView;
    private ViewPagerAdapter fragmentAdapter;
    private List<object_product> fragmentOne;


    //    private BottomNavigationView bottomNavigationView;
    private Boolean exit = false;
    private String[] tabs = {"Category", "Product"};

    //for fragment
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private RelativeLayout layout_home;

    //for comunicator to fragment
    private Comunicator comunicator;

    //    Searchview
    private SearchView searchView;

    //  belum digunain
    private CircleImageView imageProfile;
    private TextView nm, eml, point;

    //  untuj session user Login
    private userSharedPreference session;
    private Snackbar snackbar;
    private AlertDialog.Builder builder, builder1;

    private SearchBox searchBox;
    private SearchResult searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment();
        initUtil();

        //session Login
        session = new userSharedPreference(getApplicationContext());
        setingDrawer();
        setHeaderDrawable();
        getToken();
        setSearchBox();

    }

    private void setSearchBox() {
        fragmentOne = new ArrayList<>();

        searchBox = (SearchBox) findViewById(R.id.searchbox);
        searchBox.setLogoText("Search item..");


        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int id = item.getItemId();
                    if (id == R.id.search) {
//                        mViewPager.setVisibility(View.GONE);
//                        tabLayout.setVisibility(View.GONE);

                        displaySearchResult();
                    } else if (id == R.id.cart) {
                        //cart button
                        if (session.checkLogin()) {
                            notif("Opps anda belum login");
                            snackbar.setAction("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(MainActivity.this, Login.class));
                                }
                            });
                            snackbar.setActionTextColor(Color.CYAN);
                            onPause();
                        } else {
                            startActivity(new Intent(MainActivity.this, Keranjang.class));
                        }
                    }

                    return true;
                }
            });
        }
    }

    private void displaySearchResult() {
        searchBox.revealFromMenuItem(R.id.search, MainActivity.this);
        searchBox.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {
                for (int i = 0; i < fragmentOne.size(); i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        searchResult = new SearchResult(fragmentOne.get(i).getNama(), getResources().getDrawable(R.drawable.ic_history_24dp));
                    }
                    searchBox.addSearchable(searchResult);
                }
            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {
                searchBox.hideCircularly(MainActivity.this);
//                mViewPager.setVisibility(View.VISIBLE);
//                tabLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchTermChanged(String s) {

            }

            @Override
            public void onSearch(String s) {

                Log.d("data search", "====>" + s);

                for (int a = 0; a < fragmentOne.size(); a++) {

                    if (s.toLowerCase().contains(fragmentOne.get(a).getNama().toLowerCase())) {
                        Intent i = new Intent(MainActivity.this, DetailProduct.class);
                        i.putExtra("id_k", fragmentOne.get(a).getId());
                        i.putExtra("image_k", fragmentOne.get(a).getImgUrl());
                        i.putExtra("nama_k", fragmentOne.get(a).getNama());
                        i.putExtra("desc_k", fragmentOne.get(a).getDesc());
                        i.putExtra("hrg_k", fragmentOne.get(a).getHrg());
                        i.putExtra("clr_k", fragmentOne.get(a).getColor());
                        i.putExtra("brt_k", fragmentOne.get(a).getBrt());
                        i.putExtra("stock", fragmentOne.get(a).getStock());
                        startActivity(i);
                    }

                }
            }

            @Override
            public void onResultClick(SearchResult searchResult) {
                Log.d("Result clicked", "===>" + searchResult);
                for (int a = 0; a < fragmentOne.size(); a++) {
                    if (searchResult.toString().toLowerCase().contains(fragmentOne.get(a).getNama().toLowerCase())) {
                        Intent i = new Intent(MainActivity.this, DetailProduct.class);
                        i.putExtra("id_k", fragmentOne.get(a).getId());
                        i.putExtra("image_k", fragmentOne.get(a).getImgUrl());
                        i.putExtra("nama_k", fragmentOne.get(a).getNama());
                        i.putExtra("desc_k", fragmentOne.get(a).getDesc());
                        i.putExtra("hrg_k", fragmentOne.get(a).getHrg());
                        i.putExtra("clr_k", fragmentOne.get(a).getColor());
                        i.putExtra("brt_k", fragmentOne.get(a).getBrt());
                        i.putExtra("stock", fragmentOne.get(a).getStock());
                        startActivity(i);
                    }
                }
            }
        });
    }

    private void initUtil() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder1 = new AlertDialog.Builder(MainActivity.this);

    }

    public void getToken() {
        String token;
        HashMap<String, String> hashMap = session.getUserDetail();
        token = hashMap.get(userSharedPreference.KEY_TOKEN_FIREBASE);
        Log.d("Token firebase", "=====>" + token);
    }

    public void setFragment() {

//        Inisialisatio
        mViewPager = (ViewPager) findViewById(R.id.frame_container);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        fragmentAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(mViewPager);

//        if(mViewPager.getCurrentItem() == 0){
//            Fragment fragment = fragmentAdapter.getRegistrasionFragment(1);
//            ((FragmentProduct)fragment).refreshProduct();
//        }

    }

    private void setingDrawer() {

        //panggil toolbar dar layout/body
        toolbar = (Toolbar) findViewById(R.id.tolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        //memanggil tombol dari menu/menu_nav_view
        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

//        buat session Login untuk navigation menu
        if (!session.isUserLoggedIn()) {
            navigationView.inflateMenu(R.menu.menu_nav_blm_login);
        } else {
            navigationView.inflateMenu(R.menu.menu_nav_sdh_login);
        }
        //panggil drawer
        drawer = (DrawerLayout) findViewById(R.id.drawerL);
        toogle = new ActionBarDrawerToggle(this, drawer, R.string.opendrawer, R.string.closedrawer); //erorr (R.string.opendrawer, R.string.closedrawer)tambahkan scrip di asd/string

        //memasukan toogle ke drawer
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        //memanggil tombol homme dan back pada navigation drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setHeaderDrawable() {
        //      setValue header
        View header = navigationView.getHeaderView(0);
        layout_home = (RelativeLayout) header.findViewById(R.id.layout_home);
        nm = (TextView) header.findViewById(R.id.h_nama);
        eml = (TextView) header.findViewById(R.id.h_email);

        layout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.checkLogin()) {
                    notif("Ooops anda belum login.");
                } else {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        });

        if (session.checkLogin()) {
            //belum login
//            notif("Ooops anda belum login.");
        } else {
            HashMap<String, String> hashMap = session.getUserDetail();
            hashMap.get(userSharedPreference.KEY_TOKEN_FIREBASE);
            String nmN, emlN, pointN;
            nmN = hashMap.get(userSharedPreference.KEY_NAME);
            emlN = hashMap.get(userSharedPreference.KEY_EMAIL);
//
            nm.setText(nmN);
            eml.setText(emlN);
//        point.setText(pointN);
//        Picasso.with(MainActivity.this).load("link image").error(R.drawable.oranges).placeholder(R.drawable.placeholder).into(imageProfile);
//        Glide.with(this)
//                .load("link url")
//                .placeholder(R.drawable.oranges)
//                .crossFade()
//                .into(imageProfile);

        }
    }

    //untuk memfungsikan tombol menu sebelah kanan atas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
//
        return super.onOptionsItemSelected(item);
    }

    //untuk tombol back ketika navigation di klick
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            alert("Apakah kamu ingin keluar ?", 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //    menu sebelah atas
        //setSearch from google
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);

//        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView =(SearchView)menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        setSearchView();

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_login) {
            startActivity(new Intent(MainActivity.this, Login.class));
        } else if (id == R.id.menu_register) {
            startActivity(new Intent(MainActivity.this, Register.class));
        } else if (id == R.id.menu_share) {
            shareTo();
        } else if (id == R.id.menu_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.menu_pesanan) {
            startActivity(new Intent(MainActivity.this, DaftarPesanan.class));
        } else if (id == R.id.menu_keranjang) {
            startActivity(new Intent(MainActivity.this, Keranjang.class));
        } else if (id == R.id.menu_logout) {
            alert("Apakah anda ingi logout ?", 1);
        } else if (id == R.id.cart) {
            startActivity(new Intent(MainActivity.this, Keranjang.class));
        }

        // untuk set menandai menu NavigationView yang di pilih
        item.setCheckable(true);
        //set actionBar dengan judul menu item yang di pilih
        //setTitle(item.getTitle());

        //close the navigation Drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareTo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "This is text to send.");
        intent.setType("text/plain");
        // intent.setPackage("com.whatsapp");
        startActivity(intent);
    }

    private void notif(String pesan) {
        snackbar = Snackbar.make(drawer, pesan, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void alert(String pesan, int code) {
        switch (code) {
            case 0:
                builder.setTitle("Notification");
                builder.setMessage(pesan);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.gc();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case 1:
                builder1.setMessage(pesan);
                builder1.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        session.logoutUser();
                        finish();
                    }
                });
                builder1.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
        }
    }

    @Override
    protected void onRestart() {
        //////////////////////check user login, jika sudah login makan restart main activity untuk mengganti tampilan main activity/////////////////////
        if (!session.checkLogin()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        super.onRestart();
    }

    //requared search data
    @Override
    public void sendListData(String id, String name, String hrga, String imgUrl, String descrip, String brt, String clr, String stock) {
        fragmentOne.add(new object_product(id, name, hrga, imgUrl, descrip, brt, clr, stock));

//        Log.d("nama product", "===>" + name);
    }

    @Override
    public void filterDataProduct(String name) {
        mViewPager.setCurrentItem(1);
        //call method fragment product
        Fragment fragment = fragmentAdapter.getRegistrasionFragment(1);
        ((FragmentProduct)fragment).filterData(name);
    }

}
