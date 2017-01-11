使用了两个布局 布局中的id是相同的
通过setcontentView替换布局
动画实现是通过在setContentView前获取上一个视图布局内部各个子view的状态(高度, 是否显示)
与下一个视图的各个view进行比较
然后下一个视图在setContentView前执行动画

viewTreeObserver -> onPreDraw()

执行动画
然后再setContentView


