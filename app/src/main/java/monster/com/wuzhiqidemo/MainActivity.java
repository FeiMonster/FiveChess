package monster.com.wuzhiqidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private PanelView p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p = (PanelView) findViewById(R.id.ttt);
    }
    public void click(View view){
        p.resStart();
    }
}
