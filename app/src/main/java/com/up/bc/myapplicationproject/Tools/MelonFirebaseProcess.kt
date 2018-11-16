package th.ac.up.se.takingbear.Tools

import com.google.firebase.database.DataSnapshot

class MelonFirebaseProcess {

    private var isLoad = false
    private var isProcessSnapshot = false
    private var isLastSnapshot = false
    private lateinit var lastSnapshot: DataSnapshot


    fun onLoad(switch: Boolean) {
        this.isLoad = switch
    }

    fun startLoad() {
        this.isLoad = true
    }

    fun stopLoad() {
        this.isLoad = false
    }

    fun isLoad() = isLoad

    fun isInActive() = !isProcessSnapshot && !isLastSnapshot

    fun activeNow() {
        isProcessSnapshot = true

    }

    fun overActive(dataSnapshot: DataSnapshot) {
        lastSnapshot = dataSnapshot
        isLastSnapshot = true
    }

    fun loadOverActive() :DataSnapshot? {
        isLoad = false
        isProcessSnapshot = false

        return if (isLastSnapshot) {
            isLastSnapshot = false
            isProcessSnapshot = true

            lastSnapshot
        } else {
            null
        }
    }

}