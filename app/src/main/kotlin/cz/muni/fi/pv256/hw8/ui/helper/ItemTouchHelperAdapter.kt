package cz.muni.fi.pv256.hw8.ui.helper

interface ItemTouchHelperAdapter {

    /**
     * Called when the item is swiped away.
     *
     * @param position the item position
     */
    fun onItemDismiss(position: Int)
}
