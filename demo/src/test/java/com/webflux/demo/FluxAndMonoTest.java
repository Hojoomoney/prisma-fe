package com.webflux.demo;

import com.webflux.demo.common.config.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FluxAndMonoTest {
    @Mock
    CustomException customExceptionMono;
    @Mock
    CustomException customExceptionFlux;
    @BeforeEach
    public void setUp(){
        customExceptionMono = new CustomException("Mono");
        customExceptionFlux = new CustomException("Flux");
    }

    @Test
    @DisplayName("Flux just{} sample")
    void justFluxTest(){
        List<String> names = new ArrayList<>();
        Flux<String> flux = Flux.just("김구", "윤봉길", "안중근", "유관순", "이봉창").log();
        flux.subscribe(names::add);
        assertThat(names).isEqualTo(Arrays.asList("김구", "윤봉길", "안중근", "유관순", "이봉창"));
    }

    @Test
    @DisplayName("Mono just{} sample")
    void justMonoTest() {
        // Reactive Stream 에서는 Data, Event, Signal 중에서 Signal 을 사용한다.
        // onNext, onComplete, onError
        List<Signal<Integer>> list = new ArrayList<>(4);
        final Integer[] result = new Integer[1];
        Mono<Integer> mono = Mono.just(1).log()
                .doOnEach(i -> {
                    list.add(i);
                    System.out.println("Signal: " + i);
                });
        mono.subscribe(i -> result[0] = i);
        // 리스트의 사이즈는 2가 나온다. 왜냐하면 Mono 는 onNext, onComplete 두 개의 Signal 을 발생시키기 때문이다.
        assertThat(list.size()).isEqualTo(2);

    }
    @Test
    @DisplayName("Flux create{} sample")
    void createFluxTest(){
        Flux<Integer> flux = Flux.create((FluxSink<Integer> sink) -> {
            sink.onRequest(request ->{ //request 는 구독자가 요청한 데이터의 개수를 의미한다.
                for (int i = 0; i < request + 3; i++){
                sink.next(i);
                }
                sink.complete();
            });
        });
        flux.subscribe(System.out::println);
    }
    @Test
    @DisplayName("Flux generate{} sample")
    void generateFluxTest(){
        Flux<String> flux = Flux.generate(
                () -> 0, // () -> 0 은 초기값을 0으로 설정한다는 의미이다.
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state); // next() 는 데이터를 발행하는 메서드이다.
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.subscribe(System.out::println);
    }
}
