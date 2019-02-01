package humin;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Auther: Hu Min
 * @Date: 2019/2/1 13:43
 * @Description: 测试线程池 实时获取返回值
 */
public class TestForFutureAndThreadPool {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testForFutureAndThreadPool();
    }

    public static void testForFutureAndThreadPool() throws InterruptedException, ExecutionException {
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
    }

}
