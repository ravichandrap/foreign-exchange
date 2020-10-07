package com.exchange.util;

import com.exchange.beans.Trend;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RateTrendTest {

    @Test
    void empty_test() {
        List<Double> list = new ArrayList<>(0);
        assertEquals( Trend.UNDEFINED, RateTrend.getTrend(list));
    }
    @Test
    void one_value_test() {
        List<Double> list = new ArrayList<>(5);
        list.add(0.7773286618);
        assertEquals( Trend.CONSTANT, RateTrend.getTrend(list));
    }
    @Test
    void undefined_test() {
        List<Double> list = new ArrayList<>(5);
        list.add(0.7773286618);
        list.add(0.7834091885);
        list.add(0.7814565588);
        list.add(0.7851383875);
        list.add(0.7755612682);
        assertEquals(Trend.UNDEFINED, RateTrend.getTrend(list) );
    }

//    @Test
//    void constant_test() {
//        List<Double> list = new ArrayList<>(5);
//        list.add(0.7773286618);
//        list.add(0.7773286618);
//        list.add(0.7773286618);
//        list.add(0.7773286618);
//        list.add(0.7773286618);
//        assertEquals( Trend.CONSTANT, RateTrend.getTrend(list));
//    }

    @Test
    void ascending_test() {
        List<Double> list = new ArrayList<>(5);
        list.add(0.7773286614);
        list.add(0.7773286615);
        list.add(0.7773286616);
        list.add(0.7773286617);
        list.add(0.7773286618);
        assertEquals(Trend.ASCENDING, RateTrend.getTrend(list));
    }

    @Test
    void descending_test() {
        List<Double> list = new ArrayList<>(5);
        list.add(0.7773286618);
        list.add(0.7773286617);
        list.add(0.7773286616);
        list.add(0.7773286615);
        list.add(0.7773286614);
        assertEquals(Trend.DESCENDING, RateTrend.getTrend(list));
    }

}