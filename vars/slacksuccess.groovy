def success () {
    slackSend color: "good", channel: 'U01SLG5TVQF', message: "SUCCESS! The Job: ${env.JOB_NAME} has been built successfully with Build No# ${env.BUILD_NUMBER} which triggered with change in ${env.BRANCH_NAME} Branch with Job URL: ${env.BUILD_URL}"
}
def failure () {
    slackSend color: "danger", channel: 'U01SLG5TVQF', message: "FAILURE! The Job: ${env.JOB_NAME} has been failed with Build No# ${env.BUILD_NUMBER} which triggered with change in ${env.BRANCH_NAME} Branch with Job URL: ${env.BUILD_URL}" 
}
