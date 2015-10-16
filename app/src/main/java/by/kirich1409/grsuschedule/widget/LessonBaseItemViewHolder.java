package by.kirich1409.grsuschedule.widget;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import by.kirich1409.grsuschedule.databinding.ItemLessonBaseBinding;
import by.kirich1409.grsuschedule.model.Lesson;

/**
 * Created by kirillrozov on 10/14/15.
 */
public class LessonBaseItemViewHolder extends RecyclerView.ViewHolder {

    private final ItemLessonBaseBinding mBinding;

    public LessonBaseItemViewHolder(@NotNull View rootView) {
        super(rootView);
        mBinding = ItemLessonBaseBinding.bind(rootView);
    }

    public void bind(Lesson lesson) {
        mBinding.setLesson(lesson);
        mBinding.executePendingBindings();
    }

}
