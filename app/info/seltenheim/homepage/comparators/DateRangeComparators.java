package info.seltenheim.homepage.comparators;

import info.seltenheim.homepage.services.DateRangeModel;

import org.joda.time.DateTime;

public class DateRangeComparators {

    public static int compareEndDateTimes(DateRangeModel dr1, DateRangeModel dr2) {
        final DateTime end1 = dr1.getToDate();
        final DateTime end2 = dr2.getToDate();
        if (end1 == null && end2 == null) {
            return 0;
        } else if (end1 == null) {
            return 1;
        } else if (end2 == null) {
            return -1;
        } else {
            return -end1.compareTo(end2);
        }
    }
}
