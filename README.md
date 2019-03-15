# SimplePhotoPicker
一个进入相册，调用相机的**可高度定制化**的框架，持续维护中...

效果图
---------------
<div>
<img style="display:inline;"
 src="https://github.com/269138004/SimplePhotoPicker/blob/master/imgs/pic_002.png"
  width="260" height="480"/>
<img style="display:inline; margin-left:100px" 
 src="https://github.com/269138004/SimplePhotoPicker/blob/master/imgs/pic_001.png"
  width="260" height="480"/>
</div>
<div>
<img src="https://github.com/269138004/SimplePhotoPicker/blob/master/imgs/egOne.gif"
  width = "260" height="480"/>
</div>

那些你所担心但是已经帮你解决的问题
------------------------------
##### 1.Android6.0**隐私权限**请求问题--->已判断并处理
##### 2.Android7.0 **FileProvider访问**问题--->已判断并处理

目前实现的功能
--------------
##### 1.进入相册调用相机
##### 2.设置可选图片数量
##### 3.第二次进入时记录上次选中图片
##### 4.设置选中icon、未选中icon、相机icon、界面的titleView
##### 5.以回调监听选中结果，使用方便
##### 6.动态设置了Imageview的宽高，几乎可以适配所有屏幕
##### 7.类似于微信点击图片查看详情(图片的放大缩小功能)的切换效果

目前存在的一些问题
--------------
##### 1.图片放大缩小功能存在滑动不流畅问题

使用方法
-------
#### 使用

    new AlbumHelper()
        .with(MainActivity.this)
        .setPickPhotoNums(5)//可选图片数量，默认1
        .setResultPhotoList((ArrayList<String>) list_one)//接收图片结果的list集合，必须要写，不然无法记录原来已选的
        .isInDetailpage(false)//是否可以进入下一界面查看图片详情，默认可查看
        .setSelectImg(R.mipmap.ic_launcher)//设置图片选中表示，可不写，有默认
        .setUnSelectImg(R.mipmap.ic_launcher)//设置图片未选中表示，可不写，有默认
        .setCameraImg(R.mipmap.ic_launcher)//设置相机图片icon，可不写，有默认
        .setTitleView(R.layout.title_view)//设置界面的titleView
        .setOnReceiveResultListener(new SelectPhotoResult() {//选择完图片后的回调，在这里做ui刷新操作
            @Override
            public void onReceivePhotoResult(ArrayList<String> resultList) {
                list_one.clear();
                ((ArrayList<String>) list_one).addAll(resultList);
                adapter_one.notifyDataSetChanged();
            }
        }).start();//开始
    
    
##### 关于设置界面的titleview
    
    titleview的布局可以自己随意设置，但是有三个主要功能键需要加上固定的id
        1、界面的返回功能-->设置固定id：album_title_back
        2、界面的title-->设置固定id：album_title_name
        3、界面的提交功能-->设置固定id：album_title_finish
        这三部分不需要必须是三个具体的控件，只要是view的子类即可，比如返回键是一张图片加文字，只要
        给包裹图片和文字的控件设置上相应的id即可
        不要自己随意设置id，一定要使用这三个固定id


