package nerdydog.domoHomeProd;

import java.util.ArrayList;

import nerdydog.domoHomeProd.object.Actuator;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class DnDListView extends ListView {
	
	public View moveView = null;
	private Context context = null;
	public int movedPosition = 0;
	public Actuator moveEntry = null;
	
	public boolean isMoveFlag = false;
	private int firstPosition;
	private int lasPosition;
	private ArrayList<nerdydog.domoHomeProd.DnDListView.Coordinates> listCoordinates;
	
	public DnDListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		setVerticalScrollBarEnabled(false);

	}

	public DnDListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		setVerticalScrollBarEnabled(false);

	}
	
	

	
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (isMoveFlag) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.v(getClass().toString(),"action_dowm");
				int y = (int)ev.getY();
				if (getParent() instanceof AbsoluteLayout) {
					AbsoluteLayout parent = (AbsoluteLayout)getParent();
					if (moveView != null) {
						parent.removeView(DnDAdapter.emptyView);
						parent.addView(DnDAdapter.emptyView, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0, y - 22));
						
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				Log.v(getClass().toString(),"action_move");
				int yMove = (int)ev.getY();
				if (getParent() instanceof AbsoluteLayout) {
					AbsoluteLayout parent = (AbsoluteLayout)getParent();
					if (moveView != null) {
						parent.removeView(DnDAdapter.emptyView);
						parent.addView(DnDAdapter.emptyView, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0, yMove - 22));
						inChild(MotionEvent.ACTION_MOVE);
					}
				}
				break;
			case MotionEvent.ACTION_UP: 
				if (getParent() instanceof AbsoluteLayout) {
					AbsoluteLayout parent = (AbsoluteLayout)getParent();
					if (moveView != null) {
						parent.removeView(DnDAdapter.emptyView);
						inChild(MotionEvent.ACTION_UP);
//						DnDAdapter.addEntry(movedPosition);
					}
				}
				isMoveFlag = false;
				setEnabled(true);
				Log.v(getClass().toString(),"action_up: " + isMoveFlag);
				break;
			default:
				break;
		}
		}
		return super.onTouchEvent(ev);
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		l = new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
					firstPosition = getFirstVisiblePosition();
					lasPosition = getLastVisiblePosition();
			}
			
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		};
		super.setOnScrollListener(l);
	}
	
	private void inChild(int action) {
		setCoordinates();
		for (int i = 0; i < listCoordinates.size(); i++) {
			Coordinates coordinates = listCoordinates.get(i);
			int topPosition = coordinates.getTop();
			int bottomPosition = coordinates.getBottom();
			int bottomMoveViewPosition = DnDAdapter.emptyView.getBottom();
			if (i != 0) {
				if (bottomMoveViewPosition > topPosition && bottomMoveViewPosition < bottomPosition) {
					Log.v(getClass().toString(),"Move view in position: " + i);
					DnDAdapter.removeEmptyEntry();
					DnDAdapter.addEmptyEntry(getFirstVisiblePosition() + i);
					if (action == MotionEvent.ACTION_UP) {
						DnDAdapter.removeEmptyEntry();
						DnDAdapter.addEntry(getFirstVisiblePosition() + i);
					}
				}
			}
		}
	}
	
	private void setCoordinates() {
		listCoordinates = new ArrayList<Coordinates>();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child != null) {
				Coordinates coord = new Coordinates();
				coord.setTop(child.getTop());
				coord.setBottom(child.getBottom());
				listCoordinates.add(coord);
			}
		}
	}
	
	
	
	class Coordinates {
		
		private int top = 0;
		private int bottom = 0;
		
		public int getTop() {
			return top;
		}
		public void setTop(int top) {
			this.top = top;
		}
		public int getBottom() {
			return bottom;
		}
		public void setBottom(int bottom) {
			this.bottom = bottom;
		}
		
		
	}

}
