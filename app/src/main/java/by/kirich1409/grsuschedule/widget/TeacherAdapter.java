package by.kirich1409.grsuschedule.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import by.kirich1409.grsuschedule.R;
import by.kirich1409.grsuschedule.databinding.ItemTeacherBinding;
import by.kirich1409.grsuschedule.model.Teacher;

/**
 * Created by kirillrozov on 10/8/15.
 */
public class TeacherAdapter extends BaseAdapter implements SectionIndexer {

    private final Context mContext;
    private final Data mData;

    public TeacherAdapter(Context context, Data data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.teachers.length;
    }

    @Override
    public Teacher getItem(int position) {
        return mData.teachers[position];
    }

    @Override
    public long getItemId(int position) {
        return mData.teachers[position].getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemTeacherBinding binding;
        if (convertView == null) {
            binding = ItemTeacherBinding.inflate(LayoutInflater.from(mContext), parent, false);
            binding.getRoot().setTag(R.id.view_holder, binding);
        } else {
            binding = (ItemTeacherBinding) convertView.getTag(R.id.view_holder);
        }
        binding.setTeacher(mData.teachers[position]);
        return binding.getRoot();
    }

    @Override
    public Object[] getSections() {
        return mData.sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mData.positionForSection[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        return mData.sectionForPosition[position];
    }

    public static class Data {
        Object[] sections;
        int[] sectionForPosition;
        int[] positionForSection;
        Teacher[] teachers;

        public Data(Object[] sections,
                    int[] sectionForPosition,
                    int[] positionForSection,
                    Teacher[] teachers) {
            this.sections = sections;
            this.sectionForPosition = sectionForPosition;
            this.positionForSection = positionForSection;
            this.teachers = teachers;
        }
    }
}
