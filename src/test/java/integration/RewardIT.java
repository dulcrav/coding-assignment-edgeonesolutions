package integration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.stream.Stream;

class RewardIT extends BaseIT {

    @ParameterizedTest
    @MethodSource("arguments")
    @SqlGroup({
            @Sql(value = "classpath:data/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:data/rewards.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void willCalculatePoints(String userId, String totalPoints, String firstMonth, int pointsForFirstMonth,
                             String secondMonth, int pointsForSecondMonth, String thirdMonth, int pointsForThirdMonth) {
        // GIVEN

        webTestClient.get()
                .uri("/api/v1/reward/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalPoints").isEqualTo(totalPoints)
                .jsonPath("$.rewardsByMonth[0].month").isEqualTo(firstMonth)
                .jsonPath("$.rewardsByMonth[0].points").isEqualTo(pointsForFirstMonth)
                .jsonPath("$.rewardsByMonth[1].month").isEqualTo(secondMonth)
                .jsonPath("$.rewardsByMonth[1].points").isEqualTo(pointsForSecondMonth)
                .jsonPath("$.rewardsByMonth[2].month").isEqualTo(thirdMonth)
                .jsonPath("$.rewardsByMonth[2].points").isEqualTo(pointsForThirdMonth);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("1", "316", "NOVEMBER", 0, "DECEMBER", 0, "JANUARY", 316),
                Arguments.of("2", "1145", "JANUARY", 754, "FEBRUARY", 109, "MARCH", 282),
                Arguments.of("3", "0", "FEBRUARY", 0, "MARCH", 0, "APRIL", 0)
                );
    }
}
