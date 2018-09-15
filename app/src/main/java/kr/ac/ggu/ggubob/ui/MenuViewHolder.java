package kr.ac.ggu.ggubob.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import kr.ac.ggu.ggubob.R;
import kr.ac.ggu.ggubob.model.Menu;

/**
 * RecyclerView의 각각의 row에 해당하는 UI.
 * {@link MenuAdapter}에서 생성해준다.
 */
public class MenuViewHolder extends RecyclerView.ViewHolder {

  private final CardView cardView;
  private final TextView dateView;
  private final TextView mealView;
  private final TextView dishesView;

  /**
   * MenuViewHolder의 생성자.
   */
  public MenuViewHolder(View itemView) {
    super(itemView);
    // res/layout/layout_menu_item.xml 참조.
    cardView = (CardView) itemView.findViewById(R.id.menu_cardview);
    dateView = (TextView) itemView.findViewById(R.id.menu_date);
    mealView = (TextView) itemView.findViewById(R.id.menu_meal);
    dishesView = (TextView) itemView.findViewById(R.id.menu_dishes);
  }

  /**
   * Menu의 내용이 변경되어 UI를 업데이트 해야할 경우, 호출된다.
   */
  public void bind(Menu menu) {

    // 배경 색상.
    if (menu.isNow()) {
      // 현재 Menu일 경우, 포인트 색상 설정.
      cardView.setCardBackgroundColor(itemView.getResources().getColor(menu.getMealType().bgColorId));
    } else {
      // 나머지는 회색 설정.
      cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.color_gray));
    }

    // 아침의 경우에만 날짜 표시.
    if (menu.getMealType() == Menu.MealType.BREAKFAST) {
      dateView.setVisibility(View.VISIBLE);
      dateView.setText(menu.getDateString());
    } else {
      dateView.setVisibility(View.GONE);
    }

    // 아침/점심/저녁 표시.
    mealView.setText(menu.getMealType().textId);

    // 반찬 표시.
    // ArrayList의 각 사이사이를 \n로 연결 시킨다.
    String dishes = TextUtils.join("\n", menu.getDishArray());
    dishesView.setText(dishes);
  }
}
