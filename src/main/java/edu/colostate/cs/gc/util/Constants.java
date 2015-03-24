package edu.colostate.cs.gc.util;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {

    public static final int SMALL_WINDOW_SIZE = 15 * 60 * 1000;
    public static final int LARGE_WINDOW_SIZE = SMALL_WINDOW_SIZE * 2;

    public static final double EAST_CELL_SIZE_500 = 0.005986;
    public static final double SOUTH_CELL_SIZE_500 = 0.004491556;

    public static final double EAST_CELL_SIZE_250 = EAST_CELL_SIZE_500 / 2;
    public static final double SOUTH_CELL_SIZE_250 = SOUTH_CELL_SIZE_500 / 2;

    public static final double TOP_LATITUDE = 41.474937;
    public static final double BOTTOM_LATITUDE = TOP_LATITUDE - 300 * SOUTH_CELL_SIZE_500;
    public static final double LEFT_LONGITUDE = -74.913585;
    public static final double RIGHT_LONGITUDE = LEFT_LONGITUDE + 300 * EAST_CELL_SIZE_500;


}
