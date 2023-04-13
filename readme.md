如果提示MvCameraControlWrapper.dll  can not load library

把C:\Program Files (x86)\Common Files\MVS\Runtime\Win64_x64下面所有的dll拷贝到jdk的bin下面


启动命令

java -Dserver.port=8888 -Xms1024m -Xmx1024m -jar app.jar