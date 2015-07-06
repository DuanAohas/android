 

//布局文件中添加的内容
<RelativeLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal" >
                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="horizontal" >
                    <ImageView
                        android:id="@+id/iv_pre"
                        android:layout_width="51dp"
                        android:layout_height="48dp"
                        android:layout_alignParentLeft="true"
                        android:layout_alignParentTop="true"
                        android:paddingTop="0dp"
                        android:scaleType="centerInside"
                        android:paddingLeft="0dp"
                        android:src="@drawable/s_menu_previous_button" />
                    </RelativeLayout>
                    <NavigationHorizontalScrollView
                        android:id="@+id/horizontal_scrollview"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_weight="1"
                        android:scrollbars="none" />
                    <!--android:paddingBottom="2dp"-->
                    <!--android:paddingTop="2dp"-->
                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="horizontal" >
                    <ImageView
                        android:id="@+id/iv_next"
                        android:layout_width="51dp"
                        android:layout_height="48dp"
                        android:layout_alignParentRight="true"
                        android:layout_alignParentTop="true"
                        android:paddingTop="0dp"
                        android:scaleType="centerInside"
                        android:paddingRight="0dp"
                        android:src="@drawable/s_menu_next_button" />
                    </RelativeLayout>
                </RelativeLayout>


// Ａctivity 代码中加入
private NavigationHorizontalScrollView mHorizontalScrollView = null;
//
mHorizontalScrollView=(NavigationHorizontalScrollView)findViewById(R.id.horizontal_scrollview);
        mHorizontalScrollView.setImageView(R.layout.nlayout_selecttime_scrollview,(ImageView) findViewById(R.id.iv_pre),(ImageView) findViewById(R.id.iv_next));

        mHorizontalScrollView.setOnItemClickListener(new NavigationHorizontalScrollView.OnItemClickListener() {

            @Override
            public void click(int position) {
                // TODO Auto-generated method stub
//                tv.setText("You clicked "+navs.get(position).getTitle());
                if (iSelectNavDayItem != position){// 点顶部菜单 后切换菜单
                    if (NBespeakSelectTimeActivity.this.iSelect != -1) {
                        iSelectBack = NBespeakSelectTimeActivity.this.iSelect;
                        if (iSelectBack != -1) {
                            iSelectNavDayItemBack = iSelectNavDayItem;
                        }
                    }
                    iSelectNavDayItem = position;
                    adapterNavigation.notifyDataSetChanged();
                    reFlashData();
                }
            }
        });
        adapterNavigation = new NavigationAdapter();
        mHorizontalScrollView.setAdapter(adapterNavigation);

//