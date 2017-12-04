package jms.android.commons.network.Protocol

/**
 * @author D.Noguchi
 * @since 1,Sep.2017
 */
enum class StockTakingStatus(val value:Int) {
    /**  */
    YET(0),
    /** */
    DONE(1),
    /** */
    WAIT(2)
}