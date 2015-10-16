package by.kirich1409.grsuschedule.widget;

import android.content.Context;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder;
import by.kirich1409.grsuschedule.databinding.ItemFreeTimeBinding;
import by.kirich1409.grsuschedule.schedule.FreeTime;

/**
 * Created by kirillrozov on 10/10/15.
 */
public class FreeTimeViewHolder extends BaseRecyclerItemViewHolder {

    final ItemFreeTimeBinding mBinding;

    public FreeTimeViewHolder(@NotNull Context context, @NotNull View rootView) {
        super(context, rootView);
        mBinding = ItemFreeTimeBinding.bind(rootView);
    }

    public void setFreeTime(FreeTime freeTime) {
        mBinding.setFreeTime(freeTime);
        mBinding.executePendingBindings();
    }
}
