package kr.ac.ggu.ggubob.util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.ac.ggu.ggubob.model.Menu;
import kr.ac.ggu.ggubob.model.Menu.MealType;

public class MenuUtil {

  private static final String TAG = "MenuUtil";

  /**
   * 홈페이지 URL로부터 html을 파싱해서 메뉴 리스트를 만들어 반환하는 static 함수.
   * Network 작업은 반드시 background thread에서 동작하여야 합니다.
   */
  public static List<Menu> generateMenuList(String url) {
    // 반환하려는 메뉴 리스트를 생성한다.
    List<Menu> menuList = new ArrayList<>();
    try {
      // Jsoup을 이용하여 URL에 대한 Document 객체를 만든다.
      // 참고: https://jsoup.org/
      // 참고: http://yujuwon.tistory.com/entry/jsoup-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
      Document doc = Jsoup.connect(url).get();

      // 해당 URL에서 "소스보기"를 클릭하여 확인한다.
      // bab id 이후에 나타나는 <table>중 class가 tb_style02이고,
      // <tbody> 아래 <tr>들을 모두 query한다.
      for (Element row : doc.select("#bab ~ table.tb_style02 tbody tr")) {
        // <th>는 날짜.
        String date = row.select("th").get(0).text();
        // <td>는 각 식단
        Elements tds = row.select("td");
        // <td>의 개수가 3개 미만이면 잘못된 정보.
        if (tds.size() < 3) {
          continue;
        }
        // 아침, 점심, 저녁 메뉴들을 생성한다.
        Menu breakfastMenu = new Menu(date, MealType.BREAKFAST, tds.get(0).text());
        Menu lunchMenu = new Menu(date, MealType.LUNCH, tds.get(1).text());
        Menu dinnerMenu = new Menu(date, MealType.DINNER, tds.get(2).text());
        // 메뉴 리스트에 아침, 점심, 저녁을 추가한다.
        menuList.add(breakfastMenu);
        menuList.add(lunchMenu);
        menuList.add(dinnerMenu);
      }

      // 메뉴 리스트를 날짜 순서로 정렬시킨다.
      Collections.sort(menuList, new Comparator<Menu>() {
        @Override
        public int compare(Menu menu1, Menu menu2) {
          if (menu1.getMonth() > menu2.getMonth()) {
            return 1;
          } else if (menu1.getMonth() < menu2.getMonth()) {
            return -1;
          } else if (menu1.getDay() > menu2.getDay()) {
            return 1;
          } else if (menu1.getDay() < menu2.getDay()) {
            return -1;
          }
          return 0;
        }
      });
    } catch (IOException e) {
      Log.e(TAG, "Error: generateMenuList", e);
    }
    // 저장된 메뉴 리스트를 반환한다.
    return menuList;
  }
}
