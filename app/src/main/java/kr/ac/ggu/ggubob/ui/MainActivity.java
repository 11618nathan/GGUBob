package kr.ac.ggu.ggubob.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import kr.ac.ggu.ggubob.R;
import kr.ac.ggu.ggubob.model.Menu;
import kr.ac.ggu.ggubob.util.MenuUtil;

public class MainActivity extends AppCompatActivity {

    /**
     * Menu 리스트를 표시할 recyclerView와 데이터를 담당하는 menuAdapter.
     * build.gradle에 'com.android.support:recyclerview-v7:24.2.1' 추가.
     */
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    /**
     * 시간대 별 메뉴 리스트.
     */
    private List<Menu> menuList;

    /**
     * AsyncTask, Background thread 에서 작업을 수행하기 위한 Object를 만든다.
     * doInBackground()안에 background thread에서 수행할 작업을 구현하고,
     * onPostExecute()안에 수행이 끝나고 난 뒤에 수행할 작업을 구현한다.
     */
    private final AsyncTask<Void, Void, Void> menuParseTask = new AsyncTask<Void, Void, Void>() {

       /**
        * Background thread에서 수행해야할 작업을 구현한다.
        * 이 부분에서는 UI작업을 해서는 안된다.
        * 서버 접속 및 시간이 오래걸리는 작업들을 수행한다.
        */
        @Override
        protected Void doInBackground(Void... params) {
            // 홈페이지의 html을 읽어와서 parsing 한다.
            // Parsing을 하고 난 결과는 menuList에 저장을 한다.
            menuList = MenuUtil.generateMenuList("http://www.ggu.ac.kr/kor/campus_life/public_space_1.php");
            return null;
        }

        /**
         * Background에서 작업이 완료된 후, Main thread에서 수행해야할 작업을 구현한다.
         * Main thread에서는 주로 UI 업데이트 작업을 담당한다.
         */
        @Override
        protected void onPostExecute(Void params) {
            // 서버로 부터 새로 받아온 menuList를 가지고 UI를 업데이트 한다.
            updateUI();
        }
    };

    /**
     * MainActivity가 처음 실행할 때 호출되는 함수.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           // XML 파일과 연결작업을 수행한다.
        setContentView(R.layout.activity_main);
          // menuAdapter와 recyclerView를 연결한다.
        menuAdapter = new MenuAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(menuAdapter);
        // AsyncTask를 실행시킨다.
        menuParseTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * App Bar에 붙은 아이콘을 눌렀을 때의 동작을 정의한다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          // res/menu/main_menu.xml에 정의 되어있음.
          // 다음 메뉴 버튼 누를 경우.
        case R.id.action_next_menu: {
             // RecyclerView를 "다음 메뉴" 위치로 스크롤하여 이동시킨다.
            recyclerView.smoothScrollToPosition(menuAdapter.getNextMenuPosition());
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * menuList에 따라 UI를 업데이트 하는 함수.
     * UI적인 부분을 이곳에 구현하면 됩니다.
     */
    private void updateUI() {
        // MenuAdapter에 menuList를 전달하여 업데이트 시킨다.
        menuAdapter.setMenuList(menuList);
        // menuAdapter가 내용을 업데이트 하고 난 다음에 recyclerView를 "다음 메뉴"로 이동시킨다.
        recyclerView.post(new Runnable() {
        @Override
        public void run() {
            // RecyclerView를 "다음 메뉴" 위치로 스크롤하여 이동시킨다.
            recyclerView.smoothScrollToPosition(menuAdapter.getNextMenuPosition());
        }
    });
    }

}
