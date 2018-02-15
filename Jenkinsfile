#!/usr/bin/env groovy

pipeline {
    agent any

    stages {
        stage('Prepare') {
            steps {
                // Shout about new build
                slackSend color: '#42a1f4', channel: "#jenkins",
                        message: "Build Started - <${env.gitlabSourceRepoHomepage}|${env.JOB_NAME}> (<${env.BUILD_URL}|#${env.BUILD_NUMBER}>) (<${env.gitlabSourceRepoHomepage}/commit/${env.gitlabAfter}|Show commit diff>)"
            }

            post {
                always {
                    updateGitlabCommitStatus name: 'CU Jenkins', state: 'pending'
                }
            }
        }

        stage('Build & test') {
            steps {
                updateGitlabCommitStatus name: 'CU Jenkins', state: 'running'

                sh "./gradlew --no-daemon --rerun-tasks -PBUILD_NUMBER=${env.BUILD_NUMBER} clean build test"
            }
        }
    }

    post {
        success {
            // Archive artifacts
            archiveArtifacts artifacts: 'build/libs/*.jar'

            // Mark build successful on GitLab
            updateGitlabCommitStatus name: 'CU Jenkins', state: 'success'

            // Notify about it on Slack
            slackSend color: '#21ef36', channel: "#jenkins",
                    message: "Build Succeeded - <${env.gitlabSourceRepoHomepage}|${env.JOB_NAME}> (<${env.BUILD_URL}|#${env.BUILD_NUMBER}>) (<${env.gitlabSourceRepoHomepage}/commit/${env.gitlabAfter}|Show commit diff>)"
        }

        failure {
            // Mark build failed on GitLab
            updateGitlabCommitStatus name: 'CU Jenkins', state: 'failed'

            // Notify about it on Slack
            slackSend color: '#ff1d00', channel: "#jenkins",
                    message: "Build Failed - <${env.gitlabSourceRepoHomepage}|${env.JOB_NAME}> (<${env.BUILD_URL}|#${env.BUILD_NUMBER}>) (<${env.gitlabSourceRepoHomepage}/commit/${env.gitlabAfter}|Show commit diff>)"
        }

        aborted {
            // Mark build aborted on GitLab
            updateGitlabCommitStatus name: 'CU Jenkins', state: 'canceled'

            // Notify about it on Slack
            slackSend color: '#ff1d00', channel: "#jenkins",
                    message: "Build Aborted - <${env.gitlabSourceRepoHomepage}|${env.JOB_NAME}> (<${env.BUILD_URL}|#${env.BUILD_NUMBER}>) (<${env.gitlabSourceRepoHomepage}/commit/${env.gitlabAfter}|Show commit diff>)"
        }
    }
}
