package org.multiplexing.streams;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @Author: csz
 * @Date: 2018/10/25 19:17
 * 对原始流进行封装
 */
public class StreamForker<T> {
    /**
     *使用流
     */
    private final Stream<T> stream;

    private final boolean isParallel;

    /**
     * 针对流的操作 key-获取操作结果  value(function)-针对流的操作
     */
    private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();

    public StreamForker(Stream<T> stream, boolean isParallel) {
        this.stream = stream;
        this.isParallel = isParallel;
    }

    /**
     * 操作加入
     * @param key
     * @param f
     * @return 返回自身，形成流水线操作
     */
    public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f){
        forks.put(key, f);
        return this;
    }

    /**
     * 获取结果
     * @return
     */
    public Results getResults(){
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer);
        }finally {
            consumer.finish();
        }
        return consumer;
    }

    /**
     * 用于构建ForkingStreamConsumer对象
     * @return
     */
    private ForkingStreamConsumer<T> build(){
        List<BlockingQueue<T>> queues = new ArrayList<>();
        Map<Object, Future<?>> actions = forks.entrySet().stream().reduce(
                new HashMap<Object, Future<?>>(),
                (map, e) -> {
                    map.put(e.getKey(), getOperationResult(queues, e.getValue()));
                    return map;
                },
                (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                });
        return new ForkingStreamConsumer<>(queues, actions);
    }


    private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f){
        LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();
        queues.add(queue);
        Spliterator<T> spliterator = new BlockingQueuesSpliterator<>(queue);
        Stream<T> source = StreamSupport.stream(spliterator, isParallel);
        return CompletableFuture.supplyAsync(() -> f.apply(source));
    }


    /**
     * 结果接口
     */
    public interface Results {

        public <R> R get(Object key);

    }


    /**
     *Consumer对象  queues存放所有流操作对象（BlockingQueuesSpliterator<LinkedBlockingQueue>）
     * actions Stream操作  Function
     * END_OF_STREAM 流操作对象BlockingQueuesSpliterator<LinkedBlockingQueue>的尾对象
     * @param <T>
     */
    static class ForkingStreamConsumer<T> implements Consumer<T>, Results {

        static final Object END_OF_STREAM = new Object();

        private final List<BlockingQueue<T>> queues;

        private final Map<Object, Future<?>> actions;

        public ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
            this.queues = queues;
            this.actions = actions;
        }

        @Override
        public <R> R get(Object key) {
            try {
                return ((Future<R>)actions.get(key)).get();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        @Override
        public void accept(T t) {
            queues.forEach(q -> q.add(t));
        }

        void finish(){
            accept((T) END_OF_STREAM);
        }
    }

    /**
     * 顺序执行，利用LinkedBlockingQueue
     * @param <T> 流元素
     */
    class BlockingQueuesSpliterator<T> implements Spliterator<T>{

        private final BlockingQueue<T> q;

        public BlockingQueuesSpliterator(BlockingQueue<T> q) {
            this.q = q;
        }


        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            T t;
            while (true){
                try {
                    t = q.take();
                    break;
                }catch (InterruptedException e){

                }
            }

            if (t != ForkingStreamConsumer.END_OF_STREAM){
                action.accept(t);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return 0;
        }

        @Override
        public int characteristics() {
            return 0;
        }
    }

}

