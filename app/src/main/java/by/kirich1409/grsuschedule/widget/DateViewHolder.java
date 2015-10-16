package by.kirich1409.grsuschedule.widget;

import android.content.Context;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder;
import by.kirich1409.grsuschedule.databinding.ItemDateBinding;
import by.kirich1409.grsuschedule.utils.LocalDate;

/**
 * Created by kirillrozov on 10/10/15.
 */
public class DateViewHolder extends BaseRecyclerItemViewHolder {

    final ItemDateBinding mBinding;

    public DateViewHolder(@NotNull Context context, @NotNull View rootView) {
        super(context, rootView);
        mBinding = ItemDateBinding.bind(rootView);
    }

    public void setDate(LocalDate date) {
        mBinding.setDate(date);
        mBinding.executePendingBindings();
    }
}
