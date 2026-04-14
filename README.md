# 🔬 Selenium with Java

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

> [!NOTE]
> The report includes intentional failures. I modified expected values to force mismatches against the web app, so the Allure report shows real failure screenshots, logs, and error traces. This demonstrates how the framework handles failures — not actual bugs in the code.

The pipeline configures headless Chrome with forced Spanish locale and desktop resolution (1920×1080 via CDP), so tests behave identically to local execution. Timeout is increased from 10s to 30s for CI environments.

## 📁 Project Structure

**Why this structure:**

- **`main/` vs `test/` separation** — the framework (Pages, utils, config) lives in `main`. Tests live in `test`. This lets me reuse Pages without coupling them to a specific test, and I could publish the framework as a library if needed.
- **Grouped by feature, not by test type** — tests are organized by feature (`login/`, `clientes/`), not by category (`smoke/`, `regression/`). When a clientes flow breaks, I go to one place. Test type is handled with TestNG groups, not folders.
- **Externalized config** — URL, credentials, browser, and timeouts live in `config.properties`, not hardcoded. Switching environments means editing one file.
- **One Page Object per screen** — each class in `pages/` represents a real screen. If a selector changes, I touch one class. Tests never see a `By.id(...)`.

```text
selenium-java/
├── .github/
│   └── workflows/
│       └── tests.yml              # CI: runs the suite on every push/PR via GitHub Actions
├── .allure/                       # Local Allure config
├── .idea/                         # IntelliJ config (gitignored)
├── allure-results/                # Raw output from each run. Allure reads this to build the report
├── src/
│   ├── main/java/com/cesar/qa/
│   │   ├── base/
│   │   │   └── BasePage.java      # Parent class for all Pages. Centralizes waits, common actions, logging
│   │   ├── config/
│   │   │   ├── ConfigReader.java  # Reads config.properties (URL, credentials, browser, timeouts)
│   │   │   └── DriverManager.java # Creates/manages the WebDriver. ThreadLocal for parallel execution
│   │   ├── pages/
│   │   │   ├── LoginPage.java     # POM: selectors and actions for the login screen
│   │   │   ├── DashboardPage.java # POM: selectors and actions for the post-login dashboard
│   │   │   └── ClientesPage.java  # POM: clientes CRUD (create, search, edit, grid)
│   │   └── utils/
│   │       ├── AllureScreenshot.java  # Attaches screenshots to the Allure report (on failure or key steps)
│   │       ├── check/                 # Reusable validation/assertion helpers
│   │       └── ExcelReader.java       # Reads .xlsx for data-driven testing via Apache POI
│   └── test/
│       ├── java/com/cesar/qa/
│       │   ├── base/
│       │   │   └── BaseTest.java          # Test lifecycle: @BeforeMethod, @AfterMethod, driver init
│       │   ├── data/
│       │   │   ├── TestData.java          # Generic TestNG DataProviders
│       │   │   └── ClientesTestData.java  # DataProviders specific to the Clientes module
│       │   ├── listeners/
│       │   │   └── AllureListener.java    # TestNG hook: captures screenshots on failure, logs steps
│       │   └── tests/
│       │       ├── login/
│       │       │   ├── LoginPositiveTests.java   # Happy-path login cases
│       │       │   └── LoginNegativeTests.java   # Invalid credentials, empty fields, lockouts
│       │       └── clientes/
│       │           └── ClientesTests.java        # Clientes module flows
│       └── resources/
│           ├── testdata/
│           │   └── clientes-data.xlsx     # External data for parameterized tests
│           ├── allure.properties          # Report config (results dir, etc.)
│           ├── config.properties          # URL, credentials, browser, timeouts
│           ├── logback.xml                # Logging config (levels, format, appenders)
│           └── testng.xml                 # Suite: which tests to run, order, parallel or not
└── pom.xml                        # Maven dependencies (Selenium, TestNG, Allure, WebDriverManager, POI)
```

---

## Folder responsibilities

**`src/main/java`** → framework code (what does the testing).
**`src/test/java`** → the tests themselves (what gets executed).

Standard Maven split: the framework lives in `main`, tests in `test`. This way I can reuse Pages and utils without tying them to a specific test.

### `base/` (main and test)
Parent classes. Avoid code duplication:

- `BasePage` centralizes methods common to any Page (safe click, waits, type with prior clear).
- `BaseTest` centralizes the test lifecycle (open driver before, close after).

### `config/`
Everything that changes between environments (local, staging, CI) lives here.

- `ConfigReader` reads `config.properties`. If the URL changes tomorrow, I edit one file, not 20 tests.
- `DriverManager` manages the `WebDriver` instance. Uses `ThreadLocal` with parallel TestNG execution in mind.

### `pages/`
Pure Page Object Model. One class per screen:

- Private selectors inside (not exposed to tests).
- Public methods that represent user actions (`loginAs(user, pass)`, `searchClient(name)`).
- Tests never see a `By.id(...)`. If a selector changes, I touch one class.

### `utils/`
Cross-cutting tools:

- `AllureScreenshot` → screenshots attached to the report.
- `ExcelReader` → `.xlsx` reading with Apache POI for data-driven tests.
- `check/` → reusable validation helpers, to avoid repeating verbose asserts.

### `test/data/`
TestNG DataProviders. I separate generic ones (`TestData`) from module-specific ones (`ClientesTestData`). Each test pulls only what it needs.

### `test/listeners/`
`AllureListener` hooks into TestNG events (failure, success, skip) and connects them to the report: screenshot on failure, step logging, context attachments.

### `test/tests/`
Tests organized by feature module (`login/`, `clientes/`), not by test type. Easier to maintain when grouped by business feature.

### `resources/`
Config and external data:

- `config.properties` → which environment, which browser, which timeouts.
- `testng.xml` → the suite. Defines what runs, in what order, with which listeners, parallel or not.
- `logback.xml` → how the framework logs.
- `testdata/` → data files (`.xlsx`) for parameterized tests.

### `.github/workflows/tests.yml`
GitHub Actions pipeline. Every push runs the suite and publishes the Allure report to GitHub Pages. CI without a self-hosted server, free.

### `allure-results/`
Raw output from each run. Not committed (gitignored). Allure consumes it to generate the HTML report.

### `pom.xml`
Maven dependencies and plugins. Project's backbone: Selenium, TestNG, WebDriverManager, Allure, and Apache POI versions live here.

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
These are intentional next steps to strengthen data integrity validations.

- [ ] [#1 Grid validation: detect extra/missing rows (web vs Excel)](https://github.com/cesarbeassuarez/qa-automation-lab/issues/1)
- [ ] [#2 Grid validation: validate row order (or define order-insensitive strategy)](https://github.com/cesarbeassuarez/qa-automation-lab/issues/2)

## 📝 Related content

- Blog: [cesarbeassuarez.dev](https://cesarbeassuarez.dev/)
- Live report: [cesarbeassuarez.github.io/qa-automation-lab](https://cesarbeassuarez.github.io/qa-automation-lab/)
- LinkedIn: [linkedin.com/in/cesbs](https://www.linkedin.com/in/cesbs/)
