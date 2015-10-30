package com.ftd.schaepher.coursemanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

/**
 * Created by sxq on 2015/10/30.
 */
public class ExcelDisplayActivity extends AppCompatActivity {

    private static final String[][] dataToShow = { { "This", "is", "a", "test","This", "is", "a", "test" },
            { "and", "a", "second", "test","and", "a", "second", "test" } };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_display);
//        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
//        tableView.setDataAdapter(new SimpleTableDataAdapter(this, dataToShow));
//        tableView.setHorizontalScrollBarEnabled(true);
    }
}
