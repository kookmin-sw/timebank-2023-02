package kookmin.software.capstone2023.timebank.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerExtensions {
    public static <R> Logger logger(R obj) {
        return LoggerFactory.getLogger(obj.getClass().getName());
    }
}
