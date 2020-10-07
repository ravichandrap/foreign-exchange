package com.exchange.util;

import com.exchange.beans.Trend;

import java.util.List;

public class RateTrend {

    private RateTrend() {
    }

    /**
     * Find the trend of provided currency price list.
     *
     * @param list list of currency price.
     * @return ascending, descending, constant, undefined
     */
    public static Trend getTrend(List<Double> list) {
        Trend undefined = initCheck(list);
        if (undefined != null) return undefined;
        if (new Ascending().compute(list).equals(Trend.ASCENDING)) {
            return Trend.ASCENDING;
        } else if (new Descending().compute(list).equals(Trend.DESCENDING)) {
            return Trend.DESCENDING;
        } else {
            return Trend.UNDEFINED;
        }
    }


    private static Trend initCheck(List<Double> list) {
        if (list.isEmpty())
            return Trend.UNDEFINED;

        if (list.size() == 1)
            return Trend.CONSTANT;
        return null;
    }

    interface Computation<T> {
        public T compute(List<Double> l);
    }

    static class Ascending implements Computation<List<Double>> {

        public boolean isSorted(List<Double> listOfStrings, int index) {
            if (index < 2) {
                return true;
            } else if (listOfStrings.get(index - 2).compareTo(listOfStrings.get(index - 1)) > 0) {
                return false;
            } else {
                return isSorted(listOfStrings, index - 1);
            }
        }

        public Trend compute(List<Double> list) {
            if (isSorted(list, list.size())) {
                return Trend.ASCENDING;
            }
            return Trend.UNDEFINED;
        }


    }

    static class Descending implements Computation<List<Double>> {

        public boolean isSorted(List<Double> listOfStrings, int index) {
            if (index < 2) {
                return true;
            } else if (listOfStrings.get(index - 2).compareTo(listOfStrings.get(index - 1)) < 0) {
                return false;
            } else {
                return isSorted(listOfStrings, index - 1);
            }
        }

        public Trend compute(List<Double> list) {
            if (isSorted(list, list.size())) {
                return Trend.DESCENDING;
            }
            return Trend.UNDEFINED;
        }
    }
}

