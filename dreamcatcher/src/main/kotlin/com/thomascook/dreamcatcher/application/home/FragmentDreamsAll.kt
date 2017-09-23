package com.thomascook.dreamcatcher.application.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomascook.core.recyclerview.RecyclerDiffCallback
import com.thomascook.core.recyclerview.RecyclerViewBase
import com.thomascook.core.utils.ViewUtils
import com.thomascook.dreamcatcher.AppUtils
import com.thomascook.dreamcatcher.EventReporter
import com.thomascook.dreamcatcher.Model
import com.thomascook.dreamcatcher.R
import com.thomascook.dreamcatcher.application.TiFragmentBase
import com.thomascook.dreamcatcher.application.TiPresenterHolder
import com.thomascook.dreamcatcher.application.recyclerview.CellDream
import com.thomascook.dreamcatcher.databinding.FragmentDreamsAllBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.grandcentrix.thirtyinch.TiView
import java.util.*

private const val TAG = "DreamsAll"

/**
 * Array that contains cell view holder creators used by the recycler view
 */
private val VIEW_HOLDER_CREATORS = SparseArray<RecyclerViewBase.ViewHolderBaseCreator<RecyclerViewBase.ViewHolderBase>>().apply {
    put(CellDream.VIEW_ID_DARK, CellDream.CreatorDark)
}

//Recycler view adapter
private class RecyclerViewAdapter : RecyclerViewBase.ViewAdapter<RecyclerViewBase.ViewHolderBase>(
        VIEW_HOLDER_CREATORS, mutableListOf())

class FragmentDreamsAll : TiFragmentBase<TiPresenterHolder, TiView>(), TiView {

    //Reference to fragment's view binding
    private var viewBinding: FragmentDreamsAllBinding? = null

    private var diffDisposable: Disposable? = null

    override fun providePresenter(): TiPresenterHolder =
            TiPresenterHolder()

    override fun provideView(): TiView = this

    /**
     * Interface that notifies of events that should be processed outside this class
     */
    interface Listener {
        fun onFragmentBookingsAllRefresh()
    }

    override fun onDestroyView() {
        viewDisposables.clear()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        ViewUtils.saveRecyclerViewPosition(outState, viewBinding?.recyclerView)
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val _viewBinding = FragmentDreamsAllBinding.inflate(inflater ?: LayoutInflater.from(context))
        _viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        _viewBinding.recyclerView.setHasFixedSize(true)
        //_viewBinding.recyclerView.addItemDecoration(VerticalItemDecoration(context))

        //Set swipe refresh listener
        _viewBinding.swipeRefresh.setOnRefreshListener {
            val listener = activity
            if (listener is Listener)
                listener.onFragmentBookingsAllRefresh()
        }

        val recyclerAdapter = RecyclerViewAdapter()
        _viewBinding.recyclerView.adapter = recyclerAdapter

        _viewBinding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)

        viewBinding = _viewBinding
        return _viewBinding.root
    }

    fun showDreams(dreams: Collection<Model.Dream>) {

        val _viewBinding = viewBinding ?: return
        val _adapter = _viewBinding.recyclerView.adapter as RecyclerViewAdapter? ?: return

        // Cancel pending subscription
        val currentDisposable = diffDisposable
        if (currentDisposable?.isDisposed == false) {
            currentDisposable.dispose()
        }

        if (dreams.isEmpty()) {
            showNoDreams(true)
            _adapter.setData(Collections.emptyList())
        } else {
            val newList = ArrayList<RecyclerViewBase.ViewModelBase>()

            dreams.forEach { dream ->
                // Add dark cell
                newList.add(CellDream.ViewModel(dream))
            }

            var _diffDisposable: Disposable? = null
            _diffDisposable = Observable.fromCallable {
                DiffUtil.calculateDiff(RecyclerDiffCallback(_adapter.list, newList), true)
            }.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ diffResult ->
                        AppUtils.removeDisposable(viewDisposables, _diffDisposable)
                        _adapter.setData(newList)

                        showNoDreams(false)

                        diffResult.dispatchUpdatesTo(_adapter)

                        ViewUtils.restoreRecyclerViewPosition(this.mStateBundle, _viewBinding.recyclerView)

                    }, { error ->
                        AppUtils.removeDisposable(viewDisposables, _diffDisposable)
                        EventReporter.d(TAG, "Failed to calculate recycler diff", error)
                    })

            AppUtils.addDisposable(viewDisposables, _diffDisposable)

            diffDisposable = _diffDisposable
        }
    }

    // Call to show/hide loading progress
    private fun showNoDreams(show: Boolean) {

        val _viewBinding = viewBinding ?: return

        val animSet = AnimatorSet()
        if (show) {
            if (_viewBinding.imageLogo.visibility != View.VISIBLE) {
                animSet.playTogether(
                        ObjectAnimator.ofFloat(_viewBinding.imageLogo, "alpha", 0.0f, 1.0f),
                        ObjectAnimator.ofFloat(_viewBinding.emptyText, "alpha", 0.0f, 1.0f),
                        ObjectAnimator.ofFloat(_viewBinding.recyclerView, "alpha", 1.0f, 0.0f))
                animSet.addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        _viewBinding.swipeRefresh.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        _viewBinding.imageLogo.visibility = View.VISIBLE
                        _viewBinding.emptyText.visibility = View.VISIBLE
                    }
                })
            }
        } else {
            if (_viewBinding.imageLogo.visibility == View.VISIBLE) {
                animSet.playTogether(
                        ObjectAnimator.ofFloat(_viewBinding.imageLogo, "alpha", 1.0f, 0.0f),
                        ObjectAnimator.ofFloat(_viewBinding.emptyText, "alpha", 1.0f, 0.0f),
                        ObjectAnimator.ofFloat(_viewBinding.recyclerView, "alpha", 0.0f, 1.0f))
                animSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        _viewBinding.imageLogo.visibility = View.GONE
                        _viewBinding.emptyText.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animator) {
                        _viewBinding.swipeRefresh.visibility = View.VISIBLE
                    }
                })
            }
        }

        animSet.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        animSet.start()
    }
}