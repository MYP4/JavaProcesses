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
        stage("Checkstyle Develop") {
            when { expression {env.GIT_BRANCH =~ '/(develop)/'}}
            steps{
                bat 'mvn checkstyle:check'
            }
        }
        stage('Test Feature') {
            when { expression {env.GIT_BRANCH =~ '/(feature)/'}}
            steps{
                bat 'mvn test'
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
                    def csvFile = 'aggregator/target/site/jacoco-aggregate/jacoco.csv'

                    if (!fileExists(csvFile)) {
                        error "JaCoCo CSV report not found at: ${csvFile}"
                    }

                    def csvContent = readFile(csvFile)
                    def lines = csvContent.split('\n')

                    // Суммируем покрытие инструкций по всем строкам
                    def totalCovered = 0
                    def totalMissed = 0

                    lines[1..-1].each { line ->
                        if (line.trim().length() > 0) {
                            def columns = line.split(',')
                            if (columns.size() >= 5) {
                                totalMissed += columns[3].toInteger()      // INSTRUCTION_MISSED
                                totalCovered += columns[4].toInteger()     // INSTRUCTION_COVERED
                            }
                        }
                    }

                    def total = totalCovered + totalMissed
                    def coverage = total > 0 ? (totalCovered * 100 / total) : 0

                    echo "Total Instructions Coverage: ${coverage}% (${totalCovered}/${total})"

                    def threshold = 60
                    if (coverage < threshold) {
                        error "Quality Gate Failed: Instruction coverage ${coverage}% < ${threshold}%"
                    } else {
                        echo "✅ Quality Gate Passed: ${coverage}% >= ${threshold}%"
                    }
                }
            }
        }
        stage("Assembly"){
            steps{
                echo "Saving jar in Artifacts and External Directory"
                bat "copy aggregator\\target\\*.jar D:\\Practice\\archive"
            }
            post {
                always{
                    archiveArtifacts "aggregator/target/*.jar"
                }
            }
        }
    }
}