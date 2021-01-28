
The implementation based on Android ARTTI & UIAutomator.

@author lvwu

-----------

## Abstraction

应用的运行期性能测量（方法执行时长）一般使用两种方式：插装和采样。

采样方式，无需手动改app代码，覆盖全部的方法周期。插装方式，需要将代码打到app里面，对特定代码逻辑进行抓取。

采样的优点是，结果全面；缺点是，app运行时更慢，结果过于全面，分析较麻烦，视图（火焰图）不够直观。

插装的优点是，运行时更快，根据需求指定分析，输出结果简单；缺点是，逻辑要写到app里面，效率低，分析覆盖不全。

UITI工具集成了他们排除了他们的缺点。属于运行时的app无侵入插装，可以看到应用页面生命周期的方法执行时长，该工具并配套了视图界面，**结果直观，操作更加简单。**

## Introduction

首先，该工具能输出两部分内容：

1. 页面每个生命周期中(onCreate, onResume等)，执行的方法，和每个方法的耗时。

2. 页面View的渲染效率(onMeasure, onLayout, onDraw)，他们的调用次数，方法耗时。

**第一部分，是为了帮助分析生命周期中的耗时，冗余方法。从而提高页面启动时间。**

**第二部分，在有动画（滚动）的页面中，由于绘制的代码，过于复杂，可能影响动画的流畅性。该工具可以分析出运行的界面绘制效率。从而帮助提高动画性能。**

<img src="doc/a.png" width = "1200" height = "700" alt="a" align=center />

该图片显示了onCreate, onResume方法的调用，并与源代码的对照。

**演示视频：**

https://xue.alibaba-inc.com/trs/mediaDetail.htm?spm=a1z39.8650609.0.0.7df74aa5UM4bQD&mediaUid=2e1530de-398b-4b09-a835-5e10fbf3b0d0


## Usage

#### 环境

- Mac 电脑
- AndroidSDK 和 环境变量 ANDROID_HOME
- JDK 1.8 以上和 环境变量 JAVA_HOME

#### 运行

* 打开两个终端，分别运行下面两个命令：

```
# 1. open UI
sh build.sh app

# 2. attach instrumentation agent
sh build.sh attach com.taobao.trip
```

* 第一个命令会打开以下界面，绿色的按钮的开始收集数据。红色按钮是停止收集，并展示解决。

<img src="doc/c.png" width = "400" height = "300" alt="a" align=center /> 

* 第二个命令会注入程序，注入之后，手机上的应用程序运行会很慢，只要没有崩溃，等一会就可以，我在huawei android10上已自测。

#### 使用

下面说明，如何打印下一个Activity生命周期的方法树：

1. attach之后，点击红色按钮。

2. 在应用程序中点击跳转到下一个Activity。

3. 一旦有UI渲染出来就可以点击绿色按钮。

结果会出现在右边的框内，展示生命周期方法树。

#### 方法树解释

类名 #方法名 Count:调用次数 Cost:方法时长



  