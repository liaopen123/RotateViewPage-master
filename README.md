# RotateViewPage
ViewPager在切换的时候，页面会有一定角度的切换。
#使用方法
1.ViewPager本身的设置：

```
<android.support.v4.view.ViewPager
    android:id="@+id/pager"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clipToPadding="false"
    android:paddingLeft="100px"
    android:paddingRight="100px"/>

```
为它设置左右padding，然后clipToPadding = false，这样就可以看见和边上page交互转动的过程了。

2.将page之间的margin设置为负值，这样就可以在当前页面看见左右的page了。然后就是将ZoomOutPageTransformer设置为ViewPager的切换动画：

```
mPager.setPageMargin(-50);
mPager.setPageTransformer(true,new ZoomOutPageTransformer(mPager));
```
#动态效果

![pic1](https://raw.githubusercontent.com/jiasonwang/RotateViewPage/master/images/rotateviewpager.gif)

#主要实现原理

由于我们要实现左右两边可见其它pager，所以就需要增加paddingLeft/Right，但是，这会导致***ViewPager.PageTransformer***中的transformPage方法的
position无法传入0f的情况，这就会影响到对应的动画计算。查看源代码，这个position的计算值如下：

```
final float transformPos = (float) (child.getLeft() - scrollX) / getClientWidth();
mPageTransformer.transformPage(child, transformPos);
private int getClientWidth() {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
}
```
根据上面的算法，ViewPager计算一个页面是否刚好居中是根据child的布局位置和scrollX值来决定的，比如现在是第3个界面居中，那么假设现在每个page的宽度是100，说明scrollX值大概是200，屏幕
宽度是100，那么此时（200-200）／100 == 0；然而，当我们设置了padding之后，child的layout就会受到影响，所以它的child.getLeft()已经算上了paddingLeft的值，
假设paddingLeft和paddingRight为10，page的宽度是80，计算变成了child.getLeft() == 170 = 10+2\*80,scrollX = 2\*80,pagewidth = 100-10-10=80; position = (170-160)/80!=0.

显然getClientWidth，scrollX都是去除paddingLeft和paddingRight值以后的量，那么child.getLeft这里也需要返回的是从paddingLeft开始计算的位置，所以解决方案如下：

```
protected float currentCenter(View view) {
    int paddingLeft = mViewPager.getPaddingLeft();
    int paddingRight = mViewPager.getPaddingRight();
    int pageWidth = mViewPager.getMeasuredWidth() - paddingLeft - paddingRight;//等价于getClientWidth()
    int position = view.getLeft() - mViewPager.getScrollX() - paddingLeft;//考虑ViewPager有padding的情况
    return (float) position / pageWidth;
 } 
 
 public void transformPage(View view, float position) {
         position = currentCenter(view);//改写position
         ...
 }
```