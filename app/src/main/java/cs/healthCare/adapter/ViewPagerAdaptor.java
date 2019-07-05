package cs.healthCare.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cs.healthCare.fragment.ExPartFragment;
import cs.healthCare.fragment.ExRoutinFragment;


public class ViewPagerAdaptor extends FragmentPagerAdapter {

    private static int Num_ITEMS = 2;
    String[] titles = {"운동","운동루틴"};

    public ViewPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount(){
        return Num_ITEMS; //여기서 리턴하는 숫자가 페이지 갯수
    }

    @Override
    public Fragment getItem(int position){

        switch (position){
            case 0:
                return ExPartFragment.newInstance(0,"Page#1");
            case 1:
                return ExRoutinFragment.newInstance(1,"Page#2");

            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) { //탭 이름 바꾸는 곳
        return titles[position];
    }
}
