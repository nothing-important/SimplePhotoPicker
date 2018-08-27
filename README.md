# SimplePhotoPicker
一个进入相册，调用相机的简单框架，持续维护中...

效果图
---------------


使用方法
-------


                    new AlbumHelper()
                        .with(MainActivity.this)
                        .setPickPhotoNums(5)
                        .setResultPhotoList((ArrayList<String>) list_one)
                        .setSelectImg(R.mipmap.ic_launcher)
                        .setOnReceiveResultListener(new SelectPhotoResult() {
                            @Override
                            public void onReceivePhotoResult(ArrayList<String> resultList) {
                                list_one.clear();
                                ((ArrayList<String>) list_one).addAll(resultList);
                                adapter_one.notifyDataSetChanged();
                            }
                        }).start();


