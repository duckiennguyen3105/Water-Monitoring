package com.Sislab.MagicSpoon.Fomatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XAsisDateFomatter implements IAxisValueFormatter {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return simpleDateFormat.format(new Date((long) value));
    }
}
