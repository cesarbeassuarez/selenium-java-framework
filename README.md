# 🔬 QA Automation Lab

**A Selenium WebDriver + Java + TestNG automation framework, built from scratch with every architectural decision and problem documented publicly.**

This is not a course project. It's a working framework where every architectural decision, every problem, and every solution is documented publicly. Built by a QA engineer with 4+ years of experience testing enterprise ERP systems.

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Automation | Selenium WebDriver |
| Test Framework | TestNG |
| Reporting | Allure Reports |
| Data Source | Excel (Apache POI) |
| Build | Maven |
| CI/CD | GitHub Actions |
| Report Hosting | GitHub Pages |

## ⚙️ CI/CD Pipeline

Every push to `main` triggers an automated pipeline:

```
Push to main
    ↓
GitHub Actions (Ubuntu + Java 17 + Chrome headless)
    ↓
mvn clean test (96 tests)
    ↓
Allure Report generated
    ↓
Published to GitHub Pages
```

**Live report:** [cesarbeassuarez.github.io/qa-automation-lab](https://cesarbeassuarez.github.io/qa-automation-lab/)

The pipeline configures headless Chrome with forced Spanish locale and desktop resolution (1920×1080 via CDP), so tests behave identically to local execution. Timeout is increased from 10s to 30s for CI environments.

## 📂 Project Structure

```
selenium-java/
├── .github/
│   └── workflows/
│       └── tests.yml
├── .allure/
├── .idea/
├── allure-results/
├── src/
│   ├── main/java/com/cesar/qa/
│   │   ├── base/
│   │   │   └── BasePage.java
│   │   ├── config/
│   │   │   ├── ConfigReader.java
│   │   │   └── DriverManager.java
│   │   ├── pages/
│   │   │   ├── ClientesPage.java      
│   │   │   ├── DashboardPage.java   
│   │   │   └── LoginPage.java       
│   │   └── utils/
│   │       ├── AllureScreenshot.java 
│   │       ├── check/
│   │       └── ExcelReader.java
│   └── test/
│       ├── java/com/cesar/qa/
│       │   ├── base/
│       │   │   └── BaseTest.java
│       │   ├── data/
│       │   │   ├── ClientesTestData.java
│       │   │   └── TestData.java
│       │   ├── listeners/
│       │   │   └── AllureListener.java 
│       │   └── tests/
│       │       ├── clientes/
│       │       │   └── ClientesTests.java   
│       │       └── login/
│       │           ├── LoginNegativeTests.java 
│       │           └── LoginPositiveTests.java 
│       └── resources/
│           ├── testdata/
│           │   └── clientes-data.xlsx
│           ├── allure.properties       
│           ├── config.properties
│           ├── logback.xml
│           └── testng.xml              
└── pom.xml 
│
├── playwright/             # 🔜 Next — planned
└── cypress/                # 🔜 Future — planned
```

## 🧭 Build Log

Each entry represents a real development iteration. Full context on decisions and tradeoffs documented on [my blog](https://cesarbeassuarez.dev/).

| # | Focus | Date |
|---|---|---|
| 1 | [Selenium + Java desde cero](https://cesarbeassuarez.dev/selenium-java-testng-setup-intellij/) — Why Selenium over Cypress, Java over Python, TestNG over JUnit. First test executed. | 30 Dec 2025 |
| 2 | [Por qué borré todo y volví al día 1](https://cesarbeassuarez.dev/reiniciar-framework-automation-desde-cero/) — Had a full framework built with AI. Didn't understand it. Deleted everything and started from scratch. | 08 Jan 2026 |
| 3 | [pom.xml, Logback, estructura de carpetas](https://cesarbeassuarez.dev/maven-logback-estructura-proyecto-selenium/) — Maven config, logging with Logback, base/pages/utils/test folder structure. | 09 Jan 2026 |
| 4 | [Dejé de hardcodear: config.properties y DriverManager](https://cesarbeassuarez.dev/config-properties-drivermanager-selenium-java/) — Eliminated hardcoded values. Centralized driver and config management. | 13 Jan 2026 |
| 5 | [Localizadores en Selenium](https://cesarbeassuarez.dev/localizadores-selenium-xpath-css-id/) — DOM, selectors, locator hierarchy from id to XPath. Real login test. | 16 Jan 2026 |
| 6 | [Esperas Implícitas, Explícitas y Fluent Waits](https://cesarbeassuarez.dev/esperas-selenium-implicit-explicit-fluentwait/) — Why Thread.sleep kills tests. implicitWait vs WebDriverWait vs FluentWait. | 21 Jan 2026 |
| 7 | [Page Object Model: separar Pages de Tests](https://cesarbeassuarez.dev/page-object-model-selenium-java/) — LoginPage, DashboardPage anatomy. Pages interact with UI, Tests decide pass/fail. | 30 Jan 2026 |
| 8 | [Framework TestNG](https://cesarbeassuarez.dev/testng-framework-selenium-java-migracion/) — Migrated from manual main() to @Test, @BeforeMethod, BaseTest, testng.xml. | 18 Feb 2026 |
| 9 | [DataProviders y assertions reales](https://cesarbeassuarez.dev/dataprovider-testng-assertions-selenium/) — Replaced check.java with TestNG Assert. DataProviders in separate class. 5 tests, clean separation. | 20 Feb 2026 |
| 10 | [Validar grilla de clientes contra Excel](https://cesarbeassuarez.dev/validar-datos-web-contra-excel-selenium-poi/) — SlickGrid, virtual scrolling, Apache POI. 91 records validated in 1 min. | 25 Feb 2026 |
| 11 | [Allure Reports — reporting profesional](https://cesarbeassuarez.dev/allure-reports-selenium-java-testng/) — @Step, @Description, @Severity, auto screenshots on failure. Full implementation. | 26 Feb 2026 |
| 12 | [CI/CD con GitHub Actions](https://cesarbeassuarez.dev/github-actions-selenium-allure-ci-cd-pipeline/) — Headless Chrome, Allure report generation, GitHub Pages deployment. +15 commits fixing real CI problems. | 02 Mar 2026 |

## 🎯 What makes this different

- **Not a tutorial project.** Every decision reflects real testing experience on enterprise systems.
- **Documented tradeoffs.** I explain *why*, not just *how*.
- **Built in public.** Progress, mistakes, and iterations — all visible.
- **CI/CD integrated.** Tests run automatically, reports are public.

## Roadmap / Known gaps

This lab is intentionally documented in public and still evolving. Current known gaps:<br>
These are intentional next steps to strengthen data integrity validations (not performance-related).

- [ ] [#1 Grid validation: detect extra/missing rows (web vs Excel)](https://github.com/cesarbeassuarez/qa-automation-lab/issues/1)
- [ ] [#2 Grid validation: validate row order (or define order-insensitive strategy)](https://github.com/cesarbeassuarez/qa-automation-lab/issues/2)

## 📝 Related content

- Blog: [cesarbeassuarez.dev](https://cesarbeassuarez.dev/)
- Live report: [cesarbeassuarez.github.io/qa-automation-lab](https://cesarbeassuarez.github.io/qa-automation-lab/)
- LinkedIn: [linkedin.com/in/cesbs](https://www.linkedin.com/in/cesbs/)
