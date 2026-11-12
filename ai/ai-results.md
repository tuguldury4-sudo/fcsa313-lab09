# AI-ийн тестүүдийн ажиллуулалт (нэмэлт даалгавар, Сонголт 2)

AI-д Хэсэг A-ийн дараах төлөв (commit `674e6d3`: алдаатай `score > 60` код + 5 happy path тест +
jacoco-A.csv)-ийг өгч "хамралтыг 100% болгох тестүүд бич" гэв. Гарсан 28 тестийг (`AiScholarshipCalculatorTest.java.txt`)
тусдаа хуулбар төсөлд `mvn verify`-ээр ажиллуулав:

| Код | Tests run | Унасан | JaCoCo |
|---|---|---|---|
| Анхны (`score > 60`) | 28 | 1 — `letterGrade_score60_returnsD`: `expected: <D> but was: <F>` | Line 25/25, Branch 41/41 |
| Зассан (`score >= 60`) | 28 | 0 | Line 25/25, Branch 41/41 |
