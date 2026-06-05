package com.solvemeup.smucoreapi.domain.user.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 가입 시 부여할 기본 닉네임을 생성한다.
 *
 * <p>{@code 형용사 + 동물 + 4자리 수} 형태로, 사용자가 직접 변경하기 전까지
 * 읽기 쉬운 임시 닉네임을 제공한다. (예: {@code GreedyPanda4821})
 *
 * <p>생성 공간은 약 {@code 형용사 수 × 동물 수 × 9000} ≈ 3.2×10^7 이다.
 * 중복은 닉네임 유니크 제약이 최종적으로 차단하며, 희귀한 충돌은
 * 재로그인으로 자연 복구된다. (대량 생성이 필요한 시드는 별도로 유일성을 보장)
 */
@Component
public class NicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "Greedy", "Recursive", "Binary", "Dynamic", "Atomic", "Static",
            "Linear", "Hashed", "Sorted", "Nested", "Cached", "Parallel",
            "Generic", "Optimal", "Modular", "Stable", "Compact", "Logical",
            "Prime", "Scoped", "Sealed", "Virtual", "Abstract", "Boolean",
            "Mutable", "Sparse", "Lazy", "Pure", "Eager", "Typed",
            "Brave", "Calm", "Bold", "Keen", "Agile", "Quick",
            "Smart", "Sharp", "Vivid", "Solid", "Clean", "Fluent",
            "Robust", "Elegant", "Quiet", "Lucid", "Crisp", "Noble",
            "Loyal", "Steady", "Mighty", "Cosmic", "Quantum", "Clever",
            "Bright", "Witty", "Rapid", "Swift", "Dense", "Cyber"
    );

    private static final List<String> ANIMALS = List.of(
            "Panda", "Otter", "Fox", "Eagle", "Wolf", "Tiger",
            "Koala", "Lynx", "Heron", "Raven", "Viper", "Falcon",
            "Bison", "Crane", "Gecko", "Hawk", "Ibex", "Lemur",
            "Moose", "Orca", "Quail", "Seal", "Toad", "Zebra",
            "Bear", "Finch", "Dove", "Robin", "Shark", "Swan",
            "Wren", "Cobra", "Dingo", "Egret", "Ferret", "Gibbon",
            "Jaguar", "Llama", "Marmot", "Ocelot", "Possum", "Rabbit",
            "Salmon", "Turtle", "Walrus", "Badger", "Beaver", "Bobcat",
            "Coyote", "Marten", "Weasel", "Wombat", "Puma", "Hare",
            "Mole", "Newt", "Goat", "Crab", "Owl", "Yak"
    );

    private static final int NUMBER_ORIGIN = 1000;
    private static final int NUMBER_BOUND = 10000;

    public String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String animal = ANIMALS.get(random.nextInt(ANIMALS.size()));
        int number = random.nextInt(NUMBER_ORIGIN, NUMBER_BOUND);

        return adjective + animal + number;
    }
}
