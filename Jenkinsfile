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
                    archiveArtifacts "aggregator/target/site/jacoco-aggregate/**"
                }
            }
        }
        stage("Install"){
            steps{
                bat 'mvn install'
            }
        }
        stage("Quality Gate") {
            steps {
                script {
                    def reportPath = 'aggregator/target/site/jacoco-aggregate/index.html'

                    if (!fileExists(reportPath)) {
                        error "Coverage report not found at ${reportPath}"
                    }

                    def file = readFile(reportPath)

                    def regexMatch = file =~ /<tfoot>.*?<td class="ctr2">(\d+)%/

                    if (!regexMatch.find()) {
                        error "Could not find coverage data in HTML report"
                    }

                    def coverage = regexMatch[0][1] as int

                    echo "========================================"
                    echo "JaCoCo Coverage Report"
                    echo "========================================"
                    echo "Total Instruction Coverage: ${coverage}%"
                    echo "Threshold: 60%"
                    echo "========================================"

                    if (coverage < 60) {
                        error "❌ Quality Gate FAILED: Coverage ${coverage}% < 60%"
                    } else {
                        echo "✅ Quality Gate PASSED: Coverage ${coverage}% >= 60%"
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