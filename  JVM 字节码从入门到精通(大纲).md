# 一、初识字节码

## 1、java文件如何变成 .class 文件

![class文件生成过程](https://user-gold-cdn.xitu.io/2020/7/3/1731466ec5e8492e?w=693&h=305&f=png&s=105191)

## 2、魔数 0xCAFEBABE

```class
xxd Hello.class 
00000000: cafe babe 0000 0034 0022 0a00 0600 1409  .......4."......
00000010: 0015 0016 0800 170a 0018 0019 0700 1a07  ................
00000020: 001b 0100 063c 696e 6974 3e01 0003 2829  .....<init>...()
00000030: 5601 0004 436f 6465 0100 0f4c 696e 654e  V...Code...LineN
00000040: 756d 6265 7254 6162 6c65 0100 124c 6f63  umberTable...Loc
00000050: 616c 5661 7269 6162 6c65 5461 626c 6501  alVariableTable.
00000060: 0004 7468 6973 0100 074c 4865 6c6c 6f3b  ..this...LHello;
```



* 这个魔数是 JVM 识别 .class 文件的标志，虚拟机在加载类文件之前会先检查这四个字节，如果不是 0xCAFEBABE 则拒绝加载该文件

## 3、javap 详解

```java
public class Hello {
    private String hello = "111";
    public String world = "222";
    public static void main(String[] args) {
        System.out.println("Hello, World");
    }
    private void sayHello() {
        System.out.println("Hello, World");
    }
}
```

### a)、 -c 选项

* 对类进行反编译

```java
Compiled from "Hello.java"
public class Hello {
  public Hello();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: getstatic     #2  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #3  // String Hello, World
       5: invokevirtual #4  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return
}
```

 3~7行: 可以看到虽然没有写 Hello 类的构造器函数，编译器会自动加上一个默认构造器函数
* 5行: aload_0 这个操作码是 aload_x 格式操作码中的一个。它们用来把对象引用加载操作数栈。 x 表示正在被访问的局部变量数组的位置。 在这里的0代表: 非静态的杉树都有第一个默认参数this，这里的aload_0 就是把this 入栈
* 6行: invokespecial #1，invokespecial 指令调用实例初始化方法、私有方法、父类方法，#1 指的是常量池中的第一个，这里是方法引用 java/lang/Object."<init>":()V，也即构造器函数
* 7行: return，这个操作码属于ireturn、lreturn、freturn、dreturn、areturn 和 return 操作码组中的一员，其中 i 表示 int，返回整数，同类的还有 I 表示 long，f 表示 float，d 表示 double,  a 表示对象引用。没有前缀类型字母的 return 表示返回 void

以上是默认的构造器函数，接下来是 9 ~ 14 行的 main 函数

* 11行: getstatic #2，getstatic 获取指定类的静态域，并将其值压入栈顶，#2 代表常量池中的第 2 个，这里表示的是 java/lang/System.out:Ljava/io/PrintStream；，其实就是java.lang.System类的静态变量 out (类型是 PrintStream)

* 12行: ldc #3，ldc 用来将常量从运行时常量池压栈道操作数栈，#3 代表常量池的第三个(字符串 Hello，World)
* 13行: invokevirtual #4，invokevirtual 指令调用一个对象的实例方法，#4 表示PrintStream.printlin(String) 函数应用，并把栈顶两个元素出栈

过程如下图

![image.png](https://user-gold-cdn.xitu.io/2019/1/12/1684011c9741061d?imageslim)

### b)、 -p 选项

默认情况下，javap 会显示访问权限为 public、protected 和默认(包级 protected) 级别的方法，加上 -p 选项以后可以显示 private 方法和字段

  ```java
  Compiled from "Hello.java"
  public class Hello {
    private java.lang.String hello;
    public java.lang.String world;
    public Hello();
    public static void main(java.lang.String[]);
    private void sayHello();
  }
  ```

### c)、 -v 选项

javap 加上 -v 参数的输出更多详细的信息，比如栈大小、方法参数的个数

```java
Classfile /c:/Hello.class
  Last modified 2020-7-8; size 589 bytes
  MD5 checksum aca7aeab470adc51fe0eae48d7786eb4
  Compiled from "Hello.java"
public class Hello
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #10.#23        // java/lang/Object."<init>":()V
   #2 = String             #24            // 111
   #3 = Fieldref           #9.#25         // Hello.hello:Ljava/lang/String;
   #4 = String             #26            // 222
   #5 = Fieldref           #9.#27         // Hello.world:Ljava/lang/String;
   #6 = Fieldref           #28.#29        // java/lang/System.out:Ljava/io/PrintStream;
   #7 = String             #30            // Hello, World
   #8 = Methodref          #31.#32        // java/io/PrintStream.println:(Ljava/lang/String;)V
   #9 = Class              #33            // Hello
  #10 = Class              #34            // java/lang/Object
  #11 = Utf8               hello
  #12 = Utf8               Ljava/lang/String;
  #13 = Utf8               world
  #14 = Utf8               <init>
  #15 = Utf8               ()V
  #16 = Utf8               Code
  #17 = Utf8               LineNumberTable
  #18 = Utf8               main
  #19 = Utf8               ([Ljava/lang/String;)V
  #20 = Utf8               sayHello
  #21 = Utf8               SourceFile
  #22 = Utf8               Hello.java
  #23 = NameAndType        #14:#15        // "<init>":()V
  #24 = Utf8               111
  #25 = NameAndType        #11:#12        // hello:Ljava/lang/String;
  #26 = Utf8               222
  #27 = NameAndType        #13:#12        // world:Ljava/lang/String;
  #28 = Class              #35            // java/lang/System
  #29 = NameAndType        #36:#37        // out:Ljava/io/PrintStream;
  #30 = Utf8               Hello, World
  #31 = Class              #38            // java/io/PrintStream
  #32 = NameAndType        #39:#40        // println:(Ljava/lang/String;)V
  #33 = Utf8               Hello
  #34 = Utf8               java/lang/Object
  #35 = Utf8               java/lang/System
  #36 = Utf8               out
  #37 = Utf8               Ljava/io/PrintStream;
  #38 = Utf8               java/io/PrintStream
  #39 = Utf8               println
  #40 = Utf8               (Ljava/lang/String;)V
{
  public java.lang.String world;
    descriptor: Ljava/lang/String;
    flags: ACC_PUBLIC

  public Hello();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: ldc           #2                  // String 111
         7: putfield      #3                  // Field hello:Ljava/lang/String;
        10: aload_0
        11: ldc           #4                  // String 222
        13: putfield      #5                  // Field world:Ljava/lang/String;
        16: return
      LineNumberTable:
        line 1: 0
        line 2: 4
        line 3: 10

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #6                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #7                  // String Hello, World
         5: invokevirtual #8                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 5: 0
        line 6: 8
}
SourceFile: "Hello.java"
```

### d)、 -s 选项

* 输出签名的类型描述符

```
javap -s Hello  
Compiled from "Hello.java"
public class Hello {
  public Hello();
    descriptor: ()V
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
}
```

* 可以看到 main 函数的方法签名是 (Ljava/lang/String;)V。主要分为字段描述符和方法描述符

  * **字段描述符**，是一个表示类、实例或局部变量的语法符号，它的表示形式是紧凑的，比如 int 是用 I 表示的。

    | 描述符      | 类型                                        |
    | ----------- | :------------------------------------------ |
    | B           | byte                                        |
    | C           | char                                        |
    | D           | double                                      |
    | F           | Float                                       |
    | I           | Int                                         |
    | J           | Long                                        |
    | L ClassName | 应用类型(比如Ljava/lang/String; 用于字符串) |
    | S           | short                                       |
    | Z           | Boolean                                     |
    | [           | Array-of                                    |

  * **方法描述符** ，表示一个方法所需参数和返回值信息，表示形式为 （ParameterDescriptor* ）ReturnDescriptor。ParameterDescription 表示参数类型, ReturnDescriptor表示返回值信息，当没有返回值时用 V 表示。比如方法 Object foo(int i, double d, Thread t) 的描述符为 (IDLjava/lang/Thread;)Ljava/lang/Object

  ![方法描述符](https://user-gold-cdn.xitu.io/2020/7/7/17327985fbf5ca51?w=800&h=310&f=png&s=146306)

### e)、 -l 选项

* javac -g HelloWorld.java`以后重新执行`javap -l`命令就可以看到局部变量表

```java
javap -l HelloWorld
public static void main(java.lang.String[]);
LineNumberTable:
  line 3: 0
  line 4: 8
LocalVariableTable:
  Start  Length  Slot  Name   Signature
      0       9     0  args   [Ljava/lang/String;
```

# 二. class类文件解析

## 1、class 文件结构解析

* Java 虚拟机规定了 u1、u2、u4三种结构来表示 1、2、4 字节无符号整数，相同类型的若干条数据集合用表 (table) 的形式来存储。表示一个边长的结构，由代表长度的表头 (n) 和紧随着的 n 个数据项组成。class 文件采用类似 C 语言的结构体来存储数据

```
ClassFile {
      u4             magic;
      u2             minor_version;
      u2             major_version;
      u2             constant_pool_count;
      cp_info        constant_pool[constant_pool_count-1];
      u2             access_flags;
      u2             this_class;
      u2             super_class;
      u2             interfaces_count;
      u2             interfaces[interfaces_count];
      u2             fields_count;
      field_info     fields[fields_count];
      u2             methods_count;
      method_info    methods[methods_count];
      u2             attributes_count;
      attribute_info attributes[attributes_count];
  }
```

class 文件由线面是个部分组成

* 魔数（Magic Number）
* 版本号（Minor&Major Version）
* 常量池（Constant Pool）
* 类访问标记(Access Flags)
* 类索引（This Class）
* 超类索引（Super Class）
* 接口表索引（Interfaces）
* 字段表（Fields）
* 方法表（Methods）
* 属性表（Attributes）

![class.png](https://user-gold-cdn.xitu.io/2020/7/9/17331ce7017a921e?w=796&h=560&f=png&s=78316)



### 1.1 魔数

* 使用文件名后缀来区分文件类型很不靠谱，后缀可以被随便修改
* **魔数**的作用：从文件内容本身来标识文件的类型呢

* 0xCAFEBABE 

![hex.png](https://user-gold-cdn.xitu.io/2019/9/23/16d5ec476469322a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### 1.2 版本号

* 在魔数之后的四个字节分别表示副版本号 (Minor Version) 和主版本号 (Major Version)

![version.png](https://user-gold-cdn.xitu.io/2020/7/9/17331cee6012badb?w=759&h=283&f=png&s=273024)

* 如果类文件的版本号高于 JVM 自身的版本号，加载该类会被直接抛出 java.lang.UnsupportedCalssVersionError 异常
* javap -v 可以方便查看类文件版本信息

### 1.3 常量池

* 对于 JVM 字节码来说，如果操作数非常小或者很常用的数字 0 之类的，这些操作数是内嵌到字节码中的。如果是字符串常量和较大的整数等，class 文件是把这些操作数存储在一个叫常量池 （Constant Pool）的地方，当使用这些操作数时，使用的是常量池数据中的索引位置。
* 常量池结构如下

```java
{
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count-1];
}
```

分为两部分

* 常量池大小 (cp_info_count)，常量池是 class 文件中第一个出现的变长结构。常量池大小由两个字节表示。常量池真正的有效索引是 1~ n-1，0 属于保留索引，用来表示不指向任何常量池项
* 常量池项 (cp_info) 集合，最多包含 n -1 个。为什么是最多呢？Long 和 Double 类型的常量会占用两个索引位置，如果常量池包含了这两种类型，实际的常量池项的元素个数比 n - 1 要小。
* 每个常量项都由两部分构成: 表示类型的 tag 和表示内容的字节数组

```
cp_info {
    u1 tag;
    u1 info[];
}
```

#### 1.3.1 CONSTANT_Integer_info、CONSTANT_Float_info

* CONSTANT_Integer_info 和 CONSTANT_Float_info 这两种结构分别用来表示 int 和 float 类型的常量

```java
CONSTANT_Integer_info {
    u1 tag;
    u4 bytes;
}

CONSTANT_Float_info {
    u1 tag;
    u4 bytes;
}
```

* boolean、byte、short 和 char  类型的变量，在常量池中都会被当做 int 来处理
* 以整型常量 18(0x12) 为例，它在常量池中的布局结构如下图所示：

![constant_int.png](https://user-gold-cdn.xitu.io/2020/7/9/17331da074652b8d?w=769&h=132&f=png&s=21187)



#### 1.3.2 CONSTANT_Long_info 和 CONSTANT_Double_info

* CONSTANT_Long_info 和 CONSTANT_Double_info 这两种结构分别用来表示 long 和 double 类型的常量

```java
CONSTANT_Long_info {
    u1 tag;
    u4 high_bytes;
    u4 low_bytes;
}

CONSTANT_Double_info {
    u1 tag;
    u4 high_bytes;
    u4 low_bytes;
}
```

![long.png](https://user-gold-cdn.xitu.io/2020/7/9/17331db234e32bff?w=759&h=123&f=png&s=24760)

#### 1.3.3 CONSTANT_Utf8_info

* CONSTANT_Utf8_info 存储的时经过 MUTF(modified UTF-8) 编码的字符串

```java
CONSTANT_Utf8_info {
    u1 tag;
    u2 length;
    u1 bytes[length];
}
```

* 由三部分构成: 第一个字节是 tag，值为固定为1，tag 之后的两个字节 length 表示字符串的长度，第三部分是采用 MUTF-8 编码的长度字节为了length 的字节数组
* 以下是存储的字符串是"hello"

![utf8.png](https://user-gold-cdn.xitu.io/2020/7/9/17331e1f80e3e797?w=803&h=102&f=png&s=15912)

#### 1.3.4 CONSTANT_String_info 

* CONSTANT_String_info 用来表示 java.lang.String 类型的常量对象

```java
CONSTANT_String_info {
    u1 tag;
    u2 string_index;
}
```

* 第一个字节是 tag，值为8，tag 后面的两个字节是一个叫 string_index 的索引值，指向常量池中的 CONSTANT_Utf8_info，这个 CONSTANT_Utf8_info 中存储的才是真正的字符串常量
* 例如 String a = "hello"

![string.png](https://user-gold-cdn.xitu.io/2020/7/9/17331e627a1719c6?w=787&h=160&f=png&s=40102)

#### 1.3.5 CONSTANT_Class_info

```java
CONSTANT_Class_info {
    u1 tag;
    u2 name_index;
}
```

*  由两部分组成，第一个字节是 tag, 值为7，tag 后面的两个字节 name_index 是一个常量池索引，指向类型为 CONSTANT_Utf8_info 常量

![class.png](https://user-gold-cdn.xitu.io/2020/7/9/17331edd6db67443?w=764&h=128&f=png&s=39781)

#### 1.3.6 CONSTANT_NameAndType_info

* CONSTANT_NameAndType_info 结构用来表示字段或者方法。由以下三部分组成
  * tag CONSTANT_NameAndType_info 结构 tag 的值为 12
  * name_index 指向常量池中的 CONSTANT_Utf8_info，存储的时字段名或者方法名
  * Descriptor_inde 也是指向常量池中的 CONSTANT_Utf8_info，存储的是字段描述符或者方法描述符
* 以如下方法示例

```java
public void testMethod(int id, String name) {
		
}
```

* CONSTANT_NameAndType_info 的结构示意图如下

![method.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f1e0817a83ef?w=1534&h=492&f=png&s=183483)

#### 1.3.7 CONSTANT_Fieldref_info、CONSTANT_Methodref_info 和 CONSTANT_InterfaceMethodref_info

```java
CONSTANT_Fieldref_info {
    u1 tag;
    u2 class_index;
    u2 name_and_type_index;
}

CONSTANT_Methodref_info {
    u1 tag;
    u2 class_index;
    u2 name_and_type_index;
}

CONSTANT_InterfaceMethodref_info {
    u1 tag;
    u2 class_index;
    u2 name_and_type_index;
}
```

* 以 CONSTANT_Methodref_info 作为示例，表示类中的符号引用

```
方法 = 方法所属的类 + 方法名 + 方法参数及返回值描述符
```

* 由以下三部分组成
  * 第一个字节是tag : 10
  * 第二个部分是 class_index，指向 CONSTANT_Class_info 常量池的索引值
  * 第三部分是 name_and_type_index，指向 CONSTANT_NameAndType_info 常量池索引值，表示方法的参数类型和返回值的签名

```java
public class HelloWorldMain {
    public static void main(String[] args) {
        new HelloWorldMain().testMethod(1, "hi");
    }
    public void testMethod(int id, String name) {
    }
}

Constant pool:
   #2 = Class              #18            // HelloWorldMain
   #5 = Methodref          #2.#20         // HelloWorldMain.testMethod:(ILjava/lang/String;)V
  #20 = NameAndType        #13:#14        // testMethod:(ILjava/lang/String;)V
```



![methodref.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f2447517485c?w=1532&h=586&f=png&s=193257)

#### 1.3.8 CONSTANT_MethodType_info、CONSTANT_MethodHandle_info 和 CONSTANT_InvokeDynamic_info

* 从 JDK1.7 开始，为了更好支持动态语言的调用，新增了3种常量池类型
* 以下示例以 CONSTANT_InvokeDynamic_info ，主要为 invokedynamic 指令提供引导方法

```
CONSTANT_InvokeDynamic_info {
    u1 tag;
    u2 bootstrap_method_attr_index;
    u2 name_and_type_index;
}
```

* tag : 18
* bootstrap_method_attr_index : 指向引导方法表 bootstrap_methods[] 数组的索引
* name_and_type_index : 指向常量池里的 CONSTANT_NameAndType_info ，表示方法描述符

```java
public void foo() {
    new Thread (()-> {
        System.out.println("hello");
    }).start();
}
```

```java
Constant pool:
   #3 = InvokeDynamic      #0:#25         // #0:run:()Ljava/lang/Runnable;
   ...
  #25 = NameAndType        #37:#38        // run:()Ljava/lang/Runnable;

BootstrapMethods:
  0: #22 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #23 ()V
      #24 invokestatic HelloWorldMain.lambda$foo$0:()V
      #23 ()V
```

![methodinvoke.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f2a886049c8c?w=1528&h=1090&f=png)

### 1.4 类访问标记

*  用来标识一个类 是不是final、abstract等，由两个字节一共16位标识可供使用

![classtag.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f2ce2d104f47?w=1514&h=420&f=png&s=109460)

![tag.class](https://user-gold-cdn.xitu.io/2020/7/12/1733f2e86250df2a?w=1162&h=976&f=png&s=97809)

### 1.5 类、超类、接口索引表

* 用来表示类的继承关系，this_name 标识类索引、super_name 标识父类索引、interface 标识类或接口的父接口
* 本例中 this_class 为 0x0005，指向常量池中下标为 5 的元素，这个元素是由两部分组成，第一部分是类型，这里是 Class 表示是一个类，第二部分是指向常量池下标 21 的元素，这个元素是字符串 "HelloWorldMain"。

![thisclass.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f31938d4d8c5?w=1498&h=706&f=png&s=121627)

### 1.6 字段表

* 类中定义的字段会被存储到这个集合中，包括类中定义的静态和非静态字段，不包括方法定义的内部变量

```
{
    u2             fields_count;
    field_info     fields[fields_count];
}
```

* 由两部分组成
  * 字段数量（fields_count）：字段表也是一个变长的结构，类中定义的若干个字段的个数会被存储到字段数量里。
  * 字段集合（fields）：字段集合是一个类数组的结构，共有 fields_count 个，对应类中定义的若干个字段，每一个字段 field_info 的结构会在下面介绍。

![](https://user-gold-cdn.xitu.io/2020/7/12/1733f360c2b27373?w=1586&h=264&f=png&s=71213)

##### 1.6.1 字段 field_info 结构

```
field_info {
    u2             access_flags; 
    u2             name_index;
    u2             descriptor_index;
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

* access_flags：表示字段的访问标记，是 public、private 还是 protected，是否是 static，是否是 final 等。
* name_index：字段名的索引值，指向常量池的的字符串常量。
* descriptor_index：字段描述符的索引，指向常量池的字符串常量。
* attributes_count、attribute_info：表示属性的个数和属性集合。

##### 1.6.2 字段访问标记

![field.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f394e5ecca4c?w=1526&h=1084&f=png&s=138822)

##### 1.6.3 字段描述符

![](https://user-gold-cdn.xitu.io/2020/7/12/1733f3b83248024c?w=866&h=1160&f=png&s=68164)

##### 1.6.4 字段属性 

* 与字段相关的属性有下面这几个：ConstantValue、Synthetic 、Signature、Deprecated、RuntimeVisibleAnnotations 和 RuntimeInvisibleAnnotations 这六个，比较常见的是 ConstantValue 这属性，用来表示一个常量字段的值

### 1.7 方法表 

* 类中定义的方法会被存储在该集合中，与字段表一样，方法表也是一个变长集合

```
{
    u2             methods_count;
    method_info    methods[methods_count];
}
```

##### 1.7.1 方法 method_info 结构

```
method_info {
    u2             access_flags;
    u2             name_index;
    u2             descriptor_index;
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

* 由以下四部分组成
* access_flags：表示方法的访问标记，是 public、private 还是 protected，是否是 static，是否是 final 等。
* name_index：方法名的索引值，指向常量池的的字符串常量。
* descriptor_index：方法描述符的索引，指向常量池的字符串常量。
* attributes_count、attribute_info：表示方法相关属性的个数和属性集合，包含了很多有用的信息，比如方法内部的字节码就是存放在 Code 属性中。

##### 1.7.2 方法访问标记

![methodtag.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f3f67f9af202?w=1434&h=1372&f=png&s=169213)

##### 1.7.3 方法名与描述符

* 方法名: 方法名索引 name_index，指向常量池中 CONSTANT_Utf8_info 类型的字符串常量
* 描述符

```
(参数1类型 参数2类型 参数3类型 ...)返回值类型
```

![type.png](https://user-gold-cdn.xitu.io/2020/7/12/1733f43f0075d097?w=1536&h=372&f=png&s=146282)

##### 1.7.4 方法属性表

* 前面介绍了方法的访问标记、方法签名，还有一些重要的信息没有出现，如方法声明抛出的异常，方法的字节码，方法是否被标记为 deprecated，这些信息存在哪里呢？这就是方法属性表的作用。跟方法相关的属性有很多，其中重要的是 Code 和 Exceptions 属性，其中 Code 属性存放方法体的字节码指令，Exceptions属性 用于存储方法声明抛出的异常

### 1.8 属性表

* 属性出现的地方比较广泛，不止出现在字段和方法中，在顶层的 class 文件中也会出现。
* 相比于常量池只有 14 种固定的类型，属性表是的类型是更加灵活的，不同的虚拟机实现厂商可以自定义自己的属性。 属性表的结构如下所示

```
{
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

##### 1.8.1 ConstantValue 属性

* ConstantValue 属性只会出现字段 field_info 中，表示静态变量的初始值

```
ConstantValue_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 constantvalue_index;
}
```

* 其中 attribute_name_index 是指向常量池中值为 "ConstantValue" 的常量项，ConstantValue 属性的 attribute_length 值恒定为 2，constantvalue_index 指向常量池中具体的常量值索引，根据变量的类型不同 constantvalue_index 指向不同的常量项

* 以`public static final int DEFAULT_SIZE = 128;`为例，字段对应的 class 文件如下图高亮部分

![](https://user-gold-cdn.xitu.io/2020/7/12/1733f47f4e3e1556?w=1504&h=514&f=png&s=975798)

![](https://user-gold-cdn.xitu.io/2020/7/12/1733f4846551f61b?w=1536&h=874&f=png&s=193154)

##### 1.8.1 Code 属性

* 包含了所有方法的字节码

```java
Code_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 max_stack;
    u2 max_locals;
    u4 code_length;
    u1 code[code_length];
    u2 exception_table_length;
    {   u2 start_pc;
        u2 end_pc;
        u2 handler_pc;
        u2 catch_type;
    } exception_table[exception_table_length];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
```

* 以下是详细描述
  * 属性名索引（attribute_name_index）占两个字节，指向常量池中 CONSTANT_Utf8_info 常量，表示属性的名字，比如这里对应的常量池的字符串常量"Code"。
  * 属性长度（attribute_length）占用两个字节，表示属性值大小
  * max_stack 表示操作数栈的最大深度，方法执行的任意期间操作数栈的深度都不会超过这个值。它的计算规则是有入栈的指令 stack 增加，有出栈的指令 stack 减少，在整个过程中 stack 的最大值就是 max_stack 的值，增加和减少的值一般都是 1，但也有例外：LONG 和 DOUBLE 相关的指令入栈 stack 会增加 2，VOID 相关的指令则为 0。
  * max_locals 表示局部变量表的大小，它的值并不是等于方法中所有局部变量的数量之和。当一个局部作用域结束，它内部的局部变量占用的位置就可以被接下来的局部变量复用了。
  * code_length 和 code 用来表示字节码相关的信息，其中 code_length 表示字节码指令的长度，占用 4 个字节。code 是一个长度为 code_length 的字节数组，存储真正的字节码指令。
  * exception_table_length 和 exception_table 用来表示代码内部的异常表信息，如我们熟知的 try-catch 语法就会生成对应的异常表。
  * attributes_count 和 attributes[] 用来表示 Code 属性相关的附属属性，Java 虚拟机规定 Code 属性只能包含这四种可选属性：LineNumberTable、LocalVariableTable、LocalVariableTypeTable、StackMapTable。以LineNumberTable 为例，LineNumberTable 用来存放源码行号和字节码偏移量之间的对应关系，这 LineNumberTable 属于调试信息，不是类文件运行的必需的属性，默认情况下都会生成。如果没有这两个属性，那么在调试时没有办法在源码中设置断点，也没有办法在代码抛出异常的时候在错误堆栈中显示出错的行号信息

* 示例:

```java
public static void main(String[] args) {

    try {
        foo();
    } catch (NullPointerException e) {
        System.out.println();
    } catch (IOException e) {
        System.out.println();
    }

    try {
        foo();
    } catch (Exception e) {
        System.out.println();
    }
}
```

![](https://user-gold-cdn.xitu.io/2020/7/12/1733f4da18fef908?w=1492&h=902&f=png&s=231964)

## 2.2 字节码概述

字节码  = 操作码 + 操作数

# 二、字节码原理 —— 基于栈的执行引擎

## 1、stack based vs registed based

* 虚拟机常见的实现方式有两种：Stack based 和 Register based

* 举例 c = a + b
* 基于 HotSpot JVM 的源码和字节码如下

```java
void bar(int a, int b) {
    int c =  a + b;
}

对应字节码
0: iload_1 // 将 a 压入操作数栈
1: iload_2 // 将 b 压入操作数栈
2: iadd    // 将栈顶两个值出栈，相加，然后将结果放回栈顶
3: istore_3 // 将栈顶值存入局部变量表中第 3 个 slot 中
```

* 基于寄存器的 LuaVM  的 lua袁爱民和字节码如下，查看字节码使用 luac -l -l -v -s test.lua 命令

```
local function my_add(a, b)
	return a + b;
end

对应字节码
1	[3]	ADD      	2 0 1
```

* 基于寄存器的 add 指令直接把期存器 R0 和 R1 相加，结果保存在寄存器 R2 中
* 基于栈和基于寄存器的过程对比如下

![stack.png](https://user-gold-cdn.xitu.io/2020/7/21/1736ededc2bf2ec5?w=794&h=440&f=png&s=100717)

* 基于栈的指令集一致性更好，代码更加紧凑、编译器更加简单，但完成相同功能所需的指令数一般比寄存器架构多，需要频繁的入栈出栈，栈架构指令集的执行速度会相对而言慢一些。

## 2、栈帧

* 栈帧是用于支持虚拟机进行方法调用和方法执行的数据结构。栈帧随着方法调用而创建，随着方法结束而小会，栈帧的存储空间分配在 Java 虚拟机栈中。
* 每个栈帧拥有自己的局部变量表、操作数栈和指向运行时常量池的引用

### a)、局部变量表

* 局部变量表的大小在编译期间就已经确定
* Java虚拟机使用局部变量表来完成方法调用时的参数传递

### b)、操作数栈

* 栈的大小同样是在编译期间确定
* Java 虚拟机提供的一些字节码指令用来从局部变量表或者对象实例的字段中复制常量或者变量到操作数栈，也有一些指令用于从操作数栈取走数据、操作数据和操作结果重新入栈

整个 JVM 指令执行的过程就是局部变量表与操作数栈之间不断load、store的过程

![stack.png](https://user-gold-cdn.xitu.io/2020/7/21/173711213753ad62?w=682&h=240&f=png&s=96494)

### 例子:

```java
public class ScoreCalculator {
    public void record(double score) {
    }

    public double getAverage() {
        return 0;
    }
}
public static void main(String[] args) {
    ScoreCalculator calculator = new ScoreCalculator();

    int score1 = 1;
    int score2 = 2;

    calculator.record(score1);
    calculator.record(score2);

    double avg = calculator.getAverage();
} 
```

字节码如下:

```java
public static void main(java.lang.String[]);
descriptor: ([Ljava/lang/String;)V
flags: ACC_PUBLIC, ACC_STATIC
Code:
  stack=3, locals=6, args_size=1
     0: new           #2                  // class ScoreCalculator
     3: dup
     4: invokespecial #3                  // Method ScoreCalculator."<init>":()V
     7: astore_1
     
     8: iconst_1
     9: istore_2
     
    10: iconst_2
    11: istore_3
    
    12: aload_1
    13: iload_2
    14: i2d
    15: invokevirtual #4                  // Method ScoreCalculator.record:(D)V
    
    18: aload_1
    19: iload_3
    20: i2d
    21: invokevirtual #4                  // Method ScoreCalculator.record:(D)V
    
    24: aload_1
    25: invokevirtual #5                  // Method ScoreCalculator.getAverage:()D
    28: dstore        4
    
    30: return
```

* 0 ~ 7 : 新建了一个 ScoreCalculator 对象，使用 astore_1 存储在局部变量 calculator 中: astore_1的含义是把栈顶的值存储到局部变量表下标为 1 的位置上
* 8 ~ 11 : iconst_1 和 iconst_2 用来将整数 1 和 2 加载到栈顶，istore_2 和 istore_3 用来将栈顶的元素存储到局部变量表 2 和 3的位置上
* 12 ~ 15 : 可以看到 store 指令会把栈顶元素移除，所以下次我们要用到这些局部变量时，需要使用 load 命令重新把它加载到栈顶。比如我们要执行 calculator.record(score1)，对应的字节码如下

```java
12: aload_1
13: iload_2
14: i2d
15: invokevirtual #4 // Method ScoreCalculator.record:(D)V
```

可以看到 aload_1 先从局部变量表中1的位置加载 calculator 对象，iload_2 从局部变量表中2的位置加载一个整型值，i2d 这个指令用来将整型值转为 double 并将新的值重新入栈，到目前位置参数全部就绪，可以用 invokevirutal 执行方法调用了

* 24 ~ 28 : 同样是一个普通的方法调用，流程还是先 aload_1 加载 calculator 对象，invokevirtual 调用 getAverage 方法，并将栈顶元素存储到局部变量表下标为4的位置上

## 3、从二进制看 class 文件和字节码

* 手动用 16 进制编辑器去修改这些字节码文件，比较容易出错，所以产生了一些字节码操作的工具，最出名的莫过于 ASM 和 Javassist

# 四、字节码指令之控制转移 —— for 与 switch

## 1、字节码指令分类

* 加载和存储指令，比如 iload 将一个整型值从局部变量表加载到操作数栈
* 控制转移指令，比如条件分值 ifeq
* 对象操作，比如创建类实例的指令 new
* 方法调用，比如 invokevirtual 指令用于调用对象的实例方法
* 运算指令和类型转换，比如加法指令 iadd
* 线程同步，monitorenter 和 monitorexit 两条指令来支持 synchronized 关键字的语义
* 异常处理，比如 athrow 显示抛出异常

## 2、控制转移指令

## 3、switch 底层实现

# 五、字节码指令之对象初始化——new，\<init> & \<clinit>

## 1、new，\<init> & \<clinit>

## 2、相关面试题分析

## 3、思考题

# 六、字节码指令之方法调用

* invokestatic: 用于调用静态方法
* invokespecial: 用于调用私有实例方法、构造器，以及使用 super 关键字调用父类的实例方法或构造器，和所实现接口的默认方法
* invokevirtual: 用于调用非私有化实例方法
* invokeinterface: 用于调用接口方法
* Invokedynamic: 用于调用动态方法

## 1、方法的静态绑定与动态绑定

## 2、invokestatic

## 3、invokevirtual vs invokespecial

## 4、invokeinterface vs invokevirtual

## 5、思考

# 七、用HSDB来探究多态实现的原理

## 1、HSDB基础

启动 =》 Tools (类列表、堆信息、inspect对象内存、死锁检测)

## 2、利用HSDB来看多态的基础 vtable(??为什么要叫vtable)

vtable 是 Java 实现多态的基石，如果一个方法被继承和重写，会把 vtable 中指向父类的方法指针指向子类自己的实现



# 八、invokedynamic 指令解读

## 1、MethodHandle 是什么

## 2、什么是invokedynamic



# 九、匿名内部类与 lambda

## 1、测试匿名内部类的实现

## 2、测试lambda表达式

## 3、为什么Java8的Lambda表达式要基于invokedynamic



# 十、字节码角度分析面试题 ——i++ 、 ++i 区别

## 1、字节码分析笔试题

## 2、

## 3、题目

# 十一、字节码角度看语法糖 -- String 的 switch实现

## 1、switch String 的 demo

## 2、 hashCode 冲突如何处理

# 十二、try catch finally 为啥 finally 语句一定会执行

## 1、try catch 字节码分析



## 2、finally 字节码分析



## 3、面试题解析



# 十三、字节码角度看 try with resource 语法糖 -- 资源释放不用慌



## 1、suppressed 异常是什么



# 十四、字节码细数 Kotlin 优雅的语法糖



# 十五、深入分析 Kotlin 1.2 处理 when 语法的超级大 bug



# 十六、字节码角度看 synchronized -- 窥探加锁解锁的细节

## 1、代码块级别的 synchronized

## 2、方法级的 synchronized



# 十七、万恶的擦除

## 1、当泛型遇到重载



## 2、泛型的核心概念: 类型擦除(type erasure)



## 3、泛型真的被完全擦除了吗



# 十八、深入理解反射实现的原理



# 十九、Javac 源码调试和原理解析

## 1、Javac 编译原理简介

### a)、 Javac 源码调试



# 二十、 JVM的寄生插件 javaagent 那些事

## 1、Java Instrumentation 包

### a)、 Java Instrumentation 概述

### b)、Java Instrumenttation 核心方法

## 2、Javaagent 介绍

### a)、Agent的两种使用方式

### b)、Agent 包



## 3、Agent 使用方式之一: JVM 启动参数



## 4、 Agent 使用方式之二: Attach API 使用

### a)、JVM Attach API 基本使用

### b)、JVM Attach API 的底层原理

# 二十一、工业级的字节码改写框架 ASM 与 Javassist

## 1、 ASM 

*  ASM 使用了访问者（Visitor）设计模式，避免了创建和消耗大量的中间变量
* 

## 2、Javassist



# 二十二、ASM 在 cglib 与 fastjson 上的应用

## 1、cglib 的简单应用

## 2、fastjson

* 通过 ASM 自动生成序列号、反序列化字节码，减少反射开销，理论上可以提高 20% 的性能

 # 二十三、破解软件-直接修改字节码

## 1、常用工具

##  2、破解方式

# 二十四、无痛破解 Java系软件

## 1、换个花样破解

核心思路是通过 javaagent 和 ASM 来进行字节码改写，在类加载之前修改字节码逻辑

## 2、破解gceasy



# 二十五、APM简介与分布式跟踪的基本原理

## 1、什么是APM

## 2、分布式跟踪理论基础

每个请求都生成全局唯一的 Trace ID／Span ID，端到端透传到上下游所有的节点，通过 Trace ID 将不同系统的孤立的调用日志和异常日志串联在一起，同时通过 Span ID、ParentId 表达节点的父子关系

## 3、单JVM调用链路跟踪实现原理

spanId 做为当前调用id，parentId 做为父调用 id，traceId 做为整条链路 id

## 4、扩进程、异构系统的调用链路跟踪如何处理

# 二十六、一个可落地的APM整体架构

## 1、架构概览

## 2、Agent上报

## 3、数据收集服务

## 4、数据处理端

数据处理可以分为三大部分：流水查询、实时告警、离线报表分析



# 二十七、APM字节码注入的代码实现

## 1、使用更强大AdviceAdapter来生成try-catch-finally 语句块

### a)、onMethodEnter

### b)、onMethodExit

### c)、visitMaxs

### d)、visitTryCatchBlock 与 visitLabel



## 2、获取特定的上下文信息



## 3、跨进程链路调用的实现



## 4、代码仓库

> https://github.com/arthur-zhang/geek01/tree/master/javaagent-demo