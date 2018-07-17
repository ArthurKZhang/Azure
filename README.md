# Azure
côte d' Azure

### 日志
#### 1.1. 对方法的参数，返回值，耗时 的打印
#### 1.2. 类成员变量值的跟踪
使用注解@Logging

```
@Logging{
作用域 类，方法，成员变量
String Tag
int type log类型，默认debug
String traceName Trace打印的输出文件的名字
}
```
针对方法，参数和返回值默认打印，会记录方法运行时间和产生Trace输出。 针对类成员变量，每次成员变量被赋值都会输出一条log，但是意义不大，因为很多情况下对象一旦被赋值其引用不再变，变的是对象的内容。例如
```
Map m = new HashMap(...)
Map.put(......)
这种情况第二句不能引起m打印log。
```

注解的应用 和 优先级及覆盖关系举例：
```
@Logging(tag=“class”)
class Activity{
    @Logging(tag="field")
    private String str;
     
    public static void main(String[] args){new Activity().annoMethod;} 
 
 
    @Logging(tag="method")
    private void annoMethod(){
        str = "abc";
        normalMethod(str);
    }
     
    private int normalMethod(String var){return 0;}    
}
```
大致输出简化如下：

    1. tag[class]->constructor()
    2. tag[class]<-constructor()[0ms]
    3. tag[method]->annoMethod()
    4. tag[field]->set str = "abc"
    5. tag[field]->get str = "abc"
    6. tag[class]->normalMethod(var="abc")
    7. tag[class]<-normalMethod(var="abc")[1ms] return(0)
    8. tag[method]<-annoMethod()[1ms]return()
从例子可以看出，在类上的注解对类中所有的方法，构造方法，成员变量 都起作用；若方法、构造方法、Field单独加注解，则其单独注解起作用。
