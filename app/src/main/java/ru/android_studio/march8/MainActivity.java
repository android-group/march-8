package ru.android_studio.march8;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static String path;
    //private static Tracker mTracker;
    private static VideoView videoView;
    private static ImageView imageView;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AnalyticsApplication application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        path = "android.resource://" + getPackageName() + "/";

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(1);
                } else if (mViewPager.getCurrentItem() == 1) {
                    fab.setVisibility(View.GONE);
                    AskDialogFragment.newInstance().show(getFragmentManager(), "dialog");
                }


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
        return false;
    }

    public static class AskDialogFragment extends DialogFragment {
        public static DialogFragment newInstance() {
            return new AskDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(getContentView());
            Dialog dialog = builder.create();
            return dialog;
        }

        private View getContentView() {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View inflate = inflater.inflate(R.layout.dialog, null);

            Button buttonNo = (Button) inflate.findViewById(R.id.no);
            buttonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("answer")
                            .setAction("no")
                            .build());*/
                    getDialog().hide();
                    Toast.makeText(v.getContext(), "Я всё равно тебя люблю! :P",
                            Toast.LENGTH_SHORT).show();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendAnswer(false);
                }
            });

            Button buttonYes = (Button) inflate.findViewById(R.id.yes);
            buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("answer")
                            .setAction("yes")
                            .build());
                    getDialog().hide();*/
                    Toast.makeText(v.getContext(), "Люблю тебя моя малышка!)",
                            Toast.LENGTH_SHORT).show();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendAnswer(true);
                }
            });

            return inflate;
        }

        private void sendAnswer(boolean isSuccess) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"andreevym@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "be together on whole life");
            String result = "Я не согласна, но может быть еще подумаю?))";
            if (isSuccess) {
                result = "Я согласна! :)";
            }
            i.putExtra(Intent.EXTRA_TEXT, result + new SimpleDateFormat().format(new Date()));

            try {
                startActivity(Intent.createChooser(i, "Отправка email сообщения..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity().getApplication(), "К сожалению у вас не установлен почтовый клиент, мы не можем отправить сообщение.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            imageView = (ImageView) rootView.findViewById(R.id.imageView);

            TextView description = (TextView) rootView.findViewById(R.id.description);
            videoView = (VideoView) rootView.findViewById(R.id.videoView);

            int anInt = getArguments().getInt(ARG_SECTION_NUMBER);
            if (anInt == 1) {
                videoView.setVideoURI(Uri.parse(path + R.raw.trytodo));
                videoView.start();
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                /*mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("slide")
                        .setAction("first")
                        .build());*/
            } else if (anInt == 2) {
                description.setText("Любимая, поздравляю тебя с 8 марта :) Пусть этой солнечной весной почаще будем мы с тобой :)");
                imageView.setImageResource(R.drawable.teddy);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                /*mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("slide")
                        .setAction("second")
                        .build());*/
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
