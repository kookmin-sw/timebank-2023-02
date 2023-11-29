package kookmin.software.capstone2023.timebank.domain.model;


public enum Period {
    ONE_MONTH(1),
    THREE_MONTHS(3),
    SIX_MONTHS(6),
    ONE_YEAR(12);

    private final long months;

    Period(long months) {
        this.months = months;
    }
    public long getMonths() { return months; }
}
