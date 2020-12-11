package me.khrystal.simpleapplauncher.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplemobiletools.commons.extensions.applyColorFilter
import com.simplemobiletools.commons.extensions.beInvisibleIf
import kotlinx.android.synthetic.main.item_app_launcher.view.*
import me.khrystal.simpleapplauncher.R
import me.khrystal.simpleapplauncher.extensions.config
import me.khrystal.simpleapplauncher.models.AppLauncher
import java.util.*

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/11
 * update time:
 * email: 723526676@qq.com
 */
class LaunchersDialogAdapter(activity: Activity, val launchers: ArrayList<AppLauncher>) : RecyclerView.Adapter<LaunchersDialogAdapter.ViewHolder>() {

    private val config = activity.config
    private var primaryColor = config.primaryColor
    private var textColor = config.textColor
    private var selectedKeys = HashSet<Int>()

    fun toggleItemSelection(select: Boolean, pos: Int) {
        val itemKey = launchers.getOrNull(pos)?.packageName?.hashCode() ?: return

        if (select) {
            selectedKeys.add(itemKey)
        } else {
            selectedKeys.remove(itemKey)
        }

        notifyItemChanged(pos)
    }

    fun getSelectedLaunchers() = launchers.filter { selectedKeys.contains(it.packageName.hashCode()) } as java.util.ArrayList<AppLauncher>

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(launchers[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_launcher, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = launchers.size

    private fun isKeySelected(key: Int) = selectedKeys.contains(key)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(launcher: AppLauncher): View {
            val isSelected = isKeySelected(launcher.packageName.hashCode())
            itemView.apply {
                launcher_check?.beInvisibleIf(!isSelected)
                launcher_label.text = launcher.title
                launcher_label.setTextColor(textColor)
                launcher_icon.setImageDrawable(launcher.drawable!!)

                if (isSelected) {
                    launcher_check?.background?.applyColorFilter(primaryColor)
                }

                setOnClickListener { viewClicked(launcher) }
                setOnLongClickListener { viewClicked(launcher); true }
            }

            return itemView
        }

        private fun viewClicked(launcher: AppLauncher) {
            val isSelected = selectedKeys.contains(launcher.packageName.hashCode())
            toggleItemSelection(!isSelected, adapterPosition)
        }
    }
}