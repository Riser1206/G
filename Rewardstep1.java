package com.example.qwerty;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rewardstep1 {

    public static void main(MainActivity context) {

        // ── 내 계정 정보 ───────────────────────────────────────────
        String playerId = context.playerId.getText().toString();       // 내 고유 계정 ID
        int MAX_OFFLINE_SECONDS = 86400;       // 보상은 최대 24시간치까지만 인정 (그 이상은 버려짐)

        Instant lastRewardTime = null;         // 마지막으로 보상을 수령한 시간 (처음 접속이면 null)
        Instant lastLoginTime = Instant.parse(context.lastLoginTime.getText().toString());  // 마지막 로그인 시간
        Instant now = Instant.parse(context.now.getText().toString());            // 현재 시간

        // ── 고정 시드 만들기 ────────────────────────────────────────
        // # 핵심 원리
        // 랜덤 결과를 뽑을 때, 계정ID + 로그인시간 을 합쳐서 "출발점(시드)"으로 사용.
        // 같은 출발점이면 → 항상 같은 랜덤 결과가 나오기 때문에
        // 게임을 껐다 켜도 보상 목록이 그대로 유지 됨
        String fixedSeed = playerId + lastLoginTime.toEpochMilli();
        long seed = fixedSeed.hashCode(); // 문자열을 숫자로 변환 (랜덤 라이브러리에 숫자를 넣어야 하기 때문)

        // ── 첫 접속 예외 처리 ──────────────────────────────────────
        // 보상을 한 번도 받은 적 없으면(null), 로그인 시간부터 계산 시작
        if (lastRewardTime == null) {
            lastRewardTime = lastLoginTime;
        }

        context.onCreate("마지막 보상 수령 시간: " + lastRewardTime);
        context.onCreate("현재 시간: " + now);

        // ── 오프라인 시간 계산 ─────────────────────────────────────
        // 현재 시간 - 마지막 수령 시간 = 오프라인으로 있었던 시간
        Duration temp = Duration.between(lastRewardTime, now);

        // Math.min() : 24시간(86400초)을 넘어도 최대 86400초로 제한
        // ex) 30시간 접속 안 했어도 → 24시간치 보상만 지급
        int offlineTime = Math.min((int) temp.getSeconds(), MAX_OFFLINE_SECONDS);

        // 10분(600초)마다 보상 1개 지급
        // ex) 1시간 오프라인 → 60분 / 10분 = 보상 6개
        int rewardCount = offlineTime / 600;

        // ── 보상 아이템 데이터 ─────────────────────────────────────
        String[] reward  = {"무기", "방어구", "강화주문서", "스킬북"};  // 보상 종류
        String[] scroll  = {"무기 주문서", "방어구 주문서"};            // 강화주문서 세부 종류
        String[] grade   = {"레전", "유니크", "레어", "노말"};          // 장비 등급
        String[] weapon  = {"엑스칼리버", "기사의 검", "목검"};         // 무기 종류
        String[] armor   = {"전설의 갑옷", "기사의 갑옷", "수련복"};    // 방어구 종류

        List Temppoket = new ArrayList<>(); // 획득한 아이템을 담을 주머니
        Random rng = new Random(seed);              // 고정 시드를 넣은 랜덤 생성기

        // ── 보상 생성 ──────────────────────────────────────────────
        // rewardCount 횟수만큼 아이템을 1개씩 뽑아서 주머니에 넣기
        for (int i = 0; i < rewardCount; i++) {

            int num = rng.nextInt(reward.length); // 보상 종류 랜덤 선택 (무기/방어구/강화주문서/스킬북 중 하나)

            switch (reward[num]) {
                case "무기":
                    // 등급 랜덤 + 무기 이름 랜덤 → 합쳐서 저장
                    // ex) "레어 기사의 검"
                    Temppoket.add(grade[rng.nextInt(grade.length)] + " " + weapon[rng.nextInt(weapon.length)]);
                    break;

                case "방어구":
                    // 등급 랜덤 + 방어구 이름 랜덤 → 합쳐서 저장
                    // ex) "유니크 전설의 갑옷"
                    Temppoket.add(grade[rng.nextInt(grade.length)] + " " + armor[rng.nextInt(armor.length)]);
                    break;

                case "강화주문서":
                    // 무기 주문서 or 방어구 주문서 중 하나 랜덤 선택
                    Temppoket.add(scroll[rng.nextInt(scroll.length)]);
                    break;

                case "스킬북":
                    // 스킬북은 종류 구분 없이 그냥 추가
                    Temppoket.add("스킬북");
                    break;
            }
        }

        // ── 결과 출력 ──────────────────────────────────────────────
        context.onCreate("=== 오프라인 보상 목록 ===");
        for (int i = 0; i < Temppoket.size(); i++) {
            context.onCreate((i + 1) + "번째 획득: " + Temppoket.get(i));
        }
    }
}