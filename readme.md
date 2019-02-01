### Java基础学习记录

​	记录平时遇到的问题，其实之前遇到过很多，但是从现在开始，我要做一个好人。

#### 1.线程池获取线程返回值

​	当向一个线程池中提交了多个任务后，可以获得一个```Future```的集合，如果我们顺序从这个集合中调用```Future.get()```方法，那么久会造成程序一直阻塞在每一个尚未完成的任务，浪费时间。所以我就想到了比顺序遍历稍微好一点的方法。
​	
​	其实就是遍历`Future`集合，先通过`Future.isDown()`方法判断此任务是否已经完成，如果完成了就调用`Future.get()`方法拿到返回值，并从集合中移除此任务，下标保持不动; 如果任务尚未完成，那么下标加一位，查看下一个任务; 当下标到达集合尾部的时候归零，从头再来。
​	
​	可见GitHub上Java se仓库中```TestForFutureAndThreadPool.java```:

```java
// 创建一个线程池
ExecutorService executorService = Executors.newCachedThreadPool();
//创建十个任务，直接submit到线程池中
ArrayList<Future<String>> futures = new ArrayList<>();
for (int i = 10; i > 0; i--) {
    int temp = i;
    Future<String> future = executorService.submit(() -> {
        Thread.sleep(temp * 1000);
        return "c" + temp;
    });
    futures.add(future);
}
// 获取结果，因为get方法是阻塞的，所以先用isDone来判断是不是已经完成
for (int i = 0; i < futures.size(); ) {
    Future<String> future = futures.get(i);
    if (future.isDone()) {
        System.out.println(future.get());
        futures.remove(i);
    } else i++;
    // 从头开始
    if (i == futures.size()) i = 0;
}
```



