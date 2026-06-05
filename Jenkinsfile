pipeline {
    agent any
    tools {
        maven 'maven-3.9.16'
    }
    environment {
        JACOCO_CSV_PATH = 'aggregator/target/site/jacoco-aggregate/jacoco.csv'
        THRESHOLD_COVERAGE = '60'
        ARTIFACT_DIR = 'D:\\Practice\\archive'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo '✅ Code successfully checked out from SCM'
            }
        }
        stage('Compile') {
            steps {
                bat 'mvn clean compile'
                echo '✅ Compilation completed successfully'
            }
        }
        stage('Checkstyle (Develop Branch)') {
            when {
                expression { env.GIT_BRANCH =~ 'origin/develop' }
            }
            steps {
                bat 'mvn checkstyle:check'
                echo '✅ Code style validation passed'
            }
        }
        stage('Run Tests (Feature Branch)') {
            when {
                expression { env.GIT_BRANCH =~ '/(feature)/' }
            }
            steps {
                bat 'mvn test'
                echo '✅ All tests executed successfully'
            }
        }
        stage('Test Coverage Analysis') {
            steps {
                bat 'mvn verify'
                echo 'Test coverage report generated'
            }
            post {
                always {
                    archiveArtifacts artifacts: "aggregator/target/site/jacoco-aggregate/**",
                                     fingerprint: true
                    echo 'Coverage artifacts archived'
                }
            }
        }
        stage('Install to Local Repository') {
            steps {
                bat 'mvn install -DskipTests=true'
                echo '✅ Artifacts installed to local Maven repository'
            }
        }
        stage("Quality Gate") {
            steps {
                script {
                    def coverage = calculateCoverage()
                    def threshold = THRESHOLD_COVERAGE.toInteger()

                    echo """
                    ╔══════════════════════════════════════════════════╗
                    ║              📊 COVERAGE REPORT 📊               ║
                    ╠══════════════════════════════════════════════════╣
                    ║  Coverage: ${coverage}%                          ║
                    ║  Threshold: ${threshold}%                        ║
                    ╚══════════════════════════════════════════════════╝
                    """

                    if (coverage < threshold) {
                        error """
                    ╔══════════════════════════════════════════════════════════╗
                    ║                    ❌ QUALITY GATE FAILED ❌             ║
                    ╠══════════════════════════════════════════════════════════╣
                    ║  Coverage ${coverage}% is below threshold ${threshold}%  ║
                    ║  Please add more tests to improve coverage!              ║
                    ╚══════════════════════════════════════════════════════════╝
                        """
                    } else {
                        echo """
                    ╔══════════════════════════════════════════════════════════╗
                    ║                     ✅ QUALITY GATE PASSED ✅            ║
                    ╠══════════════════════════════════════════════════════════╣
                    ║  Coverage ${coverage}% meets the threshold ${threshold}% ║
                    ║  Great job maintaining code quality!                     ║
                    ╚══════════════════════════════════════════════════════════╝
                        """
                    }
                }
            }
        }
        stage("Assembly & Archive"){
            steps{
                echo "Saving jar in Artifacts and External Directory"
                bat "copy aggregator\\target\\*.jar D:\\Practice\\archive"
                echo '✅ Artifacts saved to external directory'
            }
            post {
                always{
                    archiveArtifacts "aggregator/target/*.jar"
                }
            }
        }
    }
}

def calculateCoverage() {
    def csvFile = env.JACOCO_CSV_PATH

    if (!fileExists(csvFile)) {
        error "❌ JaCoCo CSV report not found at: ${csvFile}"
    }

    def csvContent = readFile(csvFile)
    def lines = csvContent.split('\n')

    def totalCovered = 0
    def totalMissed = 0

    // Пропускаем заголовок
    for (int i = 1; i < lines.size(); i++) {
        def line = lines[i].trim()
        if (line.length() > 0) {
            def columns = line.split(',')
            if (columns.size() >= 5) {
                totalMissed += columns[3].toInteger()
                totalCovered += columns[4].toInteger()
            }
        }
    }

    def total = totalCovered + totalMissed
    def coverage = total > 0 ? (totalCovered * 100 / total) : 0

    echo """
    Coverage Statistics:
    - Instructions Covered: ${totalCovered}
    - Instructions Missed: ${totalMissed}
    - Total Instructions: ${total}
    - Coverage Percentage: ${coverage}%
    """

    return coverage
}