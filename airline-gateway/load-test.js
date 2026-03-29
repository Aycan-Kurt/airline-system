import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 20,
  duration: '30s',
};

export default function () {
  const res = http.get('http://localhost:8081/api/v1/flights/search?from=ADB&to=IST', {
    timeout: '5s',
  });

  check(res, {
    'search status is 200 or 429': (r) => r.status === 200 || r.status === 429,
  });

  sleep(1);
}