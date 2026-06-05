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
                    def jacocoHtmlPath = 'aggregator/target/site/jacoco-aggregate/index.html'

                    if (!fileExists(jacocoHtmlPath)) {
                        error "Coverage report not found: ${jacocoHtmlPath}"
                    }

                    def htmlContent = readFile(jacocoHtmlPath)

                    // В HTML обычно есть строка: <tfoot><tr><td>Total</td><td class="ctr2">...</td>
                    def coveragePattern = /Total[^<]*<td class="ctr2">(\\d+)%/
                    def matcher = (htmlContent =~ coveragePattern)

                    if (!matcher.find()) {
                        error "Cannot find coverage percentage in HTML report"
                    }

                    def coveragePercent = matcher[0][1] as int

                    echo "Code Coverage: ${coveragePercent}%"

                    if (coveragePercent < 60) {
                        error "Code coverage ${coveragePercent}% is below 60% threshold"
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