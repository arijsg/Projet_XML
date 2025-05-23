package fr.univrouen.rss25SB.adapters;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {
    @Override
    public OffsetDateTime unmarshal(String v) {
        return OffsetDateTime.parse(v, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    @Override
    public String marshal(OffsetDateTime v) {
        return v.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
