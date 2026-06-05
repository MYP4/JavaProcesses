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
                    sh 'mvn jacoco:report'

                    def reportPath = 'aggregator/target/site/jacoco-aggregate/index.html'

                    if (!fileExists(reportPath)) {
                        error "Coverage report not found at ${reportPath}"
                    }

                    def html = readFile(reportPath)

                    def lineCoveragePattern = /<td>Line<\/td>[\s\S]*?<td class="ctr2">(\d+)%/
                    def matcher = (html =~ lineCoveragePattern)

                    if (!matcher.find()) {
                        def altPattern = /ctr2[^>]*>(\d+)%/
                        matcher = (html =~ altPattern)
                        if (!matcher.find()) {
                            error "Cannot parse coverage from HTML"
                        }
                    }

                    def coveragePercent = matcher[0][1] as int

                    def classCoveragePattern = /Total[^<]*<td class="ctr2">(\d+)%/
                    matcher = (html =~ classCoveragePattern)
                    def classCoverage = matcher.find() ? matcher[0][1] as int : coveragePercent

                    echo """
                    ====================================
                    Quality Gate Results:
                    - Line Coverage: ${coveragePercent}%
                    - Class Coverage: ${classCoverage}%
                    Threshold: 60%
                    ====================================
                    """

                    if (coveragePercent < 60) {
                        error "❌ Quality Gate FAILED: Coverage ${coveragePercent}% < 60%"
                    } else {
                        echo "✅ Quality Gate PASSED: Coverage ${coveragePercent}% >= 60%"
                    }

                    writeFile file: 'coverage-result.txt', text: "Coverage: ${coveragePercent}%"
                    archiveArtifacts 'coverage-result.txt'
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