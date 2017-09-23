package com.thomascook.dreamcatcher.application.recyclerview

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.thomascook.core.recyclerview.RecyclerViewBase
import com.thomascook.dreamcatcher.Model
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomascook.dreamcatcher.R
import com.thomascook.dreamcatcher.databinding.CellDreamBinding

/**
 * Classes for managing dream cells
 */
object CellDream {

    const val VIEW_ID_DARK = R.id.cell_list_dream_dark

    class ViewModel(internal val dream: Model.Dream) : BaseObservable(), RecyclerViewBase.ViewModelBase {

        override val renderWithViewTypeId: Int
            get() = VIEW_ID_DARK

        // Reference to view holder's context used for accessing android resources
        internal var context: Context? = null

        val time: CharSequence
            @Bindable get() {
                val _context = context ?: return ""
                val dateFormatter = DateFormat.getTimeFormat(_context)
                return dateFormatter.format(dream.time)
            }

        val name: String
            @Bindable get() {
                return dream.name
            }
    }

    interface Listener {
        // Put cell callbacks in here
    }

    private class ViewHolder(private val viewBinding: CellDreamBinding, private val listener: Listener?) :
            RecyclerViewBase.ViewHolderBase(viewBinding.root), ViewHolderCallback {

        private var model: ViewModel? = null

        override fun bindAdapterData(data: RecyclerViewBase.ViewModelBase) {
            // Clear context from previous model
            var _model = model
            if (_model != null) {
                _model.context = null
            }
            _model = data as ViewModel
            _model.context = viewBinding.root.context
            viewBinding.data = _model
            viewBinding.callback = this
            model = _model
        }

        override fun onDreamClicked(view: View) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    /**
     * Interface for processing dream clicks
     */
    interface ViewHolderCallback {
        fun onDreamClicked(view: View)
    }

    /**
     * Singleton used to create cell layout that uses dark theme
     */
    object CreatorDark : RecyclerViewBase.ViewHolderBaseCreator<RecyclerViewBase.ViewHolderBase> {

        override fun createViewHolder(recycleView: ViewGroup, callback: RecyclerViewBase.ViewHolderCallback?): RecyclerViewBase.ViewHolderBase {
            val context = android.view.ContextThemeWrapper(recycleView.context, R.style.ThemeOverlay_Cell_Dark)
            val viewBinding = CellDreamBinding.inflate(LayoutInflater.from(context), recycleView, false)
            return ViewHolder(viewBinding, callback as? Listener)
        }
    }
}