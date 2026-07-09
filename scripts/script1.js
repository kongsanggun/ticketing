import http from 'k6/http';
import exec from 'k6/execution';
import {group, check} from 'k6';
import { SharedArray } from "k6/data";
import { Rate } from 'k6/metrics';

const slow_requests = new Rate('slow_requests');

export let options = {
  scenarios: {
    // 점진적 부하 증가 테스트
    ramping_load: {
      executor: 'ramping-arrival-rate',
      timeUnit: '1s',   // 요청이 만들어지는 기본 단위
      startRate: '0',   // 시작 TPS
      preAllocatedVUs: 20,
      maxVUs: 300,
      stages: [
        // 워밍업 (20TPS): 낮은 TPS에서 시작해서 천천히 올림 - 예열 단계
        {duration: '30s', target: 20},
        // 램프업 (100TPS): 목표 TPS까지 점진적으로 증가시키면서 시스템 확장성 확인
        {duration: '2m30s', target: 100},
        // 소크 (100TPS): 일정 TPS를 유지하면서 시스템 안정성 확인
        {duration: '1m30s',target: 100},
        // 램프다운 (0TPS): 부하를 점진적으로 감소시켜 리소스 정리 과정 확인
        {duration: '1m', target: 0},
      ]
    },
  },
  thresholds: {
    'http_req_duration{endpoint:read}': [
      'p(95)<300',
      'p(99)<600',
    ],

    'http_req_duration{endpoint:write}': [
      'p(95)<500',
      'p(99)<900',
    ],

    http_req_failed: [
      'rate<0.001'
    ],

    slow_requests: [
      'rate<0.002'
    ]
  },
  tags: {
    testId: '${serverName}-${testId}',
    endpoint: 'write'  // 읽기 API 테스트: 'read', 쓰기 API 테스트: 'write'
  }
}

export default function () {
  const request = JSON.stringify({
                        ticketId: Math.floor(Math.random() * 999) + 1,
                        concertId: 1,
                        stageId: Math.floor(Math.random() * 49) + 1,
                        seatClassId: Math.floor(Math.random() * 29) + 1,
                        userId: Math.floor(Math.random() * 999) + 1,
                      });
  const response = http.post("http://localhost:8000/ticket/random",
          request,
          {
              headers: {
                  "Content-Type": "application/json"
              }
         });
  slow_requests.add(response.timings.duration > 1500);
  check(response, {
    "is OK": (r) => r.status === 201 || r.status === 400 || r.status === 404 || r.status === 422,
  });
}