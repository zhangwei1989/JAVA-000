# 第一课学习笔记

## JVM
### 收获
1. GC是内存管理器更加合适
2. 字节码不要求特别精通，大概了解就可以了，只需要知道有几类以及大概是干啥的就行
3. 如果CPU是32位的，处理64位的long，得处理两次，64位的CPU处理一次就好了
4. 查看16进制字节码的方式

### 问题
1. 字节码中，new、dup、invokespecial 中为啥需要有 dup 呢？
2. 栈的操作数栈和字节码放置字节操作码和操作数占用的槽位是一样的吗？
3. 移动平均数的 astore_1 为什么不是0，datore 后面跟的 4 是啥意思？
4. Invokevirtual 和 invokeinterface 有啥区别和必要？
5. BootstrapClassLoader,URLClassLoader,ExtClassLoader,AppClassLoader 这三个类加载器的继承关系到底是什么样的？
答：BootstrapClassLoader、ExtClassLoader、AppClassLoader这三个类加载器不是继承的关系，是操作路径
6. Xmx与Xms为何要设置相等？
7. 这样的自定义类加载器，以及这样加载类有啥意义？自定义加载类还有啥好处？
答：保护重要类的数据内容
    但是如果把自定义类加载器的字节码破解出来，那重要类的数据内容不也就被破解了吗？
8. `java -classpath . HelloClassLoader`为何不行？怎么执行当前文件夹下写有 package 的 class？