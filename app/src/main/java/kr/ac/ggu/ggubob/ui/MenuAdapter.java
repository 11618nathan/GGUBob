package kr.ac.ggu.ggubob.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.ac.ggu.ggubob.R;
import kr.ac.ggu.ggubob.model.Menu;

/**
 * RecyclerView에 연결할 Menu 데이터를 관리하는 클래스.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private final ArrayList<Menu> menuList = new ArrayList<>();

    /**
     * Menu 리스트를 MenuAdapter에 연결하고 UI 업데이트 시켜준다.
     */
    public void setMenuList(List<Menu> menuList) {
        this.menuList.clear();
        this.menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    /**
     * RecyclerView에 ViewHolder를 새로이 만들어야 할 경우 호출되는 함수.
     * 보통은 재사용됨.
     */
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    /**
     * RecyclerView의 각 row에 해당하는 ViewHolder에 내용을 연결 시켜준다.
     * 각 position index에 해당하는 Menu를 ViewHolder로 연결.
     */
    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        holder.bind(menu);
    }

    /**
     * RecyclerView에 표시할 row 개수.
     */
    @Override
    public int getItemCount() {
        return menuList.size();
    }

    /**
     * 현재 시간에 맞는 메뉴를 찾아주는 함수.
     * 못찾으면 0을 반환한다.
     */
    public int getNextMenuPosition() {
        Calendar calendar = Calendar.getInstance();
        Menu.MealType mealType;

        // 월, 일, 시간을 구한다.
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour < 10) {
            mealType = Menu.MealType.BREAKFAST;
        } else if (hour < 14) {
            mealType = Menu.MealType.LUNCH;
        } else if (hour < 20) {
            mealType = Menu.MealType.DINNER;
        } else {
            // 20시가 넘은 경우, 다음날 아침식사를 찾도록 설정한다.
            mealType = Menu.MealType.BREAKFAST;
            calendar.add(Calendar.DATE, 2); // data 1일 경우 그 당일날 오전 지정. 다음날 지정을 위해 변수 DATE 2값 할당.
        }//시간 배정은 식당 운영 시간 이후 1시간이 지나면 다음 지정.

          // 메뉴 loop을 돌면서 다음에 해당하는 메뉴를 찾아낸다.
        int nowIndex = 0;

        for (Menu menu : menuList) {
            menu.setNow(month, day, mealType);
            if (menu.isNow()) {
                 nowIndex = menuList.indexOf(menu);
            }
        }

        notifyDataSetChanged();
        // 현재 시간에 해당하는 Menu의 index를 반환.

        return nowIndex;
    }
}
