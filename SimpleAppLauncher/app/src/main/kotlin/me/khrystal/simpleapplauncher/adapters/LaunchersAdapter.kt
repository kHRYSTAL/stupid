package me.khrystal.simpleapplauncher.adapters

import android.app.Activity
import android.view.Menu
import android.view.ViewGroup
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter
import com.simplemobiletools.commons.interfaces.ItemTouchHelperContract
import com.simplemobiletools.commons.interfaces.RefreshRecyclerViewListener
import com.simplemobiletools.commons.views.FastScroller
import com.simplemobiletools.commons.views.MyRecyclerView
import me.khrystal.simpleapplauncher.models.AppLauncher

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/11
 * update time:
 * email: 723526676@qq.com
 */
class LaunchersAdapter(activity: BaseSimpleActivity, val launchers: ArrayList<AppLauncher>, val listener: RefreshRecyclerViewListener?,
                       recyclerView: MyRecyclerView, fastScroller: FastScroller, itemClick: (Any) -> Unit) : MyRecyclerViewAdapter(activity, recyclerView, fastScroller, itemClick), ItemTouchHelperContract {

    override fun actionItemPressed(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getActionMenuId(): Int {
        TODO("Not yet implemented")
    }

    override fun getIsItemSelectable(position: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemKeyPosition(key: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getItemSelectionKey(position: Int): Int? {
        TODO("Not yet implemented")
    }

    override fun getSelectableItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onActionModeCreated() {
        TODO("Not yet implemented")
    }

    override fun onActionModeDestroyed() {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun prepareActionMode(menu: Menu) {
        TODO("Not yet implemented")
    }

    override fun onRowClear(myViewHolder: ViewHolder?) {
        TODO("Not yet implemented")
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onRowSelected(myViewHolder: ViewHolder?) {
        TODO("Not yet implemented")
    }

}