package ai.obvs.services.ocr.dto;

import java.util.List;

public class ExtractData {
    private List<Field> fields;
    private Table table;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
