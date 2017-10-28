package muzimuzi.jejuhackerton.com.muzimuzi.Adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.HomeFragment;
import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.QrcodeFragment;
import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.ScanFragment;
import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.SendFragment;
import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.SettingFragment;

/**
 * Created by hongseung-ui on 2017. 4. 29..
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    FragmentManager fm;
    public Fragment fragmentFirst;
    public MainViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.fm=fm;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                QrcodeFragment tab2 = new QrcodeFragment();
                return tab2;
            case 2:
                ScanFragment tab3 = new ScanFragment();
                return tab3;
            case 3:
                SendFragment tab4 = new SendFragment();
                return tab4;
            case 4:
                SettingFragment tab5 = new SettingFragment();
                return tab5;
            default:

                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_UNCHANGED;
    }

}
