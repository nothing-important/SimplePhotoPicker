# SimplePhotoPicker
一个进入相册，调用相机的简单框架，持续维护中...

效果图
---------------


使用方法
-------

###导入依赖
####gradle
        allprojects {
                repositories {
                    ...
                    maven { url 'https://jitpack.io' }
                }
            }
            
        dependencies {
        	        implementation 'com.github.269138004:SimplePhotoPicker:v1.0'
        	}
####maven
        <repositories>
            <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </repository>
        </repositories>
        
	    <dependency>
        	    <groupId>com.github.269138004</groupId>
        	    <artifactId>SimplePhotoPicker</artifactId>
        	    <version>v1.0</version>
        </dependency>
####使用
    new AlbumHelper()
        .with(MainActivity.this)
        .setPickPhotoNums(5)//可选图片数量，默认1
        .setResultPhotoList((ArrayList<String>) list_one)//接收图片结果的list集合，必须要写，不然无法记录原来已选的
        .setSelectImg(R.mipmap.ic_launcher)//设置图片选中表示，可不写，有默认
        .setUnSelectImg(R.mipmap.ic_launcher)//设置图片未选中表示，可不写，有默认
        .setCameraImg(R.mipmap.ic_launcher)//设置相机图片icon，可不写，有默认
        .setOnReceiveResultListener(new SelectPhotoResult() {//选择完图片后的回调，在这里做ui刷新操作
            @Override
            public void onReceivePhotoResult(ArrayList<String> resultList) {
                list_one.clear();
                ((ArrayList<String>) list_one).addAll(resultList);
                adapter_one.notifyDataSetChanged();
            }
        }).start();//开始


