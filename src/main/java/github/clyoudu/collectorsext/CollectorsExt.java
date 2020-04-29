package github.clyoudu.collectorsext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Create by IntelliJ IDEA
 *
 * @author chenlei
 * @dateTime 2019/1/2 16:10
 * @description CollectorsExt
 */
public class CollectorsExt {

    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    public static <T> Collector<T, ?, JSONArray> toJsonArray() {
        return new CollectorImpl<>(JSONArray::new, JSONArray::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    public static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");
        map.put("d", "d");
        System.out.println(map.entrySet().stream().collect(toJsonArray()));
        System.out.println(Stream.of("a","b","c","d").collect(toJsonArray()));
        System.out.println(Stream.of("a","b","c","d").map(s -> new JSONObject().fluentPut(s,s)).collect(toJsonArray()));

        List<JSONObject> list = new ArrayList<>();
        list.add(new JSONObject().fluentPut("name", "a").fluentPut("age", 1));
        list.add(new JSONObject().fluentPut("name", "b").fluentPut("age", 2));
        list.add(new JSONObject().fluentPut("name", "a").fluentPut("age", 3));
        list.add(new JSONObject().fluentPut("name", "c").fluentPut("age", 4));

        list.stream().filter(distinctBy(o -> o.getString("name"))).forEach(System.out::println);
    }

}
