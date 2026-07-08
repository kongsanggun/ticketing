import http from 'k6/http';
import exec from 'k6/execution';
import {group, check} from 'k6';
import { SharedArray } from "k6/data";
import { Rate } from 'k6/metrics';

const slow_requests = new Rate('slow_requests');

export let options = {
  scenarios: {
    // 스파이크 테스트
    ramping_load: {
      executor: 'ramping-arrival-rate',
      timeUnit: '1s',   // 요청이 만들어지는 기본 단위
      startRate: '0',   // 시작 TPS
      preAllocatedVUs: 60,
      maxVUs: 1500,
      stages: [
        { duration: '60s', target: 10 },   // 안정 구간
        { duration: '10s', target: 1000 },  // 급상승(스파이크)
        { duration: '20s', target: 10 },   // 급락
        { duration: '10s', target: 1000 },  // 2차 스파이크
        { duration: '50s', target: 50 },   // 상향 안정화
        { duration: '30s', target: 0 },    // 램프다운
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
                        ticketId: Math.floor(Math.random() * 1000),
                        concertId: 1,
                        stageId: Math.floor(Math.random() * 50),
                        seatClassId: Math.floor(Math.random() * 30),
                        userId: Math.floor(Math.random() * 1000),
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