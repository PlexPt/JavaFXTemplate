# JavaFXTemplate
javafx+controls+jfoenix+tilesfx+FontAwesomeFX+gradle+idea项目整合骨架项目


具体新建项目流程参考这篇文章：

http://www.52play.net/blog/post?id=16





改变的点来说说：



1、由于整合的库多了，所以要引入的模块也增多，

下面是执行时候的vm参数：

--module-path /usr/openjfx/openjfx-11/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics
--add-opens
javafx.base/com.sun.javafx.runtime=ALL-UNNAMED
--add-opens
javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
--add-opens
javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
--add-opens
javafx.base/com.sun.javafx.binding=ALL-UNNAMED
--add-opens
javafx.base/com.sun.javafx.event=ALL-UNNAMED
--add-opens
javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
--add-opens
javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED
--add-opens
javafx.graphics/javafx.scene.text=ALL-UNNAMED
--add-opens
javafx.graphics/javafx.scene=ALL-UNNAMED
--add-opens
javafx.graphics/javafx.css=ALL-UNNAMED


2、具体demo截图如下：

--jfoenix的demo的部分截图

屏幕截图_143.png

屏幕截图_144.png

屏幕截图_145.png

屏幕截图_146.png

 

-- tilesfx的demo截图，请注意，他们不是同一个文件运行的，暂时没有合并起来--反正就是可以正常运行。

屏幕截图_147.png

 

--字体的部分demo截图

屏幕截图_148.png

 


