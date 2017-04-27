package jp.kanagawa.kawasaki.kitakutter.gridviewsample;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {
    ArrayList<StartModel> mModels;
    Handler mHander = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        GridView gv = (GridView)findViewById(R.id.grid_view);
        mModels = new ArrayList<>();
        for(int i=0;i<4;i++) {
            StartModel model = new StartModel(i, true);
            mModels.add(model);
        }
        StarAdapter ad=new StarAdapter(this, mModels);
        gv.setAdapter(ad);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridView gv = (GridView) findViewById(R.id.grid_view);
                StarAdapter ad = (StarAdapter) gv.getAdapter();
                StartModel model = new StartModel(ad.getCount(), true);
                mModels.add(model);
                ad.notifyDataSetChanged();

                mHander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animator animator = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.scale_big);
                        GridView gv = (GridView) findViewById(R.id.grid_view);
                        StarAdapter ad = (StarAdapter) gv.getAdapter();
                        ImageView im = (ImageView)gv.getChildAt(ad.getCount()-1).findViewById(R.id.star_image);
                        animator.setTarget(im);
                        animator.start();
                }},100);
            }
        });

        FloatingActionButton fab8 = (FloatingActionButton) findViewById(R.id.fab8);
        fab8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 8; i++) {

                    mHander.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GridView gv = (GridView) findViewById(R.id.grid_view);
                            StarAdapter ad = (StarAdapter) gv.getAdapter();
                            StartModel model = new StartModel(ad.getCount(), true);
                            mModels.add(model);
                            ad.notifyDataSetChanged();
                        }
                    },i*200);
                }
            }
        });

        FloatingActionButton clear = (FloatingActionButton) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridView gv = (GridView) findViewById(R.id.grid_view);
                StarAdapter ad = (StarAdapter) gv.getAdapter();
                StartModel model = new StartModel(ad.getCount(), true);
                mModels.clear();
                ad.notifyDataSetChanged();
            }
        });

        FloatingActionButton dropDown = (FloatingActionButton) findViewById(R.id.drop_down);
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartModel model = new StartModel(mModels.size(), true);
                mModels.add(model);
                GridView gv = (GridView) findViewById(R.id.grid_view);
                StarAdapter ad = (StarAdapter) gv.getAdapter();
                ad.notifyDataSetChanged();

                mHander.postDelayed(() -> {
                    //行き先
                    View v = gv.getChildAt(mModels.size()-1);
                    int[] afterLoc = new int[2];
                    v.getLocationOnScreen(afterLoc);
                    float afterX = afterLoc[0]+(v.getRight()-v.getLeft())/2;
                    float afterY = afterLoc[1]+(v.getBottom()-v.getTop())/2;
                    //現在位置
                    ImageView im = (ImageView) findViewById(R.id.original_star_image);
                    int[] beforeLoc = new int[2];
                    im.getLocationOnScreen(beforeLoc);
                    float beforeX = beforeLoc[0]+(im.getRight()-im.getLeft())/2;
                    float beforeY = beforeLoc[1]+(im.getBottom()-im.getTop())/2;
                    //アニメーション(行き)
                    PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("translationX", 0, afterX-beforeX);
                    PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", 0, afterY-beforeY);
                    ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(im, holderX, holderY);
                    //アニメーション(帰り)
                    PropertyValuesHolder BackholderX = PropertyValuesHolder.ofFloat("translationX", afterX-beforeX, 0);
                    PropertyValuesHolder BackholderY = PropertyValuesHolder.ofFloat("translationY", afterY-beforeY, 0);
                    ObjectAnimator backOa = ObjectAnimator.ofPropertyValuesHolder(im, BackholderX, BackholderY);
                    AnimatorSet set = new AnimatorSet();
                    set.setDuration(1000);
                    set.playSequentially(oa,backOa);
                    set.start();
                },100);



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class StarAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<StartModel> mModels;

        public StarAdapter(Context c, ArrayList<StartModel> models) {
            mContext = c;
            mModels=models;
        }

        public int getCount() {
            return mModels.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                findViewById(R.id.grid_view);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.star, null);
                ImageView im = (ImageView)convertView.findViewById(R.id.star_image);
                im.setImageDrawable(getDrawable(R.drawable.ic_mood_red_24dp));
            }

            return convertView;
        }
    }
    public class StartModel{
        int mId;
        boolean flag=false;
        StartModel(int id,boolean f){
            mId=id;
            flag=f;
        }

        public int getmId() {
            return mId;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setId(int id) {
            this.mId = id;
        }

        public void setFlag(boolean f) {
            this.flag = f;
        }
    }
}
