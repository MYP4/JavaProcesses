pipeline {
    agent any
    tools {
        maven 'maven-3.9.16'
    }
    stages {
        stage('Checkout'){
            steps {
                checkout scm
            }
        }
        stage('Compile'){
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Test Feature') {
            when { expression {env.GIT_BRANCH =~ '/(feature)/'}}
            steps{
                bat 'mvn test'
            }
        }
        stage("Checkstyle Develop") {
            when { expression {env.GIT_BRANCH =~ '/(develop)/'}}
            steps{
                bat 'mvn checkstyle:check'
            }
        }
        stage("Test Coverage") {
            steps{
                bat 'mvn verify'
            }
            post {
                always {
                    archiveArtifacts "aggregator/target/site/**"
                }
            }
        }
        stage("Install"){
            steps{
                bat 'mvn install'
            }
        }
        stage("Quality Gate"){
            steps{
                script{
                    def jacocoReportPath = 'aggregator/target/site/jacoco-aggregate/jacoco.xml'
                    if (!fileExists(jacocoReportPath)) {
                        echo "Coverage report not found: ${jacocoReportPath}. Skipping coverage check."
                        return
                    }

                    def coverageFile = readFile(jacocoReportPath)
                    def missed = coverageFile =~ /<counter type="LINE" missed="(\\d+)"/
                    def covered = coverageFile =~ /covered="(\\d+)"/

                    if (!missed || !covered) {
                        error "Cannot read coverage data"
                    }

                    def missedLines = missed[0][1] as int
                    def coveredLines = covered[0][1] as int
                    def total = missedLines + coveredLines
                    def percent = (coveredLines * 100) / total

                    echo "Coverage: ${percent}%"
                    if (percent < 60) {
                        error "Code coverage ${percent}% is below threshold"
                    }
                }
            }
        }
        stage("Assembly"){
            steps{
                echo "Saving jar in Artifacts and External Directory"
                bat "copy aggregator\\target\\*.jar D:\\jar\\"
            }
            post {
                always{
                    archiveArtifacts "aggregator/target/*.jar"
                }
            }
        }
    }
}