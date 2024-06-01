
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Initializes the RecyclerView with the provided layout manager and adapter.
 *
 * @param layoutManager The layout manager for the RecyclerView.
 * @param adapter The adapter for the RecyclerView.
 */
fun RecyclerView.initRecycler(layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter
    this.layoutManager = layoutManager
}

/**
 * Toggles the visibility of a view based on a loading indicator.
 *
 * @param isShowLoading A flag indicating whether to show the loading indicator.
 * @param container The container view whose visibility will be toggled.
 */
fun View.isVisible(isShowLoading: Boolean, container: View) {
    if (isShowLoading) {
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    } else {
        this.visibility = View.GONE
        container.visibility = View.VISIBLE
    }
}